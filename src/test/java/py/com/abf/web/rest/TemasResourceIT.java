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
import py.com.abf.domain.EvaluacionesDetalle;
import py.com.abf.domain.RegistroClases;
import py.com.abf.domain.Temas;
import py.com.abf.repository.TemasRepository;
import py.com.abf.service.criteria.TemasCriteria;

/**
 * Integration tests for the {@link TemasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TemasResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/temas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TemasRepository temasRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemasMockMvc;

    private Temas temas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temas createEntity(EntityManager em) {
        Temas temas = new Temas().titulo(DEFAULT_TITULO).descripcion(DEFAULT_DESCRIPCION);
        return temas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temas createUpdatedEntity(EntityManager em) {
        Temas temas = new Temas().titulo(UPDATED_TITULO).descripcion(UPDATED_DESCRIPCION);
        return temas;
    }

    @BeforeEach
    public void initTest() {
        temas = createEntity(em);
    }

    @Test
    @Transactional
    void createTemas() throws Exception {
        int databaseSizeBeforeCreate = temasRepository.findAll().size();
        // Create the Temas
        restTemasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(temas)))
            .andExpect(status().isCreated());

        // Validate the Temas in the database
        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeCreate + 1);
        Temas testTemas = temasList.get(temasList.size() - 1);
        assertThat(testTemas.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testTemas.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createTemasWithExistingId() throws Exception {
        // Create the Temas with an existing ID
        temas.setId(1L);

        int databaseSizeBeforeCreate = temasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(temas)))
            .andExpect(status().isBadRequest());

        // Validate the Temas in the database
        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = temasRepository.findAll().size();
        // set the field null
        temas.setTitulo(null);

        // Create the Temas, which fails.

        restTemasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(temas)))
            .andExpect(status().isBadRequest());

        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = temasRepository.findAll().size();
        // set the field null
        temas.setDescripcion(null);

        // Create the Temas, which fails.

        restTemasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(temas)))
            .andExpect(status().isBadRequest());

        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTemas() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        // Get all the temasList
        restTemasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(temas.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getTemas() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        // Get the temas
        restTemasMockMvc
            .perform(get(ENTITY_API_URL_ID, temas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(temas.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getTemasByIdFiltering() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        Long id = temas.getId();

        defaultTemasShouldBeFound("id.equals=" + id);
        defaultTemasShouldNotBeFound("id.notEquals=" + id);

        defaultTemasShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTemasShouldNotBeFound("id.greaterThan=" + id);

        defaultTemasShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTemasShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTemasByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        // Get all the temasList where titulo equals to DEFAULT_TITULO
        defaultTemasShouldBeFound("titulo.equals=" + DEFAULT_TITULO);

        // Get all the temasList where titulo equals to UPDATED_TITULO
        defaultTemasShouldNotBeFound("titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllTemasByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        // Get all the temasList where titulo in DEFAULT_TITULO or UPDATED_TITULO
        defaultTemasShouldBeFound("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO);

        // Get all the temasList where titulo equals to UPDATED_TITULO
        defaultTemasShouldNotBeFound("titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllTemasByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        // Get all the temasList where titulo is not null
        defaultTemasShouldBeFound("titulo.specified=true");

        // Get all the temasList where titulo is null
        defaultTemasShouldNotBeFound("titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllTemasByTituloContainsSomething() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        // Get all the temasList where titulo contains DEFAULT_TITULO
        defaultTemasShouldBeFound("titulo.contains=" + DEFAULT_TITULO);

        // Get all the temasList where titulo contains UPDATED_TITULO
        defaultTemasShouldNotBeFound("titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllTemasByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        // Get all the temasList where titulo does not contain DEFAULT_TITULO
        defaultTemasShouldNotBeFound("titulo.doesNotContain=" + DEFAULT_TITULO);

        // Get all the temasList where titulo does not contain UPDATED_TITULO
        defaultTemasShouldBeFound("titulo.doesNotContain=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllTemasByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        // Get all the temasList where descripcion equals to DEFAULT_DESCRIPCION
        defaultTemasShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the temasList where descripcion equals to UPDATED_DESCRIPCION
        defaultTemasShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTemasByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        // Get all the temasList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultTemasShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the temasList where descripcion equals to UPDATED_DESCRIPCION
        defaultTemasShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTemasByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        // Get all the temasList where descripcion is not null
        defaultTemasShouldBeFound("descripcion.specified=true");

        // Get all the temasList where descripcion is null
        defaultTemasShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllTemasByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        // Get all the temasList where descripcion contains DEFAULT_DESCRIPCION
        defaultTemasShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the temasList where descripcion contains UPDATED_DESCRIPCION
        defaultTemasShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTemasByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        // Get all the temasList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultTemasShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the temasList where descripcion does not contain UPDATED_DESCRIPCION
        defaultTemasShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTemasByEvaluacionesDetalleIsEqualToSomething() throws Exception {
        EvaluacionesDetalle evaluacionesDetalle;
        if (TestUtil.findAll(em, EvaluacionesDetalle.class).isEmpty()) {
            temasRepository.saveAndFlush(temas);
            evaluacionesDetalle = EvaluacionesDetalleResourceIT.createEntity(em);
        } else {
            evaluacionesDetalle = TestUtil.findAll(em, EvaluacionesDetalle.class).get(0);
        }
        em.persist(evaluacionesDetalle);
        em.flush();
        temas.addEvaluacionesDetalle(evaluacionesDetalle);
        temasRepository.saveAndFlush(temas);
        Long evaluacionesDetalleId = evaluacionesDetalle.getId();

        // Get all the temasList where evaluacionesDetalle equals to evaluacionesDetalleId
        defaultTemasShouldBeFound("evaluacionesDetalleId.equals=" + evaluacionesDetalleId);

        // Get all the temasList where evaluacionesDetalle equals to (evaluacionesDetalleId + 1)
        defaultTemasShouldNotBeFound("evaluacionesDetalleId.equals=" + (evaluacionesDetalleId + 1));
    }

    @Test
    @Transactional
    void getAllTemasByRegistroClasesIsEqualToSomething() throws Exception {
        RegistroClases registroClases;
        if (TestUtil.findAll(em, RegistroClases.class).isEmpty()) {
            temasRepository.saveAndFlush(temas);
            registroClases = RegistroClasesResourceIT.createEntity(em);
        } else {
            registroClases = TestUtil.findAll(em, RegistroClases.class).get(0);
        }
        em.persist(registroClases);
        em.flush();
        temas.addRegistroClases(registroClases);
        temasRepository.saveAndFlush(temas);
        Long registroClasesId = registroClases.getId();

        // Get all the temasList where registroClases equals to registroClasesId
        defaultTemasShouldBeFound("registroClasesId.equals=" + registroClasesId);

        // Get all the temasList where registroClases equals to (registroClasesId + 1)
        defaultTemasShouldNotBeFound("registroClasesId.equals=" + (registroClasesId + 1));
    }

    @Test
    @Transactional
    void getAllTemasByCursosIsEqualToSomething() throws Exception {
        Cursos cursos;
        if (TestUtil.findAll(em, Cursos.class).isEmpty()) {
            temasRepository.saveAndFlush(temas);
            cursos = CursosResourceIT.createEntity(em);
        } else {
            cursos = TestUtil.findAll(em, Cursos.class).get(0);
        }
        em.persist(cursos);
        em.flush();
        temas.addCursos(cursos);
        temasRepository.saveAndFlush(temas);
        Long cursosId = cursos.getId();

        // Get all the temasList where cursos equals to cursosId
        defaultTemasShouldBeFound("cursosId.equals=" + cursosId);

        // Get all the temasList where cursos equals to (cursosId + 1)
        defaultTemasShouldNotBeFound("cursosId.equals=" + (cursosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTemasShouldBeFound(String filter) throws Exception {
        restTemasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(temas.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restTemasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTemasShouldNotBeFound(String filter) throws Exception {
        restTemasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTemasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTemas() throws Exception {
        // Get the temas
        restTemasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTemas() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        int databaseSizeBeforeUpdate = temasRepository.findAll().size();

        // Update the temas
        Temas updatedTemas = temasRepository.findById(temas.getId()).get();
        // Disconnect from session so that the updates on updatedTemas are not directly saved in db
        em.detach(updatedTemas);
        updatedTemas.titulo(UPDATED_TITULO).descripcion(UPDATED_DESCRIPCION);

        restTemasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTemas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTemas))
            )
            .andExpect(status().isOk());

        // Validate the Temas in the database
        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeUpdate);
        Temas testTemas = temasList.get(temasList.size() - 1);
        assertThat(testTemas.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testTemas.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingTemas() throws Exception {
        int databaseSizeBeforeUpdate = temasRepository.findAll().size();
        temas.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, temas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(temas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Temas in the database
        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTemas() throws Exception {
        int databaseSizeBeforeUpdate = temasRepository.findAll().size();
        temas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(temas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Temas in the database
        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTemas() throws Exception {
        int databaseSizeBeforeUpdate = temasRepository.findAll().size();
        temas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(temas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Temas in the database
        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTemasWithPatch() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        int databaseSizeBeforeUpdate = temasRepository.findAll().size();

        // Update the temas using partial update
        Temas partialUpdatedTemas = new Temas();
        partialUpdatedTemas.setId(temas.getId());

        restTemasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemas))
            )
            .andExpect(status().isOk());

        // Validate the Temas in the database
        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeUpdate);
        Temas testTemas = temasList.get(temasList.size() - 1);
        assertThat(testTemas.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testTemas.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateTemasWithPatch() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        int databaseSizeBeforeUpdate = temasRepository.findAll().size();

        // Update the temas using partial update
        Temas partialUpdatedTemas = new Temas();
        partialUpdatedTemas.setId(temas.getId());

        partialUpdatedTemas.titulo(UPDATED_TITULO).descripcion(UPDATED_DESCRIPCION);

        restTemasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemas))
            )
            .andExpect(status().isOk());

        // Validate the Temas in the database
        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeUpdate);
        Temas testTemas = temasList.get(temasList.size() - 1);
        assertThat(testTemas.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testTemas.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingTemas() throws Exception {
        int databaseSizeBeforeUpdate = temasRepository.findAll().size();
        temas.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, temas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(temas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Temas in the database
        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTemas() throws Exception {
        int databaseSizeBeforeUpdate = temasRepository.findAll().size();
        temas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(temas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Temas in the database
        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTemas() throws Exception {
        int databaseSizeBeforeUpdate = temasRepository.findAll().size();
        temas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemasMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(temas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Temas in the database
        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTemas() throws Exception {
        // Initialize the database
        temasRepository.saveAndFlush(temas);

        int databaseSizeBeforeDelete = temasRepository.findAll().size();

        // Delete the temas
        restTemasMockMvc
            .perform(delete(ENTITY_API_URL_ID, temas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Temas> temasList = temasRepository.findAll();
        assertThat(temasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
