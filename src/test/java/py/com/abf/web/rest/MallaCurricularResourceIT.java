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
import py.com.abf.domain.MallaCurricular;
import py.com.abf.domain.Temas;
import py.com.abf.domain.enumeration.Niveles;
import py.com.abf.repository.MallaCurricularRepository;
import py.com.abf.service.MallaCurricularService;
import py.com.abf.service.criteria.MallaCurricularCriteria;

/**
 * Integration tests for the {@link MallaCurricularResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MallaCurricularResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final Niveles DEFAULT_NIVEL = Niveles.PREAJEDREZ;
    private static final Niveles UPDATED_NIVEL = Niveles.INICIAL;

    private static final String ENTITY_API_URL = "/api/malla-curriculars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MallaCurricularRepository mallaCurricularRepository;

    @Mock
    private MallaCurricularRepository mallaCurricularRepositoryMock;

    @Mock
    private MallaCurricularService mallaCurricularServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMallaCurricularMockMvc;

    private MallaCurricular mallaCurricular;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MallaCurricular createEntity(EntityManager em) {
        MallaCurricular mallaCurricular = new MallaCurricular().titulo(DEFAULT_TITULO).nivel(DEFAULT_NIVEL);
        // Add required entity
        Temas temas;
        if (TestUtil.findAll(em, Temas.class).isEmpty()) {
            temas = TemasResourceIT.createEntity(em);
            em.persist(temas);
            em.flush();
        } else {
            temas = TestUtil.findAll(em, Temas.class).get(0);
        }
        mallaCurricular.getTemas().add(temas);
        return mallaCurricular;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MallaCurricular createUpdatedEntity(EntityManager em) {
        MallaCurricular mallaCurricular = new MallaCurricular().titulo(UPDATED_TITULO).nivel(UPDATED_NIVEL);
        // Add required entity
        Temas temas;
        if (TestUtil.findAll(em, Temas.class).isEmpty()) {
            temas = TemasResourceIT.createUpdatedEntity(em);
            em.persist(temas);
            em.flush();
        } else {
            temas = TestUtil.findAll(em, Temas.class).get(0);
        }
        mallaCurricular.getTemas().add(temas);
        return mallaCurricular;
    }

    @BeforeEach
    public void initTest() {
        mallaCurricular = createEntity(em);
    }

    @Test
    @Transactional
    void createMallaCurricular() throws Exception {
        int databaseSizeBeforeCreate = mallaCurricularRepository.findAll().size();
        // Create the MallaCurricular
        restMallaCurricularMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mallaCurricular))
            )
            .andExpect(status().isCreated());

        // Validate the MallaCurricular in the database
        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeCreate + 1);
        MallaCurricular testMallaCurricular = mallaCurricularList.get(mallaCurricularList.size() - 1);
        assertThat(testMallaCurricular.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testMallaCurricular.getNivel()).isEqualTo(DEFAULT_NIVEL);
    }

    @Test
    @Transactional
    void createMallaCurricularWithExistingId() throws Exception {
        // Create the MallaCurricular with an existing ID
        mallaCurricular.setId(1L);

        int databaseSizeBeforeCreate = mallaCurricularRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMallaCurricularMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mallaCurricular))
            )
            .andExpect(status().isBadRequest());

        // Validate the MallaCurricular in the database
        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = mallaCurricularRepository.findAll().size();
        // set the field null
        mallaCurricular.setTitulo(null);

        // Create the MallaCurricular, which fails.

        restMallaCurricularMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mallaCurricular))
            )
            .andExpect(status().isBadRequest());

        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNivelIsRequired() throws Exception {
        int databaseSizeBeforeTest = mallaCurricularRepository.findAll().size();
        // set the field null
        mallaCurricular.setNivel(null);

        // Create the MallaCurricular, which fails.

        restMallaCurricularMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mallaCurricular))
            )
            .andExpect(status().isBadRequest());

        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMallaCurriculars() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        // Get all the mallaCurricularList
        restMallaCurricularMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mallaCurricular.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].nivel").value(hasItem(DEFAULT_NIVEL.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMallaCurricularsWithEagerRelationshipsIsEnabled() throws Exception {
        when(mallaCurricularServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMallaCurricularMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mallaCurricularServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMallaCurricularsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(mallaCurricularServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMallaCurricularMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(mallaCurricularRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMallaCurricular() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        // Get the mallaCurricular
        restMallaCurricularMockMvc
            .perform(get(ENTITY_API_URL_ID, mallaCurricular.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mallaCurricular.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.nivel").value(DEFAULT_NIVEL.toString()));
    }

    @Test
    @Transactional
    void getMallaCurricularsByIdFiltering() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        Long id = mallaCurricular.getId();

        defaultMallaCurricularShouldBeFound("id.equals=" + id);
        defaultMallaCurricularShouldNotBeFound("id.notEquals=" + id);

        defaultMallaCurricularShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMallaCurricularShouldNotBeFound("id.greaterThan=" + id);

        defaultMallaCurricularShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMallaCurricularShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMallaCurricularsByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        // Get all the mallaCurricularList where titulo equals to DEFAULT_TITULO
        defaultMallaCurricularShouldBeFound("titulo.equals=" + DEFAULT_TITULO);

        // Get all the mallaCurricularList where titulo equals to UPDATED_TITULO
        defaultMallaCurricularShouldNotBeFound("titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllMallaCurricularsByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        // Get all the mallaCurricularList where titulo in DEFAULT_TITULO or UPDATED_TITULO
        defaultMallaCurricularShouldBeFound("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO);

        // Get all the mallaCurricularList where titulo equals to UPDATED_TITULO
        defaultMallaCurricularShouldNotBeFound("titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllMallaCurricularsByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        // Get all the mallaCurricularList where titulo is not null
        defaultMallaCurricularShouldBeFound("titulo.specified=true");

        // Get all the mallaCurricularList where titulo is null
        defaultMallaCurricularShouldNotBeFound("titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllMallaCurricularsByTituloContainsSomething() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        // Get all the mallaCurricularList where titulo contains DEFAULT_TITULO
        defaultMallaCurricularShouldBeFound("titulo.contains=" + DEFAULT_TITULO);

        // Get all the mallaCurricularList where titulo contains UPDATED_TITULO
        defaultMallaCurricularShouldNotBeFound("titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllMallaCurricularsByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        // Get all the mallaCurricularList where titulo does not contain DEFAULT_TITULO
        defaultMallaCurricularShouldNotBeFound("titulo.doesNotContain=" + DEFAULT_TITULO);

        // Get all the mallaCurricularList where titulo does not contain UPDATED_TITULO
        defaultMallaCurricularShouldBeFound("titulo.doesNotContain=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllMallaCurricularsByNivelIsEqualToSomething() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        // Get all the mallaCurricularList where nivel equals to DEFAULT_NIVEL
        defaultMallaCurricularShouldBeFound("nivel.equals=" + DEFAULT_NIVEL);

        // Get all the mallaCurricularList where nivel equals to UPDATED_NIVEL
        defaultMallaCurricularShouldNotBeFound("nivel.equals=" + UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void getAllMallaCurricularsByNivelIsInShouldWork() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        // Get all the mallaCurricularList where nivel in DEFAULT_NIVEL or UPDATED_NIVEL
        defaultMallaCurricularShouldBeFound("nivel.in=" + DEFAULT_NIVEL + "," + UPDATED_NIVEL);

        // Get all the mallaCurricularList where nivel equals to UPDATED_NIVEL
        defaultMallaCurricularShouldNotBeFound("nivel.in=" + UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void getAllMallaCurricularsByNivelIsNullOrNotNull() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        // Get all the mallaCurricularList where nivel is not null
        defaultMallaCurricularShouldBeFound("nivel.specified=true");

        // Get all the mallaCurricularList where nivel is null
        defaultMallaCurricularShouldNotBeFound("nivel.specified=false");
    }

    @Test
    @Transactional
    void getAllMallaCurricularsByTemasIsEqualToSomething() throws Exception {
        Temas temas;
        if (TestUtil.findAll(em, Temas.class).isEmpty()) {
            mallaCurricularRepository.saveAndFlush(mallaCurricular);
            temas = TemasResourceIT.createEntity(em);
        } else {
            temas = TestUtil.findAll(em, Temas.class).get(0);
        }
        em.persist(temas);
        em.flush();
        mallaCurricular.addTemas(temas);
        mallaCurricularRepository.saveAndFlush(mallaCurricular);
        Long temasId = temas.getId();

        // Get all the mallaCurricularList where temas equals to temasId
        defaultMallaCurricularShouldBeFound("temasId.equals=" + temasId);

        // Get all the mallaCurricularList where temas equals to (temasId + 1)
        defaultMallaCurricularShouldNotBeFound("temasId.equals=" + (temasId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMallaCurricularShouldBeFound(String filter) throws Exception {
        restMallaCurricularMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mallaCurricular.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].nivel").value(hasItem(DEFAULT_NIVEL.toString())));

        // Check, that the count call also returns 1
        restMallaCurricularMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMallaCurricularShouldNotBeFound(String filter) throws Exception {
        restMallaCurricularMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMallaCurricularMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMallaCurricular() throws Exception {
        // Get the mallaCurricular
        restMallaCurricularMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMallaCurricular() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        int databaseSizeBeforeUpdate = mallaCurricularRepository.findAll().size();

        // Update the mallaCurricular
        MallaCurricular updatedMallaCurricular = mallaCurricularRepository.findById(mallaCurricular.getId()).get();
        // Disconnect from session so that the updates on updatedMallaCurricular are not directly saved in db
        em.detach(updatedMallaCurricular);
        updatedMallaCurricular.titulo(UPDATED_TITULO).nivel(UPDATED_NIVEL);

        restMallaCurricularMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMallaCurricular.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMallaCurricular))
            )
            .andExpect(status().isOk());

        // Validate the MallaCurricular in the database
        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeUpdate);
        MallaCurricular testMallaCurricular = mallaCurricularList.get(mallaCurricularList.size() - 1);
        assertThat(testMallaCurricular.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testMallaCurricular.getNivel()).isEqualTo(UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void putNonExistingMallaCurricular() throws Exception {
        int databaseSizeBeforeUpdate = mallaCurricularRepository.findAll().size();
        mallaCurricular.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMallaCurricularMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mallaCurricular.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mallaCurricular))
            )
            .andExpect(status().isBadRequest());

        // Validate the MallaCurricular in the database
        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMallaCurricular() throws Exception {
        int databaseSizeBeforeUpdate = mallaCurricularRepository.findAll().size();
        mallaCurricular.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMallaCurricularMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mallaCurricular))
            )
            .andExpect(status().isBadRequest());

        // Validate the MallaCurricular in the database
        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMallaCurricular() throws Exception {
        int databaseSizeBeforeUpdate = mallaCurricularRepository.findAll().size();
        mallaCurricular.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMallaCurricularMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mallaCurricular))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MallaCurricular in the database
        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMallaCurricularWithPatch() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        int databaseSizeBeforeUpdate = mallaCurricularRepository.findAll().size();

        // Update the mallaCurricular using partial update
        MallaCurricular partialUpdatedMallaCurricular = new MallaCurricular();
        partialUpdatedMallaCurricular.setId(mallaCurricular.getId());

        partialUpdatedMallaCurricular.titulo(UPDATED_TITULO);

        restMallaCurricularMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMallaCurricular.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMallaCurricular))
            )
            .andExpect(status().isOk());

        // Validate the MallaCurricular in the database
        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeUpdate);
        MallaCurricular testMallaCurricular = mallaCurricularList.get(mallaCurricularList.size() - 1);
        assertThat(testMallaCurricular.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testMallaCurricular.getNivel()).isEqualTo(DEFAULT_NIVEL);
    }

    @Test
    @Transactional
    void fullUpdateMallaCurricularWithPatch() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        int databaseSizeBeforeUpdate = mallaCurricularRepository.findAll().size();

        // Update the mallaCurricular using partial update
        MallaCurricular partialUpdatedMallaCurricular = new MallaCurricular();
        partialUpdatedMallaCurricular.setId(mallaCurricular.getId());

        partialUpdatedMallaCurricular.titulo(UPDATED_TITULO).nivel(UPDATED_NIVEL);

        restMallaCurricularMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMallaCurricular.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMallaCurricular))
            )
            .andExpect(status().isOk());

        // Validate the MallaCurricular in the database
        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeUpdate);
        MallaCurricular testMallaCurricular = mallaCurricularList.get(mallaCurricularList.size() - 1);
        assertThat(testMallaCurricular.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testMallaCurricular.getNivel()).isEqualTo(UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void patchNonExistingMallaCurricular() throws Exception {
        int databaseSizeBeforeUpdate = mallaCurricularRepository.findAll().size();
        mallaCurricular.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMallaCurricularMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mallaCurricular.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mallaCurricular))
            )
            .andExpect(status().isBadRequest());

        // Validate the MallaCurricular in the database
        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMallaCurricular() throws Exception {
        int databaseSizeBeforeUpdate = mallaCurricularRepository.findAll().size();
        mallaCurricular.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMallaCurricularMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mallaCurricular))
            )
            .andExpect(status().isBadRequest());

        // Validate the MallaCurricular in the database
        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMallaCurricular() throws Exception {
        int databaseSizeBeforeUpdate = mallaCurricularRepository.findAll().size();
        mallaCurricular.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMallaCurricularMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mallaCurricular))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MallaCurricular in the database
        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMallaCurricular() throws Exception {
        // Initialize the database
        mallaCurricularRepository.saveAndFlush(mallaCurricular);

        int databaseSizeBeforeDelete = mallaCurricularRepository.findAll().size();

        // Delete the mallaCurricular
        restMallaCurricularMockMvc
            .perform(delete(ENTITY_API_URL_ID, mallaCurricular.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MallaCurricular> mallaCurricularList = mallaCurricularRepository.findAll();
        assertThat(mallaCurricularList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
