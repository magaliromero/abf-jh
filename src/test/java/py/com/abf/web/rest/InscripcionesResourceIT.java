package py.com.abf.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.IntegrationTest;
import py.com.abf.domain.Alumnos;
import py.com.abf.domain.Cursos;
import py.com.abf.domain.Inscripciones;
import py.com.abf.repository.InscripcionesRepository;
import py.com.abf.service.InscripcionesService;
import py.com.abf.service.criteria.InscripcionesCriteria;

/**
 * Integration tests for the {@link InscripcionesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InscripcionesResourceIT {

    private static final LocalDate DEFAULT_FECHA_INSCRIPCION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INSCRIPCION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_INSCRIPCION = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/inscripciones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InscripcionesRepository inscripcionesRepository;

    @Mock
    private InscripcionesRepository inscripcionesRepositoryMock;

    @Mock
    private InscripcionesService inscripcionesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscripcionesMockMvc;

    private Inscripciones inscripciones;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscripciones createEntity(EntityManager em) {
        Inscripciones inscripciones = new Inscripciones().fechaInscripcion(DEFAULT_FECHA_INSCRIPCION);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        inscripciones.setAlumnos(alumnos);
        // Add required entity
        Cursos cursos;
        if (TestUtil.findAll(em, Cursos.class).isEmpty()) {
            cursos = CursosResourceIT.createEntity(em);
            em.persist(cursos);
            em.flush();
        } else {
            cursos = TestUtil.findAll(em, Cursos.class).get(0);
        }
        inscripciones.setCursos(cursos);
        return inscripciones;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscripciones createUpdatedEntity(EntityManager em) {
        Inscripciones inscripciones = new Inscripciones().fechaInscripcion(UPDATED_FECHA_INSCRIPCION);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createUpdatedEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        inscripciones.setAlumnos(alumnos);
        // Add required entity
        Cursos cursos;
        if (TestUtil.findAll(em, Cursos.class).isEmpty()) {
            cursos = CursosResourceIT.createUpdatedEntity(em);
            em.persist(cursos);
            em.flush();
        } else {
            cursos = TestUtil.findAll(em, Cursos.class).get(0);
        }
        inscripciones.setCursos(cursos);
        return inscripciones;
    }

    @BeforeEach
    public void initTest() {
        inscripciones = createEntity(em);
    }

    @Test
    @Transactional
    void createInscripciones() throws Exception {
        int databaseSizeBeforeCreate = inscripcionesRepository.findAll().size();
        // Create the Inscripciones
        restInscripcionesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscripciones)))
            .andExpect(status().isCreated());

        // Validate the Inscripciones in the database
        List<Inscripciones> inscripcionesList = inscripcionesRepository.findAll();
        assertThat(inscripcionesList).hasSize(databaseSizeBeforeCreate + 1);
        Inscripciones testInscripciones = inscripcionesList.get(inscripcionesList.size() - 1);
        assertThat(testInscripciones.getFechaInscripcion()).isEqualTo(DEFAULT_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void createInscripcionesWithExistingId() throws Exception {
        // Create the Inscripciones with an existing ID
        inscripciones.setId(1L);

        int databaseSizeBeforeCreate = inscripcionesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscripcionesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscripciones)))
            .andExpect(status().isBadRequest());

        // Validate the Inscripciones in the database
        List<Inscripciones> inscripcionesList = inscripcionesRepository.findAll();
        assertThat(inscripcionesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInscripciones() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        // Get all the inscripcionesList
        restInscripcionesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscripciones.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInscripcion").value(hasItem(DEFAULT_FECHA_INSCRIPCION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInscripcionesWithEagerRelationshipsIsEnabled() throws Exception {
        when(inscripcionesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInscripcionesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inscripcionesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInscripcionesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inscripcionesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInscripcionesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(inscripcionesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInscripciones() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        // Get the inscripciones
        restInscripcionesMockMvc
            .perform(get(ENTITY_API_URL_ID, inscripciones.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscripciones.getId().intValue()))
            .andExpect(jsonPath("$.fechaInscripcion").value(DEFAULT_FECHA_INSCRIPCION.toString()));
    }

    @Test
    @Transactional
    void getInscripcionesByIdFiltering() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        Long id = inscripciones.getId();

        defaultInscripcionesShouldBeFound("id.equals=" + id);
        defaultInscripcionesShouldNotBeFound("id.notEquals=" + id);

        defaultInscripcionesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInscripcionesShouldNotBeFound("id.greaterThan=" + id);

        defaultInscripcionesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInscripcionesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInscripcionesByFechaInscripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        // Get all the inscripcionesList where fechaInscripcion equals to DEFAULT_FECHA_INSCRIPCION
        defaultInscripcionesShouldBeFound("fechaInscripcion.equals=" + DEFAULT_FECHA_INSCRIPCION);

        // Get all the inscripcionesList where fechaInscripcion equals to UPDATED_FECHA_INSCRIPCION
        defaultInscripcionesShouldNotBeFound("fechaInscripcion.equals=" + UPDATED_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void getAllInscripcionesByFechaInscripcionIsInShouldWork() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        // Get all the inscripcionesList where fechaInscripcion in DEFAULT_FECHA_INSCRIPCION or UPDATED_FECHA_INSCRIPCION
        defaultInscripcionesShouldBeFound("fechaInscripcion.in=" + DEFAULT_FECHA_INSCRIPCION + "," + UPDATED_FECHA_INSCRIPCION);

        // Get all the inscripcionesList where fechaInscripcion equals to UPDATED_FECHA_INSCRIPCION
        defaultInscripcionesShouldNotBeFound("fechaInscripcion.in=" + UPDATED_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void getAllInscripcionesByFechaInscripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        // Get all the inscripcionesList where fechaInscripcion is not null
        defaultInscripcionesShouldBeFound("fechaInscripcion.specified=true");

        // Get all the inscripcionesList where fechaInscripcion is null
        defaultInscripcionesShouldNotBeFound("fechaInscripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllInscripcionesByFechaInscripcionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        // Get all the inscripcionesList where fechaInscripcion is greater than or equal to DEFAULT_FECHA_INSCRIPCION
        defaultInscripcionesShouldBeFound("fechaInscripcion.greaterThanOrEqual=" + DEFAULT_FECHA_INSCRIPCION);

        // Get all the inscripcionesList where fechaInscripcion is greater than or equal to UPDATED_FECHA_INSCRIPCION
        defaultInscripcionesShouldNotBeFound("fechaInscripcion.greaterThanOrEqual=" + UPDATED_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void getAllInscripcionesByFechaInscripcionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        // Get all the inscripcionesList where fechaInscripcion is less than or equal to DEFAULT_FECHA_INSCRIPCION
        defaultInscripcionesShouldBeFound("fechaInscripcion.lessThanOrEqual=" + DEFAULT_FECHA_INSCRIPCION);

        // Get all the inscripcionesList where fechaInscripcion is less than or equal to SMALLER_FECHA_INSCRIPCION
        defaultInscripcionesShouldNotBeFound("fechaInscripcion.lessThanOrEqual=" + SMALLER_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void getAllInscripcionesByFechaInscripcionIsLessThanSomething() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        // Get all the inscripcionesList where fechaInscripcion is less than DEFAULT_FECHA_INSCRIPCION
        defaultInscripcionesShouldNotBeFound("fechaInscripcion.lessThan=" + DEFAULT_FECHA_INSCRIPCION);

        // Get all the inscripcionesList where fechaInscripcion is less than UPDATED_FECHA_INSCRIPCION
        defaultInscripcionesShouldBeFound("fechaInscripcion.lessThan=" + UPDATED_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void getAllInscripcionesByFechaInscripcionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        // Get all the inscripcionesList where fechaInscripcion is greater than DEFAULT_FECHA_INSCRIPCION
        defaultInscripcionesShouldNotBeFound("fechaInscripcion.greaterThan=" + DEFAULT_FECHA_INSCRIPCION);

        // Get all the inscripcionesList where fechaInscripcion is greater than SMALLER_FECHA_INSCRIPCION
        defaultInscripcionesShouldBeFound("fechaInscripcion.greaterThan=" + SMALLER_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void getAllInscripcionesByAlumnosIsEqualToSomething() throws Exception {
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            inscripcionesRepository.saveAndFlush(inscripciones);
            alumnos = AlumnosResourceIT.createEntity(em);
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        em.persist(alumnos);
        em.flush();
        inscripciones.setAlumnos(alumnos);
        inscripcionesRepository.saveAndFlush(inscripciones);
        Long alumnosId = alumnos.getId();

        // Get all the inscripcionesList where alumnos equals to alumnosId
        defaultInscripcionesShouldBeFound("alumnosId.equals=" + alumnosId);

        // Get all the inscripcionesList where alumnos equals to (alumnosId + 1)
        defaultInscripcionesShouldNotBeFound("alumnosId.equals=" + (alumnosId + 1));
    }

    @Test
    @Transactional
    void getAllInscripcionesByCursosIsEqualToSomething() throws Exception {
        Cursos cursos;
        if (TestUtil.findAll(em, Cursos.class).isEmpty()) {
            inscripcionesRepository.saveAndFlush(inscripciones);
            cursos = CursosResourceIT.createEntity(em);
        } else {
            cursos = TestUtil.findAll(em, Cursos.class).get(0);
        }
        em.persist(cursos);
        em.flush();
        inscripciones.setCursos(cursos);
        inscripcionesRepository.saveAndFlush(inscripciones);
        Long cursosId = cursos.getId();

        // Get all the inscripcionesList where cursos equals to cursosId
        defaultInscripcionesShouldBeFound("cursosId.equals=" + cursosId);

        // Get all the inscripcionesList where cursos equals to (cursosId + 1)
        defaultInscripcionesShouldNotBeFound("cursosId.equals=" + (cursosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInscripcionesShouldBeFound(String filter) throws Exception {
        restInscripcionesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscripciones.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInscripcion").value(hasItem(DEFAULT_FECHA_INSCRIPCION.toString())));

        // Check, that the count call also returns 1
        restInscripcionesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInscripcionesShouldNotBeFound(String filter) throws Exception {
        restInscripcionesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInscripcionesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInscripciones() throws Exception {
        // Get the inscripciones
        restInscripcionesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInscripciones() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        int databaseSizeBeforeUpdate = inscripcionesRepository.findAll().size();

        // Update the inscripciones
        Inscripciones updatedInscripciones = inscripcionesRepository.findById(inscripciones.getId()).get();
        // Disconnect from session so that the updates on updatedInscripciones are not directly saved in db
        em.detach(updatedInscripciones);
        updatedInscripciones.fechaInscripcion(UPDATED_FECHA_INSCRIPCION);

        restInscripcionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInscripciones.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInscripciones))
            )
            .andExpect(status().isOk());

        // Validate the Inscripciones in the database
        List<Inscripciones> inscripcionesList = inscripcionesRepository.findAll();
        assertThat(inscripcionesList).hasSize(databaseSizeBeforeUpdate);
        Inscripciones testInscripciones = inscripcionesList.get(inscripcionesList.size() - 1);
        assertThat(testInscripciones.getFechaInscripcion()).isEqualTo(UPDATED_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingInscripciones() throws Exception {
        int databaseSizeBeforeUpdate = inscripcionesRepository.findAll().size();
        inscripciones.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscripcionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscripciones.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscripciones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscripciones in the database
        List<Inscripciones> inscripcionesList = inscripcionesRepository.findAll();
        assertThat(inscripcionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInscripciones() throws Exception {
        int databaseSizeBeforeUpdate = inscripcionesRepository.findAll().size();
        inscripciones.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscripcionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscripciones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscripciones in the database
        List<Inscripciones> inscripcionesList = inscripcionesRepository.findAll();
        assertThat(inscripcionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInscripciones() throws Exception {
        int databaseSizeBeforeUpdate = inscripcionesRepository.findAll().size();
        inscripciones.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscripcionesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscripciones)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscripciones in the database
        List<Inscripciones> inscripcionesList = inscripcionesRepository.findAll();
        assertThat(inscripcionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInscripcionesWithPatch() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        int databaseSizeBeforeUpdate = inscripcionesRepository.findAll().size();

        // Update the inscripciones using partial update
        Inscripciones partialUpdatedInscripciones = new Inscripciones();
        partialUpdatedInscripciones.setId(inscripciones.getId());

        restInscripcionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscripciones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscripciones))
            )
            .andExpect(status().isOk());

        // Validate the Inscripciones in the database
        List<Inscripciones> inscripcionesList = inscripcionesRepository.findAll();
        assertThat(inscripcionesList).hasSize(databaseSizeBeforeUpdate);
        Inscripciones testInscripciones = inscripcionesList.get(inscripcionesList.size() - 1);
        assertThat(testInscripciones.getFechaInscripcion()).isEqualTo(DEFAULT_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateInscripcionesWithPatch() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        int databaseSizeBeforeUpdate = inscripcionesRepository.findAll().size();

        // Update the inscripciones using partial update
        Inscripciones partialUpdatedInscripciones = new Inscripciones();
        partialUpdatedInscripciones.setId(inscripciones.getId());

        partialUpdatedInscripciones.fechaInscripcion(UPDATED_FECHA_INSCRIPCION);

        restInscripcionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscripciones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscripciones))
            )
            .andExpect(status().isOk());

        // Validate the Inscripciones in the database
        List<Inscripciones> inscripcionesList = inscripcionesRepository.findAll();
        assertThat(inscripcionesList).hasSize(databaseSizeBeforeUpdate);
        Inscripciones testInscripciones = inscripcionesList.get(inscripcionesList.size() - 1);
        assertThat(testInscripciones.getFechaInscripcion()).isEqualTo(UPDATED_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingInscripciones() throws Exception {
        int databaseSizeBeforeUpdate = inscripcionesRepository.findAll().size();
        inscripciones.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscripcionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inscripciones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscripciones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscripciones in the database
        List<Inscripciones> inscripcionesList = inscripcionesRepository.findAll();
        assertThat(inscripcionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInscripciones() throws Exception {
        int databaseSizeBeforeUpdate = inscripcionesRepository.findAll().size();
        inscripciones.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscripcionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscripciones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscripciones in the database
        List<Inscripciones> inscripcionesList = inscripcionesRepository.findAll();
        assertThat(inscripcionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInscripciones() throws Exception {
        int databaseSizeBeforeUpdate = inscripcionesRepository.findAll().size();
        inscripciones.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscripcionesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(inscripciones))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscripciones in the database
        List<Inscripciones> inscripcionesList = inscripcionesRepository.findAll();
        assertThat(inscripcionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInscripciones() throws Exception {
        // Initialize the database
        inscripcionesRepository.saveAndFlush(inscripciones);

        int databaseSizeBeforeDelete = inscripcionesRepository.findAll().size();

        // Delete the inscripciones
        restInscripcionesMockMvc
            .perform(delete(ENTITY_API_URL_ID, inscripciones.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inscripciones> inscripcionesList = inscripcionesRepository.findAll();
        assertThat(inscripcionesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
