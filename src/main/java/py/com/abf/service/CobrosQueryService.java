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
import py.com.abf.domain.Cobros;
import py.com.abf.repository.CobrosRepository;
import py.com.abf.service.criteria.CobrosCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cobros} entities in the database.
 * The main input is a {@link CobrosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Cobros} or a {@link Page} of {@link Cobros} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CobrosQueryService extends QueryService<Cobros> {

    private final Logger log = LoggerFactory.getLogger(CobrosQueryService.class);

    private final CobrosRepository cobrosRepository;

    public CobrosQueryService(CobrosRepository cobrosRepository) {
        this.cobrosRepository = cobrosRepository;
    }

    /**
     * Return a {@link List} of {@link Cobros} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Cobros> findByCriteria(CobrosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cobros> specification = createSpecification(criteria);
        return cobrosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Cobros} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Cobros> findByCriteria(CobrosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cobros> specification = createSpecification(criteria);
        return cobrosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CobrosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cobros> specification = createSpecification(criteria);
        return cobrosRepository.count(specification);
    }

    /**
     * Function to convert {@link CobrosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cobros> createSpecification(CobrosCriteria criteria) {
        Specification<Cobros> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cobros_.id));
            }
            if (criteria.getMontoPago() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontoPago(), Cobros_.montoPago));
            }
            if (criteria.getMontoInicial() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontoInicial(), Cobros_.montoInicial));
            }
            if (criteria.getSaldo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSaldo(), Cobros_.saldo));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Cobros_.fechaRegistro));
            }
            if (criteria.getFechaPago() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaPago(), Cobros_.fechaPago));
            }
            if (criteria.getTipoPago() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoPago(), Cobros_.tipoPago));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Cobros_.descripcion));
            }
            if (criteria.getAlumnosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAlumnosId(), root -> root.join(Cobros_.alumnos, JoinType.LEFT).get(Alumnos_.id))
                    );
            }
            if (criteria.getFacturaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFacturaId(), root -> root.join(Cobros_.factura, JoinType.LEFT).get(Facturas_.id))
                    );
            }
        }
        return specification;
    }
}
