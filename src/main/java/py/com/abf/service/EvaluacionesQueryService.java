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
import py.com.abf.domain.Evaluaciones;
import py.com.abf.repository.EvaluacionesRepository;
import py.com.abf.service.criteria.EvaluacionesCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Evaluaciones} entities in the database.
 * The main input is a {@link EvaluacionesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Evaluaciones} or a {@link Page} of {@link Evaluaciones} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EvaluacionesQueryService extends QueryService<Evaluaciones> {

    private final Logger log = LoggerFactory.getLogger(EvaluacionesQueryService.class);

    private final EvaluacionesRepository evaluacionesRepository;

    public EvaluacionesQueryService(EvaluacionesRepository evaluacionesRepository) {
        this.evaluacionesRepository = evaluacionesRepository;
    }

    /**
     * Return a {@link List} of {@link Evaluaciones} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Evaluaciones> findByCriteria(EvaluacionesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Evaluaciones> specification = createSpecification(criteria);
        return evaluacionesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Evaluaciones} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Evaluaciones> findByCriteria(EvaluacionesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Evaluaciones> specification = createSpecification(criteria);
        return evaluacionesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EvaluacionesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Evaluaciones> specification = createSpecification(criteria);
        return evaluacionesRepository.count(specification);
    }

    /**
     * Function to convert {@link EvaluacionesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Evaluaciones> createSpecification(EvaluacionesCriteria criteria) {
        Specification<Evaluaciones> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Evaluaciones_.id));
            }
            if (criteria.getNroEvaluacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNroEvaluacion(), Evaluaciones_.nroEvaluacion));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Evaluaciones_.fecha));
            }
            if (criteria.getEvaluacionesDetalleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEvaluacionesDetalleId(),
                            root -> root.join(Evaluaciones_.evaluacionesDetalles, JoinType.LEFT).get(EvaluacionesDetalle_.id)
                        )
                    );
            }
            if (criteria.getAlumnosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAlumnosId(),
                            root -> root.join(Evaluaciones_.alumnos, JoinType.LEFT).get(Alumnos_.id)
                        )
                    );
            }
            if (criteria.getFuncionariosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFuncionariosId(),
                            root -> root.join(Evaluaciones_.funcionarios, JoinType.LEFT).get(Funcionarios_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
