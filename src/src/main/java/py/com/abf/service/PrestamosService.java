package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.Prestamos;

/**
 * Service Interface for managing {@link Prestamos}.
 */
public interface PrestamosService {
    /**
     * Save a prestamos.
     *
     * @param prestamos the entity to save.
     * @return the persisted entity.
     */
    Prestamos save(Prestamos prestamos);

    /**
     * Updates a prestamos.
     *
     * @param prestamos the entity to update.
     * @return the persisted entity.
     */
    Prestamos update(Prestamos prestamos);

    /**
     * Partially updates a prestamos.
     *
     * @param prestamos the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Prestamos> partialUpdate(Prestamos prestamos);

    /**
     * Get all the prestamos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Prestamos> findAll(Pageable pageable);

    /**
     * Get all the prestamos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Prestamos> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" prestamos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Prestamos> findOne(Long id);

    /**
     * Delete the "id" prestamos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
