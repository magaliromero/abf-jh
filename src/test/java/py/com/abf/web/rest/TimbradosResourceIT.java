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
import py.com.abf.domain.Sucursales;
import py.com.abf.domain.Timbrados;
import py.com.abf.repository.TimbradosRepository;
import py.com.abf.service.criteria.TimbradosCriteria;

/**
 * Integration tests for the {@link TimbradosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimbradosResourceIT {

    private static final String DEFAULT_NUMERO_TIMBRADO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_TIMBRADO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_INICIO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_FIN = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/timbrados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TimbradosRepository timbradosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimbradosMockMvc;

    private Timbrados timbrados;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Timbrados createEntity(EntityManager em) {
        Timbrados timbrados = new Timbrados()
            .numeroTimbrado(DEFAULT_NUMERO_TIMBRADO)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN);
        return timbrados;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Timbrados createUpdatedEntity(EntityManager em) {
        Timbrados timbrados = new Timbrados()
            .numeroTimbrado(UPDATED_NUMERO_TIMBRADO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN);
        return timbrados;
    }

    @BeforeEach
    public void initTest() {
        timbrados = createEntity(em);
    }

    @Test
    @Transactional
    void createTimbrados() throws Exception {
        int databaseSizeBeforeCreate = timbradosRepository.findAll().size();
        // Create the Timbrados
        restTimbradosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timbrados)))
            .andExpect(status().isCreated());

        // Validate the Timbrados in the database
        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeCreate + 1);
        Timbrados testTimbrados = timbradosList.get(timbradosList.size() - 1);
        assertThat(testTimbrados.getNumeroTimbrado()).isEqualTo(DEFAULT_NUMERO_TIMBRADO);
        assertThat(testTimbrados.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testTimbrados.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
    }

    @Test
    @Transactional
    void createTimbradosWithExistingId() throws Exception {
        // Create the Timbrados with an existing ID
        timbrados.setId(1L);

        int databaseSizeBeforeCreate = timbradosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimbradosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timbrados)))
            .andExpect(status().isBadRequest());

        // Validate the Timbrados in the database
        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroTimbradoIsRequired() throws Exception {
        int databaseSizeBeforeTest = timbradosRepository.findAll().size();
        // set the field null
        timbrados.setNumeroTimbrado(null);

        // Create the Timbrados, which fails.

        restTimbradosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timbrados)))
            .andExpect(status().isBadRequest());

        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = timbradosRepository.findAll().size();
        // set the field null
        timbrados.setFechaInicio(null);

        // Create the Timbrados, which fails.

        restTimbradosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timbrados)))
            .andExpect(status().isBadRequest());

        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = timbradosRepository.findAll().size();
        // set the field null
        timbrados.setFechaFin(null);

        // Create the Timbrados, which fails.

        restTimbradosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timbrados)))
            .andExpect(status().isBadRequest());

        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTimbrados() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList
        restTimbradosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timbrados.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroTimbrado").value(hasItem(DEFAULT_NUMERO_TIMBRADO)))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())));
    }

    @Test
    @Transactional
    void getTimbrados() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get the timbrados
        restTimbradosMockMvc
            .perform(get(ENTITY_API_URL_ID, timbrados.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timbrados.getId().intValue()))
            .andExpect(jsonPath("$.numeroTimbrado").value(DEFAULT_NUMERO_TIMBRADO))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()));
    }

    @Test
    @Transactional
    void getTimbradosByIdFiltering() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        Long id = timbrados.getId();

        defaultTimbradosShouldBeFound("id.equals=" + id);
        defaultTimbradosShouldNotBeFound("id.notEquals=" + id);

        defaultTimbradosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTimbradosShouldNotBeFound("id.greaterThan=" + id);

        defaultTimbradosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTimbradosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTimbradosByNumeroTimbradoIsEqualToSomething() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where numeroTimbrado equals to DEFAULT_NUMERO_TIMBRADO
        defaultTimbradosShouldBeFound("numeroTimbrado.equals=" + DEFAULT_NUMERO_TIMBRADO);

        // Get all the timbradosList where numeroTimbrado equals to UPDATED_NUMERO_TIMBRADO
        defaultTimbradosShouldNotBeFound("numeroTimbrado.equals=" + UPDATED_NUMERO_TIMBRADO);
    }

    @Test
    @Transactional
    void getAllTimbradosByNumeroTimbradoIsInShouldWork() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where numeroTimbrado in DEFAULT_NUMERO_TIMBRADO or UPDATED_NUMERO_TIMBRADO
        defaultTimbradosShouldBeFound("numeroTimbrado.in=" + DEFAULT_NUMERO_TIMBRADO + "," + UPDATED_NUMERO_TIMBRADO);

        // Get all the timbradosList where numeroTimbrado equals to UPDATED_NUMERO_TIMBRADO
        defaultTimbradosShouldNotBeFound("numeroTimbrado.in=" + UPDATED_NUMERO_TIMBRADO);
    }

    @Test
    @Transactional
    void getAllTimbradosByNumeroTimbradoIsNullOrNotNull() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where numeroTimbrado is not null
        defaultTimbradosShouldBeFound("numeroTimbrado.specified=true");

        // Get all the timbradosList where numeroTimbrado is null
        defaultTimbradosShouldNotBeFound("numeroTimbrado.specified=false");
    }

    @Test
    @Transactional
    void getAllTimbradosByNumeroTimbradoContainsSomething() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where numeroTimbrado contains DEFAULT_NUMERO_TIMBRADO
        defaultTimbradosShouldBeFound("numeroTimbrado.contains=" + DEFAULT_NUMERO_TIMBRADO);

        // Get all the timbradosList where numeroTimbrado contains UPDATED_NUMERO_TIMBRADO
        defaultTimbradosShouldNotBeFound("numeroTimbrado.contains=" + UPDATED_NUMERO_TIMBRADO);
    }

    @Test
    @Transactional
    void getAllTimbradosByNumeroTimbradoNotContainsSomething() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where numeroTimbrado does not contain DEFAULT_NUMERO_TIMBRADO
        defaultTimbradosShouldNotBeFound("numeroTimbrado.doesNotContain=" + DEFAULT_NUMERO_TIMBRADO);

        // Get all the timbradosList where numeroTimbrado does not contain UPDATED_NUMERO_TIMBRADO
        defaultTimbradosShouldBeFound("numeroTimbrado.doesNotContain=" + UPDATED_NUMERO_TIMBRADO);
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaInicio equals to DEFAULT_FECHA_INICIO
        defaultTimbradosShouldBeFound("fechaInicio.equals=" + DEFAULT_FECHA_INICIO);

        // Get all the timbradosList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultTimbradosShouldNotBeFound("fechaInicio.equals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaInicioIsInShouldWork() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaInicio in DEFAULT_FECHA_INICIO or UPDATED_FECHA_INICIO
        defaultTimbradosShouldBeFound("fechaInicio.in=" + DEFAULT_FECHA_INICIO + "," + UPDATED_FECHA_INICIO);

        // Get all the timbradosList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultTimbradosShouldNotBeFound("fechaInicio.in=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaInicio is not null
        defaultTimbradosShouldBeFound("fechaInicio.specified=true");

        // Get all the timbradosList where fechaInicio is null
        defaultTimbradosShouldNotBeFound("fechaInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaInicio is greater than or equal to DEFAULT_FECHA_INICIO
        defaultTimbradosShouldBeFound("fechaInicio.greaterThanOrEqual=" + DEFAULT_FECHA_INICIO);

        // Get all the timbradosList where fechaInicio is greater than or equal to UPDATED_FECHA_INICIO
        defaultTimbradosShouldNotBeFound("fechaInicio.greaterThanOrEqual=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaInicioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaInicio is less than or equal to DEFAULT_FECHA_INICIO
        defaultTimbradosShouldBeFound("fechaInicio.lessThanOrEqual=" + DEFAULT_FECHA_INICIO);

        // Get all the timbradosList where fechaInicio is less than or equal to SMALLER_FECHA_INICIO
        defaultTimbradosShouldNotBeFound("fechaInicio.lessThanOrEqual=" + SMALLER_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaInicio is less than DEFAULT_FECHA_INICIO
        defaultTimbradosShouldNotBeFound("fechaInicio.lessThan=" + DEFAULT_FECHA_INICIO);

        // Get all the timbradosList where fechaInicio is less than UPDATED_FECHA_INICIO
        defaultTimbradosShouldBeFound("fechaInicio.lessThan=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaInicioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaInicio is greater than DEFAULT_FECHA_INICIO
        defaultTimbradosShouldNotBeFound("fechaInicio.greaterThan=" + DEFAULT_FECHA_INICIO);

        // Get all the timbradosList where fechaInicio is greater than SMALLER_FECHA_INICIO
        defaultTimbradosShouldBeFound("fechaInicio.greaterThan=" + SMALLER_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaFinIsEqualToSomething() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaFin equals to DEFAULT_FECHA_FIN
        defaultTimbradosShouldBeFound("fechaFin.equals=" + DEFAULT_FECHA_FIN);

        // Get all the timbradosList where fechaFin equals to UPDATED_FECHA_FIN
        defaultTimbradosShouldNotBeFound("fechaFin.equals=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaFinIsInShouldWork() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaFin in DEFAULT_FECHA_FIN or UPDATED_FECHA_FIN
        defaultTimbradosShouldBeFound("fechaFin.in=" + DEFAULT_FECHA_FIN + "," + UPDATED_FECHA_FIN);

        // Get all the timbradosList where fechaFin equals to UPDATED_FECHA_FIN
        defaultTimbradosShouldNotBeFound("fechaFin.in=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaFin is not null
        defaultTimbradosShouldBeFound("fechaFin.specified=true");

        // Get all the timbradosList where fechaFin is null
        defaultTimbradosShouldNotBeFound("fechaFin.specified=false");
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaFin is greater than or equal to DEFAULT_FECHA_FIN
        defaultTimbradosShouldBeFound("fechaFin.greaterThanOrEqual=" + DEFAULT_FECHA_FIN);

        // Get all the timbradosList where fechaFin is greater than or equal to UPDATED_FECHA_FIN
        defaultTimbradosShouldNotBeFound("fechaFin.greaterThanOrEqual=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaFin is less than or equal to DEFAULT_FECHA_FIN
        defaultTimbradosShouldBeFound("fechaFin.lessThanOrEqual=" + DEFAULT_FECHA_FIN);

        // Get all the timbradosList where fechaFin is less than or equal to SMALLER_FECHA_FIN
        defaultTimbradosShouldNotBeFound("fechaFin.lessThanOrEqual=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaFinIsLessThanSomething() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaFin is less than DEFAULT_FECHA_FIN
        defaultTimbradosShouldNotBeFound("fechaFin.lessThan=" + DEFAULT_FECHA_FIN);

        // Get all the timbradosList where fechaFin is less than UPDATED_FECHA_FIN
        defaultTimbradosShouldBeFound("fechaFin.lessThan=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTimbradosByFechaFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        // Get all the timbradosList where fechaFin is greater than DEFAULT_FECHA_FIN
        defaultTimbradosShouldNotBeFound("fechaFin.greaterThan=" + DEFAULT_FECHA_FIN);

        // Get all the timbradosList where fechaFin is greater than SMALLER_FECHA_FIN
        defaultTimbradosShouldBeFound("fechaFin.greaterThan=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTimbradosBySucursalesIsEqualToSomething() throws Exception {
        Sucursales sucursales;
        if (TestUtil.findAll(em, Sucursales.class).isEmpty()) {
            timbradosRepository.saveAndFlush(timbrados);
            sucursales = SucursalesResourceIT.createEntity(em);
        } else {
            sucursales = TestUtil.findAll(em, Sucursales.class).get(0);
        }
        em.persist(sucursales);
        em.flush();
        timbrados.setSucursales(sucursales);
        timbradosRepository.saveAndFlush(timbrados);
        Long sucursalesId = sucursales.getId();

        // Get all the timbradosList where sucursales equals to sucursalesId
        defaultTimbradosShouldBeFound("sucursalesId.equals=" + sucursalesId);

        // Get all the timbradosList where sucursales equals to (sucursalesId + 1)
        defaultTimbradosShouldNotBeFound("sucursalesId.equals=" + (sucursalesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTimbradosShouldBeFound(String filter) throws Exception {
        restTimbradosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timbrados.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroTimbrado").value(hasItem(DEFAULT_NUMERO_TIMBRADO)))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())));

        // Check, that the count call also returns 1
        restTimbradosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTimbradosShouldNotBeFound(String filter) throws Exception {
        restTimbradosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTimbradosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTimbrados() throws Exception {
        // Get the timbrados
        restTimbradosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimbrados() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        int databaseSizeBeforeUpdate = timbradosRepository.findAll().size();

        // Update the timbrados
        Timbrados updatedTimbrados = timbradosRepository.findById(timbrados.getId()).get();
        // Disconnect from session so that the updates on updatedTimbrados are not directly saved in db
        em.detach(updatedTimbrados);
        updatedTimbrados.numeroTimbrado(UPDATED_NUMERO_TIMBRADO).fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);

        restTimbradosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTimbrados.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTimbrados))
            )
            .andExpect(status().isOk());

        // Validate the Timbrados in the database
        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeUpdate);
        Timbrados testTimbrados = timbradosList.get(timbradosList.size() - 1);
        assertThat(testTimbrados.getNumeroTimbrado()).isEqualTo(UPDATED_NUMERO_TIMBRADO);
        assertThat(testTimbrados.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testTimbrados.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void putNonExistingTimbrados() throws Exception {
        int databaseSizeBeforeUpdate = timbradosRepository.findAll().size();
        timbrados.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimbradosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timbrados.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timbrados))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timbrados in the database
        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimbrados() throws Exception {
        int databaseSizeBeforeUpdate = timbradosRepository.findAll().size();
        timbrados.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimbradosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timbrados))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timbrados in the database
        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimbrados() throws Exception {
        int databaseSizeBeforeUpdate = timbradosRepository.findAll().size();
        timbrados.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimbradosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timbrados)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Timbrados in the database
        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimbradosWithPatch() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        int databaseSizeBeforeUpdate = timbradosRepository.findAll().size();

        // Update the timbrados using partial update
        Timbrados partialUpdatedTimbrados = new Timbrados();
        partialUpdatedTimbrados.setId(timbrados.getId());

        partialUpdatedTimbrados.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);

        restTimbradosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimbrados.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTimbrados))
            )
            .andExpect(status().isOk());

        // Validate the Timbrados in the database
        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeUpdate);
        Timbrados testTimbrados = timbradosList.get(timbradosList.size() - 1);
        assertThat(testTimbrados.getNumeroTimbrado()).isEqualTo(DEFAULT_NUMERO_TIMBRADO);
        assertThat(testTimbrados.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testTimbrados.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void fullUpdateTimbradosWithPatch() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        int databaseSizeBeforeUpdate = timbradosRepository.findAll().size();

        // Update the timbrados using partial update
        Timbrados partialUpdatedTimbrados = new Timbrados();
        partialUpdatedTimbrados.setId(timbrados.getId());

        partialUpdatedTimbrados.numeroTimbrado(UPDATED_NUMERO_TIMBRADO).fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);

        restTimbradosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimbrados.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTimbrados))
            )
            .andExpect(status().isOk());

        // Validate the Timbrados in the database
        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeUpdate);
        Timbrados testTimbrados = timbradosList.get(timbradosList.size() - 1);
        assertThat(testTimbrados.getNumeroTimbrado()).isEqualTo(UPDATED_NUMERO_TIMBRADO);
        assertThat(testTimbrados.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testTimbrados.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingTimbrados() throws Exception {
        int databaseSizeBeforeUpdate = timbradosRepository.findAll().size();
        timbrados.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimbradosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timbrados.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timbrados))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timbrados in the database
        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimbrados() throws Exception {
        int databaseSizeBeforeUpdate = timbradosRepository.findAll().size();
        timbrados.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimbradosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timbrados))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timbrados in the database
        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimbrados() throws Exception {
        int databaseSizeBeforeUpdate = timbradosRepository.findAll().size();
        timbrados.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimbradosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(timbrados))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Timbrados in the database
        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimbrados() throws Exception {
        // Initialize the database
        timbradosRepository.saveAndFlush(timbrados);

        int databaseSizeBeforeDelete = timbradosRepository.findAll().size();

        // Delete the timbrados
        restTimbradosMockMvc
            .perform(delete(ENTITY_API_URL_ID, timbrados.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Timbrados> timbradosList = timbradosRepository.findAll();
        assertThat(timbradosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
