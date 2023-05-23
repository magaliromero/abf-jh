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
import py.com.abf.domain.Evaluaciones;
import py.com.abf.domain.EvaluacionesDetalle;
import py.com.abf.domain.Temas;
import py.com.abf.repository.EvaluacionesDetalleRepository;
import py.com.abf.service.EvaluacionesDetalleService;
import py.com.abf.service.criteria.EvaluacionesDetalleCriteria;

/**
 * Integration tests for the {@link EvaluacionesDetalleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EvaluacionesDetalleResourceIT {

    private static final String DEFAULT_COMENTARIOS = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIOS = "BBBBBBBBBB";

    private static final Integer DEFAULT_PUNTAJE = 1;
    private static final Integer UPDATED_PUNTAJE = 2;
    private static final Integer SMALLER_PUNTAJE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/evaluaciones-detalles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EvaluacionesDetalleRepository evaluacionesDetalleRepository;

    @Mock
    private EvaluacionesDetalleRepository evaluacionesDetalleRepositoryMock;

    @Mock
    private EvaluacionesDetalleService evaluacionesDetalleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvaluacionesDetalleMockMvc;

    private EvaluacionesDetalle evaluacionesDetalle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EvaluacionesDetalle createEntity(EntityManager em) {
        EvaluacionesDetalle evaluacionesDetalle = new EvaluacionesDetalle().comentarios(DEFAULT_COMENTARIOS).puntaje(DEFAULT_PUNTAJE);
        // Add required entity
        Evaluaciones evaluaciones;
        if (TestUtil.findAll(em, Evaluaciones.class).isEmpty()) {
            evaluaciones = EvaluacionesResourceIT.createEntity(em);
            em.persist(evaluaciones);
            em.flush();
        } else {
            evaluaciones = TestUtil.findAll(em, Evaluaciones.class).get(0);
        }
        evaluacionesDetalle.setEvaluaciones(evaluaciones);
        // Add required entity
        Temas temas;
        if (TestUtil.findAll(em, Temas.class).isEmpty()) {
            temas = TemasResourceIT.createEntity(em);
            em.persist(temas);
            em.flush();
        } else {
            temas = TestUtil.findAll(em, Temas.class).get(0);
        }
        evaluacionesDetalle.setTemas(temas);
        return evaluacionesDetalle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EvaluacionesDetalle createUpdatedEntity(EntityManager em) {
        EvaluacionesDetalle evaluacionesDetalle = new EvaluacionesDetalle().comentarios(UPDATED_COMENTARIOS).puntaje(UPDATED_PUNTAJE);
        // Add required entity
        Evaluaciones evaluaciones;
        if (TestUtil.findAll(em, Evaluaciones.class).isEmpty()) {
            evaluaciones = EvaluacionesResourceIT.createUpdatedEntity(em);
            em.persist(evaluaciones);
            em.flush();
        } else {
            evaluaciones = TestUtil.findAll(em, Evaluaciones.class).get(0);
        }
        evaluacionesDetalle.setEvaluaciones(evaluaciones);
        // Add required entity
        Temas temas;
        if (TestUtil.findAll(em, Temas.class).isEmpty()) {
            temas = TemasResourceIT.createUpdatedEntity(em);
            em.persist(temas);
            em.flush();
        } else {
            temas = TestUtil.findAll(em, Temas.class).get(0);
        }
        evaluacionesDetalle.setTemas(temas);
        return evaluacionesDetalle;
    }

    @BeforeEach
    public void initTest() {
        evaluacionesDetalle = createEntity(em);
    }

    @Test
    @Transactional
    void createEvaluacionesDetalle() throws Exception {
        int databaseSizeBeforeCreate = evaluacionesDetalleRepository.findAll().size();
        // Create the EvaluacionesDetalle
        restEvaluacionesDetalleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluacionesDetalle))
            )
            .andExpect(status().isCreated());

        // Validate the EvaluacionesDetalle in the database
        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeCreate + 1);
        EvaluacionesDetalle testEvaluacionesDetalle = evaluacionesDetalleList.get(evaluacionesDetalleList.size() - 1);
        assertThat(testEvaluacionesDetalle.getComentarios()).isEqualTo(DEFAULT_COMENTARIOS);
        assertThat(testEvaluacionesDetalle.getPuntaje()).isEqualTo(DEFAULT_PUNTAJE);
    }

    @Test
    @Transactional
    void createEvaluacionesDetalleWithExistingId() throws Exception {
        // Create the EvaluacionesDetalle with an existing ID
        evaluacionesDetalle.setId(1L);

        int databaseSizeBeforeCreate = evaluacionesDetalleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaluacionesDetalleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluacionesDetalle))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluacionesDetalle in the database
        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkComentariosIsRequired() throws Exception {
        int databaseSizeBeforeTest = evaluacionesDetalleRepository.findAll().size();
        // set the field null
        evaluacionesDetalle.setComentarios(null);

        // Create the EvaluacionesDetalle, which fails.

        restEvaluacionesDetalleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluacionesDetalle))
            )
            .andExpect(status().isBadRequest());

        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPuntajeIsRequired() throws Exception {
        int databaseSizeBeforeTest = evaluacionesDetalleRepository.findAll().size();
        // set the field null
        evaluacionesDetalle.setPuntaje(null);

        // Create the EvaluacionesDetalle, which fails.

        restEvaluacionesDetalleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluacionesDetalle))
            )
            .andExpect(status().isBadRequest());

        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetalles() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get all the evaluacionesDetalleList
        restEvaluacionesDetalleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluacionesDetalle.getId().intValue())))
            .andExpect(jsonPath("$.[*].comentarios").value(hasItem(DEFAULT_COMENTARIOS)))
            .andExpect(jsonPath("$.[*].puntaje").value(hasItem(DEFAULT_PUNTAJE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvaluacionesDetallesWithEagerRelationshipsIsEnabled() throws Exception {
        when(evaluacionesDetalleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvaluacionesDetalleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(evaluacionesDetalleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvaluacionesDetallesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(evaluacionesDetalleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvaluacionesDetalleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(evaluacionesDetalleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEvaluacionesDetalle() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get the evaluacionesDetalle
        restEvaluacionesDetalleMockMvc
            .perform(get(ENTITY_API_URL_ID, evaluacionesDetalle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evaluacionesDetalle.getId().intValue()))
            .andExpect(jsonPath("$.comentarios").value(DEFAULT_COMENTARIOS))
            .andExpect(jsonPath("$.puntaje").value(DEFAULT_PUNTAJE));
    }

    @Test
    @Transactional
    void getEvaluacionesDetallesByIdFiltering() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        Long id = evaluacionesDetalle.getId();

        defaultEvaluacionesDetalleShouldBeFound("id.equals=" + id);
        defaultEvaluacionesDetalleShouldNotBeFound("id.notEquals=" + id);

        defaultEvaluacionesDetalleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEvaluacionesDetalleShouldNotBeFound("id.greaterThan=" + id);

        defaultEvaluacionesDetalleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEvaluacionesDetalleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByComentariosIsEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get all the evaluacionesDetalleList where comentarios equals to DEFAULT_COMENTARIOS
        defaultEvaluacionesDetalleShouldBeFound("comentarios.equals=" + DEFAULT_COMENTARIOS);

        // Get all the evaluacionesDetalleList where comentarios equals to UPDATED_COMENTARIOS
        defaultEvaluacionesDetalleShouldNotBeFound("comentarios.equals=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByComentariosIsInShouldWork() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get all the evaluacionesDetalleList where comentarios in DEFAULT_COMENTARIOS or UPDATED_COMENTARIOS
        defaultEvaluacionesDetalleShouldBeFound("comentarios.in=" + DEFAULT_COMENTARIOS + "," + UPDATED_COMENTARIOS);

        // Get all the evaluacionesDetalleList where comentarios equals to UPDATED_COMENTARIOS
        defaultEvaluacionesDetalleShouldNotBeFound("comentarios.in=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByComentariosIsNullOrNotNull() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get all the evaluacionesDetalleList where comentarios is not null
        defaultEvaluacionesDetalleShouldBeFound("comentarios.specified=true");

        // Get all the evaluacionesDetalleList where comentarios is null
        defaultEvaluacionesDetalleShouldNotBeFound("comentarios.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByComentariosContainsSomething() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get all the evaluacionesDetalleList where comentarios contains DEFAULT_COMENTARIOS
        defaultEvaluacionesDetalleShouldBeFound("comentarios.contains=" + DEFAULT_COMENTARIOS);

        // Get all the evaluacionesDetalleList where comentarios contains UPDATED_COMENTARIOS
        defaultEvaluacionesDetalleShouldNotBeFound("comentarios.contains=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByComentariosNotContainsSomething() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get all the evaluacionesDetalleList where comentarios does not contain DEFAULT_COMENTARIOS
        defaultEvaluacionesDetalleShouldNotBeFound("comentarios.doesNotContain=" + DEFAULT_COMENTARIOS);

        // Get all the evaluacionesDetalleList where comentarios does not contain UPDATED_COMENTARIOS
        defaultEvaluacionesDetalleShouldBeFound("comentarios.doesNotContain=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByPuntajeIsEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get all the evaluacionesDetalleList where puntaje equals to DEFAULT_PUNTAJE
        defaultEvaluacionesDetalleShouldBeFound("puntaje.equals=" + DEFAULT_PUNTAJE);

        // Get all the evaluacionesDetalleList where puntaje equals to UPDATED_PUNTAJE
        defaultEvaluacionesDetalleShouldNotBeFound("puntaje.equals=" + UPDATED_PUNTAJE);
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByPuntajeIsInShouldWork() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get all the evaluacionesDetalleList where puntaje in DEFAULT_PUNTAJE or UPDATED_PUNTAJE
        defaultEvaluacionesDetalleShouldBeFound("puntaje.in=" + DEFAULT_PUNTAJE + "," + UPDATED_PUNTAJE);

        // Get all the evaluacionesDetalleList where puntaje equals to UPDATED_PUNTAJE
        defaultEvaluacionesDetalleShouldNotBeFound("puntaje.in=" + UPDATED_PUNTAJE);
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByPuntajeIsNullOrNotNull() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get all the evaluacionesDetalleList where puntaje is not null
        defaultEvaluacionesDetalleShouldBeFound("puntaje.specified=true");

        // Get all the evaluacionesDetalleList where puntaje is null
        defaultEvaluacionesDetalleShouldNotBeFound("puntaje.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByPuntajeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get all the evaluacionesDetalleList where puntaje is greater than or equal to DEFAULT_PUNTAJE
        defaultEvaluacionesDetalleShouldBeFound("puntaje.greaterThanOrEqual=" + DEFAULT_PUNTAJE);

        // Get all the evaluacionesDetalleList where puntaje is greater than or equal to UPDATED_PUNTAJE
        defaultEvaluacionesDetalleShouldNotBeFound("puntaje.greaterThanOrEqual=" + UPDATED_PUNTAJE);
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByPuntajeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get all the evaluacionesDetalleList where puntaje is less than or equal to DEFAULT_PUNTAJE
        defaultEvaluacionesDetalleShouldBeFound("puntaje.lessThanOrEqual=" + DEFAULT_PUNTAJE);

        // Get all the evaluacionesDetalleList where puntaje is less than or equal to SMALLER_PUNTAJE
        defaultEvaluacionesDetalleShouldNotBeFound("puntaje.lessThanOrEqual=" + SMALLER_PUNTAJE);
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByPuntajeIsLessThanSomething() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get all the evaluacionesDetalleList where puntaje is less than DEFAULT_PUNTAJE
        defaultEvaluacionesDetalleShouldNotBeFound("puntaje.lessThan=" + DEFAULT_PUNTAJE);

        // Get all the evaluacionesDetalleList where puntaje is less than UPDATED_PUNTAJE
        defaultEvaluacionesDetalleShouldBeFound("puntaje.lessThan=" + UPDATED_PUNTAJE);
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByPuntajeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        // Get all the evaluacionesDetalleList where puntaje is greater than DEFAULT_PUNTAJE
        defaultEvaluacionesDetalleShouldNotBeFound("puntaje.greaterThan=" + DEFAULT_PUNTAJE);

        // Get all the evaluacionesDetalleList where puntaje is greater than SMALLER_PUNTAJE
        defaultEvaluacionesDetalleShouldBeFound("puntaje.greaterThan=" + SMALLER_PUNTAJE);
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByEvaluacionesIsEqualToSomething() throws Exception {
        Evaluaciones evaluaciones;
        if (TestUtil.findAll(em, Evaluaciones.class).isEmpty()) {
            evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);
            evaluaciones = EvaluacionesResourceIT.createEntity(em);
        } else {
            evaluaciones = TestUtil.findAll(em, Evaluaciones.class).get(0);
        }
        em.persist(evaluaciones);
        em.flush();
        evaluacionesDetalle.setEvaluaciones(evaluaciones);
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);
        Long evaluacionesId = evaluaciones.getId();

        // Get all the evaluacionesDetalleList where evaluaciones equals to evaluacionesId
        defaultEvaluacionesDetalleShouldBeFound("evaluacionesId.equals=" + evaluacionesId);

        // Get all the evaluacionesDetalleList where evaluaciones equals to (evaluacionesId + 1)
        defaultEvaluacionesDetalleShouldNotBeFound("evaluacionesId.equals=" + (evaluacionesId + 1));
    }

    @Test
    @Transactional
    void getAllEvaluacionesDetallesByTemasIsEqualToSomething() throws Exception {
        Temas temas;
        if (TestUtil.findAll(em, Temas.class).isEmpty()) {
            evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);
            temas = TemasResourceIT.createEntity(em);
        } else {
            temas = TestUtil.findAll(em, Temas.class).get(0);
        }
        em.persist(temas);
        em.flush();
        evaluacionesDetalle.setTemas(temas);
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);
        Long temasId = temas.getId();

        // Get all the evaluacionesDetalleList where temas equals to temasId
        defaultEvaluacionesDetalleShouldBeFound("temasId.equals=" + temasId);

        // Get all the evaluacionesDetalleList where temas equals to (temasId + 1)
        defaultEvaluacionesDetalleShouldNotBeFound("temasId.equals=" + (temasId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEvaluacionesDetalleShouldBeFound(String filter) throws Exception {
        restEvaluacionesDetalleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluacionesDetalle.getId().intValue())))
            .andExpect(jsonPath("$.[*].comentarios").value(hasItem(DEFAULT_COMENTARIOS)))
            .andExpect(jsonPath("$.[*].puntaje").value(hasItem(DEFAULT_PUNTAJE)));

        // Check, that the count call also returns 1
        restEvaluacionesDetalleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEvaluacionesDetalleShouldNotBeFound(String filter) throws Exception {
        restEvaluacionesDetalleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEvaluacionesDetalleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvaluacionesDetalle() throws Exception {
        // Get the evaluacionesDetalle
        restEvaluacionesDetalleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvaluacionesDetalle() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        int databaseSizeBeforeUpdate = evaluacionesDetalleRepository.findAll().size();

        // Update the evaluacionesDetalle
        EvaluacionesDetalle updatedEvaluacionesDetalle = evaluacionesDetalleRepository.findById(evaluacionesDetalle.getId()).get();
        // Disconnect from session so that the updates on updatedEvaluacionesDetalle are not directly saved in db
        em.detach(updatedEvaluacionesDetalle);
        updatedEvaluacionesDetalle.comentarios(UPDATED_COMENTARIOS).puntaje(UPDATED_PUNTAJE);

        restEvaluacionesDetalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvaluacionesDetalle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvaluacionesDetalle))
            )
            .andExpect(status().isOk());

        // Validate the EvaluacionesDetalle in the database
        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeUpdate);
        EvaluacionesDetalle testEvaluacionesDetalle = evaluacionesDetalleList.get(evaluacionesDetalleList.size() - 1);
        assertThat(testEvaluacionesDetalle.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testEvaluacionesDetalle.getPuntaje()).isEqualTo(UPDATED_PUNTAJE);
    }

    @Test
    @Transactional
    void putNonExistingEvaluacionesDetalle() throws Exception {
        int databaseSizeBeforeUpdate = evaluacionesDetalleRepository.findAll().size();
        evaluacionesDetalle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluacionesDetalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaluacionesDetalle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evaluacionesDetalle))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluacionesDetalle in the database
        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvaluacionesDetalle() throws Exception {
        int databaseSizeBeforeUpdate = evaluacionesDetalleRepository.findAll().size();
        evaluacionesDetalle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluacionesDetalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evaluacionesDetalle))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluacionesDetalle in the database
        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvaluacionesDetalle() throws Exception {
        int databaseSizeBeforeUpdate = evaluacionesDetalleRepository.findAll().size();
        evaluacionesDetalle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluacionesDetalleMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluacionesDetalle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EvaluacionesDetalle in the database
        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEvaluacionesDetalleWithPatch() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        int databaseSizeBeforeUpdate = evaluacionesDetalleRepository.findAll().size();

        // Update the evaluacionesDetalle using partial update
        EvaluacionesDetalle partialUpdatedEvaluacionesDetalle = new EvaluacionesDetalle();
        partialUpdatedEvaluacionesDetalle.setId(evaluacionesDetalle.getId());

        partialUpdatedEvaluacionesDetalle.comentarios(UPDATED_COMENTARIOS);

        restEvaluacionesDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluacionesDetalle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvaluacionesDetalle))
            )
            .andExpect(status().isOk());

        // Validate the EvaluacionesDetalle in the database
        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeUpdate);
        EvaluacionesDetalle testEvaluacionesDetalle = evaluacionesDetalleList.get(evaluacionesDetalleList.size() - 1);
        assertThat(testEvaluacionesDetalle.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testEvaluacionesDetalle.getPuntaje()).isEqualTo(DEFAULT_PUNTAJE);
    }

    @Test
    @Transactional
    void fullUpdateEvaluacionesDetalleWithPatch() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        int databaseSizeBeforeUpdate = evaluacionesDetalleRepository.findAll().size();

        // Update the evaluacionesDetalle using partial update
        EvaluacionesDetalle partialUpdatedEvaluacionesDetalle = new EvaluacionesDetalle();
        partialUpdatedEvaluacionesDetalle.setId(evaluacionesDetalle.getId());

        partialUpdatedEvaluacionesDetalle.comentarios(UPDATED_COMENTARIOS).puntaje(UPDATED_PUNTAJE);

        restEvaluacionesDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluacionesDetalle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvaluacionesDetalle))
            )
            .andExpect(status().isOk());

        // Validate the EvaluacionesDetalle in the database
        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeUpdate);
        EvaluacionesDetalle testEvaluacionesDetalle = evaluacionesDetalleList.get(evaluacionesDetalleList.size() - 1);
        assertThat(testEvaluacionesDetalle.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testEvaluacionesDetalle.getPuntaje()).isEqualTo(UPDATED_PUNTAJE);
    }

    @Test
    @Transactional
    void patchNonExistingEvaluacionesDetalle() throws Exception {
        int databaseSizeBeforeUpdate = evaluacionesDetalleRepository.findAll().size();
        evaluacionesDetalle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluacionesDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evaluacionesDetalle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaluacionesDetalle))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluacionesDetalle in the database
        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvaluacionesDetalle() throws Exception {
        int databaseSizeBeforeUpdate = evaluacionesDetalleRepository.findAll().size();
        evaluacionesDetalle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluacionesDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaluacionesDetalle))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluacionesDetalle in the database
        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvaluacionesDetalle() throws Exception {
        int databaseSizeBeforeUpdate = evaluacionesDetalleRepository.findAll().size();
        evaluacionesDetalle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluacionesDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaluacionesDetalle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EvaluacionesDetalle in the database
        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvaluacionesDetalle() throws Exception {
        // Initialize the database
        evaluacionesDetalleRepository.saveAndFlush(evaluacionesDetalle);

        int databaseSizeBeforeDelete = evaluacionesDetalleRepository.findAll().size();

        // Delete the evaluacionesDetalle
        restEvaluacionesDetalleMockMvc
            .perform(delete(ENTITY_API_URL_ID, evaluacionesDetalle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EvaluacionesDetalle> evaluacionesDetalleList = evaluacionesDetalleRepository.findAll();
        assertThat(evaluacionesDetalleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
