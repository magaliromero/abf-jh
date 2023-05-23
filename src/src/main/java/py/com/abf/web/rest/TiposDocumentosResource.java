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
import py.com.abf.domain.TiposDocumentos;
import py.com.abf.repository.TiposDocumentosRepository;
import py.com.abf.service.TiposDocumentosQueryService;
import py.com.abf.service.TiposDocumentosService;
import py.com.abf.service.criteria.TiposDocumentosCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.TiposDocumentos}.
 */
@RestController
@RequestMapping("/api")
public class TiposDocumentosResource {

    private final Logger log = LoggerFactory.getLogger(TiposDocumentosResource.class);

    private static final String ENTITY_NAME = "tiposDocumentos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TiposDocumentosService tiposDocumentosService;

    private final TiposDocumentosRepository tiposDocumentosRepository;

    private final TiposDocumentosQueryService tiposDocumentosQueryService;

    public TiposDocumentosResource(
        TiposDocumentosService tiposDocumentosService,
        TiposDocumentosRepository tiposDocumentosRepository,
        TiposDocumentosQueryService tiposDocumentosQueryService
    ) {
        this.tiposDocumentosService = tiposDocumentosService;
        this.tiposDocumentosRepository = tiposDocumentosRepository;
        this.tiposDocumentosQueryService = tiposDocumentosQueryService;
    }

    /**
     * {@code POST  /tipos-documentos} : Create a new tiposDocumentos.
     *
     * @param tiposDocumentos the tiposDocumentos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tiposDocumentos, or with status {@code 400 (Bad Request)} if the tiposDocumentos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipos-documentos")
    public ResponseEntity<TiposDocumentos> createTiposDocumentos(@Valid @RequestBody TiposDocumentos tiposDocumentos)
        throws URISyntaxException {
        log.debug("REST request to save TiposDocumentos : {}", tiposDocumentos);
        if (tiposDocumentos.getId() != null) {
            throw new BadRequestAlertException("A new tiposDocumentos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TiposDocumentos result = tiposDocumentosService.save(tiposDocumentos);
        return ResponseEntity
            .created(new URI("/api/tipos-documentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipos-documentos/:id} : Updates an existing tiposDocumentos.
     *
     * @param id the id of the tiposDocumentos to save.
     * @param tiposDocumentos the tiposDocumentos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tiposDocumentos,
     * or with status {@code 400 (Bad Request)} if the tiposDocumentos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tiposDocumentos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipos-documentos/{id}")
    public ResponseEntity<TiposDocumentos> updateTiposDocumentos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TiposDocumentos tiposDocumentos
    ) throws URISyntaxException {
        log.debug("REST request to update TiposDocumentos : {}, {}", id, tiposDocumentos);
        if (tiposDocumentos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tiposDocumentos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tiposDocumentosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TiposDocumentos result = tiposDocumentosService.update(tiposDocumentos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tiposDocumentos.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipos-documentos/:id} : Partial updates given fields of an existing tiposDocumentos, field will ignore if it is null
     *
     * @param id the id of the tiposDocumentos to save.
     * @param tiposDocumentos the tiposDocumentos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tiposDocumentos,
     * or with status {@code 400 (Bad Request)} if the tiposDocumentos is not valid,
     * or with status {@code 404 (Not Found)} if the tiposDocumentos is not found,
     * or with status {@code 500 (Internal Server Error)} if the tiposDocumentos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipos-documentos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TiposDocumentos> partialUpdateTiposDocumentos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TiposDocumentos tiposDocumentos
    ) throws URISyntaxException {
        log.debug("REST request to partial update TiposDocumentos partially : {}, {}", id, tiposDocumentos);
        if (tiposDocumentos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tiposDocumentos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tiposDocumentosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TiposDocumentos> result = tiposDocumentosService.partialUpdate(tiposDocumentos);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tiposDocumentos.getId().toString())
        );
    }

    /**
     * {@code GET  /tipos-documentos} : get all the tiposDocumentos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tiposDocumentos in body.
     */
    @GetMapping("/tipos-documentos")
    public ResponseEntity<List<TiposDocumentos>> getAllTiposDocumentos(
        TiposDocumentosCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TiposDocumentos by criteria: {}", criteria);
        Page<TiposDocumentos> page = tiposDocumentosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipos-documentos/count} : count all the tiposDocumentos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tipos-documentos/count")
    public ResponseEntity<Long> countTiposDocumentos(TiposDocumentosCriteria criteria) {
        log.debug("REST request to count TiposDocumentos by criteria: {}", criteria);
        return ResponseEntity.ok().body(tiposDocumentosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tipos-documentos/:id} : get the "id" tiposDocumentos.
     *
     * @param id the id of the tiposDocumentos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tiposDocumentos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipos-documentos/{id}")
    public ResponseEntity<TiposDocumentos> getTiposDocumentos(@PathVariable Long id) {
        log.debug("REST request to get TiposDocumentos : {}", id);
        Optional<TiposDocumentos> tiposDocumentos = tiposDocumentosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tiposDocumentos);
    }

    /**
     * {@code DELETE  /tipos-documentos/:id} : delete the "id" tiposDocumentos.
     *
     * @param id the id of the tiposDocumentos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipos-documentos/{id}")
    public ResponseEntity<Void> deleteTiposDocumentos(@PathVariable Long id) {
        log.debug("REST request to delete TiposDocumentos : {}", id);
        tiposDocumentosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
