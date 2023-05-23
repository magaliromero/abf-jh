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
import py.com.abf.domain.Evaluaciones;
import py.com.abf.repository.EvaluacionesRepository;
import py.com.abf.service.EvaluacionesQueryService;
import py.com.abf.service.EvaluacionesService;
import py.com.abf.service.criteria.EvaluacionesCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Evaluaciones}.
 */
@RestController
@RequestMapping("/api")
public class EvaluacionesResource {

    private final Logger log = LoggerFactory.getLogger(EvaluacionesResource.class);

    private static final String ENTITY_NAME = "evaluaciones";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvaluacionesService evaluacionesService;

    private final EvaluacionesRepository evaluacionesRepository;

    private final EvaluacionesQueryService evaluacionesQueryService;

    public EvaluacionesResource(
        EvaluacionesService evaluacionesService,
        EvaluacionesRepository evaluacionesRepository,
        EvaluacionesQueryService evaluacionesQueryService
    ) {
        this.evaluacionesService = evaluacionesService;
        this.evaluacionesRepository = evaluacionesRepository;
        this.evaluacionesQueryService = evaluacionesQueryService;
    }

    /**
     * {@code POST  /evaluaciones} : Create a new evaluaciones.
     *
     * @param evaluaciones the evaluaciones to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evaluaciones, or with status {@code 400 (Bad Request)} if the evaluaciones has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/evaluaciones")
    public ResponseEntity<Evaluaciones> createEvaluaciones(@Valid @RequestBody Evaluaciones evaluaciones) throws URISyntaxException {
        log.debug("REST request to save Evaluaciones : {}", evaluaciones);
        if (evaluaciones.getId() != null) {
            throw new BadRequestAlertException("A new evaluaciones cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Evaluaciones result = evaluacionesService.save(evaluaciones);
        return ResponseEntity
            .created(new URI("/api/evaluaciones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /evaluaciones/:id} : Updates an existing evaluaciones.
     *
     * @param id the id of the evaluaciones to save.
     * @param evaluaciones the evaluaciones to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaluaciones,
     * or with status {@code 400 (Bad Request)} if the evaluaciones is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evaluaciones couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/evaluaciones/{id}")
    public ResponseEntity<Evaluaciones> updateEvaluaciones(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Evaluaciones evaluaciones
    ) throws URISyntaxException {
        log.debug("REST request to update Evaluaciones : {}, {}", id, evaluaciones);
        if (evaluaciones.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaluaciones.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaluacionesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Evaluaciones result = evaluacionesService.update(evaluaciones);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaluaciones.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /evaluaciones/:id} : Partial updates given fields of an existing evaluaciones, field will ignore if it is null
     *
     * @param id the id of the evaluaciones to save.
     * @param evaluaciones the evaluaciones to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaluaciones,
     * or with status {@code 400 (Bad Request)} if the evaluaciones is not valid,
     * or with status {@code 404 (Not Found)} if the evaluaciones is not found,
     * or with status {@code 500 (Internal Server Error)} if the evaluaciones couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/evaluaciones/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Evaluaciones> partialUpdateEvaluaciones(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Evaluaciones evaluaciones
    ) throws URISyntaxException {
        log.debug("REST request to partial update Evaluaciones partially : {}, {}", id, evaluaciones);
        if (evaluaciones.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaluaciones.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaluacionesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Evaluaciones> result = evaluacionesService.partialUpdate(evaluaciones);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaluaciones.getId().toString())
        );
    }

    /**
     * {@code GET  /evaluaciones} : get all the evaluaciones.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evaluaciones in body.
     */
    @GetMapping("/evaluaciones")
    public ResponseEntity<List<Evaluaciones>> getAllEvaluaciones(
        EvaluacionesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Evaluaciones by criteria: {}", criteria);
        Page<Evaluaciones> page = evaluacionesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /evaluaciones/count} : count all the evaluaciones.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/evaluaciones/count")
    public ResponseEntity<Long> countEvaluaciones(EvaluacionesCriteria criteria) {
        log.debug("REST request to count Evaluaciones by criteria: {}", criteria);
        return ResponseEntity.ok().body(evaluacionesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /evaluaciones/:id} : get the "id" evaluaciones.
     *
     * @param id the id of the evaluaciones to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evaluaciones, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/evaluaciones/{id}")
    public ResponseEntity<Evaluaciones> getEvaluaciones(@PathVariable Long id) {
        log.debug("REST request to get Evaluaciones : {}", id);
        Optional<Evaluaciones> evaluaciones = evaluacionesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(evaluaciones);
    }

    /**
     * {@code DELETE  /evaluaciones/:id} : delete the "id" evaluaciones.
     *
     * @param id the id of the evaluaciones to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/evaluaciones/{id}")
    public ResponseEntity<Void> deleteEvaluaciones(@PathVariable Long id) {
        log.debug("REST request to delete Evaluaciones : {}", id);
        evaluacionesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
