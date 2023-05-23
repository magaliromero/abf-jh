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
import py.com.abf.domain.Matricula;
import py.com.abf.repository.MatriculaRepository;
import py.com.abf.service.criteria.MatriculaCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Matricula} entities in the database.
 * The main input is a {@link MatriculaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Matricula} or a {@link Page} of {@link Matricula} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MatriculaQueryService extends QueryService<Matricula> {

    private final Logger log = LoggerFactory.getLogger(MatriculaQueryService.class);

    private final MatriculaRepository matriculaRepository;

    public MatriculaQueryService(MatriculaRepository matriculaRepository) {
        this.matriculaRepository = matriculaRepository;
    }

    /**
     * Return a {@link List} of {@link Matricula} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Matricula> findByCriteria(MatriculaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Matricula> specification = createSpecification(criteria);
        return matriculaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Matricula} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Matricula> findByCriteria(MatriculaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Matricula> specification = createSpecification(criteria);
        return matriculaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MatriculaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Matricula> specification = createSpecification(criteria);
        return matriculaRepository.count(specification);
    }

    /**
     * Function to convert {@link MatriculaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Matricula> createSpecification(MatriculaCriteria criteria) {
        Specification<Matricula> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Matricula_.id));
            }
            if (criteria.getConcepto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConcepto(), Matricula_.concepto));
            }
            if (criteria.getMonto() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonto(), Matricula_.monto));
            }
            if (criteria.getFechaInscripcion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaInscripcion(), Matricula_.fechaInscripcion));
            }
            if (criteria.getFechaInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaInicio(), Matricula_.fechaInicio));
            }
            if (criteria.getFechaPago() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaPago(), Matricula_.fechaPago));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Matricula_.estado));
            }
            if (criteria.getAlumnoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAlumnoId(), root -> root.join(Matricula_.alumno, JoinType.LEFT).get(Alumnos_.id))
                    );
            }
        }
        return specification;
    }
}
