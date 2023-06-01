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
import py.com.abf.domain.Alumnos;
import py.com.abf.repository.AlumnosRepository;
import py.com.abf.service.criteria.AlumnosCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Alumnos} entities in the database.
 * The main input is a {@link AlumnosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Alumnos} or a {@link Page} of {@link Alumnos} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlumnosQueryService extends QueryService<Alumnos> {

    private final Logger log = LoggerFactory.getLogger(AlumnosQueryService.class);

    private final AlumnosRepository alumnosRepository;

    public AlumnosQueryService(AlumnosRepository alumnosRepository) {
        this.alumnosRepository = alumnosRepository;
    }

    /**
     * Return a {@link List} of {@link Alumnos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Alumnos> findByCriteria(AlumnosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Alumnos> specification = createSpecification(criteria);
        return alumnosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Alumnos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Alumnos> findByCriteria(AlumnosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Alumnos> specification = createSpecification(criteria);
        return alumnosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlumnosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Alumnos> specification = createSpecification(criteria);
        return alumnosRepository.count(specification);
    }

    /**
     * Function to convert {@link AlumnosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Alumnos> createSpecification(AlumnosCriteria criteria) {
        Specification<Alumnos> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Alumnos_.id));
            }
            if (criteria.getElo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getElo(), Alumnos_.elo));
            }
            if (criteria.getFideId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFideId(), Alumnos_.fideId));
            }
            if (criteria.getNombres() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombres(), Alumnos_.nombres));
            }
            if (criteria.getApellidos() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApellidos(), Alumnos_.apellidos));
            }
            if (criteria.getNombreCompleto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombreCompleto(), Alumnos_.nombreCompleto));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Alumnos_.email));
            }
            if (criteria.getTelefono() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefono(), Alumnos_.telefono));
            }
            if (criteria.getFechaNacimiento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaNacimiento(), Alumnos_.fechaNacimiento));
            }
            if (criteria.getDocumento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumento(), Alumnos_.documento));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Alumnos_.estado));
            }
            if (criteria.getInscripcionesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInscripcionesId(),
                            root -> root.join(Alumnos_.inscripciones, JoinType.LEFT).get(Inscripciones_.id)
                        )
                    );
            }
            if (criteria.getEvaluacionesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEvaluacionesId(),
                            root -> root.join(Alumnos_.evaluaciones, JoinType.LEFT).get(Evaluaciones_.id)
                        )
                    );
            }
            if (criteria.getMatriculaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMatriculaId(),
                            root -> root.join(Alumnos_.matriculas, JoinType.LEFT).get(Matricula_.id)
                        )
                    );
            }
            if (criteria.getPrestamosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrestamosId(),
                            root -> root.join(Alumnos_.prestamos, JoinType.LEFT).get(Prestamos_.id)
                        )
                    );
            }
            if (criteria.getRegistroClasesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRegistroClasesId(),
                            root -> root.join(Alumnos_.registroClases, JoinType.LEFT).get(RegistroClases_.id)
                        )
                    );
            }
            if (criteria.getTipoDocumentosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTipoDocumentosId(),
                            root -> root.join(Alumnos_.tipoDocumentos, JoinType.LEFT).get(TiposDocumentos_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
