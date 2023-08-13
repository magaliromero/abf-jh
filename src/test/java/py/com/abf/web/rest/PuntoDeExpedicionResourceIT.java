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
import py.com.abf.domain.PuntoDeExpedicion;
import py.com.abf.domain.Sucursales;
import py.com.abf.repository.PuntoDeExpedicionRepository;
import py.com.abf.service.PuntoDeExpedicionService;
import py.com.abf.service.criteria.PuntoDeExpedicionCriteria;

/**
 * Integration tests for the {@link PuntoDeExpedicionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PuntoDeExpedicionResourceIT {

    private static final String DEFAULT_NUMERO_PUNTO_DE_EXPEDICION = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_PUNTO_DE_EXPEDICION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/punto-de-expedicions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PuntoDeExpedicionRepository puntoDeExpedicionRepository;

    @Mock
    private PuntoDeExpedicionRepository puntoDeExpedicionRepositoryMock;

    @Mock
    private PuntoDeExpedicionService puntoDeExpedicionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPuntoDeExpedicionMockMvc;

    private PuntoDeExpedicion puntoDeExpedicion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PuntoDeExpedicion createEntity(EntityManager em) {
        PuntoDeExpedicion puntoDeExpedicion = new PuntoDeExpedicion().numeroPuntoDeExpedicion(DEFAULT_NUMERO_PUNTO_DE_EXPEDICION);
        // Add required entity
        Sucursales sucursales;
        if (TestUtil.findAll(em, Sucursales.class).isEmpty()) {
            sucursales = SucursalesResourceIT.createEntity(em);
            em.persist(sucursales);
            em.flush();
        } else {
            sucursales = TestUtil.findAll(em, Sucursales.class).get(0);
        }
        puntoDeExpedicion.setSucursales(sucursales);
        return puntoDeExpedicion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PuntoDeExpedicion createUpdatedEntity(EntityManager em) {
        PuntoDeExpedicion puntoDeExpedicion = new PuntoDeExpedicion().numeroPuntoDeExpedicion(UPDATED_NUMERO_PUNTO_DE_EXPEDICION);
        // Add required entity
        Sucursales sucursales;
        if (TestUtil.findAll(em, Sucursales.class).isEmpty()) {
            sucursales = SucursalesResourceIT.createUpdatedEntity(em);
            em.persist(sucursales);
            em.flush();
        } else {
            sucursales = TestUtil.findAll(em, Sucursales.class).get(0);
        }
        puntoDeExpedicion.setSucursales(sucursales);
        return puntoDeExpedicion;
    }

    @BeforeEach
    public void initTest() {
        puntoDeExpedicion = createEntity(em);
    }

    @Test
    @Transactional
    void createPuntoDeExpedicion() throws Exception {
        int databaseSizeBeforeCreate = puntoDeExpedicionRepository.findAll().size();
        // Create the PuntoDeExpedicion
        restPuntoDeExpedicionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(puntoDeExpedicion))
            )
            .andExpect(status().isCreated());

        // Validate the PuntoDeExpedicion in the database
        List<PuntoDeExpedicion> puntoDeExpedicionList = puntoDeExpedicionRepository.findAll();
        assertThat(puntoDeExpedicionList).hasSize(databaseSizeBeforeCreate + 1);
        PuntoDeExpedicion testPuntoDeExpedicion = puntoDeExpedicionList.get(puntoDeExpedicionList.size() - 1);
        assertThat(testPuntoDeExpedicion.getNumeroPuntoDeExpedicion()).isEqualTo(DEFAULT_NUMERO_PUNTO_DE_EXPEDICION);
    }

    @Test
    @Transactional
    void createPuntoDeExpedicionWithExistingId() throws Exception {
        // Create the PuntoDeExpedicion with an existing ID
        puntoDeExpedicion.setId(1L);

        int databaseSizeBeforeCreate = puntoDeExpedicionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPuntoDeExpedicionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(puntoDeExpedicion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PuntoDeExpedicion in the database
        List<PuntoDeExpedicion> puntoDeExpedicionList = puntoDeExpedicionRepository.findAll();
        assertThat(puntoDeExpedicionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroPuntoDeExpedicionIsRequired() throws Exception {
        int databaseSizeBeforeTest = puntoDeExpedicionRepository.findAll().size();
        // set the field null
        puntoDeExpedicion.setNumeroPuntoDeExpedicion(null);

        // Create the PuntoDeExpedicion, which fails.

        restPuntoDeExpedicionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(puntoDeExpedicion))
            )
            .andExpect(status().isBadRequest());

        List<PuntoDeExpedicion> puntoDeExpedicionList = puntoDeExpedicionRepository.findAll();
        assertThat(puntoDeExpedicionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPuntoDeExpedicions() throws Exception {
        // Initialize the database
        puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);

        // Get all the puntoDeExpedicionList
        restPuntoDeExpedicionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(puntoDeExpedicion.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroPuntoDeExpedicion").value(hasItem(DEFAULT_NUMERO_PUNTO_DE_EXPEDICION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPuntoDeExpedicionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(puntoDeExpedicionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPuntoDeExpedicionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(puntoDeExpedicionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPuntoDeExpedicionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(puntoDeExpedicionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPuntoDeExpedicionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(puntoDeExpedicionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPuntoDeExpedicion() throws Exception {
        // Initialize the database
        puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);

        // Get the puntoDeExpedicion
        restPuntoDeExpedicionMockMvc
            .perform(get(ENTITY_API_URL_ID, puntoDeExpedicion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(puntoDeExpedicion.getId().intValue()))
            .andExpect(jsonPath("$.numeroPuntoDeExpedicion").value(DEFAULT_NUMERO_PUNTO_DE_EXPEDICION));
    }

    @Test
    @Transactional
    void getPuntoDeExpedicionsByIdFiltering() throws Exception {
        // Initialize the database
        puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);

        Long id = puntoDeExpedicion.getId();

        defaultPuntoDeExpedicionShouldBeFound("id.equals=" + id);
        defaultPuntoDeExpedicionShouldNotBeFound("id.notEquals=" + id);

        defaultPuntoDeExpedicionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPuntoDeExpedicionShouldNotBeFound("id.greaterThan=" + id);

        defaultPuntoDeExpedicionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPuntoDeExpedicionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPuntoDeExpedicionsByNumeroPuntoDeExpedicionIsEqualToSomething() throws Exception {
        // Initialize the database
        puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);

        // Get all the puntoDeExpedicionList where numeroPuntoDeExpedicion equals to DEFAULT_NUMERO_PUNTO_DE_EXPEDICION
        defaultPuntoDeExpedicionShouldBeFound("numeroPuntoDeExpedicion.equals=" + DEFAULT_NUMERO_PUNTO_DE_EXPEDICION);

        // Get all the puntoDeExpedicionList where numeroPuntoDeExpedicion equals to UPDATED_NUMERO_PUNTO_DE_EXPEDICION
        defaultPuntoDeExpedicionShouldNotBeFound("numeroPuntoDeExpedicion.equals=" + UPDATED_NUMERO_PUNTO_DE_EXPEDICION);
    }

    @Test
    @Transactional
    void getAllPuntoDeExpedicionsByNumeroPuntoDeExpedicionIsInShouldWork() throws Exception {
        // Initialize the database
        puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);

        // Get all the puntoDeExpedicionList where numeroPuntoDeExpedicion in DEFAULT_NUMERO_PUNTO_DE_EXPEDICION or UPDATED_NUMERO_PUNTO_DE_EXPEDICION
        defaultPuntoDeExpedicionShouldBeFound(
            "numeroPuntoDeExpedicion.in=" + DEFAULT_NUMERO_PUNTO_DE_EXPEDICION + "," + UPDATED_NUMERO_PUNTO_DE_EXPEDICION
        );

        // Get all the puntoDeExpedicionList where numeroPuntoDeExpedicion equals to UPDATED_NUMERO_PUNTO_DE_EXPEDICION
        defaultPuntoDeExpedicionShouldNotBeFound("numeroPuntoDeExpedicion.in=" + UPDATED_NUMERO_PUNTO_DE_EXPEDICION);
    }

    @Test
    @Transactional
    void getAllPuntoDeExpedicionsByNumeroPuntoDeExpedicionIsNullOrNotNull() throws Exception {
        // Initialize the database
        puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);

        // Get all the puntoDeExpedicionList where numeroPuntoDeExpedicion is not null
        defaultPuntoDeExpedicionShouldBeFound("numeroPuntoDeExpedicion.specified=true");

        // Get all the puntoDeExpedicionList where numeroPuntoDeExpedicion is null
        defaultPuntoDeExpedicionShouldNotBeFound("numeroPuntoDeExpedicion.specified=false");
    }

    @Test
    @Transactional
    void getAllPuntoDeExpedicionsByNumeroPuntoDeExpedicionContainsSomething() throws Exception {
        // Initialize the database
        puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);

        // Get all the puntoDeExpedicionList where numeroPuntoDeExpedicion contains DEFAULT_NUMERO_PUNTO_DE_EXPEDICION
        defaultPuntoDeExpedicionShouldBeFound("numeroPuntoDeExpedicion.contains=" + DEFAULT_NUMERO_PUNTO_DE_EXPEDICION);

        // Get all the puntoDeExpedicionList where numeroPuntoDeExpedicion contains UPDATED_NUMERO_PUNTO_DE_EXPEDICION
        defaultPuntoDeExpedicionShouldNotBeFound("numeroPuntoDeExpedicion.contains=" + UPDATED_NUMERO_PUNTO_DE_EXPEDICION);
    }

    @Test
    @Transactional
    void getAllPuntoDeExpedicionsByNumeroPuntoDeExpedicionNotContainsSomething() throws Exception {
        // Initialize the database
        puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);

        // Get all the puntoDeExpedicionList where numeroPuntoDeExpedicion does not contain DEFAULT_NUMERO_PUNTO_DE_EXPEDICION
        defaultPuntoDeExpedicionShouldNotBeFound("numeroPuntoDeExpedicion.doesNotContain=" + DEFAULT_NUMERO_PUNTO_DE_EXPEDICION);

        // Get all the puntoDeExpedicionList where numeroPuntoDeExpedicion does not contain UPDATED_NUMERO_PUNTO_DE_EXPEDICION
        defaultPuntoDeExpedicionShouldBeFound("numeroPuntoDeExpedicion.doesNotContain=" + UPDATED_NUMERO_PUNTO_DE_EXPEDICION);
    }

    @Test
    @Transactional
    void getAllPuntoDeExpedicionsBySucursalesIsEqualToSomething() throws Exception {
        Sucursales sucursales;
        if (TestUtil.findAll(em, Sucursales.class).isEmpty()) {
            puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);
            sucursales = SucursalesResourceIT.createEntity(em);
        } else {
            sucursales = TestUtil.findAll(em, Sucursales.class).get(0);
        }
        em.persist(sucursales);
        em.flush();
        puntoDeExpedicion.setSucursales(sucursales);
        puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);
        Long sucursalesId = sucursales.getId();

        // Get all the puntoDeExpedicionList where sucursales equals to sucursalesId
        defaultPuntoDeExpedicionShouldBeFound("sucursalesId.equals=" + sucursalesId);

        // Get all the puntoDeExpedicionList where sucursales equals to (sucursalesId + 1)
        defaultPuntoDeExpedicionShouldNotBeFound("sucursalesId.equals=" + (sucursalesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPuntoDeExpedicionShouldBeFound(String filter) throws Exception {
        restPuntoDeExpedicionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(puntoDeExpedicion.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroPuntoDeExpedicion").value(hasItem(DEFAULT_NUMERO_PUNTO_DE_EXPEDICION)));

        // Check, that the count call also returns 1
        restPuntoDeExpedicionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPuntoDeExpedicionShouldNotBeFound(String filter) throws Exception {
        restPuntoDeExpedicionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPuntoDeExpedicionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPuntoDeExpedicion() throws Exception {
        // Get the puntoDeExpedicion
        restPuntoDeExpedicionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPuntoDeExpedicion() throws Exception {
        // Initialize the database
        puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);

        int databaseSizeBeforeUpdate = puntoDeExpedicionRepository.findAll().size();

        // Update the puntoDeExpedicion
        PuntoDeExpedicion updatedPuntoDeExpedicion = puntoDeExpedicionRepository.findById(puntoDeExpedicion.getId()).get();
        // Disconnect from session so that the updates on updatedPuntoDeExpedicion are not directly saved in db
        em.detach(updatedPuntoDeExpedicion);
        updatedPuntoDeExpedicion.numeroPuntoDeExpedicion(UPDATED_NUMERO_PUNTO_DE_EXPEDICION);

        restPuntoDeExpedicionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPuntoDeExpedicion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPuntoDeExpedicion))
            )
            .andExpect(status().isOk());

        // Validate the PuntoDeExpedicion in the database
        List<PuntoDeExpedicion> puntoDeExpedicionList = puntoDeExpedicionRepository.findAll();
        assertThat(puntoDeExpedicionList).hasSize(databaseSizeBeforeUpdate);
        PuntoDeExpedicion testPuntoDeExpedicion = puntoDeExpedicionList.get(puntoDeExpedicionList.size() - 1);
        assertThat(testPuntoDeExpedicion.getNumeroPuntoDeExpedicion()).isEqualTo(UPDATED_NUMERO_PUNTO_DE_EXPEDICION);
    }

    @Test
    @Transactional
    void putNonExistingPuntoDeExpedicion() throws Exception {
        int databaseSizeBeforeUpdate = puntoDeExpedicionRepository.findAll().size();
        puntoDeExpedicion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPuntoDeExpedicionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, puntoDeExpedicion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(puntoDeExpedicion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PuntoDeExpedicion in the database
        List<PuntoDeExpedicion> puntoDeExpedicionList = puntoDeExpedicionRepository.findAll();
        assertThat(puntoDeExpedicionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPuntoDeExpedicion() throws Exception {
        int databaseSizeBeforeUpdate = puntoDeExpedicionRepository.findAll().size();
        puntoDeExpedicion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPuntoDeExpedicionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(puntoDeExpedicion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PuntoDeExpedicion in the database
        List<PuntoDeExpedicion> puntoDeExpedicionList = puntoDeExpedicionRepository.findAll();
        assertThat(puntoDeExpedicionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPuntoDeExpedicion() throws Exception {
        int databaseSizeBeforeUpdate = puntoDeExpedicionRepository.findAll().size();
        puntoDeExpedicion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPuntoDeExpedicionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(puntoDeExpedicion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PuntoDeExpedicion in the database
        List<PuntoDeExpedicion> puntoDeExpedicionList = puntoDeExpedicionRepository.findAll();
        assertThat(puntoDeExpedicionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePuntoDeExpedicionWithPatch() throws Exception {
        // Initialize the database
        puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);

        int databaseSizeBeforeUpdate = puntoDeExpedicionRepository.findAll().size();

        // Update the puntoDeExpedicion using partial update
        PuntoDeExpedicion partialUpdatedPuntoDeExpedicion = new PuntoDeExpedicion();
        partialUpdatedPuntoDeExpedicion.setId(puntoDeExpedicion.getId());

        restPuntoDeExpedicionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPuntoDeExpedicion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPuntoDeExpedicion))
            )
            .andExpect(status().isOk());

        // Validate the PuntoDeExpedicion in the database
        List<PuntoDeExpedicion> puntoDeExpedicionList = puntoDeExpedicionRepository.findAll();
        assertThat(puntoDeExpedicionList).hasSize(databaseSizeBeforeUpdate);
        PuntoDeExpedicion testPuntoDeExpedicion = puntoDeExpedicionList.get(puntoDeExpedicionList.size() - 1);
        assertThat(testPuntoDeExpedicion.getNumeroPuntoDeExpedicion()).isEqualTo(DEFAULT_NUMERO_PUNTO_DE_EXPEDICION);
    }

    @Test
    @Transactional
    void fullUpdatePuntoDeExpedicionWithPatch() throws Exception {
        // Initialize the database
        puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);

        int databaseSizeBeforeUpdate = puntoDeExpedicionRepository.findAll().size();

        // Update the puntoDeExpedicion using partial update
        PuntoDeExpedicion partialUpdatedPuntoDeExpedicion = new PuntoDeExpedicion();
        partialUpdatedPuntoDeExpedicion.setId(puntoDeExpedicion.getId());

        partialUpdatedPuntoDeExpedicion.numeroPuntoDeExpedicion(UPDATED_NUMERO_PUNTO_DE_EXPEDICION);

        restPuntoDeExpedicionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPuntoDeExpedicion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPuntoDeExpedicion))
            )
            .andExpect(status().isOk());

        // Validate the PuntoDeExpedicion in the database
        List<PuntoDeExpedicion> puntoDeExpedicionList = puntoDeExpedicionRepository.findAll();
        assertThat(puntoDeExpedicionList).hasSize(databaseSizeBeforeUpdate);
        PuntoDeExpedicion testPuntoDeExpedicion = puntoDeExpedicionList.get(puntoDeExpedicionList.size() - 1);
        assertThat(testPuntoDeExpedicion.getNumeroPuntoDeExpedicion()).isEqualTo(UPDATED_NUMERO_PUNTO_DE_EXPEDICION);
    }

    @Test
    @Transactional
    void patchNonExistingPuntoDeExpedicion() throws Exception {
        int databaseSizeBeforeUpdate = puntoDeExpedicionRepository.findAll().size();
        puntoDeExpedicion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPuntoDeExpedicionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, puntoDeExpedicion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(puntoDeExpedicion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PuntoDeExpedicion in the database
        List<PuntoDeExpedicion> puntoDeExpedicionList = puntoDeExpedicionRepository.findAll();
        assertThat(puntoDeExpedicionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPuntoDeExpedicion() throws Exception {
        int databaseSizeBeforeUpdate = puntoDeExpedicionRepository.findAll().size();
        puntoDeExpedicion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPuntoDeExpedicionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(puntoDeExpedicion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PuntoDeExpedicion in the database
        List<PuntoDeExpedicion> puntoDeExpedicionList = puntoDeExpedicionRepository.findAll();
        assertThat(puntoDeExpedicionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPuntoDeExpedicion() throws Exception {
        int databaseSizeBeforeUpdate = puntoDeExpedicionRepository.findAll().size();
        puntoDeExpedicion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPuntoDeExpedicionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(puntoDeExpedicion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PuntoDeExpedicion in the database
        List<PuntoDeExpedicion> puntoDeExpedicionList = puntoDeExpedicionRepository.findAll();
        assertThat(puntoDeExpedicionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePuntoDeExpedicion() throws Exception {
        // Initialize the database
        puntoDeExpedicionRepository.saveAndFlush(puntoDeExpedicion);

        int databaseSizeBeforeDelete = puntoDeExpedicionRepository.findAll().size();

        // Delete the puntoDeExpedicion
        restPuntoDeExpedicionMockMvc
            .perform(delete(ENTITY_API_URL_ID, puntoDeExpedicion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PuntoDeExpedicion> puntoDeExpedicionList = puntoDeExpedicionRepository.findAll();
        assertThat(puntoDeExpedicionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
