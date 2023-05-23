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
import py.com.abf.domain.EvaluacionesDetalle;
import py.com.abf.repository.EvaluacionesDetalleRepository;
import py.com.abf.service.EvaluacionesDetalleQueryService;
import py.com.abf.service.EvaluacionesDetalleService;
import py.com.abf.service.criteria.EvaluacionesDetalleCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.EvaluacionesDetalle}.
 */
@RestController
@RequestMapping("/api")
public class EvaluacionesDetalleResource {

    private final Logger log = LoggerFactory.getLogger(EvaluacionesDetalleResource.class);

    private static final String ENTITY_NAME = "evaluacionesDetalle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvaluacionesDetalleService evaluacionesDetalleService;

    private final EvaluacionesDetalleRepository evaluacionesDetalleRepository;

    private final EvaluacionesDetalleQueryService evaluacionesDetalleQueryService;

    public EvaluacionesDetalleResource(
        EvaluacionesDetalleService evaluacionesDetalleService,
        EvaluacionesDetalleRepository evaluacionesDetalleRepository,
        EvaluacionesDetalleQueryService evaluacionesDetalleQueryService
    ) {
        this.evaluacionesDetalleService = evaluacionesDetalleService;
        this.evaluacionesDetalleRepository = evaluacionesDetalleRepository;
        this.evaluacionesDetalleQueryService = evaluacionesDetalleQueryService;
    }

    /**
     * {@code POST  /evaluaciones-detalles} : Create a new evaluacionesDetalle.
     *
     * @param evaluacionesDetalle the evaluacionesDetalle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evaluacionesDetalle, or with status {@code 400 (Bad Request)} if the evaluacionesDetalle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/evaluaciones-detalles")
    public ResponseEntity<EvaluacionesDetalle> createEvaluacionesDetalle(@Valid @RequestBody EvaluacionesDetalle evaluacionesDetalle)
        throws URISyntaxException {
        log.debug("REST request to save EvaluacionesDetalle : {}", evaluacionesDetalle);
        if (evaluacionesDetalle.getId() != null) {
            throw new BadRequestAlertException("A new evaluacionesDetalle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EvaluacionesDetalle result = evaluacionesDetalleService.save(evaluacionesDetalle);
        return ResponseEntity
            .created(new URI("/api/evaluaciones-detalles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /evaluaciones-detalles/:id} : Updates an existing evaluacionesDetalle.
     *
     * @param id the id of the evaluacionesDetalle to save.
     * @param evaluacionesDetalle the evaluacionesDetalle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaluacionesDetalle,
     * or with status {@code 400 (Bad Request)} if the evaluacionesDetalle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evaluacionesDetalle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/evaluaciones-detalles/{id}")
    public ResponseEntity<EvaluacionesDetalle> updateEvaluacionesDetalle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EvaluacionesDetalle evaluacionesDetalle
    ) throws URISyntaxException {
        log.debug("REST request to update EvaluacionesDetalle : {}, {}", id, evaluacionesDetalle);
        if (evaluacionesDetalle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaluacionesDetalle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaluacionesDetalleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EvaluacionesDetalle result = evaluacionesDetalleService.update(evaluacionesDetalle);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaluacionesDetalle.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /evaluaciones-detalles/:id} : Partial updates given fields of an existing evaluacionesDetalle, field will ignore if it is null
     *
     * @param id the id of the evaluacionesDetalle to save.
     * @param evaluacionesDetalle the evaluacionesDetalle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaluacionesDetalle,
     * or with status {@code 400 (Bad Request)} if the evaluacionesDetalle is not valid,
     * or with status {@code 404 (Not Found)} if the evaluacionesDetalle is not found,
     * or with status {@code 500 (Internal Server Error)} if the evaluacionesDetalle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/evaluaciones-detalles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EvaluacionesDetalle> partialUpdateEvaluacionesDetalle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EvaluacionesDetalle evaluacionesDetalle
    ) throws URISyntaxException {
        log.debug("REST request to partial update EvaluacionesDetalle partially : {}, {}", id, evaluacionesDetalle);
        if (evaluacionesDetalle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaluacionesDetalle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaluacionesDetalleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EvaluacionesDetalle> result = evaluacionesDetalleService.partialUpdate(evaluacionesDetalle);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaluacionesDetalle.getId().toString())
        );
    }

    /**
     * {@code GET  /evaluaciones-detalles} : get all the evaluacionesDetalles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evaluacionesDetalles in body.
     */
    @GetMapping("/evaluaciones-detalles")
    public ResponseEntity<List<EvaluacionesDetalle>> getAllEvaluacionesDetalles(
        EvaluacionesDetalleCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EvaluacionesDetalles by criteria: {}", criteria);
        Page<EvaluacionesDetalle> page = evaluacionesDetalleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /evaluaciones-detalles/count} : count all the evaluacionesDetalles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/evaluaciones-detalles/count")
    public ResponseEntity<Long> countEvaluacionesDetalles(EvaluacionesDetalleCriteria criteria) {
        log.debug("REST request to count EvaluacionesDetalles by criteria: {}", criteria);
        return ResponseEntity.ok().body(evaluacionesDetalleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /evaluaciones-detalles/:id} : get the "id" evaluacionesDetalle.
     *
     * @param id the id of the evaluacionesDetalle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evaluacionesDetalle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/evaluaciones-detalles/{id}")
    public ResponseEntity<EvaluacionesDetalle> getEvaluacionesDetalle(@PathVariable Long id) {
        log.debug("REST request to get EvaluacionesDetalle : {}", id);
        Optional<EvaluacionesDetalle> evaluacionesDetalle = evaluacionesDetalleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(evaluacionesDetalle);
    }

    /**
     * {@code DELETE  /evaluaciones-detalles/:id} : delete the "id" evaluacionesDetalle.
     *
     * @param id the id of the evaluacionesDetalle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/evaluaciones-detalles/{id}")
    public ResponseEntity<Void> deleteEvaluacionesDetalle(@PathVariable Long id) {
        log.debug("REST request to delete EvaluacionesDetalle : {}", id);
        evaluacionesDetalleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
