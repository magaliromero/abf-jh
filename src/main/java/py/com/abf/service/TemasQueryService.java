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
import py.com.abf.domain.Temas;
import py.com.abf.repository.TemasRepository;
import py.com.abf.service.criteria.TemasCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Temas} entities in the database.
 * The main input is a {@link TemasCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Temas} or a {@link Page} of {@link Temas} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TemasQueryService extends QueryService<Temas> {

    private final Logger log = LoggerFactory.getLogger(TemasQueryService.class);

    private final TemasRepository temasRepository;

    public TemasQueryService(TemasRepository temasRepository) {
        this.temasRepository = temasRepository;
    }

    /**
     * Return a {@link List} of {@link Temas} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Temas> findByCriteria(TemasCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Temas> specification = createSpecification(criteria);
        return temasRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Temas} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Temas> findByCriteria(TemasCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Temas> specification = createSpecification(criteria);
        return temasRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TemasCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Temas> specification = createSpecification(criteria);
        return temasRepository.count(specification);
    }

    /**
     * Function to convert {@link TemasCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Temas> createSpecification(TemasCriteria criteria) {
        Specification<Temas> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Temas_.id));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), Temas_.titulo));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Temas_.descripcion));
            }
            if (criteria.getEvaluacionesDetalleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEvaluacionesDetalleId(),
                            root -> root.join(Temas_.evaluacionesDetalles, JoinType.LEFT).get(EvaluacionesDetalle_.id)
                        )
                    );
            }
            if (criteria.getRegistroClasesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRegistroClasesId(),
                            root -> root.join(Temas_.registroClases, JoinType.LEFT).get(RegistroClases_.id)
                        )
                    );
            }
            if (criteria.getCursosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCursosId(), root -> root.join(Temas_.cursos, JoinType.LEFT).get(Cursos_.id))
                    );
            }
        }
        return specification;
    }
}
