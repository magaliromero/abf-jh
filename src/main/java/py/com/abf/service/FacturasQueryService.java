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
import py.com.abf.domain.Facturas;
import py.com.abf.repository.FacturasRepository;
import py.com.abf.service.criteria.FacturasCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Facturas} entities in the database.
 * The main input is a {@link FacturasCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Facturas} or a {@link Page} of {@link Facturas} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacturasQueryService extends QueryService<Facturas> {

    private final Logger log = LoggerFactory.getLogger(FacturasQueryService.class);

    private final FacturasRepository facturasRepository;

    public FacturasQueryService(FacturasRepository facturasRepository) {
        this.facturasRepository = facturasRepository;
    }

    /**
     * Return a {@link List} of {@link Facturas} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Facturas> findByCriteria(FacturasCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Facturas> specification = createSpecification(criteria);
        return facturasRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Facturas} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Facturas> findByCriteria(FacturasCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Facturas> specification = createSpecification(criteria);
        return facturasRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacturasCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Facturas> specification = createSpecification(criteria);
        return facturasRepository.count(specification);
    }

    /**
     * Function to convert {@link FacturasCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Facturas> createSpecification(FacturasCriteria criteria) {
        Specification<Facturas> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Facturas_.id));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Facturas_.fecha));
            }
            if (criteria.getFacturaNro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFacturaNro(), Facturas_.facturaNro));
            }
            if (criteria.getTimbrado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimbrado(), Facturas_.timbrado));
            }
            if (criteria.getRuc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRuc(), Facturas_.ruc));
            }
            if (criteria.getCondicionVenta() != null) {
                specification = specification.and(buildSpecification(criteria.getCondicionVenta(), Facturas_.condicionVenta));
            }
            if (criteria.getCantidad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidad(), Facturas_.cantidad));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Facturas_.descripcion));
            }
            if (criteria.getPrecioUnitario() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrecioUnitario(), Facturas_.precioUnitario));
            }
            if (criteria.getValor5() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor5(), Facturas_.valor5));
            }
            if (criteria.getValor10() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor10(), Facturas_.valor10));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), Facturas_.total));
            }
            if (criteria.getTotal5() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal5(), Facturas_.total5));
            }
            if (criteria.getTotal10() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal10(), Facturas_.total10));
            }
            if (criteria.getTotalIva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalIva(), Facturas_.totalIva));
            }
            if (criteria.getCobrosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCobrosId(), root -> root.join(Facturas_.cobros, JoinType.LEFT).get(Cobros_.id))
                    );
            }
            if (criteria.getAlumnosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAlumnosId(), root -> root.join(Facturas_.alumnos, JoinType.LEFT).get(Alumnos_.id))
                    );
            }
        }
        return specification;
    }
}
