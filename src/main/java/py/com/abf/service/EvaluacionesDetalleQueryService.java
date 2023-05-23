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
import py.com.abf.domain.EvaluacionesDetalle;
import py.com.abf.repository.EvaluacionesDetalleRepository;
import py.com.abf.service.criteria.EvaluacionesDetalleCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EvaluacionesDetalle} entities in the database.
 * The main input is a {@link EvaluacionesDetalleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EvaluacionesDetalle} or a {@link Page} of {@link EvaluacionesDetalle} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EvaluacionesDetalleQueryService extends QueryService<EvaluacionesDetalle> {

    private final Logger log = LoggerFactory.getLogger(EvaluacionesDetalleQueryService.class);

    private final EvaluacionesDetalleRepository evaluacionesDetalleRepository;

    public EvaluacionesDetalleQueryService(EvaluacionesDetalleRepository evaluacionesDetalleRepository) {
        this.evaluacionesDetalleRepository = evaluacionesDetalleRepository;
    }

    /**
     * Return a {@link List} of {@link EvaluacionesDetalle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EvaluacionesDetalle> findByCriteria(EvaluacionesDetalleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EvaluacionesDetalle> specification = createSpecification(criteria);
        return evaluacionesDetalleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EvaluacionesDetalle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EvaluacionesDetalle> findByCriteria(EvaluacionesDetalleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EvaluacionesDetalle> specification = createSpecification(criteria);
        return evaluacionesDetalleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EvaluacionesDetalleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EvaluacionesDetalle> specification = createSpecification(criteria);
        return evaluacionesDetalleRepository.count(specification);
    }

    /**
     * Function to convert {@link EvaluacionesDetalleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EvaluacionesDetalle> createSpecification(EvaluacionesDetalleCriteria criteria) {
        Specification<EvaluacionesDetalle> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EvaluacionesDetalle_.id));
            }
            if (criteria.getComentarios() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComentarios(), EvaluacionesDetalle_.comentarios));
            }
            if (criteria.getPuntaje() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPuntaje(), EvaluacionesDetalle_.puntaje));
            }
            if (criteria.getEvaluacionesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEvaluacionesId(),
                            root -> root.join(EvaluacionesDetalle_.evaluaciones, JoinType.LEFT).get(Evaluaciones_.id)
                        )
                    );
            }
            if (criteria.getTemasId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemasId(),
                            root -> root.join(EvaluacionesDetalle_.temas, JoinType.LEFT).get(Temas_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
