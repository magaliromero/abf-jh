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
import py.com.abf.domain.Facturas;
import py.com.abf.domain.enumeration.CondVenta;
import py.com.abf.repository.FacturasRepository;
import py.com.abf.service.FacturasService;
import py.com.abf.service.criteria.FacturasCriteria;

/**
 * Integration tests for the {@link FacturasResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
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

    private static final Integer DEFAULT_RUC = 1;
    private static final Integer UPDATED_RUC = 2;
    private static final Integer SMALLER_RUC = 1 - 1;

    private static final CondVenta DEFAULT_CONDICION_VENTA = CondVenta.CONTADO;
    private static final CondVenta UPDATED_CONDICION_VENTA = CondVenta.CREDITO;

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRECIO_UNITARIO = 1;
    private static final Integer UPDATED_PRECIO_UNITARIO = 2;
    private static final Integer SMALLER_PRECIO_UNITARIO = 1 - 1;

    private static final Integer DEFAULT_VALOR_5 = 1;
    private static final Integer UPDATED_VALOR_5 = 2;
    private static final Integer SMALLER_VALOR_5 = 1 - 1;

    private static final Integer DEFAULT_VALOR_10 = 1;
    private static final Integer UPDATED_VALOR_10 = 2;
    private static final Integer SMALLER_VALOR_10 = 1 - 1;

    private static final Integer DEFAULT_TOTAL = 1;
    private static final Integer UPDATED_TOTAL = 2;
    private static final Integer SMALLER_TOTAL = 1 - 1;

    private static final Integer DEFAULT_TOTAL_5 = 1;
    private static final Integer UPDATED_TOTAL_5 = 2;
    private static final Integer SMALLER_TOTAL_5 = 1 - 1;

    private static final Integer DEFAULT_TOTAL_10 = 1;
    private static final Integer UPDATED_TOTAL_10 = 2;
    private static final Integer SMALLER_TOTAL_10 = 1 - 1;

    private static final Integer DEFAULT_TOTAL_IVA = 1;
    private static final Integer UPDATED_TOTAL_IVA = 2;
    private static final Integer SMALLER_TOTAL_IVA = 1 - 1;

    private static final String ENTITY_API_URL = "/api/facturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacturasRepository facturasRepository;

    @Mock
    private FacturasRepository facturasRepositoryMock;

    @Mock
    private FacturasService facturasServiceMock;

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
            .ruc(DEFAULT_RUC)
            .condicionVenta(DEFAULT_CONDICION_VENTA)
            .cantidad(DEFAULT_CANTIDAD)
            .descripcion(DEFAULT_DESCRIPCION)
            .precioUnitario(DEFAULT_PRECIO_UNITARIO)
            .valor5(DEFAULT_VALOR_5)
            .valor10(DEFAULT_VALOR_10)
            .total(DEFAULT_TOTAL)
            .total5(DEFAULT_TOTAL_5)
            .total10(DEFAULT_TOTAL_10)
            .totalIva(DEFAULT_TOTAL_IVA);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        facturas.setAlumnos(alumnos);
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
            .ruc(UPDATED_RUC)
            .condicionVenta(UPDATED_CONDICION_VENTA)
            .cantidad(UPDATED_CANTIDAD)
            .descripcion(UPDATED_DESCRIPCION)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .valor5(UPDATED_VALOR_5)
            .valor10(UPDATED_VALOR_10)
            .total(UPDATED_TOTAL)
            .total5(UPDATED_TOTAL_5)
            .total10(UPDATED_TOTAL_10)
            .totalIva(UPDATED_TOTAL_IVA);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createUpdatedEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        facturas.setAlumnos(alumnos);
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
        assertThat(testFacturas.getRuc()).isEqualTo(DEFAULT_RUC);
        assertThat(testFacturas.getCondicionVenta()).isEqualTo(DEFAULT_CONDICION_VENTA);
        assertThat(testFacturas.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testFacturas.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testFacturas.getPrecioUnitario()).isEqualTo(DEFAULT_PRECIO_UNITARIO);
        assertThat(testFacturas.getValor5()).isEqualTo(DEFAULT_VALOR_5);
        assertThat(testFacturas.getValor10()).isEqualTo(DEFAULT_VALOR_10);
        assertThat(testFacturas.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testFacturas.getTotal5()).isEqualTo(DEFAULT_TOTAL_5);
        assertThat(testFacturas.getTotal10()).isEqualTo(DEFAULT_TOTAL_10);
        assertThat(testFacturas.getTotalIva()).isEqualTo(DEFAULT_TOTAL_IVA);
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
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturasRepository.findAll().size();
        // set the field null
        facturas.setDescripcion(null);

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
            .andExpect(jsonPath("$.[*].ruc").value(hasItem(DEFAULT_RUC)))
            .andExpect(jsonPath("$.[*].condicionVenta").value(hasItem(DEFAULT_CONDICION_VENTA.toString())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(DEFAULT_PRECIO_UNITARIO)))
            .andExpect(jsonPath("$.[*].valor5").value(hasItem(DEFAULT_VALOR_5)))
            .andExpect(jsonPath("$.[*].valor10").value(hasItem(DEFAULT_VALOR_10)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].total5").value(hasItem(DEFAULT_TOTAL_5)))
            .andExpect(jsonPath("$.[*].total10").value(hasItem(DEFAULT_TOTAL_10)))
            .andExpect(jsonPath("$.[*].totalIva").value(hasItem(DEFAULT_TOTAL_IVA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacturasWithEagerRelationshipsIsEnabled() throws Exception {
        when(facturasServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacturasMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facturasServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacturasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(facturasServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacturasMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(facturasRepositoryMock, times(1)).findAll(any(Pageable.class));
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
            .andExpect(jsonPath("$.ruc").value(DEFAULT_RUC))
            .andExpect(jsonPath("$.condicionVenta").value(DEFAULT_CONDICION_VENTA.toString()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precioUnitario").value(DEFAULT_PRECIO_UNITARIO))
            .andExpect(jsonPath("$.valor5").value(DEFAULT_VALOR_5))
            .andExpect(jsonPath("$.valor10").value(DEFAULT_VALOR_10))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL))
            .andExpect(jsonPath("$.total5").value(DEFAULT_TOTAL_5))
            .andExpect(jsonPath("$.total10").value(DEFAULT_TOTAL_10))
            .andExpect(jsonPath("$.totalIva").value(DEFAULT_TOTAL_IVA));
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
    void getAllFacturasByRucIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where ruc is greater than or equal to DEFAULT_RUC
        defaultFacturasShouldBeFound("ruc.greaterThanOrEqual=" + DEFAULT_RUC);

        // Get all the facturasList where ruc is greater than or equal to UPDATED_RUC
        defaultFacturasShouldNotBeFound("ruc.greaterThanOrEqual=" + UPDATED_RUC);
    }

    @Test
    @Transactional
    void getAllFacturasByRucIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where ruc is less than or equal to DEFAULT_RUC
        defaultFacturasShouldBeFound("ruc.lessThanOrEqual=" + DEFAULT_RUC);

        // Get all the facturasList where ruc is less than or equal to SMALLER_RUC
        defaultFacturasShouldNotBeFound("ruc.lessThanOrEqual=" + SMALLER_RUC);
    }

    @Test
    @Transactional
    void getAllFacturasByRucIsLessThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where ruc is less than DEFAULT_RUC
        defaultFacturasShouldNotBeFound("ruc.lessThan=" + DEFAULT_RUC);

        // Get all the facturasList where ruc is less than UPDATED_RUC
        defaultFacturasShouldBeFound("ruc.lessThan=" + UPDATED_RUC);
    }

    @Test
    @Transactional
    void getAllFacturasByRucIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where ruc is greater than DEFAULT_RUC
        defaultFacturasShouldNotBeFound("ruc.greaterThan=" + DEFAULT_RUC);

        // Get all the facturasList where ruc is greater than SMALLER_RUC
        defaultFacturasShouldBeFound("ruc.greaterThan=" + SMALLER_RUC);
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
    void getAllFacturasByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where cantidad equals to DEFAULT_CANTIDAD
        defaultFacturasShouldBeFound("cantidad.equals=" + DEFAULT_CANTIDAD);

        // Get all the facturasList where cantidad equals to UPDATED_CANTIDAD
        defaultFacturasShouldNotBeFound("cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllFacturasByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where cantidad in DEFAULT_CANTIDAD or UPDATED_CANTIDAD
        defaultFacturasShouldBeFound("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD);

        // Get all the facturasList where cantidad equals to UPDATED_CANTIDAD
        defaultFacturasShouldNotBeFound("cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllFacturasByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where cantidad is not null
        defaultFacturasShouldBeFound("cantidad.specified=true");

        // Get all the facturasList where cantidad is null
        defaultFacturasShouldNotBeFound("cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where cantidad is greater than or equal to DEFAULT_CANTIDAD
        defaultFacturasShouldBeFound("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the facturasList where cantidad is greater than or equal to UPDATED_CANTIDAD
        defaultFacturasShouldNotBeFound("cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllFacturasByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where cantidad is less than or equal to DEFAULT_CANTIDAD
        defaultFacturasShouldBeFound("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the facturasList where cantidad is less than or equal to SMALLER_CANTIDAD
        defaultFacturasShouldNotBeFound("cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllFacturasByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where cantidad is less than DEFAULT_CANTIDAD
        defaultFacturasShouldNotBeFound("cantidad.lessThan=" + DEFAULT_CANTIDAD);

        // Get all the facturasList where cantidad is less than UPDATED_CANTIDAD
        defaultFacturasShouldBeFound("cantidad.lessThan=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllFacturasByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where cantidad is greater than DEFAULT_CANTIDAD
        defaultFacturasShouldNotBeFound("cantidad.greaterThan=" + DEFAULT_CANTIDAD);

        // Get all the facturasList where cantidad is greater than SMALLER_CANTIDAD
        defaultFacturasShouldBeFound("cantidad.greaterThan=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllFacturasByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where descripcion equals to DEFAULT_DESCRIPCION
        defaultFacturasShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the facturasList where descripcion equals to UPDATED_DESCRIPCION
        defaultFacturasShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllFacturasByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultFacturasShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the facturasList where descripcion equals to UPDATED_DESCRIPCION
        defaultFacturasShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllFacturasByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where descripcion is not null
        defaultFacturasShouldBeFound("descripcion.specified=true");

        // Get all the facturasList where descripcion is null
        defaultFacturasShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where descripcion contains DEFAULT_DESCRIPCION
        defaultFacturasShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the facturasList where descripcion contains UPDATED_DESCRIPCION
        defaultFacturasShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllFacturasByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultFacturasShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the facturasList where descripcion does not contain UPDATED_DESCRIPCION
        defaultFacturasShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllFacturasByPrecioUnitarioIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where precioUnitario equals to DEFAULT_PRECIO_UNITARIO
        defaultFacturasShouldBeFound("precioUnitario.equals=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the facturasList where precioUnitario equals to UPDATED_PRECIO_UNITARIO
        defaultFacturasShouldNotBeFound("precioUnitario.equals=" + UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllFacturasByPrecioUnitarioIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where precioUnitario in DEFAULT_PRECIO_UNITARIO or UPDATED_PRECIO_UNITARIO
        defaultFacturasShouldBeFound("precioUnitario.in=" + DEFAULT_PRECIO_UNITARIO + "," + UPDATED_PRECIO_UNITARIO);

        // Get all the facturasList where precioUnitario equals to UPDATED_PRECIO_UNITARIO
        defaultFacturasShouldNotBeFound("precioUnitario.in=" + UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllFacturasByPrecioUnitarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where precioUnitario is not null
        defaultFacturasShouldBeFound("precioUnitario.specified=true");

        // Get all the facturasList where precioUnitario is null
        defaultFacturasShouldNotBeFound("precioUnitario.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByPrecioUnitarioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where precioUnitario is greater than or equal to DEFAULT_PRECIO_UNITARIO
        defaultFacturasShouldBeFound("precioUnitario.greaterThanOrEqual=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the facturasList where precioUnitario is greater than or equal to UPDATED_PRECIO_UNITARIO
        defaultFacturasShouldNotBeFound("precioUnitario.greaterThanOrEqual=" + UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllFacturasByPrecioUnitarioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where precioUnitario is less than or equal to DEFAULT_PRECIO_UNITARIO
        defaultFacturasShouldBeFound("precioUnitario.lessThanOrEqual=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the facturasList where precioUnitario is less than or equal to SMALLER_PRECIO_UNITARIO
        defaultFacturasShouldNotBeFound("precioUnitario.lessThanOrEqual=" + SMALLER_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllFacturasByPrecioUnitarioIsLessThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where precioUnitario is less than DEFAULT_PRECIO_UNITARIO
        defaultFacturasShouldNotBeFound("precioUnitario.lessThan=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the facturasList where precioUnitario is less than UPDATED_PRECIO_UNITARIO
        defaultFacturasShouldBeFound("precioUnitario.lessThan=" + UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllFacturasByPrecioUnitarioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where precioUnitario is greater than DEFAULT_PRECIO_UNITARIO
        defaultFacturasShouldNotBeFound("precioUnitario.greaterThan=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the facturasList where precioUnitario is greater than SMALLER_PRECIO_UNITARIO
        defaultFacturasShouldBeFound("precioUnitario.greaterThan=" + SMALLER_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllFacturasByValor5IsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor5 equals to DEFAULT_VALOR_5
        defaultFacturasShouldBeFound("valor5.equals=" + DEFAULT_VALOR_5);

        // Get all the facturasList where valor5 equals to UPDATED_VALOR_5
        defaultFacturasShouldNotBeFound("valor5.equals=" + UPDATED_VALOR_5);
    }

    @Test
    @Transactional
    void getAllFacturasByValor5IsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor5 in DEFAULT_VALOR_5 or UPDATED_VALOR_5
        defaultFacturasShouldBeFound("valor5.in=" + DEFAULT_VALOR_5 + "," + UPDATED_VALOR_5);

        // Get all the facturasList where valor5 equals to UPDATED_VALOR_5
        defaultFacturasShouldNotBeFound("valor5.in=" + UPDATED_VALOR_5);
    }

    @Test
    @Transactional
    void getAllFacturasByValor5IsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor5 is not null
        defaultFacturasShouldBeFound("valor5.specified=true");

        // Get all the facturasList where valor5 is null
        defaultFacturasShouldNotBeFound("valor5.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByValor5IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor5 is greater than or equal to DEFAULT_VALOR_5
        defaultFacturasShouldBeFound("valor5.greaterThanOrEqual=" + DEFAULT_VALOR_5);

        // Get all the facturasList where valor5 is greater than or equal to UPDATED_VALOR_5
        defaultFacturasShouldNotBeFound("valor5.greaterThanOrEqual=" + UPDATED_VALOR_5);
    }

    @Test
    @Transactional
    void getAllFacturasByValor5IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor5 is less than or equal to DEFAULT_VALOR_5
        defaultFacturasShouldBeFound("valor5.lessThanOrEqual=" + DEFAULT_VALOR_5);

        // Get all the facturasList where valor5 is less than or equal to SMALLER_VALOR_5
        defaultFacturasShouldNotBeFound("valor5.lessThanOrEqual=" + SMALLER_VALOR_5);
    }

    @Test
    @Transactional
    void getAllFacturasByValor5IsLessThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor5 is less than DEFAULT_VALOR_5
        defaultFacturasShouldNotBeFound("valor5.lessThan=" + DEFAULT_VALOR_5);

        // Get all the facturasList where valor5 is less than UPDATED_VALOR_5
        defaultFacturasShouldBeFound("valor5.lessThan=" + UPDATED_VALOR_5);
    }

    @Test
    @Transactional
    void getAllFacturasByValor5IsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor5 is greater than DEFAULT_VALOR_5
        defaultFacturasShouldNotBeFound("valor5.greaterThan=" + DEFAULT_VALOR_5);

        // Get all the facturasList where valor5 is greater than SMALLER_VALOR_5
        defaultFacturasShouldBeFound("valor5.greaterThan=" + SMALLER_VALOR_5);
    }

    @Test
    @Transactional
    void getAllFacturasByValor10IsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor10 equals to DEFAULT_VALOR_10
        defaultFacturasShouldBeFound("valor10.equals=" + DEFAULT_VALOR_10);

        // Get all the facturasList where valor10 equals to UPDATED_VALOR_10
        defaultFacturasShouldNotBeFound("valor10.equals=" + UPDATED_VALOR_10);
    }

    @Test
    @Transactional
    void getAllFacturasByValor10IsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor10 in DEFAULT_VALOR_10 or UPDATED_VALOR_10
        defaultFacturasShouldBeFound("valor10.in=" + DEFAULT_VALOR_10 + "," + UPDATED_VALOR_10);

        // Get all the facturasList where valor10 equals to UPDATED_VALOR_10
        defaultFacturasShouldNotBeFound("valor10.in=" + UPDATED_VALOR_10);
    }

    @Test
    @Transactional
    void getAllFacturasByValor10IsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor10 is not null
        defaultFacturasShouldBeFound("valor10.specified=true");

        // Get all the facturasList where valor10 is null
        defaultFacturasShouldNotBeFound("valor10.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByValor10IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor10 is greater than or equal to DEFAULT_VALOR_10
        defaultFacturasShouldBeFound("valor10.greaterThanOrEqual=" + DEFAULT_VALOR_10);

        // Get all the facturasList where valor10 is greater than or equal to UPDATED_VALOR_10
        defaultFacturasShouldNotBeFound("valor10.greaterThanOrEqual=" + UPDATED_VALOR_10);
    }

    @Test
    @Transactional
    void getAllFacturasByValor10IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor10 is less than or equal to DEFAULT_VALOR_10
        defaultFacturasShouldBeFound("valor10.lessThanOrEqual=" + DEFAULT_VALOR_10);

        // Get all the facturasList where valor10 is less than or equal to SMALLER_VALOR_10
        defaultFacturasShouldNotBeFound("valor10.lessThanOrEqual=" + SMALLER_VALOR_10);
    }

    @Test
    @Transactional
    void getAllFacturasByValor10IsLessThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor10 is less than DEFAULT_VALOR_10
        defaultFacturasShouldNotBeFound("valor10.lessThan=" + DEFAULT_VALOR_10);

        // Get all the facturasList where valor10 is less than UPDATED_VALOR_10
        defaultFacturasShouldBeFound("valor10.lessThan=" + UPDATED_VALOR_10);
    }

    @Test
    @Transactional
    void getAllFacturasByValor10IsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where valor10 is greater than DEFAULT_VALOR_10
        defaultFacturasShouldNotBeFound("valor10.greaterThan=" + DEFAULT_VALOR_10);

        // Get all the facturasList where valor10 is greater than SMALLER_VALOR_10
        defaultFacturasShouldBeFound("valor10.greaterThan=" + SMALLER_VALOR_10);
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
    void getAllFacturasByTotal5IsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total5 equals to DEFAULT_TOTAL_5
        defaultFacturasShouldBeFound("total5.equals=" + DEFAULT_TOTAL_5);

        // Get all the facturasList where total5 equals to UPDATED_TOTAL_5
        defaultFacturasShouldNotBeFound("total5.equals=" + UPDATED_TOTAL_5);
    }

    @Test
    @Transactional
    void getAllFacturasByTotal5IsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total5 in DEFAULT_TOTAL_5 or UPDATED_TOTAL_5
        defaultFacturasShouldBeFound("total5.in=" + DEFAULT_TOTAL_5 + "," + UPDATED_TOTAL_5);

        // Get all the facturasList where total5 equals to UPDATED_TOTAL_5
        defaultFacturasShouldNotBeFound("total5.in=" + UPDATED_TOTAL_5);
    }

    @Test
    @Transactional
    void getAllFacturasByTotal5IsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total5 is not null
        defaultFacturasShouldBeFound("total5.specified=true");

        // Get all the facturasList where total5 is null
        defaultFacturasShouldNotBeFound("total5.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByTotal5IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total5 is greater than or equal to DEFAULT_TOTAL_5
        defaultFacturasShouldBeFound("total5.greaterThanOrEqual=" + DEFAULT_TOTAL_5);

        // Get all the facturasList where total5 is greater than or equal to UPDATED_TOTAL_5
        defaultFacturasShouldNotBeFound("total5.greaterThanOrEqual=" + UPDATED_TOTAL_5);
    }

    @Test
    @Transactional
    void getAllFacturasByTotal5IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total5 is less than or equal to DEFAULT_TOTAL_5
        defaultFacturasShouldBeFound("total5.lessThanOrEqual=" + DEFAULT_TOTAL_5);

        // Get all the facturasList where total5 is less than or equal to SMALLER_TOTAL_5
        defaultFacturasShouldNotBeFound("total5.lessThanOrEqual=" + SMALLER_TOTAL_5);
    }

    @Test
    @Transactional
    void getAllFacturasByTotal5IsLessThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total5 is less than DEFAULT_TOTAL_5
        defaultFacturasShouldNotBeFound("total5.lessThan=" + DEFAULT_TOTAL_5);

        // Get all the facturasList where total5 is less than UPDATED_TOTAL_5
        defaultFacturasShouldBeFound("total5.lessThan=" + UPDATED_TOTAL_5);
    }

    @Test
    @Transactional
    void getAllFacturasByTotal5IsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total5 is greater than DEFAULT_TOTAL_5
        defaultFacturasShouldNotBeFound("total5.greaterThan=" + DEFAULT_TOTAL_5);

        // Get all the facturasList where total5 is greater than SMALLER_TOTAL_5
        defaultFacturasShouldBeFound("total5.greaterThan=" + SMALLER_TOTAL_5);
    }

    @Test
    @Transactional
    void getAllFacturasByTotal10IsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total10 equals to DEFAULT_TOTAL_10
        defaultFacturasShouldBeFound("total10.equals=" + DEFAULT_TOTAL_10);

        // Get all the facturasList where total10 equals to UPDATED_TOTAL_10
        defaultFacturasShouldNotBeFound("total10.equals=" + UPDATED_TOTAL_10);
    }

    @Test
    @Transactional
    void getAllFacturasByTotal10IsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total10 in DEFAULT_TOTAL_10 or UPDATED_TOTAL_10
        defaultFacturasShouldBeFound("total10.in=" + DEFAULT_TOTAL_10 + "," + UPDATED_TOTAL_10);

        // Get all the facturasList where total10 equals to UPDATED_TOTAL_10
        defaultFacturasShouldNotBeFound("total10.in=" + UPDATED_TOTAL_10);
    }

    @Test
    @Transactional
    void getAllFacturasByTotal10IsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total10 is not null
        defaultFacturasShouldBeFound("total10.specified=true");

        // Get all the facturasList where total10 is null
        defaultFacturasShouldNotBeFound("total10.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByTotal10IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total10 is greater than or equal to DEFAULT_TOTAL_10
        defaultFacturasShouldBeFound("total10.greaterThanOrEqual=" + DEFAULT_TOTAL_10);

        // Get all the facturasList where total10 is greater than or equal to UPDATED_TOTAL_10
        defaultFacturasShouldNotBeFound("total10.greaterThanOrEqual=" + UPDATED_TOTAL_10);
    }

    @Test
    @Transactional
    void getAllFacturasByTotal10IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total10 is less than or equal to DEFAULT_TOTAL_10
        defaultFacturasShouldBeFound("total10.lessThanOrEqual=" + DEFAULT_TOTAL_10);

        // Get all the facturasList where total10 is less than or equal to SMALLER_TOTAL_10
        defaultFacturasShouldNotBeFound("total10.lessThanOrEqual=" + SMALLER_TOTAL_10);
    }

    @Test
    @Transactional
    void getAllFacturasByTotal10IsLessThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total10 is less than DEFAULT_TOTAL_10
        defaultFacturasShouldNotBeFound("total10.lessThan=" + DEFAULT_TOTAL_10);

        // Get all the facturasList where total10 is less than UPDATED_TOTAL_10
        defaultFacturasShouldBeFound("total10.lessThan=" + UPDATED_TOTAL_10);
    }

    @Test
    @Transactional
    void getAllFacturasByTotal10IsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where total10 is greater than DEFAULT_TOTAL_10
        defaultFacturasShouldNotBeFound("total10.greaterThan=" + DEFAULT_TOTAL_10);

        // Get all the facturasList where total10 is greater than SMALLER_TOTAL_10
        defaultFacturasShouldBeFound("total10.greaterThan=" + SMALLER_TOTAL_10);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIvaIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where totalIva equals to DEFAULT_TOTAL_IVA
        defaultFacturasShouldBeFound("totalIva.equals=" + DEFAULT_TOTAL_IVA);

        // Get all the facturasList where totalIva equals to UPDATED_TOTAL_IVA
        defaultFacturasShouldNotBeFound("totalIva.equals=" + UPDATED_TOTAL_IVA);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIvaIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where totalIva in DEFAULT_TOTAL_IVA or UPDATED_TOTAL_IVA
        defaultFacturasShouldBeFound("totalIva.in=" + DEFAULT_TOTAL_IVA + "," + UPDATED_TOTAL_IVA);

        // Get all the facturasList where totalIva equals to UPDATED_TOTAL_IVA
        defaultFacturasShouldNotBeFound("totalIva.in=" + UPDATED_TOTAL_IVA);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where totalIva is not null
        defaultFacturasShouldBeFound("totalIva.specified=true");

        // Get all the facturasList where totalIva is null
        defaultFacturasShouldNotBeFound("totalIva.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where totalIva is greater than or equal to DEFAULT_TOTAL_IVA
        defaultFacturasShouldBeFound("totalIva.greaterThanOrEqual=" + DEFAULT_TOTAL_IVA);

        // Get all the facturasList where totalIva is greater than or equal to UPDATED_TOTAL_IVA
        defaultFacturasShouldNotBeFound("totalIva.greaterThanOrEqual=" + UPDATED_TOTAL_IVA);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where totalIva is less than or equal to DEFAULT_TOTAL_IVA
        defaultFacturasShouldBeFound("totalIva.lessThanOrEqual=" + DEFAULT_TOTAL_IVA);

        // Get all the facturasList where totalIva is less than or equal to SMALLER_TOTAL_IVA
        defaultFacturasShouldNotBeFound("totalIva.lessThanOrEqual=" + SMALLER_TOTAL_IVA);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIvaIsLessThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where totalIva is less than DEFAULT_TOTAL_IVA
        defaultFacturasShouldNotBeFound("totalIva.lessThan=" + DEFAULT_TOTAL_IVA);

        // Get all the facturasList where totalIva is less than UPDATED_TOTAL_IVA
        defaultFacturasShouldBeFound("totalIva.lessThan=" + UPDATED_TOTAL_IVA);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where totalIva is greater than DEFAULT_TOTAL_IVA
        defaultFacturasShouldNotBeFound("totalIva.greaterThan=" + DEFAULT_TOTAL_IVA);

        // Get all the facturasList where totalIva is greater than SMALLER_TOTAL_IVA
        defaultFacturasShouldBeFound("totalIva.greaterThan=" + SMALLER_TOTAL_IVA);
    }

    @Test
    @Transactional
    void getAllFacturasByCobrosIsEqualToSomething() throws Exception {
        Cobros cobros;
        if (TestUtil.findAll(em, Cobros.class).isEmpty()) {
            facturasRepository.saveAndFlush(facturas);
            cobros = CobrosResourceIT.createEntity(em);
        } else {
            cobros = TestUtil.findAll(em, Cobros.class).get(0);
        }
        em.persist(cobros);
        em.flush();
        facturas.addCobros(cobros);
        facturasRepository.saveAndFlush(facturas);
        Long cobrosId = cobros.getId();

        // Get all the facturasList where cobros equals to cobrosId
        defaultFacturasShouldBeFound("cobrosId.equals=" + cobrosId);

        // Get all the facturasList where cobros equals to (cobrosId + 1)
        defaultFacturasShouldNotBeFound("cobrosId.equals=" + (cobrosId + 1));
    }

    @Test
    @Transactional
    void getAllFacturasByAlumnosIsEqualToSomething() throws Exception {
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            facturasRepository.saveAndFlush(facturas);
            alumnos = AlumnosResourceIT.createEntity(em);
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        em.persist(alumnos);
        em.flush();
        facturas.setAlumnos(alumnos);
        facturasRepository.saveAndFlush(facturas);
        Long alumnosId = alumnos.getId();

        // Get all the facturasList where alumnos equals to alumnosId
        defaultFacturasShouldBeFound("alumnosId.equals=" + alumnosId);

        // Get all the facturasList where alumnos equals to (alumnosId + 1)
        defaultFacturasShouldNotBeFound("alumnosId.equals=" + (alumnosId + 1));
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
            .andExpect(jsonPath("$.[*].ruc").value(hasItem(DEFAULT_RUC)))
            .andExpect(jsonPath("$.[*].condicionVenta").value(hasItem(DEFAULT_CONDICION_VENTA.toString())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(DEFAULT_PRECIO_UNITARIO)))
            .andExpect(jsonPath("$.[*].valor5").value(hasItem(DEFAULT_VALOR_5)))
            .andExpect(jsonPath("$.[*].valor10").value(hasItem(DEFAULT_VALOR_10)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].total5").value(hasItem(DEFAULT_TOTAL_5)))
            .andExpect(jsonPath("$.[*].total10").value(hasItem(DEFAULT_TOTAL_10)))
            .andExpect(jsonPath("$.[*].totalIva").value(hasItem(DEFAULT_TOTAL_IVA)));

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
            .ruc(UPDATED_RUC)
            .condicionVenta(UPDATED_CONDICION_VENTA)
            .cantidad(UPDATED_CANTIDAD)
            .descripcion(UPDATED_DESCRIPCION)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .valor5(UPDATED_VALOR_5)
            .valor10(UPDATED_VALOR_10)
            .total(UPDATED_TOTAL)
            .total5(UPDATED_TOTAL_5)
            .total10(UPDATED_TOTAL_10)
            .totalIva(UPDATED_TOTAL_IVA);

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
        assertThat(testFacturas.getRuc()).isEqualTo(UPDATED_RUC);
        assertThat(testFacturas.getCondicionVenta()).isEqualTo(UPDATED_CONDICION_VENTA);
        assertThat(testFacturas.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testFacturas.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testFacturas.getPrecioUnitario()).isEqualTo(UPDATED_PRECIO_UNITARIO);
        assertThat(testFacturas.getValor5()).isEqualTo(UPDATED_VALOR_5);
        assertThat(testFacturas.getValor10()).isEqualTo(UPDATED_VALOR_10);
        assertThat(testFacturas.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testFacturas.getTotal5()).isEqualTo(UPDATED_TOTAL_5);
        assertThat(testFacturas.getTotal10()).isEqualTo(UPDATED_TOTAL_10);
        assertThat(testFacturas.getTotalIva()).isEqualTo(UPDATED_TOTAL_IVA);
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
            .ruc(UPDATED_RUC)
            .condicionVenta(UPDATED_CONDICION_VENTA)
            .cantidad(UPDATED_CANTIDAD)
            .descripcion(UPDATED_DESCRIPCION)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .valor5(UPDATED_VALOR_5)
            .valor10(UPDATED_VALOR_10)
            .total(UPDATED_TOTAL)
            .totalIva(UPDATED_TOTAL_IVA);

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
        assertThat(testFacturas.getRuc()).isEqualTo(UPDATED_RUC);
        assertThat(testFacturas.getCondicionVenta()).isEqualTo(UPDATED_CONDICION_VENTA);
        assertThat(testFacturas.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testFacturas.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testFacturas.getPrecioUnitario()).isEqualTo(UPDATED_PRECIO_UNITARIO);
        assertThat(testFacturas.getValor5()).isEqualTo(UPDATED_VALOR_5);
        assertThat(testFacturas.getValor10()).isEqualTo(UPDATED_VALOR_10);
        assertThat(testFacturas.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testFacturas.getTotal5()).isEqualTo(DEFAULT_TOTAL_5);
        assertThat(testFacturas.getTotal10()).isEqualTo(DEFAULT_TOTAL_10);
        assertThat(testFacturas.getTotalIva()).isEqualTo(UPDATED_TOTAL_IVA);
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
            .ruc(UPDATED_RUC)
            .condicionVenta(UPDATED_CONDICION_VENTA)
            .cantidad(UPDATED_CANTIDAD)
            .descripcion(UPDATED_DESCRIPCION)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .valor5(UPDATED_VALOR_5)
            .valor10(UPDATED_VALOR_10)
            .total(UPDATED_TOTAL)
            .total5(UPDATED_TOTAL_5)
            .total10(UPDATED_TOTAL_10)
            .totalIva(UPDATED_TOTAL_IVA);

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
        assertThat(testFacturas.getRuc()).isEqualTo(UPDATED_RUC);
        assertThat(testFacturas.getCondicionVenta()).isEqualTo(UPDATED_CONDICION_VENTA);
        assertThat(testFacturas.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testFacturas.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testFacturas.getPrecioUnitario()).isEqualTo(UPDATED_PRECIO_UNITARIO);
        assertThat(testFacturas.getValor5()).isEqualTo(UPDATED_VALOR_5);
        assertThat(testFacturas.getValor10()).isEqualTo(UPDATED_VALOR_10);
        assertThat(testFacturas.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testFacturas.getTotal5()).isEqualTo(UPDATED_TOTAL_5);
        assertThat(testFacturas.getTotal10()).isEqualTo(UPDATED_TOTAL_10);
        assertThat(testFacturas.getTotalIva()).isEqualTo(UPDATED_TOTAL_IVA);
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
