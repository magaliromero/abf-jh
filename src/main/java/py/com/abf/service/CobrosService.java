package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.Cobros;

/**
 * Service Interface for managing {@link Cobros}.
 */
public interface CobrosService {
    /**
     * Save a cobros.
     *
     * @param cobros the entity to save.
     * @return the persisted entity.
     */
    Cobros save(Cobros cobros);

    /**
     * Updates a cobros.
     *
     * @param cobros the entity to update.
     * @return the persisted entity.
     */
    Cobros update(Cobros cobros);

    /**
     * Partially updates a cobros.
     *
     * @param cobros the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cobros> partialUpdate(Cobros cobros);

    /**
     * Get all the cobros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cobros> findAll(Pageable pageable);

    /**
     * Get all the cobros with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cobros> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cobros.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cobros> findOne(Long id);

    /**
     * Delete the "id" cobros.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
