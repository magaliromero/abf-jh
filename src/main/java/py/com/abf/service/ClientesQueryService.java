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
import py.com.abf.domain.Clientes;
import py.com.abf.repository.ClientesRepository;
import py.com.abf.service.criteria.ClientesCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Clientes} entities in the database.
 * The main input is a {@link ClientesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Clientes} or a {@link Page} of {@link Clientes} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClientesQueryService extends QueryService<Clientes> {

    private final Logger log = LoggerFactory.getLogger(ClientesQueryService.class);

    private final ClientesRepository clientesRepository;

    public ClientesQueryService(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    /**
     * Return a {@link List} of {@link Clientes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Clientes> findByCriteria(ClientesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Clientes> specification = createSpecification(criteria);
        return clientesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Clientes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Clientes> findByCriteria(ClientesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Clientes> specification = createSpecification(criteria);
        return clientesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClientesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Clientes> specification = createSpecification(criteria);
        return clientesRepository.count(specification);
    }

    /**
     * Function to convert {@link ClientesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Clientes> createSpecification(ClientesCriteria criteria) {
        Specification<Clientes> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Clientes_.id));
            }
            if (criteria.getRuc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRuc(), Clientes_.ruc));
            }
            if (criteria.getNombres() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombres(), Clientes_.nombres));
            }
            if (criteria.getApellidos() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApellidos(), Clientes_.apellidos));
            }
            if (criteria.getRazonSocial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRazonSocial(), Clientes_.razonSocial));
            }
            if (criteria.getDocumento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumento(), Clientes_.documento));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Clientes_.email));
            }
            if (criteria.getTelefono() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefono(), Clientes_.telefono));
            }
            if (criteria.getFechaNacimiento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaNacimiento(), Clientes_.fechaNacimiento));
            }
            if (criteria.getDireccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccion(), Clientes_.direccion));
            }
        }
        return specification;
    }
}
