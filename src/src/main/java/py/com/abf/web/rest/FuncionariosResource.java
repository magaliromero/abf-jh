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
import py.com.abf.domain.Funcionarios;
import py.com.abf.repository.FuncionariosRepository;
import py.com.abf.service.FuncionariosQueryService;
import py.com.abf.service.FuncionariosService;
import py.com.abf.service.criteria.FuncionariosCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Funcionarios}.
 */
@RestController
@RequestMapping("/api")
public class FuncionariosResource {

    private final Logger log = LoggerFactory.getLogger(FuncionariosResource.class);

    private static final String ENTITY_NAME = "funcionarios";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FuncionariosService funcionariosService;

    private final FuncionariosRepository funcionariosRepository;

    private final FuncionariosQueryService funcionariosQueryService;

    public FuncionariosResource(
        FuncionariosService funcionariosService,
        FuncionariosRepository funcionariosRepository,
        FuncionariosQueryService funcionariosQueryService
    ) {
        this.funcionariosService = funcionariosService;
        this.funcionariosRepository = funcionariosRepository;
        this.funcionariosQueryService = funcionariosQueryService;
    }

    /**
     * {@code POST  /funcionarios} : Create a new funcionarios.
     *
     * @param funcionarios the funcionarios to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new funcionarios, or with status {@code 400 (Bad Request)} if the funcionarios has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/funcionarios")
    public ResponseEntity<Funcionarios> createFuncionarios(@Valid @RequestBody Funcionarios funcionarios) throws URISyntaxException {
        log.debug("REST request to save Funcionarios : {}", funcionarios);
        if (funcionarios.getId() != null) {
            throw new BadRequestAlertException("A new funcionarios cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Funcionarios result = funcionariosService.save(funcionarios);
        return ResponseEntity
            .created(new URI("/api/funcionarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /funcionarios/:id} : Updates an existing funcionarios.
     *
     * @param id the id of the funcionarios to save.
     * @param funcionarios the funcionarios to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcionarios,
     * or with status {@code 400 (Bad Request)} if the funcionarios is not valid,
     * or with status {@code 500 (Internal Server Error)} if the funcionarios couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/funcionarios/{id}")
    public ResponseEntity<Funcionarios> updateFuncionarios(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Funcionarios funcionarios
    ) throws URISyntaxException {
        log.debug("REST request to update Funcionarios : {}, {}", id, funcionarios);
        if (funcionarios.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcionarios.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funcionariosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Funcionarios result = funcionariosService.update(funcionarios);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, funcionarios.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /funcionarios/:id} : Partial updates given fields of an existing funcionarios, field will ignore if it is null
     *
     * @param id the id of the funcionarios to save.
     * @param funcionarios the funcionarios to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcionarios,
     * or with status {@code 400 (Bad Request)} if the funcionarios is not valid,
     * or with status {@code 404 (Not Found)} if the funcionarios is not found,
     * or with status {@code 500 (Internal Server Error)} if the funcionarios couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/funcionarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Funcionarios> partialUpdateFuncionarios(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Funcionarios funcionarios
    ) throws URISyntaxException {
        log.debug("REST request to partial update Funcionarios partially : {}, {}", id, funcionarios);
        if (funcionarios.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcionarios.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funcionariosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Funcionarios> result = funcionariosService.partialUpdate(funcionarios);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, funcionarios.getId().toString())
        );
    }

    /**
     * {@code GET  /funcionarios} : get all the funcionarios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of funcionarios in body.
     */
    @GetMapping("/funcionarios")
    public ResponseEntity<List<Funcionarios>> getAllFuncionarios(
        FuncionariosCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Funcionarios by criteria: {}", criteria);
        Page<Funcionarios> page = funcionariosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /funcionarios/count} : count all the funcionarios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/funcionarios/count")
    public ResponseEntity<Long> countFuncionarios(FuncionariosCriteria criteria) {
        log.debug("REST request to count Funcionarios by criteria: {}", criteria);
        return ResponseEntity.ok().body(funcionariosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /funcionarios/:id} : get the "id" funcionarios.
     *
     * @param id the id of the funcionarios to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the funcionarios, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/funcionarios/{id}")
    public ResponseEntity<Funcionarios> getFuncionarios(@PathVariable Long id) {
        log.debug("REST request to get Funcionarios : {}", id);
        Optional<Funcionarios> funcionarios = funcionariosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(funcionarios);
    }

    /**
     * {@code DELETE  /funcionarios/:id} : delete the "id" funcionarios.
     *
     * @param id the id of the funcionarios to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/funcionarios/{id}")
    public ResponseEntity<Void> deleteFuncionarios(@PathVariable Long id) {
        log.debug("REST request to delete Funcionarios : {}", id);
        funcionariosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
