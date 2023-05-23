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
import py.com.abf.domain.Alumnos;
import py.com.abf.repository.AlumnosRepository;
import py.com.abf.service.AlumnosQueryService;
import py.com.abf.service.AlumnosService;
import py.com.abf.service.criteria.AlumnosCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Alumnos}.
 */
@RestController
@RequestMapping("/api")
public class AlumnosResource {

    private final Logger log = LoggerFactory.getLogger(AlumnosResource.class);

    private static final String ENTITY_NAME = "alumnos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlumnosService alumnosService;

    private final AlumnosRepository alumnosRepository;

    private final AlumnosQueryService alumnosQueryService;

    public AlumnosResource(AlumnosService alumnosService, AlumnosRepository alumnosRepository, AlumnosQueryService alumnosQueryService) {
        this.alumnosService = alumnosService;
        this.alumnosRepository = alumnosRepository;
        this.alumnosQueryService = alumnosQueryService;
    }

    /**
     * {@code POST  /alumnos} : Create a new alumnos.
     *
     * @param alumnos the alumnos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alumnos, or with status {@code 400 (Bad Request)} if the alumnos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alumnos")
    public ResponseEntity<Alumnos> createAlumnos(@Valid @RequestBody Alumnos alumnos) throws URISyntaxException {
        log.debug("REST request to save Alumnos : {}", alumnos);
        if (alumnos.getId() != null) {
            throw new BadRequestAlertException("A new alumnos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Alumnos result = alumnosService.save(alumnos);
        return ResponseEntity
            .created(new URI("/api/alumnos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alumnos/:id} : Updates an existing alumnos.
     *
     * @param id the id of the alumnos to save.
     * @param alumnos the alumnos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alumnos,
     * or with status {@code 400 (Bad Request)} if the alumnos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alumnos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alumnos/{id}")
    public ResponseEntity<Alumnos> updateAlumnos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Alumnos alumnos
    ) throws URISyntaxException {
        log.debug("REST request to update Alumnos : {}, {}", id, alumnos);
        if (alumnos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alumnos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alumnosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Alumnos result = alumnosService.update(alumnos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alumnos.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /alumnos/:id} : Partial updates given fields of an existing alumnos, field will ignore if it is null
     *
     * @param id the id of the alumnos to save.
     * @param alumnos the alumnos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alumnos,
     * or with status {@code 400 (Bad Request)} if the alumnos is not valid,
     * or with status {@code 404 (Not Found)} if the alumnos is not found,
     * or with status {@code 500 (Internal Server Error)} if the alumnos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/alumnos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Alumnos> partialUpdateAlumnos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Alumnos alumnos
    ) throws URISyntaxException {
        log.debug("REST request to partial update Alumnos partially : {}, {}", id, alumnos);
        if (alumnos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alumnos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alumnosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Alumnos> result = alumnosService.partialUpdate(alumnos);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alumnos.getId().toString())
        );
    }

    /**
     * {@code GET  /alumnos} : get all the alumnos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alumnos in body.
     */
    @GetMapping("/alumnos")
    public ResponseEntity<List<Alumnos>> getAllAlumnos(
        AlumnosCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Alumnos by criteria: {}", criteria);
        Page<Alumnos> page = alumnosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /alumnos/count} : count all the alumnos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/alumnos/count")
    public ResponseEntity<Long> countAlumnos(AlumnosCriteria criteria) {
        log.debug("REST request to count Alumnos by criteria: {}", criteria);
        return ResponseEntity.ok().body(alumnosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /alumnos/:id} : get the "id" alumnos.
     *
     * @param id the id of the alumnos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alumnos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alumnos/{id}")
    public ResponseEntity<Alumnos> getAlumnos(@PathVariable Long id) {
        log.debug("REST request to get Alumnos : {}", id);
        Optional<Alumnos> alumnos = alumnosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alumnos);
    }

    /**
     * {@code DELETE  /alumnos/:id} : delete the "id" alumnos.
     *
     * @param id the id of the alumnos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alumnos/{id}")
    public ResponseEntity<Void> deleteAlumnos(@PathVariable Long id) {
        log.debug("REST request to delete Alumnos : {}", id);
        alumnosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
