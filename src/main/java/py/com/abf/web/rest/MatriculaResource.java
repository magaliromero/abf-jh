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
import py.com.abf.domain.Matricula;
import py.com.abf.repository.MatriculaRepository;
import py.com.abf.service.MatriculaQueryService;
import py.com.abf.service.MatriculaService;
import py.com.abf.service.criteria.MatriculaCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Matricula}.
 */
@RestController
@RequestMapping("/api")
public class MatriculaResource {

    private final Logger log = LoggerFactory.getLogger(MatriculaResource.class);

    private static final String ENTITY_NAME = "matricula";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MatriculaService matriculaService;

    private final MatriculaRepository matriculaRepository;

    private final MatriculaQueryService matriculaQueryService;

    public MatriculaResource(
        MatriculaService matriculaService,
        MatriculaRepository matriculaRepository,
        MatriculaQueryService matriculaQueryService
    ) {
        this.matriculaService = matriculaService;
        this.matriculaRepository = matriculaRepository;
        this.matriculaQueryService = matriculaQueryService;
    }

    /**
     * {@code POST  /matriculas} : Create a new matricula.
     *
     * @param matricula the matricula to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new matricula, or with status {@code 400 (Bad Request)} if the matricula has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/matriculas")
    public ResponseEntity<Matricula> createMatricula(@Valid @RequestBody Matricula matricula) throws URISyntaxException {
        log.debug("REST request to save Matricula : {}", matricula);
        if (matricula.getId() != null) {
            throw new BadRequestAlertException("A new matricula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Matricula result = matriculaService.save(matricula);
        return ResponseEntity
            .created(new URI("/api/matriculas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /matriculas/:id} : Updates an existing matricula.
     *
     * @param id the id of the matricula to save.
     * @param matricula the matricula to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matricula,
     * or with status {@code 400 (Bad Request)} if the matricula is not valid,
     * or with status {@code 500 (Internal Server Error)} if the matricula couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/matriculas/{id}")
    public ResponseEntity<Matricula> updateMatricula(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Matricula matricula
    ) throws URISyntaxException {
        log.debug("REST request to update Matricula : {}, {}", id, matricula);
        if (matricula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matricula.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matriculaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Matricula result = matriculaService.update(matricula);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matricula.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /matriculas/:id} : Partial updates given fields of an existing matricula, field will ignore if it is null
     *
     * @param id the id of the matricula to save.
     * @param matricula the matricula to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matricula,
     * or with status {@code 400 (Bad Request)} if the matricula is not valid,
     * or with status {@code 404 (Not Found)} if the matricula is not found,
     * or with status {@code 500 (Internal Server Error)} if the matricula couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/matriculas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Matricula> partialUpdateMatricula(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Matricula matricula
    ) throws URISyntaxException {
        log.debug("REST request to partial update Matricula partially : {}, {}", id, matricula);
        if (matricula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matricula.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matriculaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Matricula> result = matriculaService.partialUpdate(matricula);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matricula.getId().toString())
        );
    }

    /**
     * {@code GET  /matriculas} : get all the matriculas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of matriculas in body.
     */
    @GetMapping("/matriculas")
    public ResponseEntity<List<Matricula>> getAllMatriculas(
        MatriculaCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Matriculas by criteria: {}", criteria);
        Page<Matricula> page = matriculaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /matriculas/count} : count all the matriculas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/matriculas/count")
    public ResponseEntity<Long> countMatriculas(MatriculaCriteria criteria) {
        log.debug("REST request to count Matriculas by criteria: {}", criteria);
        return ResponseEntity.ok().body(matriculaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /matriculas/:id} : get the "id" matricula.
     *
     * @param id the id of the matricula to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matricula, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/matriculas/{id}")
    public ResponseEntity<Matricula> getMatricula(@PathVariable Long id) {
        log.debug("REST request to get Matricula : {}", id);
        Optional<Matricula> matricula = matriculaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(matricula);
    }

    /**
     * {@code DELETE  /matriculas/:id} : delete the "id" matricula.
     *
     * @param id the id of the matricula to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/matriculas/{id}")
    public ResponseEntity<Void> deleteMatricula(@PathVariable Long id) {
        log.debug("REST request to delete Matricula : {}", id);
        matriculaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
