package py.com.abf.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import py.com.abf.domain.FacturaDetalle;
import py.com.abf.domain.Facturas;
import py.com.abf.domain.Productos;
import py.com.abf.repository.FacturaDetalleRepository;
import py.com.abf.service.FacturaDetalleService;
import py.com.abf.service.criteria.FacturaDetalleCriteria;

/**
 * Integration tests for the {@link FacturaDetalleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FacturaDetalleResourceIT {

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    private static final Integer DEFAULT_PRECIO_UNITARIO = 1;
    private static final Integer UPDATED_PRECIO_UNITARIO = 2;
    private static final Integer SMALLER_PRECIO_UNITARIO = 1 - 1;

    private static final Integer DEFAULT_SUBTOTAL = 1;
    private static final Integer UPDATED_SUBTOTAL = 2;
    private static final Integer SMALLER_SUBTOTAL = 1 - 1;

    private static final Integer DEFAULT_PORCENTAJE_IVA = 1;
    private static final Integer UPDATED_PORCENTAJE_IVA = 2;
    private static final Integer SMALLER_PORCENTAJE_IVA = 1 - 1;

    private static final Integer DEFAULT_VALOR_PORCENTAJE = 1;
    private static final Integer UPDATED_VALOR_PORCENTAJE = 2;
    private static final Integer SMALLER_VALOR_PORCENTAJE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/factura-detalles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacturaDetalleRepository facturaDetalleRepository;

    @Mock
    private FacturaDetalleRepository facturaDetalleRepositoryMock;

    @Mock
    private FacturaDetalleService facturaDetalleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturaDetalleMockMvc;

    private FacturaDetalle facturaDetalle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacturaDetalle createEntity(EntityManager em) {
        FacturaDetalle facturaDetalle = new FacturaDetalle()
            .cantidad(DEFAULT_CANTIDAD)
            .precioUnitario(DEFAULT_PRECIO_UNITARIO)
            .subtotal(DEFAULT_SUBTOTAL)
            .porcentajeIva(DEFAULT_PORCENTAJE_IVA)
            .valorPorcentaje(DEFAULT_VALOR_PORCENTAJE);
        // Add required entity
        Productos productos;
        if (TestUtil.findAll(em, Productos.class).isEmpty()) {
            productos = ProductosResourceIT.createEntity(em);
            em.persist(productos);
            em.flush();
        } else {
            productos = TestUtil.findAll(em, Productos.class).get(0);
        }
        facturaDetalle.setProducto(productos);
        // Add required entity
        Facturas facturas;
        if (TestUtil.findAll(em, Facturas.class).isEmpty()) {
            facturas = FacturasResourceIT.createEntity(em);
            em.persist(facturas);
            em.flush();
        } else {
            facturas = TestUtil.findAll(em, Facturas.class).get(0);
        }
        facturaDetalle.setFactura(facturas);
        return facturaDetalle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacturaDetalle createUpdatedEntity(EntityManager em) {
        FacturaDetalle facturaDetalle = new FacturaDetalle()
            .cantidad(UPDATED_CANTIDAD)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .subtotal(UPDATED_SUBTOTAL)
            .porcentajeIva(UPDATED_PORCENTAJE_IVA)
            .valorPorcentaje(UPDATED_VALOR_PORCENTAJE);
        // Add required entity
        Productos productos;
        if (TestUtil.findAll(em, Productos.class).isEmpty()) {
            productos = ProductosResourceIT.createUpdatedEntity(em);
            em.persist(productos);
            em.flush();
        } else {
            productos = TestUtil.findAll(em, Productos.class).get(0);
        }
        facturaDetalle.setProducto(productos);
        // Add required entity
        Facturas facturas;
        if (TestUtil.findAll(em, Facturas.class).isEmpty()) {
            facturas = FacturasResourceIT.createUpdatedEntity(em);
            em.persist(facturas);
            em.flush();
        } else {
            facturas = TestUtil.findAll(em, Facturas.class).get(0);
        }
        facturaDetalle.setFactura(facturas);
        return facturaDetalle;
    }

    @BeforeEach
    public void initTest() {
        facturaDetalle = createEntity(em);
    }

    @Test
    @Transactional
    void createFacturaDetalle() throws Exception {
        int databaseSizeBeforeCreate = facturaDetalleRepository.findAll().size();
        // Create the FacturaDetalle
        restFacturaDetalleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaDetalle))
            )
            .andExpect(status().isCreated());

        // Validate the FacturaDetalle in the database
        List<FacturaDetalle> facturaDetalleList = facturaDetalleRepository.findAll();
        assertThat(facturaDetalleList).hasSize(databaseSizeBeforeCreate + 1);
        FacturaDetalle testFacturaDetalle = facturaDetalleList.get(facturaDetalleList.size() - 1);
        assertThat(testFacturaDetalle.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testFacturaDetalle.getPrecioUnitario()).isEqualTo(DEFAULT_PRECIO_UNITARIO);
        assertThat(testFacturaDetalle.getSubtotal()).isEqualTo(DEFAULT_SUBTOTAL);
        assertThat(testFacturaDetalle.getPorcentajeIva()).isEqualTo(DEFAULT_PORCENTAJE_IVA);
        assertThat(testFacturaDetalle.getValorPorcentaje()).isEqualTo(DEFAULT_VALOR_PORCENTAJE);
    }

    @Test
    @Transactional
    void createFacturaDetalleWithExistingId() throws Exception {
        // Create the FacturaDetalle with an existing ID
        facturaDetalle.setId(1L);

        int databaseSizeBeforeCreate = facturaDetalleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturaDetalleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaDetalle))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaDetalle in the database
        List<FacturaDetalle> facturaDetalleList = facturaDetalleRepository.findAll();
        assertThat(facturaDetalleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFacturaDetalles() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList
        restFacturaDetalleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facturaDetalle.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(DEFAULT_PRECIO_UNITARIO)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL)))
            .andExpect(jsonPath("$.[*].porcentajeIva").value(hasItem(DEFAULT_PORCENTAJE_IVA)))
            .andExpect(jsonPath("$.[*].valorPorcentaje").value(hasItem(DEFAULT_VALOR_PORCENTAJE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacturaDetallesWithEagerRelationshipsIsEnabled() throws Exception {
        when(facturaDetalleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacturaDetalleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facturaDetalleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacturaDetallesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(facturaDetalleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacturaDetalleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(facturaDetalleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFacturaDetalle() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get the facturaDetalle
        restFacturaDetalleMockMvc
            .perform(get(ENTITY_API_URL_ID, facturaDetalle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facturaDetalle.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.precioUnitario").value(DEFAULT_PRECIO_UNITARIO))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL))
            .andExpect(jsonPath("$.porcentajeIva").value(DEFAULT_PORCENTAJE_IVA))
            .andExpect(jsonPath("$.valorPorcentaje").value(DEFAULT_VALOR_PORCENTAJE));
    }

    @Test
    @Transactional
    void getFacturaDetallesByIdFiltering() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        Long id = facturaDetalle.getId();

        defaultFacturaDetalleShouldBeFound("id.equals=" + id);
        defaultFacturaDetalleShouldNotBeFound("id.notEquals=" + id);

        defaultFacturaDetalleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFacturaDetalleShouldNotBeFound("id.greaterThan=" + id);

        defaultFacturaDetalleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFacturaDetalleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where cantidad equals to DEFAULT_CANTIDAD
        defaultFacturaDetalleShouldBeFound("cantidad.equals=" + DEFAULT_CANTIDAD);

        // Get all the facturaDetalleList where cantidad equals to UPDATED_CANTIDAD
        defaultFacturaDetalleShouldNotBeFound("cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where cantidad in DEFAULT_CANTIDAD or UPDATED_CANTIDAD
        defaultFacturaDetalleShouldBeFound("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD);

        // Get all the facturaDetalleList where cantidad equals to UPDATED_CANTIDAD
        defaultFacturaDetalleShouldNotBeFound("cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where cantidad is not null
        defaultFacturaDetalleShouldBeFound("cantidad.specified=true");

        // Get all the facturaDetalleList where cantidad is null
        defaultFacturaDetalleShouldNotBeFound("cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where cantidad is greater than or equal to DEFAULT_CANTIDAD
        defaultFacturaDetalleShouldBeFound("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the facturaDetalleList where cantidad is greater than or equal to UPDATED_CANTIDAD
        defaultFacturaDetalleShouldNotBeFound("cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where cantidad is less than or equal to DEFAULT_CANTIDAD
        defaultFacturaDetalleShouldBeFound("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the facturaDetalleList where cantidad is less than or equal to SMALLER_CANTIDAD
        defaultFacturaDetalleShouldNotBeFound("cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where cantidad is less than DEFAULT_CANTIDAD
        defaultFacturaDetalleShouldNotBeFound("cantidad.lessThan=" + DEFAULT_CANTIDAD);

        // Get all the facturaDetalleList where cantidad is less than UPDATED_CANTIDAD
        defaultFacturaDetalleShouldBeFound("cantidad.lessThan=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where cantidad is greater than DEFAULT_CANTIDAD
        defaultFacturaDetalleShouldNotBeFound("cantidad.greaterThan=" + DEFAULT_CANTIDAD);

        // Get all the facturaDetalleList where cantidad is greater than SMALLER_CANTIDAD
        defaultFacturaDetalleShouldBeFound("cantidad.greaterThan=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPrecioUnitarioIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where precioUnitario equals to DEFAULT_PRECIO_UNITARIO
        defaultFacturaDetalleShouldBeFound("precioUnitario.equals=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the facturaDetalleList where precioUnitario equals to UPDATED_PRECIO_UNITARIO
        defaultFacturaDetalleShouldNotBeFound("precioUnitario.equals=" + UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPrecioUnitarioIsInShouldWork() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where precioUnitario in DEFAULT_PRECIO_UNITARIO or UPDATED_PRECIO_UNITARIO
        defaultFacturaDetalleShouldBeFound("precioUnitario.in=" + DEFAULT_PRECIO_UNITARIO + "," + UPDATED_PRECIO_UNITARIO);

        // Get all the facturaDetalleList where precioUnitario equals to UPDATED_PRECIO_UNITARIO
        defaultFacturaDetalleShouldNotBeFound("precioUnitario.in=" + UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPrecioUnitarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where precioUnitario is not null
        defaultFacturaDetalleShouldBeFound("precioUnitario.specified=true");

        // Get all the facturaDetalleList where precioUnitario is null
        defaultFacturaDetalleShouldNotBeFound("precioUnitario.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPrecioUnitarioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where precioUnitario is greater than or equal to DEFAULT_PRECIO_UNITARIO
        defaultFacturaDetalleShouldBeFound("precioUnitario.greaterThanOrEqual=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the facturaDetalleList where precioUnitario is greater than or equal to UPDATED_PRECIO_UNITARIO
        defaultFacturaDetalleShouldNotBeFound("precioUnitario.greaterThanOrEqual=" + UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPrecioUnitarioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where precioUnitario is less than or equal to DEFAULT_PRECIO_UNITARIO
        defaultFacturaDetalleShouldBeFound("precioUnitario.lessThanOrEqual=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the facturaDetalleList where precioUnitario is less than or equal to SMALLER_PRECIO_UNITARIO
        defaultFacturaDetalleShouldNotBeFound("precioUnitario.lessThanOrEqual=" + SMALLER_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPrecioUnitarioIsLessThanSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where precioUnitario is less than DEFAULT_PRECIO_UNITARIO
        defaultFacturaDetalleShouldNotBeFound("precioUnitario.lessThan=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the facturaDetalleList where precioUnitario is less than UPDATED_PRECIO_UNITARIO
        defaultFacturaDetalleShouldBeFound("precioUnitario.lessThan=" + UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPrecioUnitarioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where precioUnitario is greater than DEFAULT_PRECIO_UNITARIO
        defaultFacturaDetalleShouldNotBeFound("precioUnitario.greaterThan=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the facturaDetalleList where precioUnitario is greater than SMALLER_PRECIO_UNITARIO
        defaultFacturaDetalleShouldBeFound("precioUnitario.greaterThan=" + SMALLER_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesBySubtotalIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where subtotal equals to DEFAULT_SUBTOTAL
        defaultFacturaDetalleShouldBeFound("subtotal.equals=" + DEFAULT_SUBTOTAL);

        // Get all the facturaDetalleList where subtotal equals to UPDATED_SUBTOTAL
        defaultFacturaDetalleShouldNotBeFound("subtotal.equals=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesBySubtotalIsInShouldWork() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where subtotal in DEFAULT_SUBTOTAL or UPDATED_SUBTOTAL
        defaultFacturaDetalleShouldBeFound("subtotal.in=" + DEFAULT_SUBTOTAL + "," + UPDATED_SUBTOTAL);

        // Get all the facturaDetalleList where subtotal equals to UPDATED_SUBTOTAL
        defaultFacturaDetalleShouldNotBeFound("subtotal.in=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesBySubtotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where subtotal is not null
        defaultFacturaDetalleShouldBeFound("subtotal.specified=true");

        // Get all the facturaDetalleList where subtotal is null
        defaultFacturaDetalleShouldNotBeFound("subtotal.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturaDetallesBySubtotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where subtotal is greater than or equal to DEFAULT_SUBTOTAL
        defaultFacturaDetalleShouldBeFound("subtotal.greaterThanOrEqual=" + DEFAULT_SUBTOTAL);

        // Get all the facturaDetalleList where subtotal is greater than or equal to UPDATED_SUBTOTAL
        defaultFacturaDetalleShouldNotBeFound("subtotal.greaterThanOrEqual=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesBySubtotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where subtotal is less than or equal to DEFAULT_SUBTOTAL
        defaultFacturaDetalleShouldBeFound("subtotal.lessThanOrEqual=" + DEFAULT_SUBTOTAL);

        // Get all the facturaDetalleList where subtotal is less than or equal to SMALLER_SUBTOTAL
        defaultFacturaDetalleShouldNotBeFound("subtotal.lessThanOrEqual=" + SMALLER_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesBySubtotalIsLessThanSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where subtotal is less than DEFAULT_SUBTOTAL
        defaultFacturaDetalleShouldNotBeFound("subtotal.lessThan=" + DEFAULT_SUBTOTAL);

        // Get all the facturaDetalleList where subtotal is less than UPDATED_SUBTOTAL
        defaultFacturaDetalleShouldBeFound("subtotal.lessThan=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesBySubtotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where subtotal is greater than DEFAULT_SUBTOTAL
        defaultFacturaDetalleShouldNotBeFound("subtotal.greaterThan=" + DEFAULT_SUBTOTAL);

        // Get all the facturaDetalleList where subtotal is greater than SMALLER_SUBTOTAL
        defaultFacturaDetalleShouldBeFound("subtotal.greaterThan=" + SMALLER_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPorcentajeIvaIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where porcentajeIva equals to DEFAULT_PORCENTAJE_IVA
        defaultFacturaDetalleShouldBeFound("porcentajeIva.equals=" + DEFAULT_PORCENTAJE_IVA);

        // Get all the facturaDetalleList where porcentajeIva equals to UPDATED_PORCENTAJE_IVA
        defaultFacturaDetalleShouldNotBeFound("porcentajeIva.equals=" + UPDATED_PORCENTAJE_IVA);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPorcentajeIvaIsInShouldWork() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where porcentajeIva in DEFAULT_PORCENTAJE_IVA or UPDATED_PORCENTAJE_IVA
        defaultFacturaDetalleShouldBeFound("porcentajeIva.in=" + DEFAULT_PORCENTAJE_IVA + "," + UPDATED_PORCENTAJE_IVA);

        // Get all the facturaDetalleList where porcentajeIva equals to UPDATED_PORCENTAJE_IVA
        defaultFacturaDetalleShouldNotBeFound("porcentajeIva.in=" + UPDATED_PORCENTAJE_IVA);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPorcentajeIvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where porcentajeIva is not null
        defaultFacturaDetalleShouldBeFound("porcentajeIva.specified=true");

        // Get all the facturaDetalleList where porcentajeIva is null
        defaultFacturaDetalleShouldNotBeFound("porcentajeIva.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPorcentajeIvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where porcentajeIva is greater than or equal to DEFAULT_PORCENTAJE_IVA
        defaultFacturaDetalleShouldBeFound("porcentajeIva.greaterThanOrEqual=" + DEFAULT_PORCENTAJE_IVA);

        // Get all the facturaDetalleList where porcentajeIva is greater than or equal to UPDATED_PORCENTAJE_IVA
        defaultFacturaDetalleShouldNotBeFound("porcentajeIva.greaterThanOrEqual=" + UPDATED_PORCENTAJE_IVA);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPorcentajeIvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where porcentajeIva is less than or equal to DEFAULT_PORCENTAJE_IVA
        defaultFacturaDetalleShouldBeFound("porcentajeIva.lessThanOrEqual=" + DEFAULT_PORCENTAJE_IVA);

        // Get all the facturaDetalleList where porcentajeIva is less than or equal to SMALLER_PORCENTAJE_IVA
        defaultFacturaDetalleShouldNotBeFound("porcentajeIva.lessThanOrEqual=" + SMALLER_PORCENTAJE_IVA);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPorcentajeIvaIsLessThanSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where porcentajeIva is less than DEFAULT_PORCENTAJE_IVA
        defaultFacturaDetalleShouldNotBeFound("porcentajeIva.lessThan=" + DEFAULT_PORCENTAJE_IVA);

        // Get all the facturaDetalleList where porcentajeIva is less than UPDATED_PORCENTAJE_IVA
        defaultFacturaDetalleShouldBeFound("porcentajeIva.lessThan=" + UPDATED_PORCENTAJE_IVA);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByPorcentajeIvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where porcentajeIva is greater than DEFAULT_PORCENTAJE_IVA
        defaultFacturaDetalleShouldNotBeFound("porcentajeIva.greaterThan=" + DEFAULT_PORCENTAJE_IVA);

        // Get all the facturaDetalleList where porcentajeIva is greater than SMALLER_PORCENTAJE_IVA
        defaultFacturaDetalleShouldBeFound("porcentajeIva.greaterThan=" + SMALLER_PORCENTAJE_IVA);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByValorPorcentajeIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where valorPorcentaje equals to DEFAULT_VALOR_PORCENTAJE
        defaultFacturaDetalleShouldBeFound("valorPorcentaje.equals=" + DEFAULT_VALOR_PORCENTAJE);

        // Get all the facturaDetalleList where valorPorcentaje equals to UPDATED_VALOR_PORCENTAJE
        defaultFacturaDetalleShouldNotBeFound("valorPorcentaje.equals=" + UPDATED_VALOR_PORCENTAJE);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByValorPorcentajeIsInShouldWork() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where valorPorcentaje in DEFAULT_VALOR_PORCENTAJE or UPDATED_VALOR_PORCENTAJE
        defaultFacturaDetalleShouldBeFound("valorPorcentaje.in=" + DEFAULT_VALOR_PORCENTAJE + "," + UPDATED_VALOR_PORCENTAJE);

        // Get all the facturaDetalleList where valorPorcentaje equals to UPDATED_VALOR_PORCENTAJE
        defaultFacturaDetalleShouldNotBeFound("valorPorcentaje.in=" + UPDATED_VALOR_PORCENTAJE);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByValorPorcentajeIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where valorPorcentaje is not null
        defaultFacturaDetalleShouldBeFound("valorPorcentaje.specified=true");

        // Get all the facturaDetalleList where valorPorcentaje is null
        defaultFacturaDetalleShouldNotBeFound("valorPorcentaje.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByValorPorcentajeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where valorPorcentaje is greater than or equal to DEFAULT_VALOR_PORCENTAJE
        defaultFacturaDetalleShouldBeFound("valorPorcentaje.greaterThanOrEqual=" + DEFAULT_VALOR_PORCENTAJE);

        // Get all the facturaDetalleList where valorPorcentaje is greater than or equal to UPDATED_VALOR_PORCENTAJE
        defaultFacturaDetalleShouldNotBeFound("valorPorcentaje.greaterThanOrEqual=" + UPDATED_VALOR_PORCENTAJE);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByValorPorcentajeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where valorPorcentaje is less than or equal to DEFAULT_VALOR_PORCENTAJE
        defaultFacturaDetalleShouldBeFound("valorPorcentaje.lessThanOrEqual=" + DEFAULT_VALOR_PORCENTAJE);

        // Get all the facturaDetalleList where valorPorcentaje is less than or equal to SMALLER_VALOR_PORCENTAJE
        defaultFacturaDetalleShouldNotBeFound("valorPorcentaje.lessThanOrEqual=" + SMALLER_VALOR_PORCENTAJE);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByValorPorcentajeIsLessThanSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where valorPorcentaje is less than DEFAULT_VALOR_PORCENTAJE
        defaultFacturaDetalleShouldNotBeFound("valorPorcentaje.lessThan=" + DEFAULT_VALOR_PORCENTAJE);

        // Get all the facturaDetalleList where valorPorcentaje is less than UPDATED_VALOR_PORCENTAJE
        defaultFacturaDetalleShouldBeFound("valorPorcentaje.lessThan=" + UPDATED_VALOR_PORCENTAJE);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByValorPorcentajeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        // Get all the facturaDetalleList where valorPorcentaje is greater than DEFAULT_VALOR_PORCENTAJE
        defaultFacturaDetalleShouldNotBeFound("valorPorcentaje.greaterThan=" + DEFAULT_VALOR_PORCENTAJE);

        // Get all the facturaDetalleList where valorPorcentaje is greater than SMALLER_VALOR_PORCENTAJE
        defaultFacturaDetalleShouldBeFound("valorPorcentaje.greaterThan=" + SMALLER_VALOR_PORCENTAJE);
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByProductoIsEqualToSomething() throws Exception {
        Productos producto;
        if (TestUtil.findAll(em, Productos.class).isEmpty()) {
            facturaDetalleRepository.saveAndFlush(facturaDetalle);
            producto = ProductosResourceIT.createEntity(em);
        } else {
            producto = TestUtil.findAll(em, Productos.class).get(0);
        }
        em.persist(producto);
        em.flush();
        facturaDetalle.setProducto(producto);
        facturaDetalleRepository.saveAndFlush(facturaDetalle);
        Long productoId = producto.getId();

        // Get all the facturaDetalleList where producto equals to productoId
        defaultFacturaDetalleShouldBeFound("productoId.equals=" + productoId);

        // Get all the facturaDetalleList where producto equals to (productoId + 1)
        defaultFacturaDetalleShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    @Test
    @Transactional
    void getAllFacturaDetallesByFacturaIsEqualToSomething() throws Exception {
        Facturas factura;
        if (TestUtil.findAll(em, Facturas.class).isEmpty()) {
            facturaDetalleRepository.saveAndFlush(facturaDetalle);
            factura = FacturasResourceIT.createEntity(em);
        } else {
            factura = TestUtil.findAll(em, Facturas.class).get(0);
        }
        em.persist(factura);
        em.flush();
        facturaDetalle.setFactura(factura);
        facturaDetalleRepository.saveAndFlush(facturaDetalle);
        Long facturaId = factura.getId();

        // Get all the facturaDetalleList where factura equals to facturaId
        defaultFacturaDetalleShouldBeFound("facturaId.equals=" + facturaId);

        // Get all the facturaDetalleList where factura equals to (facturaId + 1)
        defaultFacturaDetalleShouldNotBeFound("facturaId.equals=" + (facturaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacturaDetalleShouldBeFound(String filter) throws Exception {
        restFacturaDetalleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facturaDetalle.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(DEFAULT_PRECIO_UNITARIO)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL)))
            .andExpect(jsonPath("$.[*].porcentajeIva").value(hasItem(DEFAULT_PORCENTAJE_IVA)))
            .andExpect(jsonPath("$.[*].valorPorcentaje").value(hasItem(DEFAULT_VALOR_PORCENTAJE)));

        // Check, that the count call also returns 1
        restFacturaDetalleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacturaDetalleShouldNotBeFound(String filter) throws Exception {
        restFacturaDetalleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacturaDetalleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacturaDetalle() throws Exception {
        // Get the facturaDetalle
        restFacturaDetalleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacturaDetalle() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        int databaseSizeBeforeUpdate = facturaDetalleRepository.findAll().size();

        // Update the facturaDetalle
        FacturaDetalle updatedFacturaDetalle = facturaDetalleRepository.findById(facturaDetalle.getId()).get();
        // Disconnect from session so that the updates on updatedFacturaDetalle are not directly saved in db
        em.detach(updatedFacturaDetalle);
        updatedFacturaDetalle
            .cantidad(UPDATED_CANTIDAD)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .subtotal(UPDATED_SUBTOTAL)
            .porcentajeIva(UPDATED_PORCENTAJE_IVA)
            .valorPorcentaje(UPDATED_VALOR_PORCENTAJE);

        restFacturaDetalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacturaDetalle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFacturaDetalle))
            )
            .andExpect(status().isOk());

        // Validate the FacturaDetalle in the database
        List<FacturaDetalle> facturaDetalleList = facturaDetalleRepository.findAll();
        assertThat(facturaDetalleList).hasSize(databaseSizeBeforeUpdate);
        FacturaDetalle testFacturaDetalle = facturaDetalleList.get(facturaDetalleList.size() - 1);
        assertThat(testFacturaDetalle.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testFacturaDetalle.getPrecioUnitario()).isEqualTo(UPDATED_PRECIO_UNITARIO);
        assertThat(testFacturaDetalle.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testFacturaDetalle.getPorcentajeIva()).isEqualTo(UPDATED_PORCENTAJE_IVA);
        assertThat(testFacturaDetalle.getValorPorcentaje()).isEqualTo(UPDATED_VALOR_PORCENTAJE);
    }

    @Test
    @Transactional
    void putNonExistingFacturaDetalle() throws Exception {
        int databaseSizeBeforeUpdate = facturaDetalleRepository.findAll().size();
        facturaDetalle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaDetalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturaDetalle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaDetalle))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaDetalle in the database
        List<FacturaDetalle> facturaDetalleList = facturaDetalleRepository.findAll();
        assertThat(facturaDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacturaDetalle() throws Exception {
        int databaseSizeBeforeUpdate = facturaDetalleRepository.findAll().size();
        facturaDetalle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaDetalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaDetalle))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaDetalle in the database
        List<FacturaDetalle> facturaDetalleList = facturaDetalleRepository.findAll();
        assertThat(facturaDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacturaDetalle() throws Exception {
        int databaseSizeBeforeUpdate = facturaDetalleRepository.findAll().size();
        facturaDetalle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaDetalleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaDetalle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacturaDetalle in the database
        List<FacturaDetalle> facturaDetalleList = facturaDetalleRepository.findAll();
        assertThat(facturaDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacturaDetalleWithPatch() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        int databaseSizeBeforeUpdate = facturaDetalleRepository.findAll().size();

        // Update the facturaDetalle using partial update
        FacturaDetalle partialUpdatedFacturaDetalle = new FacturaDetalle();
        partialUpdatedFacturaDetalle.setId(facturaDetalle.getId());

        partialUpdatedFacturaDetalle.cantidad(UPDATED_CANTIDAD);

        restFacturaDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacturaDetalle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacturaDetalle))
            )
            .andExpect(status().isOk());

        // Validate the FacturaDetalle in the database
        List<FacturaDetalle> facturaDetalleList = facturaDetalleRepository.findAll();
        assertThat(facturaDetalleList).hasSize(databaseSizeBeforeUpdate);
        FacturaDetalle testFacturaDetalle = facturaDetalleList.get(facturaDetalleList.size() - 1);
        assertThat(testFacturaDetalle.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testFacturaDetalle.getPrecioUnitario()).isEqualTo(DEFAULT_PRECIO_UNITARIO);
        assertThat(testFacturaDetalle.getSubtotal()).isEqualTo(DEFAULT_SUBTOTAL);
        assertThat(testFacturaDetalle.getPorcentajeIva()).isEqualTo(DEFAULT_PORCENTAJE_IVA);
        assertThat(testFacturaDetalle.getValorPorcentaje()).isEqualTo(DEFAULT_VALOR_PORCENTAJE);
    }

    @Test
    @Transactional
    void fullUpdateFacturaDetalleWithPatch() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        int databaseSizeBeforeUpdate = facturaDetalleRepository.findAll().size();

        // Update the facturaDetalle using partial update
        FacturaDetalle partialUpdatedFacturaDetalle = new FacturaDetalle();
        partialUpdatedFacturaDetalle.setId(facturaDetalle.getId());

        partialUpdatedFacturaDetalle
            .cantidad(UPDATED_CANTIDAD)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .subtotal(UPDATED_SUBTOTAL)
            .porcentajeIva(UPDATED_PORCENTAJE_IVA)
            .valorPorcentaje(UPDATED_VALOR_PORCENTAJE);

        restFacturaDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacturaDetalle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacturaDetalle))
            )
            .andExpect(status().isOk());

        // Validate the FacturaDetalle in the database
        List<FacturaDetalle> facturaDetalleList = facturaDetalleRepository.findAll();
        assertThat(facturaDetalleList).hasSize(databaseSizeBeforeUpdate);
        FacturaDetalle testFacturaDetalle = facturaDetalleList.get(facturaDetalleList.size() - 1);
        assertThat(testFacturaDetalle.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testFacturaDetalle.getPrecioUnitario()).isEqualTo(UPDATED_PRECIO_UNITARIO);
        assertThat(testFacturaDetalle.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testFacturaDetalle.getPorcentajeIva()).isEqualTo(UPDATED_PORCENTAJE_IVA);
        assertThat(testFacturaDetalle.getValorPorcentaje()).isEqualTo(UPDATED_VALOR_PORCENTAJE);
    }

    @Test
    @Transactional
    void patchNonExistingFacturaDetalle() throws Exception {
        int databaseSizeBeforeUpdate = facturaDetalleRepository.findAll().size();
        facturaDetalle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facturaDetalle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturaDetalle))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaDetalle in the database
        List<FacturaDetalle> facturaDetalleList = facturaDetalleRepository.findAll();
        assertThat(facturaDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacturaDetalle() throws Exception {
        int databaseSizeBeforeUpdate = facturaDetalleRepository.findAll().size();
        facturaDetalle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturaDetalle))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaDetalle in the database
        List<FacturaDetalle> facturaDetalleList = facturaDetalleRepository.findAll();
        assertThat(facturaDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacturaDetalle() throws Exception {
        int databaseSizeBeforeUpdate = facturaDetalleRepository.findAll().size();
        facturaDetalle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(facturaDetalle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacturaDetalle in the database
        List<FacturaDetalle> facturaDetalleList = facturaDetalleRepository.findAll();
        assertThat(facturaDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacturaDetalle() throws Exception {
        // Initialize the database
        facturaDetalleRepository.saveAndFlush(facturaDetalle);

        int databaseSizeBeforeDelete = facturaDetalleRepository.findAll().size();

        // Delete the facturaDetalle
        restFacturaDetalleMockMvc
            .perform(delete(ENTITY_API_URL_ID, facturaDetalle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FacturaDetalle> facturaDetalleList = facturaDetalleRepository.findAll();
        assertThat(facturaDetalleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
