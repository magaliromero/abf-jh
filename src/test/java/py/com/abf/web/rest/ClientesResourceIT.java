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
import py.com.abf.domain.Clientes;
import py.com.abf.repository.ClientesRepository;
import py.com.abf.service.criteria.ClientesCriteria;

/**
 * Integration tests for the {@link ClientesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientesResourceIT {

    private static final String DEFAULT_RUC = "AAAAAAAAAA";
    private static final String UPDATED_RUC = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRES = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRES = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_RAZON_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAZON_SOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_NACIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientesMockMvc;

    private Clientes clientes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clientes createEntity(EntityManager em) {
        Clientes clientes = new Clientes()
            .ruc(DEFAULT_RUC)
            .nombres(DEFAULT_NOMBRES)
            .apellidos(DEFAULT_APELLIDOS)
            .razonSocial(DEFAULT_RAZON_SOCIAL)
            .documento(DEFAULT_DOCUMENTO)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .direccion(DEFAULT_DIRECCION);
        return clientes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clientes createUpdatedEntity(EntityManager em) {
        Clientes clientes = new Clientes()
            .ruc(UPDATED_RUC)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .documento(UPDATED_DOCUMENTO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .direccion(UPDATED_DIRECCION);
        return clientes;
    }

    @BeforeEach
    public void initTest() {
        clientes = createEntity(em);
    }

    @Test
    @Transactional
    void createClientes() throws Exception {
        int databaseSizeBeforeCreate = clientesRepository.findAll().size();
        // Create the Clientes
        restClientesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientes)))
            .andExpect(status().isCreated());

        // Validate the Clientes in the database
        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeCreate + 1);
        Clientes testClientes = clientesList.get(clientesList.size() - 1);
        assertThat(testClientes.getRuc()).isEqualTo(DEFAULT_RUC);
        assertThat(testClientes.getNombres()).isEqualTo(DEFAULT_NOMBRES);
        assertThat(testClientes.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testClientes.getRazonSocial()).isEqualTo(DEFAULT_RAZON_SOCIAL);
        assertThat(testClientes.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
        assertThat(testClientes.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClientes.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testClientes.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testClientes.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    void createClientesWithExistingId() throws Exception {
        // Create the Clientes with an existing ID
        clientes.setId(1L);

        int databaseSizeBeforeCreate = clientesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientes)))
            .andExpect(status().isBadRequest());

        // Validate the Clientes in the database
        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombresIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientesRepository.findAll().size();
        // set the field null
        clientes.setNombres(null);

        // Create the Clientes, which fails.

        restClientesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientes)))
            .andExpect(status().isBadRequest());

        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApellidosIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientesRepository.findAll().size();
        // set the field null
        clientes.setApellidos(null);

        // Create the Clientes, which fails.

        restClientesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientes)))
            .andExpect(status().isBadRequest());

        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRazonSocialIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientesRepository.findAll().size();
        // set the field null
        clientes.setRazonSocial(null);

        // Create the Clientes, which fails.

        restClientesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientes)))
            .andExpect(status().isBadRequest());

        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientesRepository.findAll().size();
        // set the field null
        clientes.setDocumento(null);

        // Create the Clientes, which fails.

        restClientesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientes)))
            .andExpect(status().isBadRequest());

        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClientes() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList
        restClientesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientes.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruc").value(hasItem(DEFAULT_RUC)))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].razonSocial").value(hasItem(DEFAULT_RAZON_SOCIAL)))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)));
    }

    @Test
    @Transactional
    void getClientes() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get the clientes
        restClientesMockMvc
            .perform(get(ENTITY_API_URL_ID, clientes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clientes.getId().intValue()))
            .andExpect(jsonPath("$.ruc").value(DEFAULT_RUC))
            .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.razonSocial").value(DEFAULT_RAZON_SOCIAL))
            .andExpect(jsonPath("$.documento").value(DEFAULT_DOCUMENTO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION));
    }

    @Test
    @Transactional
    void getClientesByIdFiltering() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        Long id = clientes.getId();

        defaultClientesShouldBeFound("id.equals=" + id);
        defaultClientesShouldNotBeFound("id.notEquals=" + id);

        defaultClientesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClientesShouldNotBeFound("id.greaterThan=" + id);

        defaultClientesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClientesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClientesByRucIsEqualToSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where ruc equals to DEFAULT_RUC
        defaultClientesShouldBeFound("ruc.equals=" + DEFAULT_RUC);

        // Get all the clientesList where ruc equals to UPDATED_RUC
        defaultClientesShouldNotBeFound("ruc.equals=" + UPDATED_RUC);
    }

    @Test
    @Transactional
    void getAllClientesByRucIsInShouldWork() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where ruc in DEFAULT_RUC or UPDATED_RUC
        defaultClientesShouldBeFound("ruc.in=" + DEFAULT_RUC + "," + UPDATED_RUC);

        // Get all the clientesList where ruc equals to UPDATED_RUC
        defaultClientesShouldNotBeFound("ruc.in=" + UPDATED_RUC);
    }

    @Test
    @Transactional
    void getAllClientesByRucIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where ruc is not null
        defaultClientesShouldBeFound("ruc.specified=true");

        // Get all the clientesList where ruc is null
        defaultClientesShouldNotBeFound("ruc.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByRucContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where ruc contains DEFAULT_RUC
        defaultClientesShouldBeFound("ruc.contains=" + DEFAULT_RUC);

        // Get all the clientesList where ruc contains UPDATED_RUC
        defaultClientesShouldNotBeFound("ruc.contains=" + UPDATED_RUC);
    }

    @Test
    @Transactional
    void getAllClientesByRucNotContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where ruc does not contain DEFAULT_RUC
        defaultClientesShouldNotBeFound("ruc.doesNotContain=" + DEFAULT_RUC);

        // Get all the clientesList where ruc does not contain UPDATED_RUC
        defaultClientesShouldBeFound("ruc.doesNotContain=" + UPDATED_RUC);
    }

    @Test
    @Transactional
    void getAllClientesByNombresIsEqualToSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where nombres equals to DEFAULT_NOMBRES
        defaultClientesShouldBeFound("nombres.equals=" + DEFAULT_NOMBRES);

        // Get all the clientesList where nombres equals to UPDATED_NOMBRES
        defaultClientesShouldNotBeFound("nombres.equals=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllClientesByNombresIsInShouldWork() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where nombres in DEFAULT_NOMBRES or UPDATED_NOMBRES
        defaultClientesShouldBeFound("nombres.in=" + DEFAULT_NOMBRES + "," + UPDATED_NOMBRES);

        // Get all the clientesList where nombres equals to UPDATED_NOMBRES
        defaultClientesShouldNotBeFound("nombres.in=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllClientesByNombresIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where nombres is not null
        defaultClientesShouldBeFound("nombres.specified=true");

        // Get all the clientesList where nombres is null
        defaultClientesShouldNotBeFound("nombres.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByNombresContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where nombres contains DEFAULT_NOMBRES
        defaultClientesShouldBeFound("nombres.contains=" + DEFAULT_NOMBRES);

        // Get all the clientesList where nombres contains UPDATED_NOMBRES
        defaultClientesShouldNotBeFound("nombres.contains=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllClientesByNombresNotContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where nombres does not contain DEFAULT_NOMBRES
        defaultClientesShouldNotBeFound("nombres.doesNotContain=" + DEFAULT_NOMBRES);

        // Get all the clientesList where nombres does not contain UPDATED_NOMBRES
        defaultClientesShouldBeFound("nombres.doesNotContain=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllClientesByApellidosIsEqualToSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where apellidos equals to DEFAULT_APELLIDOS
        defaultClientesShouldBeFound("apellidos.equals=" + DEFAULT_APELLIDOS);

        // Get all the clientesList where apellidos equals to UPDATED_APELLIDOS
        defaultClientesShouldNotBeFound("apellidos.equals=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllClientesByApellidosIsInShouldWork() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where apellidos in DEFAULT_APELLIDOS or UPDATED_APELLIDOS
        defaultClientesShouldBeFound("apellidos.in=" + DEFAULT_APELLIDOS + "," + UPDATED_APELLIDOS);

        // Get all the clientesList where apellidos equals to UPDATED_APELLIDOS
        defaultClientesShouldNotBeFound("apellidos.in=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllClientesByApellidosIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where apellidos is not null
        defaultClientesShouldBeFound("apellidos.specified=true");

        // Get all the clientesList where apellidos is null
        defaultClientesShouldNotBeFound("apellidos.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByApellidosContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where apellidos contains DEFAULT_APELLIDOS
        defaultClientesShouldBeFound("apellidos.contains=" + DEFAULT_APELLIDOS);

        // Get all the clientesList where apellidos contains UPDATED_APELLIDOS
        defaultClientesShouldNotBeFound("apellidos.contains=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllClientesByApellidosNotContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where apellidos does not contain DEFAULT_APELLIDOS
        defaultClientesShouldNotBeFound("apellidos.doesNotContain=" + DEFAULT_APELLIDOS);

        // Get all the clientesList where apellidos does not contain UPDATED_APELLIDOS
        defaultClientesShouldBeFound("apellidos.doesNotContain=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllClientesByRazonSocialIsEqualToSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where razonSocial equals to DEFAULT_RAZON_SOCIAL
        defaultClientesShouldBeFound("razonSocial.equals=" + DEFAULT_RAZON_SOCIAL);

        // Get all the clientesList where razonSocial equals to UPDATED_RAZON_SOCIAL
        defaultClientesShouldNotBeFound("razonSocial.equals=" + UPDATED_RAZON_SOCIAL);
    }

    @Test
    @Transactional
    void getAllClientesByRazonSocialIsInShouldWork() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where razonSocial in DEFAULT_RAZON_SOCIAL or UPDATED_RAZON_SOCIAL
        defaultClientesShouldBeFound("razonSocial.in=" + DEFAULT_RAZON_SOCIAL + "," + UPDATED_RAZON_SOCIAL);

        // Get all the clientesList where razonSocial equals to UPDATED_RAZON_SOCIAL
        defaultClientesShouldNotBeFound("razonSocial.in=" + UPDATED_RAZON_SOCIAL);
    }

    @Test
    @Transactional
    void getAllClientesByRazonSocialIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where razonSocial is not null
        defaultClientesShouldBeFound("razonSocial.specified=true");

        // Get all the clientesList where razonSocial is null
        defaultClientesShouldNotBeFound("razonSocial.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByRazonSocialContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where razonSocial contains DEFAULT_RAZON_SOCIAL
        defaultClientesShouldBeFound("razonSocial.contains=" + DEFAULT_RAZON_SOCIAL);

        // Get all the clientesList where razonSocial contains UPDATED_RAZON_SOCIAL
        defaultClientesShouldNotBeFound("razonSocial.contains=" + UPDATED_RAZON_SOCIAL);
    }

    @Test
    @Transactional
    void getAllClientesByRazonSocialNotContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where razonSocial does not contain DEFAULT_RAZON_SOCIAL
        defaultClientesShouldNotBeFound("razonSocial.doesNotContain=" + DEFAULT_RAZON_SOCIAL);

        // Get all the clientesList where razonSocial does not contain UPDATED_RAZON_SOCIAL
        defaultClientesShouldBeFound("razonSocial.doesNotContain=" + UPDATED_RAZON_SOCIAL);
    }

    @Test
    @Transactional
    void getAllClientesByDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where documento equals to DEFAULT_DOCUMENTO
        defaultClientesShouldBeFound("documento.equals=" + DEFAULT_DOCUMENTO);

        // Get all the clientesList where documento equals to UPDATED_DOCUMENTO
        defaultClientesShouldNotBeFound("documento.equals=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where documento in DEFAULT_DOCUMENTO or UPDATED_DOCUMENTO
        defaultClientesShouldBeFound("documento.in=" + DEFAULT_DOCUMENTO + "," + UPDATED_DOCUMENTO);

        // Get all the clientesList where documento equals to UPDATED_DOCUMENTO
        defaultClientesShouldNotBeFound("documento.in=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where documento is not null
        defaultClientesShouldBeFound("documento.specified=true");

        // Get all the clientesList where documento is null
        defaultClientesShouldNotBeFound("documento.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByDocumentoContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where documento contains DEFAULT_DOCUMENTO
        defaultClientesShouldBeFound("documento.contains=" + DEFAULT_DOCUMENTO);

        // Get all the clientesList where documento contains UPDATED_DOCUMENTO
        defaultClientesShouldNotBeFound("documento.contains=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDocumentoNotContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where documento does not contain DEFAULT_DOCUMENTO
        defaultClientesShouldNotBeFound("documento.doesNotContain=" + DEFAULT_DOCUMENTO);

        // Get all the clientesList where documento does not contain UPDATED_DOCUMENTO
        defaultClientesShouldBeFound("documento.doesNotContain=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where email equals to DEFAULT_EMAIL
        defaultClientesShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the clientesList where email equals to UPDATED_EMAIL
        defaultClientesShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultClientesShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the clientesList where email equals to UPDATED_EMAIL
        defaultClientesShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where email is not null
        defaultClientesShouldBeFound("email.specified=true");

        // Get all the clientesList where email is null
        defaultClientesShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByEmailContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where email contains DEFAULT_EMAIL
        defaultClientesShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the clientesList where email contains UPDATED_EMAIL
        defaultClientesShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where email does not contain DEFAULT_EMAIL
        defaultClientesShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the clientesList where email does not contain UPDATED_EMAIL
        defaultClientesShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientesByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where telefono equals to DEFAULT_TELEFONO
        defaultClientesShouldBeFound("telefono.equals=" + DEFAULT_TELEFONO);

        // Get all the clientesList where telefono equals to UPDATED_TELEFONO
        defaultClientesShouldNotBeFound("telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where telefono in DEFAULT_TELEFONO or UPDATED_TELEFONO
        defaultClientesShouldBeFound("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO);

        // Get all the clientesList where telefono equals to UPDATED_TELEFONO
        defaultClientesShouldNotBeFound("telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where telefono is not null
        defaultClientesShouldBeFound("telefono.specified=true");

        // Get all the clientesList where telefono is null
        defaultClientesShouldNotBeFound("telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where telefono contains DEFAULT_TELEFONO
        defaultClientesShouldBeFound("telefono.contains=" + DEFAULT_TELEFONO);

        // Get all the clientesList where telefono contains UPDATED_TELEFONO
        defaultClientesShouldNotBeFound("telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where telefono does not contain DEFAULT_TELEFONO
        defaultClientesShouldNotBeFound("telefono.doesNotContain=" + DEFAULT_TELEFONO);

        // Get all the clientesList where telefono does not contain UPDATED_TELEFONO
        defaultClientesShouldBeFound("telefono.doesNotContain=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where fechaNacimiento equals to DEFAULT_FECHA_NACIMIENTO
        defaultClientesShouldBeFound("fechaNacimiento.equals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the clientesList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultClientesShouldNotBeFound("fechaNacimiento.equals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsInShouldWork() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where fechaNacimiento in DEFAULT_FECHA_NACIMIENTO or UPDATED_FECHA_NACIMIENTO
        defaultClientesShouldBeFound("fechaNacimiento.in=" + DEFAULT_FECHA_NACIMIENTO + "," + UPDATED_FECHA_NACIMIENTO);

        // Get all the clientesList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultClientesShouldNotBeFound("fechaNacimiento.in=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where fechaNacimiento is not null
        defaultClientesShouldBeFound("fechaNacimiento.specified=true");

        // Get all the clientesList where fechaNacimiento is null
        defaultClientesShouldNotBeFound("fechaNacimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where fechaNacimiento is greater than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultClientesShouldBeFound("fechaNacimiento.greaterThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the clientesList where fechaNacimiento is greater than or equal to UPDATED_FECHA_NACIMIENTO
        defaultClientesShouldNotBeFound("fechaNacimiento.greaterThanOrEqual=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where fechaNacimiento is less than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultClientesShouldBeFound("fechaNacimiento.lessThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the clientesList where fechaNacimiento is less than or equal to SMALLER_FECHA_NACIMIENTO
        defaultClientesShouldNotBeFound("fechaNacimiento.lessThanOrEqual=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where fechaNacimiento is less than DEFAULT_FECHA_NACIMIENTO
        defaultClientesShouldNotBeFound("fechaNacimiento.lessThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the clientesList where fechaNacimiento is less than UPDATED_FECHA_NACIMIENTO
        defaultClientesShouldBeFound("fechaNacimiento.lessThan=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where fechaNacimiento is greater than DEFAULT_FECHA_NACIMIENTO
        defaultClientesShouldNotBeFound("fechaNacimiento.greaterThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the clientesList where fechaNacimiento is greater than SMALLER_FECHA_NACIMIENTO
        defaultClientesShouldBeFound("fechaNacimiento.greaterThan=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where direccion equals to DEFAULT_DIRECCION
        defaultClientesShouldBeFound("direccion.equals=" + DEFAULT_DIRECCION);

        // Get all the clientesList where direccion equals to UPDATED_DIRECCION
        defaultClientesShouldNotBeFound("direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllClientesByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where direccion in DEFAULT_DIRECCION or UPDATED_DIRECCION
        defaultClientesShouldBeFound("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION);

        // Get all the clientesList where direccion equals to UPDATED_DIRECCION
        defaultClientesShouldNotBeFound("direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllClientesByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where direccion is not null
        defaultClientesShouldBeFound("direccion.specified=true");

        // Get all the clientesList where direccion is null
        defaultClientesShouldNotBeFound("direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByDireccionContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where direccion contains DEFAULT_DIRECCION
        defaultClientesShouldBeFound("direccion.contains=" + DEFAULT_DIRECCION);

        // Get all the clientesList where direccion contains UPDATED_DIRECCION
        defaultClientesShouldNotBeFound("direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllClientesByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        // Get all the clientesList where direccion does not contain DEFAULT_DIRECCION
        defaultClientesShouldNotBeFound("direccion.doesNotContain=" + DEFAULT_DIRECCION);

        // Get all the clientesList where direccion does not contain UPDATED_DIRECCION
        defaultClientesShouldBeFound("direccion.doesNotContain=" + UPDATED_DIRECCION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientesShouldBeFound(String filter) throws Exception {
        restClientesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientes.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruc").value(hasItem(DEFAULT_RUC)))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].razonSocial").value(hasItem(DEFAULT_RAZON_SOCIAL)))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)));

        // Check, that the count call also returns 1
        restClientesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientesShouldNotBeFound(String filter) throws Exception {
        restClientesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClientes() throws Exception {
        // Get the clientes
        restClientesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClientes() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        int databaseSizeBeforeUpdate = clientesRepository.findAll().size();

        // Update the clientes
        Clientes updatedClientes = clientesRepository.findById(clientes.getId()).get();
        // Disconnect from session so that the updates on updatedClientes are not directly saved in db
        em.detach(updatedClientes);
        updatedClientes
            .ruc(UPDATED_RUC)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .documento(UPDATED_DOCUMENTO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .direccion(UPDATED_DIRECCION);

        restClientesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClientes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClientes))
            )
            .andExpect(status().isOk());

        // Validate the Clientes in the database
        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeUpdate);
        Clientes testClientes = clientesList.get(clientesList.size() - 1);
        assertThat(testClientes.getRuc()).isEqualTo(UPDATED_RUC);
        assertThat(testClientes.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testClientes.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testClientes.getRazonSocial()).isEqualTo(UPDATED_RAZON_SOCIAL);
        assertThat(testClientes.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testClientes.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClientes.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testClientes.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testClientes.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void putNonExistingClientes() throws Exception {
        int databaseSizeBeforeUpdate = clientesRepository.findAll().size();
        clientes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clientes in the database
        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClientes() throws Exception {
        int databaseSizeBeforeUpdate = clientesRepository.findAll().size();
        clientes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clientes in the database
        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClientes() throws Exception {
        int databaseSizeBeforeUpdate = clientesRepository.findAll().size();
        clientes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clientes in the database
        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientesWithPatch() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        int databaseSizeBeforeUpdate = clientesRepository.findAll().size();

        // Update the clientes using partial update
        Clientes partialUpdatedClientes = new Clientes();
        partialUpdatedClientes.setId(clientes.getId());

        partialUpdatedClientes
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .direccion(UPDATED_DIRECCION);

        restClientesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClientes))
            )
            .andExpect(status().isOk());

        // Validate the Clientes in the database
        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeUpdate);
        Clientes testClientes = clientesList.get(clientesList.size() - 1);
        assertThat(testClientes.getRuc()).isEqualTo(DEFAULT_RUC);
        assertThat(testClientes.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testClientes.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testClientes.getRazonSocial()).isEqualTo(DEFAULT_RAZON_SOCIAL);
        assertThat(testClientes.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
        assertThat(testClientes.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClientes.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testClientes.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testClientes.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void fullUpdateClientesWithPatch() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        int databaseSizeBeforeUpdate = clientesRepository.findAll().size();

        // Update the clientes using partial update
        Clientes partialUpdatedClientes = new Clientes();
        partialUpdatedClientes.setId(clientes.getId());

        partialUpdatedClientes
            .ruc(UPDATED_RUC)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .documento(UPDATED_DOCUMENTO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .direccion(UPDATED_DIRECCION);

        restClientesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClientes))
            )
            .andExpect(status().isOk());

        // Validate the Clientes in the database
        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeUpdate);
        Clientes testClientes = clientesList.get(clientesList.size() - 1);
        assertThat(testClientes.getRuc()).isEqualTo(UPDATED_RUC);
        assertThat(testClientes.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testClientes.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testClientes.getRazonSocial()).isEqualTo(UPDATED_RAZON_SOCIAL);
        assertThat(testClientes.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testClientes.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClientes.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testClientes.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testClientes.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void patchNonExistingClientes() throws Exception {
        int databaseSizeBeforeUpdate = clientesRepository.findAll().size();
        clientes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clientes in the database
        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClientes() throws Exception {
        int databaseSizeBeforeUpdate = clientesRepository.findAll().size();
        clientes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clientes in the database
        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClientes() throws Exception {
        int databaseSizeBeforeUpdate = clientesRepository.findAll().size();
        clientes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clientes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clientes in the database
        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClientes() throws Exception {
        // Initialize the database
        clientesRepository.saveAndFlush(clientes);

        int databaseSizeBeforeDelete = clientesRepository.findAll().size();

        // Delete the clientes
        restClientesMockMvc
            .perform(delete(ENTITY_API_URL_ID, clientes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Clientes> clientesList = clientesRepository.findAll();
        assertThat(clientesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
