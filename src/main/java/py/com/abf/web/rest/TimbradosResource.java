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
import py.com.abf.domain.Timbrados;
import py.com.abf.repository.TimbradosRepository;
import py.com.abf.service.TimbradosQueryService;
import py.com.abf.service.TimbradosService;
import py.com.abf.service.criteria.TimbradosCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Timbrados}.
 */
@RestController
@RequestMapping("/api")
public class TimbradosResource {

    private final Logger log = LoggerFactory.getLogger(TimbradosResource.class);

    private static final String ENTITY_NAME = "timbrados";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimbradosService timbradosService;

    private final TimbradosRepository timbradosRepository;

    private final TimbradosQueryService timbradosQueryService;

    public TimbradosResource(
        TimbradosService timbradosService,
        TimbradosRepository timbradosRepository,
        TimbradosQueryService timbradosQueryService
    ) {
        this.timbradosService = timbradosService;
        this.timbradosRepository = timbradosRepository;
        this.timbradosQueryService = timbradosQueryService;
    }

    /**
     * {@code POST  /timbrados} : Create a new timbrados.
     *
     * @param timbrados the timbrados to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timbrados, or with status {@code 400 (Bad Request)} if the timbrados has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/timbrados")
    public ResponseEntity<Timbrados> createTimbrados(@Valid @RequestBody Timbrados timbrados) throws URISyntaxException {
        log.debug("REST request to save Timbrados : {}", timbrados);
        if (timbrados.getId() != null) {
            throw new BadRequestAlertException("A new timbrados cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Timbrados result = timbradosService.save(timbrados);
        return ResponseEntity
            .created(new URI("/api/timbrados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /timbrados/:id} : Updates an existing timbrados.
     *
     * @param id the id of the timbrados to save.
     * @param timbrados the timbrados to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timbrados,
     * or with status {@code 400 (Bad Request)} if the timbrados is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timbrados couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/timbrados/{id}")
    public ResponseEntity<Timbrados> updateTimbrados(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Timbrados timbrados
    ) throws URISyntaxException {
        log.debug("REST request to update Timbrados : {}, {}", id, timbrados);
        if (timbrados.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timbrados.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timbradosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Timbrados result = timbradosService.update(timbrados);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timbrados.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /timbrados/:id} : Partial updates given fields of an existing timbrados, field will ignore if it is null
     *
     * @param id the id of the timbrados to save.
     * @param timbrados the timbrados to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timbrados,
     * or with status {@code 400 (Bad Request)} if the timbrados is not valid,
     * or with status {@code 404 (Not Found)} if the timbrados is not found,
     * or with status {@code 500 (Internal Server Error)} if the timbrados couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/timbrados/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Timbrados> partialUpdateTimbrados(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Timbrados timbrados
    ) throws URISyntaxException {
        log.debug("REST request to partial update Timbrados partially : {}, {}", id, timbrados);
        if (timbrados.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timbrados.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timbradosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Timbrados> result = timbradosService.partialUpdate(timbrados);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timbrados.getId().toString())
        );
    }

    /**
     * {@code GET  /timbrados} : get all the timbrados.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timbrados in body.
     */
    @GetMapping("/timbrados")
    public ResponseEntity<List<Timbrados>> getAllTimbrados(
        TimbradosCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Timbrados by criteria: {}", criteria);
        Page<Timbrados> page = timbradosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /timbrados/count} : count all the timbrados.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/timbrados/count")
    public ResponseEntity<Long> countTimbrados(TimbradosCriteria criteria) {
        log.debug("REST request to count Timbrados by criteria: {}", criteria);
        return ResponseEntity.ok().body(timbradosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /timbrados/:id} : get the "id" timbrados.
     *
     * @param id the id of the timbrados to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timbrados, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/timbrados/{id}")
    public ResponseEntity<Timbrados> getTimbrados(@PathVariable Long id) {
        log.debug("REST request to get Timbrados : {}", id);
        Optional<Timbrados> timbrados = timbradosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timbrados);
    }

    /**
     * {@code DELETE  /timbrados/:id} : delete the "id" timbrados.
     *
     * @param id the id of the timbrados to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/timbrados/{id}")
    public ResponseEntity<Void> deleteTimbrados(@PathVariable Long id) {
        log.debug("REST request to delete Timbrados : {}", id);
        timbradosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
