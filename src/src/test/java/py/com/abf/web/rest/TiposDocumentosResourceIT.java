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
import py.com.abf.domain.Alumnos;
import py.com.abf.domain.Funcionarios;
import py.com.abf.domain.TiposDocumentos;
import py.com.abf.repository.TiposDocumentosRepository;
import py.com.abf.service.criteria.TiposDocumentosCriteria;

/**
 * Integration tests for the {@link TiposDocumentosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TiposDocumentosResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipos-documentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TiposDocumentosRepository tiposDocumentosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTiposDocumentosMockMvc;

    private TiposDocumentos tiposDocumentos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TiposDocumentos createEntity(EntityManager em) {
        TiposDocumentos tiposDocumentos = new TiposDocumentos().codigo(DEFAULT_CODIGO).descripcion(DEFAULT_DESCRIPCION);
        return tiposDocumentos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TiposDocumentos createUpdatedEntity(EntityManager em) {
        TiposDocumentos tiposDocumentos = new TiposDocumentos().codigo(UPDATED_CODIGO).descripcion(UPDATED_DESCRIPCION);
        return tiposDocumentos;
    }

    @BeforeEach
    public void initTest() {
        tiposDocumentos = createEntity(em);
    }

    @Test
    @Transactional
    void createTiposDocumentos() throws Exception {
        int databaseSizeBeforeCreate = tiposDocumentosRepository.findAll().size();
        // Create the TiposDocumentos
        restTiposDocumentosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tiposDocumentos))
            )
            .andExpect(status().isCreated());

        // Validate the TiposDocumentos in the database
        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeCreate + 1);
        TiposDocumentos testTiposDocumentos = tiposDocumentosList.get(tiposDocumentosList.size() - 1);
        assertThat(testTiposDocumentos.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testTiposDocumentos.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createTiposDocumentosWithExistingId() throws Exception {
        // Create the TiposDocumentos with an existing ID
        tiposDocumentos.setId(1L);

        int databaseSizeBeforeCreate = tiposDocumentosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTiposDocumentosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tiposDocumentos))
            )
            .andExpect(status().isBadRequest());

        // Validate the TiposDocumentos in the database
        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tiposDocumentosRepository.findAll().size();
        // set the field null
        tiposDocumentos.setCodigo(null);

        // Create the TiposDocumentos, which fails.

        restTiposDocumentosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tiposDocumentos))
            )
            .andExpect(status().isBadRequest());

        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = tiposDocumentosRepository.findAll().size();
        // set the field null
        tiposDocumentos.setDescripcion(null);

        // Create the TiposDocumentos, which fails.

        restTiposDocumentosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tiposDocumentos))
            )
            .andExpect(status().isBadRequest());

        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTiposDocumentos() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        // Get all the tiposDocumentosList
        restTiposDocumentosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tiposDocumentos.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getTiposDocumentos() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        // Get the tiposDocumentos
        restTiposDocumentosMockMvc
            .perform(get(ENTITY_API_URL_ID, tiposDocumentos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tiposDocumentos.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getTiposDocumentosByIdFiltering() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        Long id = tiposDocumentos.getId();

        defaultTiposDocumentosShouldBeFound("id.equals=" + id);
        defaultTiposDocumentosShouldNotBeFound("id.notEquals=" + id);

        defaultTiposDocumentosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTiposDocumentosShouldNotBeFound("id.greaterThan=" + id);

        defaultTiposDocumentosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTiposDocumentosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTiposDocumentosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        // Get all the tiposDocumentosList where codigo equals to DEFAULT_CODIGO
        defaultTiposDocumentosShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the tiposDocumentosList where codigo equals to UPDATED_CODIGO
        defaultTiposDocumentosShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTiposDocumentosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        // Get all the tiposDocumentosList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultTiposDocumentosShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the tiposDocumentosList where codigo equals to UPDATED_CODIGO
        defaultTiposDocumentosShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTiposDocumentosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        // Get all the tiposDocumentosList where codigo is not null
        defaultTiposDocumentosShouldBeFound("codigo.specified=true");

        // Get all the tiposDocumentosList where codigo is null
        defaultTiposDocumentosShouldNotBeFound("codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllTiposDocumentosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        // Get all the tiposDocumentosList where codigo contains DEFAULT_CODIGO
        defaultTiposDocumentosShouldBeFound("codigo.contains=" + DEFAULT_CODIGO);

        // Get all the tiposDocumentosList where codigo contains UPDATED_CODIGO
        defaultTiposDocumentosShouldNotBeFound("codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTiposDocumentosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        // Get all the tiposDocumentosList where codigo does not contain DEFAULT_CODIGO
        defaultTiposDocumentosShouldNotBeFound("codigo.doesNotContain=" + DEFAULT_CODIGO);

        // Get all the tiposDocumentosList where codigo does not contain UPDATED_CODIGO
        defaultTiposDocumentosShouldBeFound("codigo.doesNotContain=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTiposDocumentosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        // Get all the tiposDocumentosList where descripcion equals to DEFAULT_DESCRIPCION
        defaultTiposDocumentosShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the tiposDocumentosList where descripcion equals to UPDATED_DESCRIPCION
        defaultTiposDocumentosShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTiposDocumentosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        // Get all the tiposDocumentosList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultTiposDocumentosShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the tiposDocumentosList where descripcion equals to UPDATED_DESCRIPCION
        defaultTiposDocumentosShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTiposDocumentosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        // Get all the tiposDocumentosList where descripcion is not null
        defaultTiposDocumentosShouldBeFound("descripcion.specified=true");

        // Get all the tiposDocumentosList where descripcion is null
        defaultTiposDocumentosShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllTiposDocumentosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        // Get all the tiposDocumentosList where descripcion contains DEFAULT_DESCRIPCION
        defaultTiposDocumentosShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the tiposDocumentosList where descripcion contains UPDATED_DESCRIPCION
        defaultTiposDocumentosShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTiposDocumentosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        // Get all the tiposDocumentosList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultTiposDocumentosShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the tiposDocumentosList where descripcion does not contain UPDATED_DESCRIPCION
        defaultTiposDocumentosShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTiposDocumentosByAlumnosIsEqualToSomething() throws Exception {
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            tiposDocumentosRepository.saveAndFlush(tiposDocumentos);
            alumnos = AlumnosResourceIT.createEntity(em);
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        em.persist(alumnos);
        em.flush();
        tiposDocumentos.addAlumnos(alumnos);
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);
        Long alumnosId = alumnos.getId();

        // Get all the tiposDocumentosList where alumnos equals to alumnosId
        defaultTiposDocumentosShouldBeFound("alumnosId.equals=" + alumnosId);

        // Get all the tiposDocumentosList where alumnos equals to (alumnosId + 1)
        defaultTiposDocumentosShouldNotBeFound("alumnosId.equals=" + (alumnosId + 1));
    }

    @Test
    @Transactional
    void getAllTiposDocumentosByFuncionariosIsEqualToSomething() throws Exception {
        Funcionarios funcionarios;
        if (TestUtil.findAll(em, Funcionarios.class).isEmpty()) {
            tiposDocumentosRepository.saveAndFlush(tiposDocumentos);
            funcionarios = FuncionariosResourceIT.createEntity(em);
        } else {
            funcionarios = TestUtil.findAll(em, Funcionarios.class).get(0);
        }
        em.persist(funcionarios);
        em.flush();
        tiposDocumentos.addFuncionarios(funcionarios);
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);
        Long funcionariosId = funcionarios.getId();

        // Get all the tiposDocumentosList where funcionarios equals to funcionariosId
        defaultTiposDocumentosShouldBeFound("funcionariosId.equals=" + funcionariosId);

        // Get all the tiposDocumentosList where funcionarios equals to (funcionariosId + 1)
        defaultTiposDocumentosShouldNotBeFound("funcionariosId.equals=" + (funcionariosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTiposDocumentosShouldBeFound(String filter) throws Exception {
        restTiposDocumentosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tiposDocumentos.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restTiposDocumentosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTiposDocumentosShouldNotBeFound(String filter) throws Exception {
        restTiposDocumentosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTiposDocumentosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTiposDocumentos() throws Exception {
        // Get the tiposDocumentos
        restTiposDocumentosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTiposDocumentos() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        int databaseSizeBeforeUpdate = tiposDocumentosRepository.findAll().size();

        // Update the tiposDocumentos
        TiposDocumentos updatedTiposDocumentos = tiposDocumentosRepository.findById(tiposDocumentos.getId()).get();
        // Disconnect from session so that the updates on updatedTiposDocumentos are not directly saved in db
        em.detach(updatedTiposDocumentos);
        updatedTiposDocumentos.codigo(UPDATED_CODIGO).descripcion(UPDATED_DESCRIPCION);

        restTiposDocumentosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTiposDocumentos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTiposDocumentos))
            )
            .andExpect(status().isOk());

        // Validate the TiposDocumentos in the database
        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeUpdate);
        TiposDocumentos testTiposDocumentos = tiposDocumentosList.get(tiposDocumentosList.size() - 1);
        assertThat(testTiposDocumentos.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testTiposDocumentos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingTiposDocumentos() throws Exception {
        int databaseSizeBeforeUpdate = tiposDocumentosRepository.findAll().size();
        tiposDocumentos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTiposDocumentosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tiposDocumentos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tiposDocumentos))
            )
            .andExpect(status().isBadRequest());

        // Validate the TiposDocumentos in the database
        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTiposDocumentos() throws Exception {
        int databaseSizeBeforeUpdate = tiposDocumentosRepository.findAll().size();
        tiposDocumentos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiposDocumentosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tiposDocumentos))
            )
            .andExpect(status().isBadRequest());

        // Validate the TiposDocumentos in the database
        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTiposDocumentos() throws Exception {
        int databaseSizeBeforeUpdate = tiposDocumentosRepository.findAll().size();
        tiposDocumentos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiposDocumentosMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tiposDocumentos))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TiposDocumentos in the database
        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTiposDocumentosWithPatch() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        int databaseSizeBeforeUpdate = tiposDocumentosRepository.findAll().size();

        // Update the tiposDocumentos using partial update
        TiposDocumentos partialUpdatedTiposDocumentos = new TiposDocumentos();
        partialUpdatedTiposDocumentos.setId(tiposDocumentos.getId());

        partialUpdatedTiposDocumentos.codigo(UPDATED_CODIGO).descripcion(UPDATED_DESCRIPCION);

        restTiposDocumentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTiposDocumentos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTiposDocumentos))
            )
            .andExpect(status().isOk());

        // Validate the TiposDocumentos in the database
        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeUpdate);
        TiposDocumentos testTiposDocumentos = tiposDocumentosList.get(tiposDocumentosList.size() - 1);
        assertThat(testTiposDocumentos.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testTiposDocumentos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateTiposDocumentosWithPatch() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        int databaseSizeBeforeUpdate = tiposDocumentosRepository.findAll().size();

        // Update the tiposDocumentos using partial update
        TiposDocumentos partialUpdatedTiposDocumentos = new TiposDocumentos();
        partialUpdatedTiposDocumentos.setId(tiposDocumentos.getId());

        partialUpdatedTiposDocumentos.codigo(UPDATED_CODIGO).descripcion(UPDATED_DESCRIPCION);

        restTiposDocumentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTiposDocumentos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTiposDocumentos))
            )
            .andExpect(status().isOk());

        // Validate the TiposDocumentos in the database
        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeUpdate);
        TiposDocumentos testTiposDocumentos = tiposDocumentosList.get(tiposDocumentosList.size() - 1);
        assertThat(testTiposDocumentos.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testTiposDocumentos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingTiposDocumentos() throws Exception {
        int databaseSizeBeforeUpdate = tiposDocumentosRepository.findAll().size();
        tiposDocumentos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTiposDocumentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tiposDocumentos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tiposDocumentos))
            )
            .andExpect(status().isBadRequest());

        // Validate the TiposDocumentos in the database
        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTiposDocumentos() throws Exception {
        int databaseSizeBeforeUpdate = tiposDocumentosRepository.findAll().size();
        tiposDocumentos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiposDocumentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tiposDocumentos))
            )
            .andExpect(status().isBadRequest());

        // Validate the TiposDocumentos in the database
        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTiposDocumentos() throws Exception {
        int databaseSizeBeforeUpdate = tiposDocumentosRepository.findAll().size();
        tiposDocumentos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiposDocumentosMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tiposDocumentos))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TiposDocumentos in the database
        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTiposDocumentos() throws Exception {
        // Initialize the database
        tiposDocumentosRepository.saveAndFlush(tiposDocumentos);

        int databaseSizeBeforeDelete = tiposDocumentosRepository.findAll().size();

        // Delete the tiposDocumentos
        restTiposDocumentosMockMvc
            .perform(delete(ENTITY_API_URL_ID, tiposDocumentos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TiposDocumentos> tiposDocumentosList = tiposDocumentosRepository.findAll();
        assertThat(tiposDocumentosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
