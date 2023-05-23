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
import py.com.abf.domain.Materiales;
import py.com.abf.domain.Prestamos;
import py.com.abf.domain.enumeration.EstadosPrestamos;
import py.com.abf.repository.PrestamosRepository;
import py.com.abf.service.PrestamosService;
import py.com.abf.service.criteria.PrestamosCriteria;

/**
 * Integration tests for the {@link PrestamosResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrestamosResourceIT {

    private static final LocalDate DEFAULT_FECHA_PRESTAMO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_PRESTAMO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_PRESTAMO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_DEVOLUCION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_DEVOLUCION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_DEVOLUCION = LocalDate.ofEpochDay(-1L);

    private static final EstadosPrestamos DEFAULT_ESTADO = EstadosPrestamos.DEVUELTO;
    private static final EstadosPrestamos UPDATED_ESTADO = EstadosPrestamos.PRESTADO;

    private static final String ENTITY_API_URL = "/api/prestamos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrestamosRepository prestamosRepository;

    @Mock
    private PrestamosRepository prestamosRepositoryMock;

    @Mock
    private PrestamosService prestamosServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrestamosMockMvc;

    private Prestamos prestamos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestamos createEntity(EntityManager em) {
        Prestamos prestamos = new Prestamos()
            .fechaPrestamo(DEFAULT_FECHA_PRESTAMO)
            .fechaDevolucion(DEFAULT_FECHA_DEVOLUCION)
            .estado(DEFAULT_ESTADO);
        // Add required entity
        Materiales materiales;
        if (TestUtil.findAll(em, Materiales.class).isEmpty()) {
            materiales = MaterialesResourceIT.createEntity(em);
            em.persist(materiales);
            em.flush();
        } else {
            materiales = TestUtil.findAll(em, Materiales.class).get(0);
        }
        prestamos.setMateriales(materiales);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        prestamos.setAlumnos(alumnos);
        return prestamos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestamos createUpdatedEntity(EntityManager em) {
        Prestamos prestamos = new Prestamos()
            .fechaPrestamo(UPDATED_FECHA_PRESTAMO)
            .fechaDevolucion(UPDATED_FECHA_DEVOLUCION)
            .estado(UPDATED_ESTADO);
        // Add required entity
        Materiales materiales;
        if (TestUtil.findAll(em, Materiales.class).isEmpty()) {
            materiales = MaterialesResourceIT.createUpdatedEntity(em);
            em.persist(materiales);
            em.flush();
        } else {
            materiales = TestUtil.findAll(em, Materiales.class).get(0);
        }
        prestamos.setMateriales(materiales);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createUpdatedEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        prestamos.setAlumnos(alumnos);
        return prestamos;
    }

    @BeforeEach
    public void initTest() {
        prestamos = createEntity(em);
    }

    @Test
    @Transactional
    void createPrestamos() throws Exception {
        int databaseSizeBeforeCreate = prestamosRepository.findAll().size();
        // Create the Prestamos
        restPrestamosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestamos)))
            .andExpect(status().isCreated());

        // Validate the Prestamos in the database
        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeCreate + 1);
        Prestamos testPrestamos = prestamosList.get(prestamosList.size() - 1);
        assertThat(testPrestamos.getFechaPrestamo()).isEqualTo(DEFAULT_FECHA_PRESTAMO);
        assertThat(testPrestamos.getFechaDevolucion()).isEqualTo(DEFAULT_FECHA_DEVOLUCION);
        assertThat(testPrestamos.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createPrestamosWithExistingId() throws Exception {
        // Create the Prestamos with an existing ID
        prestamos.setId(1L);

        int databaseSizeBeforeCreate = prestamosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrestamosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestamos)))
            .andExpect(status().isBadRequest());

        // Validate the Prestamos in the database
        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaPrestamoIsRequired() throws Exception {
        int databaseSizeBeforeTest = prestamosRepository.findAll().size();
        // set the field null
        prestamos.setFechaPrestamo(null);

        // Create the Prestamos, which fails.

        restPrestamosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestamos)))
            .andExpect(status().isBadRequest());

        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = prestamosRepository.findAll().size();
        // set the field null
        prestamos.setEstado(null);

        // Create the Prestamos, which fails.

        restPrestamosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestamos)))
            .andExpect(status().isBadRequest());

        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrestamos() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList
        restPrestamosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestamos.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaPrestamo").value(hasItem(DEFAULT_FECHA_PRESTAMO.toString())))
            .andExpect(jsonPath("$.[*].fechaDevolucion").value(hasItem(DEFAULT_FECHA_DEVOLUCION.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrestamosWithEagerRelationshipsIsEnabled() throws Exception {
        when(prestamosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrestamosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prestamosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrestamosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(prestamosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrestamosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(prestamosRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPrestamos() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get the prestamos
        restPrestamosMockMvc
            .perform(get(ENTITY_API_URL_ID, prestamos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prestamos.getId().intValue()))
            .andExpect(jsonPath("$.fechaPrestamo").value(DEFAULT_FECHA_PRESTAMO.toString()))
            .andExpect(jsonPath("$.fechaDevolucion").value(DEFAULT_FECHA_DEVOLUCION.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getPrestamosByIdFiltering() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        Long id = prestamos.getId();

        defaultPrestamosShouldBeFound("id.equals=" + id);
        defaultPrestamosShouldNotBeFound("id.notEquals=" + id);

        defaultPrestamosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrestamosShouldNotBeFound("id.greaterThan=" + id);

        defaultPrestamosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrestamosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaPrestamoIsEqualToSomething() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaPrestamo equals to DEFAULT_FECHA_PRESTAMO
        defaultPrestamosShouldBeFound("fechaPrestamo.equals=" + DEFAULT_FECHA_PRESTAMO);

        // Get all the prestamosList where fechaPrestamo equals to UPDATED_FECHA_PRESTAMO
        defaultPrestamosShouldNotBeFound("fechaPrestamo.equals=" + UPDATED_FECHA_PRESTAMO);
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaPrestamoIsInShouldWork() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaPrestamo in DEFAULT_FECHA_PRESTAMO or UPDATED_FECHA_PRESTAMO
        defaultPrestamosShouldBeFound("fechaPrestamo.in=" + DEFAULT_FECHA_PRESTAMO + "," + UPDATED_FECHA_PRESTAMO);

        // Get all the prestamosList where fechaPrestamo equals to UPDATED_FECHA_PRESTAMO
        defaultPrestamosShouldNotBeFound("fechaPrestamo.in=" + UPDATED_FECHA_PRESTAMO);
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaPrestamoIsNullOrNotNull() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaPrestamo is not null
        defaultPrestamosShouldBeFound("fechaPrestamo.specified=true");

        // Get all the prestamosList where fechaPrestamo is null
        defaultPrestamosShouldNotBeFound("fechaPrestamo.specified=false");
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaPrestamoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaPrestamo is greater than or equal to DEFAULT_FECHA_PRESTAMO
        defaultPrestamosShouldBeFound("fechaPrestamo.greaterThanOrEqual=" + DEFAULT_FECHA_PRESTAMO);

        // Get all the prestamosList where fechaPrestamo is greater than or equal to UPDATED_FECHA_PRESTAMO
        defaultPrestamosShouldNotBeFound("fechaPrestamo.greaterThanOrEqual=" + UPDATED_FECHA_PRESTAMO);
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaPrestamoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaPrestamo is less than or equal to DEFAULT_FECHA_PRESTAMO
        defaultPrestamosShouldBeFound("fechaPrestamo.lessThanOrEqual=" + DEFAULT_FECHA_PRESTAMO);

        // Get all the prestamosList where fechaPrestamo is less than or equal to SMALLER_FECHA_PRESTAMO
        defaultPrestamosShouldNotBeFound("fechaPrestamo.lessThanOrEqual=" + SMALLER_FECHA_PRESTAMO);
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaPrestamoIsLessThanSomething() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaPrestamo is less than DEFAULT_FECHA_PRESTAMO
        defaultPrestamosShouldNotBeFound("fechaPrestamo.lessThan=" + DEFAULT_FECHA_PRESTAMO);

        // Get all the prestamosList where fechaPrestamo is less than UPDATED_FECHA_PRESTAMO
        defaultPrestamosShouldBeFound("fechaPrestamo.lessThan=" + UPDATED_FECHA_PRESTAMO);
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaPrestamoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaPrestamo is greater than DEFAULT_FECHA_PRESTAMO
        defaultPrestamosShouldNotBeFound("fechaPrestamo.greaterThan=" + DEFAULT_FECHA_PRESTAMO);

        // Get all the prestamosList where fechaPrestamo is greater than SMALLER_FECHA_PRESTAMO
        defaultPrestamosShouldBeFound("fechaPrestamo.greaterThan=" + SMALLER_FECHA_PRESTAMO);
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaDevolucionIsEqualToSomething() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaDevolucion equals to DEFAULT_FECHA_DEVOLUCION
        defaultPrestamosShouldBeFound("fechaDevolucion.equals=" + DEFAULT_FECHA_DEVOLUCION);

        // Get all the prestamosList where fechaDevolucion equals to UPDATED_FECHA_DEVOLUCION
        defaultPrestamosShouldNotBeFound("fechaDevolucion.equals=" + UPDATED_FECHA_DEVOLUCION);
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaDevolucionIsInShouldWork() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaDevolucion in DEFAULT_FECHA_DEVOLUCION or UPDATED_FECHA_DEVOLUCION
        defaultPrestamosShouldBeFound("fechaDevolucion.in=" + DEFAULT_FECHA_DEVOLUCION + "," + UPDATED_FECHA_DEVOLUCION);

        // Get all the prestamosList where fechaDevolucion equals to UPDATED_FECHA_DEVOLUCION
        defaultPrestamosShouldNotBeFound("fechaDevolucion.in=" + UPDATED_FECHA_DEVOLUCION);
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaDevolucionIsNullOrNotNull() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaDevolucion is not null
        defaultPrestamosShouldBeFound("fechaDevolucion.specified=true");

        // Get all the prestamosList where fechaDevolucion is null
        defaultPrestamosShouldNotBeFound("fechaDevolucion.specified=false");
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaDevolucionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaDevolucion is greater than or equal to DEFAULT_FECHA_DEVOLUCION
        defaultPrestamosShouldBeFound("fechaDevolucion.greaterThanOrEqual=" + DEFAULT_FECHA_DEVOLUCION);

        // Get all the prestamosList where fechaDevolucion is greater than or equal to UPDATED_FECHA_DEVOLUCION
        defaultPrestamosShouldNotBeFound("fechaDevolucion.greaterThanOrEqual=" + UPDATED_FECHA_DEVOLUCION);
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaDevolucionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaDevolucion is less than or equal to DEFAULT_FECHA_DEVOLUCION
        defaultPrestamosShouldBeFound("fechaDevolucion.lessThanOrEqual=" + DEFAULT_FECHA_DEVOLUCION);

        // Get all the prestamosList where fechaDevolucion is less than or equal to SMALLER_FECHA_DEVOLUCION
        defaultPrestamosShouldNotBeFound("fechaDevolucion.lessThanOrEqual=" + SMALLER_FECHA_DEVOLUCION);
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaDevolucionIsLessThanSomething() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaDevolucion is less than DEFAULT_FECHA_DEVOLUCION
        defaultPrestamosShouldNotBeFound("fechaDevolucion.lessThan=" + DEFAULT_FECHA_DEVOLUCION);

        // Get all the prestamosList where fechaDevolucion is less than UPDATED_FECHA_DEVOLUCION
        defaultPrestamosShouldBeFound("fechaDevolucion.lessThan=" + UPDATED_FECHA_DEVOLUCION);
    }

    @Test
    @Transactional
    void getAllPrestamosByFechaDevolucionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where fechaDevolucion is greater than DEFAULT_FECHA_DEVOLUCION
        defaultPrestamosShouldNotBeFound("fechaDevolucion.greaterThan=" + DEFAULT_FECHA_DEVOLUCION);

        // Get all the prestamosList where fechaDevolucion is greater than SMALLER_FECHA_DEVOLUCION
        defaultPrestamosShouldBeFound("fechaDevolucion.greaterThan=" + SMALLER_FECHA_DEVOLUCION);
    }

    @Test
    @Transactional
    void getAllPrestamosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where estado equals to DEFAULT_ESTADO
        defaultPrestamosShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the prestamosList where estado equals to UPDATED_ESTADO
        defaultPrestamosShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPrestamosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultPrestamosShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the prestamosList where estado equals to UPDATED_ESTADO
        defaultPrestamosShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPrestamosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        // Get all the prestamosList where estado is not null
        defaultPrestamosShouldBeFound("estado.specified=true");

        // Get all the prestamosList where estado is null
        defaultPrestamosShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllPrestamosByMaterialesIsEqualToSomething() throws Exception {
        Materiales materiales;
        if (TestUtil.findAll(em, Materiales.class).isEmpty()) {
            prestamosRepository.saveAndFlush(prestamos);
            materiales = MaterialesResourceIT.createEntity(em);
        } else {
            materiales = TestUtil.findAll(em, Materiales.class).get(0);
        }
        em.persist(materiales);
        em.flush();
        prestamos.setMateriales(materiales);
        prestamosRepository.saveAndFlush(prestamos);
        Long materialesId = materiales.getId();

        // Get all the prestamosList where materiales equals to materialesId
        defaultPrestamosShouldBeFound("materialesId.equals=" + materialesId);

        // Get all the prestamosList where materiales equals to (materialesId + 1)
        defaultPrestamosShouldNotBeFound("materialesId.equals=" + (materialesId + 1));
    }

    @Test
    @Transactional
    void getAllPrestamosByAlumnosIsEqualToSomething() throws Exception {
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            prestamosRepository.saveAndFlush(prestamos);
            alumnos = AlumnosResourceIT.createEntity(em);
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        em.persist(alumnos);
        em.flush();
        prestamos.setAlumnos(alumnos);
        prestamosRepository.saveAndFlush(prestamos);
        Long alumnosId = alumnos.getId();

        // Get all the prestamosList where alumnos equals to alumnosId
        defaultPrestamosShouldBeFound("alumnosId.equals=" + alumnosId);

        // Get all the prestamosList where alumnos equals to (alumnosId + 1)
        defaultPrestamosShouldNotBeFound("alumnosId.equals=" + (alumnosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrestamosShouldBeFound(String filter) throws Exception {
        restPrestamosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestamos.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaPrestamo").value(hasItem(DEFAULT_FECHA_PRESTAMO.toString())))
            .andExpect(jsonPath("$.[*].fechaDevolucion").value(hasItem(DEFAULT_FECHA_DEVOLUCION.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));

        // Check, that the count call also returns 1
        restPrestamosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrestamosShouldNotBeFound(String filter) throws Exception {
        restPrestamosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrestamosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrestamos() throws Exception {
        // Get the prestamos
        restPrestamosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrestamos() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        int databaseSizeBeforeUpdate = prestamosRepository.findAll().size();

        // Update the prestamos
        Prestamos updatedPrestamos = prestamosRepository.findById(prestamos.getId()).get();
        // Disconnect from session so that the updates on updatedPrestamos are not directly saved in db
        em.detach(updatedPrestamos);
        updatedPrestamos.fechaPrestamo(UPDATED_FECHA_PRESTAMO).fechaDevolucion(UPDATED_FECHA_DEVOLUCION).estado(UPDATED_ESTADO);

        restPrestamosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrestamos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrestamos))
            )
            .andExpect(status().isOk());

        // Validate the Prestamos in the database
        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeUpdate);
        Prestamos testPrestamos = prestamosList.get(prestamosList.size() - 1);
        assertThat(testPrestamos.getFechaPrestamo()).isEqualTo(UPDATED_FECHA_PRESTAMO);
        assertThat(testPrestamos.getFechaDevolucion()).isEqualTo(UPDATED_FECHA_DEVOLUCION);
        assertThat(testPrestamos.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingPrestamos() throws Exception {
        int databaseSizeBeforeUpdate = prestamosRepository.findAll().size();
        prestamos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestamosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestamos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prestamos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestamos in the database
        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrestamos() throws Exception {
        int databaseSizeBeforeUpdate = prestamosRepository.findAll().size();
        prestamos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestamosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prestamos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestamos in the database
        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrestamos() throws Exception {
        int databaseSizeBeforeUpdate = prestamosRepository.findAll().size();
        prestamos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestamosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestamos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestamos in the database
        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrestamosWithPatch() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        int databaseSizeBeforeUpdate = prestamosRepository.findAll().size();

        // Update the prestamos using partial update
        Prestamos partialUpdatedPrestamos = new Prestamos();
        partialUpdatedPrestamos.setId(prestamos.getId());

        restPrestamosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestamos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrestamos))
            )
            .andExpect(status().isOk());

        // Validate the Prestamos in the database
        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeUpdate);
        Prestamos testPrestamos = prestamosList.get(prestamosList.size() - 1);
        assertThat(testPrestamos.getFechaPrestamo()).isEqualTo(DEFAULT_FECHA_PRESTAMO);
        assertThat(testPrestamos.getFechaDevolucion()).isEqualTo(DEFAULT_FECHA_DEVOLUCION);
        assertThat(testPrestamos.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdatePrestamosWithPatch() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        int databaseSizeBeforeUpdate = prestamosRepository.findAll().size();

        // Update the prestamos using partial update
        Prestamos partialUpdatedPrestamos = new Prestamos();
        partialUpdatedPrestamos.setId(prestamos.getId());

        partialUpdatedPrestamos.fechaPrestamo(UPDATED_FECHA_PRESTAMO).fechaDevolucion(UPDATED_FECHA_DEVOLUCION).estado(UPDATED_ESTADO);

        restPrestamosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestamos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrestamos))
            )
            .andExpect(status().isOk());

        // Validate the Prestamos in the database
        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeUpdate);
        Prestamos testPrestamos = prestamosList.get(prestamosList.size() - 1);
        assertThat(testPrestamos.getFechaPrestamo()).isEqualTo(UPDATED_FECHA_PRESTAMO);
        assertThat(testPrestamos.getFechaDevolucion()).isEqualTo(UPDATED_FECHA_DEVOLUCION);
        assertThat(testPrestamos.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingPrestamos() throws Exception {
        int databaseSizeBeforeUpdate = prestamosRepository.findAll().size();
        prestamos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestamosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prestamos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prestamos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestamos in the database
        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrestamos() throws Exception {
        int databaseSizeBeforeUpdate = prestamosRepository.findAll().size();
        prestamos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestamosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prestamos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestamos in the database
        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrestamos() throws Exception {
        int databaseSizeBeforeUpdate = prestamosRepository.findAll().size();
        prestamos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestamosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(prestamos))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestamos in the database
        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrestamos() throws Exception {
        // Initialize the database
        prestamosRepository.saveAndFlush(prestamos);

        int databaseSizeBeforeDelete = prestamosRepository.findAll().size();

        // Delete the prestamos
        restPrestamosMockMvc
            .perform(delete(ENTITY_API_URL_ID, prestamos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prestamos> prestamosList = prestamosRepository.findAll();
        assertThat(prestamosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
