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
import py.com.abf.domain.Productos;
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

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_TOTAL = 1;
    private static final Integer UPDATED_TOTAL = 2;
    private static final Integer SMALLER_TOTAL = 1 - 1;

    private static final Integer DEFAULT_CANTIDAD_HORAS = 1;
    private static final Integer UPDATED_CANTIDAD_HORAS = 2;
    private static final Integer SMALLER_CANTIDAD_HORAS = 1 - 1;

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
        Pagos pagos = new Pagos().fecha(DEFAULT_FECHA).total(DEFAULT_TOTAL).cantidadHoras(DEFAULT_CANTIDAD_HORAS);
        // Add required entity
        Productos productos;
        if (TestUtil.findAll(em, Productos.class).isEmpty()) {
            productos = ProductosResourceIT.createEntity(em);
            em.persist(productos);
            em.flush();
        } else {
            productos = TestUtil.findAll(em, Productos.class).get(0);
        }
        pagos.setProducto(productos);
        return pagos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pagos createUpdatedEntity(EntityManager em) {
        Pagos pagos = new Pagos().fecha(UPDATED_FECHA).total(UPDATED_TOTAL).cantidadHoras(UPDATED_CANTIDAD_HORAS);
        // Add required entity
        Productos productos;
        if (TestUtil.findAll(em, Productos.class).isEmpty()) {
            productos = ProductosResourceIT.createUpdatedEntity(em);
            em.persist(productos);
            em.flush();
        } else {
            productos = TestUtil.findAll(em, Productos.class).get(0);
        }
        pagos.setProducto(productos);
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
        assertThat(testPagos.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testPagos.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testPagos.getCantidadHoras()).isEqualTo(DEFAULT_CANTIDAD_HORAS);
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
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagosRepository.findAll().size();
        // set the field null
        pagos.setFecha(null);

        // Create the Pagos, which fails.

        restPagosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagos)))
            .andExpect(status().isBadRequest());

        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagosRepository.findAll().size();
        // set the field null
        pagos.setTotal(null);

        // Create the Pagos, which fails.

        restPagosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagos)))
            .andExpect(status().isBadRequest());

        List<Pagos> pagosList = pagosRepository.findAll();
        assertThat(pagosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadHorasIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagosRepository.findAll().size();
        // set the field null
        pagos.setCantidadHoras(null);

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
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].cantidadHoras").value(hasItem(DEFAULT_CANTIDAD_HORAS)));
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
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL))
            .andExpect(jsonPath("$.cantidadHoras").value(DEFAULT_CANTIDAD_HORAS));
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
    void getAllPagosByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fecha equals to DEFAULT_FECHA
        defaultPagosShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the pagosList where fecha equals to UPDATED_FECHA
        defaultPagosShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllPagosByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultPagosShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the pagosList where fecha equals to UPDATED_FECHA
        defaultPagosShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllPagosByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fecha is not null
        defaultPagosShouldBeFound("fecha.specified=true");

        // Get all the pagosList where fecha is null
        defaultPagosShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fecha is greater than or equal to DEFAULT_FECHA
        defaultPagosShouldBeFound("fecha.greaterThanOrEqual=" + DEFAULT_FECHA);

        // Get all the pagosList where fecha is greater than or equal to UPDATED_FECHA
        defaultPagosShouldNotBeFound("fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllPagosByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fecha is less than or equal to DEFAULT_FECHA
        defaultPagosShouldBeFound("fecha.lessThanOrEqual=" + DEFAULT_FECHA);

        // Get all the pagosList where fecha is less than or equal to SMALLER_FECHA
        defaultPagosShouldNotBeFound("fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllPagosByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fecha is less than DEFAULT_FECHA
        defaultPagosShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the pagosList where fecha is less than UPDATED_FECHA
        defaultPagosShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllPagosByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where fecha is greater than DEFAULT_FECHA
        defaultPagosShouldNotBeFound("fecha.greaterThan=" + DEFAULT_FECHA);

        // Get all the pagosList where fecha is greater than SMALLER_FECHA
        defaultPagosShouldBeFound("fecha.greaterThan=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllPagosByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where total equals to DEFAULT_TOTAL
        defaultPagosShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the pagosList where total equals to UPDATED_TOTAL
        defaultPagosShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllPagosByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultPagosShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the pagosList where total equals to UPDATED_TOTAL
        defaultPagosShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllPagosByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where total is not null
        defaultPagosShouldBeFound("total.specified=true");

        // Get all the pagosList where total is null
        defaultPagosShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where total is greater than or equal to DEFAULT_TOTAL
        defaultPagosShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the pagosList where total is greater than or equal to UPDATED_TOTAL
        defaultPagosShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllPagosByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where total is less than or equal to DEFAULT_TOTAL
        defaultPagosShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the pagosList where total is less than or equal to SMALLER_TOTAL
        defaultPagosShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllPagosByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where total is less than DEFAULT_TOTAL
        defaultPagosShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the pagosList where total is less than UPDATED_TOTAL
        defaultPagosShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllPagosByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where total is greater than DEFAULT_TOTAL
        defaultPagosShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the pagosList where total is greater than SMALLER_TOTAL
        defaultPagosShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllPagosByCantidadHorasIsEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where cantidadHoras equals to DEFAULT_CANTIDAD_HORAS
        defaultPagosShouldBeFound("cantidadHoras.equals=" + DEFAULT_CANTIDAD_HORAS);

        // Get all the pagosList where cantidadHoras equals to UPDATED_CANTIDAD_HORAS
        defaultPagosShouldNotBeFound("cantidadHoras.equals=" + UPDATED_CANTIDAD_HORAS);
    }

    @Test
    @Transactional
    void getAllPagosByCantidadHorasIsInShouldWork() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where cantidadHoras in DEFAULT_CANTIDAD_HORAS or UPDATED_CANTIDAD_HORAS
        defaultPagosShouldBeFound("cantidadHoras.in=" + DEFAULT_CANTIDAD_HORAS + "," + UPDATED_CANTIDAD_HORAS);

        // Get all the pagosList where cantidadHoras equals to UPDATED_CANTIDAD_HORAS
        defaultPagosShouldNotBeFound("cantidadHoras.in=" + UPDATED_CANTIDAD_HORAS);
    }

    @Test
    @Transactional
    void getAllPagosByCantidadHorasIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where cantidadHoras is not null
        defaultPagosShouldBeFound("cantidadHoras.specified=true");

        // Get all the pagosList where cantidadHoras is null
        defaultPagosShouldNotBeFound("cantidadHoras.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByCantidadHorasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where cantidadHoras is greater than or equal to DEFAULT_CANTIDAD_HORAS
        defaultPagosShouldBeFound("cantidadHoras.greaterThanOrEqual=" + DEFAULT_CANTIDAD_HORAS);

        // Get all the pagosList where cantidadHoras is greater than or equal to UPDATED_CANTIDAD_HORAS
        defaultPagosShouldNotBeFound("cantidadHoras.greaterThanOrEqual=" + UPDATED_CANTIDAD_HORAS);
    }

    @Test
    @Transactional
    void getAllPagosByCantidadHorasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where cantidadHoras is less than or equal to DEFAULT_CANTIDAD_HORAS
        defaultPagosShouldBeFound("cantidadHoras.lessThanOrEqual=" + DEFAULT_CANTIDAD_HORAS);

        // Get all the pagosList where cantidadHoras is less than or equal to SMALLER_CANTIDAD_HORAS
        defaultPagosShouldNotBeFound("cantidadHoras.lessThanOrEqual=" + SMALLER_CANTIDAD_HORAS);
    }

    @Test
    @Transactional
    void getAllPagosByCantidadHorasIsLessThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where cantidadHoras is less than DEFAULT_CANTIDAD_HORAS
        defaultPagosShouldNotBeFound("cantidadHoras.lessThan=" + DEFAULT_CANTIDAD_HORAS);

        // Get all the pagosList where cantidadHoras is less than UPDATED_CANTIDAD_HORAS
        defaultPagosShouldBeFound("cantidadHoras.lessThan=" + UPDATED_CANTIDAD_HORAS);
    }

    @Test
    @Transactional
    void getAllPagosByCantidadHorasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pagosRepository.saveAndFlush(pagos);

        // Get all the pagosList where cantidadHoras is greater than DEFAULT_CANTIDAD_HORAS
        defaultPagosShouldNotBeFound("cantidadHoras.greaterThan=" + DEFAULT_CANTIDAD_HORAS);

        // Get all the pagosList where cantidadHoras is greater than SMALLER_CANTIDAD_HORAS
        defaultPagosShouldBeFound("cantidadHoras.greaterThan=" + SMALLER_CANTIDAD_HORAS);
    }

    @Test
    @Transactional
    void getAllPagosByProductoIsEqualToSomething() throws Exception {
        Productos producto;
        if (TestUtil.findAll(em, Productos.class).isEmpty()) {
            pagosRepository.saveAndFlush(pagos);
            producto = ProductosResourceIT.createEntity(em);
        } else {
            producto = TestUtil.findAll(em, Productos.class).get(0);
        }
        em.persist(producto);
        em.flush();
        pagos.setProducto(producto);
        pagosRepository.saveAndFlush(pagos);
        Long productoId = producto.getId();

        // Get all the pagosList where producto equals to productoId
        defaultPagosShouldBeFound("productoId.equals=" + productoId);

        // Get all the pagosList where producto equals to (productoId + 1)
        defaultPagosShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    @Test
    @Transactional
    void getAllPagosByFuncionarioIsEqualToSomething() throws Exception {
        Funcionarios funcionario;
        if (TestUtil.findAll(em, Funcionarios.class).isEmpty()) {
            pagosRepository.saveAndFlush(pagos);
            funcionario = FuncionariosResourceIT.createEntity(em);
        } else {
            funcionario = TestUtil.findAll(em, Funcionarios.class).get(0);
        }
        em.persist(funcionario);
        em.flush();
        pagos.setFuncionario(funcionario);
        pagosRepository.saveAndFlush(pagos);
        Long funcionarioId = funcionario.getId();

        // Get all the pagosList where funcionario equals to funcionarioId
        defaultPagosShouldBeFound("funcionarioId.equals=" + funcionarioId);

        // Get all the pagosList where funcionario equals to (funcionarioId + 1)
        defaultPagosShouldNotBeFound("funcionarioId.equals=" + (funcionarioId + 1));
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
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].cantidadHoras").value(hasItem(DEFAULT_CANTIDAD_HORAS)));

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
        updatedPagos.fecha(UPDATED_FECHA).total(UPDATED_TOTAL).cantidadHoras(UPDATED_CANTIDAD_HORAS);

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
        assertThat(testPagos.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testPagos.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testPagos.getCantidadHoras()).isEqualTo(UPDATED_CANTIDAD_HORAS);
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

        partialUpdatedPagos.total(UPDATED_TOTAL).cantidadHoras(UPDATED_CANTIDAD_HORAS);

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
        assertThat(testPagos.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testPagos.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testPagos.getCantidadHoras()).isEqualTo(UPDATED_CANTIDAD_HORAS);
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

        partialUpdatedPagos.fecha(UPDATED_FECHA).total(UPDATED_TOTAL).cantidadHoras(UPDATED_CANTIDAD_HORAS);

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
        assertThat(testPagos.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testPagos.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testPagos.getCantidadHoras()).isEqualTo(UPDATED_CANTIDAD_HORAS);
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
