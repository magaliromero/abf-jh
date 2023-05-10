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
import py.com.abf.domain.Alumnos;
import py.com.abf.domain.FichaPartidasTorneos;
import py.com.abf.domain.Torneos;
import py.com.abf.repository.FichaPartidasTorneosRepository;
import py.com.abf.service.FichaPartidasTorneosService;
import py.com.abf.service.criteria.FichaPartidasTorneosCriteria;

/**
 * Integration tests for the {@link FichaPartidasTorneosResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FichaPartidasTorneosResourceIT {

    private static final String DEFAULT_NOMBRE_CONTRINCANTE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CONTRINCANTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURACION = 1;
    private static final Integer UPDATED_DURACION = 2;
    private static final Integer SMALLER_DURACION = 1 - 1;

    private static final String DEFAULT_WINNER = "AAAAAAAAAA";
    private static final String UPDATED_WINNER = "BBBBBBBBBB";

    private static final String DEFAULT_RESULTADO = "AAAAAAAAAA";
    private static final String UPDATED_RESULTADO = "BBBBBBBBBB";

    private static final String DEFAULT_COMENTARIOS = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIOS = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_ARBITRO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_ARBITRO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ficha-partidas-torneos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FichaPartidasTorneosRepository fichaPartidasTorneosRepository;

    @Mock
    private FichaPartidasTorneosRepository fichaPartidasTorneosRepositoryMock;

    @Mock
    private FichaPartidasTorneosService fichaPartidasTorneosServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFichaPartidasTorneosMockMvc;

    private FichaPartidasTorneos fichaPartidasTorneos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FichaPartidasTorneos createEntity(EntityManager em) {
        FichaPartidasTorneos fichaPartidasTorneos = new FichaPartidasTorneos()
            .nombreContrincante(DEFAULT_NOMBRE_CONTRINCANTE)
            .duracion(DEFAULT_DURACION)
            .winner(DEFAULT_WINNER)
            .resultado(DEFAULT_RESULTADO)
            .comentarios(DEFAULT_COMENTARIOS)
            .nombreArbitro(DEFAULT_NOMBRE_ARBITRO);
        // Add required entity
        Torneos torneos;
        if (TestUtil.findAll(em, Torneos.class).isEmpty()) {
            torneos = TorneosResourceIT.createEntity(em);
            em.persist(torneos);
            em.flush();
        } else {
            torneos = TestUtil.findAll(em, Torneos.class).get(0);
        }
        fichaPartidasTorneos.setTorneos(torneos);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        fichaPartidasTorneos.setAlumnos(alumnos);
        return fichaPartidasTorneos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FichaPartidasTorneos createUpdatedEntity(EntityManager em) {
        FichaPartidasTorneos fichaPartidasTorneos = new FichaPartidasTorneos()
            .nombreContrincante(UPDATED_NOMBRE_CONTRINCANTE)
            .duracion(UPDATED_DURACION)
            .winner(UPDATED_WINNER)
            .resultado(UPDATED_RESULTADO)
            .comentarios(UPDATED_COMENTARIOS)
            .nombreArbitro(UPDATED_NOMBRE_ARBITRO);
        // Add required entity
        Torneos torneos;
        if (TestUtil.findAll(em, Torneos.class).isEmpty()) {
            torneos = TorneosResourceIT.createUpdatedEntity(em);
            em.persist(torneos);
            em.flush();
        } else {
            torneos = TestUtil.findAll(em, Torneos.class).get(0);
        }
        fichaPartidasTorneos.setTorneos(torneos);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createUpdatedEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        fichaPartidasTorneos.setAlumnos(alumnos);
        return fichaPartidasTorneos;
    }

    @BeforeEach
    public void initTest() {
        fichaPartidasTorneos = createEntity(em);
    }

    @Test
    @Transactional
    void createFichaPartidasTorneos() throws Exception {
        int databaseSizeBeforeCreate = fichaPartidasTorneosRepository.findAll().size();
        // Create the FichaPartidasTorneos
        restFichaPartidasTorneosMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichaPartidasTorneos))
            )
            .andExpect(status().isCreated());

        // Validate the FichaPartidasTorneos in the database
        List<FichaPartidasTorneos> fichaPartidasTorneosList = fichaPartidasTorneosRepository.findAll();
        assertThat(fichaPartidasTorneosList).hasSize(databaseSizeBeforeCreate + 1);
        FichaPartidasTorneos testFichaPartidasTorneos = fichaPartidasTorneosList.get(fichaPartidasTorneosList.size() - 1);
        assertThat(testFichaPartidasTorneos.getNombreContrincante()).isEqualTo(DEFAULT_NOMBRE_CONTRINCANTE);
        assertThat(testFichaPartidasTorneos.getDuracion()).isEqualTo(DEFAULT_DURACION);
        assertThat(testFichaPartidasTorneos.getWinner()).isEqualTo(DEFAULT_WINNER);
        assertThat(testFichaPartidasTorneos.getResultado()).isEqualTo(DEFAULT_RESULTADO);
        assertThat(testFichaPartidasTorneos.getComentarios()).isEqualTo(DEFAULT_COMENTARIOS);
        assertThat(testFichaPartidasTorneos.getNombreArbitro()).isEqualTo(DEFAULT_NOMBRE_ARBITRO);
    }

    @Test
    @Transactional
    void createFichaPartidasTorneosWithExistingId() throws Exception {
        // Create the FichaPartidasTorneos with an existing ID
        fichaPartidasTorneos.setId(1L);

        int databaseSizeBeforeCreate = fichaPartidasTorneosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFichaPartidasTorneosMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichaPartidasTorneos))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichaPartidasTorneos in the database
        List<FichaPartidasTorneos> fichaPartidasTorneosList = fichaPartidasTorneosRepository.findAll();
        assertThat(fichaPartidasTorneosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneos() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList
        restFichaPartidasTorneosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fichaPartidasTorneos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreContrincante").value(hasItem(DEFAULT_NOMBRE_CONTRINCANTE)))
            .andExpect(jsonPath("$.[*].duracion").value(hasItem(DEFAULT_DURACION)))
            .andExpect(jsonPath("$.[*].winner").value(hasItem(DEFAULT_WINNER)))
            .andExpect(jsonPath("$.[*].resultado").value(hasItem(DEFAULT_RESULTADO)))
            .andExpect(jsonPath("$.[*].comentarios").value(hasItem(DEFAULT_COMENTARIOS)))
            .andExpect(jsonPath("$.[*].nombreArbitro").value(hasItem(DEFAULT_NOMBRE_ARBITRO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFichaPartidasTorneosWithEagerRelationshipsIsEnabled() throws Exception {
        when(fichaPartidasTorneosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFichaPartidasTorneosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fichaPartidasTorneosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFichaPartidasTorneosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fichaPartidasTorneosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFichaPartidasTorneosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(fichaPartidasTorneosRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFichaPartidasTorneos() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get the fichaPartidasTorneos
        restFichaPartidasTorneosMockMvc
            .perform(get(ENTITY_API_URL_ID, fichaPartidasTorneos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fichaPartidasTorneos.getId().intValue()))
            .andExpect(jsonPath("$.nombreContrincante").value(DEFAULT_NOMBRE_CONTRINCANTE))
            .andExpect(jsonPath("$.duracion").value(DEFAULT_DURACION))
            .andExpect(jsonPath("$.winner").value(DEFAULT_WINNER))
            .andExpect(jsonPath("$.resultado").value(DEFAULT_RESULTADO))
            .andExpect(jsonPath("$.comentarios").value(DEFAULT_COMENTARIOS))
            .andExpect(jsonPath("$.nombreArbitro").value(DEFAULT_NOMBRE_ARBITRO));
    }

    @Test
    @Transactional
    void getFichaPartidasTorneosByIdFiltering() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        Long id = fichaPartidasTorneos.getId();

        defaultFichaPartidasTorneosShouldBeFound("id.equals=" + id);
        defaultFichaPartidasTorneosShouldNotBeFound("id.notEquals=" + id);

        defaultFichaPartidasTorneosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFichaPartidasTorneosShouldNotBeFound("id.greaterThan=" + id);

        defaultFichaPartidasTorneosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFichaPartidasTorneosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByNombreContrincanteIsEqualToSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where nombreContrincante equals to DEFAULT_NOMBRE_CONTRINCANTE
        defaultFichaPartidasTorneosShouldBeFound("nombreContrincante.equals=" + DEFAULT_NOMBRE_CONTRINCANTE);

        // Get all the fichaPartidasTorneosList where nombreContrincante equals to UPDATED_NOMBRE_CONTRINCANTE
        defaultFichaPartidasTorneosShouldNotBeFound("nombreContrincante.equals=" + UPDATED_NOMBRE_CONTRINCANTE);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByNombreContrincanteIsInShouldWork() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where nombreContrincante in DEFAULT_NOMBRE_CONTRINCANTE or UPDATED_NOMBRE_CONTRINCANTE
        defaultFichaPartidasTorneosShouldBeFound(
            "nombreContrincante.in=" + DEFAULT_NOMBRE_CONTRINCANTE + "," + UPDATED_NOMBRE_CONTRINCANTE
        );

        // Get all the fichaPartidasTorneosList where nombreContrincante equals to UPDATED_NOMBRE_CONTRINCANTE
        defaultFichaPartidasTorneosShouldNotBeFound("nombreContrincante.in=" + UPDATED_NOMBRE_CONTRINCANTE);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByNombreContrincanteIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where nombreContrincante is not null
        defaultFichaPartidasTorneosShouldBeFound("nombreContrincante.specified=true");

        // Get all the fichaPartidasTorneosList where nombreContrincante is null
        defaultFichaPartidasTorneosShouldNotBeFound("nombreContrincante.specified=false");
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByNombreContrincanteContainsSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where nombreContrincante contains DEFAULT_NOMBRE_CONTRINCANTE
        defaultFichaPartidasTorneosShouldBeFound("nombreContrincante.contains=" + DEFAULT_NOMBRE_CONTRINCANTE);

        // Get all the fichaPartidasTorneosList where nombreContrincante contains UPDATED_NOMBRE_CONTRINCANTE
        defaultFichaPartidasTorneosShouldNotBeFound("nombreContrincante.contains=" + UPDATED_NOMBRE_CONTRINCANTE);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByNombreContrincanteNotContainsSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where nombreContrincante does not contain DEFAULT_NOMBRE_CONTRINCANTE
        defaultFichaPartidasTorneosShouldNotBeFound("nombreContrincante.doesNotContain=" + DEFAULT_NOMBRE_CONTRINCANTE);

        // Get all the fichaPartidasTorneosList where nombreContrincante does not contain UPDATED_NOMBRE_CONTRINCANTE
        defaultFichaPartidasTorneosShouldBeFound("nombreContrincante.doesNotContain=" + UPDATED_NOMBRE_CONTRINCANTE);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByDuracionIsEqualToSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where duracion equals to DEFAULT_DURACION
        defaultFichaPartidasTorneosShouldBeFound("duracion.equals=" + DEFAULT_DURACION);

        // Get all the fichaPartidasTorneosList where duracion equals to UPDATED_DURACION
        defaultFichaPartidasTorneosShouldNotBeFound("duracion.equals=" + UPDATED_DURACION);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByDuracionIsInShouldWork() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where duracion in DEFAULT_DURACION or UPDATED_DURACION
        defaultFichaPartidasTorneosShouldBeFound("duracion.in=" + DEFAULT_DURACION + "," + UPDATED_DURACION);

        // Get all the fichaPartidasTorneosList where duracion equals to UPDATED_DURACION
        defaultFichaPartidasTorneosShouldNotBeFound("duracion.in=" + UPDATED_DURACION);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByDuracionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where duracion is not null
        defaultFichaPartidasTorneosShouldBeFound("duracion.specified=true");

        // Get all the fichaPartidasTorneosList where duracion is null
        defaultFichaPartidasTorneosShouldNotBeFound("duracion.specified=false");
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByDuracionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where duracion is greater than or equal to DEFAULT_DURACION
        defaultFichaPartidasTorneosShouldBeFound("duracion.greaterThanOrEqual=" + DEFAULT_DURACION);

        // Get all the fichaPartidasTorneosList where duracion is greater than or equal to UPDATED_DURACION
        defaultFichaPartidasTorneosShouldNotBeFound("duracion.greaterThanOrEqual=" + UPDATED_DURACION);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByDuracionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where duracion is less than or equal to DEFAULT_DURACION
        defaultFichaPartidasTorneosShouldBeFound("duracion.lessThanOrEqual=" + DEFAULT_DURACION);

        // Get all the fichaPartidasTorneosList where duracion is less than or equal to SMALLER_DURACION
        defaultFichaPartidasTorneosShouldNotBeFound("duracion.lessThanOrEqual=" + SMALLER_DURACION);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByDuracionIsLessThanSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where duracion is less than DEFAULT_DURACION
        defaultFichaPartidasTorneosShouldNotBeFound("duracion.lessThan=" + DEFAULT_DURACION);

        // Get all the fichaPartidasTorneosList where duracion is less than UPDATED_DURACION
        defaultFichaPartidasTorneosShouldBeFound("duracion.lessThan=" + UPDATED_DURACION);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByDuracionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where duracion is greater than DEFAULT_DURACION
        defaultFichaPartidasTorneosShouldNotBeFound("duracion.greaterThan=" + DEFAULT_DURACION);

        // Get all the fichaPartidasTorneosList where duracion is greater than SMALLER_DURACION
        defaultFichaPartidasTorneosShouldBeFound("duracion.greaterThan=" + SMALLER_DURACION);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByWinnerIsEqualToSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where winner equals to DEFAULT_WINNER
        defaultFichaPartidasTorneosShouldBeFound("winner.equals=" + DEFAULT_WINNER);

        // Get all the fichaPartidasTorneosList where winner equals to UPDATED_WINNER
        defaultFichaPartidasTorneosShouldNotBeFound("winner.equals=" + UPDATED_WINNER);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByWinnerIsInShouldWork() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where winner in DEFAULT_WINNER or UPDATED_WINNER
        defaultFichaPartidasTorneosShouldBeFound("winner.in=" + DEFAULT_WINNER + "," + UPDATED_WINNER);

        // Get all the fichaPartidasTorneosList where winner equals to UPDATED_WINNER
        defaultFichaPartidasTorneosShouldNotBeFound("winner.in=" + UPDATED_WINNER);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByWinnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where winner is not null
        defaultFichaPartidasTorneosShouldBeFound("winner.specified=true");

        // Get all the fichaPartidasTorneosList where winner is null
        defaultFichaPartidasTorneosShouldNotBeFound("winner.specified=false");
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByWinnerContainsSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where winner contains DEFAULT_WINNER
        defaultFichaPartidasTorneosShouldBeFound("winner.contains=" + DEFAULT_WINNER);

        // Get all the fichaPartidasTorneosList where winner contains UPDATED_WINNER
        defaultFichaPartidasTorneosShouldNotBeFound("winner.contains=" + UPDATED_WINNER);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByWinnerNotContainsSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where winner does not contain DEFAULT_WINNER
        defaultFichaPartidasTorneosShouldNotBeFound("winner.doesNotContain=" + DEFAULT_WINNER);

        // Get all the fichaPartidasTorneosList where winner does not contain UPDATED_WINNER
        defaultFichaPartidasTorneosShouldBeFound("winner.doesNotContain=" + UPDATED_WINNER);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByResultadoIsEqualToSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where resultado equals to DEFAULT_RESULTADO
        defaultFichaPartidasTorneosShouldBeFound("resultado.equals=" + DEFAULT_RESULTADO);

        // Get all the fichaPartidasTorneosList where resultado equals to UPDATED_RESULTADO
        defaultFichaPartidasTorneosShouldNotBeFound("resultado.equals=" + UPDATED_RESULTADO);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByResultadoIsInShouldWork() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where resultado in DEFAULT_RESULTADO or UPDATED_RESULTADO
        defaultFichaPartidasTorneosShouldBeFound("resultado.in=" + DEFAULT_RESULTADO + "," + UPDATED_RESULTADO);

        // Get all the fichaPartidasTorneosList where resultado equals to UPDATED_RESULTADO
        defaultFichaPartidasTorneosShouldNotBeFound("resultado.in=" + UPDATED_RESULTADO);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByResultadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where resultado is not null
        defaultFichaPartidasTorneosShouldBeFound("resultado.specified=true");

        // Get all the fichaPartidasTorneosList where resultado is null
        defaultFichaPartidasTorneosShouldNotBeFound("resultado.specified=false");
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByResultadoContainsSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where resultado contains DEFAULT_RESULTADO
        defaultFichaPartidasTorneosShouldBeFound("resultado.contains=" + DEFAULT_RESULTADO);

        // Get all the fichaPartidasTorneosList where resultado contains UPDATED_RESULTADO
        defaultFichaPartidasTorneosShouldNotBeFound("resultado.contains=" + UPDATED_RESULTADO);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByResultadoNotContainsSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where resultado does not contain DEFAULT_RESULTADO
        defaultFichaPartidasTorneosShouldNotBeFound("resultado.doesNotContain=" + DEFAULT_RESULTADO);

        // Get all the fichaPartidasTorneosList where resultado does not contain UPDATED_RESULTADO
        defaultFichaPartidasTorneosShouldBeFound("resultado.doesNotContain=" + UPDATED_RESULTADO);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByComentariosIsEqualToSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where comentarios equals to DEFAULT_COMENTARIOS
        defaultFichaPartidasTorneosShouldBeFound("comentarios.equals=" + DEFAULT_COMENTARIOS);

        // Get all the fichaPartidasTorneosList where comentarios equals to UPDATED_COMENTARIOS
        defaultFichaPartidasTorneosShouldNotBeFound("comentarios.equals=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByComentariosIsInShouldWork() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where comentarios in DEFAULT_COMENTARIOS or UPDATED_COMENTARIOS
        defaultFichaPartidasTorneosShouldBeFound("comentarios.in=" + DEFAULT_COMENTARIOS + "," + UPDATED_COMENTARIOS);

        // Get all the fichaPartidasTorneosList where comentarios equals to UPDATED_COMENTARIOS
        defaultFichaPartidasTorneosShouldNotBeFound("comentarios.in=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByComentariosIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where comentarios is not null
        defaultFichaPartidasTorneosShouldBeFound("comentarios.specified=true");

        // Get all the fichaPartidasTorneosList where comentarios is null
        defaultFichaPartidasTorneosShouldNotBeFound("comentarios.specified=false");
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByComentariosContainsSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where comentarios contains DEFAULT_COMENTARIOS
        defaultFichaPartidasTorneosShouldBeFound("comentarios.contains=" + DEFAULT_COMENTARIOS);

        // Get all the fichaPartidasTorneosList where comentarios contains UPDATED_COMENTARIOS
        defaultFichaPartidasTorneosShouldNotBeFound("comentarios.contains=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByComentariosNotContainsSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where comentarios does not contain DEFAULT_COMENTARIOS
        defaultFichaPartidasTorneosShouldNotBeFound("comentarios.doesNotContain=" + DEFAULT_COMENTARIOS);

        // Get all the fichaPartidasTorneosList where comentarios does not contain UPDATED_COMENTARIOS
        defaultFichaPartidasTorneosShouldBeFound("comentarios.doesNotContain=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByNombreArbitroIsEqualToSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where nombreArbitro equals to DEFAULT_NOMBRE_ARBITRO
        defaultFichaPartidasTorneosShouldBeFound("nombreArbitro.equals=" + DEFAULT_NOMBRE_ARBITRO);

        // Get all the fichaPartidasTorneosList where nombreArbitro equals to UPDATED_NOMBRE_ARBITRO
        defaultFichaPartidasTorneosShouldNotBeFound("nombreArbitro.equals=" + UPDATED_NOMBRE_ARBITRO);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByNombreArbitroIsInShouldWork() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where nombreArbitro in DEFAULT_NOMBRE_ARBITRO or UPDATED_NOMBRE_ARBITRO
        defaultFichaPartidasTorneosShouldBeFound("nombreArbitro.in=" + DEFAULT_NOMBRE_ARBITRO + "," + UPDATED_NOMBRE_ARBITRO);

        // Get all the fichaPartidasTorneosList where nombreArbitro equals to UPDATED_NOMBRE_ARBITRO
        defaultFichaPartidasTorneosShouldNotBeFound("nombreArbitro.in=" + UPDATED_NOMBRE_ARBITRO);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByNombreArbitroIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where nombreArbitro is not null
        defaultFichaPartidasTorneosShouldBeFound("nombreArbitro.specified=true");

        // Get all the fichaPartidasTorneosList where nombreArbitro is null
        defaultFichaPartidasTorneosShouldNotBeFound("nombreArbitro.specified=false");
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByNombreArbitroContainsSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where nombreArbitro contains DEFAULT_NOMBRE_ARBITRO
        defaultFichaPartidasTorneosShouldBeFound("nombreArbitro.contains=" + DEFAULT_NOMBRE_ARBITRO);

        // Get all the fichaPartidasTorneosList where nombreArbitro contains UPDATED_NOMBRE_ARBITRO
        defaultFichaPartidasTorneosShouldNotBeFound("nombreArbitro.contains=" + UPDATED_NOMBRE_ARBITRO);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByNombreArbitroNotContainsSomething() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        // Get all the fichaPartidasTorneosList where nombreArbitro does not contain DEFAULT_NOMBRE_ARBITRO
        defaultFichaPartidasTorneosShouldNotBeFound("nombreArbitro.doesNotContain=" + DEFAULT_NOMBRE_ARBITRO);

        // Get all the fichaPartidasTorneosList where nombreArbitro does not contain UPDATED_NOMBRE_ARBITRO
        defaultFichaPartidasTorneosShouldBeFound("nombreArbitro.doesNotContain=" + UPDATED_NOMBRE_ARBITRO);
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByTorneosIsEqualToSomething() throws Exception {
        Torneos torneos;
        if (TestUtil.findAll(em, Torneos.class).isEmpty()) {
            fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);
            torneos = TorneosResourceIT.createEntity(em);
        } else {
            torneos = TestUtil.findAll(em, Torneos.class).get(0);
        }
        em.persist(torneos);
        em.flush();
        fichaPartidasTorneos.setTorneos(torneos);
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);
        Long torneosId = torneos.getId();

        // Get all the fichaPartidasTorneosList where torneos equals to torneosId
        defaultFichaPartidasTorneosShouldBeFound("torneosId.equals=" + torneosId);

        // Get all the fichaPartidasTorneosList where torneos equals to (torneosId + 1)
        defaultFichaPartidasTorneosShouldNotBeFound("torneosId.equals=" + (torneosId + 1));
    }

    @Test
    @Transactional
    void getAllFichaPartidasTorneosByAlumnosIsEqualToSomething() throws Exception {
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);
            alumnos = AlumnosResourceIT.createEntity(em);
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        em.persist(alumnos);
        em.flush();
        fichaPartidasTorneos.setAlumnos(alumnos);
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);
        Long alumnosId = alumnos.getId();

        // Get all the fichaPartidasTorneosList where alumnos equals to alumnosId
        defaultFichaPartidasTorneosShouldBeFound("alumnosId.equals=" + alumnosId);

        // Get all the fichaPartidasTorneosList where alumnos equals to (alumnosId + 1)
        defaultFichaPartidasTorneosShouldNotBeFound("alumnosId.equals=" + (alumnosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFichaPartidasTorneosShouldBeFound(String filter) throws Exception {
        restFichaPartidasTorneosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fichaPartidasTorneos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreContrincante").value(hasItem(DEFAULT_NOMBRE_CONTRINCANTE)))
            .andExpect(jsonPath("$.[*].duracion").value(hasItem(DEFAULT_DURACION)))
            .andExpect(jsonPath("$.[*].winner").value(hasItem(DEFAULT_WINNER)))
            .andExpect(jsonPath("$.[*].resultado").value(hasItem(DEFAULT_RESULTADO)))
            .andExpect(jsonPath("$.[*].comentarios").value(hasItem(DEFAULT_COMENTARIOS)))
            .andExpect(jsonPath("$.[*].nombreArbitro").value(hasItem(DEFAULT_NOMBRE_ARBITRO)));

        // Check, that the count call also returns 1
        restFichaPartidasTorneosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFichaPartidasTorneosShouldNotBeFound(String filter) throws Exception {
        restFichaPartidasTorneosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFichaPartidasTorneosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFichaPartidasTorneos() throws Exception {
        // Get the fichaPartidasTorneos
        restFichaPartidasTorneosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFichaPartidasTorneos() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        int databaseSizeBeforeUpdate = fichaPartidasTorneosRepository.findAll().size();

        // Update the fichaPartidasTorneos
        FichaPartidasTorneos updatedFichaPartidasTorneos = fichaPartidasTorneosRepository.findById(fichaPartidasTorneos.getId()).get();
        // Disconnect from session so that the updates on updatedFichaPartidasTorneos are not directly saved in db
        em.detach(updatedFichaPartidasTorneos);
        updatedFichaPartidasTorneos
            .nombreContrincante(UPDATED_NOMBRE_CONTRINCANTE)
            .duracion(UPDATED_DURACION)
            .winner(UPDATED_WINNER)
            .resultado(UPDATED_RESULTADO)
            .comentarios(UPDATED_COMENTARIOS)
            .nombreArbitro(UPDATED_NOMBRE_ARBITRO);

        restFichaPartidasTorneosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFichaPartidasTorneos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFichaPartidasTorneos))
            )
            .andExpect(status().isOk());

        // Validate the FichaPartidasTorneos in the database
        List<FichaPartidasTorneos> fichaPartidasTorneosList = fichaPartidasTorneosRepository.findAll();
        assertThat(fichaPartidasTorneosList).hasSize(databaseSizeBeforeUpdate);
        FichaPartidasTorneos testFichaPartidasTorneos = fichaPartidasTorneosList.get(fichaPartidasTorneosList.size() - 1);
        assertThat(testFichaPartidasTorneos.getNombreContrincante()).isEqualTo(UPDATED_NOMBRE_CONTRINCANTE);
        assertThat(testFichaPartidasTorneos.getDuracion()).isEqualTo(UPDATED_DURACION);
        assertThat(testFichaPartidasTorneos.getWinner()).isEqualTo(UPDATED_WINNER);
        assertThat(testFichaPartidasTorneos.getResultado()).isEqualTo(UPDATED_RESULTADO);
        assertThat(testFichaPartidasTorneos.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testFichaPartidasTorneos.getNombreArbitro()).isEqualTo(UPDATED_NOMBRE_ARBITRO);
    }

    @Test
    @Transactional
    void putNonExistingFichaPartidasTorneos() throws Exception {
        int databaseSizeBeforeUpdate = fichaPartidasTorneosRepository.findAll().size();
        fichaPartidasTorneos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFichaPartidasTorneosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fichaPartidasTorneos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichaPartidasTorneos))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichaPartidasTorneos in the database
        List<FichaPartidasTorneos> fichaPartidasTorneosList = fichaPartidasTorneosRepository.findAll();
        assertThat(fichaPartidasTorneosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFichaPartidasTorneos() throws Exception {
        int databaseSizeBeforeUpdate = fichaPartidasTorneosRepository.findAll().size();
        fichaPartidasTorneos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichaPartidasTorneosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichaPartidasTorneos))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichaPartidasTorneos in the database
        List<FichaPartidasTorneos> fichaPartidasTorneosList = fichaPartidasTorneosRepository.findAll();
        assertThat(fichaPartidasTorneosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFichaPartidasTorneos() throws Exception {
        int databaseSizeBeforeUpdate = fichaPartidasTorneosRepository.findAll().size();
        fichaPartidasTorneos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichaPartidasTorneosMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichaPartidasTorneos))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FichaPartidasTorneos in the database
        List<FichaPartidasTorneos> fichaPartidasTorneosList = fichaPartidasTorneosRepository.findAll();
        assertThat(fichaPartidasTorneosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFichaPartidasTorneosWithPatch() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        int databaseSizeBeforeUpdate = fichaPartidasTorneosRepository.findAll().size();

        // Update the fichaPartidasTorneos using partial update
        FichaPartidasTorneos partialUpdatedFichaPartidasTorneos = new FichaPartidasTorneos();
        partialUpdatedFichaPartidasTorneos.setId(fichaPartidasTorneos.getId());

        partialUpdatedFichaPartidasTorneos
            .nombreContrincante(UPDATED_NOMBRE_CONTRINCANTE)
            .winner(UPDATED_WINNER)
            .nombreArbitro(UPDATED_NOMBRE_ARBITRO);

        restFichaPartidasTorneosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFichaPartidasTorneos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFichaPartidasTorneos))
            )
            .andExpect(status().isOk());

        // Validate the FichaPartidasTorneos in the database
        List<FichaPartidasTorneos> fichaPartidasTorneosList = fichaPartidasTorneosRepository.findAll();
        assertThat(fichaPartidasTorneosList).hasSize(databaseSizeBeforeUpdate);
        FichaPartidasTorneos testFichaPartidasTorneos = fichaPartidasTorneosList.get(fichaPartidasTorneosList.size() - 1);
        assertThat(testFichaPartidasTorneos.getNombreContrincante()).isEqualTo(UPDATED_NOMBRE_CONTRINCANTE);
        assertThat(testFichaPartidasTorneos.getDuracion()).isEqualTo(DEFAULT_DURACION);
        assertThat(testFichaPartidasTorneos.getWinner()).isEqualTo(UPDATED_WINNER);
        assertThat(testFichaPartidasTorneos.getResultado()).isEqualTo(DEFAULT_RESULTADO);
        assertThat(testFichaPartidasTorneos.getComentarios()).isEqualTo(DEFAULT_COMENTARIOS);
        assertThat(testFichaPartidasTorneos.getNombreArbitro()).isEqualTo(UPDATED_NOMBRE_ARBITRO);
    }

    @Test
    @Transactional
    void fullUpdateFichaPartidasTorneosWithPatch() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        int databaseSizeBeforeUpdate = fichaPartidasTorneosRepository.findAll().size();

        // Update the fichaPartidasTorneos using partial update
        FichaPartidasTorneos partialUpdatedFichaPartidasTorneos = new FichaPartidasTorneos();
        partialUpdatedFichaPartidasTorneos.setId(fichaPartidasTorneos.getId());

        partialUpdatedFichaPartidasTorneos
            .nombreContrincante(UPDATED_NOMBRE_CONTRINCANTE)
            .duracion(UPDATED_DURACION)
            .winner(UPDATED_WINNER)
            .resultado(UPDATED_RESULTADO)
            .comentarios(UPDATED_COMENTARIOS)
            .nombreArbitro(UPDATED_NOMBRE_ARBITRO);

        restFichaPartidasTorneosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFichaPartidasTorneos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFichaPartidasTorneos))
            )
            .andExpect(status().isOk());

        // Validate the FichaPartidasTorneos in the database
        List<FichaPartidasTorneos> fichaPartidasTorneosList = fichaPartidasTorneosRepository.findAll();
        assertThat(fichaPartidasTorneosList).hasSize(databaseSizeBeforeUpdate);
        FichaPartidasTorneos testFichaPartidasTorneos = fichaPartidasTorneosList.get(fichaPartidasTorneosList.size() - 1);
        assertThat(testFichaPartidasTorneos.getNombreContrincante()).isEqualTo(UPDATED_NOMBRE_CONTRINCANTE);
        assertThat(testFichaPartidasTorneos.getDuracion()).isEqualTo(UPDATED_DURACION);
        assertThat(testFichaPartidasTorneos.getWinner()).isEqualTo(UPDATED_WINNER);
        assertThat(testFichaPartidasTorneos.getResultado()).isEqualTo(UPDATED_RESULTADO);
        assertThat(testFichaPartidasTorneos.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testFichaPartidasTorneos.getNombreArbitro()).isEqualTo(UPDATED_NOMBRE_ARBITRO);
    }

    @Test
    @Transactional
    void patchNonExistingFichaPartidasTorneos() throws Exception {
        int databaseSizeBeforeUpdate = fichaPartidasTorneosRepository.findAll().size();
        fichaPartidasTorneos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFichaPartidasTorneosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fichaPartidasTorneos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fichaPartidasTorneos))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichaPartidasTorneos in the database
        List<FichaPartidasTorneos> fichaPartidasTorneosList = fichaPartidasTorneosRepository.findAll();
        assertThat(fichaPartidasTorneosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFichaPartidasTorneos() throws Exception {
        int databaseSizeBeforeUpdate = fichaPartidasTorneosRepository.findAll().size();
        fichaPartidasTorneos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichaPartidasTorneosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fichaPartidasTorneos))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichaPartidasTorneos in the database
        List<FichaPartidasTorneos> fichaPartidasTorneosList = fichaPartidasTorneosRepository.findAll();
        assertThat(fichaPartidasTorneosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFichaPartidasTorneos() throws Exception {
        int databaseSizeBeforeUpdate = fichaPartidasTorneosRepository.findAll().size();
        fichaPartidasTorneos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichaPartidasTorneosMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fichaPartidasTorneos))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FichaPartidasTorneos in the database
        List<FichaPartidasTorneos> fichaPartidasTorneosList = fichaPartidasTorneosRepository.findAll();
        assertThat(fichaPartidasTorneosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFichaPartidasTorneos() throws Exception {
        // Initialize the database
        fichaPartidasTorneosRepository.saveAndFlush(fichaPartidasTorneos);

        int databaseSizeBeforeDelete = fichaPartidasTorneosRepository.findAll().size();

        // Delete the fichaPartidasTorneos
        restFichaPartidasTorneosMockMvc
            .perform(delete(ENTITY_API_URL_ID, fichaPartidasTorneos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FichaPartidasTorneos> fichaPartidasTorneosList = fichaPartidasTorneosRepository.findAll();
        assertThat(fichaPartidasTorneosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
