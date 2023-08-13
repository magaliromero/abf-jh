package py.com.abf.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import py.com.abf.domain.Clientes;
import py.com.abf.repository.ClientesRepository;
import py.com.abf.service.ClientesQueryService;
import py.com.abf.service.ClientesService;
import py.com.abf.service.criteria.ClientesCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Clientes}.
 */
@RestController
@RequestMapping("/api")
public class ClientesResource {

    private final Logger log = LoggerFactory.getLogger(ClientesResource.class);

    private static final String ENTITY_NAME = "clientes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientesService clientesService;

    private final ClientesRepository clientesRepository;

    private final ClientesQueryService clientesQueryService;

    public ClientesResource(
        ClientesService clientesService,
        ClientesRepository clientesRepository,
        ClientesQueryService clientesQueryService
    ) {
        this.clientesService = clientesService;
        this.clientesRepository = clientesRepository;
        this.clientesQueryService = clientesQueryService;
    }

    /**
     * {@code POST  /clientes} : Create a new clientes.
     *
     * @param clientes the clientes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientes, or with status {@code 400 (Bad Request)} if the clientes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clientes")
    public ResponseEntity<Clientes> createClientes(@Valid @RequestBody Clientes clientes) throws URISyntaxException {
        log.debug("REST request to save Clientes : {}", clientes);
        if (clientes.getId() != null) {
            throw new BadRequestAlertException("A new clientes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Clientes result = clientesService.save(clientes);
        return ResponseEntity
            .created(new URI("/api/clientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clientes/:id} : Updates an existing clientes.
     *
     * @param id the id of the clientes to save.
     * @param clientes the clientes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientes,
     * or with status {@code 400 (Bad Request)} if the clientes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clientes/{id}")
    public ResponseEntity<Clientes> updateClientes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Clientes clientes
    ) throws URISyntaxException {
        log.debug("REST request to update Clientes : {}, {}", id, clientes);
        if (clientes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Clientes result = clientesService.update(clientes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clientes/:id} : Partial updates given fields of an existing clientes, field will ignore if it is null
     *
     * @param id the id of the clientes to save.
     * @param clientes the clientes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientes,
     * or with status {@code 400 (Bad Request)} if the clientes is not valid,
     * or with status {@code 404 (Not Found)} if the clientes is not found,
     * or with status {@code 500 (Internal Server Error)} if the clientes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clientes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Clientes> partialUpdateClientes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Clientes clientes
    ) throws URISyntaxException {
        log.debug("REST request to partial update Clientes partially : {}, {}", id, clientes);
        if (clientes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Clientes> result = clientesService.partialUpdate(clientes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientes.getId().toString())
        );
    }

    /**
     * {@code GET  /clientes} : get all the clientes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clientes in body.
     */
    @GetMapping("/clientes")
    public ResponseEntity<List<Clientes>> getAllClientes(
        ClientesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Clientes by criteria: {}", criteria);
        Page<Clientes> page = clientesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /clientes/count} : count all the clientes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/clientes/count")
    public ResponseEntity<Long> countClientes(ClientesCriteria criteria) {
        log.debug("REST request to count Clientes by criteria: {}", criteria);
        return ResponseEntity.ok().body(clientesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /clientes/:id} : get the "id" clientes.
     *
     * @param id the id of the clientes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clientes/{id}")
    public ResponseEntity<Clientes> getClientes(@PathVariable Long id) {
        log.debug("REST request to get Clientes : {}", id);
        Optional<Clientes> clientes = clientesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clientes);
    }

    /**
     * {@code DELETE  /clientes/:id} : delete the "id" clientes.
     *
     * @param id the id of the clientes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Void> deleteClientes(@PathVariable Long id) {
        log.debug("REST request to delete Clientes : {}", id);
        clientesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
