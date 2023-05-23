package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.Pagos;

/**
 * Service Interface for managing {@link Pagos}.
 */
public interface PagosService {
    /**
     * Save a pagos.
     *
     * @param pagos the entity to save.
     * @return the persisted entity.
     */
    Pagos save(Pagos pagos);

    /**
     * Updates a pagos.
     *
     * @param pagos the entity to update.
     * @return the persisted entity.
     */
    Pagos update(Pagos pagos);

    /**
     * Partially updates a pagos.
     *
     * @param pagos the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pagos> partialUpdate(Pagos pagos);

    /**
     * Get all the pagos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pagos> findAll(Pageable pageable);

    /**
     * Get all the pagos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pagos> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" pagos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pagos> findOne(Long id);

    /**
     * Delete the "id" pagos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
