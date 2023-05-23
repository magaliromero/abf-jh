package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.Productos;

/**
 * Service Interface for managing {@link Productos}.
 */
public interface ProductosService {
    /**
     * Save a productos.
     *
     * @param productos the entity to save.
     * @return the persisted entity.
     */
    Productos save(Productos productos);

    /**
     * Updates a productos.
     *
     * @param productos the entity to update.
     * @return the persisted entity.
     */
    Productos update(Productos productos);

    /**
     * Partially updates a productos.
     *
     * @param productos the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Productos> partialUpdate(Productos productos);

    /**
     * Get all the productos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Productos> findAll(Pageable pageable);

    /**
     * Get the "id" productos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Productos> findOne(Long id);

    /**
     * Delete the "id" productos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
