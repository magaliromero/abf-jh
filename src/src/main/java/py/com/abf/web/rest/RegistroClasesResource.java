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
import py.com.abf.domain.RegistroClases;
import py.com.abf.repository.RegistroClasesRepository;
import py.com.abf.service.RegistroClasesQueryService;
import py.com.abf.service.RegistroClasesService;
import py.com.abf.service.criteria.RegistroClasesCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.RegistroClases}.
 */
@RestController
@RequestMapping("/api")
public class RegistroClasesResource {

    private final Logger log = LoggerFactory.getLogger(RegistroClasesResource.class);

    private static final String ENTITY_NAME = "registroClases";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegistroClasesService registroClasesService;

    private final RegistroClasesRepository registroClasesRepository;

    private final RegistroClasesQueryService registroClasesQueryService;

    public RegistroClasesResource(
        RegistroClasesService registroClasesService,
        RegistroClasesRepository registroClasesRepository,
        RegistroClasesQueryService registroClasesQueryService
    ) {
        this.registroClasesService = registroClasesService;
        this.registroClasesRepository = registroClasesRepository;
        this.registroClasesQueryService = registroClasesQueryService;
    }

    /**
     * {@code POST  /registro-clases} : Create a new registroClases.
     *
     * @param registroClases the registroClases to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new registroClases, or with status {@code 400 (Bad Request)} if the registroClases has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/registro-clases")
    public ResponseEntity<RegistroClases> createRegistroClases(@Valid @RequestBody RegistroClases registroClases)
        throws URISyntaxException {
        log.debug("REST request to save RegistroClases : {}", registroClases);
        if (registroClases.getId() != null) {
            throw new BadRequestAlertException("A new registroClases cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegistroClases result = registroClasesService.save(registroClases);
        return ResponseEntity
            .created(new URI("/api/registro-clases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /registro-clases/:id} : Updates an existing registroClases.
     *
     * @param id the id of the registroClases to save.
     * @param registroClases the registroClases to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registroClases,
     * or with status {@code 400 (Bad Request)} if the registroClases is not valid,
     * or with status {@code 500 (Internal Server Error)} if the registroClases couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/registro-clases/{id}")
    public ResponseEntity<RegistroClases> updateRegistroClases(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RegistroClases registroClases
    ) throws URISyntaxException {
        log.debug("REST request to update RegistroClases : {}, {}", id, registroClases);
        if (registroClases.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registroClases.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registroClasesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RegistroClases result = registroClasesService.update(registroClases);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, registroClases.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /registro-clases/:id} : Partial updates given fields of an existing registroClases, field will ignore if it is null
     *
     * @param id the id of the registroClases to save.
     * @param registroClases the registroClases to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registroClases,
     * or with status {@code 400 (Bad Request)} if the registroClases is not valid,
     * or with status {@code 404 (Not Found)} if the registroClases is not found,
     * or with status {@code 500 (Internal Server Error)} if the registroClases couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/registro-clases/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RegistroClases> partialUpdateRegistroClases(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RegistroClases registroClases
    ) throws URISyntaxException {
        log.debug("REST request to partial update RegistroClases partially : {}, {}", id, registroClases);
        if (registroClases.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registroClases.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registroClasesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RegistroClases> result = registroClasesService.partialUpdate(registroClases);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, registroClases.getId().toString())
        );
    }

    /**
     * {@code GET  /registro-clases} : get all the registroClases.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registroClases in body.
     */
    @GetMapping("/registro-clases")
    public ResponseEntity<List<RegistroClases>> getAllRegistroClases(
        RegistroClasesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get RegistroClases by criteria: {}", criteria);
        Page<RegistroClases> page = registroClasesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /registro-clases/count} : count all the registroClases.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/registro-clases/count")
    public ResponseEntity<Long> countRegistroClases(RegistroClasesCriteria criteria) {
        log.debug("REST request to count RegistroClases by criteria: {}", criteria);
        return ResponseEntity.ok().body(registroClasesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /registro-clases/:id} : get the "id" registroClases.
     *
     * @param id the id of the registroClases to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the registroClases, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/registro-clases/{id}")
    public ResponseEntity<RegistroClases> getRegistroClases(@PathVariable Long id) {
        log.debug("REST request to get RegistroClases : {}", id);
        Optional<RegistroClases> registroClases = registroClasesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(registroClases);
    }

    /**
     * {@code DELETE  /registro-clases/:id} : delete the "id" registroClases.
     *
     * @param id the id of the registroClases to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/registro-clases/{id}")
    public ResponseEntity<Void> deleteRegistroClases(@PathVariable Long id) {
        log.debug("REST request to delete RegistroClases : {}", id);
        registroClasesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
