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
import py.com.abf.domain.FacturaDetalle;
import py.com.abf.repository.FacturaDetalleRepository;
import py.com.abf.service.FacturaDetalleQueryService;
import py.com.abf.service.FacturaDetalleService;
import py.com.abf.service.criteria.FacturaDetalleCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.FacturaDetalle}.
 */
@RestController
@RequestMapping("/api")
public class FacturaDetalleResource {

    private final Logger log = LoggerFactory.getLogger(FacturaDetalleResource.class);

    private static final String ENTITY_NAME = "facturaDetalle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacturaDetalleService facturaDetalleService;

    private final FacturaDetalleRepository facturaDetalleRepository;

    private final FacturaDetalleQueryService facturaDetalleQueryService;

    public FacturaDetalleResource(
        FacturaDetalleService facturaDetalleService,
        FacturaDetalleRepository facturaDetalleRepository,
        FacturaDetalleQueryService facturaDetalleQueryService
    ) {
        this.facturaDetalleService = facturaDetalleService;
        this.facturaDetalleRepository = facturaDetalleRepository;
        this.facturaDetalleQueryService = facturaDetalleQueryService;
    }

    /**
     * {@code POST  /factura-detalles} : Create a new facturaDetalle.
     *
     * @param facturaDetalle the facturaDetalle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facturaDetalle, or with status {@code 400 (Bad Request)} if the facturaDetalle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/factura-detalles")
    public ResponseEntity<FacturaDetalle> createFacturaDetalle(@Valid @RequestBody FacturaDetalle facturaDetalle)
        throws URISyntaxException {
        log.debug("REST request to save FacturaDetalle : {}", facturaDetalle);
        if (facturaDetalle.getId() != null) {
            throw new BadRequestAlertException("A new facturaDetalle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacturaDetalle result = facturaDetalleService.save(facturaDetalle);
        return ResponseEntity
            .created(new URI("/api/factura-detalles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /factura-detalles/:id} : Updates an existing facturaDetalle.
     *
     * @param id the id of the facturaDetalle to save.
     * @param facturaDetalle the facturaDetalle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturaDetalle,
     * or with status {@code 400 (Bad Request)} if the facturaDetalle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facturaDetalle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/factura-detalles/{id}")
    public ResponseEntity<FacturaDetalle> updateFacturaDetalle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacturaDetalle facturaDetalle
    ) throws URISyntaxException {
        log.debug("REST request to update FacturaDetalle : {}, {}", id, facturaDetalle);
        if (facturaDetalle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facturaDetalle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facturaDetalleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacturaDetalle result = facturaDetalleService.update(facturaDetalle);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facturaDetalle.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /factura-detalles/:id} : Partial updates given fields of an existing facturaDetalle, field will ignore if it is null
     *
     * @param id the id of the facturaDetalle to save.
     * @param facturaDetalle the facturaDetalle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturaDetalle,
     * or with status {@code 400 (Bad Request)} if the facturaDetalle is not valid,
     * or with status {@code 404 (Not Found)} if the facturaDetalle is not found,
     * or with status {@code 500 (Internal Server Error)} if the facturaDetalle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/factura-detalles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacturaDetalle> partialUpdateFacturaDetalle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacturaDetalle facturaDetalle
    ) throws URISyntaxException {
        log.debug("REST request to partial update FacturaDetalle partially : {}, {}", id, facturaDetalle);
        if (facturaDetalle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facturaDetalle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facturaDetalleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacturaDetalle> result = facturaDetalleService.partialUpdate(facturaDetalle);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facturaDetalle.getId().toString())
        );
    }

    /**
     * {@code GET  /factura-detalles} : get all the facturaDetalles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facturaDetalles in body.
     */
    @GetMapping("/factura-detalles")
    public ResponseEntity<List<FacturaDetalle>> getAllFacturaDetalles(
        FacturaDetalleCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FacturaDetalles by criteria: {}", criteria);
        Page<FacturaDetalle> page = facturaDetalleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /factura-detalles/count} : count all the facturaDetalles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/factura-detalles/count")
    public ResponseEntity<Long> countFacturaDetalles(FacturaDetalleCriteria criteria) {
        log.debug("REST request to count FacturaDetalles by criteria: {}", criteria);
        return ResponseEntity.ok().body(facturaDetalleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /factura-detalles/:id} : get the "id" facturaDetalle.
     *
     * @param id the id of the facturaDetalle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facturaDetalle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/factura-detalles/{id}")
    public ResponseEntity<FacturaDetalle> getFacturaDetalle(@PathVariable Long id) {
        log.debug("REST request to get FacturaDetalle : {}", id);
        Optional<FacturaDetalle> facturaDetalle = facturaDetalleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facturaDetalle);
    }

    /**
     * {@code DELETE  /factura-detalles/:id} : delete the "id" facturaDetalle.
     *
     * @param id the id of the facturaDetalle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/factura-detalles/{id}")
    public ResponseEntity<Void> deleteFacturaDetalle(@PathVariable Long id) {
        log.debug("REST request to delete FacturaDetalle : {}", id);
        facturaDetalleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
