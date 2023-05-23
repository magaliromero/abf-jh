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
import py.com.abf.domain.Temas;
import py.com.abf.repository.TemasRepository;
import py.com.abf.service.TemasQueryService;
import py.com.abf.service.TemasService;
import py.com.abf.service.criteria.TemasCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Temas}.
 */
@RestController
@RequestMapping("/api")
public class TemasResource {

    private final Logger log = LoggerFactory.getLogger(TemasResource.class);

    private static final String ENTITY_NAME = "temas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemasService temasService;

    private final TemasRepository temasRepository;

    private final TemasQueryService temasQueryService;

    public TemasResource(TemasService temasService, TemasRepository temasRepository, TemasQueryService temasQueryService) {
        this.temasService = temasService;
        this.temasRepository = temasRepository;
        this.temasQueryService = temasQueryService;
    }

    /**
     * {@code POST  /temas} : Create a new temas.
     *
     * @param temas the temas to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new temas, or with status {@code 400 (Bad Request)} if the temas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/temas")
    public ResponseEntity<Temas> createTemas(@Valid @RequestBody Temas temas) throws URISyntaxException {
        log.debug("REST request to save Temas : {}", temas);
        if (temas.getId() != null) {
            throw new BadRequestAlertException("A new temas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Temas result = temasService.save(temas);
        return ResponseEntity
            .created(new URI("/api/temas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /temas/:id} : Updates an existing temas.
     *
     * @param id the id of the temas to save.
     * @param temas the temas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated temas,
     * or with status {@code 400 (Bad Request)} if the temas is not valid,
     * or with status {@code 500 (Internal Server Error)} if the temas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/temas/{id}")
    public ResponseEntity<Temas> updateTemas(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Temas temas)
        throws URISyntaxException {
        log.debug("REST request to update Temas : {}, {}", id, temas);
        if (temas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, temas.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!temasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Temas result = temasService.update(temas);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, temas.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /temas/:id} : Partial updates given fields of an existing temas, field will ignore if it is null
     *
     * @param id the id of the temas to save.
     * @param temas the temas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated temas,
     * or with status {@code 400 (Bad Request)} if the temas is not valid,
     * or with status {@code 404 (Not Found)} if the temas is not found,
     * or with status {@code 500 (Internal Server Error)} if the temas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/temas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Temas> partialUpdateTemas(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Temas temas
    ) throws URISyntaxException {
        log.debug("REST request to partial update Temas partially : {}, {}", id, temas);
        if (temas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, temas.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!temasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Temas> result = temasService.partialUpdate(temas);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, temas.getId().toString())
        );
    }

    /**
     * {@code GET  /temas} : get all the temas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of temas in body.
     */
    @GetMapping("/temas")
    public ResponseEntity<List<Temas>> getAllTemas(
        TemasCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Temas by criteria: {}", criteria);
        Page<Temas> page = temasQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /temas/count} : count all the temas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/temas/count")
    public ResponseEntity<Long> countTemas(TemasCriteria criteria) {
        log.debug("REST request to count Temas by criteria: {}", criteria);
        return ResponseEntity.ok().body(temasQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /temas/:id} : get the "id" temas.
     *
     * @param id the id of the temas to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the temas, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/temas/{id}")
    public ResponseEntity<Temas> getTemas(@PathVariable Long id) {
        log.debug("REST request to get Temas : {}", id);
        Optional<Temas> temas = temasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(temas);
    }

    /**
     * {@code DELETE  /temas/:id} : delete the "id" temas.
     *
     * @param id the id of the temas to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/temas/{id}")
    public ResponseEntity<Void> deleteTemas(@PathVariable Long id) {
        log.debug("REST request to delete Temas : {}", id);
        temasService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
