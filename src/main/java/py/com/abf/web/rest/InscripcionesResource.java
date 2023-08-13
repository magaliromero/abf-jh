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
import py.com.abf.domain.Inscripciones;
import py.com.abf.repository.InscripcionesRepository;
import py.com.abf.service.InscripcionesQueryService;
import py.com.abf.service.InscripcionesService;
import py.com.abf.service.criteria.InscripcionesCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Inscripciones}.
 */
@RestController
@RequestMapping("/api")
public class InscripcionesResource {

    private final Logger log = LoggerFactory.getLogger(InscripcionesResource.class);

    private static final String ENTITY_NAME = "inscripciones";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InscripcionesService inscripcionesService;

    private final InscripcionesRepository inscripcionesRepository;

    private final InscripcionesQueryService inscripcionesQueryService;

    public InscripcionesResource(
        InscripcionesService inscripcionesService,
        InscripcionesRepository inscripcionesRepository,
        InscripcionesQueryService inscripcionesQueryService
    ) {
        this.inscripcionesService = inscripcionesService;
        this.inscripcionesRepository = inscripcionesRepository;
        this.inscripcionesQueryService = inscripcionesQueryService;
    }

    /**
     * {@code POST  /inscripciones} : Create a new inscripciones.
     *
     * @param inscripciones the inscripciones to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inscripciones, or with status {@code 400 (Bad Request)} if the inscripciones has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inscripciones")
    public ResponseEntity<Inscripciones> createInscripciones(@Valid @RequestBody Inscripciones inscripciones) throws URISyntaxException {
        log.debug("REST request to save Inscripciones : {}", inscripciones);
        if (inscripciones.getId() != null) {
            throw new BadRequestAlertException("A new inscripciones cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Inscripciones result = inscripcionesService.save(inscripciones);
        return ResponseEntity
            .created(new URI("/api/inscripciones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inscripciones/:id} : Updates an existing inscripciones.
     *
     * @param id the id of the inscripciones to save.
     * @param inscripciones the inscripciones to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscripciones,
     * or with status {@code 400 (Bad Request)} if the inscripciones is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inscripciones couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inscripciones/{id}")
    public ResponseEntity<Inscripciones> updateInscripciones(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Inscripciones inscripciones
    ) throws URISyntaxException {
        log.debug("REST request to update Inscripciones : {}, {}", id, inscripciones);
        if (inscripciones.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscripciones.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscripcionesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Inscripciones result = inscripcionesService.update(inscripciones);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscripciones.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /inscripciones/:id} : Partial updates given fields of an existing inscripciones, field will ignore if it is null
     *
     * @param id the id of the inscripciones to save.
     * @param inscripciones the inscripciones to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscripciones,
     * or with status {@code 400 (Bad Request)} if the inscripciones is not valid,
     * or with status {@code 404 (Not Found)} if the inscripciones is not found,
     * or with status {@code 500 (Internal Server Error)} if the inscripciones couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inscripciones/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Inscripciones> partialUpdateInscripciones(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Inscripciones inscripciones
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inscripciones partially : {}, {}", id, inscripciones);
        if (inscripciones.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscripciones.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscripcionesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Inscripciones> result = inscripcionesService.partialUpdate(inscripciones);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscripciones.getId().toString())
        );
    }

    /**
     * {@code GET  /inscripciones} : get all the inscripciones.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inscripciones in body.
     */
    @GetMapping("/inscripciones")
    public ResponseEntity<List<Inscripciones>> getAllInscripciones(
        InscripcionesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Inscripciones by criteria: {}", criteria);
        Page<Inscripciones> page = inscripcionesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inscripciones/count} : count all the inscripciones.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/inscripciones/count")
    public ResponseEntity<Long> countInscripciones(InscripcionesCriteria criteria) {
        log.debug("REST request to count Inscripciones by criteria: {}", criteria);
        return ResponseEntity.ok().body(inscripcionesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /inscripciones/:id} : get the "id" inscripciones.
     *
     * @param id the id of the inscripciones to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inscripciones, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inscripciones/{id}")
    public ResponseEntity<Inscripciones> getInscripciones(@PathVariable Long id) {
        log.debug("REST request to get Inscripciones : {}", id);
        Optional<Inscripciones> inscripciones = inscripcionesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inscripciones);
    }

    /**
     * {@code DELETE  /inscripciones/:id} : delete the "id" inscripciones.
     *
     * @param id the id of the inscripciones to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inscripciones/{id}")
    public ResponseEntity<Void> deleteInscripciones(@PathVariable Long id) {
        log.debug("REST request to delete Inscripciones : {}", id);
        inscripcionesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
