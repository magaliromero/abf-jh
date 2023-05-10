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
import py.com.abf.domain.FichaPartidasTorneos;
import py.com.abf.repository.FichaPartidasTorneosRepository;
import py.com.abf.service.criteria.FichaPartidasTorneosCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FichaPartidasTorneos} entities in the database.
 * The main input is a {@link FichaPartidasTorneosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FichaPartidasTorneos} or a {@link Page} of {@link FichaPartidasTorneos} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FichaPartidasTorneosQueryService extends QueryService<FichaPartidasTorneos> {

    private final Logger log = LoggerFactory.getLogger(FichaPartidasTorneosQueryService.class);

    private final FichaPartidasTorneosRepository fichaPartidasTorneosRepository;

    public FichaPartidasTorneosQueryService(FichaPartidasTorneosRepository fichaPartidasTorneosRepository) {
        this.fichaPartidasTorneosRepository = fichaPartidasTorneosRepository;
    }

    /**
     * Return a {@link List} of {@link FichaPartidasTorneos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FichaPartidasTorneos> findByCriteria(FichaPartidasTorneosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FichaPartidasTorneos> specification = createSpecification(criteria);
        return fichaPartidasTorneosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link FichaPartidasTorneos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FichaPartidasTorneos> findByCriteria(FichaPartidasTorneosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FichaPartidasTorneos> specification = createSpecification(criteria);
        return fichaPartidasTorneosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FichaPartidasTorneosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FichaPartidasTorneos> specification = createSpecification(criteria);
        return fichaPartidasTorneosRepository.count(specification);
    }

    /**
     * Function to convert {@link FichaPartidasTorneosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FichaPartidasTorneos> createSpecification(FichaPartidasTorneosCriteria criteria) {
        Specification<FichaPartidasTorneos> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FichaPartidasTorneos_.id));
            }
            if (criteria.getNombreContrincante() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getNombreContrincante(), FichaPartidasTorneos_.nombreContrincante));
            }
            if (criteria.getDuracion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuracion(), FichaPartidasTorneos_.duracion));
            }
            if (criteria.getWinner() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWinner(), FichaPartidasTorneos_.winner));
            }
            if (criteria.getResultado() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResultado(), FichaPartidasTorneos_.resultado));
            }
            if (criteria.getComentarios() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComentarios(), FichaPartidasTorneos_.comentarios));
            }
            if (criteria.getNombreArbitro() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getNombreArbitro(), FichaPartidasTorneos_.nombreArbitro));
            }
            if (criteria.getTorneosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTorneosId(),
                            root -> root.join(FichaPartidasTorneos_.torneos, JoinType.LEFT).get(Torneos_.id)
                        )
                    );
            }
            if (criteria.getAlumnosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAlumnosId(),
                            root -> root.join(FichaPartidasTorneos_.alumnos, JoinType.LEFT).get(Alumnos_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
