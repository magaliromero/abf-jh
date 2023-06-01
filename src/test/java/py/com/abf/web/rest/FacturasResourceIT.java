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
import py.com.abf.domain.FacturaDetalle;
import py.com.abf.domain.Facturas;
import py.com.abf.domain.enumeration.CondicionVenta;
import py.com.abf.repository.FacturasRepository;
import py.com.abf.service.criteria.FacturasCriteria;

/**
 * Integration tests for the {@link FacturasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacturasResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_FACTURA_NRO = "AAAAAAAAAA";
    private static final String UPDATED_FACTURA_NRO = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIMBRADO = 1;
    private static final Integer UPDATED_TIMBRADO = 2;
    private static final Integer SMALLER_TIMBRADO = 1 - 1;

    private static final String DEFAULT_RAZON_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAZON_SOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_RUC = "AAAAAAAAAA";
    private static final String UPDATED_RUC = "BBBBBBBBBB";

    private static final CondicionVenta DEFAULT_CONDICION_VENTA = CondicionVenta.CONTADO;
    private static final CondicionVenta UPDATED_CONDICION_VENTA = CondicionVenta.CONTADO;

    private static final Integer DEFAULT_TOTAL = 1;
    private static final Integer UPDATED_TOTAL = 2;
    private static final Integer SMALLER_TOTAL = 1 - 1;

    private static final String ENTITY_API_URL = "/api/facturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacturasRepository facturasRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturasMockMvc;

    private Facturas facturas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facturas createEntity(EntityManager em) {
        Facturas facturas = new Facturas()
            .fecha(DEFAULT_FECHA)
            .facturaNro(DEFAULT_FACTURA_NRO)
            .timbrado(DEFAULT_TIMBRADO)
            .razonSocial(DEFAULT_RAZON_SOCIAL)
            .ruc(DEFAULT_RUC)
            .condicionVenta(DEFAULT_CONDICION_VENTA)
            .total(DEFAULT_TOTAL);
        return facturas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facturas createUpdatedEntity(EntityManager em) {
        Facturas facturas = new Facturas()
            .fecha(UPDATED_FECHA)
            .facturaNro(UPDATED_FACTURA_NRO)
            .timbrado(UPDATED_TIMBRADO)
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .ruc(UPDATED_RUC)
            .condicionVenta(UPDATED_CONDICION_VENTA)
            .total(UPDATED_TOTAL);
        return facturas;
    }

    @BeforeEach
    public void initTest() {
        facturas = createEntity(em);
    }

    @Test
    @Transactional
    void createFacturas() throws Exception {
        int databaseSizeBeforeCreate = facturasRepository.findAll().size();
        // Create the Facturas
        restFacturasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isCreated());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeCreate + 1);
        Facturas testFacturas = facturasList.get(facturasList.size() - 1);
        assertThat(testFacturas.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testFacturas.getFacturaNro()).isEqualTo(DEFAULT_FACTURA_NRO);
        assertThat(testFacturas.getTimbrado()).isEqualTo(DEFAULT_TIMBRADO);
        assertThat(testFacturas.getRazonSocial()).isEqualTo(DEFAULT_RAZON_SOCIAL);
        assertThat(testFacturas.getRuc()).isEqualTo(DEFAULT_RUC);
        assertThat(testFacturas.getCondicionVenta()).isEqualTo(DEFAULT_CONDICION_VENTA);
        assertThat(testFacturas.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void createFacturasWithExistingId() throws Exception {
        // Create the Facturas with an existing ID
        facturas.setId(1L);

        int databaseSizeBeforeCreate = facturasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isBadRequest());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturasRepository.findAll().size();
        // set the field null
        facturas.setFecha(null);

        // Create the Facturas, which fails.

        restFacturasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isBadRequest());

        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFacturaNroIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturasRepository.findAll().size();
        // set the field null
        facturas.setFacturaNro(null);

        // Create the Facturas, which fails.

        restFacturasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isBadRequest());

        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimbradoIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturasRepository.findAll().size();
        // set the field null
        facturas.setTimbrado(null);

        // Create the Facturas, which fails.

        restFacturasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isBadRequest());

        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRazonSocialIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturasRepository.findAll().size();
        // set the field null
        facturas.setRazonSocial(null);

        // Create the Facturas, which fails.

        restFacturasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isBadRequest());

        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRucIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturasRepository.findAll().size();
        // set the field null
        facturas.setRuc(null);

        // Create the Facturas, which fails.

        restFacturasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isBadRequest());

        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCondicionVentaIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturasRepository.findAll().size();
        // set the field null
        facturas.setCondicionVenta(null);

        // Create the Facturas, which fails.

        restFacturasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isBadRequest());

        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturasRepository.findAll().size();
        // set the field null
        facturas.setTotal(null);

        // Create the Facturas, which fails.

        restFacturasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isBadRequest());

        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacturas() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList
        restFacturasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facturas.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].facturaNro").value(hasItem(DEFAULT_FACTURA_NRO)))
            .andExpect(jsonPath("$.[*].timbrado").value(hasItem(DEFAULT_TIMBRADO)))
            .andExpect(jsonPath("$.[*].razonSocial").value(hasItem(DEFAULT_RAZON_SOCIAL)))
            .andExpect(jsonPath("$.[*].ruc").value(hasItem(DEFAULT_RUC)))
            .andExpect(jsonPath("$.[*].condicionVenta").value(hasItem(DEFAULT_CONDICION_VENTA.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)));
    }

    @Test
    @Transactional
    void getFacturas() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get the facturas
        restFacturasMockMvc
            .perform(get(ENTITY_API_URL_ID, facturas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facturas.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.facturaNro").value(DEFAULT_FACTURA_NRO))
            .andExpect(jsonPath("$.timbrado").value(DEFAULT_TIMBRADO))
            .andExpect(jsonPath("$.razonSocial").value(DEFAULT_RAZON_SOCIAL))
            .andExpect(jsonPath("$.ruc").value(DEFAULT_RUC))
            .andExpect(jsonPath("$.condicionVenta").value(DEFAULT_CONDICION_VENTA.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL));
    }

    @Test
    @Transactional
    void getFacturasByIdFiltering() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        Long id = facturas.getId();

        defaultFacturasShouldBeFound("id.equals=" + id);
        defaultFacturasShouldNotBeFound("id.notEquals=" + id);

        defaultFacturasShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFacturasShouldNotBeFound("id.greaterThan=" + id);

        defaultFacturasShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFacturasShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where fecha equals to DEFAULT_FECHA
        defaultFacturasShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the facturasList where fecha equals to UPDATED_FECHA
        defaultFacturasShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultFacturasShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the facturasList where fecha equals to UPDATED_FECHA
        defaultFacturasShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where fecha is not null
        defaultFacturasShouldBeFound("fecha.specified=true");

        // Get all the facturasList where fecha is null
        defaultFacturasShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where fecha is greater than or equal to DEFAULT_FECHA
        defaultFacturasShouldBeFound("fecha.greaterThanOrEqual=" + DEFAULT_FECHA);

        // Get all the facturasList where fecha is greater than or equal to UPDATED_FECHA
        defaultFacturasShouldNotBeFound("fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where fecha is less than or equal to DEFAULT_FECHA
        defaultFacturasShouldBeFound("fecha.lessThanOrEqual=" + DEFAULT_FECHA);

        // Get all the facturasList where fecha is less than or equal to SMALLER_FECHA
        defaultFacturasShouldNotBeFound("fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where fecha is less than DEFAULT_FECHA
        defaultFacturasShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the facturasList where fecha is less than UPDATED_FECHA
        defaultFacturasShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where fecha is greater than DEFAULT_FECHA
        defaultFacturasShouldNotBeFound("fecha.greaterThan=" + DEFAULT_FECHA);

        // Get all the facturasList where fecha is greater than SMALLER_FECHA
        defaultFacturasShouldBeFound("fecha.greaterThan=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFacturaNroIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where facturaNro equals to DEFAULT_FACTURA_NRO
        defaultFacturasShouldBeFound("facturaNro.equals=" + DEFAULT_FACTURA_NRO);

        // Get all the facturasList where facturaNro equals to UPDATED_FACTURA_NRO
        defaultFacturasShouldNotBeFound("facturaNro.equals=" + UPDATED_FACTURA_NRO);
    }

    @Test
    @Transactional
    void getAllFacturasByFacturaNroIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where facturaNro in DEFAULT_FACTURA_NRO or UPDATED_FACTURA_NRO
        defaultFacturasShouldBeFound("facturaNro.in=" + DEFAULT_FACTURA_NRO + "," + UPDATED_FACTURA_NRO);

        // Get all the facturasList where facturaNro equals to UPDATED_FACTURA_NRO
        defaultFacturasShouldNotBeFound("facturaNro.in=" + UPDATED_FACTURA_NRO);
    }

    @Test
    @Transactional
    void getAllFacturasByFacturaNroIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where facturaNro is not null
        defaultFacturasShouldBeFound("facturaNro.specified=true");

        // Get all the facturasList where facturaNro is null
        defaultFacturasShouldNotBeFound("facturaNro.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByFacturaNroContainsSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where facturaNro contains DEFAULT_FACTURA_NRO
        defaultFacturasShouldBeFound("facturaNro.contains=" + DEFAULT_FACTURA_NRO);

        // Get all the facturasList where facturaNro contains UPDATED_FACTURA_NRO
        defaultFacturasShouldNotBeFound("facturaNro.contains=" + UPDATED_FACTURA_NRO);
    }

    @Test
    @Transactional
    void getAllFacturasByFacturaNroNotContainsSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where facturaNro does not contain DEFAULT_FACTURA_NRO
        defaultFacturasShouldNotBeFound("facturaNro.doesNotContain=" + DEFAULT_FACTURA_NRO);

        // Get all the facturasList where facturaNro does not contain UPDATED_FACTURA_NRO
        defaultFacturasShouldBeFound("facturaNro.doesNotContain=" + UPDATED_FACTURA_NRO);
    }

    @Test
    @Transactional
    void getAllFacturasByTimbradoIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where timbrado equals to DEFAULT_TIMBRADO
        defaultFacturasShouldBeFound("timbrado.equals=" + DEFAULT_TIMBRADO);

        // Get all the facturasList where timbrado equals to UPDATED_TIMBRADO
        defaultFacturasShouldNotBeFound("timbrado.equals=" + UPDATED_TIMBRADO);
    }

    @Test
    @Transactional
    void getAllFacturasByTimbradoIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where timbrado in DEFAULT_TIMBRADO or UPDATED_TIMBRADO
        defaultFacturasShouldBeFound("timbrado.in=" + DEFAULT_TIMBRADO + "," + UPDATED_TIMBRADO);

        // Get all the facturasList where timbrado equals to UPDATED_TIMBRADO
        defaultFacturasShouldNotBeFound("timbrado.in=" + UPDATED_TIMBRADO);
    }

    @Test
    @Transactional
    void getAllFacturasByTimbradoIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where timbrado is not null
        defaultFacturasShouldBeFound("timbrado.specified=true");

        // Get all the facturasList where timbrado is null
        defaultFacturasShouldNotBeFound("timbrado.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByTimbradoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where timbrado is greater than or equal to DEFAULT_TIMBRADO
        defaultFacturasShouldBeFound("timbrado.greaterThanOrEqual=" + DEFAULT_TIMBRADO);

        // Get all the facturasList where timbrado is greater than or equal to UPDATED_TIMBRADO
        defaultFacturasShouldNotBeFound("timbrado.greaterThanOrEqual=" + UPDATED_TIMBRADO);
    }

    @Test
    @Transactional
    void getAllFacturasByTimbradoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where timbrado is less than or equal to DEFAULT_TIMBRADO
        defaultFacturasShouldBeFound("timbrado.lessThanOrEqual=" + DEFAULT_TIMBRADO);

        // Get all the facturasList where timbrado is less than or equal to SMALLER_TIMBRADO
        defaultFacturasShouldNotBeFound("timbrado.lessThanOrEqual=" + SMALLER_TIMBRADO);
    }

    @Test
    @Transactional
    void getAllFacturasByTimbradoIsLessThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where timbrado is less than DEFAULT_TIMBRADO
        defaultFacturasShouldNotBeFound("timbrado.lessThan=" + DEFAULT_TIMBRADO);

        // Get all the facturasList where timbrado is less than UPDATED_TIMBRADO
        defaultFacturasShouldBeFound("timbrado.lessThan=" + UPDATED_TIMBRADO);
    }

    @Test
    @Transactional
    void getAllFacturasByTimbradoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where timbrado is greater than DEFAULT_TIMBRADO
        defaultFacturasShouldNotBeFound("timbrado.greaterThan=" + DEFAULT_TIMBRADO);

        // Get all the facturasList where timbrado is greater than SMALLER_TIMBRADO
        defaultFacturasShouldBeFound("timbrado.greaterThan=" + SMALLER_TIMBRADO);
    }

    @Test
    @Transactional
    void getAllFacturasByRazonSocialIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where razonSocial equals to DEFAULT_RAZON_SOCIAL
        defaultFacturasShouldBeFound("razonSocial.equals=" + DEFAULT_RAZON_SOCIAL);

        // Get all the facturasList where razonSocial equals to UPDATED_RAZON_SOCIAL
        defaultFacturasShouldNotBeFound("razonSocial.equals=" + UPDATED_RAZON_SOCIAL);
    }

    @Test
    @Transactional
    void getAllFacturasByRazonSocialIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where razonSocial in DEFAULT_RAZON_SOCIAL or UPDATED_RAZON_SOCIAL
        defaultFacturasShouldBeFound("razonSocial.in=" + DEFAULT_RAZON_SOCIAL + "," + UPDATED_RAZON_SOCIAL);

        // Get all the facturasList where razonSocial equals to UPDATED_RAZON_SOCIAL
        defaultFacturasShouldNotBeFound("razonSocial.in=" + UPDATED_RAZON_SOCIAL);
    }

    @Test
    @Transactional
    void getAllFacturasByRazonSocialIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where razonSocial is not null
        defaultFacturasShouldBeFound("razonSocial.specified=true");

        // Get all the facturasList where razonSocial is null
        defaultFacturasShouldNotBeFound("razonSocial.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByRazonSocialContainsSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where razonSocial contains DEFAULT_RAZON_SOCIAL
        defaultFacturasShouldBeFound("razonSocial.contains=" + DEFAULT_RAZON_SOCIAL);

        // Get all the facturasList where razonSocial contains UPDATED_RAZON_SOCIAL
        defaultFacturasShouldNotBeFound("razonSocial.contains=" + UPDATED_RAZON_SOCIAL);
    }

    @Test
    @Transactional
    void getAllFacturasByRazonSocialNotContainsSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where razonSocial does not contain DEFAULT_RAZON_SOCIAL
        defaultFacturasShouldNotBeFound("razonSocial.doesNotContain=" + DEFAULT_RAZON_SOCIAL);

        // Get all the facturasList where razonSocial does not contain UPDATED_RAZON_SOCIAL
        defaultFacturasShouldBeFound("razonSocial.doesNotContain=" + UPDATED_RAZON_SOCIAL);
    }

    @Test
    @Transactional
    void getAllFacturasByRucIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where ruc equals to DEFAULT_RUC
        defaultFacturasShouldBeFound("ruc.equals=" + DEFAULT_RUC);

        // Get all the facturasList where ruc equals to UPDATED_RUC
        defaultFacturasShouldNotBeFound("ruc.equals=" + UPDATED_RUC);
    }

    @Test
    @Transactional
    void getAllFacturasByRucIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where ruc in DEFAULT_RUC or UPDATED_RUC
        defaultFacturasShouldBeFound("ruc.in=" + DEFAULT_RUC + "," + UPDATED_RUC);

        // Get all the facturasList where ruc equals to UPDATED_RUC
        defaultFacturasShouldNotBeFound("ruc.in=" + UPDATED_RUC);
    }

    @Test
    @Transactional
    void getAllFacturasByRucIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where ruc is not null
        defaultFacturasShouldBeFound("ruc.specified=true");

        // Get all the facturasList where ruc is null
        defaultFacturasShouldNotBeFound("ruc.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByRucContainsSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where ruc contains DEFAULT_RUC
        defaultFacturasShouldBeFound("ruc.contains=" + DEFAULT_RUC);

        // Get all the facturasList where ruc contains UPDATED_RUC
        defaultFacturasShouldNotBeFound("ruc.contains=" + UPDATED_RUC);
    }

    @Test
    @Transactional
    void getAllFacturasByRucNotContainsSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where ruc does not contain DEFAULT_RUC
        defaultFacturasShouldNotBeFound("ruc.doesNotContain=" + DEFAULT_RUC);

        // Get all the facturasList where ruc does not contain UPDATED_RUC
        defaultFacturasShouldBeFound("ruc.doesNotContain=" + UPDATED_RUC);
    }

    @Test
    @Transactional
    void getAllFacturasByCondicionVentaIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where condicionVenta equals to DEFAULT_CONDICION_VENTA
        defaultFacturasShouldBeFound("condicionVenta.equals=" + DEFAULT_CONDICION_VENTA);

        // Get all the facturasList where condicionVenta equals to UPDATED_CONDICION_VENTA
        defaultFacturasShouldNotBeFound("condicionVenta.equals=" + UPDATED_CONDICION_VENTA);
    }

    @Test
    @Transactional
    void getAllFacturasByCondicionVentaIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where condicionVenta in DEFAULT_CONDICION_VENTA or UPDATED_CONDICION_VENTA
        defaultFacturasShouldBeFound("condicionVenta.in=" + DEFAULT_CONDICION_VENTA + "," + UPDATED_CONDICION_VENTA);

        // Get all the facturasList where condicionVenta equals to UPDATED_CONDICION_VENTA
        defaultFacturasShouldNotBeFound("condicionVenta.in=" + UPDATED_CONDICION_VENTA);
    }

    @Test
    @Transactional
    void getAllFacturasByCondicionVentaIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where condicionVenta is not null
        defaultFacturasShouldBeFound("condicionVenta.specified=true");

        // Get all the facturasList where condicionVenta is null
        defaultFacturasShouldNotBeFound("condicionVenta.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total equals to DEFAULT_TOTAL
        defaultFacturasShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the facturasList where total equals to UPDATED_TOTAL
        defaultFacturasShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultFacturasShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the facturasList where total equals to UPDATED_TOTAL
        defaultFacturasShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total is not null
        defaultFacturasShouldBeFound("total.specified=true");

        // Get all the facturasList where total is null
        defaultFacturasShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total is greater than or equal to DEFAULT_TOTAL
        defaultFacturasShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the facturasList where total is greater than or equal to UPDATED_TOTAL
        defaultFacturasShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total is less than or equal to DEFAULT_TOTAL
        defaultFacturasShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the facturasList where total is less than or equal to SMALLER_TOTAL
        defaultFacturasShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total is less than DEFAULT_TOTAL
        defaultFacturasShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the facturasList where total is less than UPDATED_TOTAL
        defaultFacturasShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total is greater than DEFAULT_TOTAL
        defaultFacturasShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the facturasList where total is greater than SMALLER_TOTAL
        defaultFacturasShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByFacturaDetalleIsEqualToSomething() throws Exception {
        FacturaDetalle facturaDetalle;
        if (TestUtil.findAll(em, FacturaDetalle.class).isEmpty()) {
            facturasRepository.saveAndFlush(facturas);
            facturaDetalle = FacturaDetalleResourceIT.createEntity(em);
        } else {
            facturaDetalle = TestUtil.findAll(em, FacturaDetalle.class).get(0);
        }
        em.persist(facturaDetalle);
        em.flush();
        facturas.addFacturaDetalle(facturaDetalle);
        facturasRepository.saveAndFlush(facturas);
        Long facturaDetalleId = facturaDetalle.getId();

        // Get all the facturasList where facturaDetalle equals to facturaDetalleId
        defaultFacturasShouldBeFound("facturaDetalleId.equals=" + facturaDetalleId);

        // Get all the facturasList where facturaDetalle equals to (facturaDetalleId + 1)
        defaultFacturasShouldNotBeFound("facturaDetalleId.equals=" + (facturaDetalleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacturasShouldBeFound(String filter) throws Exception {
        restFacturasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facturas.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].facturaNro").value(hasItem(DEFAULT_FACTURA_NRO)))
            .andExpect(jsonPath("$.[*].timbrado").value(hasItem(DEFAULT_TIMBRADO)))
            .andExpect(jsonPath("$.[*].razonSocial").value(hasItem(DEFAULT_RAZON_SOCIAL)))
            .andExpect(jsonPath("$.[*].ruc").value(hasItem(DEFAULT_RUC)))
            .andExpect(jsonPath("$.[*].condicionVenta").value(hasItem(DEFAULT_CONDICION_VENTA.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)));

        // Check, that the count call also returns 1
        restFacturasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacturasShouldNotBeFound(String filter) throws Exception {
        restFacturasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacturasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacturas() throws Exception {
        // Get the facturas
        restFacturasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacturas() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();

        // Update the facturas
        Facturas updatedFacturas = facturasRepository.findById(facturas.getId()).get();
        // Disconnect from session so that the updates on updatedFacturas are not directly saved in db
        em.detach(updatedFacturas);
        updatedFacturas
            .fecha(UPDATED_FECHA)
            .facturaNro(UPDATED_FACTURA_NRO)
            .timbrado(UPDATED_TIMBRADO)
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .ruc(UPDATED_RUC)
            .condicionVenta(UPDATED_CONDICION_VENTA)
            .total(UPDATED_TOTAL);

        restFacturasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacturas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFacturas))
            )
            .andExpect(status().isOk());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
        Facturas testFacturas = facturasList.get(facturasList.size() - 1);
        assertThat(testFacturas.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testFacturas.getFacturaNro()).isEqualTo(UPDATED_FACTURA_NRO);
        assertThat(testFacturas.getTimbrado()).isEqualTo(UPDATED_TIMBRADO);
        assertThat(testFacturas.getRazonSocial()).isEqualTo(UPDATED_RAZON_SOCIAL);
        assertThat(testFacturas.getRuc()).isEqualTo(UPDATED_RUC);
        assertThat(testFacturas.getCondicionVenta()).isEqualTo(UPDATED_CONDICION_VENTA);
        assertThat(testFacturas.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingFacturas() throws Exception {
        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();
        facturas.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacturas() throws Exception {
        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();
        facturas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacturas() throws Exception {
        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();
        facturas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacturasWithPatch() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();

        // Update the facturas using partial update
        Facturas partialUpdatedFacturas = new Facturas();
        partialUpdatedFacturas.setId(facturas.getId());

        partialUpdatedFacturas
            .facturaNro(UPDATED_FACTURA_NRO)
            .timbrado(UPDATED_TIMBRADO)
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .ruc(UPDATED_RUC)
            .condicionVenta(UPDATED_CONDICION_VENTA)
            .total(UPDATED_TOTAL);

        restFacturasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacturas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacturas))
            )
            .andExpect(status().isOk());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
        Facturas testFacturas = facturasList.get(facturasList.size() - 1);
        assertThat(testFacturas.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testFacturas.getFacturaNro()).isEqualTo(UPDATED_FACTURA_NRO);
        assertThat(testFacturas.getTimbrado()).isEqualTo(UPDATED_TIMBRADO);
        assertThat(testFacturas.getRazonSocial()).isEqualTo(UPDATED_RAZON_SOCIAL);
        assertThat(testFacturas.getRuc()).isEqualTo(UPDATED_RUC);
        assertThat(testFacturas.getCondicionVenta()).isEqualTo(UPDATED_CONDICION_VENTA);
        assertThat(testFacturas.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateFacturasWithPatch() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();

        // Update the facturas using partial update
        Facturas partialUpdatedFacturas = new Facturas();
        partialUpdatedFacturas.setId(facturas.getId());

        partialUpdatedFacturas
            .fecha(UPDATED_FECHA)
            .facturaNro(UPDATED_FACTURA_NRO)
            .timbrado(UPDATED_TIMBRADO)
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .ruc(UPDATED_RUC)
            .condicionVenta(UPDATED_CONDICION_VENTA)
            .total(UPDATED_TOTAL);

        restFacturasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacturas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacturas))
            )
            .andExpect(status().isOk());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
        Facturas testFacturas = facturasList.get(facturasList.size() - 1);
        assertThat(testFacturas.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testFacturas.getFacturaNro()).isEqualTo(UPDATED_FACTURA_NRO);
        assertThat(testFacturas.getTimbrado()).isEqualTo(UPDATED_TIMBRADO);
        assertThat(testFacturas.getRazonSocial()).isEqualTo(UPDATED_RAZON_SOCIAL);
        assertThat(testFacturas.getRuc()).isEqualTo(UPDATED_RUC);
        assertThat(testFacturas.getCondicionVenta()).isEqualTo(UPDATED_CONDICION_VENTA);
        assertThat(testFacturas.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingFacturas() throws Exception {
        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();
        facturas.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facturas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacturas() throws Exception {
        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();
        facturas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacturas() throws Exception {
        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();
        facturas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturasMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacturas() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        int databaseSizeBeforeDelete = facturasRepository.findAll().size();

        // Delete the facturas
        restFacturasMockMvc
            .perform(delete(ENTITY_API_URL_ID, facturas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
