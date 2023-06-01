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
import py.com.abf.domain.RegistroClases;
import py.com.abf.repository.RegistroClasesRepository;
import py.com.abf.service.criteria.RegistroClasesCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link RegistroClases} entities in the database.
 * The main input is a {@link RegistroClasesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RegistroClases} or a {@link Page} of {@link RegistroClases} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RegistroClasesQueryService extends QueryService<RegistroClases> {

    private final Logger log = LoggerFactory.getLogger(RegistroClasesQueryService.class);

    private final RegistroClasesRepository registroClasesRepository;

    public RegistroClasesQueryService(RegistroClasesRepository registroClasesRepository) {
        this.registroClasesRepository = registroClasesRepository;
    }

    /**
     * Return a {@link List} of {@link RegistroClases} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RegistroClases> findByCriteria(RegistroClasesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RegistroClases> specification = createSpecification(criteria);
        return registroClasesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RegistroClases} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RegistroClases> findByCriteria(RegistroClasesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RegistroClases> specification = createSpecification(criteria);
        return registroClasesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RegistroClasesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RegistroClases> specification = createSpecification(criteria);
        return registroClasesRepository.count(specification);
    }

    /**
     * Function to convert {@link RegistroClasesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RegistroClases> createSpecification(RegistroClasesCriteria criteria) {
        Specification<RegistroClases> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RegistroClases_.id));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), RegistroClases_.fecha));
            }
            if (criteria.getCantidadHoras() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidadHoras(), RegistroClases_.cantidadHoras));
            }
            if (criteria.getAsistenciaAlumno() != null) {
                specification = specification.and(buildSpecification(criteria.getAsistenciaAlumno(), RegistroClases_.asistenciaAlumno));
            }
            if (criteria.getTemasId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTemasId(), root -> root.join(RegistroClases_.temas, JoinType.LEFT).get(Temas_.id))
                    );
            }
            if (criteria.getFuncionarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFuncionarioId(),
                            root -> root.join(RegistroClases_.funcionario, JoinType.LEFT).get(Funcionarios_.id)
                        )
                    );
            }
            if (criteria.getAlumnosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAlumnosId(),
                            root -> root.join(RegistroClases_.alumnos, JoinType.LEFT).get(Alumnos_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
