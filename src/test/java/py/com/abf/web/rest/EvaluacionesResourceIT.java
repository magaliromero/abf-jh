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
import py.com.abf.domain.Evaluaciones;
import py.com.abf.domain.EvaluacionesDetalle;
import py.com.abf.domain.Funcionarios;
import py.com.abf.repository.EvaluacionesRepository;
import py.com.abf.service.EvaluacionesService;
import py.com.abf.service.criteria.EvaluacionesCriteria;

/**
 * Integration tests for the {@link EvaluacionesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EvaluacionesResourceIT {

    private static final Integer DEFAULT_NRO_EVALUACION = 1;
    private static final Integer UPDATED_NRO_EVALUACION = 2;
    private static final Integer SMALLER_NRO_EVALUACION = 1 - 1;

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/evaluaciones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EvaluacionesRepository evaluacionesRepository;

    @Mock
    private EvaluacionesRepository evaluacionesRepositoryMock;

    @Mock
    private EvaluacionesService evaluacionesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvaluacionesMockMvc;

    private Evaluaciones evaluaciones;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaluaciones createEntity(EntityManager em) {
        Evaluaciones evaluaciones = new Evaluaciones().nroEvaluacion(DEFAULT_NRO_EVALUACION).fecha(DEFAULT_FECHA);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        evaluaciones.setAlumnos(alumnos);
        // Add required entity
        Funcionarios funcionarios;
        if (TestUtil.findAll(em, Funcionarios.class).isEmpty()) {
            funcionarios = FuncionariosResourceIT.createEntity(em);
            em.persist(funcionarios);
            em.flush();
        } else {
            funcionarios = TestUtil.findAll(em, Funcionarios.class).get(0);
        }
        evaluaciones.setFuncionarios(funcionarios);
        return evaluaciones;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaluaciones createUpdatedEntity(EntityManager em) {
        Evaluaciones evaluaciones = new Evaluaciones().nroEvaluacion(UPDATED_NRO_EVALUACION).fecha(UPDATED_FECHA);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createUpdatedEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        evaluaciones.setAlumnos(alumnos);
        // Add required entity
        Funcionarios funcionarios;
        if (TestUtil.findAll(em, Funcionarios.class).isEmpty()) {
            funcionarios = FuncionariosResourceIT.createUpdatedEntity(em);
            em.persist(funcionarios);
            em.flush();
        } else {
            funcionarios = TestUtil.findAll(em, Funcionarios.class).get(0);
        }
        evaluaciones.setFuncionarios(funcionarios);
        return evaluaciones;
    }

    @BeforeEach
    public void initTest() {
        evaluaciones = createEntity(em);
    }

    @Test
    @Transactional
    void createEvaluaciones() throws Exception {
        int databaseSizeBeforeCreate = evaluacionesRepository.findAll().size();
        // Create the Evaluaciones
        restEvaluacionesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluaciones)))
            .andExpect(status().isCreated());

        // Validate the Evaluaciones in the database
        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeCreate + 1);
        Evaluaciones testEvaluaciones = evaluacionesList.get(evaluacionesList.size() - 1);
        assertThat(testEvaluaciones.getNroEvaluacion()).isEqualTo(DEFAULT_NRO_EVALUACION);
        assertThat(testEvaluaciones.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void createEvaluacionesWithExistingId() throws Exception {
        // Create the Evaluaciones with an existing ID
        evaluaciones.setId(1L);

        int databaseSizeBeforeCreate = evaluacionesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaluacionesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluaciones)))
            .andExpect(status().isBadRequest());

        // Validate the Evaluaciones in the database
        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNroEvaluacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = evaluacionesRepository.findAll().size();
        // set the field null
        evaluaciones.setNroEvaluacion(null);

        // Create the Evaluaciones, which fails.

        restEvaluacionesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluaciones)))
            .andExpect(status().isBadRequest());

        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = evaluacionesRepository.findAll().size();
        // set the field null
        evaluaciones.setFecha(null);

        // Create the Evaluaciones, which fails.

        restEvaluacionesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluaciones)))
            .andExpect(status().isBadRequest());

        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvaluaciones() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList
        restEvaluacionesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluaciones.getId().intValue())))
            .andExpect(jsonPath("$.[*].nroEvaluacion").value(hasItem(DEFAULT_NRO_EVALUACION)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvaluacionesWithEagerRelationshipsIsEnabled() throws Exception {
        when(evaluacionesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvaluacionesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(evaluacionesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvaluacionesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(evaluacionesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvaluacionesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(evaluacionesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEvaluaciones() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get the evaluaciones
        restEvaluacionesMockMvc
            .perform(get(ENTITY_API_URL_ID, evaluaciones.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evaluaciones.getId().intValue()))
            .andExpect(jsonPath("$.nroEvaluacion").value(DEFAULT_NRO_EVALUACION))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    void getEvaluacionesByIdFiltering() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        Long id = evaluaciones.getId();

        defaultEvaluacionesShouldBeFound("id.equals=" + id);
        defaultEvaluacionesShouldNotBeFound("id.notEquals=" + id);

        defaultEvaluacionesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEvaluacionesShouldNotBeFound("id.greaterThan=" + id);

        defaultEvaluacionesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEvaluacionesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByNroEvaluacionIsEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where nroEvaluacion equals to DEFAULT_NRO_EVALUACION
        defaultEvaluacionesShouldBeFound("nroEvaluacion.equals=" + DEFAULT_NRO_EVALUACION);

        // Get all the evaluacionesList where nroEvaluacion equals to UPDATED_NRO_EVALUACION
        defaultEvaluacionesShouldNotBeFound("nroEvaluacion.equals=" + UPDATED_NRO_EVALUACION);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByNroEvaluacionIsInShouldWork() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where nroEvaluacion in DEFAULT_NRO_EVALUACION or UPDATED_NRO_EVALUACION
        defaultEvaluacionesShouldBeFound("nroEvaluacion.in=" + DEFAULT_NRO_EVALUACION + "," + UPDATED_NRO_EVALUACION);

        // Get all the evaluacionesList where nroEvaluacion equals to UPDATED_NRO_EVALUACION
        defaultEvaluacionesShouldNotBeFound("nroEvaluacion.in=" + UPDATED_NRO_EVALUACION);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByNroEvaluacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where nroEvaluacion is not null
        defaultEvaluacionesShouldBeFound("nroEvaluacion.specified=true");

        // Get all the evaluacionesList where nroEvaluacion is null
        defaultEvaluacionesShouldNotBeFound("nroEvaluacion.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluacionesByNroEvaluacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where nroEvaluacion is greater than or equal to DEFAULT_NRO_EVALUACION
        defaultEvaluacionesShouldBeFound("nroEvaluacion.greaterThanOrEqual=" + DEFAULT_NRO_EVALUACION);

        // Get all the evaluacionesList where nroEvaluacion is greater than or equal to UPDATED_NRO_EVALUACION
        defaultEvaluacionesShouldNotBeFound("nroEvaluacion.greaterThanOrEqual=" + UPDATED_NRO_EVALUACION);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByNroEvaluacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where nroEvaluacion is less than or equal to DEFAULT_NRO_EVALUACION
        defaultEvaluacionesShouldBeFound("nroEvaluacion.lessThanOrEqual=" + DEFAULT_NRO_EVALUACION);

        // Get all the evaluacionesList where nroEvaluacion is less than or equal to SMALLER_NRO_EVALUACION
        defaultEvaluacionesShouldNotBeFound("nroEvaluacion.lessThanOrEqual=" + SMALLER_NRO_EVALUACION);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByNroEvaluacionIsLessThanSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where nroEvaluacion is less than DEFAULT_NRO_EVALUACION
        defaultEvaluacionesShouldNotBeFound("nroEvaluacion.lessThan=" + DEFAULT_NRO_EVALUACION);

        // Get all the evaluacionesList where nroEvaluacion is less than UPDATED_NRO_EVALUACION
        defaultEvaluacionesShouldBeFound("nroEvaluacion.lessThan=" + UPDATED_NRO_EVALUACION);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByNroEvaluacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where nroEvaluacion is greater than DEFAULT_NRO_EVALUACION
        defaultEvaluacionesShouldNotBeFound("nroEvaluacion.greaterThan=" + DEFAULT_NRO_EVALUACION);

        // Get all the evaluacionesList where nroEvaluacion is greater than SMALLER_NRO_EVALUACION
        defaultEvaluacionesShouldBeFound("nroEvaluacion.greaterThan=" + SMALLER_NRO_EVALUACION);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where fecha equals to DEFAULT_FECHA
        defaultEvaluacionesShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the evaluacionesList where fecha equals to UPDATED_FECHA
        defaultEvaluacionesShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultEvaluacionesShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the evaluacionesList where fecha equals to UPDATED_FECHA
        defaultEvaluacionesShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where fecha is not null
        defaultEvaluacionesShouldBeFound("fecha.specified=true");

        // Get all the evaluacionesList where fecha is null
        defaultEvaluacionesShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluacionesByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where fecha is greater than or equal to DEFAULT_FECHA
        defaultEvaluacionesShouldBeFound("fecha.greaterThanOrEqual=" + DEFAULT_FECHA);

        // Get all the evaluacionesList where fecha is greater than or equal to UPDATED_FECHA
        defaultEvaluacionesShouldNotBeFound("fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where fecha is less than or equal to DEFAULT_FECHA
        defaultEvaluacionesShouldBeFound("fecha.lessThanOrEqual=" + DEFAULT_FECHA);

        // Get all the evaluacionesList where fecha is less than or equal to SMALLER_FECHA
        defaultEvaluacionesShouldNotBeFound("fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where fecha is less than DEFAULT_FECHA
        defaultEvaluacionesShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the evaluacionesList where fecha is less than UPDATED_FECHA
        defaultEvaluacionesShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where fecha is greater than DEFAULT_FECHA
        defaultEvaluacionesShouldNotBeFound("fecha.greaterThan=" + DEFAULT_FECHA);

        // Get all the evaluacionesList where fecha is greater than SMALLER_FECHA
        defaultEvaluacionesShouldBeFound("fecha.greaterThan=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByEvaluacionesDetalleIsEqualToSomething() throws Exception {
        EvaluacionesDetalle evaluacionesDetalle;
        if (TestUtil.findAll(em, EvaluacionesDetalle.class).isEmpty()) {
            evaluacionesRepository.saveAndFlush(evaluaciones);
            evaluacionesDetalle = EvaluacionesDetalleResourceIT.createEntity(em);
        } else {
            evaluacionesDetalle = TestUtil.findAll(em, EvaluacionesDetalle.class).get(0);
        }
        em.persist(evaluacionesDetalle);
        em.flush();
        evaluaciones.addEvaluacionesDetalle(evaluacionesDetalle);
        evaluacionesRepository.saveAndFlush(evaluaciones);
        Long evaluacionesDetalleId = evaluacionesDetalle.getId();

        // Get all the evaluacionesList where evaluacionesDetalle equals to evaluacionesDetalleId
        defaultEvaluacionesShouldBeFound("evaluacionesDetalleId.equals=" + evaluacionesDetalleId);

        // Get all the evaluacionesList where evaluacionesDetalle equals to (evaluacionesDetalleId + 1)
        defaultEvaluacionesShouldNotBeFound("evaluacionesDetalleId.equals=" + (evaluacionesDetalleId + 1));
    }

    @Test
    @Transactional
    void getAllEvaluacionesByAlumnosIsEqualToSomething() throws Exception {
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            evaluacionesRepository.saveAndFlush(evaluaciones);
            alumnos = AlumnosResourceIT.createEntity(em);
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        em.persist(alumnos);
        em.flush();
        evaluaciones.setAlumnos(alumnos);
        evaluacionesRepository.saveAndFlush(evaluaciones);
        Long alumnosId = alumnos.getId();

        // Get all the evaluacionesList where alumnos equals to alumnosId
        defaultEvaluacionesShouldBeFound("alumnosId.equals=" + alumnosId);

        // Get all the evaluacionesList where alumnos equals to (alumnosId + 1)
        defaultEvaluacionesShouldNotBeFound("alumnosId.equals=" + (alumnosId + 1));
    }

    @Test
    @Transactional
    void getAllEvaluacionesByFuncionariosIsEqualToSomething() throws Exception {
        Funcionarios funcionarios;
        if (TestUtil.findAll(em, Funcionarios.class).isEmpty()) {
            evaluacionesRepository.saveAndFlush(evaluaciones);
            funcionarios = FuncionariosResourceIT.createEntity(em);
        } else {
            funcionarios = TestUtil.findAll(em, Funcionarios.class).get(0);
        }
        em.persist(funcionarios);
        em.flush();
        evaluaciones.setFuncionarios(funcionarios);
        evaluacionesRepository.saveAndFlush(evaluaciones);
        Long funcionariosId = funcionarios.getId();

        // Get all the evaluacionesList where funcionarios equals to funcionariosId
        defaultEvaluacionesShouldBeFound("funcionariosId.equals=" + funcionariosId);

        // Get all the evaluacionesList where funcionarios equals to (funcionariosId + 1)
        defaultEvaluacionesShouldNotBeFound("funcionariosId.equals=" + (funcionariosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEvaluacionesShouldBeFound(String filter) throws Exception {
        restEvaluacionesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluaciones.getId().intValue())))
            .andExpect(jsonPath("$.[*].nroEvaluacion").value(hasItem(DEFAULT_NRO_EVALUACION)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));

        // Check, that the count call also returns 1
        restEvaluacionesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEvaluacionesShouldNotBeFound(String filter) throws Exception {
        restEvaluacionesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEvaluacionesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvaluaciones() throws Exception {
        // Get the evaluaciones
        restEvaluacionesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvaluaciones() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        int databaseSizeBeforeUpdate = evaluacionesRepository.findAll().size();

        // Update the evaluaciones
        Evaluaciones updatedEvaluaciones = evaluacionesRepository.findById(evaluaciones.getId()).get();
        // Disconnect from session so that the updates on updatedEvaluaciones are not directly saved in db
        em.detach(updatedEvaluaciones);
        updatedEvaluaciones.nroEvaluacion(UPDATED_NRO_EVALUACION).fecha(UPDATED_FECHA);

        restEvaluacionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvaluaciones.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvaluaciones))
            )
            .andExpect(status().isOk());

        // Validate the Evaluaciones in the database
        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeUpdate);
        Evaluaciones testEvaluaciones = evaluacionesList.get(evaluacionesList.size() - 1);
        assertThat(testEvaluaciones.getNroEvaluacion()).isEqualTo(UPDATED_NRO_EVALUACION);
        assertThat(testEvaluaciones.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void putNonExistingEvaluaciones() throws Exception {
        int databaseSizeBeforeUpdate = evaluacionesRepository.findAll().size();
        evaluaciones.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluacionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaluaciones.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evaluaciones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluaciones in the database
        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvaluaciones() throws Exception {
        int databaseSizeBeforeUpdate = evaluacionesRepository.findAll().size();
        evaluaciones.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluacionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evaluaciones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluaciones in the database
        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvaluaciones() throws Exception {
        int databaseSizeBeforeUpdate = evaluacionesRepository.findAll().size();
        evaluaciones.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluacionesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluaciones)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evaluaciones in the database
        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEvaluacionesWithPatch() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        int databaseSizeBeforeUpdate = evaluacionesRepository.findAll().size();

        // Update the evaluaciones using partial update
        Evaluaciones partialUpdatedEvaluaciones = new Evaluaciones();
        partialUpdatedEvaluaciones.setId(evaluaciones.getId());

        partialUpdatedEvaluaciones.nroEvaluacion(UPDATED_NRO_EVALUACION).fecha(UPDATED_FECHA);

        restEvaluacionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluaciones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvaluaciones))
            )
            .andExpect(status().isOk());

        // Validate the Evaluaciones in the database
        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeUpdate);
        Evaluaciones testEvaluaciones = evaluacionesList.get(evaluacionesList.size() - 1);
        assertThat(testEvaluaciones.getNroEvaluacion()).isEqualTo(UPDATED_NRO_EVALUACION);
        assertThat(testEvaluaciones.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void fullUpdateEvaluacionesWithPatch() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        int databaseSizeBeforeUpdate = evaluacionesRepository.findAll().size();

        // Update the evaluaciones using partial update
        Evaluaciones partialUpdatedEvaluaciones = new Evaluaciones();
        partialUpdatedEvaluaciones.setId(evaluaciones.getId());

        partialUpdatedEvaluaciones.nroEvaluacion(UPDATED_NRO_EVALUACION).fecha(UPDATED_FECHA);

        restEvaluacionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluaciones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvaluaciones))
            )
            .andExpect(status().isOk());

        // Validate the Evaluaciones in the database
        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeUpdate);
        Evaluaciones testEvaluaciones = evaluacionesList.get(evaluacionesList.size() - 1);
        assertThat(testEvaluaciones.getNroEvaluacion()).isEqualTo(UPDATED_NRO_EVALUACION);
        assertThat(testEvaluaciones.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void patchNonExistingEvaluaciones() throws Exception {
        int databaseSizeBeforeUpdate = evaluacionesRepository.findAll().size();
        evaluaciones.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluacionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evaluaciones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaluaciones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluaciones in the database
        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvaluaciones() throws Exception {
        int databaseSizeBeforeUpdate = evaluacionesRepository.findAll().size();
        evaluaciones.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluacionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaluaciones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluaciones in the database
        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvaluaciones() throws Exception {
        int databaseSizeBeforeUpdate = evaluacionesRepository.findAll().size();
        evaluaciones.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluacionesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(evaluaciones))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evaluaciones in the database
        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvaluaciones() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        int databaseSizeBeforeDelete = evaluacionesRepository.findAll().size();

        // Delete the evaluaciones
        restEvaluacionesMockMvc
            .perform(delete(ENTITY_API_URL_ID, evaluaciones.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Evaluaciones> evaluacionesList = evaluacionesRepository.findAll();
        assertThat(evaluacionesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
