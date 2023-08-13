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
import py.com.abf.domain.Cursos;
import py.com.abf.repository.CursosRepository;
import py.com.abf.service.CursosQueryService;
import py.com.abf.service.CursosService;
import py.com.abf.service.criteria.CursosCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Cursos}.
 */
@RestController
@RequestMapping("/api")
public class CursosResource {

    private final Logger log = LoggerFactory.getLogger(CursosResource.class);

    private static final String ENTITY_NAME = "cursos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CursosService cursosService;

    private final CursosRepository cursosRepository;

    private final CursosQueryService cursosQueryService;

    public CursosResource(CursosService cursosService, CursosRepository cursosRepository, CursosQueryService cursosQueryService) {
        this.cursosService = cursosService;
        this.cursosRepository = cursosRepository;
        this.cursosQueryService = cursosQueryService;
    }

    /**
     * {@code POST  /cursos} : Create a new cursos.
     *
     * @param cursos the cursos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cursos, or with status {@code 400 (Bad Request)} if the cursos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cursos")
    public ResponseEntity<Cursos> createCursos(@Valid @RequestBody Cursos cursos) throws URISyntaxException {
        log.debug("REST request to save Cursos : {}", cursos);
        if (cursos.getId() != null) {
            throw new BadRequestAlertException("A new cursos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cursos result = cursosService.save(cursos);
        return ResponseEntity
            .created(new URI("/api/cursos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cursos/:id} : Updates an existing cursos.
     *
     * @param id the id of the cursos to save.
     * @param cursos the cursos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cursos,
     * or with status {@code 400 (Bad Request)} if the cursos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cursos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cursos/{id}")
    public ResponseEntity<Cursos> updateCursos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Cursos cursos
    ) throws URISyntaxException {
        log.debug("REST request to update Cursos : {}, {}", id, cursos);
        if (cursos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cursos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cursosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cursos result = cursosService.update(cursos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cursos.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cursos/:id} : Partial updates given fields of an existing cursos, field will ignore if it is null
     *
     * @param id the id of the cursos to save.
     * @param cursos the cursos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cursos,
     * or with status {@code 400 (Bad Request)} if the cursos is not valid,
     * or with status {@code 404 (Not Found)} if the cursos is not found,
     * or with status {@code 500 (Internal Server Error)} if the cursos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cursos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cursos> partialUpdateCursos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Cursos cursos
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cursos partially : {}, {}", id, cursos);
        if (cursos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cursos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cursosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cursos> result = cursosService.partialUpdate(cursos);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cursos.getId().toString())
        );
    }

    /**
     * {@code GET  /cursos} : get all the cursos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cursos in body.
     */
    @GetMapping("/cursos")
    public ResponseEntity<List<Cursos>> getAllCursos(
        CursosCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Cursos by criteria: {}", criteria);
        Page<Cursos> page = cursosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cursos/count} : count all the cursos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cursos/count")
    public ResponseEntity<Long> countCursos(CursosCriteria criteria) {
        log.debug("REST request to count Cursos by criteria: {}", criteria);
        return ResponseEntity.ok().body(cursosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cursos/:id} : get the "id" cursos.
     *
     * @param id the id of the cursos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cursos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cursos/{id}")
    public ResponseEntity<Cursos> getCursos(@PathVariable Long id) {
        log.debug("REST request to get Cursos : {}", id);
        Optional<Cursos> cursos = cursosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cursos);
    }

    /**
     * {@code DELETE  /cursos/:id} : delete the "id" cursos.
     *
     * @param id the id of the cursos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cursos/{id}")
    public ResponseEntity<Void> deleteCursos(@PathVariable Long id) {
        log.debug("REST request to delete Cursos : {}", id);
        cursosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
