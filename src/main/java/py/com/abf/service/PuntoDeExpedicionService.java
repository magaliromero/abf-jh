package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.PuntoDeExpedicion;

/**
 * Service Interface for managing {@link PuntoDeExpedicion}.
 */
public interface PuntoDeExpedicionService {
    /**
     * Save a puntoDeExpedicion.
     *
     * @param puntoDeExpedicion the entity to save.
     * @return the persisted entity.
     */
    PuntoDeExpedicion save(PuntoDeExpedicion puntoDeExpedicion);

    /**
     * Updates a puntoDeExpedicion.
     *
     * @param puntoDeExpedicion the entity to update.
     * @return the persisted entity.
     */
    PuntoDeExpedicion update(PuntoDeExpedicion puntoDeExpedicion);

    /**
     * Partially updates a puntoDeExpedicion.
     *
     * @param puntoDeExpedicion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PuntoDeExpedicion> partialUpdate(PuntoDeExpedicion puntoDeExpedicion);

    /**
     * Get all the puntoDeExpedicions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PuntoDeExpedicion> findAll(Pageable pageable);

    /**
     * Get all the puntoDeExpedicions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PuntoDeExpedicion> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" puntoDeExpedicion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PuntoDeExpedicion> findOne(Long id);

    /**
     * Delete the "id" puntoDeExpedicion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
