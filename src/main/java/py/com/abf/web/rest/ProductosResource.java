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
import py.com.abf.domain.Productos;
import py.com.abf.repository.ProductosRepository;
import py.com.abf.service.ProductosQueryService;
import py.com.abf.service.ProductosService;
import py.com.abf.service.criteria.ProductosCriteria;
import py.com.abf.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link py.com.abf.domain.Productos}.
 */
@RestController
@RequestMapping("/api")
public class ProductosResource {

    private final Logger log = LoggerFactory.getLogger(ProductosResource.class);

    private static final String ENTITY_NAME = "productos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductosService productosService;

    private final ProductosRepository productosRepository;

    private final ProductosQueryService productosQueryService;

    public ProductosResource(
        ProductosService productosService,
        ProductosRepository productosRepository,
        ProductosQueryService productosQueryService
    ) {
        this.productosService = productosService;
        this.productosRepository = productosRepository;
        this.productosQueryService = productosQueryService;
    }

    /**
     * {@code POST  /productos} : Create a new productos.
     *
     * @param productos the productos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productos, or with status {@code 400 (Bad Request)} if the productos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/productos")
    public ResponseEntity<Productos> createProductos(@Valid @RequestBody Productos productos) throws URISyntaxException {
        log.debug("REST request to save Productos : {}", productos);
        if (productos.getId() != null) {
            throw new BadRequestAlertException("A new productos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Productos result = productosService.save(productos);
        return ResponseEntity
            .created(new URI("/api/productos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /productos/:id} : Updates an existing productos.
     *
     * @param id the id of the productos to save.
     * @param productos the productos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productos,
     * or with status {@code 400 (Bad Request)} if the productos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/productos/{id}")
    public ResponseEntity<Productos> updateProductos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Productos productos
    ) throws URISyntaxException {
        log.debug("REST request to update Productos : {}, {}", id, productos);
        if (productos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Productos result = productosService.update(productos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productos.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /productos/:id} : Partial updates given fields of an existing productos, field will ignore if it is null
     *
     * @param id the id of the productos to save.
     * @param productos the productos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productos,
     * or with status {@code 400 (Bad Request)} if the productos is not valid,
     * or with status {@code 404 (Not Found)} if the productos is not found,
     * or with status {@code 500 (Internal Server Error)} if the productos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/productos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Productos> partialUpdateProductos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Productos productos
    ) throws URISyntaxException {
        log.debug("REST request to partial update Productos partially : {}, {}", id, productos);
        if (productos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Productos> result = productosService.partialUpdate(productos);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productos.getId().toString())
        );
    }

    /**
     * {@code GET  /productos} : get all the productos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productos in body.
     */
    @GetMapping("/productos")
    public ResponseEntity<List<Productos>> getAllProductos(
        ProductosCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Productos by criteria: {}", criteria);
        Page<Productos> page = productosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /productos/count} : count all the productos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/productos/count")
    public ResponseEntity<Long> countProductos(ProductosCriteria criteria) {
        log.debug("REST request to count Productos by criteria: {}", criteria);
        return ResponseEntity.ok().body(productosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /productos/:id} : get the "id" productos.
     *
     * @param id the id of the productos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<Productos> getProductos(@PathVariable Long id) {
        log.debug("REST request to get Productos : {}", id);
        Optional<Productos> productos = productosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productos);
    }

    /**
     * {@code DELETE  /productos/:id} : delete the "id" productos.
     *
     * @param id the id of the productos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> deleteProductos(@PathVariable Long id) {
        log.debug("REST request to delete Productos : {}", id);
        productosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
