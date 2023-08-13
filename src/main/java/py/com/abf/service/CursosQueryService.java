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
import py.com.abf.domain.Cursos;
import py.com.abf.repository.CursosRepository;
import py.com.abf.service.criteria.CursosCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cursos} entities in the database.
 * The main input is a {@link CursosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Cursos} or a {@link Page} of {@link Cursos} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CursosQueryService extends QueryService<Cursos> {

    private final Logger log = LoggerFactory.getLogger(CursosQueryService.class);

    private final CursosRepository cursosRepository;

    public CursosQueryService(CursosRepository cursosRepository) {
        this.cursosRepository = cursosRepository;
    }

    /**
     * Return a {@link List} of {@link Cursos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Cursos> findByCriteria(CursosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cursos> specification = createSpecification(criteria);
        return cursosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Cursos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Cursos> findByCriteria(CursosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cursos> specification = createSpecification(criteria);
        return cursosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CursosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cursos> specification = createSpecification(criteria);
        return cursosRepository.count(specification);
    }

    /**
     * Function to convert {@link CursosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cursos> createSpecification(CursosCriteria criteria) {
        Specification<Cursos> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cursos_.id));
            }
            if (criteria.getNombreCurso() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombreCurso(), Cursos_.nombreCurso));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Cursos_.descripcion));
            }
            if (criteria.getFechaInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaInicio(), Cursos_.fechaInicio));
            }
            if (criteria.getFechaFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaFin(), Cursos_.fechaFin));
            }
            if (criteria.getCantidadClases() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidadClases(), Cursos_.cantidadClases));
            }
            if (criteria.getNivel() != null) {
                specification = specification.and(buildSpecification(criteria.getNivel(), Cursos_.nivel));
            }
            if (criteria.getInscripcionesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInscripcionesId(),
                            root -> root.join(Cursos_.inscripciones, JoinType.LEFT).get(Inscripciones_.id)
                        )
                    );
            }
            if (criteria.getTemasId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTemasId(), root -> root.join(Cursos_.temas, JoinType.LEFT).get(Temas_.id))
                    );
            }
        }
        return specification;
    }
}
