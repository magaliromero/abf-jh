package py.com.abf.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import py.com.abf.domain.Pagos;
import py.com.abf.domain.Productos;
import py.com.abf.domain.enumeration.TipoProductos;
import py.com.abf.repository.ProductosRepository;
import py.com.abf.service.criteria.ProductosCriteria;

/**
 * Integration tests for the {@link ProductosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductosResourceIT {

    private static final TipoProductos DEFAULT_TIPO_PRODUCTO = TipoProductos.SERVICIO;
    private static final TipoProductos UPDATED_TIPO_PRODUCTO = TipoProductos.PRODUCTO;

    private static final Integer DEFAULT_PRECIO_UNITARIO = 1;
    private static final Integer UPDATED_PRECIO_UNITARIO = 2;
    private static final Integer SMALLER_PRECIO_UNITARIO = 1 - 1;

    private static final Integer DEFAULT_PORCENTAJE_IVA = 1;
    private static final Integer UPDATED_PORCENTAJE_IVA = 2;
    private static final Integer SMALLER_PORCENTAJE_IVA = 1 - 1;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductosMockMvc;

    private Productos productos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Productos createEntity(EntityManager em) {
        Productos productos = new Productos()
            .tipoProducto(DEFAULT_TIPO_PRODUCTO)
            .precioUnitario(DEFAULT_PRECIO_UNITARIO)
            .porcentajeIva(DEFAULT_PORCENTAJE_IVA)
            .descripcion(DEFAULT_DESCRIPCION);
        return productos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Productos createUpdatedEntity(EntityManager em) {
        Productos productos = new Productos()
            .tipoProducto(UPDATED_TIPO_PRODUCTO)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .porcentajeIva(UPDATED_PORCENTAJE_IVA)
            .descripcion(UPDATED_DESCRIPCION);
        return productos;
    }

    @BeforeEach
    public void initTest() {
        productos = createEntity(em);
    }

    @Test
    @Transactional
    void createProductos() throws Exception {
        int databaseSizeBeforeCreate = productosRepository.findAll().size();
        // Create the Productos
        restProductosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productos)))
            .andExpect(status().isCreated());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeCreate + 1);
        Productos testProductos = productosList.get(productosList.size() - 1);
        assertThat(testProductos.getTipoProducto()).isEqualTo(DEFAULT_TIPO_PRODUCTO);
        assertThat(testProductos.getPrecioUnitario()).isEqualTo(DEFAULT_PRECIO_UNITARIO);
        assertThat(testProductos.getPorcentajeIva()).isEqualTo(DEFAULT_PORCENTAJE_IVA);
        assertThat(testProductos.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createProductosWithExistingId() throws Exception {
        // Create the Productos with an existing ID
        productos.setId(1L);

        int databaseSizeBeforeCreate = productosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productos)))
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoProductoIsRequired() throws Exception {
        int databaseSizeBeforeTest = productosRepository.findAll().size();
        // set the field null
        productos.setTipoProducto(null);

        // Create the Productos, which fails.

        restProductosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productos)))
            .andExpect(status().isBadRequest());

        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioUnitarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = productosRepository.findAll().size();
        // set the field null
        productos.setPrecioUnitario(null);

        // Create the Productos, which fails.

        restProductosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productos)))
            .andExpect(status().isBadRequest());

        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPorcentajeIvaIsRequired() throws Exception {
        int databaseSizeBeforeTest = productosRepository.findAll().size();
        // set the field null
        productos.setPorcentajeIva(null);

        // Create the Productos, which fails.

        restProductosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productos)))
            .andExpect(status().isBadRequest());

        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = productosRepository.findAll().size();
        // set the field null
        productos.setDescripcion(null);

        // Create the Productos, which fails.

        restProductosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productos)))
            .andExpect(status().isBadRequest());

        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductos() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList
        restProductosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productos.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoProducto").value(hasItem(DEFAULT_TIPO_PRODUCTO.toString())))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(DEFAULT_PRECIO_UNITARIO)))
            .andExpect(jsonPath("$.[*].porcentajeIva").value(hasItem(DEFAULT_PORCENTAJE_IVA)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getProductos() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get the productos
        restProductosMockMvc
            .perform(get(ENTITY_API_URL_ID, productos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productos.getId().intValue()))
            .andExpect(jsonPath("$.tipoProducto").value(DEFAULT_TIPO_PRODUCTO.toString()))
            .andExpect(jsonPath("$.precioUnitario").value(DEFAULT_PRECIO_UNITARIO))
            .andExpect(jsonPath("$.porcentajeIva").value(DEFAULT_PORCENTAJE_IVA))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getProductosByIdFiltering() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        Long id = productos.getId();

        defaultProductosShouldBeFound("id.equals=" + id);
        defaultProductosShouldNotBeFound("id.notEquals=" + id);

        defaultProductosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductosShouldNotBeFound("id.greaterThan=" + id);

        defaultProductosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductosByTipoProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where tipoProducto equals to DEFAULT_TIPO_PRODUCTO
        defaultProductosShouldBeFound("tipoProducto.equals=" + DEFAULT_TIPO_PRODUCTO);

        // Get all the productosList where tipoProducto equals to UPDATED_TIPO_PRODUCTO
        defaultProductosShouldNotBeFound("tipoProducto.equals=" + UPDATED_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllProductosByTipoProductoIsInShouldWork() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where tipoProducto in DEFAULT_TIPO_PRODUCTO or UPDATED_TIPO_PRODUCTO
        defaultProductosShouldBeFound("tipoProducto.in=" + DEFAULT_TIPO_PRODUCTO + "," + UPDATED_TIPO_PRODUCTO);

        // Get all the productosList where tipoProducto equals to UPDATED_TIPO_PRODUCTO
        defaultProductosShouldNotBeFound("tipoProducto.in=" + UPDATED_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllProductosByTipoProductoIsNullOrNotNull() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where tipoProducto is not null
        defaultProductosShouldBeFound("tipoProducto.specified=true");

        // Get all the productosList where tipoProducto is null
        defaultProductosShouldNotBeFound("tipoProducto.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUnitarioIsEqualToSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where precioUnitario equals to DEFAULT_PRECIO_UNITARIO
        defaultProductosShouldBeFound("precioUnitario.equals=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the productosList where precioUnitario equals to UPDATED_PRECIO_UNITARIO
        defaultProductosShouldNotBeFound("precioUnitario.equals=" + UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUnitarioIsInShouldWork() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where precioUnitario in DEFAULT_PRECIO_UNITARIO or UPDATED_PRECIO_UNITARIO
        defaultProductosShouldBeFound("precioUnitario.in=" + DEFAULT_PRECIO_UNITARIO + "," + UPDATED_PRECIO_UNITARIO);

        // Get all the productosList where precioUnitario equals to UPDATED_PRECIO_UNITARIO
        defaultProductosShouldNotBeFound("precioUnitario.in=" + UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUnitarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where precioUnitario is not null
        defaultProductosShouldBeFound("precioUnitario.specified=true");

        // Get all the productosList where precioUnitario is null
        defaultProductosShouldNotBeFound("precioUnitario.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUnitarioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where precioUnitario is greater than or equal to DEFAULT_PRECIO_UNITARIO
        defaultProductosShouldBeFound("precioUnitario.greaterThanOrEqual=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the productosList where precioUnitario is greater than or equal to UPDATED_PRECIO_UNITARIO
        defaultProductosShouldNotBeFound("precioUnitario.greaterThanOrEqual=" + UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUnitarioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where precioUnitario is less than or equal to DEFAULT_PRECIO_UNITARIO
        defaultProductosShouldBeFound("precioUnitario.lessThanOrEqual=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the productosList where precioUnitario is less than or equal to SMALLER_PRECIO_UNITARIO
        defaultProductosShouldNotBeFound("precioUnitario.lessThanOrEqual=" + SMALLER_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUnitarioIsLessThanSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where precioUnitario is less than DEFAULT_PRECIO_UNITARIO
        defaultProductosShouldNotBeFound("precioUnitario.lessThan=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the productosList where precioUnitario is less than UPDATED_PRECIO_UNITARIO
        defaultProductosShouldBeFound("precioUnitario.lessThan=" + UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUnitarioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where precioUnitario is greater than DEFAULT_PRECIO_UNITARIO
        defaultProductosShouldNotBeFound("precioUnitario.greaterThan=" + DEFAULT_PRECIO_UNITARIO);

        // Get all the productosList where precioUnitario is greater than SMALLER_PRECIO_UNITARIO
        defaultProductosShouldBeFound("precioUnitario.greaterThan=" + SMALLER_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void getAllProductosByPorcentajeIvaIsEqualToSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where porcentajeIva equals to DEFAULT_PORCENTAJE_IVA
        defaultProductosShouldBeFound("porcentajeIva.equals=" + DEFAULT_PORCENTAJE_IVA);

        // Get all the productosList where porcentajeIva equals to UPDATED_PORCENTAJE_IVA
        defaultProductosShouldNotBeFound("porcentajeIva.equals=" + UPDATED_PORCENTAJE_IVA);
    }

    @Test
    @Transactional
    void getAllProductosByPorcentajeIvaIsInShouldWork() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where porcentajeIva in DEFAULT_PORCENTAJE_IVA or UPDATED_PORCENTAJE_IVA
        defaultProductosShouldBeFound("porcentajeIva.in=" + DEFAULT_PORCENTAJE_IVA + "," + UPDATED_PORCENTAJE_IVA);

        // Get all the productosList where porcentajeIva equals to UPDATED_PORCENTAJE_IVA
        defaultProductosShouldNotBeFound("porcentajeIva.in=" + UPDATED_PORCENTAJE_IVA);
    }

    @Test
    @Transactional
    void getAllProductosByPorcentajeIvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where porcentajeIva is not null
        defaultProductosShouldBeFound("porcentajeIva.specified=true");

        // Get all the productosList where porcentajeIva is null
        defaultProductosShouldNotBeFound("porcentajeIva.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByPorcentajeIvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where porcentajeIva is greater than or equal to DEFAULT_PORCENTAJE_IVA
        defaultProductosShouldBeFound("porcentajeIva.greaterThanOrEqual=" + DEFAULT_PORCENTAJE_IVA);

        // Get all the productosList where porcentajeIva is greater than or equal to UPDATED_PORCENTAJE_IVA
        defaultProductosShouldNotBeFound("porcentajeIva.greaterThanOrEqual=" + UPDATED_PORCENTAJE_IVA);
    }

    @Test
    @Transactional
    void getAllProductosByPorcentajeIvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where porcentajeIva is less than or equal to DEFAULT_PORCENTAJE_IVA
        defaultProductosShouldBeFound("porcentajeIva.lessThanOrEqual=" + DEFAULT_PORCENTAJE_IVA);

        // Get all the productosList where porcentajeIva is less than or equal to SMALLER_PORCENTAJE_IVA
        defaultProductosShouldNotBeFound("porcentajeIva.lessThanOrEqual=" + SMALLER_PORCENTAJE_IVA);
    }

    @Test
    @Transactional
    void getAllProductosByPorcentajeIvaIsLessThanSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where porcentajeIva is less than DEFAULT_PORCENTAJE_IVA
        defaultProductosShouldNotBeFound("porcentajeIva.lessThan=" + DEFAULT_PORCENTAJE_IVA);

        // Get all the productosList where porcentajeIva is less than UPDATED_PORCENTAJE_IVA
        defaultProductosShouldBeFound("porcentajeIva.lessThan=" + UPDATED_PORCENTAJE_IVA);
    }

    @Test
    @Transactional
    void getAllProductosByPorcentajeIvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where porcentajeIva is greater than DEFAULT_PORCENTAJE_IVA
        defaultProductosShouldNotBeFound("porcentajeIva.greaterThan=" + DEFAULT_PORCENTAJE_IVA);

        // Get all the productosList where porcentajeIva is greater than SMALLER_PORCENTAJE_IVA
        defaultProductosShouldBeFound("porcentajeIva.greaterThan=" + SMALLER_PORCENTAJE_IVA);
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where descripcion equals to DEFAULT_DESCRIPCION
        defaultProductosShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the productosList where descripcion equals to UPDATED_DESCRIPCION
        defaultProductosShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultProductosShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the productosList where descripcion equals to UPDATED_DESCRIPCION
        defaultProductosShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where descripcion is not null
        defaultProductosShouldBeFound("descripcion.specified=true");

        // Get all the productosList where descripcion is null
        defaultProductosShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where descripcion contains DEFAULT_DESCRIPCION
        defaultProductosShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the productosList where descripcion contains UPDATED_DESCRIPCION
        defaultProductosShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultProductosShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the productosList where descripcion does not contain UPDATED_DESCRIPCION
        defaultProductosShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProductosByPagosIsEqualToSomething() throws Exception {
        Pagos pagos;
        if (TestUtil.findAll(em, Pagos.class).isEmpty()) {
            productosRepository.saveAndFlush(productos);
            pagos = PagosResourceIT.createEntity(em);
        } else {
            pagos = TestUtil.findAll(em, Pagos.class).get(0);
        }
        em.persist(pagos);
        em.flush();
        productos.addPagos(pagos);
        productosRepository.saveAndFlush(productos);
        Long pagosId = pagos.getId();

        // Get all the productosList where pagos equals to pagosId
        defaultProductosShouldBeFound("pagosId.equals=" + pagosId);

        // Get all the productosList where pagos equals to (pagosId + 1)
        defaultProductosShouldNotBeFound("pagosId.equals=" + (pagosId + 1));
    }

    @Test
    @Transactional
    void getAllProductosByFacturaDetalleIsEqualToSomething() throws Exception {
        FacturaDetalle facturaDetalle;
        if (TestUtil.findAll(em, FacturaDetalle.class).isEmpty()) {
            productosRepository.saveAndFlush(productos);
            facturaDetalle = FacturaDetalleResourceIT.createEntity(em);
        } else {
            facturaDetalle = TestUtil.findAll(em, FacturaDetalle.class).get(0);
        }
        em.persist(facturaDetalle);
        em.flush();
        productos.addFacturaDetalle(facturaDetalle);
        productosRepository.saveAndFlush(productos);
        Long facturaDetalleId = facturaDetalle.getId();

        // Get all the productosList where facturaDetalle equals to facturaDetalleId
        defaultProductosShouldBeFound("facturaDetalleId.equals=" + facturaDetalleId);

        // Get all the productosList where facturaDetalle equals to (facturaDetalleId + 1)
        defaultProductosShouldNotBeFound("facturaDetalleId.equals=" + (facturaDetalleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductosShouldBeFound(String filter) throws Exception {
        restProductosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productos.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoProducto").value(hasItem(DEFAULT_TIPO_PRODUCTO.toString())))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(DEFAULT_PRECIO_UNITARIO)))
            .andExpect(jsonPath("$.[*].porcentajeIva").value(hasItem(DEFAULT_PORCENTAJE_IVA)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restProductosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductosShouldNotBeFound(String filter) throws Exception {
        restProductosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductos() throws Exception {
        // Get the productos
        restProductosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductos() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        int databaseSizeBeforeUpdate = productosRepository.findAll().size();

        // Update the productos
        Productos updatedProductos = productosRepository.findById(productos.getId()).get();
        // Disconnect from session so that the updates on updatedProductos are not directly saved in db
        em.detach(updatedProductos);
        updatedProductos
            .tipoProducto(UPDATED_TIPO_PRODUCTO)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .porcentajeIva(UPDATED_PORCENTAJE_IVA)
            .descripcion(UPDATED_DESCRIPCION);

        restProductosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductos))
            )
            .andExpect(status().isOk());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
        Productos testProductos = productosList.get(productosList.size() - 1);
        assertThat(testProductos.getTipoProducto()).isEqualTo(UPDATED_TIPO_PRODUCTO);
        assertThat(testProductos.getPrecioUnitario()).isEqualTo(UPDATED_PRECIO_UNITARIO);
        assertThat(testProductos.getPorcentajeIva()).isEqualTo(UPDATED_PORCENTAJE_IVA);
        assertThat(testProductos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductosWithPatch() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        int databaseSizeBeforeUpdate = productosRepository.findAll().size();

        // Update the productos using partial update
        Productos partialUpdatedProductos = new Productos();
        partialUpdatedProductos.setId(productos.getId());

        partialUpdatedProductos.tipoProducto(UPDATED_TIPO_PRODUCTO).descripcion(UPDATED_DESCRIPCION);

        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductos))
            )
            .andExpect(status().isOk());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
        Productos testProductos = productosList.get(productosList.size() - 1);
        assertThat(testProductos.getTipoProducto()).isEqualTo(UPDATED_TIPO_PRODUCTO);
        assertThat(testProductos.getPrecioUnitario()).isEqualTo(DEFAULT_PRECIO_UNITARIO);
        assertThat(testProductos.getPorcentajeIva()).isEqualTo(DEFAULT_PORCENTAJE_IVA);
        assertThat(testProductos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateProductosWithPatch() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        int databaseSizeBeforeUpdate = productosRepository.findAll().size();

        // Update the productos using partial update
        Productos partialUpdatedProductos = new Productos();
        partialUpdatedProductos.setId(productos.getId());

        partialUpdatedProductos
            .tipoProducto(UPDATED_TIPO_PRODUCTO)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .porcentajeIva(UPDATED_PORCENTAJE_IVA)
            .descripcion(UPDATED_DESCRIPCION);

        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductos))
            )
            .andExpect(status().isOk());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
        Productos testProductos = productosList.get(productosList.size() - 1);
        assertThat(testProductos.getTipoProducto()).isEqualTo(UPDATED_TIPO_PRODUCTO);
        assertThat(testProductos.getPrecioUnitario()).isEqualTo(UPDATED_PRECIO_UNITARIO);
        assertThat(testProductos.getPorcentajeIva()).isEqualTo(UPDATED_PORCENTAJE_IVA);
        assertThat(testProductos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productos))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductos() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        int databaseSizeBeforeDelete = productosRepository.findAll().size();

        // Delete the productos
        restProductosMockMvc
            .perform(delete(ENTITY_API_URL_ID, productos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
