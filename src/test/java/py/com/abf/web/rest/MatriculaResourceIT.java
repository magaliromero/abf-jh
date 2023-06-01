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
import py.com.abf.domain.Matricula;
import py.com.abf.domain.enumeration.EstadosPagos;
import py.com.abf.repository.MatriculaRepository;
import py.com.abf.service.MatriculaService;
import py.com.abf.service.criteria.MatriculaCriteria;

/**
 * Integration tests for the {@link MatriculaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MatriculaResourceIT {

    private static final String DEFAULT_CONCEPTO = "AAAAAAAAAA";
    private static final String UPDATED_CONCEPTO = "BBBBBBBBBB";

    private static final Integer DEFAULT_MONTO = 1;
    private static final Integer UPDATED_MONTO = 2;
    private static final Integer SMALLER_MONTO = 1 - 1;

    private static final LocalDate DEFAULT_FECHA_INSCRIPCION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INSCRIPCION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_INSCRIPCION = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_INICIO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_PAGO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_PAGO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_PAGO = LocalDate.ofEpochDay(-1L);

    private static final EstadosPagos DEFAULT_ESTADO = EstadosPagos.PAGADO;
    private static final EstadosPagos UPDATED_ESTADO = EstadosPagos.PENDIENTE;

    private static final String ENTITY_API_URL = "/api/matriculas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Mock
    private MatriculaRepository matriculaRepositoryMock;

    @Mock
    private MatriculaService matriculaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatriculaMockMvc;

    private Matricula matricula;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matricula createEntity(EntityManager em) {
        Matricula matricula = new Matricula()
            .concepto(DEFAULT_CONCEPTO)
            .monto(DEFAULT_MONTO)
            .fechaInscripcion(DEFAULT_FECHA_INSCRIPCION)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaPago(DEFAULT_FECHA_PAGO)
            .estado(DEFAULT_ESTADO);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        matricula.setAlumno(alumnos);
        return matricula;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matricula createUpdatedEntity(EntityManager em) {
        Matricula matricula = new Matricula()
            .concepto(UPDATED_CONCEPTO)
            .monto(UPDATED_MONTO)
            .fechaInscripcion(UPDATED_FECHA_INSCRIPCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaPago(UPDATED_FECHA_PAGO)
            .estado(UPDATED_ESTADO);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createUpdatedEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        matricula.setAlumno(alumnos);
        return matricula;
    }

    @BeforeEach
    public void initTest() {
        matricula = createEntity(em);
    }

    @Test
    @Transactional
    void createMatricula() throws Exception {
        int databaseSizeBeforeCreate = matriculaRepository.findAll().size();
        // Create the Matricula
        restMatriculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matricula)))
            .andExpect(status().isCreated());

        // Validate the Matricula in the database
        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeCreate + 1);
        Matricula testMatricula = matriculaList.get(matriculaList.size() - 1);
        assertThat(testMatricula.getConcepto()).isEqualTo(DEFAULT_CONCEPTO);
        assertThat(testMatricula.getMonto()).isEqualTo(DEFAULT_MONTO);
        assertThat(testMatricula.getFechaInscripcion()).isEqualTo(DEFAULT_FECHA_INSCRIPCION);
        assertThat(testMatricula.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testMatricula.getFechaPago()).isEqualTo(DEFAULT_FECHA_PAGO);
        assertThat(testMatricula.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createMatriculaWithExistingId() throws Exception {
        // Create the Matricula with an existing ID
        matricula.setId(1L);

        int databaseSizeBeforeCreate = matriculaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatriculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matricula)))
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConceptoIsRequired() throws Exception {
        int databaseSizeBeforeTest = matriculaRepository.findAll().size();
        // set the field null
        matricula.setConcepto(null);

        // Create the Matricula, which fails.

        restMatriculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matricula)))
            .andExpect(status().isBadRequest());

        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = matriculaRepository.findAll().size();
        // set the field null
        matricula.setMonto(null);

        // Create the Matricula, which fails.

        restMatriculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matricula)))
            .andExpect(status().isBadRequest());

        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaInscripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = matriculaRepository.findAll().size();
        // set the field null
        matricula.setFechaInscripcion(null);

        // Create the Matricula, which fails.

        restMatriculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matricula)))
            .andExpect(status().isBadRequest());

        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = matriculaRepository.findAll().size();
        // set the field null
        matricula.setFechaInicio(null);

        // Create the Matricula, which fails.

        restMatriculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matricula)))
            .andExpect(status().isBadRequest());

        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = matriculaRepository.findAll().size();
        // set the field null
        matricula.setEstado(null);

        // Create the Matricula, which fails.

        restMatriculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matricula)))
            .andExpect(status().isBadRequest());

        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMatriculas() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList
        restMatriculaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matricula.getId().intValue())))
            .andExpect(jsonPath("$.[*].concepto").value(hasItem(DEFAULT_CONCEPTO)))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO)))
            .andExpect(jsonPath("$.[*].fechaInscripcion").value(hasItem(DEFAULT_FECHA_INSCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaPago").value(hasItem(DEFAULT_FECHA_PAGO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatriculasWithEagerRelationshipsIsEnabled() throws Exception {
        when(matriculaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatriculaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(matriculaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatriculasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(matriculaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatriculaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(matriculaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMatricula() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get the matricula
        restMatriculaMockMvc
            .perform(get(ENTITY_API_URL_ID, matricula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matricula.getId().intValue()))
            .andExpect(jsonPath("$.concepto").value(DEFAULT_CONCEPTO))
            .andExpect(jsonPath("$.monto").value(DEFAULT_MONTO))
            .andExpect(jsonPath("$.fechaInscripcion").value(DEFAULT_FECHA_INSCRIPCION.toString()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaPago").value(DEFAULT_FECHA_PAGO.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getMatriculasByIdFiltering() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        Long id = matricula.getId();

        defaultMatriculaShouldBeFound("id.equals=" + id);
        defaultMatriculaShouldNotBeFound("id.notEquals=" + id);

        defaultMatriculaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMatriculaShouldNotBeFound("id.greaterThan=" + id);

        defaultMatriculaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMatriculaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMatriculasByConceptoIsEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where concepto equals to DEFAULT_CONCEPTO
        defaultMatriculaShouldBeFound("concepto.equals=" + DEFAULT_CONCEPTO);

        // Get all the matriculaList where concepto equals to UPDATED_CONCEPTO
        defaultMatriculaShouldNotBeFound("concepto.equals=" + UPDATED_CONCEPTO);
    }

    @Test
    @Transactional
    void getAllMatriculasByConceptoIsInShouldWork() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where concepto in DEFAULT_CONCEPTO or UPDATED_CONCEPTO
        defaultMatriculaShouldBeFound("concepto.in=" + DEFAULT_CONCEPTO + "," + UPDATED_CONCEPTO);

        // Get all the matriculaList where concepto equals to UPDATED_CONCEPTO
        defaultMatriculaShouldNotBeFound("concepto.in=" + UPDATED_CONCEPTO);
    }

    @Test
    @Transactional
    void getAllMatriculasByConceptoIsNullOrNotNull() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where concepto is not null
        defaultMatriculaShouldBeFound("concepto.specified=true");

        // Get all the matriculaList where concepto is null
        defaultMatriculaShouldNotBeFound("concepto.specified=false");
    }

    @Test
    @Transactional
    void getAllMatriculasByConceptoContainsSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where concepto contains DEFAULT_CONCEPTO
        defaultMatriculaShouldBeFound("concepto.contains=" + DEFAULT_CONCEPTO);

        // Get all the matriculaList where concepto contains UPDATED_CONCEPTO
        defaultMatriculaShouldNotBeFound("concepto.contains=" + UPDATED_CONCEPTO);
    }

    @Test
    @Transactional
    void getAllMatriculasByConceptoNotContainsSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where concepto does not contain DEFAULT_CONCEPTO
        defaultMatriculaShouldNotBeFound("concepto.doesNotContain=" + DEFAULT_CONCEPTO);

        // Get all the matriculaList where concepto does not contain UPDATED_CONCEPTO
        defaultMatriculaShouldBeFound("concepto.doesNotContain=" + UPDATED_CONCEPTO);
    }

    @Test
    @Transactional
    void getAllMatriculasByMontoIsEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where monto equals to DEFAULT_MONTO
        defaultMatriculaShouldBeFound("monto.equals=" + DEFAULT_MONTO);

        // Get all the matriculaList where monto equals to UPDATED_MONTO
        defaultMatriculaShouldNotBeFound("monto.equals=" + UPDATED_MONTO);
    }

    @Test
    @Transactional
    void getAllMatriculasByMontoIsInShouldWork() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where monto in DEFAULT_MONTO or UPDATED_MONTO
        defaultMatriculaShouldBeFound("monto.in=" + DEFAULT_MONTO + "," + UPDATED_MONTO);

        // Get all the matriculaList where monto equals to UPDATED_MONTO
        defaultMatriculaShouldNotBeFound("monto.in=" + UPDATED_MONTO);
    }

    @Test
    @Transactional
    void getAllMatriculasByMontoIsNullOrNotNull() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where monto is not null
        defaultMatriculaShouldBeFound("monto.specified=true");

        // Get all the matriculaList where monto is null
        defaultMatriculaShouldNotBeFound("monto.specified=false");
    }

    @Test
    @Transactional
    void getAllMatriculasByMontoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where monto is greater than or equal to DEFAULT_MONTO
        defaultMatriculaShouldBeFound("monto.greaterThanOrEqual=" + DEFAULT_MONTO);

        // Get all the matriculaList where monto is greater than or equal to UPDATED_MONTO
        defaultMatriculaShouldNotBeFound("monto.greaterThanOrEqual=" + UPDATED_MONTO);
    }

    @Test
    @Transactional
    void getAllMatriculasByMontoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where monto is less than or equal to DEFAULT_MONTO
        defaultMatriculaShouldBeFound("monto.lessThanOrEqual=" + DEFAULT_MONTO);

        // Get all the matriculaList where monto is less than or equal to SMALLER_MONTO
        defaultMatriculaShouldNotBeFound("monto.lessThanOrEqual=" + SMALLER_MONTO);
    }

    @Test
    @Transactional
    void getAllMatriculasByMontoIsLessThanSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where monto is less than DEFAULT_MONTO
        defaultMatriculaShouldNotBeFound("monto.lessThan=" + DEFAULT_MONTO);

        // Get all the matriculaList where monto is less than UPDATED_MONTO
        defaultMatriculaShouldBeFound("monto.lessThan=" + UPDATED_MONTO);
    }

    @Test
    @Transactional
    void getAllMatriculasByMontoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where monto is greater than DEFAULT_MONTO
        defaultMatriculaShouldNotBeFound("monto.greaterThan=" + DEFAULT_MONTO);

        // Get all the matriculaList where monto is greater than SMALLER_MONTO
        defaultMatriculaShouldBeFound("monto.greaterThan=" + SMALLER_MONTO);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInscripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInscripcion equals to DEFAULT_FECHA_INSCRIPCION
        defaultMatriculaShouldBeFound("fechaInscripcion.equals=" + DEFAULT_FECHA_INSCRIPCION);

        // Get all the matriculaList where fechaInscripcion equals to UPDATED_FECHA_INSCRIPCION
        defaultMatriculaShouldNotBeFound("fechaInscripcion.equals=" + UPDATED_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInscripcionIsInShouldWork() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInscripcion in DEFAULT_FECHA_INSCRIPCION or UPDATED_FECHA_INSCRIPCION
        defaultMatriculaShouldBeFound("fechaInscripcion.in=" + DEFAULT_FECHA_INSCRIPCION + "," + UPDATED_FECHA_INSCRIPCION);

        // Get all the matriculaList where fechaInscripcion equals to UPDATED_FECHA_INSCRIPCION
        defaultMatriculaShouldNotBeFound("fechaInscripcion.in=" + UPDATED_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInscripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInscripcion is not null
        defaultMatriculaShouldBeFound("fechaInscripcion.specified=true");

        // Get all the matriculaList where fechaInscripcion is null
        defaultMatriculaShouldNotBeFound("fechaInscripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInscripcionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInscripcion is greater than or equal to DEFAULT_FECHA_INSCRIPCION
        defaultMatriculaShouldBeFound("fechaInscripcion.greaterThanOrEqual=" + DEFAULT_FECHA_INSCRIPCION);

        // Get all the matriculaList where fechaInscripcion is greater than or equal to UPDATED_FECHA_INSCRIPCION
        defaultMatriculaShouldNotBeFound("fechaInscripcion.greaterThanOrEqual=" + UPDATED_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInscripcionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInscripcion is less than or equal to DEFAULT_FECHA_INSCRIPCION
        defaultMatriculaShouldBeFound("fechaInscripcion.lessThanOrEqual=" + DEFAULT_FECHA_INSCRIPCION);

        // Get all the matriculaList where fechaInscripcion is less than or equal to SMALLER_FECHA_INSCRIPCION
        defaultMatriculaShouldNotBeFound("fechaInscripcion.lessThanOrEqual=" + SMALLER_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInscripcionIsLessThanSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInscripcion is less than DEFAULT_FECHA_INSCRIPCION
        defaultMatriculaShouldNotBeFound("fechaInscripcion.lessThan=" + DEFAULT_FECHA_INSCRIPCION);

        // Get all the matriculaList where fechaInscripcion is less than UPDATED_FECHA_INSCRIPCION
        defaultMatriculaShouldBeFound("fechaInscripcion.lessThan=" + UPDATED_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInscripcionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInscripcion is greater than DEFAULT_FECHA_INSCRIPCION
        defaultMatriculaShouldNotBeFound("fechaInscripcion.greaterThan=" + DEFAULT_FECHA_INSCRIPCION);

        // Get all the matriculaList where fechaInscripcion is greater than SMALLER_FECHA_INSCRIPCION
        defaultMatriculaShouldBeFound("fechaInscripcion.greaterThan=" + SMALLER_FECHA_INSCRIPCION);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInicio equals to DEFAULT_FECHA_INICIO
        defaultMatriculaShouldBeFound("fechaInicio.equals=" + DEFAULT_FECHA_INICIO);

        // Get all the matriculaList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultMatriculaShouldNotBeFound("fechaInicio.equals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInicioIsInShouldWork() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInicio in DEFAULT_FECHA_INICIO or UPDATED_FECHA_INICIO
        defaultMatriculaShouldBeFound("fechaInicio.in=" + DEFAULT_FECHA_INICIO + "," + UPDATED_FECHA_INICIO);

        // Get all the matriculaList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultMatriculaShouldNotBeFound("fechaInicio.in=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInicio is not null
        defaultMatriculaShouldBeFound("fechaInicio.specified=true");

        // Get all the matriculaList where fechaInicio is null
        defaultMatriculaShouldNotBeFound("fechaInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInicio is greater than or equal to DEFAULT_FECHA_INICIO
        defaultMatriculaShouldBeFound("fechaInicio.greaterThanOrEqual=" + DEFAULT_FECHA_INICIO);

        // Get all the matriculaList where fechaInicio is greater than or equal to UPDATED_FECHA_INICIO
        defaultMatriculaShouldNotBeFound("fechaInicio.greaterThanOrEqual=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInicioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInicio is less than or equal to DEFAULT_FECHA_INICIO
        defaultMatriculaShouldBeFound("fechaInicio.lessThanOrEqual=" + DEFAULT_FECHA_INICIO);

        // Get all the matriculaList where fechaInicio is less than or equal to SMALLER_FECHA_INICIO
        defaultMatriculaShouldNotBeFound("fechaInicio.lessThanOrEqual=" + SMALLER_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInicio is less than DEFAULT_FECHA_INICIO
        defaultMatriculaShouldNotBeFound("fechaInicio.lessThan=" + DEFAULT_FECHA_INICIO);

        // Get all the matriculaList where fechaInicio is less than UPDATED_FECHA_INICIO
        defaultMatriculaShouldBeFound("fechaInicio.lessThan=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaInicioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaInicio is greater than DEFAULT_FECHA_INICIO
        defaultMatriculaShouldNotBeFound("fechaInicio.greaterThan=" + DEFAULT_FECHA_INICIO);

        // Get all the matriculaList where fechaInicio is greater than SMALLER_FECHA_INICIO
        defaultMatriculaShouldBeFound("fechaInicio.greaterThan=" + SMALLER_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaPago equals to DEFAULT_FECHA_PAGO
        defaultMatriculaShouldBeFound("fechaPago.equals=" + DEFAULT_FECHA_PAGO);

        // Get all the matriculaList where fechaPago equals to UPDATED_FECHA_PAGO
        defaultMatriculaShouldNotBeFound("fechaPago.equals=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaPagoIsInShouldWork() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaPago in DEFAULT_FECHA_PAGO or UPDATED_FECHA_PAGO
        defaultMatriculaShouldBeFound("fechaPago.in=" + DEFAULT_FECHA_PAGO + "," + UPDATED_FECHA_PAGO);

        // Get all the matriculaList where fechaPago equals to UPDATED_FECHA_PAGO
        defaultMatriculaShouldNotBeFound("fechaPago.in=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaPago is not null
        defaultMatriculaShouldBeFound("fechaPago.specified=true");

        // Get all the matriculaList where fechaPago is null
        defaultMatriculaShouldNotBeFound("fechaPago.specified=false");
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaPagoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaPago is greater than or equal to DEFAULT_FECHA_PAGO
        defaultMatriculaShouldBeFound("fechaPago.greaterThanOrEqual=" + DEFAULT_FECHA_PAGO);

        // Get all the matriculaList where fechaPago is greater than or equal to UPDATED_FECHA_PAGO
        defaultMatriculaShouldNotBeFound("fechaPago.greaterThanOrEqual=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaPagoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaPago is less than or equal to DEFAULT_FECHA_PAGO
        defaultMatriculaShouldBeFound("fechaPago.lessThanOrEqual=" + DEFAULT_FECHA_PAGO);

        // Get all the matriculaList where fechaPago is less than or equal to SMALLER_FECHA_PAGO
        defaultMatriculaShouldNotBeFound("fechaPago.lessThanOrEqual=" + SMALLER_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaPagoIsLessThanSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaPago is less than DEFAULT_FECHA_PAGO
        defaultMatriculaShouldNotBeFound("fechaPago.lessThan=" + DEFAULT_FECHA_PAGO);

        // Get all the matriculaList where fechaPago is less than UPDATED_FECHA_PAGO
        defaultMatriculaShouldBeFound("fechaPago.lessThan=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllMatriculasByFechaPagoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where fechaPago is greater than DEFAULT_FECHA_PAGO
        defaultMatriculaShouldNotBeFound("fechaPago.greaterThan=" + DEFAULT_FECHA_PAGO);

        // Get all the matriculaList where fechaPago is greater than SMALLER_FECHA_PAGO
        defaultMatriculaShouldBeFound("fechaPago.greaterThan=" + SMALLER_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllMatriculasByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where estado equals to DEFAULT_ESTADO
        defaultMatriculaShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the matriculaList where estado equals to UPDATED_ESTADO
        defaultMatriculaShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllMatriculasByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultMatriculaShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the matriculaList where estado equals to UPDATED_ESTADO
        defaultMatriculaShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllMatriculasByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList where estado is not null
        defaultMatriculaShouldBeFound("estado.specified=true");

        // Get all the matriculaList where estado is null
        defaultMatriculaShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllMatriculasByAlumnoIsEqualToSomething() throws Exception {
        Alumnos alumno;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            matriculaRepository.saveAndFlush(matricula);
            alumno = AlumnosResourceIT.createEntity(em);
        } else {
            alumno = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        em.persist(alumno);
        em.flush();
        matricula.setAlumno(alumno);
        matriculaRepository.saveAndFlush(matricula);
        Long alumnoId = alumno.getId();

        // Get all the matriculaList where alumno equals to alumnoId
        defaultMatriculaShouldBeFound("alumnoId.equals=" + alumnoId);

        // Get all the matriculaList where alumno equals to (alumnoId + 1)
        defaultMatriculaShouldNotBeFound("alumnoId.equals=" + (alumnoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMatriculaShouldBeFound(String filter) throws Exception {
        restMatriculaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matricula.getId().intValue())))
            .andExpect(jsonPath("$.[*].concepto").value(hasItem(DEFAULT_CONCEPTO)))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO)))
            .andExpect(jsonPath("$.[*].fechaInscripcion").value(hasItem(DEFAULT_FECHA_INSCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaPago").value(hasItem(DEFAULT_FECHA_PAGO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));

        // Check, that the count call also returns 1
        restMatriculaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMatriculaShouldNotBeFound(String filter) throws Exception {
        restMatriculaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMatriculaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMatricula() throws Exception {
        // Get the matricula
        restMatriculaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMatricula() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        int databaseSizeBeforeUpdate = matriculaRepository.findAll().size();

        // Update the matricula
        Matricula updatedMatricula = matriculaRepository.findById(matricula.getId()).get();
        // Disconnect from session so that the updates on updatedMatricula are not directly saved in db
        em.detach(updatedMatricula);
        updatedMatricula
            .concepto(UPDATED_CONCEPTO)
            .monto(UPDATED_MONTO)
            .fechaInscripcion(UPDATED_FECHA_INSCRIPCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaPago(UPDATED_FECHA_PAGO)
            .estado(UPDATED_ESTADO);

        restMatriculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMatricula.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMatricula))
            )
            .andExpect(status().isOk());

        // Validate the Matricula in the database
        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeUpdate);
        Matricula testMatricula = matriculaList.get(matriculaList.size() - 1);
        assertThat(testMatricula.getConcepto()).isEqualTo(UPDATED_CONCEPTO);
        assertThat(testMatricula.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testMatricula.getFechaInscripcion()).isEqualTo(UPDATED_FECHA_INSCRIPCION);
        assertThat(testMatricula.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testMatricula.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testMatricula.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingMatricula() throws Exception {
        int databaseSizeBeforeUpdate = matriculaRepository.findAll().size();
        matricula.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matricula.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matricula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatricula() throws Exception {
        int databaseSizeBeforeUpdate = matriculaRepository.findAll().size();
        matricula.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matricula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatricula() throws Exception {
        int databaseSizeBeforeUpdate = matriculaRepository.findAll().size();
        matricula.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matricula)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matricula in the database
        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatriculaWithPatch() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        int databaseSizeBeforeUpdate = matriculaRepository.findAll().size();

        // Update the matricula using partial update
        Matricula partialUpdatedMatricula = new Matricula();
        partialUpdatedMatricula.setId(matricula.getId());

        partialUpdatedMatricula.monto(UPDATED_MONTO).fechaInicio(UPDATED_FECHA_INICIO).estado(UPDATED_ESTADO);

        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatricula.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatricula))
            )
            .andExpect(status().isOk());

        // Validate the Matricula in the database
        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeUpdate);
        Matricula testMatricula = matriculaList.get(matriculaList.size() - 1);
        assertThat(testMatricula.getConcepto()).isEqualTo(DEFAULT_CONCEPTO);
        assertThat(testMatricula.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testMatricula.getFechaInscripcion()).isEqualTo(DEFAULT_FECHA_INSCRIPCION);
        assertThat(testMatricula.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testMatricula.getFechaPago()).isEqualTo(DEFAULT_FECHA_PAGO);
        assertThat(testMatricula.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateMatriculaWithPatch() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        int databaseSizeBeforeUpdate = matriculaRepository.findAll().size();

        // Update the matricula using partial update
        Matricula partialUpdatedMatricula = new Matricula();
        partialUpdatedMatricula.setId(matricula.getId());

        partialUpdatedMatricula
            .concepto(UPDATED_CONCEPTO)
            .monto(UPDATED_MONTO)
            .fechaInscripcion(UPDATED_FECHA_INSCRIPCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaPago(UPDATED_FECHA_PAGO)
            .estado(UPDATED_ESTADO);

        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatricula.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatricula))
            )
            .andExpect(status().isOk());

        // Validate the Matricula in the database
        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeUpdate);
        Matricula testMatricula = matriculaList.get(matriculaList.size() - 1);
        assertThat(testMatricula.getConcepto()).isEqualTo(UPDATED_CONCEPTO);
        assertThat(testMatricula.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testMatricula.getFechaInscripcion()).isEqualTo(UPDATED_FECHA_INSCRIPCION);
        assertThat(testMatricula.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testMatricula.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testMatricula.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingMatricula() throws Exception {
        int databaseSizeBeforeUpdate = matriculaRepository.findAll().size();
        matricula.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matricula.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matricula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatricula() throws Exception {
        int databaseSizeBeforeUpdate = matriculaRepository.findAll().size();
        matricula.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matricula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatricula() throws Exception {
        int databaseSizeBeforeUpdate = matriculaRepository.findAll().size();
        matricula.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(matricula))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matricula in the database
        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatricula() throws Exception {
        // Initialize the database
        matriculaRepository.saveAndFlush(matricula);

        int databaseSizeBeforeDelete = matriculaRepository.findAll().size();

        // Delete the matricula
        restMatriculaMockMvc
            .perform(delete(ENTITY_API_URL_ID, matricula.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Matricula> matriculaList = matriculaRepository.findAll();
        assertThat(matriculaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
