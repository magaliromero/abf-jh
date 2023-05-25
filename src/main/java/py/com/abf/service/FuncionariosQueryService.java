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
import py.com.abf.domain.Funcionarios;
import py.com.abf.repository.FuncionariosRepository;
import py.com.abf.service.criteria.FuncionariosCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Funcionarios} entities in the database.
 * The main input is a {@link FuncionariosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Funcionarios} or a {@link Page} of {@link Funcionarios} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FuncionariosQueryService extends QueryService<Funcionarios> {

    private final Logger log = LoggerFactory.getLogger(FuncionariosQueryService.class);

    private final FuncionariosRepository funcionariosRepository;

    public FuncionariosQueryService(FuncionariosRepository funcionariosRepository) {
        this.funcionariosRepository = funcionariosRepository;
    }

    /**
     * Return a {@link List} of {@link Funcionarios} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Funcionarios> findByCriteria(FuncionariosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Funcionarios> specification = createSpecification(criteria);
        return funcionariosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Funcionarios} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Funcionarios> findByCriteria(FuncionariosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Funcionarios> specification = createSpecification(criteria);
        return funcionariosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FuncionariosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Funcionarios> specification = createSpecification(criteria);
        return funcionariosRepository.count(specification);
    }

    /**
     * Function to convert {@link FuncionariosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Funcionarios> createSpecification(FuncionariosCriteria criteria) {
        Specification<Funcionarios> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Funcionarios_.id));
            }
            if (criteria.getElo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getElo(), Funcionarios_.elo));
            }
            if (criteria.getFideId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFideId(), Funcionarios_.fideId));
            }
            if (criteria.getNombres() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombres(), Funcionarios_.nombres));
            }
            if (criteria.getApellidos() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApellidos(), Funcionarios_.apellidos));
            }
            if (criteria.getNombreCompleto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombreCompleto(), Funcionarios_.nombreCompleto));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Funcionarios_.email));
            }
            if (criteria.getTelefono() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefono(), Funcionarios_.telefono));
            }
            if (criteria.getFechaNacimiento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaNacimiento(), Funcionarios_.fechaNacimiento));
            }
            if (criteria.getDocumento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumento(), Funcionarios_.documento));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Funcionarios_.estado));
            }
            if (criteria.getTipoFuncionario() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoFuncionario(), Funcionarios_.tipoFuncionario));
            }
            if (criteria.getEvaluacionesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEvaluacionesId(),
                            root -> root.join(Funcionarios_.evaluaciones, JoinType.LEFT).get(Evaluaciones_.id)
                        )
                    );
            }
            if (criteria.getPagosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPagosId(), root -> root.join(Funcionarios_.pagos, JoinType.LEFT).get(Pagos_.id))
                    );
            }
            if (criteria.getRegistroClasesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRegistroClasesId(),
                            root -> root.join(Funcionarios_.registroClases, JoinType.LEFT).get(RegistroClases_.id)
                        )
                    );
            }
            if (criteria.getTipoDocumentosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTipoDocumentosId(),
                            root -> root.join(Funcionarios_.tipoDocumentos, JoinType.LEFT).get(TiposDocumentos_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
