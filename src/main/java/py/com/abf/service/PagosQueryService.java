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
import py.com.abf.domain.Pagos;
import py.com.abf.repository.PagosRepository;
import py.com.abf.service.criteria.PagosCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Pagos} entities in the database.
 * The main input is a {@link PagosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Pagos} or a {@link Page} of {@link Pagos} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PagosQueryService extends QueryService<Pagos> {

    private final Logger log = LoggerFactory.getLogger(PagosQueryService.class);

    private final PagosRepository pagosRepository;

    public PagosQueryService(PagosRepository pagosRepository) {
        this.pagosRepository = pagosRepository;
    }

    /**
     * Return a {@link List} of {@link Pagos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Pagos> findByCriteria(PagosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pagos> specification = createSpecification(criteria);
        return pagosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Pagos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Pagos> findByCriteria(PagosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pagos> specification = createSpecification(criteria);
        return pagosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PagosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pagos> specification = createSpecification(criteria);
        return pagosRepository.count(specification);
    }

    /**
     * Function to convert {@link PagosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pagos> createSpecification(PagosCriteria criteria) {
        Specification<Pagos> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pagos_.id));
            }
            if (criteria.getMontoPago() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontoPago(), Pagos_.montoPago));
            }
            if (criteria.getMontoInicial() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontoInicial(), Pagos_.montoInicial));
            }
            if (criteria.getSaldo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSaldo(), Pagos_.saldo));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Pagos_.fechaRegistro));
            }
            if (criteria.getFechaPago() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaPago(), Pagos_.fechaPago));
            }
            if (criteria.getTipoPago() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipoPago(), Pagos_.tipoPago));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Pagos_.descripcion));
            }
            if (criteria.getIdUsuarioRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIdUsuarioRegistro(), Pagos_.idUsuarioRegistro));
            }
            if (criteria.getAlumnosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAlumnosId(), root -> root.join(Pagos_.alumnos, JoinType.LEFT).get(Alumnos_.id))
                    );
            }
            if (criteria.getFuncionariosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFuncionariosId(),
                            root -> root.join(Pagos_.funcionarios, JoinType.LEFT).get(Funcionarios_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
