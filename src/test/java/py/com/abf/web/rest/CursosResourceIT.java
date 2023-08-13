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
import py.com.abf.domain.Cursos;
import py.com.abf.domain.Inscripciones;
import py.com.abf.domain.Temas;
import py.com.abf.domain.enumeration.Niveles;
import py.com.abf.repository.CursosRepository;
import py.com.abf.service.CursosService;
import py.com.abf.service.criteria.CursosCriteria;

/**
 * Integration tests for the {@link CursosResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CursosResourceIT {

    private static final String DEFAULT_NOMBRE_CURSO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CURSO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_INICIO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_FIN = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_CANTIDAD_CLASES = 1;
    private static final Integer UPDATED_CANTIDAD_CLASES = 2;
    private static final Integer SMALLER_CANTIDAD_CLASES = 1 - 1;

    private static final Niveles DEFAULT_NIVEL = Niveles.PREAJEDREZ;
    private static final Niveles UPDATED_NIVEL = Niveles.INICIAL;

    private static final String ENTITY_API_URL = "/api/cursos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CursosRepository cursosRepository;

    @Mock
    private CursosRepository cursosRepositoryMock;

    @Mock
    private CursosService cursosServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCursosMockMvc;

    private Cursos cursos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cursos createEntity(EntityManager em) {
        Cursos cursos = new Cursos()
            .nombreCurso(DEFAULT_NOMBRE_CURSO)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .cantidadClases(DEFAULT_CANTIDAD_CLASES)
            .nivel(DEFAULT_NIVEL);
        // Add required entity
        Temas temas;
        if (TestUtil.findAll(em, Temas.class).isEmpty()) {
            temas = TemasResourceIT.createEntity(em);
            em.persist(temas);
            em.flush();
        } else {
            temas = TestUtil.findAll(em, Temas.class).get(0);
        }
        cursos.setTemas(temas);
        return cursos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cursos createUpdatedEntity(EntityManager em) {
        Cursos cursos = new Cursos()
            .nombreCurso(UPDATED_NOMBRE_CURSO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .cantidadClases(UPDATED_CANTIDAD_CLASES)
            .nivel(UPDATED_NIVEL);
        // Add required entity
        Temas temas;
        if (TestUtil.findAll(em, Temas.class).isEmpty()) {
            temas = TemasResourceIT.createUpdatedEntity(em);
            em.persist(temas);
            em.flush();
        } else {
            temas = TestUtil.findAll(em, Temas.class).get(0);
        }
        cursos.setTemas(temas);
        return cursos;
    }

    @BeforeEach
    public void initTest() {
        cursos = createEntity(em);
    }

    @Test
    @Transactional
    void createCursos() throws Exception {
        int databaseSizeBeforeCreate = cursosRepository.findAll().size();
        // Create the Cursos
        restCursosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cursos)))
            .andExpect(status().isCreated());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeCreate + 1);
        Cursos testCursos = cursosList.get(cursosList.size() - 1);
        assertThat(testCursos.getNombreCurso()).isEqualTo(DEFAULT_NOMBRE_CURSO);
        assertThat(testCursos.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCursos.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testCursos.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testCursos.getCantidadClases()).isEqualTo(DEFAULT_CANTIDAD_CLASES);
        assertThat(testCursos.getNivel()).isEqualTo(DEFAULT_NIVEL);
    }

    @Test
    @Transactional
    void createCursosWithExistingId() throws Exception {
        // Create the Cursos with an existing ID
        cursos.setId(1L);

        int databaseSizeBeforeCreate = cursosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCursosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cursos)))
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreCursoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cursosRepository.findAll().size();
        // set the field null
        cursos.setNombreCurso(null);

        // Create the Cursos, which fails.

        restCursosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cursos)))
            .andExpect(status().isBadRequest());

        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cursosRepository.findAll().size();
        // set the field null
        cursos.setDescripcion(null);

        // Create the Cursos, which fails.

        restCursosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cursos)))
            .andExpect(status().isBadRequest());

        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNivelIsRequired() throws Exception {
        int databaseSizeBeforeTest = cursosRepository.findAll().size();
        // set the field null
        cursos.setNivel(null);

        // Create the Cursos, which fails.

        restCursosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cursos)))
            .andExpect(status().isBadRequest());

        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCursos() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList
        restCursosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cursos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreCurso").value(hasItem(DEFAULT_NOMBRE_CURSO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].cantidadClases").value(hasItem(DEFAULT_CANTIDAD_CLASES)))
            .andExpect(jsonPath("$.[*].nivel").value(hasItem(DEFAULT_NIVEL.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCursosWithEagerRelationshipsIsEnabled() throws Exception {
        when(cursosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCursosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cursosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCursosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cursosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCursosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cursosRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCursos() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get the cursos
        restCursosMockMvc
            .perform(get(ENTITY_API_URL_ID, cursos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cursos.getId().intValue()))
            .andExpect(jsonPath("$.nombreCurso").value(DEFAULT_NOMBRE_CURSO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.cantidadClases").value(DEFAULT_CANTIDAD_CLASES))
            .andExpect(jsonPath("$.nivel").value(DEFAULT_NIVEL.toString()));
    }

    @Test
    @Transactional
    void getCursosByIdFiltering() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        Long id = cursos.getId();

        defaultCursosShouldBeFound("id.equals=" + id);
        defaultCursosShouldNotBeFound("id.notEquals=" + id);

        defaultCursosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCursosShouldNotBeFound("id.greaterThan=" + id);

        defaultCursosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCursosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCursosByNombreCursoIsEqualToSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where nombreCurso equals to DEFAULT_NOMBRE_CURSO
        defaultCursosShouldBeFound("nombreCurso.equals=" + DEFAULT_NOMBRE_CURSO);

        // Get all the cursosList where nombreCurso equals to UPDATED_NOMBRE_CURSO
        defaultCursosShouldNotBeFound("nombreCurso.equals=" + UPDATED_NOMBRE_CURSO);
    }

    @Test
    @Transactional
    void getAllCursosByNombreCursoIsInShouldWork() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where nombreCurso in DEFAULT_NOMBRE_CURSO or UPDATED_NOMBRE_CURSO
        defaultCursosShouldBeFound("nombreCurso.in=" + DEFAULT_NOMBRE_CURSO + "," + UPDATED_NOMBRE_CURSO);

        // Get all the cursosList where nombreCurso equals to UPDATED_NOMBRE_CURSO
        defaultCursosShouldNotBeFound("nombreCurso.in=" + UPDATED_NOMBRE_CURSO);
    }

    @Test
    @Transactional
    void getAllCursosByNombreCursoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where nombreCurso is not null
        defaultCursosShouldBeFound("nombreCurso.specified=true");

        // Get all the cursosList where nombreCurso is null
        defaultCursosShouldNotBeFound("nombreCurso.specified=false");
    }

    @Test
    @Transactional
    void getAllCursosByNombreCursoContainsSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where nombreCurso contains DEFAULT_NOMBRE_CURSO
        defaultCursosShouldBeFound("nombreCurso.contains=" + DEFAULT_NOMBRE_CURSO);

        // Get all the cursosList where nombreCurso contains UPDATED_NOMBRE_CURSO
        defaultCursosShouldNotBeFound("nombreCurso.contains=" + UPDATED_NOMBRE_CURSO);
    }

    @Test
    @Transactional
    void getAllCursosByNombreCursoNotContainsSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where nombreCurso does not contain DEFAULT_NOMBRE_CURSO
        defaultCursosShouldNotBeFound("nombreCurso.doesNotContain=" + DEFAULT_NOMBRE_CURSO);

        // Get all the cursosList where nombreCurso does not contain UPDATED_NOMBRE_CURSO
        defaultCursosShouldBeFound("nombreCurso.doesNotContain=" + UPDATED_NOMBRE_CURSO);
    }

    @Test
    @Transactional
    void getAllCursosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where descripcion equals to DEFAULT_DESCRIPCION
        defaultCursosShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the cursosList where descripcion equals to UPDATED_DESCRIPCION
        defaultCursosShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCursosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultCursosShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the cursosList where descripcion equals to UPDATED_DESCRIPCION
        defaultCursosShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCursosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where descripcion is not null
        defaultCursosShouldBeFound("descripcion.specified=true");

        // Get all the cursosList where descripcion is null
        defaultCursosShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllCursosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where descripcion contains DEFAULT_DESCRIPCION
        defaultCursosShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the cursosList where descripcion contains UPDATED_DESCRIPCION
        defaultCursosShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCursosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultCursosShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the cursosList where descripcion does not contain UPDATED_DESCRIPCION
        defaultCursosShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaInicio equals to DEFAULT_FECHA_INICIO
        defaultCursosShouldBeFound("fechaInicio.equals=" + DEFAULT_FECHA_INICIO);

        // Get all the cursosList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultCursosShouldNotBeFound("fechaInicio.equals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsInShouldWork() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaInicio in DEFAULT_FECHA_INICIO or UPDATED_FECHA_INICIO
        defaultCursosShouldBeFound("fechaInicio.in=" + DEFAULT_FECHA_INICIO + "," + UPDATED_FECHA_INICIO);

        // Get all the cursosList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultCursosShouldNotBeFound("fechaInicio.in=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaInicio is not null
        defaultCursosShouldBeFound("fechaInicio.specified=true");

        // Get all the cursosList where fechaInicio is null
        defaultCursosShouldNotBeFound("fechaInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaInicio is greater than or equal to DEFAULT_FECHA_INICIO
        defaultCursosShouldBeFound("fechaInicio.greaterThanOrEqual=" + DEFAULT_FECHA_INICIO);

        // Get all the cursosList where fechaInicio is greater than or equal to UPDATED_FECHA_INICIO
        defaultCursosShouldNotBeFound("fechaInicio.greaterThanOrEqual=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaInicio is less than or equal to DEFAULT_FECHA_INICIO
        defaultCursosShouldBeFound("fechaInicio.lessThanOrEqual=" + DEFAULT_FECHA_INICIO);

        // Get all the cursosList where fechaInicio is less than or equal to SMALLER_FECHA_INICIO
        defaultCursosShouldNotBeFound("fechaInicio.lessThanOrEqual=" + SMALLER_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaInicio is less than DEFAULT_FECHA_INICIO
        defaultCursosShouldNotBeFound("fechaInicio.lessThan=" + DEFAULT_FECHA_INICIO);

        // Get all the cursosList where fechaInicio is less than UPDATED_FECHA_INICIO
        defaultCursosShouldBeFound("fechaInicio.lessThan=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaInicio is greater than DEFAULT_FECHA_INICIO
        defaultCursosShouldNotBeFound("fechaInicio.greaterThan=" + DEFAULT_FECHA_INICIO);

        // Get all the cursosList where fechaInicio is greater than SMALLER_FECHA_INICIO
        defaultCursosShouldBeFound("fechaInicio.greaterThan=" + SMALLER_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsEqualToSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaFin equals to DEFAULT_FECHA_FIN
        defaultCursosShouldBeFound("fechaFin.equals=" + DEFAULT_FECHA_FIN);

        // Get all the cursosList where fechaFin equals to UPDATED_FECHA_FIN
        defaultCursosShouldNotBeFound("fechaFin.equals=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsInShouldWork() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaFin in DEFAULT_FECHA_FIN or UPDATED_FECHA_FIN
        defaultCursosShouldBeFound("fechaFin.in=" + DEFAULT_FECHA_FIN + "," + UPDATED_FECHA_FIN);

        // Get all the cursosList where fechaFin equals to UPDATED_FECHA_FIN
        defaultCursosShouldNotBeFound("fechaFin.in=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaFin is not null
        defaultCursosShouldBeFound("fechaFin.specified=true");

        // Get all the cursosList where fechaFin is null
        defaultCursosShouldNotBeFound("fechaFin.specified=false");
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaFin is greater than or equal to DEFAULT_FECHA_FIN
        defaultCursosShouldBeFound("fechaFin.greaterThanOrEqual=" + DEFAULT_FECHA_FIN);

        // Get all the cursosList where fechaFin is greater than or equal to UPDATED_FECHA_FIN
        defaultCursosShouldNotBeFound("fechaFin.greaterThanOrEqual=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaFin is less than or equal to DEFAULT_FECHA_FIN
        defaultCursosShouldBeFound("fechaFin.lessThanOrEqual=" + DEFAULT_FECHA_FIN);

        // Get all the cursosList where fechaFin is less than or equal to SMALLER_FECHA_FIN
        defaultCursosShouldNotBeFound("fechaFin.lessThanOrEqual=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsLessThanSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaFin is less than DEFAULT_FECHA_FIN
        defaultCursosShouldNotBeFound("fechaFin.lessThan=" + DEFAULT_FECHA_FIN);

        // Get all the cursosList where fechaFin is less than UPDATED_FECHA_FIN
        defaultCursosShouldBeFound("fechaFin.lessThan=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where fechaFin is greater than DEFAULT_FECHA_FIN
        defaultCursosShouldNotBeFound("fechaFin.greaterThan=" + DEFAULT_FECHA_FIN);

        // Get all the cursosList where fechaFin is greater than SMALLER_FECHA_FIN
        defaultCursosShouldBeFound("fechaFin.greaterThan=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllCursosByCantidadClasesIsEqualToSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where cantidadClases equals to DEFAULT_CANTIDAD_CLASES
        defaultCursosShouldBeFound("cantidadClases.equals=" + DEFAULT_CANTIDAD_CLASES);

        // Get all the cursosList where cantidadClases equals to UPDATED_CANTIDAD_CLASES
        defaultCursosShouldNotBeFound("cantidadClases.equals=" + UPDATED_CANTIDAD_CLASES);
    }

    @Test
    @Transactional
    void getAllCursosByCantidadClasesIsInShouldWork() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where cantidadClases in DEFAULT_CANTIDAD_CLASES or UPDATED_CANTIDAD_CLASES
        defaultCursosShouldBeFound("cantidadClases.in=" + DEFAULT_CANTIDAD_CLASES + "," + UPDATED_CANTIDAD_CLASES);

        // Get all the cursosList where cantidadClases equals to UPDATED_CANTIDAD_CLASES
        defaultCursosShouldNotBeFound("cantidadClases.in=" + UPDATED_CANTIDAD_CLASES);
    }

    @Test
    @Transactional
    void getAllCursosByCantidadClasesIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where cantidadClases is not null
        defaultCursosShouldBeFound("cantidadClases.specified=true");

        // Get all the cursosList where cantidadClases is null
        defaultCursosShouldNotBeFound("cantidadClases.specified=false");
    }

    @Test
    @Transactional
    void getAllCursosByCantidadClasesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where cantidadClases is greater than or equal to DEFAULT_CANTIDAD_CLASES
        defaultCursosShouldBeFound("cantidadClases.greaterThanOrEqual=" + DEFAULT_CANTIDAD_CLASES);

        // Get all the cursosList where cantidadClases is greater than or equal to UPDATED_CANTIDAD_CLASES
        defaultCursosShouldNotBeFound("cantidadClases.greaterThanOrEqual=" + UPDATED_CANTIDAD_CLASES);
    }

    @Test
    @Transactional
    void getAllCursosByCantidadClasesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where cantidadClases is less than or equal to DEFAULT_CANTIDAD_CLASES
        defaultCursosShouldBeFound("cantidadClases.lessThanOrEqual=" + DEFAULT_CANTIDAD_CLASES);

        // Get all the cursosList where cantidadClases is less than or equal to SMALLER_CANTIDAD_CLASES
        defaultCursosShouldNotBeFound("cantidadClases.lessThanOrEqual=" + SMALLER_CANTIDAD_CLASES);
    }

    @Test
    @Transactional
    void getAllCursosByCantidadClasesIsLessThanSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where cantidadClases is less than DEFAULT_CANTIDAD_CLASES
        defaultCursosShouldNotBeFound("cantidadClases.lessThan=" + DEFAULT_CANTIDAD_CLASES);

        // Get all the cursosList where cantidadClases is less than UPDATED_CANTIDAD_CLASES
        defaultCursosShouldBeFound("cantidadClases.lessThan=" + UPDATED_CANTIDAD_CLASES);
    }

    @Test
    @Transactional
    void getAllCursosByCantidadClasesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where cantidadClases is greater than DEFAULT_CANTIDAD_CLASES
        defaultCursosShouldNotBeFound("cantidadClases.greaterThan=" + DEFAULT_CANTIDAD_CLASES);

        // Get all the cursosList where cantidadClases is greater than SMALLER_CANTIDAD_CLASES
        defaultCursosShouldBeFound("cantidadClases.greaterThan=" + SMALLER_CANTIDAD_CLASES);
    }

    @Test
    @Transactional
    void getAllCursosByNivelIsEqualToSomething() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where nivel equals to DEFAULT_NIVEL
        defaultCursosShouldBeFound("nivel.equals=" + DEFAULT_NIVEL);

        // Get all the cursosList where nivel equals to UPDATED_NIVEL
        defaultCursosShouldNotBeFound("nivel.equals=" + UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void getAllCursosByNivelIsInShouldWork() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where nivel in DEFAULT_NIVEL or UPDATED_NIVEL
        defaultCursosShouldBeFound("nivel.in=" + DEFAULT_NIVEL + "," + UPDATED_NIVEL);

        // Get all the cursosList where nivel equals to UPDATED_NIVEL
        defaultCursosShouldNotBeFound("nivel.in=" + UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void getAllCursosByNivelIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList where nivel is not null
        defaultCursosShouldBeFound("nivel.specified=true");

        // Get all the cursosList where nivel is null
        defaultCursosShouldNotBeFound("nivel.specified=false");
    }

    @Test
    @Transactional
    void getAllCursosByInscripcionesIsEqualToSomething() throws Exception {
        Inscripciones inscripciones;
        if (TestUtil.findAll(em, Inscripciones.class).isEmpty()) {
            cursosRepository.saveAndFlush(cursos);
            inscripciones = InscripcionesResourceIT.createEntity(em);
        } else {
            inscripciones = TestUtil.findAll(em, Inscripciones.class).get(0);
        }
        em.persist(inscripciones);
        em.flush();
        cursos.addInscripciones(inscripciones);
        cursosRepository.saveAndFlush(cursos);
        Long inscripcionesId = inscripciones.getId();

        // Get all the cursosList where inscripciones equals to inscripcionesId
        defaultCursosShouldBeFound("inscripcionesId.equals=" + inscripcionesId);

        // Get all the cursosList where inscripciones equals to (inscripcionesId + 1)
        defaultCursosShouldNotBeFound("inscripcionesId.equals=" + (inscripcionesId + 1));
    }

    @Test
    @Transactional
    void getAllCursosByTemasIsEqualToSomething() throws Exception {
        Temas temas;
        if (TestUtil.findAll(em, Temas.class).isEmpty()) {
            cursosRepository.saveAndFlush(cursos);
            temas = TemasResourceIT.createEntity(em);
        } else {
            temas = TestUtil.findAll(em, Temas.class).get(0);
        }
        em.persist(temas);
        em.flush();
        cursos.setTemas(temas);
        cursosRepository.saveAndFlush(cursos);
        Long temasId = temas.getId();

        // Get all the cursosList where temas equals to temasId
        defaultCursosShouldBeFound("temasId.equals=" + temasId);

        // Get all the cursosList where temas equals to (temasId + 1)
        defaultCursosShouldNotBeFound("temasId.equals=" + (temasId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCursosShouldBeFound(String filter) throws Exception {
        restCursosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cursos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreCurso").value(hasItem(DEFAULT_NOMBRE_CURSO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].cantidadClases").value(hasItem(DEFAULT_CANTIDAD_CLASES)))
            .andExpect(jsonPath("$.[*].nivel").value(hasItem(DEFAULT_NIVEL.toString())));

        // Check, that the count call also returns 1
        restCursosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCursosShouldNotBeFound(String filter) throws Exception {
        restCursosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCursosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCursos() throws Exception {
        // Get the cursos
        restCursosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCursos() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();

        // Update the cursos
        Cursos updatedCursos = cursosRepository.findById(cursos.getId()).get();
        // Disconnect from session so that the updates on updatedCursos are not directly saved in db
        em.detach(updatedCursos);
        updatedCursos
            .nombreCurso(UPDATED_NOMBRE_CURSO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .cantidadClases(UPDATED_CANTIDAD_CLASES)
            .nivel(UPDATED_NIVEL);

        restCursosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCursos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCursos))
            )
            .andExpect(status().isOk());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
        Cursos testCursos = cursosList.get(cursosList.size() - 1);
        assertThat(testCursos.getNombreCurso()).isEqualTo(UPDATED_NOMBRE_CURSO);
        assertThat(testCursos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCursos.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testCursos.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testCursos.getCantidadClases()).isEqualTo(UPDATED_CANTIDAD_CLASES);
        assertThat(testCursos.getNivel()).isEqualTo(UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void putNonExistingCursos() throws Exception {
        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();
        cursos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cursos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cursos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCursos() throws Exception {
        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();
        cursos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cursos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCursos() throws Exception {
        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();
        cursos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cursos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCursosWithPatch() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();

        // Update the cursos using partial update
        Cursos partialUpdatedCursos = new Cursos();
        partialUpdatedCursos.setId(cursos.getId());

        partialUpdatedCursos.fechaFin(UPDATED_FECHA_FIN).cantidadClases(UPDATED_CANTIDAD_CLASES).nivel(UPDATED_NIVEL);

        restCursosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCursos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCursos))
            )
            .andExpect(status().isOk());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
        Cursos testCursos = cursosList.get(cursosList.size() - 1);
        assertThat(testCursos.getNombreCurso()).isEqualTo(DEFAULT_NOMBRE_CURSO);
        assertThat(testCursos.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCursos.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testCursos.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testCursos.getCantidadClases()).isEqualTo(UPDATED_CANTIDAD_CLASES);
        assertThat(testCursos.getNivel()).isEqualTo(UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void fullUpdateCursosWithPatch() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();

        // Update the cursos using partial update
        Cursos partialUpdatedCursos = new Cursos();
        partialUpdatedCursos.setId(cursos.getId());

        partialUpdatedCursos
            .nombreCurso(UPDATED_NOMBRE_CURSO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .cantidadClases(UPDATED_CANTIDAD_CLASES)
            .nivel(UPDATED_NIVEL);

        restCursosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCursos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCursos))
            )
            .andExpect(status().isOk());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
        Cursos testCursos = cursosList.get(cursosList.size() - 1);
        assertThat(testCursos.getNombreCurso()).isEqualTo(UPDATED_NOMBRE_CURSO);
        assertThat(testCursos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCursos.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testCursos.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testCursos.getCantidadClases()).isEqualTo(UPDATED_CANTIDAD_CLASES);
        assertThat(testCursos.getNivel()).isEqualTo(UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void patchNonExistingCursos() throws Exception {
        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();
        cursos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cursos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cursos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCursos() throws Exception {
        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();
        cursos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cursos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCursos() throws Exception {
        int databaseSizeBeforeUpdate = cursosRepository.findAll().size();
        cursos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cursos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cursos in the database
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCursos() throws Exception {
        // Initialize the database
        cursosRepository.saveAndFlush(cursos);

        int databaseSizeBeforeDelete = cursosRepository.findAll().size();

        // Delete the cursos
        restCursosMockMvc
            .perform(delete(ENTITY_API_URL_ID, cursos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cursos> cursosList = cursosRepository.findAll();
        assertThat(cursosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
