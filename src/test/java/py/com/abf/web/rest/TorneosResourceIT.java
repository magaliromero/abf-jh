package py.com.abf.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
import py.com.abf.domain.FichaPartidasTorneos;
import py.com.abf.domain.Torneos;
import py.com.abf.domain.enumeration.TiposTorneos;
import py.com.abf.repository.TorneosRepository;
import py.com.abf.service.criteria.TorneosCriteria;

/**
 * Integration tests for the {@link TorneosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TorneosResourceIT {

    private static final String DEFAULT_NOMBRE_TORNEO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_TORNEO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_INICIO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_FIN = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_LUGAR = "AAAAAAAAAA";
    private static final String UPDATED_LUGAR = "BBBBBBBBBB";

    private static final String DEFAULT_TIEMPO = "AAAAAAAAAA";
    private static final String UPDATED_TIEMPO = "BBBBBBBBBB";

    private static final TiposTorneos DEFAULT_TIPO_TORNEO = TiposTorneos.INTERNO;
    private static final TiposTorneos UPDATED_TIPO_TORNEO = TiposTorneos.NACIONAL;

    private static final Boolean DEFAULT_TORNEO_EVALUADO = false;
    private static final Boolean UPDATED_TORNEO_EVALUADO = true;

    private static final Boolean DEFAULT_FEDERADO = false;
    private static final Boolean UPDATED_FEDERADO = true;

    private static final String ENTITY_API_URL = "/api/torneos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TorneosRepository torneosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTorneosMockMvc;

    private Torneos torneos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Torneos createEntity(EntityManager em) {
        Torneos torneos = new Torneos()
            .nombreTorneo(DEFAULT_NOMBRE_TORNEO)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .lugar(DEFAULT_LUGAR)
            .tiempo(DEFAULT_TIEMPO)
            .tipoTorneo(DEFAULT_TIPO_TORNEO)
            .torneoEvaluado(DEFAULT_TORNEO_EVALUADO)
            .federado(DEFAULT_FEDERADO);
        return torneos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Torneos createUpdatedEntity(EntityManager em) {
        Torneos torneos = new Torneos()
            .nombreTorneo(UPDATED_NOMBRE_TORNEO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .lugar(UPDATED_LUGAR)
            .tiempo(UPDATED_TIEMPO)
            .tipoTorneo(UPDATED_TIPO_TORNEO)
            .torneoEvaluado(UPDATED_TORNEO_EVALUADO)
            .federado(UPDATED_FEDERADO);
        return torneos;
    }

    @BeforeEach
    public void initTest() {
        torneos = createEntity(em);
    }

    @Test
    @Transactional
    void createTorneos() throws Exception {
        int databaseSizeBeforeCreate = torneosRepository.findAll().size();
        // Create the Torneos
        restTorneosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(torneos)))
            .andExpect(status().isCreated());

        // Validate the Torneos in the database
        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeCreate + 1);
        Torneos testTorneos = torneosList.get(torneosList.size() - 1);
        assertThat(testTorneos.getNombreTorneo()).isEqualTo(DEFAULT_NOMBRE_TORNEO);
        assertThat(testTorneos.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testTorneos.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testTorneos.getLugar()).isEqualTo(DEFAULT_LUGAR);
        assertThat(testTorneos.getTiempo()).isEqualTo(DEFAULT_TIEMPO);
        assertThat(testTorneos.getTipoTorneo()).isEqualTo(DEFAULT_TIPO_TORNEO);
        assertThat(testTorneos.getTorneoEvaluado()).isEqualTo(DEFAULT_TORNEO_EVALUADO);
        assertThat(testTorneos.getFederado()).isEqualTo(DEFAULT_FEDERADO);
    }

    @Test
    @Transactional
    void createTorneosWithExistingId() throws Exception {
        // Create the Torneos with an existing ID
        torneos.setId(1L);

        int databaseSizeBeforeCreate = torneosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTorneosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(torneos)))
            .andExpect(status().isBadRequest());

        // Validate the Torneos in the database
        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreTorneoIsRequired() throws Exception {
        int databaseSizeBeforeTest = torneosRepository.findAll().size();
        // set the field null
        torneos.setNombreTorneo(null);

        // Create the Torneos, which fails.

        restTorneosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(torneos)))
            .andExpect(status().isBadRequest());

        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = torneosRepository.findAll().size();
        // set the field null
        torneos.setFechaInicio(null);

        // Create the Torneos, which fails.

        restTorneosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(torneos)))
            .andExpect(status().isBadRequest());

        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = torneosRepository.findAll().size();
        // set the field null
        torneos.setFechaFin(null);

        // Create the Torneos, which fails.

        restTorneosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(torneos)))
            .andExpect(status().isBadRequest());

        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLugarIsRequired() throws Exception {
        int databaseSizeBeforeTest = torneosRepository.findAll().size();
        // set the field null
        torneos.setLugar(null);

        // Create the Torneos, which fails.

        restTorneosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(torneos)))
            .andExpect(status().isBadRequest());

        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoTorneoIsRequired() throws Exception {
        int databaseSizeBeforeTest = torneosRepository.findAll().size();
        // set the field null
        torneos.setTipoTorneo(null);

        // Create the Torneos, which fails.

        restTorneosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(torneos)))
            .andExpect(status().isBadRequest());

        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTorneoEvaluadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = torneosRepository.findAll().size();
        // set the field null
        torneos.setTorneoEvaluado(null);

        // Create the Torneos, which fails.

        restTorneosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(torneos)))
            .andExpect(status().isBadRequest());

        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFederadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = torneosRepository.findAll().size();
        // set the field null
        torneos.setFederado(null);

        // Create the Torneos, which fails.

        restTorneosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(torneos)))
            .andExpect(status().isBadRequest());

        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTorneos() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList
        restTorneosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(torneos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreTorneo").value(hasItem(DEFAULT_NOMBRE_TORNEO)))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].lugar").value(hasItem(DEFAULT_LUGAR)))
            .andExpect(jsonPath("$.[*].tiempo").value(hasItem(DEFAULT_TIEMPO)))
            .andExpect(jsonPath("$.[*].tipoTorneo").value(hasItem(DEFAULT_TIPO_TORNEO.toString())))
            .andExpect(jsonPath("$.[*].torneoEvaluado").value(hasItem(DEFAULT_TORNEO_EVALUADO.booleanValue())))
            .andExpect(jsonPath("$.[*].federado").value(hasItem(DEFAULT_FEDERADO.booleanValue())));
    }

    @Test
    @Transactional
    void getTorneos() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get the torneos
        restTorneosMockMvc
            .perform(get(ENTITY_API_URL_ID, torneos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(torneos.getId().intValue()))
            .andExpect(jsonPath("$.nombreTorneo").value(DEFAULT_NOMBRE_TORNEO))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.lugar").value(DEFAULT_LUGAR))
            .andExpect(jsonPath("$.tiempo").value(DEFAULT_TIEMPO))
            .andExpect(jsonPath("$.tipoTorneo").value(DEFAULT_TIPO_TORNEO.toString()))
            .andExpect(jsonPath("$.torneoEvaluado").value(DEFAULT_TORNEO_EVALUADO.booleanValue()))
            .andExpect(jsonPath("$.federado").value(DEFAULT_FEDERADO.booleanValue()));
    }

    @Test
    @Transactional
    void getTorneosByIdFiltering() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        Long id = torneos.getId();

        defaultTorneosShouldBeFound("id.equals=" + id);
        defaultTorneosShouldNotBeFound("id.notEquals=" + id);

        defaultTorneosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTorneosShouldNotBeFound("id.greaterThan=" + id);

        defaultTorneosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTorneosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTorneosByNombreTorneoIsEqualToSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where nombreTorneo equals to DEFAULT_NOMBRE_TORNEO
        defaultTorneosShouldBeFound("nombreTorneo.equals=" + DEFAULT_NOMBRE_TORNEO);

        // Get all the torneosList where nombreTorneo equals to UPDATED_NOMBRE_TORNEO
        defaultTorneosShouldNotBeFound("nombreTorneo.equals=" + UPDATED_NOMBRE_TORNEO);
    }

    @Test
    @Transactional
    void getAllTorneosByNombreTorneoIsInShouldWork() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where nombreTorneo in DEFAULT_NOMBRE_TORNEO or UPDATED_NOMBRE_TORNEO
        defaultTorneosShouldBeFound("nombreTorneo.in=" + DEFAULT_NOMBRE_TORNEO + "," + UPDATED_NOMBRE_TORNEO);

        // Get all the torneosList where nombreTorneo equals to UPDATED_NOMBRE_TORNEO
        defaultTorneosShouldNotBeFound("nombreTorneo.in=" + UPDATED_NOMBRE_TORNEO);
    }

    @Test
    @Transactional
    void getAllTorneosByNombreTorneoIsNullOrNotNull() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where nombreTorneo is not null
        defaultTorneosShouldBeFound("nombreTorneo.specified=true");

        // Get all the torneosList where nombreTorneo is null
        defaultTorneosShouldNotBeFound("nombreTorneo.specified=false");
    }

    @Test
    @Transactional
    void getAllTorneosByNombreTorneoContainsSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where nombreTorneo contains DEFAULT_NOMBRE_TORNEO
        defaultTorneosShouldBeFound("nombreTorneo.contains=" + DEFAULT_NOMBRE_TORNEO);

        // Get all the torneosList where nombreTorneo contains UPDATED_NOMBRE_TORNEO
        defaultTorneosShouldNotBeFound("nombreTorneo.contains=" + UPDATED_NOMBRE_TORNEO);
    }

    @Test
    @Transactional
    void getAllTorneosByNombreTorneoNotContainsSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where nombreTorneo does not contain DEFAULT_NOMBRE_TORNEO
        defaultTorneosShouldNotBeFound("nombreTorneo.doesNotContain=" + DEFAULT_NOMBRE_TORNEO);

        // Get all the torneosList where nombreTorneo does not contain UPDATED_NOMBRE_TORNEO
        defaultTorneosShouldBeFound("nombreTorneo.doesNotContain=" + UPDATED_NOMBRE_TORNEO);
    }

    @Test
    @Transactional
    void getAllTorneosByFechaInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaInicio equals to DEFAULT_FECHA_INICIO
        defaultTorneosShouldBeFound("fechaInicio.equals=" + DEFAULT_FECHA_INICIO);

        // Get all the torneosList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultTorneosShouldNotBeFound("fechaInicio.equals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTorneosByFechaInicioIsInShouldWork() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaInicio in DEFAULT_FECHA_INICIO or UPDATED_FECHA_INICIO
        defaultTorneosShouldBeFound("fechaInicio.in=" + DEFAULT_FECHA_INICIO + "," + UPDATED_FECHA_INICIO);

        // Get all the torneosList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultTorneosShouldNotBeFound("fechaInicio.in=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTorneosByFechaInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaInicio is not null
        defaultTorneosShouldBeFound("fechaInicio.specified=true");

        // Get all the torneosList where fechaInicio is null
        defaultTorneosShouldNotBeFound("fechaInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllTorneosByFechaInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaInicio is greater than or equal to DEFAULT_FECHA_INICIO
        defaultTorneosShouldBeFound("fechaInicio.greaterThanOrEqual=" + DEFAULT_FECHA_INICIO);

        // Get all the torneosList where fechaInicio is greater than or equal to UPDATED_FECHA_INICIO
        defaultTorneosShouldNotBeFound("fechaInicio.greaterThanOrEqual=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTorneosByFechaInicioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaInicio is less than or equal to DEFAULT_FECHA_INICIO
        defaultTorneosShouldBeFound("fechaInicio.lessThanOrEqual=" + DEFAULT_FECHA_INICIO);

        // Get all the torneosList where fechaInicio is less than or equal to SMALLER_FECHA_INICIO
        defaultTorneosShouldNotBeFound("fechaInicio.lessThanOrEqual=" + SMALLER_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTorneosByFechaInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaInicio is less than DEFAULT_FECHA_INICIO
        defaultTorneosShouldNotBeFound("fechaInicio.lessThan=" + DEFAULT_FECHA_INICIO);

        // Get all the torneosList where fechaInicio is less than UPDATED_FECHA_INICIO
        defaultTorneosShouldBeFound("fechaInicio.lessThan=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTorneosByFechaInicioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaInicio is greater than DEFAULT_FECHA_INICIO
        defaultTorneosShouldNotBeFound("fechaInicio.greaterThan=" + DEFAULT_FECHA_INICIO);

        // Get all the torneosList where fechaInicio is greater than SMALLER_FECHA_INICIO
        defaultTorneosShouldBeFound("fechaInicio.greaterThan=" + SMALLER_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTorneosByFechaFinIsEqualToSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaFin equals to DEFAULT_FECHA_FIN
        defaultTorneosShouldBeFound("fechaFin.equals=" + DEFAULT_FECHA_FIN);

        // Get all the torneosList where fechaFin equals to UPDATED_FECHA_FIN
        defaultTorneosShouldNotBeFound("fechaFin.equals=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTorneosByFechaFinIsInShouldWork() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaFin in DEFAULT_FECHA_FIN or UPDATED_FECHA_FIN
        defaultTorneosShouldBeFound("fechaFin.in=" + DEFAULT_FECHA_FIN + "," + UPDATED_FECHA_FIN);

        // Get all the torneosList where fechaFin equals to UPDATED_FECHA_FIN
        defaultTorneosShouldNotBeFound("fechaFin.in=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTorneosByFechaFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaFin is not null
        defaultTorneosShouldBeFound("fechaFin.specified=true");

        // Get all the torneosList where fechaFin is null
        defaultTorneosShouldNotBeFound("fechaFin.specified=false");
    }

    @Test
    @Transactional
    void getAllTorneosByFechaFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaFin is greater than or equal to DEFAULT_FECHA_FIN
        defaultTorneosShouldBeFound("fechaFin.greaterThanOrEqual=" + DEFAULT_FECHA_FIN);

        // Get all the torneosList where fechaFin is greater than or equal to UPDATED_FECHA_FIN
        defaultTorneosShouldNotBeFound("fechaFin.greaterThanOrEqual=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTorneosByFechaFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaFin is less than or equal to DEFAULT_FECHA_FIN
        defaultTorneosShouldBeFound("fechaFin.lessThanOrEqual=" + DEFAULT_FECHA_FIN);

        // Get all the torneosList where fechaFin is less than or equal to SMALLER_FECHA_FIN
        defaultTorneosShouldNotBeFound("fechaFin.lessThanOrEqual=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTorneosByFechaFinIsLessThanSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaFin is less than DEFAULT_FECHA_FIN
        defaultTorneosShouldNotBeFound("fechaFin.lessThan=" + DEFAULT_FECHA_FIN);

        // Get all the torneosList where fechaFin is less than UPDATED_FECHA_FIN
        defaultTorneosShouldBeFound("fechaFin.lessThan=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTorneosByFechaFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where fechaFin is greater than DEFAULT_FECHA_FIN
        defaultTorneosShouldNotBeFound("fechaFin.greaterThan=" + DEFAULT_FECHA_FIN);

        // Get all the torneosList where fechaFin is greater than SMALLER_FECHA_FIN
        defaultTorneosShouldBeFound("fechaFin.greaterThan=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTorneosByLugarIsEqualToSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where lugar equals to DEFAULT_LUGAR
        defaultTorneosShouldBeFound("lugar.equals=" + DEFAULT_LUGAR);

        // Get all the torneosList where lugar equals to UPDATED_LUGAR
        defaultTorneosShouldNotBeFound("lugar.equals=" + UPDATED_LUGAR);
    }

    @Test
    @Transactional
    void getAllTorneosByLugarIsInShouldWork() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where lugar in DEFAULT_LUGAR or UPDATED_LUGAR
        defaultTorneosShouldBeFound("lugar.in=" + DEFAULT_LUGAR + "," + UPDATED_LUGAR);

        // Get all the torneosList where lugar equals to UPDATED_LUGAR
        defaultTorneosShouldNotBeFound("lugar.in=" + UPDATED_LUGAR);
    }

    @Test
    @Transactional
    void getAllTorneosByLugarIsNullOrNotNull() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where lugar is not null
        defaultTorneosShouldBeFound("lugar.specified=true");

        // Get all the torneosList where lugar is null
        defaultTorneosShouldNotBeFound("lugar.specified=false");
    }

    @Test
    @Transactional
    void getAllTorneosByLugarContainsSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where lugar contains DEFAULT_LUGAR
        defaultTorneosShouldBeFound("lugar.contains=" + DEFAULT_LUGAR);

        // Get all the torneosList where lugar contains UPDATED_LUGAR
        defaultTorneosShouldNotBeFound("lugar.contains=" + UPDATED_LUGAR);
    }

    @Test
    @Transactional
    void getAllTorneosByLugarNotContainsSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where lugar does not contain DEFAULT_LUGAR
        defaultTorneosShouldNotBeFound("lugar.doesNotContain=" + DEFAULT_LUGAR);

        // Get all the torneosList where lugar does not contain UPDATED_LUGAR
        defaultTorneosShouldBeFound("lugar.doesNotContain=" + UPDATED_LUGAR);
    }

    @Test
    @Transactional
    void getAllTorneosByTiempoIsEqualToSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where tiempo equals to DEFAULT_TIEMPO
        defaultTorneosShouldBeFound("tiempo.equals=" + DEFAULT_TIEMPO);

        // Get all the torneosList where tiempo equals to UPDATED_TIEMPO
        defaultTorneosShouldNotBeFound("tiempo.equals=" + UPDATED_TIEMPO);
    }

    @Test
    @Transactional
    void getAllTorneosByTiempoIsInShouldWork() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where tiempo in DEFAULT_TIEMPO or UPDATED_TIEMPO
        defaultTorneosShouldBeFound("tiempo.in=" + DEFAULT_TIEMPO + "," + UPDATED_TIEMPO);

        // Get all the torneosList where tiempo equals to UPDATED_TIEMPO
        defaultTorneosShouldNotBeFound("tiempo.in=" + UPDATED_TIEMPO);
    }

    @Test
    @Transactional
    void getAllTorneosByTiempoIsNullOrNotNull() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where tiempo is not null
        defaultTorneosShouldBeFound("tiempo.specified=true");

        // Get all the torneosList where tiempo is null
        defaultTorneosShouldNotBeFound("tiempo.specified=false");
    }

    @Test
    @Transactional
    void getAllTorneosByTiempoContainsSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where tiempo contains DEFAULT_TIEMPO
        defaultTorneosShouldBeFound("tiempo.contains=" + DEFAULT_TIEMPO);

        // Get all the torneosList where tiempo contains UPDATED_TIEMPO
        defaultTorneosShouldNotBeFound("tiempo.contains=" + UPDATED_TIEMPO);
    }

    @Test
    @Transactional
    void getAllTorneosByTiempoNotContainsSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where tiempo does not contain DEFAULT_TIEMPO
        defaultTorneosShouldNotBeFound("tiempo.doesNotContain=" + DEFAULT_TIEMPO);

        // Get all the torneosList where tiempo does not contain UPDATED_TIEMPO
        defaultTorneosShouldBeFound("tiempo.doesNotContain=" + UPDATED_TIEMPO);
    }

    @Test
    @Transactional
    void getAllTorneosByTipoTorneoIsEqualToSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where tipoTorneo equals to DEFAULT_TIPO_TORNEO
        defaultTorneosShouldBeFound("tipoTorneo.equals=" + DEFAULT_TIPO_TORNEO);

        // Get all the torneosList where tipoTorneo equals to UPDATED_TIPO_TORNEO
        defaultTorneosShouldNotBeFound("tipoTorneo.equals=" + UPDATED_TIPO_TORNEO);
    }

    @Test
    @Transactional
    void getAllTorneosByTipoTorneoIsInShouldWork() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where tipoTorneo in DEFAULT_TIPO_TORNEO or UPDATED_TIPO_TORNEO
        defaultTorneosShouldBeFound("tipoTorneo.in=" + DEFAULT_TIPO_TORNEO + "," + UPDATED_TIPO_TORNEO);

        // Get all the torneosList where tipoTorneo equals to UPDATED_TIPO_TORNEO
        defaultTorneosShouldNotBeFound("tipoTorneo.in=" + UPDATED_TIPO_TORNEO);
    }

    @Test
    @Transactional
    void getAllTorneosByTipoTorneoIsNullOrNotNull() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where tipoTorneo is not null
        defaultTorneosShouldBeFound("tipoTorneo.specified=true");

        // Get all the torneosList where tipoTorneo is null
        defaultTorneosShouldNotBeFound("tipoTorneo.specified=false");
    }

    @Test
    @Transactional
    void getAllTorneosByTorneoEvaluadoIsEqualToSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where torneoEvaluado equals to DEFAULT_TORNEO_EVALUADO
        defaultTorneosShouldBeFound("torneoEvaluado.equals=" + DEFAULT_TORNEO_EVALUADO);

        // Get all the torneosList where torneoEvaluado equals to UPDATED_TORNEO_EVALUADO
        defaultTorneosShouldNotBeFound("torneoEvaluado.equals=" + UPDATED_TORNEO_EVALUADO);
    }

    @Test
    @Transactional
    void getAllTorneosByTorneoEvaluadoIsInShouldWork() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where torneoEvaluado in DEFAULT_TORNEO_EVALUADO or UPDATED_TORNEO_EVALUADO
        defaultTorneosShouldBeFound("torneoEvaluado.in=" + DEFAULT_TORNEO_EVALUADO + "," + UPDATED_TORNEO_EVALUADO);

        // Get all the torneosList where torneoEvaluado equals to UPDATED_TORNEO_EVALUADO
        defaultTorneosShouldNotBeFound("torneoEvaluado.in=" + UPDATED_TORNEO_EVALUADO);
    }

    @Test
    @Transactional
    void getAllTorneosByTorneoEvaluadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where torneoEvaluado is not null
        defaultTorneosShouldBeFound("torneoEvaluado.specified=true");

        // Get all the torneosList where torneoEvaluado is null
        defaultTorneosShouldNotBeFound("torneoEvaluado.specified=false");
    }

    @Test
    @Transactional
    void getAllTorneosByFederadoIsEqualToSomething() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where federado equals to DEFAULT_FEDERADO
        defaultTorneosShouldBeFound("federado.equals=" + DEFAULT_FEDERADO);

        // Get all the torneosList where federado equals to UPDATED_FEDERADO
        defaultTorneosShouldNotBeFound("federado.equals=" + UPDATED_FEDERADO);
    }

    @Test
    @Transactional
    void getAllTorneosByFederadoIsInShouldWork() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where federado in DEFAULT_FEDERADO or UPDATED_FEDERADO
        defaultTorneosShouldBeFound("federado.in=" + DEFAULT_FEDERADO + "," + UPDATED_FEDERADO);

        // Get all the torneosList where federado equals to UPDATED_FEDERADO
        defaultTorneosShouldNotBeFound("federado.in=" + UPDATED_FEDERADO);
    }

    @Test
    @Transactional
    void getAllTorneosByFederadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        // Get all the torneosList where federado is not null
        defaultTorneosShouldBeFound("federado.specified=true");

        // Get all the torneosList where federado is null
        defaultTorneosShouldNotBeFound("federado.specified=false");
    }

    @Test
    @Transactional
    void getAllTorneosByFichaPartidasTorneosIsEqualToSomething() throws Exception {
        FichaPartidasTorneos fichaPartidasTorneos;
        if (TestUtil.findAll(em, FichaPartidasTorneos.class).isEmpty()) {
            torneosRepository.saveAndFlush(torneos);
            fichaPartidasTorneos = FichaPartidasTorneosResourceIT.createEntity(em);
        } else {
            fichaPartidasTorneos = TestUtil.findAll(em, FichaPartidasTorneos.class).get(0);
        }
        em.persist(fichaPartidasTorneos);
        em.flush();
        torneos.addFichaPartidasTorneos(fichaPartidasTorneos);
        torneosRepository.saveAndFlush(torneos);
        Long fichaPartidasTorneosId = fichaPartidasTorneos.getId();

        // Get all the torneosList where fichaPartidasTorneos equals to fichaPartidasTorneosId
        defaultTorneosShouldBeFound("fichaPartidasTorneosId.equals=" + fichaPartidasTorneosId);

        // Get all the torneosList where fichaPartidasTorneos equals to (fichaPartidasTorneosId + 1)
        defaultTorneosShouldNotBeFound("fichaPartidasTorneosId.equals=" + (fichaPartidasTorneosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTorneosShouldBeFound(String filter) throws Exception {
        restTorneosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(torneos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreTorneo").value(hasItem(DEFAULT_NOMBRE_TORNEO)))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].lugar").value(hasItem(DEFAULT_LUGAR)))
            .andExpect(jsonPath("$.[*].tiempo").value(hasItem(DEFAULT_TIEMPO)))
            .andExpect(jsonPath("$.[*].tipoTorneo").value(hasItem(DEFAULT_TIPO_TORNEO.toString())))
            .andExpect(jsonPath("$.[*].torneoEvaluado").value(hasItem(DEFAULT_TORNEO_EVALUADO.booleanValue())))
            .andExpect(jsonPath("$.[*].federado").value(hasItem(DEFAULT_FEDERADO.booleanValue())));

        // Check, that the count call also returns 1
        restTorneosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTorneosShouldNotBeFound(String filter) throws Exception {
        restTorneosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTorneosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTorneos() throws Exception {
        // Get the torneos
        restTorneosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTorneos() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        int databaseSizeBeforeUpdate = torneosRepository.findAll().size();

        // Update the torneos
        Torneos updatedTorneos = torneosRepository.findById(torneos.getId()).get();
        // Disconnect from session so that the updates on updatedTorneos are not directly saved in db
        em.detach(updatedTorneos);
        updatedTorneos
            .nombreTorneo(UPDATED_NOMBRE_TORNEO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .lugar(UPDATED_LUGAR)
            .tiempo(UPDATED_TIEMPO)
            .tipoTorneo(UPDATED_TIPO_TORNEO)
            .torneoEvaluado(UPDATED_TORNEO_EVALUADO)
            .federado(UPDATED_FEDERADO);

        restTorneosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTorneos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTorneos))
            )
            .andExpect(status().isOk());

        // Validate the Torneos in the database
        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeUpdate);
        Torneos testTorneos = torneosList.get(torneosList.size() - 1);
        assertThat(testTorneos.getNombreTorneo()).isEqualTo(UPDATED_NOMBRE_TORNEO);
        assertThat(testTorneos.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testTorneos.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testTorneos.getLugar()).isEqualTo(UPDATED_LUGAR);
        assertThat(testTorneos.getTiempo()).isEqualTo(UPDATED_TIEMPO);
        assertThat(testTorneos.getTipoTorneo()).isEqualTo(UPDATED_TIPO_TORNEO);
        assertThat(testTorneos.getTorneoEvaluado()).isEqualTo(UPDATED_TORNEO_EVALUADO);
        assertThat(testTorneos.getFederado()).isEqualTo(UPDATED_FEDERADO);
    }

    @Test
    @Transactional
    void putNonExistingTorneos() throws Exception {
        int databaseSizeBeforeUpdate = torneosRepository.findAll().size();
        torneos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTorneosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, torneos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(torneos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Torneos in the database
        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTorneos() throws Exception {
        int databaseSizeBeforeUpdate = torneosRepository.findAll().size();
        torneos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTorneosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(torneos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Torneos in the database
        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTorneos() throws Exception {
        int databaseSizeBeforeUpdate = torneosRepository.findAll().size();
        torneos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTorneosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(torneos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Torneos in the database
        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTorneosWithPatch() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        int databaseSizeBeforeUpdate = torneosRepository.findAll().size();

        // Update the torneos using partial update
        Torneos partialUpdatedTorneos = new Torneos();
        partialUpdatedTorneos.setId(torneos.getId());

        partialUpdatedTorneos
            .nombreTorneo(UPDATED_NOMBRE_TORNEO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .tiempo(UPDATED_TIEMPO)
            .tipoTorneo(UPDATED_TIPO_TORNEO)
            .torneoEvaluado(UPDATED_TORNEO_EVALUADO);

        restTorneosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTorneos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTorneos))
            )
            .andExpect(status().isOk());

        // Validate the Torneos in the database
        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeUpdate);
        Torneos testTorneos = torneosList.get(torneosList.size() - 1);
        assertThat(testTorneos.getNombreTorneo()).isEqualTo(UPDATED_NOMBRE_TORNEO);
        assertThat(testTorneos.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testTorneos.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testTorneos.getLugar()).isEqualTo(DEFAULT_LUGAR);
        assertThat(testTorneos.getTiempo()).isEqualTo(UPDATED_TIEMPO);
        assertThat(testTorneos.getTipoTorneo()).isEqualTo(UPDATED_TIPO_TORNEO);
        assertThat(testTorneos.getTorneoEvaluado()).isEqualTo(UPDATED_TORNEO_EVALUADO);
        assertThat(testTorneos.getFederado()).isEqualTo(DEFAULT_FEDERADO);
    }

    @Test
    @Transactional
    void fullUpdateTorneosWithPatch() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        int databaseSizeBeforeUpdate = torneosRepository.findAll().size();

        // Update the torneos using partial update
        Torneos partialUpdatedTorneos = new Torneos();
        partialUpdatedTorneos.setId(torneos.getId());

        partialUpdatedTorneos
            .nombreTorneo(UPDATED_NOMBRE_TORNEO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .lugar(UPDATED_LUGAR)
            .tiempo(UPDATED_TIEMPO)
            .tipoTorneo(UPDATED_TIPO_TORNEO)
            .torneoEvaluado(UPDATED_TORNEO_EVALUADO)
            .federado(UPDATED_FEDERADO);

        restTorneosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTorneos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTorneos))
            )
            .andExpect(status().isOk());

        // Validate the Torneos in the database
        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeUpdate);
        Torneos testTorneos = torneosList.get(torneosList.size() - 1);
        assertThat(testTorneos.getNombreTorneo()).isEqualTo(UPDATED_NOMBRE_TORNEO);
        assertThat(testTorneos.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testTorneos.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testTorneos.getLugar()).isEqualTo(UPDATED_LUGAR);
        assertThat(testTorneos.getTiempo()).isEqualTo(UPDATED_TIEMPO);
        assertThat(testTorneos.getTipoTorneo()).isEqualTo(UPDATED_TIPO_TORNEO);
        assertThat(testTorneos.getTorneoEvaluado()).isEqualTo(UPDATED_TORNEO_EVALUADO);
        assertThat(testTorneos.getFederado()).isEqualTo(UPDATED_FEDERADO);
    }

    @Test
    @Transactional
    void patchNonExistingTorneos() throws Exception {
        int databaseSizeBeforeUpdate = torneosRepository.findAll().size();
        torneos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTorneosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, torneos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(torneos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Torneos in the database
        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTorneos() throws Exception {
        int databaseSizeBeforeUpdate = torneosRepository.findAll().size();
        torneos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTorneosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(torneos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Torneos in the database
        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTorneos() throws Exception {
        int databaseSizeBeforeUpdate = torneosRepository.findAll().size();
        torneos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTorneosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(torneos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Torneos in the database
        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTorneos() throws Exception {
        // Initialize the database
        torneosRepository.saveAndFlush(torneos);

        int databaseSizeBeforeDelete = torneosRepository.findAll().size();

        // Delete the torneos
        restTorneosMockMvc
            .perform(delete(ENTITY_API_URL_ID, torneos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Torneos> torneosList = torneosRepository.findAll();
        assertThat(torneosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
