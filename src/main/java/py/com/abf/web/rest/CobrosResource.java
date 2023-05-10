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
import py.com.abf.domain.Cobros;
import py.com.abf.repository.CobrosRepository;
import py.com.abf.service.CobrosQueryService;
import py.com.abf.service.CobrosService;
import py.com.abf.service.criteria.CobrosCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Cobros}.
 */
@RestController
@RequestMapping("/api")
public class CobrosResource {

    private final Logger log = LoggerFactory.getLogger(CobrosResource.class);

    private static final String ENTITY_NAME = "cobros";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CobrosService cobrosService;

    private final CobrosRepository cobrosRepository;

    private final CobrosQueryService cobrosQueryService;

    public CobrosResource(CobrosService cobrosService, CobrosRepository cobrosRepository, CobrosQueryService cobrosQueryService) {
        this.cobrosService = cobrosService;
        this.cobrosRepository = cobrosRepository;
        this.cobrosQueryService = cobrosQueryService;
    }

    /**
     * {@code POST  /cobros} : Create a new cobros.
     *
     * @param cobros the cobros to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cobros, or with status {@code 400 (Bad Request)} if the cobros has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cobros")
    public ResponseEntity<Cobros> createCobros(@Valid @RequestBody Cobros cobros) throws URISyntaxException {
        log.debug("REST request to save Cobros : {}", cobros);
        if (cobros.getId() != null) {
            throw new BadRequestAlertException("A new cobros cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cobros result = cobrosService.save(cobros);
        return ResponseEntity
            .created(new URI("/api/cobros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cobros/:id} : Updates an existing cobros.
     *
     * @param id the id of the cobros to save.
     * @param cobros the cobros to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cobros,
     * or with status {@code 400 (Bad Request)} if the cobros is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cobros couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cobros/{id}")
    public ResponseEntity<Cobros> updateCobros(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Cobros cobros
    ) throws URISyntaxException {
        log.debug("REST request to update Cobros : {}, {}", id, cobros);
        if (cobros.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cobros.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cobrosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cobros result = cobrosService.update(cobros);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cobros.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cobros/:id} : Partial updates given fields of an existing cobros, field will ignore if it is null
     *
     * @param id the id of the cobros to save.
     * @param cobros the cobros to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cobros,
     * or with status {@code 400 (Bad Request)} if the cobros is not valid,
     * or with status {@code 404 (Not Found)} if the cobros is not found,
     * or with status {@code 500 (Internal Server Error)} if the cobros couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cobros/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cobros> partialUpdateCobros(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Cobros cobros
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cobros partially : {}, {}", id, cobros);
        if (cobros.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cobros.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cobrosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cobros> result = cobrosService.partialUpdate(cobros);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cobros.getId().toString())
        );
    }

    /**
     * {@code GET  /cobros} : get all the cobros.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cobros in body.
     */
    @GetMapping("/cobros")
    public ResponseEntity<List<Cobros>> getAllCobros(
        CobrosCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Cobros by criteria: {}", criteria);
        Page<Cobros> page = cobrosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cobros/count} : count all the cobros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cobros/count")
    public ResponseEntity<Long> countCobros(CobrosCriteria criteria) {
        log.debug("REST request to count Cobros by criteria: {}", criteria);
        return ResponseEntity.ok().body(cobrosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cobros/:id} : get the "id" cobros.
     *
     * @param id the id of the cobros to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cobros, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cobros/{id}")
    public ResponseEntity<Cobros> getCobros(@PathVariable Long id) {
        log.debug("REST request to get Cobros : {}", id);
        Optional<Cobros> cobros = cobrosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cobros);
    }

    /**
     * {@code DELETE  /cobros/:id} : delete the "id" cobros.
     *
     * @param id the id of the cobros to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cobros/{id}")
    public ResponseEntity<Void> deleteCobros(@PathVariable Long id) {
        log.debug("REST request to delete Cobros : {}", id);
        cobrosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
