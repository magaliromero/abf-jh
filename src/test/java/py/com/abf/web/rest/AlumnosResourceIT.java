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
import py.com.abf.domain.Inscripciones;
import py.com.abf.domain.Matricula;
import py.com.abf.domain.Prestamos;
import py.com.abf.domain.RegistroClases;
import py.com.abf.domain.TiposDocumentos;
import py.com.abf.domain.enumeration.EstadosPersona;
import py.com.abf.repository.AlumnosRepository;
import py.com.abf.service.AlumnosService;
import py.com.abf.service.criteria.AlumnosCriteria;

/**
 * Integration tests for the {@link AlumnosResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AlumnosResourceIT {

    private static final Integer DEFAULT_ELO = 1;
    private static final Integer UPDATED_ELO = 2;
    private static final Integer SMALLER_ELO = 1 - 1;

    private static final Integer DEFAULT_FIDE_ID = 1;
    private static final Integer UPDATED_FIDE_ID = 2;
    private static final Integer SMALLER_FIDE_ID = 1 - 1;

    private static final String DEFAULT_NOMBRES = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRES = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_COMPLETO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_COMPLETO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_NACIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTO = "BBBBBBBBBB";

    private static final EstadosPersona DEFAULT_ESTADO = EstadosPersona.ACTIVO;
    private static final EstadosPersona UPDATED_ESTADO = EstadosPersona.INACTIVO;

    private static final String ENTITY_API_URL = "/api/alumnos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlumnosRepository alumnosRepository;

    @Mock
    private AlumnosRepository alumnosRepositoryMock;

    @Mock
    private AlumnosService alumnosServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlumnosMockMvc;

    private Alumnos alumnos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alumnos createEntity(EntityManager em) {
        Alumnos alumnos = new Alumnos()
            .elo(DEFAULT_ELO)
            .fideId(DEFAULT_FIDE_ID)
            .nombres(DEFAULT_NOMBRES)
            .apellidos(DEFAULT_APELLIDOS)
            .nombreCompleto(DEFAULT_NOMBRE_COMPLETO)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .documento(DEFAULT_DOCUMENTO)
            .estado(DEFAULT_ESTADO);
        // Add required entity
        TiposDocumentos tiposDocumentos;
        if (TestUtil.findAll(em, TiposDocumentos.class).isEmpty()) {
            tiposDocumentos = TiposDocumentosResourceIT.createEntity(em);
            em.persist(tiposDocumentos);
            em.flush();
        } else {
            tiposDocumentos = TestUtil.findAll(em, TiposDocumentos.class).get(0);
        }
        alumnos.setTipoDocumentos(tiposDocumentos);
        return alumnos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alumnos createUpdatedEntity(EntityManager em) {
        Alumnos alumnos = new Alumnos()
            .elo(UPDATED_ELO)
            .fideId(UPDATED_FIDE_ID)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .nombreCompleto(UPDATED_NOMBRE_COMPLETO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .documento(UPDATED_DOCUMENTO)
            .estado(UPDATED_ESTADO);
        // Add required entity
        TiposDocumentos tiposDocumentos;
        if (TestUtil.findAll(em, TiposDocumentos.class).isEmpty()) {
            tiposDocumentos = TiposDocumentosResourceIT.createUpdatedEntity(em);
            em.persist(tiposDocumentos);
            em.flush();
        } else {
            tiposDocumentos = TestUtil.findAll(em, TiposDocumentos.class).get(0);
        }
        alumnos.setTipoDocumentos(tiposDocumentos);
        return alumnos;
    }

    @BeforeEach
    public void initTest() {
        alumnos = createEntity(em);
    }

    @Test
    @Transactional
    void createAlumnos() throws Exception {
        int databaseSizeBeforeCreate = alumnosRepository.findAll().size();
        // Create the Alumnos
        restAlumnosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumnos)))
            .andExpect(status().isCreated());

        // Validate the Alumnos in the database
        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeCreate + 1);
        Alumnos testAlumnos = alumnosList.get(alumnosList.size() - 1);
        assertThat(testAlumnos.getElo()).isEqualTo(DEFAULT_ELO);
        assertThat(testAlumnos.getFideId()).isEqualTo(DEFAULT_FIDE_ID);
        assertThat(testAlumnos.getNombres()).isEqualTo(DEFAULT_NOMBRES);
        assertThat(testAlumnos.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testAlumnos.getNombreCompleto()).isEqualTo(DEFAULT_NOMBRE_COMPLETO);
        assertThat(testAlumnos.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAlumnos.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testAlumnos.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testAlumnos.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
        assertThat(testAlumnos.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createAlumnosWithExistingId() throws Exception {
        // Create the Alumnos with an existing ID
        alumnos.setId(1L);

        int databaseSizeBeforeCreate = alumnosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlumnosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumnos)))
            .andExpect(status().isBadRequest());

        // Validate the Alumnos in the database
        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombresIsRequired() throws Exception {
        int databaseSizeBeforeTest = alumnosRepository.findAll().size();
        // set the field null
        alumnos.setNombres(null);

        // Create the Alumnos, which fails.

        restAlumnosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumnos)))
            .andExpect(status().isBadRequest());

        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApellidosIsRequired() throws Exception {
        int databaseSizeBeforeTest = alumnosRepository.findAll().size();
        // set the field null
        alumnos.setApellidos(null);

        // Create the Alumnos, which fails.

        restAlumnosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumnos)))
            .andExpect(status().isBadRequest());

        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreCompletoIsRequired() throws Exception {
        int databaseSizeBeforeTest = alumnosRepository.findAll().size();
        // set the field null
        alumnos.setNombreCompleto(null);

        // Create the Alumnos, which fails.

        restAlumnosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumnos)))
            .andExpect(status().isBadRequest());

        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = alumnosRepository.findAll().size();
        // set the field null
        alumnos.setTelefono(null);

        // Create the Alumnos, which fails.

        restAlumnosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumnos)))
            .andExpect(status().isBadRequest());

        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaNacimientoIsRequired() throws Exception {
        int databaseSizeBeforeTest = alumnosRepository.findAll().size();
        // set the field null
        alumnos.setFechaNacimiento(null);

        // Create the Alumnos, which fails.

        restAlumnosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumnos)))
            .andExpect(status().isBadRequest());

        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = alumnosRepository.findAll().size();
        // set the field null
        alumnos.setDocumento(null);

        // Create the Alumnos, which fails.

        restAlumnosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumnos)))
            .andExpect(status().isBadRequest());

        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = alumnosRepository.findAll().size();
        // set the field null
        alumnos.setEstado(null);

        // Create the Alumnos, which fails.

        restAlumnosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumnos)))
            .andExpect(status().isBadRequest());

        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlumnos() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList
        restAlumnosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alumnos.getId().intValue())))
            .andExpect(jsonPath("$.[*].elo").value(hasItem(DEFAULT_ELO)))
            .andExpect(jsonPath("$.[*].fideId").value(hasItem(DEFAULT_FIDE_ID)))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].nombreCompleto").value(hasItem(DEFAULT_NOMBRE_COMPLETO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlumnosWithEagerRelationshipsIsEnabled() throws Exception {
        when(alumnosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlumnosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(alumnosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlumnosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(alumnosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlumnosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(alumnosRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAlumnos() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get the alumnos
        restAlumnosMockMvc
            .perform(get(ENTITY_API_URL_ID, alumnos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alumnos.getId().intValue()))
            .andExpect(jsonPath("$.elo").value(DEFAULT_ELO))
            .andExpect(jsonPath("$.fideId").value(DEFAULT_FIDE_ID))
            .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.nombreCompleto").value(DEFAULT_NOMBRE_COMPLETO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.documento").value(DEFAULT_DOCUMENTO))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getAlumnosByIdFiltering() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        Long id = alumnos.getId();

        defaultAlumnosShouldBeFound("id.equals=" + id);
        defaultAlumnosShouldNotBeFound("id.notEquals=" + id);

        defaultAlumnosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAlumnosShouldNotBeFound("id.greaterThan=" + id);

        defaultAlumnosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAlumnosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlumnosByEloIsEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where elo equals to DEFAULT_ELO
        defaultAlumnosShouldBeFound("elo.equals=" + DEFAULT_ELO);

        // Get all the alumnosList where elo equals to UPDATED_ELO
        defaultAlumnosShouldNotBeFound("elo.equals=" + UPDATED_ELO);
    }

    @Test
    @Transactional
    void getAllAlumnosByEloIsInShouldWork() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where elo in DEFAULT_ELO or UPDATED_ELO
        defaultAlumnosShouldBeFound("elo.in=" + DEFAULT_ELO + "," + UPDATED_ELO);

        // Get all the alumnosList where elo equals to UPDATED_ELO
        defaultAlumnosShouldNotBeFound("elo.in=" + UPDATED_ELO);
    }

    @Test
    @Transactional
    void getAllAlumnosByEloIsNullOrNotNull() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where elo is not null
        defaultAlumnosShouldBeFound("elo.specified=true");

        // Get all the alumnosList where elo is null
        defaultAlumnosShouldNotBeFound("elo.specified=false");
    }

    @Test
    @Transactional
    void getAllAlumnosByEloIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where elo is greater than or equal to DEFAULT_ELO
        defaultAlumnosShouldBeFound("elo.greaterThanOrEqual=" + DEFAULT_ELO);

        // Get all the alumnosList where elo is greater than or equal to UPDATED_ELO
        defaultAlumnosShouldNotBeFound("elo.greaterThanOrEqual=" + UPDATED_ELO);
    }

    @Test
    @Transactional
    void getAllAlumnosByEloIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where elo is less than or equal to DEFAULT_ELO
        defaultAlumnosShouldBeFound("elo.lessThanOrEqual=" + DEFAULT_ELO);

        // Get all the alumnosList where elo is less than or equal to SMALLER_ELO
        defaultAlumnosShouldNotBeFound("elo.lessThanOrEqual=" + SMALLER_ELO);
    }

    @Test
    @Transactional
    void getAllAlumnosByEloIsLessThanSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where elo is less than DEFAULT_ELO
        defaultAlumnosShouldNotBeFound("elo.lessThan=" + DEFAULT_ELO);

        // Get all the alumnosList where elo is less than UPDATED_ELO
        defaultAlumnosShouldBeFound("elo.lessThan=" + UPDATED_ELO);
    }

    @Test
    @Transactional
    void getAllAlumnosByEloIsGreaterThanSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where elo is greater than DEFAULT_ELO
        defaultAlumnosShouldNotBeFound("elo.greaterThan=" + DEFAULT_ELO);

        // Get all the alumnosList where elo is greater than SMALLER_ELO
        defaultAlumnosShouldBeFound("elo.greaterThan=" + SMALLER_ELO);
    }

    @Test
    @Transactional
    void getAllAlumnosByFideIdIsEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fideId equals to DEFAULT_FIDE_ID
        defaultAlumnosShouldBeFound("fideId.equals=" + DEFAULT_FIDE_ID);

        // Get all the alumnosList where fideId equals to UPDATED_FIDE_ID
        defaultAlumnosShouldNotBeFound("fideId.equals=" + UPDATED_FIDE_ID);
    }

    @Test
    @Transactional
    void getAllAlumnosByFideIdIsInShouldWork() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fideId in DEFAULT_FIDE_ID or UPDATED_FIDE_ID
        defaultAlumnosShouldBeFound("fideId.in=" + DEFAULT_FIDE_ID + "," + UPDATED_FIDE_ID);

        // Get all the alumnosList where fideId equals to UPDATED_FIDE_ID
        defaultAlumnosShouldNotBeFound("fideId.in=" + UPDATED_FIDE_ID);
    }

    @Test
    @Transactional
    void getAllAlumnosByFideIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fideId is not null
        defaultAlumnosShouldBeFound("fideId.specified=true");

        // Get all the alumnosList where fideId is null
        defaultAlumnosShouldNotBeFound("fideId.specified=false");
    }

    @Test
    @Transactional
    void getAllAlumnosByFideIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fideId is greater than or equal to DEFAULT_FIDE_ID
        defaultAlumnosShouldBeFound("fideId.greaterThanOrEqual=" + DEFAULT_FIDE_ID);

        // Get all the alumnosList where fideId is greater than or equal to UPDATED_FIDE_ID
        defaultAlumnosShouldNotBeFound("fideId.greaterThanOrEqual=" + UPDATED_FIDE_ID);
    }

    @Test
    @Transactional
    void getAllAlumnosByFideIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fideId is less than or equal to DEFAULT_FIDE_ID
        defaultAlumnosShouldBeFound("fideId.lessThanOrEqual=" + DEFAULT_FIDE_ID);

        // Get all the alumnosList where fideId is less than or equal to SMALLER_FIDE_ID
        defaultAlumnosShouldNotBeFound("fideId.lessThanOrEqual=" + SMALLER_FIDE_ID);
    }

    @Test
    @Transactional
    void getAllAlumnosByFideIdIsLessThanSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fideId is less than DEFAULT_FIDE_ID
        defaultAlumnosShouldNotBeFound("fideId.lessThan=" + DEFAULT_FIDE_ID);

        // Get all the alumnosList where fideId is less than UPDATED_FIDE_ID
        defaultAlumnosShouldBeFound("fideId.lessThan=" + UPDATED_FIDE_ID);
    }

    @Test
    @Transactional
    void getAllAlumnosByFideIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fideId is greater than DEFAULT_FIDE_ID
        defaultAlumnosShouldNotBeFound("fideId.greaterThan=" + DEFAULT_FIDE_ID);

        // Get all the alumnosList where fideId is greater than SMALLER_FIDE_ID
        defaultAlumnosShouldBeFound("fideId.greaterThan=" + SMALLER_FIDE_ID);
    }

    @Test
    @Transactional
    void getAllAlumnosByNombresIsEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where nombres equals to DEFAULT_NOMBRES
        defaultAlumnosShouldBeFound("nombres.equals=" + DEFAULT_NOMBRES);

        // Get all the alumnosList where nombres equals to UPDATED_NOMBRES
        defaultAlumnosShouldNotBeFound("nombres.equals=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllAlumnosByNombresIsInShouldWork() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where nombres in DEFAULT_NOMBRES or UPDATED_NOMBRES
        defaultAlumnosShouldBeFound("nombres.in=" + DEFAULT_NOMBRES + "," + UPDATED_NOMBRES);

        // Get all the alumnosList where nombres equals to UPDATED_NOMBRES
        defaultAlumnosShouldNotBeFound("nombres.in=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllAlumnosByNombresIsNullOrNotNull() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where nombres is not null
        defaultAlumnosShouldBeFound("nombres.specified=true");

        // Get all the alumnosList where nombres is null
        defaultAlumnosShouldNotBeFound("nombres.specified=false");
    }

    @Test
    @Transactional
    void getAllAlumnosByNombresContainsSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where nombres contains DEFAULT_NOMBRES
        defaultAlumnosShouldBeFound("nombres.contains=" + DEFAULT_NOMBRES);

        // Get all the alumnosList where nombres contains UPDATED_NOMBRES
        defaultAlumnosShouldNotBeFound("nombres.contains=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllAlumnosByNombresNotContainsSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where nombres does not contain DEFAULT_NOMBRES
        defaultAlumnosShouldNotBeFound("nombres.doesNotContain=" + DEFAULT_NOMBRES);

        // Get all the alumnosList where nombres does not contain UPDATED_NOMBRES
        defaultAlumnosShouldBeFound("nombres.doesNotContain=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllAlumnosByApellidosIsEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where apellidos equals to DEFAULT_APELLIDOS
        defaultAlumnosShouldBeFound("apellidos.equals=" + DEFAULT_APELLIDOS);

        // Get all the alumnosList where apellidos equals to UPDATED_APELLIDOS
        defaultAlumnosShouldNotBeFound("apellidos.equals=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllAlumnosByApellidosIsInShouldWork() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where apellidos in DEFAULT_APELLIDOS or UPDATED_APELLIDOS
        defaultAlumnosShouldBeFound("apellidos.in=" + DEFAULT_APELLIDOS + "," + UPDATED_APELLIDOS);

        // Get all the alumnosList where apellidos equals to UPDATED_APELLIDOS
        defaultAlumnosShouldNotBeFound("apellidos.in=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllAlumnosByApellidosIsNullOrNotNull() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where apellidos is not null
        defaultAlumnosShouldBeFound("apellidos.specified=true");

        // Get all the alumnosList where apellidos is null
        defaultAlumnosShouldNotBeFound("apellidos.specified=false");
    }

    @Test
    @Transactional
    void getAllAlumnosByApellidosContainsSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where apellidos contains DEFAULT_APELLIDOS
        defaultAlumnosShouldBeFound("apellidos.contains=" + DEFAULT_APELLIDOS);

        // Get all the alumnosList where apellidos contains UPDATED_APELLIDOS
        defaultAlumnosShouldNotBeFound("apellidos.contains=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllAlumnosByApellidosNotContainsSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where apellidos does not contain DEFAULT_APELLIDOS
        defaultAlumnosShouldNotBeFound("apellidos.doesNotContain=" + DEFAULT_APELLIDOS);

        // Get all the alumnosList where apellidos does not contain UPDATED_APELLIDOS
        defaultAlumnosShouldBeFound("apellidos.doesNotContain=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllAlumnosByNombreCompletoIsEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where nombreCompleto equals to DEFAULT_NOMBRE_COMPLETO
        defaultAlumnosShouldBeFound("nombreCompleto.equals=" + DEFAULT_NOMBRE_COMPLETO);

        // Get all the alumnosList where nombreCompleto equals to UPDATED_NOMBRE_COMPLETO
        defaultAlumnosShouldNotBeFound("nombreCompleto.equals=" + UPDATED_NOMBRE_COMPLETO);
    }

    @Test
    @Transactional
    void getAllAlumnosByNombreCompletoIsInShouldWork() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where nombreCompleto in DEFAULT_NOMBRE_COMPLETO or UPDATED_NOMBRE_COMPLETO
        defaultAlumnosShouldBeFound("nombreCompleto.in=" + DEFAULT_NOMBRE_COMPLETO + "," + UPDATED_NOMBRE_COMPLETO);

        // Get all the alumnosList where nombreCompleto equals to UPDATED_NOMBRE_COMPLETO
        defaultAlumnosShouldNotBeFound("nombreCompleto.in=" + UPDATED_NOMBRE_COMPLETO);
    }

    @Test
    @Transactional
    void getAllAlumnosByNombreCompletoIsNullOrNotNull() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where nombreCompleto is not null
        defaultAlumnosShouldBeFound("nombreCompleto.specified=true");

        // Get all the alumnosList where nombreCompleto is null
        defaultAlumnosShouldNotBeFound("nombreCompleto.specified=false");
    }

    @Test
    @Transactional
    void getAllAlumnosByNombreCompletoContainsSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where nombreCompleto contains DEFAULT_NOMBRE_COMPLETO
        defaultAlumnosShouldBeFound("nombreCompleto.contains=" + DEFAULT_NOMBRE_COMPLETO);

        // Get all the alumnosList where nombreCompleto contains UPDATED_NOMBRE_COMPLETO
        defaultAlumnosShouldNotBeFound("nombreCompleto.contains=" + UPDATED_NOMBRE_COMPLETO);
    }

    @Test
    @Transactional
    void getAllAlumnosByNombreCompletoNotContainsSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where nombreCompleto does not contain DEFAULT_NOMBRE_COMPLETO
        defaultAlumnosShouldNotBeFound("nombreCompleto.doesNotContain=" + DEFAULT_NOMBRE_COMPLETO);

        // Get all the alumnosList where nombreCompleto does not contain UPDATED_NOMBRE_COMPLETO
        defaultAlumnosShouldBeFound("nombreCompleto.doesNotContain=" + UPDATED_NOMBRE_COMPLETO);
    }

    @Test
    @Transactional
    void getAllAlumnosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where email equals to DEFAULT_EMAIL
        defaultAlumnosShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the alumnosList where email equals to UPDATED_EMAIL
        defaultAlumnosShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlumnosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultAlumnosShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the alumnosList where email equals to UPDATED_EMAIL
        defaultAlumnosShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlumnosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where email is not null
        defaultAlumnosShouldBeFound("email.specified=true");

        // Get all the alumnosList where email is null
        defaultAlumnosShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllAlumnosByEmailContainsSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where email contains DEFAULT_EMAIL
        defaultAlumnosShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the alumnosList where email contains UPDATED_EMAIL
        defaultAlumnosShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlumnosByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where email does not contain DEFAULT_EMAIL
        defaultAlumnosShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the alumnosList where email does not contain UPDATED_EMAIL
        defaultAlumnosShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlumnosByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where telefono equals to DEFAULT_TELEFONO
        defaultAlumnosShouldBeFound("telefono.equals=" + DEFAULT_TELEFONO);

        // Get all the alumnosList where telefono equals to UPDATED_TELEFONO
        defaultAlumnosShouldNotBeFound("telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllAlumnosByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where telefono in DEFAULT_TELEFONO or UPDATED_TELEFONO
        defaultAlumnosShouldBeFound("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO);

        // Get all the alumnosList where telefono equals to UPDATED_TELEFONO
        defaultAlumnosShouldNotBeFound("telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllAlumnosByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where telefono is not null
        defaultAlumnosShouldBeFound("telefono.specified=true");

        // Get all the alumnosList where telefono is null
        defaultAlumnosShouldNotBeFound("telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllAlumnosByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where telefono contains DEFAULT_TELEFONO
        defaultAlumnosShouldBeFound("telefono.contains=" + DEFAULT_TELEFONO);

        // Get all the alumnosList where telefono contains UPDATED_TELEFONO
        defaultAlumnosShouldNotBeFound("telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllAlumnosByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where telefono does not contain DEFAULT_TELEFONO
        defaultAlumnosShouldNotBeFound("telefono.doesNotContain=" + DEFAULT_TELEFONO);

        // Get all the alumnosList where telefono does not contain UPDATED_TELEFONO
        defaultAlumnosShouldBeFound("telefono.doesNotContain=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllAlumnosByFechaNacimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fechaNacimiento equals to DEFAULT_FECHA_NACIMIENTO
        defaultAlumnosShouldBeFound("fechaNacimiento.equals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the alumnosList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultAlumnosShouldNotBeFound("fechaNacimiento.equals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllAlumnosByFechaNacimientoIsInShouldWork() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fechaNacimiento in DEFAULT_FECHA_NACIMIENTO or UPDATED_FECHA_NACIMIENTO
        defaultAlumnosShouldBeFound("fechaNacimiento.in=" + DEFAULT_FECHA_NACIMIENTO + "," + UPDATED_FECHA_NACIMIENTO);

        // Get all the alumnosList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultAlumnosShouldNotBeFound("fechaNacimiento.in=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllAlumnosByFechaNacimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fechaNacimiento is not null
        defaultAlumnosShouldBeFound("fechaNacimiento.specified=true");

        // Get all the alumnosList where fechaNacimiento is null
        defaultAlumnosShouldNotBeFound("fechaNacimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllAlumnosByFechaNacimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fechaNacimiento is greater than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultAlumnosShouldBeFound("fechaNacimiento.greaterThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the alumnosList where fechaNacimiento is greater than or equal to UPDATED_FECHA_NACIMIENTO
        defaultAlumnosShouldNotBeFound("fechaNacimiento.greaterThanOrEqual=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllAlumnosByFechaNacimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fechaNacimiento is less than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultAlumnosShouldBeFound("fechaNacimiento.lessThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the alumnosList where fechaNacimiento is less than or equal to SMALLER_FECHA_NACIMIENTO
        defaultAlumnosShouldNotBeFound("fechaNacimiento.lessThanOrEqual=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllAlumnosByFechaNacimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fechaNacimiento is less than DEFAULT_FECHA_NACIMIENTO
        defaultAlumnosShouldNotBeFound("fechaNacimiento.lessThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the alumnosList where fechaNacimiento is less than UPDATED_FECHA_NACIMIENTO
        defaultAlumnosShouldBeFound("fechaNacimiento.lessThan=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllAlumnosByFechaNacimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where fechaNacimiento is greater than DEFAULT_FECHA_NACIMIENTO
        defaultAlumnosShouldNotBeFound("fechaNacimiento.greaterThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the alumnosList where fechaNacimiento is greater than SMALLER_FECHA_NACIMIENTO
        defaultAlumnosShouldBeFound("fechaNacimiento.greaterThan=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllAlumnosByDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where documento equals to DEFAULT_DOCUMENTO
        defaultAlumnosShouldBeFound("documento.equals=" + DEFAULT_DOCUMENTO);

        // Get all the alumnosList where documento equals to UPDATED_DOCUMENTO
        defaultAlumnosShouldNotBeFound("documento.equals=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllAlumnosByDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where documento in DEFAULT_DOCUMENTO or UPDATED_DOCUMENTO
        defaultAlumnosShouldBeFound("documento.in=" + DEFAULT_DOCUMENTO + "," + UPDATED_DOCUMENTO);

        // Get all the alumnosList where documento equals to UPDATED_DOCUMENTO
        defaultAlumnosShouldNotBeFound("documento.in=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllAlumnosByDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where documento is not null
        defaultAlumnosShouldBeFound("documento.specified=true");

        // Get all the alumnosList where documento is null
        defaultAlumnosShouldNotBeFound("documento.specified=false");
    }

    @Test
    @Transactional
    void getAllAlumnosByDocumentoContainsSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where documento contains DEFAULT_DOCUMENTO
        defaultAlumnosShouldBeFound("documento.contains=" + DEFAULT_DOCUMENTO);

        // Get all the alumnosList where documento contains UPDATED_DOCUMENTO
        defaultAlumnosShouldNotBeFound("documento.contains=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllAlumnosByDocumentoNotContainsSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where documento does not contain DEFAULT_DOCUMENTO
        defaultAlumnosShouldNotBeFound("documento.doesNotContain=" + DEFAULT_DOCUMENTO);

        // Get all the alumnosList where documento does not contain UPDATED_DOCUMENTO
        defaultAlumnosShouldBeFound("documento.doesNotContain=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllAlumnosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where estado equals to DEFAULT_ESTADO
        defaultAlumnosShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the alumnosList where estado equals to UPDATED_ESTADO
        defaultAlumnosShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllAlumnosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultAlumnosShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the alumnosList where estado equals to UPDATED_ESTADO
        defaultAlumnosShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllAlumnosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        // Get all the alumnosList where estado is not null
        defaultAlumnosShouldBeFound("estado.specified=true");

        // Get all the alumnosList where estado is null
        defaultAlumnosShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllAlumnosByInscripcionesIsEqualToSomething() throws Exception {
        Inscripciones inscripciones;
        if (TestUtil.findAll(em, Inscripciones.class).isEmpty()) {
            alumnosRepository.saveAndFlush(alumnos);
            inscripciones = InscripcionesResourceIT.createEntity(em);
        } else {
            inscripciones = TestUtil.findAll(em, Inscripciones.class).get(0);
        }
        em.persist(inscripciones);
        em.flush();
        alumnos.addInscripciones(inscripciones);
        alumnosRepository.saveAndFlush(alumnos);
        Long inscripcionesId = inscripciones.getId();

        // Get all the alumnosList where inscripciones equals to inscripcionesId
        defaultAlumnosShouldBeFound("inscripcionesId.equals=" + inscripcionesId);

        // Get all the alumnosList where inscripciones equals to (inscripcionesId + 1)
        defaultAlumnosShouldNotBeFound("inscripcionesId.equals=" + (inscripcionesId + 1));
    }

    @Test
    @Transactional
    void getAllAlumnosByEvaluacionesIsEqualToSomething() throws Exception {
        Evaluaciones evaluaciones;
        if (TestUtil.findAll(em, Evaluaciones.class).isEmpty()) {
            alumnosRepository.saveAndFlush(alumnos);
            evaluaciones = EvaluacionesResourceIT.createEntity(em);
        } else {
            evaluaciones = TestUtil.findAll(em, Evaluaciones.class).get(0);
        }
        em.persist(evaluaciones);
        em.flush();
        alumnos.addEvaluaciones(evaluaciones);
        alumnosRepository.saveAndFlush(alumnos);
        Long evaluacionesId = evaluaciones.getId();

        // Get all the alumnosList where evaluaciones equals to evaluacionesId
        defaultAlumnosShouldBeFound("evaluacionesId.equals=" + evaluacionesId);

        // Get all the alumnosList where evaluaciones equals to (evaluacionesId + 1)
        defaultAlumnosShouldNotBeFound("evaluacionesId.equals=" + (evaluacionesId + 1));
    }

    @Test
    @Transactional
    void getAllAlumnosByMatriculaIsEqualToSomething() throws Exception {
        Matricula matricula;
        if (TestUtil.findAll(em, Matricula.class).isEmpty()) {
            alumnosRepository.saveAndFlush(alumnos);
            matricula = MatriculaResourceIT.createEntity(em);
        } else {
            matricula = TestUtil.findAll(em, Matricula.class).get(0);
        }
        em.persist(matricula);
        em.flush();
        alumnos.addMatricula(matricula);
        alumnosRepository.saveAndFlush(alumnos);
        Long matriculaId = matricula.getId();

        // Get all the alumnosList where matricula equals to matriculaId
        defaultAlumnosShouldBeFound("matriculaId.equals=" + matriculaId);

        // Get all the alumnosList where matricula equals to (matriculaId + 1)
        defaultAlumnosShouldNotBeFound("matriculaId.equals=" + (matriculaId + 1));
    }

    @Test
    @Transactional
    void getAllAlumnosByPrestamosIsEqualToSomething() throws Exception {
        Prestamos prestamos;
        if (TestUtil.findAll(em, Prestamos.class).isEmpty()) {
            alumnosRepository.saveAndFlush(alumnos);
            prestamos = PrestamosResourceIT.createEntity(em);
        } else {
            prestamos = TestUtil.findAll(em, Prestamos.class).get(0);
        }
        em.persist(prestamos);
        em.flush();
        alumnos.addPrestamos(prestamos);
        alumnosRepository.saveAndFlush(alumnos);
        Long prestamosId = prestamos.getId();

        // Get all the alumnosList where prestamos equals to prestamosId
        defaultAlumnosShouldBeFound("prestamosId.equals=" + prestamosId);

        // Get all the alumnosList where prestamos equals to (prestamosId + 1)
        defaultAlumnosShouldNotBeFound("prestamosId.equals=" + (prestamosId + 1));
    }

    @Test
    @Transactional
    void getAllAlumnosByRegistroClasesIsEqualToSomething() throws Exception {
        RegistroClases registroClases;
        if (TestUtil.findAll(em, RegistroClases.class).isEmpty()) {
            alumnosRepository.saveAndFlush(alumnos);
            registroClases = RegistroClasesResourceIT.createEntity(em);
        } else {
            registroClases = TestUtil.findAll(em, RegistroClases.class).get(0);
        }
        em.persist(registroClases);
        em.flush();
        alumnos.addRegistroClases(registroClases);
        alumnosRepository.saveAndFlush(alumnos);
        Long registroClasesId = registroClases.getId();

        // Get all the alumnosList where registroClases equals to registroClasesId
        defaultAlumnosShouldBeFound("registroClasesId.equals=" + registroClasesId);

        // Get all the alumnosList where registroClases equals to (registroClasesId + 1)
        defaultAlumnosShouldNotBeFound("registroClasesId.equals=" + (registroClasesId + 1));
    }

    @Test
    @Transactional
    void getAllAlumnosByTipoDocumentosIsEqualToSomething() throws Exception {
        TiposDocumentos tipoDocumentos;
        if (TestUtil.findAll(em, TiposDocumentos.class).isEmpty()) {
            alumnosRepository.saveAndFlush(alumnos);
            tipoDocumentos = TiposDocumentosResourceIT.createEntity(em);
        } else {
            tipoDocumentos = TestUtil.findAll(em, TiposDocumentos.class).get(0);
        }
        em.persist(tipoDocumentos);
        em.flush();
        alumnos.setTipoDocumentos(tipoDocumentos);
        alumnosRepository.saveAndFlush(alumnos);
        Long tipoDocumentosId = tipoDocumentos.getId();

        // Get all the alumnosList where tipoDocumentos equals to tipoDocumentosId
        defaultAlumnosShouldBeFound("tipoDocumentosId.equals=" + tipoDocumentosId);

        // Get all the alumnosList where tipoDocumentos equals to (tipoDocumentosId + 1)
        defaultAlumnosShouldNotBeFound("tipoDocumentosId.equals=" + (tipoDocumentosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlumnosShouldBeFound(String filter) throws Exception {
        restAlumnosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alumnos.getId().intValue())))
            .andExpect(jsonPath("$.[*].elo").value(hasItem(DEFAULT_ELO)))
            .andExpect(jsonPath("$.[*].fideId").value(hasItem(DEFAULT_FIDE_ID)))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].nombreCompleto").value(hasItem(DEFAULT_NOMBRE_COMPLETO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));

        // Check, that the count call also returns 1
        restAlumnosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlumnosShouldNotBeFound(String filter) throws Exception {
        restAlumnosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlumnosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlumnos() throws Exception {
        // Get the alumnos
        restAlumnosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlumnos() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        int databaseSizeBeforeUpdate = alumnosRepository.findAll().size();

        // Update the alumnos
        Alumnos updatedAlumnos = alumnosRepository.findById(alumnos.getId()).get();
        // Disconnect from session so that the updates on updatedAlumnos are not directly saved in db
        em.detach(updatedAlumnos);
        updatedAlumnos
            .elo(UPDATED_ELO)
            .fideId(UPDATED_FIDE_ID)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .nombreCompleto(UPDATED_NOMBRE_COMPLETO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .documento(UPDATED_DOCUMENTO)
            .estado(UPDATED_ESTADO);

        restAlumnosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlumnos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAlumnos))
            )
            .andExpect(status().isOk());

        // Validate the Alumnos in the database
        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeUpdate);
        Alumnos testAlumnos = alumnosList.get(alumnosList.size() - 1);
        assertThat(testAlumnos.getElo()).isEqualTo(UPDATED_ELO);
        assertThat(testAlumnos.getFideId()).isEqualTo(UPDATED_FIDE_ID);
        assertThat(testAlumnos.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testAlumnos.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testAlumnos.getNombreCompleto()).isEqualTo(UPDATED_NOMBRE_COMPLETO);
        assertThat(testAlumnos.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAlumnos.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testAlumnos.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testAlumnos.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testAlumnos.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingAlumnos() throws Exception {
        int databaseSizeBeforeUpdate = alumnosRepository.findAll().size();
        alumnos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlumnosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alumnos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alumnos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumnos in the database
        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlumnos() throws Exception {
        int databaseSizeBeforeUpdate = alumnosRepository.findAll().size();
        alumnos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alumnos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumnos in the database
        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlumnos() throws Exception {
        int databaseSizeBeforeUpdate = alumnosRepository.findAll().size();
        alumnos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumnos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alumnos in the database
        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlumnosWithPatch() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        int databaseSizeBeforeUpdate = alumnosRepository.findAll().size();

        // Update the alumnos using partial update
        Alumnos partialUpdatedAlumnos = new Alumnos();
        partialUpdatedAlumnos.setId(alumnos.getId());

        partialUpdatedAlumnos
            .fideId(UPDATED_FIDE_ID)
            .nombreCompleto(UPDATED_NOMBRE_COMPLETO)
            .email(UPDATED_EMAIL)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .estado(UPDATED_ESTADO);

        restAlumnosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlumnos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlumnos))
            )
            .andExpect(status().isOk());

        // Validate the Alumnos in the database
        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeUpdate);
        Alumnos testAlumnos = alumnosList.get(alumnosList.size() - 1);
        assertThat(testAlumnos.getElo()).isEqualTo(DEFAULT_ELO);
        assertThat(testAlumnos.getFideId()).isEqualTo(UPDATED_FIDE_ID);
        assertThat(testAlumnos.getNombres()).isEqualTo(DEFAULT_NOMBRES);
        assertThat(testAlumnos.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testAlumnos.getNombreCompleto()).isEqualTo(UPDATED_NOMBRE_COMPLETO);
        assertThat(testAlumnos.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAlumnos.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testAlumnos.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testAlumnos.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
        assertThat(testAlumnos.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateAlumnosWithPatch() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        int databaseSizeBeforeUpdate = alumnosRepository.findAll().size();

        // Update the alumnos using partial update
        Alumnos partialUpdatedAlumnos = new Alumnos();
        partialUpdatedAlumnos.setId(alumnos.getId());

        partialUpdatedAlumnos
            .elo(UPDATED_ELO)
            .fideId(UPDATED_FIDE_ID)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .nombreCompleto(UPDATED_NOMBRE_COMPLETO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .documento(UPDATED_DOCUMENTO)
            .estado(UPDATED_ESTADO);

        restAlumnosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlumnos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlumnos))
            )
            .andExpect(status().isOk());

        // Validate the Alumnos in the database
        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeUpdate);
        Alumnos testAlumnos = alumnosList.get(alumnosList.size() - 1);
        assertThat(testAlumnos.getElo()).isEqualTo(UPDATED_ELO);
        assertThat(testAlumnos.getFideId()).isEqualTo(UPDATED_FIDE_ID);
        assertThat(testAlumnos.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testAlumnos.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testAlumnos.getNombreCompleto()).isEqualTo(UPDATED_NOMBRE_COMPLETO);
        assertThat(testAlumnos.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAlumnos.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testAlumnos.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testAlumnos.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testAlumnos.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingAlumnos() throws Exception {
        int databaseSizeBeforeUpdate = alumnosRepository.findAll().size();
        alumnos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlumnosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alumnos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alumnos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumnos in the database
        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlumnos() throws Exception {
        int databaseSizeBeforeUpdate = alumnosRepository.findAll().size();
        alumnos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alumnos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumnos in the database
        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlumnos() throws Exception {
        int databaseSizeBeforeUpdate = alumnosRepository.findAll().size();
        alumnos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(alumnos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alumnos in the database
        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlumnos() throws Exception {
        // Initialize the database
        alumnosRepository.saveAndFlush(alumnos);

        int databaseSizeBeforeDelete = alumnosRepository.findAll().size();

        // Delete the alumnos
        restAlumnosMockMvc
            .perform(delete(ENTITY_API_URL_ID, alumnos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Alumnos> alumnosList = alumnosRepository.findAll();
        assertThat(alumnosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
