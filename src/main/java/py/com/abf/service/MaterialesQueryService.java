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
import py.com.abf.domain.Materiales;
import py.com.abf.repository.MaterialesRepository;
import py.com.abf.service.criteria.MaterialesCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Materiales} entities in the database.
 * The main input is a {@link MaterialesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Materiales} or a {@link Page} of {@link Materiales} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaterialesQueryService extends QueryService<Materiales> {

    private final Logger log = LoggerFactory.getLogger(MaterialesQueryService.class);

    private final MaterialesRepository materialesRepository;

    public MaterialesQueryService(MaterialesRepository materialesRepository) {
        this.materialesRepository = materialesRepository;
    }

    /**
     * Return a {@link List} of {@link Materiales} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Materiales> findByCriteria(MaterialesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Materiales> specification = createSpecification(criteria);
        return materialesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Materiales} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Materiales> findByCriteria(MaterialesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Materiales> specification = createSpecification(criteria);
        return materialesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaterialesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Materiales> specification = createSpecification(criteria);
        return materialesRepository.count(specification);
    }

    /**
     * Function to convert {@link MaterialesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Materiales> createSpecification(MaterialesCriteria criteria) {
        Specification<Materiales> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Materiales_.id));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Materiales_.descripcion));
            }
            if (criteria.getCantidad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidad(), Materiales_.cantidad));
            }
            if (criteria.getCantidadEnPrestamo() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCantidadEnPrestamo(), Materiales_.cantidadEnPrestamo));
            }
            if (criteria.getPrestamosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrestamosId(),
                            root -> root.join(Materiales_.prestamos, JoinType.LEFT).get(Prestamos_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
