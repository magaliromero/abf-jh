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
import py.com.abf.domain.Pagos;
import py.com.abf.repository.PagosRepository;
import py.com.abf.service.PagosQueryService;
import py.com.abf.service.PagosService;
import py.com.abf.service.criteria.PagosCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Pagos}.
 */
@RestController
@RequestMapping("/api")
public class PagosResource {

    private final Logger log = LoggerFactory.getLogger(PagosResource.class);

    private static final String ENTITY_NAME = "pagos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PagosService pagosService;

    private final PagosRepository pagosRepository;

    private final PagosQueryService pagosQueryService;

    public PagosResource(PagosService pagosService, PagosRepository pagosRepository, PagosQueryService pagosQueryService) {
        this.pagosService = pagosService;
        this.pagosRepository = pagosRepository;
        this.pagosQueryService = pagosQueryService;
    }

    /**
     * {@code POST  /pagos} : Create a new pagos.
     *
     * @param pagos the pagos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pagos, or with status {@code 400 (Bad Request)} if the pagos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pagos")
    public ResponseEntity<Pagos> createPagos(@Valid @RequestBody Pagos pagos) throws URISyntaxException {
        log.debug("REST request to save Pagos : {}", pagos);
        if (pagos.getId() != null) {
            throw new BadRequestAlertException("A new pagos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pagos result = pagosService.save(pagos);
        return ResponseEntity
            .created(new URI("/api/pagos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pagos/:id} : Updates an existing pagos.
     *
     * @param id the id of the pagos to save.
     * @param pagos the pagos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pagos,
     * or with status {@code 400 (Bad Request)} if the pagos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pagos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pagos/{id}")
    public ResponseEntity<Pagos> updatePagos(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Pagos pagos)
        throws URISyntaxException {
        log.debug("REST request to update Pagos : {}, {}", id, pagos);
        if (pagos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pagos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pagosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pagos result = pagosService.update(pagos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pagos.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pagos/:id} : Partial updates given fields of an existing pagos, field will ignore if it is null
     *
     * @param id the id of the pagos to save.
     * @param pagos the pagos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pagos,
     * or with status {@code 400 (Bad Request)} if the pagos is not valid,
     * or with status {@code 404 (Not Found)} if the pagos is not found,
     * or with status {@code 500 (Internal Server Error)} if the pagos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pagos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pagos> partialUpdatePagos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Pagos pagos
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pagos partially : {}, {}", id, pagos);
        if (pagos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pagos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pagosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pagos> result = pagosService.partialUpdate(pagos);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pagos.getId().toString())
        );
    }

    /**
     * {@code GET  /pagos} : get all the pagos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pagos in body.
     */
    @GetMapping("/pagos")
    public ResponseEntity<List<Pagos>> getAllPagos(
        PagosCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Pagos by criteria: {}", criteria);
        Page<Pagos> page = pagosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pagos/count} : count all the pagos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pagos/count")
    public ResponseEntity<Long> countPagos(PagosCriteria criteria) {
        log.debug("REST request to count Pagos by criteria: {}", criteria);
        return ResponseEntity.ok().body(pagosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pagos/:id} : get the "id" pagos.
     *
     * @param id the id of the pagos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pagos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pagos/{id}")
    public ResponseEntity<Pagos> getPagos(@PathVariable Long id) {
        log.debug("REST request to get Pagos : {}", id);
        Optional<Pagos> pagos = pagosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pagos);
    }

    /**
     * {@code DELETE  /pagos/:id} : delete the "id" pagos.
     *
     * @param id the id of the pagos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pagos/{id}")
    public ResponseEntity<Void> deletePagos(@PathVariable Long id) {
        log.debug("REST request to delete Pagos : {}", id);
        pagosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
