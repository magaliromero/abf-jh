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
import py.com.abf.domain.FacturaDetalle;
import py.com.abf.repository.FacturaDetalleRepository;
import py.com.abf.service.criteria.FacturaDetalleCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FacturaDetalle} entities in the database.
 * The main input is a {@link FacturaDetalleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FacturaDetalle} or a {@link Page} of {@link FacturaDetalle} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacturaDetalleQueryService extends QueryService<FacturaDetalle> {

    private final Logger log = LoggerFactory.getLogger(FacturaDetalleQueryService.class);

    private final FacturaDetalleRepository facturaDetalleRepository;

    public FacturaDetalleQueryService(FacturaDetalleRepository facturaDetalleRepository) {
        this.facturaDetalleRepository = facturaDetalleRepository;
    }

    /**
     * Return a {@link List} of {@link FacturaDetalle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FacturaDetalle> findByCriteria(FacturaDetalleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FacturaDetalle> specification = createSpecification(criteria);
        return facturaDetalleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link FacturaDetalle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacturaDetalle> findByCriteria(FacturaDetalleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FacturaDetalle> specification = createSpecification(criteria);
        return facturaDetalleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacturaDetalleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FacturaDetalle> specification = createSpecification(criteria);
        return facturaDetalleRepository.count(specification);
    }

    /**
     * Function to convert {@link FacturaDetalleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FacturaDetalle> createSpecification(FacturaDetalleCriteria criteria) {
        Specification<FacturaDetalle> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FacturaDetalle_.id));
            }
            if (criteria.getCantidad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidad(), FacturaDetalle_.cantidad));
            }
            if (criteria.getPrecioUnitario() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrecioUnitario(), FacturaDetalle_.precioUnitario));
            }
            if (criteria.getSubtotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubtotal(), FacturaDetalle_.subtotal));
            }
            if (criteria.getPorcentajeIva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPorcentajeIva(), FacturaDetalle_.porcentajeIva));
            }
            if (criteria.getValorPorcentaje() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorPorcentaje(), FacturaDetalle_.valorPorcentaje));
            }
            if (criteria.getProductoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductoId(),
                            root -> root.join(FacturaDetalle_.producto, JoinType.LEFT).get(Productos_.id)
                        )
                    );
            }
            if (criteria.getFacturaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFacturaId(),
                            root -> root.join(FacturaDetalle_.factura, JoinType.LEFT).get(Facturas_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
