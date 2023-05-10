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
import py.com.abf.domain.Funcionarios;
import py.com.abf.domain.Pagos;
import py.com.abf.domain.enumeration.TiposPagos;
import py.com.abf.repository.PagosRepository;
import py.com.abf.service.PagosService;
import py.com.abf.service.criteria.PagosCriteria;

/**
 * Integration tests for the {@link PagosResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PagosResourceIT {

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

    private static final TiposPagos DEFAULT_TIPO_PAGO = TiposPagos.PARCIAL;
    private static final TiposPagos UPDATED_TIPO_PAGO = TiposPagos.CUOTA;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pagos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PagosRepository pagosRepository;

    @Mock
    private PagosRepository pagosRepositoryMock;

    @Mock
    private PagosService pagosServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPagosMockMvc;

    private Pagos pagos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pagos createEntity(EntityManager em) {
        Pagos pagos = new Pagos()
            .montoPago(DEFAULT_MONTO_PAGO)
            .montoInicial(DEFAULT_MONTO_INICIAL)
            .saldo(DEFAULT_SALDO)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO)
            .fechaPago(DEFAULT_FECHA_PAGO)
            .tipoPago(DEFAULT_TIPO_PAGO)
            .descripcion(DEFAULT_DESCRIPCION);
        // Add required entity
        Funcionarios funcionarios;
        if (TestUtil.findAll(em, Funcionarios.class).isEmpty()) {
            funcionarios = FuncionariosResourceIT.createEntity(em);
            em.persist(funcionarios);
            em.flush();
        } else {
            funcionarios = TestUtil.findAll(em, Funcionarios.class).get(0);
        }
        pagos.setFuncionarios(funcionarios);
        return pagos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pagos createUpdatedEntity(EntityManager em) {
        Pagos pagos = new Pagos()
            .montoPago(UPDATED_MONTO_PAGO)
            .montoInicial(UPDATED_MONTO_INICIAL)
            .saldo(UPDATED_SALDO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaPago(UPDATED_FECHA_PAGO)
            .tipoPago(UPDATED_TIPO_PAGO)
            .descripcion(UPDATED_DESCRIPCION);
        // Add required entity
        Funcionarios funcionarios;
        if (TestUtil.findAll(em, Funcionarios.class).isEmpty()) {
            funcionarios = FuncionariosResourceIT.createUpdatedEntity(em);
            em.persist(funcionarios);
            em.flush();
        } else {
            funcionarios = TestUtil.findAll(em, Funcionarios.class).get(0);
        }
        pagos.setFuncionarios(funcionarios);
        return pagos;
    }

    @BeforeEach
    public void initTest() {
        pagos = createEntity(em);
    }

    @Test
    @Transactional
    void createPagos() throws Exception {
        int databaseSizeBeforeCreate = pagosRepository.findAll().size();
        // Create the Pagos
        restPagosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagos)))
            .andExpect(status().isCreated());

        // Validate the Pagos in the database
        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeCreate + 1);
        Pagos testPagos = pagosList.get(pagosList.size() - 1);
        assertThat(testPagos.getMontoPago()).isEqualTo(DEFAULT_MONTO_PAGO);
        assertThat(testPagos.getMontoInicial()).isEqualTo(DEFAULT_MONTO_INICIAL);
        assertThat(testPagos.getSaldo()).isEqualTo(DEFAULT_SALDO);
        assertThat(testPagos.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testPagos.getFechaPago()).isEqualTo(DEFAULT_FECHA_PAGO);
        assertThat(testPagos.getTipoPago()).isEqualTo(DEFAULT_TIPO_PAGO);
        assertThat(testPagos.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createPagosWithExistingId() throws Exception {
        // Create the Pagos with an existing ID
        pagos.setId(1L);

        int databaseSizeBeforeCreate = pagosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPagosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagos)))
            .andExpect(status().isBadRequest());

        // Validate the Pagos in the database
        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMontoPagoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagosRepository.findAll().size();
        // set the field null
        pagos.setMontoPago(null);

        // Create the Pagos, which fails.

        restPagosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagos)))
            .andExpect(status().isBadRequest());

        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontoInicialIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagosRepository.findAll().size();
        // set the field null
        pagos.setMontoInicial(null);

        // Create the Pagos, which fails.

        restPagosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagos)))
            .andExpect(status().isBadRequest());

        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSaldoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagosRepository.findAll().size();
        // set the field null
        pagos.setSaldo(null);

        // Create the Pagos, which fails.

        restPagosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagos)))
            .andExpect(status().isBadRequest());

        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaRegistroIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagosRepository.findAll().size();
        // set the field null
        pagos.setFechaRegistro(null);

        // Create the Pagos, which fails.

        restPagosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagos)))
            .andExpect(status().isBadRequest());

        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaPagoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagosRepository.findAll().size();
        // set the field null
        pagos.setFechaPago(null);

        // Create the Pagos, which fails.

        restPagosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagos)))
            .andExpect(status().isBadRequest());

        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoPagoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagosRepository.findAll().size();
        // set the field null
        pagos.setTipoPago(null);

        // Create the Pagos, which fails.

        restPagosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagos)))
            .andExpect(status().isBadRequest());

        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagosRepository.findAll().size();
        // set the field null
        pagos.setDescripcion(null);

        // Create the Pagos, which fails.

        restPagosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagos)))
            .andExpect(status().isBadRequest());

        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPagos() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList
        restPagosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pagos.getId().intValue())))
            .andExpect(jsonPath("$.[*].montoPago").value(hasItem(DEFAULT_MONTO_PAGO)))
            .andExpect(jsonPath("$.[*].montoInicial").value(hasItem(DEFAULT_MONTO_INICIAL)))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(DEFAULT_SALDO)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].fechaPago").value(hasItem(DEFAULT_FECHA_PAGO.toString())))
            .andExpect(jsonPath("$.[*].tipoPago").value(hasItem(DEFAULT_TIPO_PAGO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPagosWithEagerRelationshipsIsEnabled() throws Exception {
        when(pagosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPagosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pagosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPagosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pagosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPagosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pagosRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPagos() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get the pagos
        restPagosMockMvc
            .perform(get(ENTITY_API_URL_ID, pagos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pagos.getId().intValue()))
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
    void getPagosByIdFiltering() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        Long id = pagos.getId();

        defaultPagosShouldBeFound("id.equals=" + id);
        defaultPagosShouldNotBeFound("id.notEquals=" + id);

        defaultPagosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPagosShouldNotBeFound("id.greaterThan=" + id);

        defaultPagosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPagosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPagosByMontoPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoPago equals to DEFAULT_MONTO_PAGO
        defaultPagosShouldBeFound("montoPago.equals=" + DEFAULT_MONTO_PAGO);

        // Get all the pagosList where montoPago equals to UPDATED_MONTO_PAGO
        defaultPagosShouldNotBeFound("montoPago.equals=" + UPDATED_MONTO_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByMontoPagoIsInShouldWork() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoPago in DEFAULT_MONTO_PAGO or UPDATED_MONTO_PAGO
        defaultPagosShouldBeFound("montoPago.in=" + DEFAULT_MONTO_PAGO + "," + UPDATED_MONTO_PAGO);

        // Get all the pagosList where montoPago equals to UPDATED_MONTO_PAGO
        defaultPagosShouldNotBeFound("montoPago.in=" + UPDATED_MONTO_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByMontoPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoPago is not null
        defaultPagosShouldBeFound("montoPago.specified=true");

        // Get all the pagosList where montoPago is null
        defaultPagosShouldNotBeFound("montoPago.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByMontoPagoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoPago is greater than or equal to DEFAULT_MONTO_PAGO
        defaultPagosShouldBeFound("montoPago.greaterThanOrEqual=" + DEFAULT_MONTO_PAGO);

        // Get all the pagosList where montoPago is greater than or equal to UPDATED_MONTO_PAGO
        defaultPagosShouldNotBeFound("montoPago.greaterThanOrEqual=" + UPDATED_MONTO_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByMontoPagoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoPago is less than or equal to DEFAULT_MONTO_PAGO
        defaultPagosShouldBeFound("montoPago.lessThanOrEqual=" + DEFAULT_MONTO_PAGO);

        // Get all the pagosList where montoPago is less than or equal to SMALLER_MONTO_PAGO
        defaultPagosShouldNotBeFound("montoPago.lessThanOrEqual=" + SMALLER_MONTO_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByMontoPagoIsLessThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoPago is less than DEFAULT_MONTO_PAGO
        defaultPagosShouldNotBeFound("montoPago.lessThan=" + DEFAULT_MONTO_PAGO);

        // Get all the pagosList where montoPago is less than UPDATED_MONTO_PAGO
        defaultPagosShouldBeFound("montoPago.lessThan=" + UPDATED_MONTO_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByMontoPagoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoPago is greater than DEFAULT_MONTO_PAGO
        defaultPagosShouldNotBeFound("montoPago.greaterThan=" + DEFAULT_MONTO_PAGO);

        // Get all the pagosList where montoPago is greater than SMALLER_MONTO_PAGO
        defaultPagosShouldBeFound("montoPago.greaterThan=" + SMALLER_MONTO_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByMontoInicialIsEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoInicial equals to DEFAULT_MONTO_INICIAL
        defaultPagosShouldBeFound("montoInicial.equals=" + DEFAULT_MONTO_INICIAL);

        // Get all the pagosList where montoInicial equals to UPDATED_MONTO_INICIAL
        defaultPagosShouldNotBeFound("montoInicial.equals=" + UPDATED_MONTO_INICIAL);
    }

    @Test
    @Transactional
    void getAllPagosByMontoInicialIsInShouldWork() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoInicial in DEFAULT_MONTO_INICIAL or UPDATED_MONTO_INICIAL
        defaultPagosShouldBeFound("montoInicial.in=" + DEFAULT_MONTO_INICIAL + "," + UPDATED_MONTO_INICIAL);

        // Get all the pagosList where montoInicial equals to UPDATED_MONTO_INICIAL
        defaultPagosShouldNotBeFound("montoInicial.in=" + UPDATED_MONTO_INICIAL);
    }

    @Test
    @Transactional
    void getAllPagosByMontoInicialIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoInicial is not null
        defaultPagosShouldBeFound("montoInicial.specified=true");

        // Get all the pagosList where montoInicial is null
        defaultPagosShouldNotBeFound("montoInicial.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByMontoInicialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoInicial is greater than or equal to DEFAULT_MONTO_INICIAL
        defaultPagosShouldBeFound("montoInicial.greaterThanOrEqual=" + DEFAULT_MONTO_INICIAL);

        // Get all the pagosList where montoInicial is greater than or equal to UPDATED_MONTO_INICIAL
        defaultPagosShouldNotBeFound("montoInicial.greaterThanOrEqual=" + UPDATED_MONTO_INICIAL);
    }

    @Test
    @Transactional
    void getAllPagosByMontoInicialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoInicial is less than or equal to DEFAULT_MONTO_INICIAL
        defaultPagosShouldBeFound("montoInicial.lessThanOrEqual=" + DEFAULT_MONTO_INICIAL);

        // Get all the pagosList where montoInicial is less than or equal to SMALLER_MONTO_INICIAL
        defaultPagosShouldNotBeFound("montoInicial.lessThanOrEqual=" + SMALLER_MONTO_INICIAL);
    }

    @Test
    @Transactional
    void getAllPagosByMontoInicialIsLessThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoInicial is less than DEFAULT_MONTO_INICIAL
        defaultPagosShouldNotBeFound("montoInicial.lessThan=" + DEFAULT_MONTO_INICIAL);

        // Get all the pagosList where montoInicial is less than UPDATED_MONTO_INICIAL
        defaultPagosShouldBeFound("montoInicial.lessThan=" + UPDATED_MONTO_INICIAL);
    }

    @Test
    @Transactional
    void getAllPagosByMontoInicialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where montoInicial is greater than DEFAULT_MONTO_INICIAL
        defaultPagosShouldNotBeFound("montoInicial.greaterThan=" + DEFAULT_MONTO_INICIAL);

        // Get all the pagosList where montoInicial is greater than SMALLER_MONTO_INICIAL
        defaultPagosShouldBeFound("montoInicial.greaterThan=" + SMALLER_MONTO_INICIAL);
    }

    @Test
    @Transactional
    void getAllPagosBySaldoIsEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where saldo equals to DEFAULT_SALDO
        defaultPagosShouldBeFound("saldo.equals=" + DEFAULT_SALDO);

        // Get all the pagosList where saldo equals to UPDATED_SALDO
        defaultPagosShouldNotBeFound("saldo.equals=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllPagosBySaldoIsInShouldWork() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where saldo in DEFAULT_SALDO or UPDATED_SALDO
        defaultPagosShouldBeFound("saldo.in=" + DEFAULT_SALDO + "," + UPDATED_SALDO);

        // Get all the pagosList where saldo equals to UPDATED_SALDO
        defaultPagosShouldNotBeFound("saldo.in=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllPagosBySaldoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where saldo is not null
        defaultPagosShouldBeFound("saldo.specified=true");

        // Get all the pagosList where saldo is null
        defaultPagosShouldNotBeFound("saldo.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosBySaldoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where saldo is greater than or equal to DEFAULT_SALDO
        defaultPagosShouldBeFound("saldo.greaterThanOrEqual=" + DEFAULT_SALDO);

        // Get all the pagosList where saldo is greater than or equal to UPDATED_SALDO
        defaultPagosShouldNotBeFound("saldo.greaterThanOrEqual=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllPagosBySaldoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where saldo is less than or equal to DEFAULT_SALDO
        defaultPagosShouldBeFound("saldo.lessThanOrEqual=" + DEFAULT_SALDO);

        // Get all the pagosList where saldo is less than or equal to SMALLER_SALDO
        defaultPagosShouldNotBeFound("saldo.lessThanOrEqual=" + SMALLER_SALDO);
    }

    @Test
    @Transactional
    void getAllPagosBySaldoIsLessThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where saldo is less than DEFAULT_SALDO
        defaultPagosShouldNotBeFound("saldo.lessThan=" + DEFAULT_SALDO);

        // Get all the pagosList where saldo is less than UPDATED_SALDO
        defaultPagosShouldBeFound("saldo.lessThan=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllPagosBySaldoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where saldo is greater than DEFAULT_SALDO
        defaultPagosShouldNotBeFound("saldo.greaterThan=" + DEFAULT_SALDO);

        // Get all the pagosList where saldo is greater than SMALLER_SALDO
        defaultPagosShouldBeFound("saldo.greaterThan=" + SMALLER_SALDO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultPagosShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the pagosList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultPagosShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultPagosShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the pagosList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultPagosShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaRegistro is not null
        defaultPagosShouldBeFound("fechaRegistro.specified=true");

        // Get all the pagosList where fechaRegistro is null
        defaultPagosShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByFechaRegistroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaRegistro is greater than or equal to DEFAULT_FECHA_REGISTRO
        defaultPagosShouldBeFound("fechaRegistro.greaterThanOrEqual=" + DEFAULT_FECHA_REGISTRO);

        // Get all the pagosList where fechaRegistro is greater than or equal to UPDATED_FECHA_REGISTRO
        defaultPagosShouldNotBeFound("fechaRegistro.greaterThanOrEqual=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaRegistroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaRegistro is less than or equal to DEFAULT_FECHA_REGISTRO
        defaultPagosShouldBeFound("fechaRegistro.lessThanOrEqual=" + DEFAULT_FECHA_REGISTRO);

        // Get all the pagosList where fechaRegistro is less than or equal to SMALLER_FECHA_REGISTRO
        defaultPagosShouldNotBeFound("fechaRegistro.lessThanOrEqual=" + SMALLER_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaRegistroIsLessThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaRegistro is less than DEFAULT_FECHA_REGISTRO
        defaultPagosShouldNotBeFound("fechaRegistro.lessThan=" + DEFAULT_FECHA_REGISTRO);

        // Get all the pagosList where fechaRegistro is less than UPDATED_FECHA_REGISTRO
        defaultPagosShouldBeFound("fechaRegistro.lessThan=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaRegistroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaRegistro is greater than DEFAULT_FECHA_REGISTRO
        defaultPagosShouldNotBeFound("fechaRegistro.greaterThan=" + DEFAULT_FECHA_REGISTRO);

        // Get all the pagosList where fechaRegistro is greater than SMALLER_FECHA_REGISTRO
        defaultPagosShouldBeFound("fechaRegistro.greaterThan=" + SMALLER_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaPago equals to DEFAULT_FECHA_PAGO
        defaultPagosShouldBeFound("fechaPago.equals=" + DEFAULT_FECHA_PAGO);

        // Get all the pagosList where fechaPago equals to UPDATED_FECHA_PAGO
        defaultPagosShouldNotBeFound("fechaPago.equals=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsInShouldWork() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaPago in DEFAULT_FECHA_PAGO or UPDATED_FECHA_PAGO
        defaultPagosShouldBeFound("fechaPago.in=" + DEFAULT_FECHA_PAGO + "," + UPDATED_FECHA_PAGO);

        // Get all the pagosList where fechaPago equals to UPDATED_FECHA_PAGO
        defaultPagosShouldNotBeFound("fechaPago.in=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaPago is not null
        defaultPagosShouldBeFound("fechaPago.specified=true");

        // Get all the pagosList where fechaPago is null
        defaultPagosShouldNotBeFound("fechaPago.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaPago is greater than or equal to DEFAULT_FECHA_PAGO
        defaultPagosShouldBeFound("fechaPago.greaterThanOrEqual=" + DEFAULT_FECHA_PAGO);

        // Get all the pagosList where fechaPago is greater than or equal to UPDATED_FECHA_PAGO
        defaultPagosShouldNotBeFound("fechaPago.greaterThanOrEqual=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaPago is less than or equal to DEFAULT_FECHA_PAGO
        defaultPagosShouldBeFound("fechaPago.lessThanOrEqual=" + DEFAULT_FECHA_PAGO);

        // Get all the pagosList where fechaPago is less than or equal to SMALLER_FECHA_PAGO
        defaultPagosShouldNotBeFound("fechaPago.lessThanOrEqual=" + SMALLER_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsLessThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaPago is less than DEFAULT_FECHA_PAGO
        defaultPagosShouldNotBeFound("fechaPago.lessThan=" + DEFAULT_FECHA_PAGO);

        // Get all the pagosList where fechaPago is less than UPDATED_FECHA_PAGO
        defaultPagosShouldBeFound("fechaPago.lessThan=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fechaPago is greater than DEFAULT_FECHA_PAGO
        defaultPagosShouldNotBeFound("fechaPago.greaterThan=" + DEFAULT_FECHA_PAGO);

        // Get all the pagosList where fechaPago is greater than SMALLER_FECHA_PAGO
        defaultPagosShouldBeFound("fechaPago.greaterThan=" + SMALLER_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByTipoPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where tipoPago equals to DEFAULT_TIPO_PAGO
        defaultPagosShouldBeFound("tipoPago.equals=" + DEFAULT_TIPO_PAGO);

        // Get all the pagosList where tipoPago equals to UPDATED_TIPO_PAGO
        defaultPagosShouldNotBeFound("tipoPago.equals=" + UPDATED_TIPO_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByTipoPagoIsInShouldWork() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where tipoPago in DEFAULT_TIPO_PAGO or UPDATED_TIPO_PAGO
        defaultPagosShouldBeFound("tipoPago.in=" + DEFAULT_TIPO_PAGO + "," + UPDATED_TIPO_PAGO);

        // Get all the pagosList where tipoPago equals to UPDATED_TIPO_PAGO
        defaultPagosShouldNotBeFound("tipoPago.in=" + UPDATED_TIPO_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByTipoPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where tipoPago is not null
        defaultPagosShouldBeFound("tipoPago.specified=true");

        // Get all the pagosList where tipoPago is null
        defaultPagosShouldNotBeFound("tipoPago.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where descripcion equals to DEFAULT_DESCRIPCION
        defaultPagosShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the pagosList where descripcion equals to UPDATED_DESCRIPCION
        defaultPagosShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPagosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultPagosShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the pagosList where descripcion equals to UPDATED_DESCRIPCION
        defaultPagosShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPagosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where descripcion is not null
        defaultPagosShouldBeFound("descripcion.specified=true");

        // Get all the pagosList where descripcion is null
        defaultPagosShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where descripcion contains DEFAULT_DESCRIPCION
        defaultPagosShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the pagosList where descripcion contains UPDATED_DESCRIPCION
        defaultPagosShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPagosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultPagosShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the pagosList where descripcion does not contain UPDATED_DESCRIPCION
        defaultPagosShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPagosByFuncionariosIsEqualToSomething() throws Exception {
        Funcionarios funcionarios;
        if (TestUtil.findAll(em, Funcionarios.class).isEmpty()) {
            pagosRepository.saveAndFlush(pagos);
            funcionarios = FuncionariosResourceIT.createEntity(em);
        } else {
            funcionarios = TestUtil.findAll(em, Funcionarios.class).get(0);
        }
        em.persist(funcionarios);
        em.flush();
        pagos.setFuncionarios(funcionarios);
        pagosRepository.saveAndFlush(pagos);
        Long funcionariosId = funcionarios.getId();

        // Get all the pagosList where funcionarios equals to funcionariosId
        defaultPagosShouldBeFound("funcionariosId.equals=" + funcionariosId);

        // Get all the pagosList where funcionarios equals to (funcionariosId + 1)
        defaultPagosShouldNotBeFound("funcionariosId.equals=" + (funcionariosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPagosShouldBeFound(String filter) throws Exception {
        restPagosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pagos.getId().intValue())))
            .andExpect(jsonPath("$.[*].montoPago").value(hasItem(DEFAULT_MONTO_PAGO)))
            .andExpect(jsonPath("$.[*].montoInicial").value(hasItem(DEFAULT_MONTO_INICIAL)))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(DEFAULT_SALDO)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].fechaPago").value(hasItem(DEFAULT_FECHA_PAGO.toString())))
            .andExpect(jsonPath("$.[*].tipoPago").value(hasItem(DEFAULT_TIPO_PAGO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restPagosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPagosShouldNotBeFound(String filter) throws Exception {
        restPagosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPagosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPagos() throws Exception {
        // Get the pagos
        restPagosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPagos() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        int databaseSizeBeforeUpdate = pagosRepository.findAll().size();

        // Update the pagos
        Pagos updatedPagos = pagosRepository.findById(pagos.getId()).get();
        // Disconnect from session so that the updates on updatedPagos are not directly saved in db
        em.detach(updatedPagos);
        updatedPagos
            .montoPago(UPDATED_MONTO_PAGO)
            .montoInicial(UPDATED_MONTO_INICIAL)
            .saldo(UPDATED_SALDO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaPago(UPDATED_FECHA_PAGO)
            .tipoPago(UPDATED_TIPO_PAGO)
            .descripcion(UPDATED_DESCRIPCION);

        restPagosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPagos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPagos))
            )
            .andExpect(status().isOk());

        // Validate the Pagos in the database
        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeUpdate);
        Pagos testPagos = pagosList.get(pagosList.size() - 1);
        assertThat(testPagos.getMontoPago()).isEqualTo(UPDATED_MONTO_PAGO);
        assertThat(testPagos.getMontoInicial()).isEqualTo(UPDATED_MONTO_INICIAL);
        assertThat(testPagos.getSaldo()).isEqualTo(UPDATED_SALDO);
        assertThat(testPagos.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testPagos.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testPagos.getTipoPago()).isEqualTo(UPDATED_TIPO_PAGO);
        assertThat(testPagos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingPagos() throws Exception {
        int databaseSizeBeforeUpdate = pagosRepository.findAll().size();
        pagos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pagos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pagos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pagos in the database
        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPagos() throws Exception {
        int databaseSizeBeforeUpdate = pagosRepository.findAll().size();
        pagos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pagos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pagos in the database
        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPagos() throws Exception {
        int databaseSizeBeforeUpdate = pagosRepository.findAll().size();
        pagos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pagos in the database
        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePagosWithPatch() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        int databaseSizeBeforeUpdate = pagosRepository.findAll().size();

        // Update the pagos using partial update
        Pagos partialUpdatedPagos = new Pagos();
        partialUpdatedPagos.setId(pagos.getId());

        partialUpdatedPagos
            .montoInicial(UPDATED_MONTO_INICIAL)
            .saldo(UPDATED_SALDO)
            .fechaPago(UPDATED_FECHA_PAGO)
            .descripcion(UPDATED_DESCRIPCION);

        restPagosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPagos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPagos))
            )
            .andExpect(status().isOk());

        // Validate the Pagos in the database
        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeUpdate);
        Pagos testPagos = pagosList.get(pagosList.size() - 1);
        assertThat(testPagos.getMontoPago()).isEqualTo(DEFAULT_MONTO_PAGO);
        assertThat(testPagos.getMontoInicial()).isEqualTo(UPDATED_MONTO_INICIAL);
        assertThat(testPagos.getSaldo()).isEqualTo(UPDATED_SALDO);
        assertThat(testPagos.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testPagos.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testPagos.getTipoPago()).isEqualTo(DEFAULT_TIPO_PAGO);
        assertThat(testPagos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdatePagosWithPatch() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        int databaseSizeBeforeUpdate = pagosRepository.findAll().size();

        // Update the pagos using partial update
        Pagos partialUpdatedPagos = new Pagos();
        partialUpdatedPagos.setId(pagos.getId());

        partialUpdatedPagos
            .montoPago(UPDATED_MONTO_PAGO)
            .montoInicial(UPDATED_MONTO_INICIAL)
            .saldo(UPDATED_SALDO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaPago(UPDATED_FECHA_PAGO)
            .tipoPago(UPDATED_TIPO_PAGO)
            .descripcion(UPDATED_DESCRIPCION);

        restPagosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPagos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPagos))
            )
            .andExpect(status().isOk());

        // Validate the Pagos in the database
        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeUpdate);
        Pagos testPagos = pagosList.get(pagosList.size() - 1);
        assertThat(testPagos.getMontoPago()).isEqualTo(UPDATED_MONTO_PAGO);
        assertThat(testPagos.getMontoInicial()).isEqualTo(UPDATED_MONTO_INICIAL);
        assertThat(testPagos.getSaldo()).isEqualTo(UPDATED_SALDO);
        assertThat(testPagos.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testPagos.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testPagos.getTipoPago()).isEqualTo(UPDATED_TIPO_PAGO);
        assertThat(testPagos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingPagos() throws Exception {
        int databaseSizeBeforeUpdate = pagosRepository.findAll().size();
        pagos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pagos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pagos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pagos in the database
        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPagos() throws Exception {
        int databaseSizeBeforeUpdate = pagosRepository.findAll().size();
        pagos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pagos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pagos in the database
        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPagos() throws Exception {
        int databaseSizeBeforeUpdate = pagosRepository.findAll().size();
        pagos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pagos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pagos in the database
        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePagos() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        int databaseSizeBeforeDelete = pagosRepository.findAll().size();

        // Delete the pagos
        restPagosMockMvc
            .perform(delete(ENTITY_API_URL_ID, pagos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
