package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.Timbrados;

/**
 * Service Interface for managing {@link Timbrados}.
 */
public interface TimbradosService {
    /**
     * Save a timbrados.
     *
     * @param timbrados the entity to save.
     * @return the persisted entity.
     */
    Timbrados save(Timbrados timbrados);

    /**
     * Updates a timbrados.
     *
     * @param timbrados the entity to update.
     * @return the persisted entity.
     */
    Timbrados update(Timbrados timbrados);

    /**
     * Partially updates a timbrados.
     *
     * @param timbrados the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Timbrados> partialUpdate(Timbrados timbrados);

    /**
     * Get all the timbrados.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Timbrados> findAll(Pageable pageable);

    /**
     * Get the "id" timbrados.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Timbrados> findOne(Long id);

    /**
     * Delete the "id" timbrados.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
