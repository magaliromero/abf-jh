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
import py.com.abf.domain.PuntoDeExpedicion;
import py.com.abf.repository.PuntoDeExpedicionRepository;
import py.com.abf.service.criteria.PuntoDeExpedicionCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PuntoDeExpedicion} entities in the database.
 * The main input is a {@link PuntoDeExpedicionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PuntoDeExpedicion} or a {@link Page} of {@link PuntoDeExpedicion} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PuntoDeExpedicionQueryService extends QueryService<PuntoDeExpedicion> {

    private final Logger log = LoggerFactory.getLogger(PuntoDeExpedicionQueryService.class);

    private final PuntoDeExpedicionRepository puntoDeExpedicionRepository;

    public PuntoDeExpedicionQueryService(PuntoDeExpedicionRepository puntoDeExpedicionRepository) {
        this.puntoDeExpedicionRepository = puntoDeExpedicionRepository;
    }

    /**
     * Return a {@link List} of {@link PuntoDeExpedicion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PuntoDeExpedicion> findByCriteria(PuntoDeExpedicionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PuntoDeExpedicion> specification = createSpecification(criteria);
        return puntoDeExpedicionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PuntoDeExpedicion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PuntoDeExpedicion> findByCriteria(PuntoDeExpedicionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PuntoDeExpedicion> specification = createSpecification(criteria);
        return puntoDeExpedicionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PuntoDeExpedicionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PuntoDeExpedicion> specification = createSpecification(criteria);
        return puntoDeExpedicionRepository.count(specification);
    }

    /**
     * Function to convert {@link PuntoDeExpedicionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PuntoDeExpedicion> createSpecification(PuntoDeExpedicionCriteria criteria) {
        Specification<PuntoDeExpedicion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PuntoDeExpedicion_.id));
            }
            if (criteria.getNumeroPuntoDeExpedicion() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getNumeroPuntoDeExpedicion(), PuntoDeExpedicion_.numeroPuntoDeExpedicion)
                    );
            }
            if (criteria.getSucursalesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSucursalesId(),
                            root -> root.join(PuntoDeExpedicion_.sucursales, JoinType.LEFT).get(Sucursales_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
