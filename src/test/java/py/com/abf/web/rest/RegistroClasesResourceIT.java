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
import py.com.abf.domain.Funcionarios;
import py.com.abf.domain.RegistroClases;
import py.com.abf.domain.Temas;
import py.com.abf.repository.RegistroClasesRepository;
import py.com.abf.service.RegistroClasesService;
import py.com.abf.service.criteria.RegistroClasesCriteria;

/**
 * Integration tests for the {@link RegistroClasesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RegistroClasesResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_CANTIDAD_HORAS = 1;
    private static final Integer UPDATED_CANTIDAD_HORAS = 2;
    private static final Integer SMALLER_CANTIDAD_HORAS = 1 - 1;

    private static final Boolean DEFAULT_ASISTENCIA_ALUMNO = false;
    private static final Boolean UPDATED_ASISTENCIA_ALUMNO = true;

    private static final String ENTITY_API_URL = "/api/registro-clases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RegistroClasesRepository registroClasesRepository;

    @Mock
    private RegistroClasesRepository registroClasesRepositoryMock;

    @Mock
    private RegistroClasesService registroClasesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegistroClasesMockMvc;

    private RegistroClases registroClases;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegistroClases createEntity(EntityManager em) {
        RegistroClases registroClases = new RegistroClases()
            .fecha(DEFAULT_FECHA)
            .cantidadHoras(DEFAULT_CANTIDAD_HORAS)
            .asistenciaAlumno(DEFAULT_ASISTENCIA_ALUMNO);
        // Add required entity
        Temas temas;
        if (TestUtil.findAll(em, Temas.class).isEmpty()) {
            temas = TemasResourceIT.createEntity(em);
            em.persist(temas);
            em.flush();
        } else {
            temas = TestUtil.findAll(em, Temas.class).get(0);
        }
        registroClases.setTemas(temas);
        // Add required entity
        Funcionarios funcionarios;
        if (TestUtil.findAll(em, Funcionarios.class).isEmpty()) {
            funcionarios = FuncionariosResourceIT.createEntity(em);
            em.persist(funcionarios);
            em.flush();
        } else {
            funcionarios = TestUtil.findAll(em, Funcionarios.class).get(0);
        }
        registroClases.setFuncionario(funcionarios);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        registroClases.setAlumnos(alumnos);
        return registroClases;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegistroClases createUpdatedEntity(EntityManager em) {
        RegistroClases registroClases = new RegistroClases()
            .fecha(UPDATED_FECHA)
            .cantidadHoras(UPDATED_CANTIDAD_HORAS)
            .asistenciaAlumno(UPDATED_ASISTENCIA_ALUMNO);
        // Add required entity
        Temas temas;
        if (TestUtil.findAll(em, Temas.class).isEmpty()) {
            temas = TemasResourceIT.createUpdatedEntity(em);
            em.persist(temas);
            em.flush();
        } else {
            temas = TestUtil.findAll(em, Temas.class).get(0);
        }
        registroClases.setTemas(temas);
        // Add required entity
        Funcionarios funcionarios;
        if (TestUtil.findAll(em, Funcionarios.class).isEmpty()) {
            funcionarios = FuncionariosResourceIT.createUpdatedEntity(em);
            em.persist(funcionarios);
            em.flush();
        } else {
            funcionarios = TestUtil.findAll(em, Funcionarios.class).get(0);
        }
        registroClases.setFuncionario(funcionarios);
        // Add required entity
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            alumnos = AlumnosResourceIT.createUpdatedEntity(em);
            em.persist(alumnos);
            em.flush();
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        registroClases.setAlumnos(alumnos);
        return registroClases;
    }

    @BeforeEach
    public void initTest() {
        registroClases = createEntity(em);
    }

    @Test
    @Transactional
    void createRegistroClases() throws Exception {
        int databaseSizeBeforeCreate = registroClasesRepository.findAll().size();
        // Create the RegistroClases
        restRegistroClasesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registroClases))
            )
            .andExpect(status().isCreated());

        // Validate the RegistroClases in the database
        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeCreate + 1);
        RegistroClases testRegistroClases = registroClasesList.get(registroClasesList.size() - 1);
        assertThat(testRegistroClases.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testRegistroClases.getCantidadHoras()).isEqualTo(DEFAULT_CANTIDAD_HORAS);
        assertThat(testRegistroClases.getAsistenciaAlumno()).isEqualTo(DEFAULT_ASISTENCIA_ALUMNO);
    }

    @Test
    @Transactional
    void createRegistroClasesWithExistingId() throws Exception {
        // Create the RegistroClases with an existing ID
        registroClases.setId(1L);

        int databaseSizeBeforeCreate = registroClasesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistroClasesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registroClases))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroClases in the database
        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = registroClasesRepository.findAll().size();
        // set the field null
        registroClases.setFecha(null);

        // Create the RegistroClases, which fails.

        restRegistroClasesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registroClases))
            )
            .andExpect(status().isBadRequest());

        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadHorasIsRequired() throws Exception {
        int databaseSizeBeforeTest = registroClasesRepository.findAll().size();
        // set the field null
        registroClases.setCantidadHoras(null);

        // Create the RegistroClases, which fails.

        restRegistroClasesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registroClases))
            )
            .andExpect(status().isBadRequest());

        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRegistroClases() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList
        restRegistroClasesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registroClases.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].cantidadHoras").value(hasItem(DEFAULT_CANTIDAD_HORAS)))
            .andExpect(jsonPath("$.[*].asistenciaAlumno").value(hasItem(DEFAULT_ASISTENCIA_ALUMNO.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRegistroClasesWithEagerRelationshipsIsEnabled() throws Exception {
        when(registroClasesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRegistroClasesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(registroClasesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRegistroClasesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(registroClasesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRegistroClasesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(registroClasesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRegistroClases() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get the registroClases
        restRegistroClasesMockMvc
            .perform(get(ENTITY_API_URL_ID, registroClases.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(registroClases.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.cantidadHoras").value(DEFAULT_CANTIDAD_HORAS))
            .andExpect(jsonPath("$.asistenciaAlumno").value(DEFAULT_ASISTENCIA_ALUMNO.booleanValue()));
    }

    @Test
    @Transactional
    void getRegistroClasesByIdFiltering() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        Long id = registroClases.getId();

        defaultRegistroClasesShouldBeFound("id.equals=" + id);
        defaultRegistroClasesShouldNotBeFound("id.notEquals=" + id);

        defaultRegistroClasesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRegistroClasesShouldNotBeFound("id.greaterThan=" + id);

        defaultRegistroClasesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRegistroClasesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where fecha equals to DEFAULT_FECHA
        defaultRegistroClasesShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the registroClasesList where fecha equals to UPDATED_FECHA
        defaultRegistroClasesShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultRegistroClasesShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the registroClasesList where fecha equals to UPDATED_FECHA
        defaultRegistroClasesShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where fecha is not null
        defaultRegistroClasesShouldBeFound("fecha.specified=true");

        // Get all the registroClasesList where fecha is null
        defaultRegistroClasesShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistroClasesByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where fecha is greater than or equal to DEFAULT_FECHA
        defaultRegistroClasesShouldBeFound("fecha.greaterThanOrEqual=" + DEFAULT_FECHA);

        // Get all the registroClasesList where fecha is greater than or equal to UPDATED_FECHA
        defaultRegistroClasesShouldNotBeFound("fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where fecha is less than or equal to DEFAULT_FECHA
        defaultRegistroClasesShouldBeFound("fecha.lessThanOrEqual=" + DEFAULT_FECHA);

        // Get all the registroClasesList where fecha is less than or equal to SMALLER_FECHA
        defaultRegistroClasesShouldNotBeFound("fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where fecha is less than DEFAULT_FECHA
        defaultRegistroClasesShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the registroClasesList where fecha is less than UPDATED_FECHA
        defaultRegistroClasesShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where fecha is greater than DEFAULT_FECHA
        defaultRegistroClasesShouldNotBeFound("fecha.greaterThan=" + DEFAULT_FECHA);

        // Get all the registroClasesList where fecha is greater than SMALLER_FECHA
        defaultRegistroClasesShouldBeFound("fecha.greaterThan=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByCantidadHorasIsEqualToSomething() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where cantidadHoras equals to DEFAULT_CANTIDAD_HORAS
        defaultRegistroClasesShouldBeFound("cantidadHoras.equals=" + DEFAULT_CANTIDAD_HORAS);

        // Get all the registroClasesList where cantidadHoras equals to UPDATED_CANTIDAD_HORAS
        defaultRegistroClasesShouldNotBeFound("cantidadHoras.equals=" + UPDATED_CANTIDAD_HORAS);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByCantidadHorasIsInShouldWork() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where cantidadHoras in DEFAULT_CANTIDAD_HORAS or UPDATED_CANTIDAD_HORAS
        defaultRegistroClasesShouldBeFound("cantidadHoras.in=" + DEFAULT_CANTIDAD_HORAS + "," + UPDATED_CANTIDAD_HORAS);

        // Get all the registroClasesList where cantidadHoras equals to UPDATED_CANTIDAD_HORAS
        defaultRegistroClasesShouldNotBeFound("cantidadHoras.in=" + UPDATED_CANTIDAD_HORAS);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByCantidadHorasIsNullOrNotNull() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where cantidadHoras is not null
        defaultRegistroClasesShouldBeFound("cantidadHoras.specified=true");

        // Get all the registroClasesList where cantidadHoras is null
        defaultRegistroClasesShouldNotBeFound("cantidadHoras.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistroClasesByCantidadHorasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where cantidadHoras is greater than or equal to DEFAULT_CANTIDAD_HORAS
        defaultRegistroClasesShouldBeFound("cantidadHoras.greaterThanOrEqual=" + DEFAULT_CANTIDAD_HORAS);

        // Get all the registroClasesList where cantidadHoras is greater than or equal to UPDATED_CANTIDAD_HORAS
        defaultRegistroClasesShouldNotBeFound("cantidadHoras.greaterThanOrEqual=" + UPDATED_CANTIDAD_HORAS);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByCantidadHorasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where cantidadHoras is less than or equal to DEFAULT_CANTIDAD_HORAS
        defaultRegistroClasesShouldBeFound("cantidadHoras.lessThanOrEqual=" + DEFAULT_CANTIDAD_HORAS);

        // Get all the registroClasesList where cantidadHoras is less than or equal to SMALLER_CANTIDAD_HORAS
        defaultRegistroClasesShouldNotBeFound("cantidadHoras.lessThanOrEqual=" + SMALLER_CANTIDAD_HORAS);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByCantidadHorasIsLessThanSomething() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where cantidadHoras is less than DEFAULT_CANTIDAD_HORAS
        defaultRegistroClasesShouldNotBeFound("cantidadHoras.lessThan=" + DEFAULT_CANTIDAD_HORAS);

        // Get all the registroClasesList where cantidadHoras is less than UPDATED_CANTIDAD_HORAS
        defaultRegistroClasesShouldBeFound("cantidadHoras.lessThan=" + UPDATED_CANTIDAD_HORAS);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByCantidadHorasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where cantidadHoras is greater than DEFAULT_CANTIDAD_HORAS
        defaultRegistroClasesShouldNotBeFound("cantidadHoras.greaterThan=" + DEFAULT_CANTIDAD_HORAS);

        // Get all the registroClasesList where cantidadHoras is greater than SMALLER_CANTIDAD_HORAS
        defaultRegistroClasesShouldBeFound("cantidadHoras.greaterThan=" + SMALLER_CANTIDAD_HORAS);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByAsistenciaAlumnoIsEqualToSomething() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where asistenciaAlumno equals to DEFAULT_ASISTENCIA_ALUMNO
        defaultRegistroClasesShouldBeFound("asistenciaAlumno.equals=" + DEFAULT_ASISTENCIA_ALUMNO);

        // Get all the registroClasesList where asistenciaAlumno equals to UPDATED_ASISTENCIA_ALUMNO
        defaultRegistroClasesShouldNotBeFound("asistenciaAlumno.equals=" + UPDATED_ASISTENCIA_ALUMNO);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByAsistenciaAlumnoIsInShouldWork() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where asistenciaAlumno in DEFAULT_ASISTENCIA_ALUMNO or UPDATED_ASISTENCIA_ALUMNO
        defaultRegistroClasesShouldBeFound("asistenciaAlumno.in=" + DEFAULT_ASISTENCIA_ALUMNO + "," + UPDATED_ASISTENCIA_ALUMNO);

        // Get all the registroClasesList where asistenciaAlumno equals to UPDATED_ASISTENCIA_ALUMNO
        defaultRegistroClasesShouldNotBeFound("asistenciaAlumno.in=" + UPDATED_ASISTENCIA_ALUMNO);
    }

    @Test
    @Transactional
    void getAllRegistroClasesByAsistenciaAlumnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        // Get all the registroClasesList where asistenciaAlumno is not null
        defaultRegistroClasesShouldBeFound("asistenciaAlumno.specified=true");

        // Get all the registroClasesList where asistenciaAlumno is null
        defaultRegistroClasesShouldNotBeFound("asistenciaAlumno.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistroClasesByTemasIsEqualToSomething() throws Exception {
        Temas temas;
        if (TestUtil.findAll(em, Temas.class).isEmpty()) {
            registroClasesRepository.saveAndFlush(registroClases);
            temas = TemasResourceIT.createEntity(em);
        } else {
            temas = TestUtil.findAll(em, Temas.class).get(0);
        }
        em.persist(temas);
        em.flush();
        registroClases.setTemas(temas);
        registroClasesRepository.saveAndFlush(registroClases);
        Long temasId = temas.getId();

        // Get all the registroClasesList where temas equals to temasId
        defaultRegistroClasesShouldBeFound("temasId.equals=" + temasId);

        // Get all the registroClasesList where temas equals to (temasId + 1)
        defaultRegistroClasesShouldNotBeFound("temasId.equals=" + (temasId + 1));
    }

    @Test
    @Transactional
    void getAllRegistroClasesByFuncionarioIsEqualToSomething() throws Exception {
        Funcionarios funcionario;
        if (TestUtil.findAll(em, Funcionarios.class).isEmpty()) {
            registroClasesRepository.saveAndFlush(registroClases);
            funcionario = FuncionariosResourceIT.createEntity(em);
        } else {
            funcionario = TestUtil.findAll(em, Funcionarios.class).get(0);
        }
        em.persist(funcionario);
        em.flush();
        registroClases.setFuncionario(funcionario);
        registroClasesRepository.saveAndFlush(registroClases);
        Long funcionarioId = funcionario.getId();

        // Get all the registroClasesList where funcionario equals to funcionarioId
        defaultRegistroClasesShouldBeFound("funcionarioId.equals=" + funcionarioId);

        // Get all the registroClasesList where funcionario equals to (funcionarioId + 1)
        defaultRegistroClasesShouldNotBeFound("funcionarioId.equals=" + (funcionarioId + 1));
    }

    @Test
    @Transactional
    void getAllRegistroClasesByAlumnosIsEqualToSomething() throws Exception {
        Alumnos alumnos;
        if (TestUtil.findAll(em, Alumnos.class).isEmpty()) {
            registroClasesRepository.saveAndFlush(registroClases);
            alumnos = AlumnosResourceIT.createEntity(em);
        } else {
            alumnos = TestUtil.findAll(em, Alumnos.class).get(0);
        }
        em.persist(alumnos);
        em.flush();
        registroClases.setAlumnos(alumnos);
        registroClasesRepository.saveAndFlush(registroClases);
        Long alumnosId = alumnos.getId();

        // Get all the registroClasesList where alumnos equals to alumnosId
        defaultRegistroClasesShouldBeFound("alumnosId.equals=" + alumnosId);

        // Get all the registroClasesList where alumnos equals to (alumnosId + 1)
        defaultRegistroClasesShouldNotBeFound("alumnosId.equals=" + (alumnosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRegistroClasesShouldBeFound(String filter) throws Exception {
        restRegistroClasesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registroClases.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].cantidadHoras").value(hasItem(DEFAULT_CANTIDAD_HORAS)))
            .andExpect(jsonPath("$.[*].asistenciaAlumno").value(hasItem(DEFAULT_ASISTENCIA_ALUMNO.booleanValue())));

        // Check, that the count call also returns 1
        restRegistroClasesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRegistroClasesShouldNotBeFound(String filter) throws Exception {
        restRegistroClasesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegistroClasesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRegistroClases() throws Exception {
        // Get the registroClases
        restRegistroClasesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRegistroClases() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        int databaseSizeBeforeUpdate = registroClasesRepository.findAll().size();

        // Update the registroClases
        RegistroClases updatedRegistroClases = registroClasesRepository.findById(registroClases.getId()).get();
        // Disconnect from session so that the updates on updatedRegistroClases are not directly saved in db
        em.detach(updatedRegistroClases);
        updatedRegistroClases.fecha(UPDATED_FECHA).cantidadHoras(UPDATED_CANTIDAD_HORAS).asistenciaAlumno(UPDATED_ASISTENCIA_ALUMNO);

        restRegistroClasesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRegistroClases.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRegistroClases))
            )
            .andExpect(status().isOk());

        // Validate the RegistroClases in the database
        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeUpdate);
        RegistroClases testRegistroClases = registroClasesList.get(registroClasesList.size() - 1);
        assertThat(testRegistroClases.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testRegistroClases.getCantidadHoras()).isEqualTo(UPDATED_CANTIDAD_HORAS);
        assertThat(testRegistroClases.getAsistenciaAlumno()).isEqualTo(UPDATED_ASISTENCIA_ALUMNO);
    }

    @Test
    @Transactional
    void putNonExistingRegistroClases() throws Exception {
        int databaseSizeBeforeUpdate = registroClasesRepository.findAll().size();
        registroClases.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistroClasesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, registroClases.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroClases))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroClases in the database
        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegistroClases() throws Exception {
        int databaseSizeBeforeUpdate = registroClasesRepository.findAll().size();
        registroClases.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroClasesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroClases))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroClases in the database
        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegistroClases() throws Exception {
        int databaseSizeBeforeUpdate = registroClasesRepository.findAll().size();
        registroClases.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroClasesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registroClases)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RegistroClases in the database
        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegistroClasesWithPatch() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        int databaseSizeBeforeUpdate = registroClasesRepository.findAll().size();

        // Update the registroClases using partial update
        RegistroClases partialUpdatedRegistroClases = new RegistroClases();
        partialUpdatedRegistroClases.setId(registroClases.getId());

        partialUpdatedRegistroClases.cantidadHoras(UPDATED_CANTIDAD_HORAS);

        restRegistroClasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistroClases.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegistroClases))
            )
            .andExpect(status().isOk());

        // Validate the RegistroClases in the database
        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeUpdate);
        RegistroClases testRegistroClases = registroClasesList.get(registroClasesList.size() - 1);
        assertThat(testRegistroClases.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testRegistroClases.getCantidadHoras()).isEqualTo(UPDATED_CANTIDAD_HORAS);
        assertThat(testRegistroClases.getAsistenciaAlumno()).isEqualTo(DEFAULT_ASISTENCIA_ALUMNO);
    }

    @Test
    @Transactional
    void fullUpdateRegistroClasesWithPatch() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        int databaseSizeBeforeUpdate = registroClasesRepository.findAll().size();

        // Update the registroClases using partial update
        RegistroClases partialUpdatedRegistroClases = new RegistroClases();
        partialUpdatedRegistroClases.setId(registroClases.getId());

        partialUpdatedRegistroClases.fecha(UPDATED_FECHA).cantidadHoras(UPDATED_CANTIDAD_HORAS).asistenciaAlumno(UPDATED_ASISTENCIA_ALUMNO);

        restRegistroClasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistroClases.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegistroClases))
            )
            .andExpect(status().isOk());

        // Validate the RegistroClases in the database
        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeUpdate);
        RegistroClases testRegistroClases = registroClasesList.get(registroClasesList.size() - 1);
        assertThat(testRegistroClases.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testRegistroClases.getCantidadHoras()).isEqualTo(UPDATED_CANTIDAD_HORAS);
        assertThat(testRegistroClases.getAsistenciaAlumno()).isEqualTo(UPDATED_ASISTENCIA_ALUMNO);
    }

    @Test
    @Transactional
    void patchNonExistingRegistroClases() throws Exception {
        int databaseSizeBeforeUpdate = registroClasesRepository.findAll().size();
        registroClases.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistroClasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, registroClases.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(registroClases))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroClases in the database
        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegistroClases() throws Exception {
        int databaseSizeBeforeUpdate = registroClasesRepository.findAll().size();
        registroClases.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroClasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(registroClases))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroClases in the database
        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegistroClases() throws Exception {
        int databaseSizeBeforeUpdate = registroClasesRepository.findAll().size();
        registroClases.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroClasesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(registroClases))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RegistroClases in the database
        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegistroClases() throws Exception {
        // Initialize the database
        registroClasesRepository.saveAndFlush(registroClases);

        int databaseSizeBeforeDelete = registroClasesRepository.findAll().size();

        // Delete the registroClases
        restRegistroClasesMockMvc
            .perform(delete(ENTITY_API_URL_ID, registroClases.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RegistroClases> registroClasesList = registroClasesRepository.findAll();
        assertThat(registroClasesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
