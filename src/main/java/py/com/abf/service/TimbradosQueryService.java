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
import py.com.abf.domain.Timbrados;
import py.com.abf.repository.TimbradosRepository;
import py.com.abf.service.criteria.TimbradosCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Timbrados} entities in the database.
 * The main input is a {@link TimbradosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Timbrados} or a {@link Page} of {@link Timbrados} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TimbradosQueryService extends QueryService<Timbrados> {

    private final Logger log = LoggerFactory.getLogger(TimbradosQueryService.class);

    private final TimbradosRepository timbradosRepository;

    public TimbradosQueryService(TimbradosRepository timbradosRepository) {
        this.timbradosRepository = timbradosRepository;
    }

    /**
     * Return a {@link List} of {@link Timbrados} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Timbrados> findByCriteria(TimbradosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Timbrados> specification = createSpecification(criteria);
        return timbradosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Timbrados} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Timbrados> findByCriteria(TimbradosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Timbrados> specification = createSpecification(criteria);
        return timbradosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TimbradosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Timbrados> specification = createSpecification(criteria);
        return timbradosRepository.count(specification);
    }

    /**
     * Function to convert {@link TimbradosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Timbrados> createSpecification(TimbradosCriteria criteria) {
        Specification<Timbrados> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Timbrados_.id));
            }
            if (criteria.getNumeroTimbrado() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroTimbrado(), Timbrados_.numeroTimbrado));
            }
            if (criteria.getFechaInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaInicio(), Timbrados_.fechaInicio));
            }
            if (criteria.getFechaFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaFin(), Timbrados_.fechaFin));
            }
            if (criteria.getSucursalesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSucursalesId(),
                            root -> root.join(Timbrados_.sucursales, JoinType.LEFT).get(Sucursales_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
