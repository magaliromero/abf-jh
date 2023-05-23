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
import py.com.abf.domain.TiposDocumentos;
import py.com.abf.repository.TiposDocumentosRepository;
import py.com.abf.service.criteria.TiposDocumentosCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TiposDocumentos} entities in the database.
 * The main input is a {@link TiposDocumentosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TiposDocumentos} or a {@link Page} of {@link TiposDocumentos} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TiposDocumentosQueryService extends QueryService<TiposDocumentos> {

    private final Logger log = LoggerFactory.getLogger(TiposDocumentosQueryService.class);

    private final TiposDocumentosRepository tiposDocumentosRepository;

    public TiposDocumentosQueryService(TiposDocumentosRepository tiposDocumentosRepository) {
        this.tiposDocumentosRepository = tiposDocumentosRepository;
    }

    /**
     * Return a {@link List} of {@link TiposDocumentos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TiposDocumentos> findByCriteria(TiposDocumentosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TiposDocumentos> specification = createSpecification(criteria);
        return tiposDocumentosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TiposDocumentos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TiposDocumentos> findByCriteria(TiposDocumentosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TiposDocumentos> specification = createSpecification(criteria);
        return tiposDocumentosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TiposDocumentosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TiposDocumentos> specification = createSpecification(criteria);
        return tiposDocumentosRepository.count(specification);
    }

    /**
     * Function to convert {@link TiposDocumentosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TiposDocumentos> createSpecification(TiposDocumentosCriteria criteria) {
        Specification<TiposDocumentos> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TiposDocumentos_.id));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigo(), TiposDocumentos_.codigo));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), TiposDocumentos_.descripcion));
            }
            if (criteria.getAlumnosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAlumnosId(),
                            root -> root.join(TiposDocumentos_.alumnos, JoinType.LEFT).get(Alumnos_.id)
                        )
                    );
            }
            if (criteria.getFuncionariosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFuncionariosId(),
                            root -> root.join(TiposDocumentos_.funcionarios, JoinType.LEFT).get(Funcionarios_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
