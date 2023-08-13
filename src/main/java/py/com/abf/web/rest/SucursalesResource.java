package py.com.abf.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
import py.com.abf.domain.Sucursales;
import py.com.abf.repository.SucursalesRepository;
import py.com.abf.service.SucursalesQueryService;
import py.com.abf.service.SucursalesService;
import py.com.abf.service.criteria.SucursalesCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Sucursales}.
 */
@RestController
@RequestMapping("/api")
public class SucursalesResource {

    private final Logger log = LoggerFactory.getLogger(SucursalesResource.class);

    private static final String ENTITY_NAME = "sucursales";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SucursalesService sucursalesService;

    private final SucursalesRepository sucursalesRepository;

    private final SucursalesQueryService sucursalesQueryService;

    public SucursalesResource(
        SucursalesService sucursalesService,
        SucursalesRepository sucursalesRepository,
        SucursalesQueryService sucursalesQueryService
    ) {
        this.sucursalesService = sucursalesService;
        this.sucursalesRepository = sucursalesRepository;
        this.sucursalesQueryService = sucursalesQueryService;
    }

    /**
     * {@code POST  /sucursales} : Create a new sucursales.
     *
     * @param sucursales the sucursales to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sucursales, or with status {@code 400 (Bad Request)} if the sucursales has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sucursales")
    public ResponseEntity<Sucursales> createSucursales(@Valid @RequestBody Sucursales sucursales) throws URISyntaxException {
        log.debug("REST request to save Sucursales : {}", sucursales);
        if (sucursales.getId() != null) {
            throw new BadRequestAlertException("A new sucursales cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sucursales result = sucursalesService.save(sucursales);
        return ResponseEntity
            .created(new URI("/api/sucursales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sucursales/:id} : Updates an existing sucursales.
     *
     * @param id the id of the sucursales to save.
     * @param sucursales the sucursales to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sucursales,
     * or with status {@code 400 (Bad Request)} if the sucursales is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sucursales couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sucursales/{id}")
    public ResponseEntity<Sucursales> updateSucursales(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Sucursales sucursales
    ) throws URISyntaxException {
        log.debug("REST request to update Sucursales : {}, {}", id, sucursales);
        if (sucursales.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sucursales.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sucursalesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sucursales result = sucursalesService.update(sucursales);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sucursales.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sucursales/:id} : Partial updates given fields of an existing sucursales, field will ignore if it is null
     *
     * @param id the id of the sucursales to save.
     * @param sucursales the sucursales to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sucursales,
     * or with status {@code 400 (Bad Request)} if the sucursales is not valid,
     * or with status {@code 404 (Not Found)} if the sucursales is not found,
     * or with status {@code 500 (Internal Server Error)} if the sucursales couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sucursales/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sucursales> partialUpdateSucursales(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Sucursales sucursales
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sucursales partially : {}, {}", id, sucursales);
        if (sucursales.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sucursales.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sucursalesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sucursales> result = sucursalesService.partialUpdate(sucursales);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sucursales.getId().toString())
        );
    }

    /**
     * {@code GET  /sucursales} : get all the sucursales.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sucursales in body.
     */
    @GetMapping("/sucursales")
    public ResponseEntity<List<Sucursales>> getAllSucursales(
        SucursalesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Sucursales by criteria: {}", criteria);
        Page<Sucursales> page = sucursalesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sucursales/count} : count all the sucursales.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sucursales/count")
    public ResponseEntity<Long> countSucursales(SucursalesCriteria criteria) {
        log.debug("REST request to count Sucursales by criteria: {}", criteria);
        return ResponseEntity.ok().body(sucursalesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sucursales/:id} : get the "id" sucursales.
     *
     * @param id the id of the sucursales to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sucursales, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sucursales/{id}")
    public ResponseEntity<Sucursales> getSucursales(@PathVariable Long id) {
        log.debug("REST request to get Sucursales : {}", id);
        Optional<Sucursales> sucursales = sucursalesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sucursales);
    }

    /**
     * {@code DELETE  /sucursales/:id} : delete the "id" sucursales.
     *
     * @param id the id of the sucursales to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sucursales/{id}")
    public ResponseEntity<Void> deleteSucursales(@PathVariable Long id) {
        log.debug("REST request to delete Sucursales : {}", id);
        sucursalesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
