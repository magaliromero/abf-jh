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
import py.com.abf.domain.Torneos;
import py.com.abf.repository.TorneosRepository;
import py.com.abf.service.criteria.TorneosCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Torneos} entities in the database.
 * The main input is a {@link TorneosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Torneos} or a {@link Page} of {@link Torneos} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TorneosQueryService extends QueryService<Torneos> {

    private final Logger log = LoggerFactory.getLogger(TorneosQueryService.class);

    private final TorneosRepository torneosRepository;

    public TorneosQueryService(TorneosRepository torneosRepository) {
        this.torneosRepository = torneosRepository;
    }

    /**
     * Return a {@link List} of {@link Torneos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Torneos> findByCriteria(TorneosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Torneos> specification = createSpecification(criteria);
        return torneosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Torneos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Torneos> findByCriteria(TorneosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Torneos> specification = createSpecification(criteria);
        return torneosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TorneosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Torneos> specification = createSpecification(criteria);
        return torneosRepository.count(specification);
    }

    /**
     * Function to convert {@link TorneosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Torneos> createSpecification(TorneosCriteria criteria) {
        Specification<Torneos> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Torneos_.id));
            }
            if (criteria.getNombreTorneo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombreTorneo(), Torneos_.nombreTorneo));
            }
            if (criteria.getFechaInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaInicio(), Torneos_.fechaInicio));
            }
            if (criteria.getFechaFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaFin(), Torneos_.fechaFin));
            }
            if (criteria.getLugar() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLugar(), Torneos_.lugar));
            }
            if (criteria.getTiempo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTiempo(), Torneos_.tiempo));
            }
            if (criteria.getTipoTorneo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoTorneo(), Torneos_.tipoTorneo));
            }
            if (criteria.getTorneoEvaluado() != null) {
                specification = specification.and(buildSpecification(criteria.getTorneoEvaluado(), Torneos_.torneoEvaluado));
            }
            if (criteria.getFederado() != null) {
                specification = specification.and(buildSpecification(criteria.getFederado(), Torneos_.federado));
            }
            if (criteria.getFichaPartidasTorneosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFichaPartidasTorneosId(),
                            root -> root.join(Torneos_.fichaPartidasTorneos, JoinType.LEFT).get(FichaPartidasTorneos_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
