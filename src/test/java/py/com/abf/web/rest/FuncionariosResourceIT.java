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
import py.com.abf.domain.Evaluaciones;
import py.com.abf.domain.Funcionarios;
import py.com.abf.domain.Pagos;
import py.com.abf.domain.RegistroClases;
import py.com.abf.domain.TiposDocumentos;
import py.com.abf.domain.enumeration.EstadosPersona;
import py.com.abf.domain.enumeration.TipoFuncionarios;
import py.com.abf.repository.FuncionariosRepository;
import py.com.abf.service.FuncionariosService;
import py.com.abf.service.criteria.FuncionariosCriteria;

/**
 * Integration tests for the {@link FuncionariosResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FuncionariosResourceIT {

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

    private static final TipoFuncionarios DEFAULT_TIPO_FUNCIONARIO = TipoFuncionarios.PROFESORES;
    private static final TipoFuncionarios UPDATED_TIPO_FUNCIONARIO = TipoFuncionarios.FUNCIONARIOS;

    private static final String ENTITY_API_URL = "/api/funcionarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FuncionariosRepository funcionariosRepository;

    @Mock
    private FuncionariosRepository funcionariosRepositoryMock;

    @Mock
    private FuncionariosService funcionariosServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFuncionariosMockMvc;

    private Funcionarios funcionarios;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funcionarios createEntity(EntityManager em) {
        Funcionarios funcionarios = new Funcionarios()
            .elo(DEFAULT_ELO)
            .fideId(DEFAULT_FIDE_ID)
            .nombres(DEFAULT_NOMBRES)
            .apellidos(DEFAULT_APELLIDOS)
            .nombreCompleto(DEFAULT_NOMBRE_COMPLETO)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .documento(DEFAULT_DOCUMENTO)
            .estado(DEFAULT_ESTADO)
            .tipoFuncionario(DEFAULT_TIPO_FUNCIONARIO);
        // Add required entity
        TiposDocumentos tiposDocumentos;
        if (TestUtil.findAll(em, TiposDocumentos.class).isEmpty()) {
            tiposDocumentos = TiposDocumentosResourceIT.createEntity(em);
            em.persist(tiposDocumentos);
            em.flush();
        } else {
            tiposDocumentos = TestUtil.findAll(em, TiposDocumentos.class).get(0);
        }
        funcionarios.setTipoDocumentos(tiposDocumentos);
        return funcionarios;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funcionarios createUpdatedEntity(EntityManager em) {
        Funcionarios funcionarios = new Funcionarios()
            .elo(UPDATED_ELO)
            .fideId(UPDATED_FIDE_ID)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .nombreCompleto(UPDATED_NOMBRE_COMPLETO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .documento(UPDATED_DOCUMENTO)
            .estado(UPDATED_ESTADO)
            .tipoFuncionario(UPDATED_TIPO_FUNCIONARIO);
        // Add required entity
        TiposDocumentos tiposDocumentos;
        if (TestUtil.findAll(em, TiposDocumentos.class).isEmpty()) {
            tiposDocumentos = TiposDocumentosResourceIT.createUpdatedEntity(em);
            em.persist(tiposDocumentos);
            em.flush();
        } else {
            tiposDocumentos = TestUtil.findAll(em, TiposDocumentos.class).get(0);
        }
        funcionarios.setTipoDocumentos(tiposDocumentos);
        return funcionarios;
    }

    @BeforeEach
    public void initTest() {
        funcionarios = createEntity(em);
    }

    @Test
    @Transactional
    void createFuncionarios() throws Exception {
        int databaseSizeBeforeCreate = funcionariosRepository.findAll().size();
        // Create the Funcionarios
        restFuncionariosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarios)))
            .andExpect(status().isCreated());

        // Validate the Funcionarios in the database
        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeCreate + 1);
        Funcionarios testFuncionarios = funcionariosList.get(funcionariosList.size() - 1);
        assertThat(testFuncionarios.getElo()).isEqualTo(DEFAULT_ELO);
        assertThat(testFuncionarios.getFideId()).isEqualTo(DEFAULT_FIDE_ID);
        assertThat(testFuncionarios.getNombres()).isEqualTo(DEFAULT_NOMBRES);
        assertThat(testFuncionarios.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testFuncionarios.getNombreCompleto()).isEqualTo(DEFAULT_NOMBRE_COMPLETO);
        assertThat(testFuncionarios.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFuncionarios.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testFuncionarios.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testFuncionarios.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
        assertThat(testFuncionarios.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testFuncionarios.getTipoFuncionario()).isEqualTo(DEFAULT_TIPO_FUNCIONARIO);
    }

    @Test
    @Transactional
    void createFuncionariosWithExistingId() throws Exception {
        // Create the Funcionarios with an existing ID
        funcionarios.setId(1L);

        int databaseSizeBeforeCreate = funcionariosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFuncionariosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarios)))
            .andExpect(status().isBadRequest());

        // Validate the Funcionarios in the database
        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombresIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionariosRepository.findAll().size();
        // set the field null
        funcionarios.setNombres(null);

        // Create the Funcionarios, which fails.

        restFuncionariosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarios)))
            .andExpect(status().isBadRequest());

        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApellidosIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionariosRepository.findAll().size();
        // set the field null
        funcionarios.setApellidos(null);

        // Create the Funcionarios, which fails.

        restFuncionariosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarios)))
            .andExpect(status().isBadRequest());

        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreCompletoIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionariosRepository.findAll().size();
        // set the field null
        funcionarios.setNombreCompleto(null);

        // Create the Funcionarios, which fails.

        restFuncionariosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarios)))
            .andExpect(status().isBadRequest());

        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionariosRepository.findAll().size();
        // set the field null
        funcionarios.setEmail(null);

        // Create the Funcionarios, which fails.

        restFuncionariosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarios)))
            .andExpect(status().isBadRequest());

        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionariosRepository.findAll().size();
        // set the field null
        funcionarios.setTelefono(null);

        // Create the Funcionarios, which fails.

        restFuncionariosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarios)))
            .andExpect(status().isBadRequest());

        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaNacimientoIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionariosRepository.findAll().size();
        // set the field null
        funcionarios.setFechaNacimiento(null);

        // Create the Funcionarios, which fails.

        restFuncionariosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarios)))
            .andExpect(status().isBadRequest());

        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionariosRepository.findAll().size();
        // set the field null
        funcionarios.setDocumento(null);

        // Create the Funcionarios, which fails.

        restFuncionariosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarios)))
            .andExpect(status().isBadRequest());

        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionariosRepository.findAll().size();
        // set the field null
        funcionarios.setEstado(null);

        // Create the Funcionarios, which fails.

        restFuncionariosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarios)))
            .andExpect(status().isBadRequest());

        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFuncionarios() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList
        restFuncionariosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funcionarios.getId().intValue())))
            .andExpect(jsonPath("$.[*].elo").value(hasItem(DEFAULT_ELO)))
            .andExpect(jsonPath("$.[*].fideId").value(hasItem(DEFAULT_FIDE_ID)))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].nombreCompleto").value(hasItem(DEFAULT_NOMBRE_COMPLETO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].tipoFuncionario").value(hasItem(DEFAULT_TIPO_FUNCIONARIO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFuncionariosWithEagerRelationshipsIsEnabled() throws Exception {
        when(funcionariosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFuncionariosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(funcionariosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFuncionariosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(funcionariosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFuncionariosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(funcionariosRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFuncionarios() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get the funcionarios
        restFuncionariosMockMvc
            .perform(get(ENTITY_API_URL_ID, funcionarios.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(funcionarios.getId().intValue()))
            .andExpect(jsonPath("$.elo").value(DEFAULT_ELO))
            .andExpect(jsonPath("$.fideId").value(DEFAULT_FIDE_ID))
            .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.nombreCompleto").value(DEFAULT_NOMBRE_COMPLETO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.documento").value(DEFAULT_DOCUMENTO))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.tipoFuncionario").value(DEFAULT_TIPO_FUNCIONARIO.toString()));
    }

    @Test
    @Transactional
    void getFuncionariosByIdFiltering() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        Long id = funcionarios.getId();

        defaultFuncionariosShouldBeFound("id.equals=" + id);
        defaultFuncionariosShouldNotBeFound("id.notEquals=" + id);

        defaultFuncionariosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFuncionariosShouldNotBeFound("id.greaterThan=" + id);

        defaultFuncionariosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFuncionariosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFuncionariosByEloIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where elo equals to DEFAULT_ELO
        defaultFuncionariosShouldBeFound("elo.equals=" + DEFAULT_ELO);

        // Get all the funcionariosList where elo equals to UPDATED_ELO
        defaultFuncionariosShouldNotBeFound("elo.equals=" + UPDATED_ELO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByEloIsInShouldWork() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where elo in DEFAULT_ELO or UPDATED_ELO
        defaultFuncionariosShouldBeFound("elo.in=" + DEFAULT_ELO + "," + UPDATED_ELO);

        // Get all the funcionariosList where elo equals to UPDATED_ELO
        defaultFuncionariosShouldNotBeFound("elo.in=" + UPDATED_ELO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByEloIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where elo is not null
        defaultFuncionariosShouldBeFound("elo.specified=true");

        // Get all the funcionariosList where elo is null
        defaultFuncionariosShouldNotBeFound("elo.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByEloIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where elo is greater than or equal to DEFAULT_ELO
        defaultFuncionariosShouldBeFound("elo.greaterThanOrEqual=" + DEFAULT_ELO);

        // Get all the funcionariosList where elo is greater than or equal to UPDATED_ELO
        defaultFuncionariosShouldNotBeFound("elo.greaterThanOrEqual=" + UPDATED_ELO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByEloIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where elo is less than or equal to DEFAULT_ELO
        defaultFuncionariosShouldBeFound("elo.lessThanOrEqual=" + DEFAULT_ELO);

        // Get all the funcionariosList where elo is less than or equal to SMALLER_ELO
        defaultFuncionariosShouldNotBeFound("elo.lessThanOrEqual=" + SMALLER_ELO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByEloIsLessThanSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where elo is less than DEFAULT_ELO
        defaultFuncionariosShouldNotBeFound("elo.lessThan=" + DEFAULT_ELO);

        // Get all the funcionariosList where elo is less than UPDATED_ELO
        defaultFuncionariosShouldBeFound("elo.lessThan=" + UPDATED_ELO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByEloIsGreaterThanSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where elo is greater than DEFAULT_ELO
        defaultFuncionariosShouldNotBeFound("elo.greaterThan=" + DEFAULT_ELO);

        // Get all the funcionariosList where elo is greater than SMALLER_ELO
        defaultFuncionariosShouldBeFound("elo.greaterThan=" + SMALLER_ELO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByFideIdIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fideId equals to DEFAULT_FIDE_ID
        defaultFuncionariosShouldBeFound("fideId.equals=" + DEFAULT_FIDE_ID);

        // Get all the funcionariosList where fideId equals to UPDATED_FIDE_ID
        defaultFuncionariosShouldNotBeFound("fideId.equals=" + UPDATED_FIDE_ID);
    }

    @Test
    @Transactional
    void getAllFuncionariosByFideIdIsInShouldWork() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fideId in DEFAULT_FIDE_ID or UPDATED_FIDE_ID
        defaultFuncionariosShouldBeFound("fideId.in=" + DEFAULT_FIDE_ID + "," + UPDATED_FIDE_ID);

        // Get all the funcionariosList where fideId equals to UPDATED_FIDE_ID
        defaultFuncionariosShouldNotBeFound("fideId.in=" + UPDATED_FIDE_ID);
    }

    @Test
    @Transactional
    void getAllFuncionariosByFideIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fideId is not null
        defaultFuncionariosShouldBeFound("fideId.specified=true");

        // Get all the funcionariosList where fideId is null
        defaultFuncionariosShouldNotBeFound("fideId.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByFideIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fideId is greater than or equal to DEFAULT_FIDE_ID
        defaultFuncionariosShouldBeFound("fideId.greaterThanOrEqual=" + DEFAULT_FIDE_ID);

        // Get all the funcionariosList where fideId is greater than or equal to UPDATED_FIDE_ID
        defaultFuncionariosShouldNotBeFound("fideId.greaterThanOrEqual=" + UPDATED_FIDE_ID);
    }

    @Test
    @Transactional
    void getAllFuncionariosByFideIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fideId is less than or equal to DEFAULT_FIDE_ID
        defaultFuncionariosShouldBeFound("fideId.lessThanOrEqual=" + DEFAULT_FIDE_ID);

        // Get all the funcionariosList where fideId is less than or equal to SMALLER_FIDE_ID
        defaultFuncionariosShouldNotBeFound("fideId.lessThanOrEqual=" + SMALLER_FIDE_ID);
    }

    @Test
    @Transactional
    void getAllFuncionariosByFideIdIsLessThanSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fideId is less than DEFAULT_FIDE_ID
        defaultFuncionariosShouldNotBeFound("fideId.lessThan=" + DEFAULT_FIDE_ID);

        // Get all the funcionariosList where fideId is less than UPDATED_FIDE_ID
        defaultFuncionariosShouldBeFound("fideId.lessThan=" + UPDATED_FIDE_ID);
    }

    @Test
    @Transactional
    void getAllFuncionariosByFideIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fideId is greater than DEFAULT_FIDE_ID
        defaultFuncionariosShouldNotBeFound("fideId.greaterThan=" + DEFAULT_FIDE_ID);

        // Get all the funcionariosList where fideId is greater than SMALLER_FIDE_ID
        defaultFuncionariosShouldBeFound("fideId.greaterThan=" + SMALLER_FIDE_ID);
    }

    @Test
    @Transactional
    void getAllFuncionariosByNombresIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where nombres equals to DEFAULT_NOMBRES
        defaultFuncionariosShouldBeFound("nombres.equals=" + DEFAULT_NOMBRES);

        // Get all the funcionariosList where nombres equals to UPDATED_NOMBRES
        defaultFuncionariosShouldNotBeFound("nombres.equals=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllFuncionariosByNombresIsInShouldWork() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where nombres in DEFAULT_NOMBRES or UPDATED_NOMBRES
        defaultFuncionariosShouldBeFound("nombres.in=" + DEFAULT_NOMBRES + "," + UPDATED_NOMBRES);

        // Get all the funcionariosList where nombres equals to UPDATED_NOMBRES
        defaultFuncionariosShouldNotBeFound("nombres.in=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllFuncionariosByNombresIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where nombres is not null
        defaultFuncionariosShouldBeFound("nombres.specified=true");

        // Get all the funcionariosList where nombres is null
        defaultFuncionariosShouldNotBeFound("nombres.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByNombresContainsSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where nombres contains DEFAULT_NOMBRES
        defaultFuncionariosShouldBeFound("nombres.contains=" + DEFAULT_NOMBRES);

        // Get all the funcionariosList where nombres contains UPDATED_NOMBRES
        defaultFuncionariosShouldNotBeFound("nombres.contains=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllFuncionariosByNombresNotContainsSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where nombres does not contain DEFAULT_NOMBRES
        defaultFuncionariosShouldNotBeFound("nombres.doesNotContain=" + DEFAULT_NOMBRES);

        // Get all the funcionariosList where nombres does not contain UPDATED_NOMBRES
        defaultFuncionariosShouldBeFound("nombres.doesNotContain=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllFuncionariosByApellidosIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where apellidos equals to DEFAULT_APELLIDOS
        defaultFuncionariosShouldBeFound("apellidos.equals=" + DEFAULT_APELLIDOS);

        // Get all the funcionariosList where apellidos equals to UPDATED_APELLIDOS
        defaultFuncionariosShouldNotBeFound("apellidos.equals=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllFuncionariosByApellidosIsInShouldWork() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where apellidos in DEFAULT_APELLIDOS or UPDATED_APELLIDOS
        defaultFuncionariosShouldBeFound("apellidos.in=" + DEFAULT_APELLIDOS + "," + UPDATED_APELLIDOS);

        // Get all the funcionariosList where apellidos equals to UPDATED_APELLIDOS
        defaultFuncionariosShouldNotBeFound("apellidos.in=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllFuncionariosByApellidosIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where apellidos is not null
        defaultFuncionariosShouldBeFound("apellidos.specified=true");

        // Get all the funcionariosList where apellidos is null
        defaultFuncionariosShouldNotBeFound("apellidos.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByApellidosContainsSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where apellidos contains DEFAULT_APELLIDOS
        defaultFuncionariosShouldBeFound("apellidos.contains=" + DEFAULT_APELLIDOS);

        // Get all the funcionariosList where apellidos contains UPDATED_APELLIDOS
        defaultFuncionariosShouldNotBeFound("apellidos.contains=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllFuncionariosByApellidosNotContainsSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where apellidos does not contain DEFAULT_APELLIDOS
        defaultFuncionariosShouldNotBeFound("apellidos.doesNotContain=" + DEFAULT_APELLIDOS);

        // Get all the funcionariosList where apellidos does not contain UPDATED_APELLIDOS
        defaultFuncionariosShouldBeFound("apellidos.doesNotContain=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllFuncionariosByNombreCompletoIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where nombreCompleto equals to DEFAULT_NOMBRE_COMPLETO
        defaultFuncionariosShouldBeFound("nombreCompleto.equals=" + DEFAULT_NOMBRE_COMPLETO);

        // Get all the funcionariosList where nombreCompleto equals to UPDATED_NOMBRE_COMPLETO
        defaultFuncionariosShouldNotBeFound("nombreCompleto.equals=" + UPDATED_NOMBRE_COMPLETO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByNombreCompletoIsInShouldWork() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where nombreCompleto in DEFAULT_NOMBRE_COMPLETO or UPDATED_NOMBRE_COMPLETO
        defaultFuncionariosShouldBeFound("nombreCompleto.in=" + DEFAULT_NOMBRE_COMPLETO + "," + UPDATED_NOMBRE_COMPLETO);

        // Get all the funcionariosList where nombreCompleto equals to UPDATED_NOMBRE_COMPLETO
        defaultFuncionariosShouldNotBeFound("nombreCompleto.in=" + UPDATED_NOMBRE_COMPLETO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByNombreCompletoIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where nombreCompleto is not null
        defaultFuncionariosShouldBeFound("nombreCompleto.specified=true");

        // Get all the funcionariosList where nombreCompleto is null
        defaultFuncionariosShouldNotBeFound("nombreCompleto.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByNombreCompletoContainsSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where nombreCompleto contains DEFAULT_NOMBRE_COMPLETO
        defaultFuncionariosShouldBeFound("nombreCompleto.contains=" + DEFAULT_NOMBRE_COMPLETO);

        // Get all the funcionariosList where nombreCompleto contains UPDATED_NOMBRE_COMPLETO
        defaultFuncionariosShouldNotBeFound("nombreCompleto.contains=" + UPDATED_NOMBRE_COMPLETO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByNombreCompletoNotContainsSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where nombreCompleto does not contain DEFAULT_NOMBRE_COMPLETO
        defaultFuncionariosShouldNotBeFound("nombreCompleto.doesNotContain=" + DEFAULT_NOMBRE_COMPLETO);

        // Get all the funcionariosList where nombreCompleto does not contain UPDATED_NOMBRE_COMPLETO
        defaultFuncionariosShouldBeFound("nombreCompleto.doesNotContain=" + UPDATED_NOMBRE_COMPLETO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where email equals to DEFAULT_EMAIL
        defaultFuncionariosShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the funcionariosList where email equals to UPDATED_EMAIL
        defaultFuncionariosShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFuncionariosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultFuncionariosShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the funcionariosList where email equals to UPDATED_EMAIL
        defaultFuncionariosShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFuncionariosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where email is not null
        defaultFuncionariosShouldBeFound("email.specified=true");

        // Get all the funcionariosList where email is null
        defaultFuncionariosShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByEmailContainsSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where email contains DEFAULT_EMAIL
        defaultFuncionariosShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the funcionariosList where email contains UPDATED_EMAIL
        defaultFuncionariosShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFuncionariosByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where email does not contain DEFAULT_EMAIL
        defaultFuncionariosShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the funcionariosList where email does not contain UPDATED_EMAIL
        defaultFuncionariosShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFuncionariosByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where telefono equals to DEFAULT_TELEFONO
        defaultFuncionariosShouldBeFound("telefono.equals=" + DEFAULT_TELEFONO);

        // Get all the funcionariosList where telefono equals to UPDATED_TELEFONO
        defaultFuncionariosShouldNotBeFound("telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where telefono in DEFAULT_TELEFONO or UPDATED_TELEFONO
        defaultFuncionariosShouldBeFound("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO);

        // Get all the funcionariosList where telefono equals to UPDATED_TELEFONO
        defaultFuncionariosShouldNotBeFound("telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where telefono is not null
        defaultFuncionariosShouldBeFound("telefono.specified=true");

        // Get all the funcionariosList where telefono is null
        defaultFuncionariosShouldNotBeFound("telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where telefono contains DEFAULT_TELEFONO
        defaultFuncionariosShouldBeFound("telefono.contains=" + DEFAULT_TELEFONO);

        // Get all the funcionariosList where telefono contains UPDATED_TELEFONO
        defaultFuncionariosShouldNotBeFound("telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where telefono does not contain DEFAULT_TELEFONO
        defaultFuncionariosShouldNotBeFound("telefono.doesNotContain=" + DEFAULT_TELEFONO);

        // Get all the funcionariosList where telefono does not contain UPDATED_TELEFONO
        defaultFuncionariosShouldBeFound("telefono.doesNotContain=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByFechaNacimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fechaNacimiento equals to DEFAULT_FECHA_NACIMIENTO
        defaultFuncionariosShouldBeFound("fechaNacimiento.equals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the funcionariosList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultFuncionariosShouldNotBeFound("fechaNacimiento.equals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByFechaNacimientoIsInShouldWork() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fechaNacimiento in DEFAULT_FECHA_NACIMIENTO or UPDATED_FECHA_NACIMIENTO
        defaultFuncionariosShouldBeFound("fechaNacimiento.in=" + DEFAULT_FECHA_NACIMIENTO + "," + UPDATED_FECHA_NACIMIENTO);

        // Get all the funcionariosList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultFuncionariosShouldNotBeFound("fechaNacimiento.in=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByFechaNacimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fechaNacimiento is not null
        defaultFuncionariosShouldBeFound("fechaNacimiento.specified=true");

        // Get all the funcionariosList where fechaNacimiento is null
        defaultFuncionariosShouldNotBeFound("fechaNacimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByFechaNacimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fechaNacimiento is greater than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultFuncionariosShouldBeFound("fechaNacimiento.greaterThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the funcionariosList where fechaNacimiento is greater than or equal to UPDATED_FECHA_NACIMIENTO
        defaultFuncionariosShouldNotBeFound("fechaNacimiento.greaterThanOrEqual=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByFechaNacimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fechaNacimiento is less than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultFuncionariosShouldBeFound("fechaNacimiento.lessThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the funcionariosList where fechaNacimiento is less than or equal to SMALLER_FECHA_NACIMIENTO
        defaultFuncionariosShouldNotBeFound("fechaNacimiento.lessThanOrEqual=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByFechaNacimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fechaNacimiento is less than DEFAULT_FECHA_NACIMIENTO
        defaultFuncionariosShouldNotBeFound("fechaNacimiento.lessThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the funcionariosList where fechaNacimiento is less than UPDATED_FECHA_NACIMIENTO
        defaultFuncionariosShouldBeFound("fechaNacimiento.lessThan=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByFechaNacimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where fechaNacimiento is greater than DEFAULT_FECHA_NACIMIENTO
        defaultFuncionariosShouldNotBeFound("fechaNacimiento.greaterThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the funcionariosList where fechaNacimiento is greater than SMALLER_FECHA_NACIMIENTO
        defaultFuncionariosShouldBeFound("fechaNacimiento.greaterThan=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where documento equals to DEFAULT_DOCUMENTO
        defaultFuncionariosShouldBeFound("documento.equals=" + DEFAULT_DOCUMENTO);

        // Get all the funcionariosList where documento equals to UPDATED_DOCUMENTO
        defaultFuncionariosShouldNotBeFound("documento.equals=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where documento in DEFAULT_DOCUMENTO or UPDATED_DOCUMENTO
        defaultFuncionariosShouldBeFound("documento.in=" + DEFAULT_DOCUMENTO + "," + UPDATED_DOCUMENTO);

        // Get all the funcionariosList where documento equals to UPDATED_DOCUMENTO
        defaultFuncionariosShouldNotBeFound("documento.in=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where documento is not null
        defaultFuncionariosShouldBeFound("documento.specified=true");

        // Get all the funcionariosList where documento is null
        defaultFuncionariosShouldNotBeFound("documento.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByDocumentoContainsSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where documento contains DEFAULT_DOCUMENTO
        defaultFuncionariosShouldBeFound("documento.contains=" + DEFAULT_DOCUMENTO);

        // Get all the funcionariosList where documento contains UPDATED_DOCUMENTO
        defaultFuncionariosShouldNotBeFound("documento.contains=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByDocumentoNotContainsSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where documento does not contain DEFAULT_DOCUMENTO
        defaultFuncionariosShouldNotBeFound("documento.doesNotContain=" + DEFAULT_DOCUMENTO);

        // Get all the funcionariosList where documento does not contain UPDATED_DOCUMENTO
        defaultFuncionariosShouldBeFound("documento.doesNotContain=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where estado equals to DEFAULT_ESTADO
        defaultFuncionariosShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the funcionariosList where estado equals to UPDATED_ESTADO
        defaultFuncionariosShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultFuncionariosShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the funcionariosList where estado equals to UPDATED_ESTADO
        defaultFuncionariosShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where estado is not null
        defaultFuncionariosShouldBeFound("estado.specified=true");

        // Get all the funcionariosList where estado is null
        defaultFuncionariosShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByTipoFuncionarioIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where tipoFuncionario equals to DEFAULT_TIPO_FUNCIONARIO
        defaultFuncionariosShouldBeFound("tipoFuncionario.equals=" + DEFAULT_TIPO_FUNCIONARIO);

        // Get all the funcionariosList where tipoFuncionario equals to UPDATED_TIPO_FUNCIONARIO
        defaultFuncionariosShouldNotBeFound("tipoFuncionario.equals=" + UPDATED_TIPO_FUNCIONARIO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByTipoFuncionarioIsInShouldWork() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where tipoFuncionario in DEFAULT_TIPO_FUNCIONARIO or UPDATED_TIPO_FUNCIONARIO
        defaultFuncionariosShouldBeFound("tipoFuncionario.in=" + DEFAULT_TIPO_FUNCIONARIO + "," + UPDATED_TIPO_FUNCIONARIO);

        // Get all the funcionariosList where tipoFuncionario equals to UPDATED_TIPO_FUNCIONARIO
        defaultFuncionariosShouldNotBeFound("tipoFuncionario.in=" + UPDATED_TIPO_FUNCIONARIO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByTipoFuncionarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        // Get all the funcionariosList where tipoFuncionario is not null
        defaultFuncionariosShouldBeFound("tipoFuncionario.specified=true");

        // Get all the funcionariosList where tipoFuncionario is null
        defaultFuncionariosShouldNotBeFound("tipoFuncionario.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByEvaluacionesIsEqualToSomething() throws Exception {
        Evaluaciones evaluaciones;
        if (TestUtil.findAll(em, Evaluaciones.class).isEmpty()) {
            funcionariosRepository.saveAndFlush(funcionarios);
            evaluaciones = EvaluacionesResourceIT.createEntity(em);
        } else {
            evaluaciones = TestUtil.findAll(em, Evaluaciones.class).get(0);
        }
        em.persist(evaluaciones);
        em.flush();
        funcionarios.addEvaluaciones(evaluaciones);
        funcionariosRepository.saveAndFlush(funcionarios);
        Long evaluacionesId = evaluaciones.getId();

        // Get all the funcionariosList where evaluaciones equals to evaluacionesId
        defaultFuncionariosShouldBeFound("evaluacionesId.equals=" + evaluacionesId);

        // Get all the funcionariosList where evaluaciones equals to (evaluacionesId + 1)
        defaultFuncionariosShouldNotBeFound("evaluacionesId.equals=" + (evaluacionesId + 1));
    }

    @Test
    @Transactional
    void getAllFuncionariosByPagosIsEqualToSomething() throws Exception {
        Pagos pagos;
        if (TestUtil.findAll(em, Pagos.class).isEmpty()) {
            funcionariosRepository.saveAndFlush(funcionarios);
            pagos = PagosResourceIT.createEntity(em);
        } else {
            pagos = TestUtil.findAll(em, Pagos.class).get(0);
        }
        em.persist(pagos);
        em.flush();
        funcionarios.addPagos(pagos);
        funcionariosRepository.saveAndFlush(funcionarios);
        Long pagosId = pagos.getId();

        // Get all the funcionariosList where pagos equals to pagosId
        defaultFuncionariosShouldBeFound("pagosId.equals=" + pagosId);

        // Get all the funcionariosList where pagos equals to (pagosId + 1)
        defaultFuncionariosShouldNotBeFound("pagosId.equals=" + (pagosId + 1));
    }

    @Test
    @Transactional
    void getAllFuncionariosByRegistroClasesIsEqualToSomething() throws Exception {
        RegistroClases registroClases;
        if (TestUtil.findAll(em, RegistroClases.class).isEmpty()) {
            funcionariosRepository.saveAndFlush(funcionarios);
            registroClases = RegistroClasesResourceIT.createEntity(em);
        } else {
            registroClases = TestUtil.findAll(em, RegistroClases.class).get(0);
        }
        em.persist(registroClases);
        em.flush();
        funcionarios.addRegistroClases(registroClases);
        funcionariosRepository.saveAndFlush(funcionarios);
        Long registroClasesId = registroClases.getId();

        // Get all the funcionariosList where registroClases equals to registroClasesId
        defaultFuncionariosShouldBeFound("registroClasesId.equals=" + registroClasesId);

        // Get all the funcionariosList where registroClases equals to (registroClasesId + 1)
        defaultFuncionariosShouldNotBeFound("registroClasesId.equals=" + (registroClasesId + 1));
    }

    @Test
    @Transactional
    void getAllFuncionariosByTipoDocumentosIsEqualToSomething() throws Exception {
        TiposDocumentos tipoDocumentos;
        if (TestUtil.findAll(em, TiposDocumentos.class).isEmpty()) {
            funcionariosRepository.saveAndFlush(funcionarios);
            tipoDocumentos = TiposDocumentosResourceIT.createEntity(em);
        } else {
            tipoDocumentos = TestUtil.findAll(em, TiposDocumentos.class).get(0);
        }
        em.persist(tipoDocumentos);
        em.flush();
        funcionarios.setTipoDocumentos(tipoDocumentos);
        funcionariosRepository.saveAndFlush(funcionarios);
        Long tipoDocumentosId = tipoDocumentos.getId();

        // Get all the funcionariosList where tipoDocumentos equals to tipoDocumentosId
        defaultFuncionariosShouldBeFound("tipoDocumentosId.equals=" + tipoDocumentosId);

        // Get all the funcionariosList where tipoDocumentos equals to (tipoDocumentosId + 1)
        defaultFuncionariosShouldNotBeFound("tipoDocumentosId.equals=" + (tipoDocumentosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFuncionariosShouldBeFound(String filter) throws Exception {
        restFuncionariosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funcionarios.getId().intValue())))
            .andExpect(jsonPath("$.[*].elo").value(hasItem(DEFAULT_ELO)))
            .andExpect(jsonPath("$.[*].fideId").value(hasItem(DEFAULT_FIDE_ID)))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].nombreCompleto").value(hasItem(DEFAULT_NOMBRE_COMPLETO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].tipoFuncionario").value(hasItem(DEFAULT_TIPO_FUNCIONARIO.toString())));

        // Check, that the count call also returns 1
        restFuncionariosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFuncionariosShouldNotBeFound(String filter) throws Exception {
        restFuncionariosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFuncionariosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFuncionarios() throws Exception {
        // Get the funcionarios
        restFuncionariosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFuncionarios() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        int databaseSizeBeforeUpdate = funcionariosRepository.findAll().size();

        // Update the funcionarios
        Funcionarios updatedFuncionarios = funcionariosRepository.findById(funcionarios.getId()).get();
        // Disconnect from session so that the updates on updatedFuncionarios are not directly saved in db
        em.detach(updatedFuncionarios);
        updatedFuncionarios
            .elo(UPDATED_ELO)
            .fideId(UPDATED_FIDE_ID)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .nombreCompleto(UPDATED_NOMBRE_COMPLETO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .documento(UPDATED_DOCUMENTO)
            .estado(UPDATED_ESTADO)
            .tipoFuncionario(UPDATED_TIPO_FUNCIONARIO);

        restFuncionariosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFuncionarios.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFuncionarios))
            )
            .andExpect(status().isOk());

        // Validate the Funcionarios in the database
        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeUpdate);
        Funcionarios testFuncionarios = funcionariosList.get(funcionariosList.size() - 1);
        assertThat(testFuncionarios.getElo()).isEqualTo(UPDATED_ELO);
        assertThat(testFuncionarios.getFideId()).isEqualTo(UPDATED_FIDE_ID);
        assertThat(testFuncionarios.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testFuncionarios.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testFuncionarios.getNombreCompleto()).isEqualTo(UPDATED_NOMBRE_COMPLETO);
        assertThat(testFuncionarios.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFuncionarios.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testFuncionarios.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testFuncionarios.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testFuncionarios.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testFuncionarios.getTipoFuncionario()).isEqualTo(UPDATED_TIPO_FUNCIONARIO);
    }

    @Test
    @Transactional
    void putNonExistingFuncionarios() throws Exception {
        int databaseSizeBeforeUpdate = funcionariosRepository.findAll().size();
        funcionarios.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuncionariosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, funcionarios.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funcionarios))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionarios in the database
        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFuncionarios() throws Exception {
        int databaseSizeBeforeUpdate = funcionariosRepository.findAll().size();
        funcionarios.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionariosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funcionarios))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionarios in the database
        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFuncionarios() throws Exception {
        int databaseSizeBeforeUpdate = funcionariosRepository.findAll().size();
        funcionarios.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionariosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarios)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Funcionarios in the database
        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFuncionariosWithPatch() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        int databaseSizeBeforeUpdate = funcionariosRepository.findAll().size();

        // Update the funcionarios using partial update
        Funcionarios partialUpdatedFuncionarios = new Funcionarios();
        partialUpdatedFuncionarios.setId(funcionarios.getId());

        partialUpdatedFuncionarios
            .fideId(UPDATED_FIDE_ID)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .documento(UPDATED_DOCUMENTO)
            .estado(UPDATED_ESTADO);

        restFuncionariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuncionarios.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFuncionarios))
            )
            .andExpect(status().isOk());

        // Validate the Funcionarios in the database
        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeUpdate);
        Funcionarios testFuncionarios = funcionariosList.get(funcionariosList.size() - 1);
        assertThat(testFuncionarios.getElo()).isEqualTo(DEFAULT_ELO);
        assertThat(testFuncionarios.getFideId()).isEqualTo(UPDATED_FIDE_ID);
        assertThat(testFuncionarios.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testFuncionarios.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testFuncionarios.getNombreCompleto()).isEqualTo(DEFAULT_NOMBRE_COMPLETO);
        assertThat(testFuncionarios.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFuncionarios.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testFuncionarios.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testFuncionarios.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testFuncionarios.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testFuncionarios.getTipoFuncionario()).isEqualTo(DEFAULT_TIPO_FUNCIONARIO);
    }

    @Test
    @Transactional
    void fullUpdateFuncionariosWithPatch() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        int databaseSizeBeforeUpdate = funcionariosRepository.findAll().size();

        // Update the funcionarios using partial update
        Funcionarios partialUpdatedFuncionarios = new Funcionarios();
        partialUpdatedFuncionarios.setId(funcionarios.getId());

        partialUpdatedFuncionarios
            .elo(UPDATED_ELO)
            .fideId(UPDATED_FIDE_ID)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .nombreCompleto(UPDATED_NOMBRE_COMPLETO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .documento(UPDATED_DOCUMENTO)
            .estado(UPDATED_ESTADO)
            .tipoFuncionario(UPDATED_TIPO_FUNCIONARIO);

        restFuncionariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuncionarios.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFuncionarios))
            )
            .andExpect(status().isOk());

        // Validate the Funcionarios in the database
        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeUpdate);
        Funcionarios testFuncionarios = funcionariosList.get(funcionariosList.size() - 1);
        assertThat(testFuncionarios.getElo()).isEqualTo(UPDATED_ELO);
        assertThat(testFuncionarios.getFideId()).isEqualTo(UPDATED_FIDE_ID);
        assertThat(testFuncionarios.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testFuncionarios.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testFuncionarios.getNombreCompleto()).isEqualTo(UPDATED_NOMBRE_COMPLETO);
        assertThat(testFuncionarios.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFuncionarios.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testFuncionarios.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testFuncionarios.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testFuncionarios.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testFuncionarios.getTipoFuncionario()).isEqualTo(UPDATED_TIPO_FUNCIONARIO);
    }

    @Test
    @Transactional
    void patchNonExistingFuncionarios() throws Exception {
        int databaseSizeBeforeUpdate = funcionariosRepository.findAll().size();
        funcionarios.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuncionariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, funcionarios.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funcionarios))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionarios in the database
        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFuncionarios() throws Exception {
        int databaseSizeBeforeUpdate = funcionariosRepository.findAll().size();
        funcionarios.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funcionarios))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionarios in the database
        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFuncionarios() throws Exception {
        int databaseSizeBeforeUpdate = funcionariosRepository.findAll().size();
        funcionarios.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionariosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(funcionarios))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Funcionarios in the database
        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFuncionarios() throws Exception {
        // Initialize the database
        funcionariosRepository.saveAndFlush(funcionarios);

        int databaseSizeBeforeDelete = funcionariosRepository.findAll().size();

        // Delete the funcionarios
        restFuncionariosMockMvc
            .perform(delete(ENTITY_API_URL_ID, funcionarios.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Funcionarios> funcionariosList = funcionariosRepository.findAll();
        assertThat(funcionariosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
