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
import py.com.abf.domain.Inscripciones;
import py.com.abf.repository.InscripcionesRepository;
import py.com.abf.service.criteria.InscripcionesCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Inscripciones} entities in the database.
 * The main input is a {@link InscripcionesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Inscripciones} or a {@link Page} of {@link Inscripciones} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InscripcionesQueryService extends QueryService<Inscripciones> {

    private final Logger log = LoggerFactory.getLogger(InscripcionesQueryService.class);

    private final InscripcionesRepository inscripcionesRepository;

    public InscripcionesQueryService(InscripcionesRepository inscripcionesRepository) {
        this.inscripcionesRepository = inscripcionesRepository;
    }

    /**
     * Return a {@link List} of {@link Inscripciones} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Inscripciones> findByCriteria(InscripcionesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Inscripciones> specification = createSpecification(criteria);
        return inscripcionesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Inscripciones} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Inscripciones> findByCriteria(InscripcionesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Inscripciones> specification = createSpecification(criteria);
        return inscripcionesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InscripcionesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Inscripciones> specification = createSpecification(criteria);
        return inscripcionesRepository.count(specification);
    }

    /**
     * Function to convert {@link InscripcionesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Inscripciones> createSpecification(InscripcionesCriteria criteria) {
        Specification<Inscripciones> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Inscripciones_.id));
            }
            if (criteria.getFechaInscripcion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaInscripcion(), Inscripciones_.fechaInscripcion));
            }
            if (criteria.getAlumnosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAlumnosId(),
                            root -> root.join(Inscripciones_.alumnos, JoinType.LEFT).get(Alumnos_.id)
                        )
                    );
            }
            if (criteria.getCursosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCursosId(), root -> root.join(Inscripciones_.cursos, JoinType.LEFT).get(Cursos_.id))
                    );
            }
        }
        return specification;
    }
}
