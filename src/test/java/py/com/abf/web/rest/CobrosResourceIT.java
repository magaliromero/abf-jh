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
import py.com.abf.domain.Cobros;
import py.com.abf.domain.enumeration.TiposPagos;
import py.com.abf.repository.CobrosRepository;
import py.com.abf.service.CobrosService;
import py.com.abf.service.criteria.CobrosCriteria;

/**
 * Integration tests for the {@link CobrosResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CobrosResourceIT {

    private static final Integer DEFAULT_MONTO_PAGO = 1;
    private static final Integer UPDATED_MONTO_PAGO = 2;
    private static final Integer SMALLER_MONTO_PAGO = 1 - 1;

    private static final Integer DEFAULT_MONTO_INICIAL = 1;
    private static final Integer UPDATED_MONTO_INICIAL = 2;
    private static final Integer SMALLER_MONTO_INICIAL = 1 - 1;

    private static final Integer DEFAULT_SALDO = 1;
    private static final Integer UPDATED_SALDO = 2;
    private static final Integer SMALLER_SALDO = 1 - 1;

    private static final LocalDate DEFAULT_FECHA_REGISTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_REGISTRO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_REGISTRO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_PAGO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_PAGO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_PAGO = LocalDate.ofEpochDay(-1L);

    private static final TiposPagos DEFAULT_TIPO_PAGO = TiposPagos.CUOTA;
    private static final TiposPagos UPDATED_TIPO_PAGO = TiposPagos.MENSUAL;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cobros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CobrosRepository cobrosRepository;

    @Mock
    private CobrosRepository cobrosRepositoryMock;

    @Mock
    private CobrosService cobrosServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCobrosMockMvc;

    private Cobros cobros;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cobros createEntity(EntityManager em) {
        Cobros cobros = new Cobros()
            .montoPago(DEFAULT_MONTO_PAGO)
            .montoInicial(DEFAULT_MONTO_INICIAL)
            .saldo(DEFAULT_SALDO)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO)
            .fechaPago(DEFAULT_FECHA_PAGO)
            .tipoPago(DEFAULT_TIPO_PAGO)
            .descripcion(DEFAULT_DESCRIPCION);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        cobros.setAlumnos(alumnos);
        return cobros;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cobros createUpdatedEntity(EntityManager em) {
        Cobros cobros = new Cobros()
            .montoPago(UPDATED_MONTO_PAGO)
            .montoInicial(UPDATED_MONTO_INICIAL)
            .saldo(UPDATED_SALDO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaPago(UPDATED_FECHA_PAGO)
            .tipoPago(UPDATED_TIPO_PAGO)
            .descripcion(UPDATED_DESCRIPCION);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createUpdatedEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        cobros.setAlumnos(alumnos);
        return cobros;
    }

    @BeforeEach
    public void initTest() {
        cobros = createEntity(em);
    }

    @Test
    @Transactional
    void createCobros() throws Exception {
        int databaseSizeBeforeCreate = cobrosRepository.findAll().size();
        // Create the Cobros
        restCobrosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cobros)))
            .andExpect(status().isCreated());

        // Validate the Cobros in the database
        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeCreate + 1);
        Cobros testCobros = cobrosList.get(cobrosList.size() - 1);
        assertThat(testCobros.getMontoPago()).isEqualTo(DEFAULT_MONTO_PAGO);
        assertThat(testCobros.getMontoInicial()).isEqualTo(DEFAULT_MONTO_INICIAL);
        assertThat(testCobros.getSaldo()).isEqualTo(DEFAULT_SALDO);
        assertThat(testCobros.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testCobros.getFechaPago()).isEqualTo(DEFAULT_FECHA_PAGO);
        assertThat(testCobros.getTipoPago()).isEqualTo(DEFAULT_TIPO_PAGO);
        assertThat(testCobros.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createCobrosWithExistingId() throws Exception {
        // Create the Cobros with an existing ID
        cobros.setId(1L);

        int databaseSizeBeforeCreate = cobrosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCobrosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cobros)))
            .andExpect(status().isBadRequest());

        // Validate the Cobros in the database
        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMontoPagoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cobrosRepository.findAll().size();
        // set the field null
        cobros.setMontoPago(null);

        // Create the Cobros, which fails.

        restCobrosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cobros)))
            .andExpect(status().isBadRequest());

        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontoInicialIsRequired() throws Exception {
        int databaseSizeBeforeTest = cobrosRepository.findAll().size();
        // set the field null
        cobros.setMontoInicial(null);

        // Create the Cobros, which fails.

        restCobrosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cobros)))
            .andExpect(status().isBadRequest());

        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSaldoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cobrosRepository.findAll().size();
        // set the field null
        cobros.setSaldo(null);

        // Create the Cobros, which fails.

        restCobrosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cobros)))
            .andExpect(status().isBadRequest());

        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaRegistroIsRequired() throws Exception {
        int databaseSizeBeforeTest = cobrosRepository.findAll().size();
        // set the field null
        cobros.setFechaRegistro(null);

        // Create the Cobros, which fails.

        restCobrosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cobros)))
            .andExpect(status().isBadRequest());

        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaPagoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cobrosRepository.findAll().size();
        // set the field null
        cobros.setFechaPago(null);

        // Create the Cobros, which fails.

        restCobrosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cobros)))
            .andExpect(status().isBadRequest());

        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoPagoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cobrosRepository.findAll().size();
        // set the field null
        cobros.setTipoPago(null);

        // Create the Cobros, which fails.

        restCobrosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cobros)))
            .andExpect(status().isBadRequest());

        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cobrosRepository.findAll().size();
        // set the field null
        cobros.setDescripcion(null);

        // Create the Cobros, which fails.

        restCobrosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cobros)))
            .andExpect(status().isBadRequest());

        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCobros() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList
        restCobrosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cobros.getId().intValue())))
            .andExpect(jsonPath("$.[*].montoPago").value(hasItem(DEFAULT_MONTO_PAGO)))
            .andExpect(jsonPath("$.[*].montoInicial").value(hasItem(DEFAULT_MONTO_INICIAL)))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(DEFAULT_SALDO)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].fechaPago").value(hasItem(DEFAULT_FECHA_PAGO.toString())))
            .andExpect(jsonPath("$.[*].tipoPago").value(hasItem(DEFAULT_TIPO_PAGO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCobrosWithEagerRelationshipsIsEnabled() throws Exception {
        when(cobrosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCobrosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cobrosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCobrosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cobrosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCobrosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cobrosRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCobros() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get the cobros
        restCobrosMockMvc
            .perform(get(ENTITY_API_URL_ID, cobros.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cobros.getId().intValue()))
            .andExpect(jsonPath("$.montoPago").value(DEFAULT_MONTO_PAGO))
            .andExpect(jsonPath("$.montoInicial").value(DEFAULT_MONTO_INICIAL))
            .andExpect(jsonPath("$.saldo").value(DEFAULT_SALDO))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()))
            .andExpect(jsonPath("$.fechaPago").value(DEFAULT_FECHA_PAGO.toString()))
            .andExpect(jsonPath("$.tipoPago").value(DEFAULT_TIPO_PAGO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getCobrosByIdFiltering() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        Long id = cobros.getId();

        defaultCobrosShouldBeFound("id.equals=" + id);
        defaultCobrosShouldNotBeFound("id.notEquals=" + id);

        defaultCobrosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCobrosShouldNotBeFound("id.greaterThan=" + id);

        defaultCobrosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCobrosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCobrosByMontoPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoPago equals to DEFAULT_MONTO_PAGO
        defaultCobrosShouldBeFound("montoPago.equals=" + DEFAULT_MONTO_PAGO);

        // Get all the cobrosList where montoPago equals to UPDATED_MONTO_PAGO
        defaultCobrosShouldNotBeFound("montoPago.equals=" + UPDATED_MONTO_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByMontoPagoIsInShouldWork() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoPago in DEFAULT_MONTO_PAGO or UPDATED_MONTO_PAGO
        defaultCobrosShouldBeFound("montoPago.in=" + DEFAULT_MONTO_PAGO + "," + UPDATED_MONTO_PAGO);

        // Get all the cobrosList where montoPago equals to UPDATED_MONTO_PAGO
        defaultCobrosShouldNotBeFound("montoPago.in=" + UPDATED_MONTO_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByMontoPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoPago is not null
        defaultCobrosShouldBeFound("montoPago.specified=true");

        // Get all the cobrosList where montoPago is null
        defaultCobrosShouldNotBeFound("montoPago.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrosByMontoPagoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoPago is greater than or equal to DEFAULT_MONTO_PAGO
        defaultCobrosShouldBeFound("montoPago.greaterThanOrEqual=" + DEFAULT_MONTO_PAGO);

        // Get all the cobrosList where montoPago is greater than or equal to UPDATED_MONTO_PAGO
        defaultCobrosShouldNotBeFound("montoPago.greaterThanOrEqual=" + UPDATED_MONTO_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByMontoPagoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoPago is less than or equal to DEFAULT_MONTO_PAGO
        defaultCobrosShouldBeFound("montoPago.lessThanOrEqual=" + DEFAULT_MONTO_PAGO);

        // Get all the cobrosList where montoPago is less than or equal to SMALLER_MONTO_PAGO
        defaultCobrosShouldNotBeFound("montoPago.lessThanOrEqual=" + SMALLER_MONTO_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByMontoPagoIsLessThanSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoPago is less than DEFAULT_MONTO_PAGO
        defaultCobrosShouldNotBeFound("montoPago.lessThan=" + DEFAULT_MONTO_PAGO);

        // Get all the cobrosList where montoPago is less than UPDATED_MONTO_PAGO
        defaultCobrosShouldBeFound("montoPago.lessThan=" + UPDATED_MONTO_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByMontoPagoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoPago is greater than DEFAULT_MONTO_PAGO
        defaultCobrosShouldNotBeFound("montoPago.greaterThan=" + DEFAULT_MONTO_PAGO);

        // Get all the cobrosList where montoPago is greater than SMALLER_MONTO_PAGO
        defaultCobrosShouldBeFound("montoPago.greaterThan=" + SMALLER_MONTO_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByMontoInicialIsEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoInicial equals to DEFAULT_MONTO_INICIAL
        defaultCobrosShouldBeFound("montoInicial.equals=" + DEFAULT_MONTO_INICIAL);

        // Get all the cobrosList where montoInicial equals to UPDATED_MONTO_INICIAL
        defaultCobrosShouldNotBeFound("montoInicial.equals=" + UPDATED_MONTO_INICIAL);
    }

    @Test
    @Transactional
    void getAllCobrosByMontoInicialIsInShouldWork() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoInicial in DEFAULT_MONTO_INICIAL or UPDATED_MONTO_INICIAL
        defaultCobrosShouldBeFound("montoInicial.in=" + DEFAULT_MONTO_INICIAL + "," + UPDATED_MONTO_INICIAL);

        // Get all the cobrosList where montoInicial equals to UPDATED_MONTO_INICIAL
        defaultCobrosShouldNotBeFound("montoInicial.in=" + UPDATED_MONTO_INICIAL);
    }

    @Test
    @Transactional
    void getAllCobrosByMontoInicialIsNullOrNotNull() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoInicial is not null
        defaultCobrosShouldBeFound("montoInicial.specified=true");

        // Get all the cobrosList where montoInicial is null
        defaultCobrosShouldNotBeFound("montoInicial.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrosByMontoInicialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoInicial is greater than or equal to DEFAULT_MONTO_INICIAL
        defaultCobrosShouldBeFound("montoInicial.greaterThanOrEqual=" + DEFAULT_MONTO_INICIAL);

        // Get all the cobrosList where montoInicial is greater than or equal to UPDATED_MONTO_INICIAL
        defaultCobrosShouldNotBeFound("montoInicial.greaterThanOrEqual=" + UPDATED_MONTO_INICIAL);
    }

    @Test
    @Transactional
    void getAllCobrosByMontoInicialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoInicial is less than or equal to DEFAULT_MONTO_INICIAL
        defaultCobrosShouldBeFound("montoInicial.lessThanOrEqual=" + DEFAULT_MONTO_INICIAL);

        // Get all the cobrosList where montoInicial is less than or equal to SMALLER_MONTO_INICIAL
        defaultCobrosShouldNotBeFound("montoInicial.lessThanOrEqual=" + SMALLER_MONTO_INICIAL);
    }

    @Test
    @Transactional
    void getAllCobrosByMontoInicialIsLessThanSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoInicial is less than DEFAULT_MONTO_INICIAL
        defaultCobrosShouldNotBeFound("montoInicial.lessThan=" + DEFAULT_MONTO_INICIAL);

        // Get all the cobrosList where montoInicial is less than UPDATED_MONTO_INICIAL
        defaultCobrosShouldBeFound("montoInicial.lessThan=" + UPDATED_MONTO_INICIAL);
    }

    @Test
    @Transactional
    void getAllCobrosByMontoInicialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where montoInicial is greater than DEFAULT_MONTO_INICIAL
        defaultCobrosShouldNotBeFound("montoInicial.greaterThan=" + DEFAULT_MONTO_INICIAL);

        // Get all the cobrosList where montoInicial is greater than SMALLER_MONTO_INICIAL
        defaultCobrosShouldBeFound("montoInicial.greaterThan=" + SMALLER_MONTO_INICIAL);
    }

    @Test
    @Transactional
    void getAllCobrosBySaldoIsEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where saldo equals to DEFAULT_SALDO
        defaultCobrosShouldBeFound("saldo.equals=" + DEFAULT_SALDO);

        // Get all the cobrosList where saldo equals to UPDATED_SALDO
        defaultCobrosShouldNotBeFound("saldo.equals=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllCobrosBySaldoIsInShouldWork() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where saldo in DEFAULT_SALDO or UPDATED_SALDO
        defaultCobrosShouldBeFound("saldo.in=" + DEFAULT_SALDO + "," + UPDATED_SALDO);

        // Get all the cobrosList where saldo equals to UPDATED_SALDO
        defaultCobrosShouldNotBeFound("saldo.in=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllCobrosBySaldoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where saldo is not null
        defaultCobrosShouldBeFound("saldo.specified=true");

        // Get all the cobrosList where saldo is null
        defaultCobrosShouldNotBeFound("saldo.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrosBySaldoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where saldo is greater than or equal to DEFAULT_SALDO
        defaultCobrosShouldBeFound("saldo.greaterThanOrEqual=" + DEFAULT_SALDO);

        // Get all the cobrosList where saldo is greater than or equal to UPDATED_SALDO
        defaultCobrosShouldNotBeFound("saldo.greaterThanOrEqual=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllCobrosBySaldoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where saldo is less than or equal to DEFAULT_SALDO
        defaultCobrosShouldBeFound("saldo.lessThanOrEqual=" + DEFAULT_SALDO);

        // Get all the cobrosList where saldo is less than or equal to SMALLER_SALDO
        defaultCobrosShouldNotBeFound("saldo.lessThanOrEqual=" + SMALLER_SALDO);
    }

    @Test
    @Transactional
    void getAllCobrosBySaldoIsLessThanSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where saldo is less than DEFAULT_SALDO
        defaultCobrosShouldNotBeFound("saldo.lessThan=" + DEFAULT_SALDO);

        // Get all the cobrosList where saldo is less than UPDATED_SALDO
        defaultCobrosShouldBeFound("saldo.lessThan=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllCobrosBySaldoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where saldo is greater than DEFAULT_SALDO
        defaultCobrosShouldNotBeFound("saldo.greaterThan=" + DEFAULT_SALDO);

        // Get all the cobrosList where saldo is greater than SMALLER_SALDO
        defaultCobrosShouldBeFound("saldo.greaterThan=" + SMALLER_SALDO);
    }

    @Test
    @Transactional
    void getAllCobrosByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultCobrosShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the cobrosList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultCobrosShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCobrosByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultCobrosShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the cobrosList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultCobrosShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCobrosByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaRegistro is not null
        defaultCobrosShouldBeFound("fechaRegistro.specified=true");

        // Get all the cobrosList where fechaRegistro is null
        defaultCobrosShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrosByFechaRegistroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaRegistro is greater than or equal to DEFAULT_FECHA_REGISTRO
        defaultCobrosShouldBeFound("fechaRegistro.greaterThanOrEqual=" + DEFAULT_FECHA_REGISTRO);

        // Get all the cobrosList where fechaRegistro is greater than or equal to UPDATED_FECHA_REGISTRO
        defaultCobrosShouldNotBeFound("fechaRegistro.greaterThanOrEqual=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCobrosByFechaRegistroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaRegistro is less than or equal to DEFAULT_FECHA_REGISTRO
        defaultCobrosShouldBeFound("fechaRegistro.lessThanOrEqual=" + DEFAULT_FECHA_REGISTRO);

        // Get all the cobrosList where fechaRegistro is less than or equal to SMALLER_FECHA_REGISTRO
        defaultCobrosShouldNotBeFound("fechaRegistro.lessThanOrEqual=" + SMALLER_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCobrosByFechaRegistroIsLessThanSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaRegistro is less than DEFAULT_FECHA_REGISTRO
        defaultCobrosShouldNotBeFound("fechaRegistro.lessThan=" + DEFAULT_FECHA_REGISTRO);

        // Get all the cobrosList where fechaRegistro is less than UPDATED_FECHA_REGISTRO
        defaultCobrosShouldBeFound("fechaRegistro.lessThan=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCobrosByFechaRegistroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaRegistro is greater than DEFAULT_FECHA_REGISTRO
        defaultCobrosShouldNotBeFound("fechaRegistro.greaterThan=" + DEFAULT_FECHA_REGISTRO);

        // Get all the cobrosList where fechaRegistro is greater than SMALLER_FECHA_REGISTRO
        defaultCobrosShouldBeFound("fechaRegistro.greaterThan=" + SMALLER_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCobrosByFechaPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaPago equals to DEFAULT_FECHA_PAGO
        defaultCobrosShouldBeFound("fechaPago.equals=" + DEFAULT_FECHA_PAGO);

        // Get all the cobrosList where fechaPago equals to UPDATED_FECHA_PAGO
        defaultCobrosShouldNotBeFound("fechaPago.equals=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByFechaPagoIsInShouldWork() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaPago in DEFAULT_FECHA_PAGO or UPDATED_FECHA_PAGO
        defaultCobrosShouldBeFound("fechaPago.in=" + DEFAULT_FECHA_PAGO + "," + UPDATED_FECHA_PAGO);

        // Get all the cobrosList where fechaPago equals to UPDATED_FECHA_PAGO
        defaultCobrosShouldNotBeFound("fechaPago.in=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByFechaPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaPago is not null
        defaultCobrosShouldBeFound("fechaPago.specified=true");

        // Get all the cobrosList where fechaPago is null
        defaultCobrosShouldNotBeFound("fechaPago.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrosByFechaPagoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaPago is greater than or equal to DEFAULT_FECHA_PAGO
        defaultCobrosShouldBeFound("fechaPago.greaterThanOrEqual=" + DEFAULT_FECHA_PAGO);

        // Get all the cobrosList where fechaPago is greater than or equal to UPDATED_FECHA_PAGO
        defaultCobrosShouldNotBeFound("fechaPago.greaterThanOrEqual=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByFechaPagoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaPago is less than or equal to DEFAULT_FECHA_PAGO
        defaultCobrosShouldBeFound("fechaPago.lessThanOrEqual=" + DEFAULT_FECHA_PAGO);

        // Get all the cobrosList where fechaPago is less than or equal to SMALLER_FECHA_PAGO
        defaultCobrosShouldNotBeFound("fechaPago.lessThanOrEqual=" + SMALLER_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByFechaPagoIsLessThanSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaPago is less than DEFAULT_FECHA_PAGO
        defaultCobrosShouldNotBeFound("fechaPago.lessThan=" + DEFAULT_FECHA_PAGO);

        // Get all the cobrosList where fechaPago is less than UPDATED_FECHA_PAGO
        defaultCobrosShouldBeFound("fechaPago.lessThan=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByFechaPagoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where fechaPago is greater than DEFAULT_FECHA_PAGO
        defaultCobrosShouldNotBeFound("fechaPago.greaterThan=" + DEFAULT_FECHA_PAGO);

        // Get all the cobrosList where fechaPago is greater than SMALLER_FECHA_PAGO
        defaultCobrosShouldBeFound("fechaPago.greaterThan=" + SMALLER_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByTipoPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where tipoPago equals to DEFAULT_TIPO_PAGO
        defaultCobrosShouldBeFound("tipoPago.equals=" + DEFAULT_TIPO_PAGO);

        // Get all the cobrosList where tipoPago equals to UPDATED_TIPO_PAGO
        defaultCobrosShouldNotBeFound("tipoPago.equals=" + UPDATED_TIPO_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByTipoPagoIsInShouldWork() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where tipoPago in DEFAULT_TIPO_PAGO or UPDATED_TIPO_PAGO
        defaultCobrosShouldBeFound("tipoPago.in=" + DEFAULT_TIPO_PAGO + "," + UPDATED_TIPO_PAGO);

        // Get all the cobrosList where tipoPago equals to UPDATED_TIPO_PAGO
        defaultCobrosShouldNotBeFound("tipoPago.in=" + UPDATED_TIPO_PAGO);
    }

    @Test
    @Transactional
    void getAllCobrosByTipoPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where tipoPago is not null
        defaultCobrosShouldBeFound("tipoPago.specified=true");

        // Get all the cobrosList where tipoPago is null
        defaultCobrosShouldNotBeFound("tipoPago.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where descripcion equals to DEFAULT_DESCRIPCION
        defaultCobrosShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the cobrosList where descripcion equals to UPDATED_DESCRIPCION
        defaultCobrosShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCobrosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultCobrosShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the cobrosList where descripcion equals to UPDATED_DESCRIPCION
        defaultCobrosShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCobrosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where descripcion is not null
        defaultCobrosShouldBeFound("descripcion.specified=true");

        // Get all the cobrosList where descripcion is null
        defaultCobrosShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where descripcion contains DEFAULT_DESCRIPCION
        defaultCobrosShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the cobrosList where descripcion contains UPDATED_DESCRIPCION
        defaultCobrosShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCobrosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        // Get all the cobrosList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultCobrosShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the cobrosList where descripcion does not contain UPDATED_DESCRIPCION
        defaultCobrosShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCobrosByAlumnosIsEqualToSomething() throws Exception {
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            cobrosRepository.saveAndFlush(cobros);
            alumnos = AlumnosResourceIT.createEntity(em);
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        em.persist(alumnos);
        em.flush();
        cobros.setAlumnos(alumnos);
        cobrosRepository.saveAndFlush(cobros);
        Long alumnosId = alumnos.getId();

        // Get all the cobrosList where alumnos equals to alumnosId
        defaultCobrosShouldBeFound("alumnosId.equals=" + alumnosId);

        // Get all the cobrosList where alumnos equals to (alumnosId + 1)
        defaultCobrosShouldNotBeFound("alumnosId.equals=" + (alumnosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCobrosShouldBeFound(String filter) throws Exception {
        restCobrosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cobros.getId().intValue())))
            .andExpect(jsonPath("$.[*].montoPago").value(hasItem(DEFAULT_MONTO_PAGO)))
            .andExpect(jsonPath("$.[*].montoInicial").value(hasItem(DEFAULT_MONTO_INICIAL)))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(DEFAULT_SALDO)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].fechaPago").value(hasItem(DEFAULT_FECHA_PAGO.toString())))
            .andExpect(jsonPath("$.[*].tipoPago").value(hasItem(DEFAULT_TIPO_PAGO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restCobrosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCobrosShouldNotBeFound(String filter) throws Exception {
        restCobrosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCobrosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCobros() throws Exception {
        // Get the cobros
        restCobrosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCobros() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        int databaseSizeBeforeUpdate = cobrosRepository.findAll().size();

        // Update the cobros
        Cobros updatedCobros = cobrosRepository.findById(cobros.getId()).get();
        // Disconnect from session so that the updates on updatedCobros are not directly saved in db
        em.detach(updatedCobros);
        updatedCobros
            .montoPago(UPDATED_MONTO_PAGO)
            .montoInicial(UPDATED_MONTO_INICIAL)
            .saldo(UPDATED_SALDO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaPago(UPDATED_FECHA_PAGO)
            .tipoPago(UPDATED_TIPO_PAGO)
            .descripcion(UPDATED_DESCRIPCION);

        restCobrosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCobros.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCobros))
            )
            .andExpect(status().isOk());

        // Validate the Cobros in the database
        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeUpdate);
        Cobros testCobros = cobrosList.get(cobrosList.size() - 1);
        assertThat(testCobros.getMontoPago()).isEqualTo(UPDATED_MONTO_PAGO);
        assertThat(testCobros.getMontoInicial()).isEqualTo(UPDATED_MONTO_INICIAL);
        assertThat(testCobros.getSaldo()).isEqualTo(UPDATED_SALDO);
        assertThat(testCobros.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testCobros.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testCobros.getTipoPago()).isEqualTo(UPDATED_TIPO_PAGO);
        assertThat(testCobros.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingCobros() throws Exception {
        int databaseSizeBeforeUpdate = cobrosRepository.findAll().size();
        cobros.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCobrosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cobros.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cobros))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cobros in the database
        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCobros() throws Exception {
        int databaseSizeBeforeUpdate = cobrosRepository.findAll().size();
        cobros.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCobrosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cobros))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cobros in the database
        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCobros() throws Exception {
        int databaseSizeBeforeUpdate = cobrosRepository.findAll().size();
        cobros.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCobrosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cobros)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cobros in the database
        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCobrosWithPatch() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        int databaseSizeBeforeUpdate = cobrosRepository.findAll().size();

        // Update the cobros using partial update
        Cobros partialUpdatedCobros = new Cobros();
        partialUpdatedCobros.setId(cobros.getId());

        partialUpdatedCobros
            .montoPago(UPDATED_MONTO_PAGO)
            .montoInicial(UPDATED_MONTO_INICIAL)
            .fechaPago(UPDATED_FECHA_PAGO)
            .tipoPago(UPDATED_TIPO_PAGO)
            .descripcion(UPDATED_DESCRIPCION);

        restCobrosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCobros.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCobros))
            )
            .andExpect(status().isOk());

        // Validate the Cobros in the database
        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeUpdate);
        Cobros testCobros = cobrosList.get(cobrosList.size() - 1);
        assertThat(testCobros.getMontoPago()).isEqualTo(UPDATED_MONTO_PAGO);
        assertThat(testCobros.getMontoInicial()).isEqualTo(UPDATED_MONTO_INICIAL);
        assertThat(testCobros.getSaldo()).isEqualTo(DEFAULT_SALDO);
        assertThat(testCobros.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testCobros.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testCobros.getTipoPago()).isEqualTo(UPDATED_TIPO_PAGO);
        assertThat(testCobros.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateCobrosWithPatch() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        int databaseSizeBeforeUpdate = cobrosRepository.findAll().size();

        // Update the cobros using partial update
        Cobros partialUpdatedCobros = new Cobros();
        partialUpdatedCobros.setId(cobros.getId());

        partialUpdatedCobros
            .montoPago(UPDATED_MONTO_PAGO)
            .montoInicial(UPDATED_MONTO_INICIAL)
            .saldo(UPDATED_SALDO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaPago(UPDATED_FECHA_PAGO)
            .tipoPago(UPDATED_TIPO_PAGO)
            .descripcion(UPDATED_DESCRIPCION);

        restCobrosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCobros.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCobros))
            )
            .andExpect(status().isOk());

        // Validate the Cobros in the database
        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeUpdate);
        Cobros testCobros = cobrosList.get(cobrosList.size() - 1);
        assertThat(testCobros.getMontoPago()).isEqualTo(UPDATED_MONTO_PAGO);
        assertThat(testCobros.getMontoInicial()).isEqualTo(UPDATED_MONTO_INICIAL);
        assertThat(testCobros.getSaldo()).isEqualTo(UPDATED_SALDO);
        assertThat(testCobros.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testCobros.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testCobros.getTipoPago()).isEqualTo(UPDATED_TIPO_PAGO);
        assertThat(testCobros.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingCobros() throws Exception {
        int databaseSizeBeforeUpdate = cobrosRepository.findAll().size();
        cobros.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCobrosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cobros.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cobros))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cobros in the database
        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCobros() throws Exception {
        int databaseSizeBeforeUpdate = cobrosRepository.findAll().size();
        cobros.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCobrosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cobros))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cobros in the database
        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCobros() throws Exception {
        int databaseSizeBeforeUpdate = cobrosRepository.findAll().size();
        cobros.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCobrosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cobros)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cobros in the database
        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCobros() throws Exception {
        // Initialize the database
        cobrosRepository.saveAndFlush(cobros);

        int databaseSizeBeforeDelete = cobrosRepository.findAll().size();

        // Delete the cobros
        restCobrosMockMvc
            .perform(delete(ENTITY_API_URL_ID, cobros.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cobros> cobrosList = cobrosRepository.findAll();
        assertThat(cobrosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
