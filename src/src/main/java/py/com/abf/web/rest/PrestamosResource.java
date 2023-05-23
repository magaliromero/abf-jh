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
import py.com.abf.domain.Prestamos;
import py.com.abf.repository.PrestamosRepository;
import py.com.abf.service.PrestamosQueryService;
import py.com.abf.service.PrestamosService;
import py.com.abf.service.criteria.PrestamosCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Prestamos}.
 */
@RestController
@RequestMapping("/api")
public class PrestamosResource {

    private final Logger log = LoggerFactory.getLogger(PrestamosResource.class);

    private static final String ENTITY_NAME = "prestamos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrestamosService prestamosService;

    private final PrestamosRepository prestamosRepository;

    private final PrestamosQueryService prestamosQueryService;

    public PrestamosResource(
        PrestamosService prestamosService,
        PrestamosRepository prestamosRepository,
        PrestamosQueryService prestamosQueryService
    ) {
        this.prestamosService = prestamosService;
        this.prestamosRepository = prestamosRepository;
        this.prestamosQueryService = prestamosQueryService;
    }

    /**
     * {@code POST  /prestamos} : Create a new prestamos.
     *
     * @param prestamos the prestamos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prestamos, or with status {@code 400 (Bad Request)} if the prestamos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prestamos")
    public ResponseEntity<Prestamos> createPrestamos(@Valid @RequestBody Prestamos prestamos) throws URISyntaxException {
        log.debug("REST request to save Prestamos : {}", prestamos);
        if (prestamos.getId() != null) {
            throw new BadRequestAlertException("A new prestamos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prestamos result = prestamosService.save(prestamos);
        return ResponseEntity
            .created(new URI("/api/prestamos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prestamos/:id} : Updates an existing prestamos.
     *
     * @param id the id of the prestamos to save.
     * @param prestamos the prestamos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestamos,
     * or with status {@code 400 (Bad Request)} if the prestamos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prestamos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prestamos/{id}")
    public ResponseEntity<Prestamos> updatePrestamos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Prestamos prestamos
    ) throws URISyntaxException {
        log.debug("REST request to update Prestamos : {}, {}", id, prestamos);
        if (prestamos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestamos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prestamosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Prestamos result = prestamosService.update(prestamos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prestamos.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prestamos/:id} : Partial updates given fields of an existing prestamos, field will ignore if it is null
     *
     * @param id the id of the prestamos to save.
     * @param prestamos the prestamos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestamos,
     * or with status {@code 400 (Bad Request)} if the prestamos is not valid,
     * or with status {@code 404 (Not Found)} if the prestamos is not found,
     * or with status {@code 500 (Internal Server Error)} if the prestamos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prestamos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Prestamos> partialUpdatePrestamos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Prestamos prestamos
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prestamos partially : {}, {}", id, prestamos);
        if (prestamos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestamos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prestamosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Prestamos> result = prestamosService.partialUpdate(prestamos);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prestamos.getId().toString())
        );
    }

    /**
     * {@code GET  /prestamos} : get all the prestamos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prestamos in body.
     */
    @GetMapping("/prestamos")
    public ResponseEntity<List<Prestamos>> getAllPrestamos(
        PrestamosCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Prestamos by criteria: {}", criteria);
        Page<Prestamos> page = prestamosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prestamos/count} : count all the prestamos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prestamos/count")
    public ResponseEntity<Long> countPrestamos(PrestamosCriteria criteria) {
        log.debug("REST request to count Prestamos by criteria: {}", criteria);
        return ResponseEntity.ok().body(prestamosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prestamos/:id} : get the "id" prestamos.
     *
     * @param id the id of the prestamos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prestamos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prestamos/{id}")
    public ResponseEntity<Prestamos> getPrestamos(@PathVariable Long id) {
        log.debug("REST request to get Prestamos : {}", id);
        Optional<Prestamos> prestamos = prestamosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prestamos);
    }

    /**
     * {@code DELETE  /prestamos/:id} : delete the "id" prestamos.
     *
     * @param id the id of the prestamos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prestamos/{id}")
    public ResponseEntity<Void> deletePrestamos(@PathVariable Long id) {
        log.debug("REST request to delete Prestamos : {}", id);
        prestamosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
