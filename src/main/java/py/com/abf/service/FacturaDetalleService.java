package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.FacturaDetalle;

/**
 * Service Interface for managing {@link FacturaDetalle}.
 */
public interface FacturaDetalleService {
    /**
     * Save a facturaDetalle.
     *
     * @param facturaDetalle the entity to save.
     * @return the persisted entity.
     */
    FacturaDetalle save(FacturaDetalle facturaDetalle);

    /**
     * Updates a facturaDetalle.
     *
     * @param facturaDetalle the entity to update.
     * @return the persisted entity.
     */
    FacturaDetalle update(FacturaDetalle facturaDetalle);

    /**
     * Partially updates a facturaDetalle.
     *
     * @param facturaDetalle the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FacturaDetalle> partialUpdate(FacturaDetalle facturaDetalle);

    /**
     * Get all the facturaDetalles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FacturaDetalle> findAll(Pageable pageable);

    /**
     * Get all the facturaDetalles with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FacturaDetalle> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" facturaDetalle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FacturaDetalle> findOne(Long id);

    /**
     * Delete the "id" facturaDetalle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
