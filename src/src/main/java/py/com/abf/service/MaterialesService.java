package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.Materiales;

/**
 * Service Interface for managing {@link Materiales}.
 */
public interface MaterialesService {
    /**
     * Save a materiales.
     *
     * @param materiales the entity to save.
     * @return the persisted entity.
     */
    Materiales save(Materiales materiales);

    /**
     * Updates a materiales.
     *
     * @param materiales the entity to update.
     * @return the persisted entity.
     */
    Materiales update(Materiales materiales);

    /**
     * Partially updates a materiales.
     *
     * @param materiales the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Materiales> partialUpdate(Materiales materiales);

    /**
     * Get all the materiales.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Materiales> findAll(Pageable pageable);

    /**
     * Get the "id" materiales.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Materiales> findOne(Long id);

    /**
     * Delete the "id" materiales.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
