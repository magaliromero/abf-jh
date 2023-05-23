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
import py.com.abf.domain.Materiales;
import py.com.abf.repository.MaterialesRepository;
import py.com.abf.service.MaterialesQueryService;
import py.com.abf.service.MaterialesService;
import py.com.abf.service.criteria.MaterialesCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Materiales}.
 */
@RestController
@RequestMapping("/api")
public class MaterialesResource {

    private final Logger log = LoggerFactory.getLogger(MaterialesResource.class);

    private static final String ENTITY_NAME = "materiales";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialesService materialesService;

    private final MaterialesRepository materialesRepository;

    private final MaterialesQueryService materialesQueryService;

    public MaterialesResource(
        MaterialesService materialesService,
        MaterialesRepository materialesRepository,
        MaterialesQueryService materialesQueryService
    ) {
        this.materialesService = materialesService;
        this.materialesRepository = materialesRepository;
        this.materialesQueryService = materialesQueryService;
    }

    /**
     * {@code POST  /materiales} : Create a new materiales.
     *
     * @param materiales the materiales to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materiales, or with status {@code 400 (Bad Request)} if the materiales has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/materiales")
    public ResponseEntity<Materiales> createMateriales(@Valid @RequestBody Materiales materiales) throws URISyntaxException {
        log.debug("REST request to save Materiales : {}", materiales);
        if (materiales.getId() != null) {
            throw new BadRequestAlertException("A new materiales cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Materiales result = materialesService.save(materiales);
        return ResponseEntity
            .created(new URI("/api/materiales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /materiales/:id} : Updates an existing materiales.
     *
     * @param id the id of the materiales to save.
     * @param materiales the materiales to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materiales,
     * or with status {@code 400 (Bad Request)} if the materiales is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materiales couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/materiales/{id}")
    public ResponseEntity<Materiales> updateMateriales(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Materiales materiales
    ) throws URISyntaxException {
        log.debug("REST request to update Materiales : {}, {}", id, materiales);
        if (materiales.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materiales.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Materiales result = materialesService.update(materiales);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materiales.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /materiales/:id} : Partial updates given fields of an existing materiales, field will ignore if it is null
     *
     * @param id the id of the materiales to save.
     * @param materiales the materiales to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materiales,
     * or with status {@code 400 (Bad Request)} if the materiales is not valid,
     * or with status {@code 404 (Not Found)} if the materiales is not found,
     * or with status {@code 500 (Internal Server Error)} if the materiales couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/materiales/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Materiales> partialUpdateMateriales(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Materiales materiales
    ) throws URISyntaxException {
        log.debug("REST request to partial update Materiales partially : {}, {}", id, materiales);
        if (materiales.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materiales.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Materiales> result = materialesService.partialUpdate(materiales);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materiales.getId().toString())
        );
    }

    /**
     * {@code GET  /materiales} : get all the materiales.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materiales in body.
     */
    @GetMapping("/materiales")
    public ResponseEntity<List<Materiales>> getAllMateriales(
        MaterialesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Materiales by criteria: {}", criteria);
        Page<Materiales> page = materialesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /materiales/count} : count all the materiales.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/materiales/count")
    public ResponseEntity<Long> countMateriales(MaterialesCriteria criteria) {
        log.debug("REST request to count Materiales by criteria: {}", criteria);
        return ResponseEntity.ok().body(materialesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /materiales/:id} : get the "id" materiales.
     *
     * @param id the id of the materiales to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materiales, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/materiales/{id}")
    public ResponseEntity<Materiales> getMateriales(@PathVariable Long id) {
        log.debug("REST request to get Materiales : {}", id);
        Optional<Materiales> materiales = materialesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materiales);
    }

    /**
     * {@code DELETE  /materiales/:id} : delete the "id" materiales.
     *
     * @param id the id of the materiales to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/materiales/{id}")
    public ResponseEntity<Void> deleteMateriales(@PathVariable Long id) {
        log.debug("REST request to delete Materiales : {}", id);
        materialesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
