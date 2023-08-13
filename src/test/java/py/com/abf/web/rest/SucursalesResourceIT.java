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
import py.com.abf.domain.PuntoDeExpedicion;
import py.com.abf.domain.Sucursales;
import py.com.abf.domain.Timbrados;
import py.com.abf.repository.SucursalesRepository;
import py.com.abf.service.criteria.SucursalesCriteria;

/**
 * Integration tests for the {@link SucursalesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SucursalesResourceIT {

    private static final String DEFAULT_NOMBRE_SUCURSAL = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_SUCURSAL = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_ESTABLECIMIENTO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_ESTABLECIMIENTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sucursales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SucursalesRepository sucursalesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSucursalesMockMvc;

    private Sucursales sucursales;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sucursales createEntity(EntityManager em) {
        Sucursales sucursales = new Sucursales()
            .nombreSucursal(DEFAULT_NOMBRE_SUCURSAL)
            .direccion(DEFAULT_DIRECCION)
            .numeroEstablecimiento(DEFAULT_NUMERO_ESTABLECIMIENTO);
        return sucursales;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sucursales createUpdatedEntity(EntityManager em) {
        Sucursales sucursales = new Sucursales()
            .nombreSucursal(UPDATED_NOMBRE_SUCURSAL)
            .direccion(UPDATED_DIRECCION)
            .numeroEstablecimiento(UPDATED_NUMERO_ESTABLECIMIENTO);
        return sucursales;
    }

    @BeforeEach
    public void initTest() {
        sucursales = createEntity(em);
    }

    @Test
    @Transactional
    void createSucursales() throws Exception {
        int databaseSizeBeforeCreate = sucursalesRepository.findAll().size();
        // Create the Sucursales
        restSucursalesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sucursales)))
            .andExpect(status().isCreated());

        // Validate the Sucursales in the database
        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeCreate + 1);
        Sucursales testSucursales = sucursalesList.get(sucursalesList.size() - 1);
        assertThat(testSucursales.getNombreSucursal()).isEqualTo(DEFAULT_NOMBRE_SUCURSAL);
        assertThat(testSucursales.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testSucursales.getNumeroEstablecimiento()).isEqualTo(DEFAULT_NUMERO_ESTABLECIMIENTO);
    }

    @Test
    @Transactional
    void createSucursalesWithExistingId() throws Exception {
        // Create the Sucursales with an existing ID
        sucursales.setId(1L);

        int databaseSizeBeforeCreate = sucursalesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSucursalesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sucursales)))
            .andExpect(status().isBadRequest());

        // Validate the Sucursales in the database
        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreSucursalIsRequired() throws Exception {
        int databaseSizeBeforeTest = sucursalesRepository.findAll().size();
        // set the field null
        sucursales.setNombreSucursal(null);

        // Create the Sucursales, which fails.

        restSucursalesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sucursales)))
            .andExpect(status().isBadRequest());

        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroEstablecimientoIsRequired() throws Exception {
        int databaseSizeBeforeTest = sucursalesRepository.findAll().size();
        // set the field null
        sucursales.setNumeroEstablecimiento(null);

        // Create the Sucursales, which fails.

        restSucursalesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sucursales)))
            .andExpect(status().isBadRequest());

        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSucursales() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList
        restSucursalesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sucursales.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreSucursal").value(hasItem(DEFAULT_NOMBRE_SUCURSAL)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].numeroEstablecimiento").value(hasItem(DEFAULT_NUMERO_ESTABLECIMIENTO)));
    }

    @Test
    @Transactional
    void getSucursales() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get the sucursales
        restSucursalesMockMvc
            .perform(get(ENTITY_API_URL_ID, sucursales.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sucursales.getId().intValue()))
            .andExpect(jsonPath("$.nombreSucursal").value(DEFAULT_NOMBRE_SUCURSAL))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.numeroEstablecimiento").value(DEFAULT_NUMERO_ESTABLECIMIENTO));
    }

    @Test
    @Transactional
    void getSucursalesByIdFiltering() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        Long id = sucursales.getId();

        defaultSucursalesShouldBeFound("id.equals=" + id);
        defaultSucursalesShouldNotBeFound("id.notEquals=" + id);

        defaultSucursalesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSucursalesShouldNotBeFound("id.greaterThan=" + id);

        defaultSucursalesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSucursalesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSucursalesByNombreSucursalIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where nombreSucursal equals to DEFAULT_NOMBRE_SUCURSAL
        defaultSucursalesShouldBeFound("nombreSucursal.equals=" + DEFAULT_NOMBRE_SUCURSAL);

        // Get all the sucursalesList where nombreSucursal equals to UPDATED_NOMBRE_SUCURSAL
        defaultSucursalesShouldNotBeFound("nombreSucursal.equals=" + UPDATED_NOMBRE_SUCURSAL);
    }

    @Test
    @Transactional
    void getAllSucursalesByNombreSucursalIsInShouldWork() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where nombreSucursal in DEFAULT_NOMBRE_SUCURSAL or UPDATED_NOMBRE_SUCURSAL
        defaultSucursalesShouldBeFound("nombreSucursal.in=" + DEFAULT_NOMBRE_SUCURSAL + "," + UPDATED_NOMBRE_SUCURSAL);

        // Get all the sucursalesList where nombreSucursal equals to UPDATED_NOMBRE_SUCURSAL
        defaultSucursalesShouldNotBeFound("nombreSucursal.in=" + UPDATED_NOMBRE_SUCURSAL);
    }

    @Test
    @Transactional
    void getAllSucursalesByNombreSucursalIsNullOrNotNull() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where nombreSucursal is not null
        defaultSucursalesShouldBeFound("nombreSucursal.specified=true");

        // Get all the sucursalesList where nombreSucursal is null
        defaultSucursalesShouldNotBeFound("nombreSucursal.specified=false");
    }

    @Test
    @Transactional
    void getAllSucursalesByNombreSucursalContainsSomething() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where nombreSucursal contains DEFAULT_NOMBRE_SUCURSAL
        defaultSucursalesShouldBeFound("nombreSucursal.contains=" + DEFAULT_NOMBRE_SUCURSAL);

        // Get all the sucursalesList where nombreSucursal contains UPDATED_NOMBRE_SUCURSAL
        defaultSucursalesShouldNotBeFound("nombreSucursal.contains=" + UPDATED_NOMBRE_SUCURSAL);
    }

    @Test
    @Transactional
    void getAllSucursalesByNombreSucursalNotContainsSomething() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where nombreSucursal does not contain DEFAULT_NOMBRE_SUCURSAL
        defaultSucursalesShouldNotBeFound("nombreSucursal.doesNotContain=" + DEFAULT_NOMBRE_SUCURSAL);

        // Get all the sucursalesList where nombreSucursal does not contain UPDATED_NOMBRE_SUCURSAL
        defaultSucursalesShouldBeFound("nombreSucursal.doesNotContain=" + UPDATED_NOMBRE_SUCURSAL);
    }

    @Test
    @Transactional
    void getAllSucursalesByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where direccion equals to DEFAULT_DIRECCION
        defaultSucursalesShouldBeFound("direccion.equals=" + DEFAULT_DIRECCION);

        // Get all the sucursalesList where direccion equals to UPDATED_DIRECCION
        defaultSucursalesShouldNotBeFound("direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllSucursalesByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where direccion in DEFAULT_DIRECCION or UPDATED_DIRECCION
        defaultSucursalesShouldBeFound("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION);

        // Get all the sucursalesList where direccion equals to UPDATED_DIRECCION
        defaultSucursalesShouldNotBeFound("direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllSucursalesByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where direccion is not null
        defaultSucursalesShouldBeFound("direccion.specified=true");

        // Get all the sucursalesList where direccion is null
        defaultSucursalesShouldNotBeFound("direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllSucursalesByDireccionContainsSomething() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where direccion contains DEFAULT_DIRECCION
        defaultSucursalesShouldBeFound("direccion.contains=" + DEFAULT_DIRECCION);

        // Get all the sucursalesList where direccion contains UPDATED_DIRECCION
        defaultSucursalesShouldNotBeFound("direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllSucursalesByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where direccion does not contain DEFAULT_DIRECCION
        defaultSucursalesShouldNotBeFound("direccion.doesNotContain=" + DEFAULT_DIRECCION);

        // Get all the sucursalesList where direccion does not contain UPDATED_DIRECCION
        defaultSucursalesShouldBeFound("direccion.doesNotContain=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllSucursalesByNumeroEstablecimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where numeroEstablecimiento equals to DEFAULT_NUMERO_ESTABLECIMIENTO
        defaultSucursalesShouldBeFound("numeroEstablecimiento.equals=" + DEFAULT_NUMERO_ESTABLECIMIENTO);

        // Get all the sucursalesList where numeroEstablecimiento equals to UPDATED_NUMERO_ESTABLECIMIENTO
        defaultSucursalesShouldNotBeFound("numeroEstablecimiento.equals=" + UPDATED_NUMERO_ESTABLECIMIENTO);
    }

    @Test
    @Transactional
    void getAllSucursalesByNumeroEstablecimientoIsInShouldWork() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where numeroEstablecimiento in DEFAULT_NUMERO_ESTABLECIMIENTO or UPDATED_NUMERO_ESTABLECIMIENTO
        defaultSucursalesShouldBeFound("numeroEstablecimiento.in=" + DEFAULT_NUMERO_ESTABLECIMIENTO + "," + UPDATED_NUMERO_ESTABLECIMIENTO);

        // Get all the sucursalesList where numeroEstablecimiento equals to UPDATED_NUMERO_ESTABLECIMIENTO
        defaultSucursalesShouldNotBeFound("numeroEstablecimiento.in=" + UPDATED_NUMERO_ESTABLECIMIENTO);
    }

    @Test
    @Transactional
    void getAllSucursalesByNumeroEstablecimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where numeroEstablecimiento is not null
        defaultSucursalesShouldBeFound("numeroEstablecimiento.specified=true");

        // Get all the sucursalesList where numeroEstablecimiento is null
        defaultSucursalesShouldNotBeFound("numeroEstablecimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllSucursalesByNumeroEstablecimientoContainsSomething() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where numeroEstablecimiento contains DEFAULT_NUMERO_ESTABLECIMIENTO
        defaultSucursalesShouldBeFound("numeroEstablecimiento.contains=" + DEFAULT_NUMERO_ESTABLECIMIENTO);

        // Get all the sucursalesList where numeroEstablecimiento contains UPDATED_NUMERO_ESTABLECIMIENTO
        defaultSucursalesShouldNotBeFound("numeroEstablecimiento.contains=" + UPDATED_NUMERO_ESTABLECIMIENTO);
    }

    @Test
    @Transactional
    void getAllSucursalesByNumeroEstablecimientoNotContainsSomething() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        // Get all the sucursalesList where numeroEstablecimiento does not contain DEFAULT_NUMERO_ESTABLECIMIENTO
        defaultSucursalesShouldNotBeFound("numeroEstablecimiento.doesNotContain=" + DEFAULT_NUMERO_ESTABLECIMIENTO);

        // Get all the sucursalesList where numeroEstablecimiento does not contain UPDATED_NUMERO_ESTABLECIMIENTO
        defaultSucursalesShouldBeFound("numeroEstablecimiento.doesNotContain=" + UPDATED_NUMERO_ESTABLECIMIENTO);
    }

    @Test
    @Transactional
    void getAllSucursalesByPuntoDeExpedicionIsEqualToSomething() throws Exception {
        PuntoDeExpedicion puntoDeExpedicion;
        if (TestUtil.findAll(em, PuntoDeExpedicion.class).isEmpty()) {
            sucursalesRepository.saveAndFlush(sucursales);
            puntoDeExpedicion = PuntoDeExpedicionResourceIT.createEntity(em);
        } else {
            puntoDeExpedicion = TestUtil.findAll(em, PuntoDeExpedicion.class).get(0);
        }
        em.persist(puntoDeExpedicion);
        em.flush();
        sucursales.addPuntoDeExpedicion(puntoDeExpedicion);
        sucursalesRepository.saveAndFlush(sucursales);
        Long puntoDeExpedicionId = puntoDeExpedicion.getId();

        // Get all the sucursalesList where puntoDeExpedicion equals to puntoDeExpedicionId
        defaultSucursalesShouldBeFound("puntoDeExpedicionId.equals=" + puntoDeExpedicionId);

        // Get all the sucursalesList where puntoDeExpedicion equals to (puntoDeExpedicionId + 1)
        defaultSucursalesShouldNotBeFound("puntoDeExpedicionId.equals=" + (puntoDeExpedicionId + 1));
    }

    @Test
    @Transactional
    void getAllSucursalesByTimbradosIsEqualToSomething() throws Exception {
        Timbrados timbrados;
        if (TestUtil.findAll(em, Timbrados.class).isEmpty()) {
            sucursalesRepository.saveAndFlush(sucursales);
            timbrados = TimbradosResourceIT.createEntity(em);
        } else {
            timbrados = TestUtil.findAll(em, Timbrados.class).get(0);
        }
        em.persist(timbrados);
        em.flush();
        sucursales.setTimbrados(timbrados);
        timbrados.setSucursales(sucursales);
        sucursalesRepository.saveAndFlush(sucursales);
        Long timbradosId = timbrados.getId();

        // Get all the sucursalesList where timbrados equals to timbradosId
        defaultSucursalesShouldBeFound("timbradosId.equals=" + timbradosId);

        // Get all the sucursalesList where timbrados equals to (timbradosId + 1)
        defaultSucursalesShouldNotBeFound("timbradosId.equals=" + (timbradosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSucursalesShouldBeFound(String filter) throws Exception {
        restSucursalesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sucursales.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreSucursal").value(hasItem(DEFAULT_NOMBRE_SUCURSAL)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].numeroEstablecimiento").value(hasItem(DEFAULT_NUMERO_ESTABLECIMIENTO)));

        // Check, that the count call also returns 1
        restSucursalesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSucursalesShouldNotBeFound(String filter) throws Exception {
        restSucursalesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSucursalesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSucursales() throws Exception {
        // Get the sucursales
        restSucursalesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSucursales() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        int databaseSizeBeforeUpdate = sucursalesRepository.findAll().size();

        // Update the sucursales
        Sucursales updatedSucursales = sucursalesRepository.findById(sucursales.getId()).get();
        // Disconnect from session so that the updates on updatedSucursales are not directly saved in db
        em.detach(updatedSucursales);
        updatedSucursales
            .nombreSucursal(UPDATED_NOMBRE_SUCURSAL)
            .direccion(UPDATED_DIRECCION)
            .numeroEstablecimiento(UPDATED_NUMERO_ESTABLECIMIENTO);

        restSucursalesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSucursales.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSucursales))
            )
            .andExpect(status().isOk());

        // Validate the Sucursales in the database
        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeUpdate);
        Sucursales testSucursales = sucursalesList.get(sucursalesList.size() - 1);
        assertThat(testSucursales.getNombreSucursal()).isEqualTo(UPDATED_NOMBRE_SUCURSAL);
        assertThat(testSucursales.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testSucursales.getNumeroEstablecimiento()).isEqualTo(UPDATED_NUMERO_ESTABLECIMIENTO);
    }

    @Test
    @Transactional
    void putNonExistingSucursales() throws Exception {
        int databaseSizeBeforeUpdate = sucursalesRepository.findAll().size();
        sucursales.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSucursalesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sucursales.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sucursales))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sucursales in the database
        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSucursales() throws Exception {
        int databaseSizeBeforeUpdate = sucursalesRepository.findAll().size();
        sucursales.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSucursalesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sucursales))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sucursales in the database
        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSucursales() throws Exception {
        int databaseSizeBeforeUpdate = sucursalesRepository.findAll().size();
        sucursales.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSucursalesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sucursales)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sucursales in the database
        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSucursalesWithPatch() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        int databaseSizeBeforeUpdate = sucursalesRepository.findAll().size();

        // Update the sucursales using partial update
        Sucursales partialUpdatedSucursales = new Sucursales();
        partialUpdatedSucursales.setId(sucursales.getId());

        restSucursalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSucursales.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSucursales))
            )
            .andExpect(status().isOk());

        // Validate the Sucursales in the database
        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeUpdate);
        Sucursales testSucursales = sucursalesList.get(sucursalesList.size() - 1);
        assertThat(testSucursales.getNombreSucursal()).isEqualTo(DEFAULT_NOMBRE_SUCURSAL);
        assertThat(testSucursales.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testSucursales.getNumeroEstablecimiento()).isEqualTo(DEFAULT_NUMERO_ESTABLECIMIENTO);
    }

    @Test
    @Transactional
    void fullUpdateSucursalesWithPatch() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        int databaseSizeBeforeUpdate = sucursalesRepository.findAll().size();

        // Update the sucursales using partial update
        Sucursales partialUpdatedSucursales = new Sucursales();
        partialUpdatedSucursales.setId(sucursales.getId());

        partialUpdatedSucursales
            .nombreSucursal(UPDATED_NOMBRE_SUCURSAL)
            .direccion(UPDATED_DIRECCION)
            .numeroEstablecimiento(UPDATED_NUMERO_ESTABLECIMIENTO);

        restSucursalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSucursales.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSucursales))
            )
            .andExpect(status().isOk());

        // Validate the Sucursales in the database
        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeUpdate);
        Sucursales testSucursales = sucursalesList.get(sucursalesList.size() - 1);
        assertThat(testSucursales.getNombreSucursal()).isEqualTo(UPDATED_NOMBRE_SUCURSAL);
        assertThat(testSucursales.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testSucursales.getNumeroEstablecimiento()).isEqualTo(UPDATED_NUMERO_ESTABLECIMIENTO);
    }

    @Test
    @Transactional
    void patchNonExistingSucursales() throws Exception {
        int databaseSizeBeforeUpdate = sucursalesRepository.findAll().size();
        sucursales.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSucursalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sucursales.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sucursales))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sucursales in the database
        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSucursales() throws Exception {
        int databaseSizeBeforeUpdate = sucursalesRepository.findAll().size();
        sucursales.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSucursalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sucursales))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sucursales in the database
        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSucursales() throws Exception {
        int databaseSizeBeforeUpdate = sucursalesRepository.findAll().size();
        sucursales.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSucursalesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sucursales))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sucursales in the database
        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSucursales() throws Exception {
        // Initialize the database
        sucursalesRepository.saveAndFlush(sucursales);

        int databaseSizeBeforeDelete = sucursalesRepository.findAll().size();

        // Delete the sucursales
        restSucursalesMockMvc
            .perform(delete(ENTITY_API_URL_ID, sucursales.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sucursales> sucursalesList = sucursalesRepository.findAll();
        assertThat(sucursalesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
