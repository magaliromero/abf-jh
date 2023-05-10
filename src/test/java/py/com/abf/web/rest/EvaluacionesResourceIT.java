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
import py.com.abf.domain.enumeration.TiposEvaluaciones;
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

    private static final TiposEvaluaciones DEFAULT_TIPO_EVALUACION = TiposEvaluaciones.FINAL;
    private static final TiposEvaluaciones UPDATED_TIPO_EVALUACION = TiposEvaluaciones.ACUMULATIVA;

    private static final Integer DEFAULT_ID_EXAMEN = 1;
    private static final Integer UPDATED_ID_EXAMEN = 2;
    private static final Integer SMALLER_ID_EXAMEN = 1 - 1;

    private static final Integer DEFAULT_ID_ACTA = 1;
    private static final Integer UPDATED_ID_ACTA = 2;
    private static final Integer SMALLER_ID_ACTA = 1 - 1;

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_PUNTOS_LOGRADOS = 1;
    private static final Integer UPDATED_PUNTOS_LOGRADOS = 2;
    private static final Integer SMALLER_PUNTOS_LOGRADOS = 1 - 1;

    private static final Integer DEFAULT_PORCENTAJE = 1;
    private static final Integer UPDATED_PORCENTAJE = 2;
    private static final Integer SMALLER_PORCENTAJE = 1 - 1;

    private static final String DEFAULT_COMENTARIOS = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIOS = "BBBBBBBBBB";

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
        Evaluaciones evaluaciones = new Evaluaciones()
            .tipoEvaluacion(DEFAULT_TIPO_EVALUACION)
            .idExamen(DEFAULT_ID_EXAMEN)
            .idActa(DEFAULT_ID_ACTA)
            .fecha(DEFAULT_FECHA)
            .puntosLogrados(DEFAULT_PUNTOS_LOGRADOS)
            .porcentaje(DEFAULT_PORCENTAJE)
            .comentarios(DEFAULT_COMENTARIOS);
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
        return evaluaciones;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaluaciones createUpdatedEntity(EntityManager em) {
        Evaluaciones evaluaciones = new Evaluaciones()
            .tipoEvaluacion(UPDATED_TIPO_EVALUACION)
            .idExamen(UPDATED_ID_EXAMEN)
            .idActa(UPDATED_ID_ACTA)
            .fecha(UPDATED_FECHA)
            .puntosLogrados(UPDATED_PUNTOS_LOGRADOS)
            .porcentaje(UPDATED_PORCENTAJE)
            .comentarios(UPDATED_COMENTARIOS);
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
        assertThat(testEvaluaciones.getTipoEvaluacion()).isEqualTo(DEFAULT_TIPO_EVALUACION);
        assertThat(testEvaluaciones.getIdExamen()).isEqualTo(DEFAULT_ID_EXAMEN);
        assertThat(testEvaluaciones.getIdActa()).isEqualTo(DEFAULT_ID_ACTA);
        assertThat(testEvaluaciones.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testEvaluaciones.getPuntosLogrados()).isEqualTo(DEFAULT_PUNTOS_LOGRADOS);
        assertThat(testEvaluaciones.getPorcentaje()).isEqualTo(DEFAULT_PORCENTAJE);
        assertThat(testEvaluaciones.getComentarios()).isEqualTo(DEFAULT_COMENTARIOS);
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
    void checkTipoEvaluacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = evaluacionesRepository.findAll().size();
        // set the field null
        evaluaciones.setTipoEvaluacion(null);

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
            .andExpect(jsonPath("$.[*].tipoEvaluacion").value(hasItem(DEFAULT_TIPO_EVALUACION.toString())))
            .andExpect(jsonPath("$.[*].idExamen").value(hasItem(DEFAULT_ID_EXAMEN)))
            .andExpect(jsonPath("$.[*].idActa").value(hasItem(DEFAULT_ID_ACTA)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].puntosLogrados").value(hasItem(DEFAULT_PUNTOS_LOGRADOS)))
            .andExpect(jsonPath("$.[*].porcentaje").value(hasItem(DEFAULT_PORCENTAJE)))
            .andExpect(jsonPath("$.[*].comentarios").value(hasItem(DEFAULT_COMENTARIOS)));
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
            .andExpect(jsonPath("$.tipoEvaluacion").value(DEFAULT_TIPO_EVALUACION.toString()))
            .andExpect(jsonPath("$.idExamen").value(DEFAULT_ID_EXAMEN))
            .andExpect(jsonPath("$.idActa").value(DEFAULT_ID_ACTA))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.puntosLogrados").value(DEFAULT_PUNTOS_LOGRADOS))
            .andExpect(jsonPath("$.porcentaje").value(DEFAULT_PORCENTAJE))
            .andExpect(jsonPath("$.comentarios").value(DEFAULT_COMENTARIOS));
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
    void getAllEvaluacionesByTipoEvaluacionIsEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where tipoEvaluacion equals to DEFAULT_TIPO_EVALUACION
        defaultEvaluacionesShouldBeFound("tipoEvaluacion.equals=" + DEFAULT_TIPO_EVALUACION);

        // Get all the evaluacionesList where tipoEvaluacion equals to UPDATED_TIPO_EVALUACION
        defaultEvaluacionesShouldNotBeFound("tipoEvaluacion.equals=" + UPDATED_TIPO_EVALUACION);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByTipoEvaluacionIsInShouldWork() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where tipoEvaluacion in DEFAULT_TIPO_EVALUACION or UPDATED_TIPO_EVALUACION
        defaultEvaluacionesShouldBeFound("tipoEvaluacion.in=" + DEFAULT_TIPO_EVALUACION + "," + UPDATED_TIPO_EVALUACION);

        // Get all the evaluacionesList where tipoEvaluacion equals to UPDATED_TIPO_EVALUACION
        defaultEvaluacionesShouldNotBeFound("tipoEvaluacion.in=" + UPDATED_TIPO_EVALUACION);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByTipoEvaluacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where tipoEvaluacion is not null
        defaultEvaluacionesShouldBeFound("tipoEvaluacion.specified=true");

        // Get all the evaluacionesList where tipoEvaluacion is null
        defaultEvaluacionesShouldNotBeFound("tipoEvaluacion.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdExamenIsEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idExamen equals to DEFAULT_ID_EXAMEN
        defaultEvaluacionesShouldBeFound("idExamen.equals=" + DEFAULT_ID_EXAMEN);

        // Get all the evaluacionesList where idExamen equals to UPDATED_ID_EXAMEN
        defaultEvaluacionesShouldNotBeFound("idExamen.equals=" + UPDATED_ID_EXAMEN);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdExamenIsInShouldWork() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idExamen in DEFAULT_ID_EXAMEN or UPDATED_ID_EXAMEN
        defaultEvaluacionesShouldBeFound("idExamen.in=" + DEFAULT_ID_EXAMEN + "," + UPDATED_ID_EXAMEN);

        // Get all the evaluacionesList where idExamen equals to UPDATED_ID_EXAMEN
        defaultEvaluacionesShouldNotBeFound("idExamen.in=" + UPDATED_ID_EXAMEN);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdExamenIsNullOrNotNull() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idExamen is not null
        defaultEvaluacionesShouldBeFound("idExamen.specified=true");

        // Get all the evaluacionesList where idExamen is null
        defaultEvaluacionesShouldNotBeFound("idExamen.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdExamenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idExamen is greater than or equal to DEFAULT_ID_EXAMEN
        defaultEvaluacionesShouldBeFound("idExamen.greaterThanOrEqual=" + DEFAULT_ID_EXAMEN);

        // Get all the evaluacionesList where idExamen is greater than or equal to UPDATED_ID_EXAMEN
        defaultEvaluacionesShouldNotBeFound("idExamen.greaterThanOrEqual=" + UPDATED_ID_EXAMEN);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdExamenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idExamen is less than or equal to DEFAULT_ID_EXAMEN
        defaultEvaluacionesShouldBeFound("idExamen.lessThanOrEqual=" + DEFAULT_ID_EXAMEN);

        // Get all the evaluacionesList where idExamen is less than or equal to SMALLER_ID_EXAMEN
        defaultEvaluacionesShouldNotBeFound("idExamen.lessThanOrEqual=" + SMALLER_ID_EXAMEN);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdExamenIsLessThanSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idExamen is less than DEFAULT_ID_EXAMEN
        defaultEvaluacionesShouldNotBeFound("idExamen.lessThan=" + DEFAULT_ID_EXAMEN);

        // Get all the evaluacionesList where idExamen is less than UPDATED_ID_EXAMEN
        defaultEvaluacionesShouldBeFound("idExamen.lessThan=" + UPDATED_ID_EXAMEN);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdExamenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idExamen is greater than DEFAULT_ID_EXAMEN
        defaultEvaluacionesShouldNotBeFound("idExamen.greaterThan=" + DEFAULT_ID_EXAMEN);

        // Get all the evaluacionesList where idExamen is greater than SMALLER_ID_EXAMEN
        defaultEvaluacionesShouldBeFound("idExamen.greaterThan=" + SMALLER_ID_EXAMEN);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdActaIsEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idActa equals to DEFAULT_ID_ACTA
        defaultEvaluacionesShouldBeFound("idActa.equals=" + DEFAULT_ID_ACTA);

        // Get all the evaluacionesList where idActa equals to UPDATED_ID_ACTA
        defaultEvaluacionesShouldNotBeFound("idActa.equals=" + UPDATED_ID_ACTA);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdActaIsInShouldWork() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idActa in DEFAULT_ID_ACTA or UPDATED_ID_ACTA
        defaultEvaluacionesShouldBeFound("idActa.in=" + DEFAULT_ID_ACTA + "," + UPDATED_ID_ACTA);

        // Get all the evaluacionesList where idActa equals to UPDATED_ID_ACTA
        defaultEvaluacionesShouldNotBeFound("idActa.in=" + UPDATED_ID_ACTA);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdActaIsNullOrNotNull() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idActa is not null
        defaultEvaluacionesShouldBeFound("idActa.specified=true");

        // Get all the evaluacionesList where idActa is null
        defaultEvaluacionesShouldNotBeFound("idActa.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdActaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idActa is greater than or equal to DEFAULT_ID_ACTA
        defaultEvaluacionesShouldBeFound("idActa.greaterThanOrEqual=" + DEFAULT_ID_ACTA);

        // Get all the evaluacionesList where idActa is greater than or equal to UPDATED_ID_ACTA
        defaultEvaluacionesShouldNotBeFound("idActa.greaterThanOrEqual=" + UPDATED_ID_ACTA);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdActaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idActa is less than or equal to DEFAULT_ID_ACTA
        defaultEvaluacionesShouldBeFound("idActa.lessThanOrEqual=" + DEFAULT_ID_ACTA);

        // Get all the evaluacionesList where idActa is less than or equal to SMALLER_ID_ACTA
        defaultEvaluacionesShouldNotBeFound("idActa.lessThanOrEqual=" + SMALLER_ID_ACTA);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdActaIsLessThanSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idActa is less than DEFAULT_ID_ACTA
        defaultEvaluacionesShouldNotBeFound("idActa.lessThan=" + DEFAULT_ID_ACTA);

        // Get all the evaluacionesList where idActa is less than UPDATED_ID_ACTA
        defaultEvaluacionesShouldBeFound("idActa.lessThan=" + UPDATED_ID_ACTA);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByIdActaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where idActa is greater than DEFAULT_ID_ACTA
        defaultEvaluacionesShouldNotBeFound("idActa.greaterThan=" + DEFAULT_ID_ACTA);

        // Get all the evaluacionesList where idActa is greater than SMALLER_ID_ACTA
        defaultEvaluacionesShouldBeFound("idActa.greaterThan=" + SMALLER_ID_ACTA);
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
    void getAllEvaluacionesByPuntosLogradosIsEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where puntosLogrados equals to DEFAULT_PUNTOS_LOGRADOS
        defaultEvaluacionesShouldBeFound("puntosLogrados.equals=" + DEFAULT_PUNTOS_LOGRADOS);

        // Get all the evaluacionesList where puntosLogrados equals to UPDATED_PUNTOS_LOGRADOS
        defaultEvaluacionesShouldNotBeFound("puntosLogrados.equals=" + UPDATED_PUNTOS_LOGRADOS);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByPuntosLogradosIsInShouldWork() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where puntosLogrados in DEFAULT_PUNTOS_LOGRADOS or UPDATED_PUNTOS_LOGRADOS
        defaultEvaluacionesShouldBeFound("puntosLogrados.in=" + DEFAULT_PUNTOS_LOGRADOS + "," + UPDATED_PUNTOS_LOGRADOS);

        // Get all the evaluacionesList where puntosLogrados equals to UPDATED_PUNTOS_LOGRADOS
        defaultEvaluacionesShouldNotBeFound("puntosLogrados.in=" + UPDATED_PUNTOS_LOGRADOS);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByPuntosLogradosIsNullOrNotNull() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where puntosLogrados is not null
        defaultEvaluacionesShouldBeFound("puntosLogrados.specified=true");

        // Get all the evaluacionesList where puntosLogrados is null
        defaultEvaluacionesShouldNotBeFound("puntosLogrados.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluacionesByPuntosLogradosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where puntosLogrados is greater than or equal to DEFAULT_PUNTOS_LOGRADOS
        defaultEvaluacionesShouldBeFound("puntosLogrados.greaterThanOrEqual=" + DEFAULT_PUNTOS_LOGRADOS);

        // Get all the evaluacionesList where puntosLogrados is greater than or equal to UPDATED_PUNTOS_LOGRADOS
        defaultEvaluacionesShouldNotBeFound("puntosLogrados.greaterThanOrEqual=" + UPDATED_PUNTOS_LOGRADOS);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByPuntosLogradosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where puntosLogrados is less than or equal to DEFAULT_PUNTOS_LOGRADOS
        defaultEvaluacionesShouldBeFound("puntosLogrados.lessThanOrEqual=" + DEFAULT_PUNTOS_LOGRADOS);

        // Get all the evaluacionesList where puntosLogrados is less than or equal to SMALLER_PUNTOS_LOGRADOS
        defaultEvaluacionesShouldNotBeFound("puntosLogrados.lessThanOrEqual=" + SMALLER_PUNTOS_LOGRADOS);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByPuntosLogradosIsLessThanSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where puntosLogrados is less than DEFAULT_PUNTOS_LOGRADOS
        defaultEvaluacionesShouldNotBeFound("puntosLogrados.lessThan=" + DEFAULT_PUNTOS_LOGRADOS);

        // Get all the evaluacionesList where puntosLogrados is less than UPDATED_PUNTOS_LOGRADOS
        defaultEvaluacionesShouldBeFound("puntosLogrados.lessThan=" + UPDATED_PUNTOS_LOGRADOS);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByPuntosLogradosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where puntosLogrados is greater than DEFAULT_PUNTOS_LOGRADOS
        defaultEvaluacionesShouldNotBeFound("puntosLogrados.greaterThan=" + DEFAULT_PUNTOS_LOGRADOS);

        // Get all the evaluacionesList where puntosLogrados is greater than SMALLER_PUNTOS_LOGRADOS
        defaultEvaluacionesShouldBeFound("puntosLogrados.greaterThan=" + SMALLER_PUNTOS_LOGRADOS);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByPorcentajeIsEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where porcentaje equals to DEFAULT_PORCENTAJE
        defaultEvaluacionesShouldBeFound("porcentaje.equals=" + DEFAULT_PORCENTAJE);

        // Get all the evaluacionesList where porcentaje equals to UPDATED_PORCENTAJE
        defaultEvaluacionesShouldNotBeFound("porcentaje.equals=" + UPDATED_PORCENTAJE);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByPorcentajeIsInShouldWork() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where porcentaje in DEFAULT_PORCENTAJE or UPDATED_PORCENTAJE
        defaultEvaluacionesShouldBeFound("porcentaje.in=" + DEFAULT_PORCENTAJE + "," + UPDATED_PORCENTAJE);

        // Get all the evaluacionesList where porcentaje equals to UPDATED_PORCENTAJE
        defaultEvaluacionesShouldNotBeFound("porcentaje.in=" + UPDATED_PORCENTAJE);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByPorcentajeIsNullOrNotNull() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where porcentaje is not null
        defaultEvaluacionesShouldBeFound("porcentaje.specified=true");

        // Get all the evaluacionesList where porcentaje is null
        defaultEvaluacionesShouldNotBeFound("porcentaje.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluacionesByPorcentajeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where porcentaje is greater than or equal to DEFAULT_PORCENTAJE
        defaultEvaluacionesShouldBeFound("porcentaje.greaterThanOrEqual=" + DEFAULT_PORCENTAJE);

        // Get all the evaluacionesList where porcentaje is greater than or equal to UPDATED_PORCENTAJE
        defaultEvaluacionesShouldNotBeFound("porcentaje.greaterThanOrEqual=" + UPDATED_PORCENTAJE);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByPorcentajeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where porcentaje is less than or equal to DEFAULT_PORCENTAJE
        defaultEvaluacionesShouldBeFound("porcentaje.lessThanOrEqual=" + DEFAULT_PORCENTAJE);

        // Get all the evaluacionesList where porcentaje is less than or equal to SMALLER_PORCENTAJE
        defaultEvaluacionesShouldNotBeFound("porcentaje.lessThanOrEqual=" + SMALLER_PORCENTAJE);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByPorcentajeIsLessThanSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where porcentaje is less than DEFAULT_PORCENTAJE
        defaultEvaluacionesShouldNotBeFound("porcentaje.lessThan=" + DEFAULT_PORCENTAJE);

        // Get all the evaluacionesList where porcentaje is less than UPDATED_PORCENTAJE
        defaultEvaluacionesShouldBeFound("porcentaje.lessThan=" + UPDATED_PORCENTAJE);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByPorcentajeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where porcentaje is greater than DEFAULT_PORCENTAJE
        defaultEvaluacionesShouldNotBeFound("porcentaje.greaterThan=" + DEFAULT_PORCENTAJE);

        // Get all the evaluacionesList where porcentaje is greater than SMALLER_PORCENTAJE
        defaultEvaluacionesShouldBeFound("porcentaje.greaterThan=" + SMALLER_PORCENTAJE);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByComentariosIsEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where comentarios equals to DEFAULT_COMENTARIOS
        defaultEvaluacionesShouldBeFound("comentarios.equals=" + DEFAULT_COMENTARIOS);

        // Get all the evaluacionesList where comentarios equals to UPDATED_COMENTARIOS
        defaultEvaluacionesShouldNotBeFound("comentarios.equals=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByComentariosIsInShouldWork() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where comentarios in DEFAULT_COMENTARIOS or UPDATED_COMENTARIOS
        defaultEvaluacionesShouldBeFound("comentarios.in=" + DEFAULT_COMENTARIOS + "," + UPDATED_COMENTARIOS);

        // Get all the evaluacionesList where comentarios equals to UPDATED_COMENTARIOS
        defaultEvaluacionesShouldNotBeFound("comentarios.in=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByComentariosIsNullOrNotNull() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where comentarios is not null
        defaultEvaluacionesShouldBeFound("comentarios.specified=true");

        // Get all the evaluacionesList where comentarios is null
        defaultEvaluacionesShouldNotBeFound("comentarios.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluacionesByComentariosContainsSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where comentarios contains DEFAULT_COMENTARIOS
        defaultEvaluacionesShouldBeFound("comentarios.contains=" + DEFAULT_COMENTARIOS);

        // Get all the evaluacionesList where comentarios contains UPDATED_COMENTARIOS
        defaultEvaluacionesShouldNotBeFound("comentarios.contains=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllEvaluacionesByComentariosNotContainsSomething() throws Exception {
        // Initialize the database
        evaluacionesRepository.saveAndFlush(evaluaciones);

        // Get all the evaluacionesList where comentarios does not contain DEFAULT_COMENTARIOS
        defaultEvaluacionesShouldNotBeFound("comentarios.doesNotContain=" + DEFAULT_COMENTARIOS);

        // Get all the evaluacionesList where comentarios does not contain UPDATED_COMENTARIOS
        defaultEvaluacionesShouldBeFound("comentarios.doesNotContain=" + UPDATED_COMENTARIOS);
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

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEvaluacionesShouldBeFound(String filter) throws Exception {
        restEvaluacionesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluaciones.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoEvaluacion").value(hasItem(DEFAULT_TIPO_EVALUACION.toString())))
            .andExpect(jsonPath("$.[*].idExamen").value(hasItem(DEFAULT_ID_EXAMEN)))
            .andExpect(jsonPath("$.[*].idActa").value(hasItem(DEFAULT_ID_ACTA)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].puntosLogrados").value(hasItem(DEFAULT_PUNTOS_LOGRADOS)))
            .andExpect(jsonPath("$.[*].porcentaje").value(hasItem(DEFAULT_PORCENTAJE)))
            .andExpect(jsonPath("$.[*].comentarios").value(hasItem(DEFAULT_COMENTARIOS)));

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
        updatedEvaluaciones
            .tipoEvaluacion(UPDATED_TIPO_EVALUACION)
            .idExamen(UPDATED_ID_EXAMEN)
            .idActa(UPDATED_ID_ACTA)
            .fecha(UPDATED_FECHA)
            .puntosLogrados(UPDATED_PUNTOS_LOGRADOS)
            .porcentaje(UPDATED_PORCENTAJE)
            .comentarios(UPDATED_COMENTARIOS);

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
        assertThat(testEvaluaciones.getTipoEvaluacion()).isEqualTo(UPDATED_TIPO_EVALUACION);
        assertThat(testEvaluaciones.getIdExamen()).isEqualTo(UPDATED_ID_EXAMEN);
        assertThat(testEvaluaciones.getIdActa()).isEqualTo(UPDATED_ID_ACTA);
        assertThat(testEvaluaciones.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEvaluaciones.getPuntosLogrados()).isEqualTo(UPDATED_PUNTOS_LOGRADOS);
        assertThat(testEvaluaciones.getPorcentaje()).isEqualTo(UPDATED_PORCENTAJE);
        assertThat(testEvaluaciones.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
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

        partialUpdatedEvaluaciones
            .tipoEvaluacion(UPDATED_TIPO_EVALUACION)
            .idExamen(UPDATED_ID_EXAMEN)
            .idActa(UPDATED_ID_ACTA)
            .fecha(UPDATED_FECHA)
            .puntosLogrados(UPDATED_PUNTOS_LOGRADOS)
            .porcentaje(UPDATED_PORCENTAJE);

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
        assertThat(testEvaluaciones.getTipoEvaluacion()).isEqualTo(UPDATED_TIPO_EVALUACION);
        assertThat(testEvaluaciones.getIdExamen()).isEqualTo(UPDATED_ID_EXAMEN);
        assertThat(testEvaluaciones.getIdActa()).isEqualTo(UPDATED_ID_ACTA);
        assertThat(testEvaluaciones.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEvaluaciones.getPuntosLogrados()).isEqualTo(UPDATED_PUNTOS_LOGRADOS);
        assertThat(testEvaluaciones.getPorcentaje()).isEqualTo(UPDATED_PORCENTAJE);
        assertThat(testEvaluaciones.getComentarios()).isEqualTo(DEFAULT_COMENTARIOS);
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

        partialUpdatedEvaluaciones
            .tipoEvaluacion(UPDATED_TIPO_EVALUACION)
            .idExamen(UPDATED_ID_EXAMEN)
            .idActa(UPDATED_ID_ACTA)
            .fecha(UPDATED_FECHA)
            .puntosLogrados(UPDATED_PUNTOS_LOGRADOS)
            .porcentaje(UPDATED_PORCENTAJE)
            .comentarios(UPDATED_COMENTARIOS);

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
        assertThat(testEvaluaciones.getTipoEvaluacion()).isEqualTo(UPDATED_TIPO_EVALUACION);
        assertThat(testEvaluaciones.getIdExamen()).isEqualTo(UPDATED_ID_EXAMEN);
        assertThat(testEvaluaciones.getIdActa()).isEqualTo(UPDATED_ID_ACTA);
        assertThat(testEvaluaciones.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEvaluaciones.getPuntosLogrados()).isEqualTo(UPDATED_PUNTOS_LOGRADOS);
        assertThat(testEvaluaciones.getPorcentaje()).isEqualTo(UPDATED_PORCENTAJE);
        assertThat(testEvaluaciones.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
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
