package py.com.abf.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.IntegrationTest;
import py.com.abf.domain.Cursos;
import py.com.abf.domain.RegistroClases;
import py.com.abf.repository.CursosRepository;
import py.com.abf.service.criteria.CursosCriteria;

/**
 * Integration tests for the {@link CursosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CursosResourceIT {

    private static final String DEFAULT_NOMBRE_CURSO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CURSO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cursos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CursosRepository cursosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCursosMockMvc;

    private Cursos cursos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cursos createEntity(EntityManager em) {
        Cursos cursos = new Cursos().nombreCurso(DEFAULT_NOMBRE_CURSO);
        return cursos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cursos createUpdatedEntity(EntityManager em) {
        Cursos cursos = new Cursos().nombreCurso(UPDATED_NOMBRE_CURSO);
        return cursos;
    }

    @BeforeEach
    public void initTest() {
        cursos = createEntity(em);
    }

    @Test
    @Transactional
    void createCursos() throws Exception {
        int databaseSizeBeforeCreate = cursosRepository.findAll().size();
        // Create the Cursos
        restCursosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cursos)))
            .andExpect(status().isCreated());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeCreate + 1);
        Cursos testCursos = cursosList.get(cursosList.size() - 1);
        assertThat(testCursos.getNombreCurso()).isEqualTo(DEFAULT_NOMBRE_CURSO);
    }

    @Test
    @Transactional
    void createCursosWithExistingId() throws Exception {
        // Create the Cursos with an existing ID
        cursos.setId(1L);

        int databaseSizeBeforeCreate = cursosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCursosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cursos)))
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCursos() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList
        restCursosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cursos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreCurso").value(hasItem(DEFAULT_NOMBRE_CURSO)));
    }

    @Test
    @Transactional
    void getCursos() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get the cursos
        restCursosMockMvc
            .perform(get(ENTITY_API_URL_ID, cursos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cursos.getId().intValue()))
            .andExpect(jsonPath("$.nombreCurso").value(DEFAULT_NOMBRE_CURSO));
    }

    @Test
    @Transactional
    void getCursosByIdFiltering() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        Long id = cursos.getId();

        defaultCursosShouldBeFound("id.equals=" + id);
        defaultCursosShouldNotBeFound("id.notEquals=" + id);

        defaultCursosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCursosShouldNotBeFound("id.greaterThan=" + id);

        defaultCursosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCursosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCursosByNombreCursoIsEqualToSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where nombreCurso equals to DEFAULT_NOMBRE_CURSO
        defaultCursosShouldBeFound("nombreCurso.equals=" + DEFAULT_NOMBRE_CURSO);

        // Get all the cursosList where nombreCurso equals to UPDATED_NOMBRE_CURSO
        defaultCursosShouldNotBeFound("nombreCurso.equals=" + UPDATED_NOMBRE_CURSO);
    }

    @Test
    @Transactional
    void getAllCursosByNombreCursoIsInShouldWork() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where nombreCurso in DEFAULT_NOMBRE_CURSO or UPDATED_NOMBRE_CURSO
        defaultCursosShouldBeFound("nombreCurso.in=" + DEFAULT_NOMBRE_CURSO + "," + UPDATED_NOMBRE_CURSO);

        // Get all the cursosList where nombreCurso equals to UPDATED_NOMBRE_CURSO
        defaultCursosShouldNotBeFound("nombreCurso.in=" + UPDATED_NOMBRE_CURSO);
    }

    @Test
    @Transactional
    void getAllCursosByNombreCursoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where nombreCurso is not null
        defaultCursosShouldBeFound("nombreCurso.specified=true");

        // Get all the cursosList where nombreCurso is null
        defaultCursosShouldNotBeFound("nombreCurso.specified=false");
    }

    @Test
    @Transactional
    void getAllCursosByNombreCursoContainsSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where nombreCurso contains DEFAULT_NOMBRE_CURSO
        defaultCursosShouldBeFound("nombreCurso.contains=" + DEFAULT_NOMBRE_CURSO);

        // Get all the cursosList where nombreCurso contains UPDATED_NOMBRE_CURSO
        defaultCursosShouldNotBeFound("nombreCurso.contains=" + UPDATED_NOMBRE_CURSO);
    }

    @Test
    @Transactional
    void getAllCursosByNombreCursoNotContainsSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where nombreCurso does not contain DEFAULT_NOMBRE_CURSO
        defaultCursosShouldNotBeFound("nombreCurso.doesNotContain=" + DEFAULT_NOMBRE_CURSO);

        // Get all the cursosList where nombreCurso does not contain UPDATED_NOMBRE_CURSO
        defaultCursosShouldBeFound("nombreCurso.doesNotContain=" + UPDATED_NOMBRE_CURSO);
    }

    @Test
    @Transactional
    void getAllCursosByRegistroClasesIsEqualToSomething() throws Exception {
        RegistroClases registroClases;
        if (TestUtil.findAll(em, RegistroClases.class).isEmpty()) {
            cursosRepository.saveAndFlush(cursos);
            registroClases = RegistroClasesResourceIT.createEntity(em);
        } else {
            registroClases = TestUtil.findAll(em, RegistroClases.class).get(0);
        }
        em.persist(registroClases);
        em.flush();
        cursos.addRegistroClases(registroClases);
        cursosRepository.saveAndFlush(cursos);
        Long registroClasesId = registroClases.getId();

        // Get all the cursosList where registroClases equals to registroClasesId
        defaultCursosShouldBeFound("registroClasesId.equals=" + registroClasesId);

        // Get all the cursosList where registroClases equals to (registroClasesId + 1)
        defaultCursosShouldNotBeFound("registroClasesId.equals=" + (registroClasesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCursosShouldBeFound(String filter) throws Exception {
        restCursosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cursos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreCurso").value(hasItem(DEFAULT_NOMBRE_CURSO)));

        // Check, that the count call also returns 1
        restCursosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCursosShouldNotBeFound(String filter) throws Exception {
        restCursosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCursosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCursos() throws Exception {
        // Get the cursos
        restCursosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCursos() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();

        // Update the cursos
        Cursos updatedCursos = cursosRepository.findById(cursos.getId()).get();
        // Disconnect from session so that the updates on updatedCursos are not directly saved in db
        em.detach(updatedCursos);
        updatedCursos.nombreCurso(UPDATED_NOMBRE_CURSO);

        restCursosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCursos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCursos))
            )
            .andExpect(status().isOk());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
        Cursos testCursos = cursosList.get(cursosList.size() - 1);
        assertThat(testCursos.getNombreCurso()).isEqualTo(UPDATED_NOMBRE_CURSO);
    }

    @Test
    @Transactional
    void putNonExistingCursos() throws Exception {
        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();
        cursos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cursos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cursos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCursos() throws Exception {
        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();
        cursos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cursos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCursos() throws Exception {
        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();
        cursos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cursos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCursosWithPatch() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();

        // Update the cursos using partial update
        Cursos partialUpdatedCursos = new Cursos();
        partialUpdatedCursos.setId(cursos.getId());

        restCursosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCursos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCursos))
            )
            .andExpect(status().isOk());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
        Cursos testCursos = cursosList.get(cursosList.size() - 1);
        assertThat(testCursos.getNombreCurso()).isEqualTo(DEFAULT_NOMBRE_CURSO);
    }

    @Test
    @Transactional
    void fullUpdateCursosWithPatch() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();

        // Update the cursos using partial update
        Cursos partialUpdatedCursos = new Cursos();
        partialUpdatedCursos.setId(cursos.getId());

        partialUpdatedCursos.nombreCurso(UPDATED_NOMBRE_CURSO);

        restCursosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCursos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCursos))
            )
            .andExpect(status().isOk());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
        Cursos testCursos = cursosList.get(cursosList.size() - 1);
        assertThat(testCursos.getNombreCurso()).isEqualTo(UPDATED_NOMBRE_CURSO);
    }

    @Test
    @Transactional
    void patchNonExistingCursos() throws Exception {
        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();
        cursos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cursos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cursos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCursos() throws Exception {
        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();
        cursos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cursos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCursos() throws Exception {
        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();
        cursos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cursos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCursos() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        int databaseSizeBeforeDelete = cursosRepository.findAll().size();

        // Delete the cursos
        restCursosMockMvc
            .perform(delete(ENTITY_API_URL_ID, cursos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
