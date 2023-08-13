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
import py.com.abf.domain.PuntoDeExpedicion;
import py.com.abf.repository.PuntoDeExpedicionRepository;
import py.com.abf.service.PuntoDeExpedicionQueryService;
import py.com.abf.service.PuntoDeExpedicionService;
import py.com.abf.service.criteria.PuntoDeExpedicionCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.PuntoDeExpedicion}.
 */
@RestController
@RequestMapping("/api")
public class PuntoDeExpedicionResource {

    private final Logger log = LoggerFactory.getLogger(PuntoDeExpedicionResource.class);

    private static final String ENTITY_NAME = "puntoDeExpedicion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PuntoDeExpedicionService puntoDeExpedicionService;

    private final PuntoDeExpedicionRepository puntoDeExpedicionRepository;

    private final PuntoDeExpedicionQueryService puntoDeExpedicionQueryService;

    public PuntoDeExpedicionResource(
        PuntoDeExpedicionService puntoDeExpedicionService,
        PuntoDeExpedicionRepository puntoDeExpedicionRepository,
        PuntoDeExpedicionQueryService puntoDeExpedicionQueryService
    ) {
        this.puntoDeExpedicionService = puntoDeExpedicionService;
        this.puntoDeExpedicionRepository = puntoDeExpedicionRepository;
        this.puntoDeExpedicionQueryService = puntoDeExpedicionQueryService;
    }

    /**
     * {@code POST  /punto-de-expedicions} : Create a new puntoDeExpedicion.
     *
     * @param puntoDeExpedicion the puntoDeExpedicion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new puntoDeExpedicion, or with status {@code 400 (Bad Request)} if the puntoDeExpedicion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/punto-de-expedicions")
    public ResponseEntity<PuntoDeExpedicion> createPuntoDeExpedicion(@Valid @RequestBody PuntoDeExpedicion puntoDeExpedicion)
        throws URISyntaxException {
        log.debug("REST request to save PuntoDeExpedicion : {}", puntoDeExpedicion);
        if (puntoDeExpedicion.getId() != null) {
            throw new BadRequestAlertException("A new puntoDeExpedicion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PuntoDeExpedicion result = puntoDeExpedicionService.save(puntoDeExpedicion);
        return ResponseEntity
            .created(new URI("/api/punto-de-expedicions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /punto-de-expedicions/:id} : Updates an existing puntoDeExpedicion.
     *
     * @param id the id of the puntoDeExpedicion to save.
     * @param puntoDeExpedicion the puntoDeExpedicion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated puntoDeExpedicion,
     * or with status {@code 400 (Bad Request)} if the puntoDeExpedicion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the puntoDeExpedicion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/punto-de-expedicions/{id}")
    public ResponseEntity<PuntoDeExpedicion> updatePuntoDeExpedicion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PuntoDeExpedicion puntoDeExpedicion
    ) throws URISyntaxException {
        log.debug("REST request to update PuntoDeExpedicion : {}, {}", id, puntoDeExpedicion);
        if (puntoDeExpedicion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, puntoDeExpedicion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!puntoDeExpedicionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PuntoDeExpedicion result = puntoDeExpedicionService.update(puntoDeExpedicion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, puntoDeExpedicion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /punto-de-expedicions/:id} : Partial updates given fields of an existing puntoDeExpedicion, field will ignore if it is null
     *
     * @param id the id of the puntoDeExpedicion to save.
     * @param puntoDeExpedicion the puntoDeExpedicion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated puntoDeExpedicion,
     * or with status {@code 400 (Bad Request)} if the puntoDeExpedicion is not valid,
     * or with status {@code 404 (Not Found)} if the puntoDeExpedicion is not found,
     * or with status {@code 500 (Internal Server Error)} if the puntoDeExpedicion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/punto-de-expedicions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PuntoDeExpedicion> partialUpdatePuntoDeExpedicion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PuntoDeExpedicion puntoDeExpedicion
    ) throws URISyntaxException {
        log.debug("REST request to partial update PuntoDeExpedicion partially : {}, {}", id, puntoDeExpedicion);
        if (puntoDeExpedicion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, puntoDeExpedicion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!puntoDeExpedicionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PuntoDeExpedicion> result = puntoDeExpedicionService.partialUpdate(puntoDeExpedicion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, puntoDeExpedicion.getId().toString())
        );
    }

    /**
     * {@code GET  /punto-de-expedicions} : get all the puntoDeExpedicions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of puntoDeExpedicions in body.
     */
    @GetMapping("/punto-de-expedicions")
    public ResponseEntity<List<PuntoDeExpedicion>> getAllPuntoDeExpedicions(
        PuntoDeExpedicionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PuntoDeExpedicions by criteria: {}", criteria);
        Page<PuntoDeExpedicion> page = puntoDeExpedicionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /punto-de-expedicions/count} : count all the puntoDeExpedicions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/punto-de-expedicions/count")
    public ResponseEntity<Long> countPuntoDeExpedicions(PuntoDeExpedicionCriteria criteria) {
        log.debug("REST request to count PuntoDeExpedicions by criteria: {}", criteria);
        return ResponseEntity.ok().body(puntoDeExpedicionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /punto-de-expedicions/:id} : get the "id" puntoDeExpedicion.
     *
     * @param id the id of the puntoDeExpedicion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the puntoDeExpedicion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/punto-de-expedicions/{id}")
    public ResponseEntity<PuntoDeExpedicion> getPuntoDeExpedicion(@PathVariable Long id) {
        log.debug("REST request to get PuntoDeExpedicion : {}", id);
        Optional<PuntoDeExpedicion> puntoDeExpedicion = puntoDeExpedicionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(puntoDeExpedicion);
    }

    /**
     * {@code DELETE  /punto-de-expedicions/:id} : delete the "id" puntoDeExpedicion.
     *
     * @param id the id of the puntoDeExpedicion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/punto-de-expedicions/{id}")
    public ResponseEntity<Void> deletePuntoDeExpedicion(@PathVariable Long id) {
        log.debug("REST request to delete PuntoDeExpedicion : {}", id);
        puntoDeExpedicionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
