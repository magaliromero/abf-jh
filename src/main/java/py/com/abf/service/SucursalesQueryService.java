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
import py.com.abf.domain.Sucursales;
import py.com.abf.repository.SucursalesRepository;
import py.com.abf.service.criteria.SucursalesCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Sucursales} entities in the database.
 * The main input is a {@link SucursalesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sucursales} or a {@link Page} of {@link Sucursales} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SucursalesQueryService extends QueryService<Sucursales> {

    private final Logger log = LoggerFactory.getLogger(SucursalesQueryService.class);

    private final SucursalesRepository sucursalesRepository;

    public SucursalesQueryService(SucursalesRepository sucursalesRepository) {
        this.sucursalesRepository = sucursalesRepository;
    }

    /**
     * Return a {@link List} of {@link Sucursales} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sucursales> findByCriteria(SucursalesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sucursales> specification = createSpecification(criteria);
        return sucursalesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sucursales} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sucursales> findByCriteria(SucursalesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sucursales> specification = createSpecification(criteria);
        return sucursalesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SucursalesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sucursales> specification = createSpecification(criteria);
        return sucursalesRepository.count(specification);
    }

    /**
     * Function to convert {@link SucursalesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sucursales> createSpecification(SucursalesCriteria criteria) {
        Specification<Sucursales> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sucursales_.id));
            }
            if (criteria.getNombreSucursal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombreSucursal(), Sucursales_.nombreSucursal));
            }
            if (criteria.getDireccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccion(), Sucursales_.direccion));
            }
            if (criteria.getNumeroEstablecimiento() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getNumeroEstablecimiento(), Sucursales_.numeroEstablecimiento));
            }
            if (criteria.getPuntoDeExpedicionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPuntoDeExpedicionId(),
                            root -> root.join(Sucursales_.puntoDeExpedicions, JoinType.LEFT).get(PuntoDeExpedicion_.id)
                        )
                    );
            }
            if (criteria.getTimbradosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTimbradosId(),
                            root -> root.join(Sucursales_.timbrados, JoinType.LEFT).get(Timbrados_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
