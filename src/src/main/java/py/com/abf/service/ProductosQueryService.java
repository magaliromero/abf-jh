package py.com.abf.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.*; // for static metamodels
import py.com.abf.domain.Productos;
import py.com.abf.repository.ProductosRepository;
import py.com.abf.service.criteria.ProductosCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Productos} entities in the database.
 * The main input is a {@link ProductosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Productos} or a {@link Page} of {@link Productos} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductosQueryService extends QueryService<Productos> {

    private final Logger log = LoggerFactory.getLogger(ProductosQueryService.class);

    private final ProductosRepository productosRepository;

    public ProductosQueryService(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    /**
     * Return a {@link List} of {@link Productos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Productos> findByCriteria(ProductosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Productos> specification = createSpecification(criteria);
        return productosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Productos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Productos> findByCriteria(ProductosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Productos> specification = createSpecification(criteria);
        return productosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Productos> specification = createSpecification(criteria);
        return productosRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Productos> createSpecification(ProductosCriteria criteria) {
        Specification<Productos> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Productos_.id));
            }
            if (criteria.getTipoProducto() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoProducto(), Productos_.tipoProducto));
            }
            if (criteria.getPrecioUnitario() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrecioUnitario(), Productos_.precioUnitario));
            }
            if (criteria.getPorcentajeIva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPorcentajeIva(), Productos_.porcentajeIva));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Productos_.descripcion));
            }
            if (criteria.getPagosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPagosId(), root -> root.join(Productos_.pagos, JoinType.LEFT).get(Pagos_.id))
                    );
            }
            if (criteria.getFacturaDetalleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFacturaDetalleId(),
                            root -> root.join(Productos_.facturaDetalles, JoinType.LEFT).get(FacturaDetalle_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
