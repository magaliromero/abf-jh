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
import py.com.abf.domain.MallaCurricular;
import py.com.abf.repository.MallaCurricularRepository;
import py.com.abf.service.criteria.MallaCurricularCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MallaCurricular} entities in the database.
 * The main input is a {@link MallaCurricularCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MallaCurricular} or a {@link Page} of {@link MallaCurricular} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MallaCurricularQueryService extends QueryService<MallaCurricular> {

    private final Logger log = LoggerFactory.getLogger(MallaCurricularQueryService.class);

    private final MallaCurricularRepository mallaCurricularRepository;

    public MallaCurricularQueryService(MallaCurricularRepository mallaCurricularRepository) {
        this.mallaCurricularRepository = mallaCurricularRepository;
    }

    /**
     * Return a {@link List} of {@link MallaCurricular} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MallaCurricular> findByCriteria(MallaCurricularCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MallaCurricular> specification = createSpecification(criteria);
        return mallaCurricularRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MallaCurricular} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MallaCurricular> findByCriteria(MallaCurricularCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MallaCurricular> specification = createSpecification(criteria);
        return mallaCurricularRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MallaCurricularCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MallaCurricular> specification = createSpecification(criteria);
        return mallaCurricularRepository.count(specification);
    }

    /**
     * Function to convert {@link MallaCurricularCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MallaCurricular> createSpecification(MallaCurricularCriteria criteria) {
        Specification<MallaCurricular> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MallaCurricular_.id));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), MallaCurricular_.titulo));
            }
            if (criteria.getNivel() != null) {
                specification = specification.and(buildSpecification(criteria.getNivel(), MallaCurricular_.nivel));
            }
            if (criteria.getTemasId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTemasId(), root -> root.join(MallaCurricular_.temas, JoinType.LEFT).get(Temas_.id))
                    );
            }
        }
        return specification;
    }
}
