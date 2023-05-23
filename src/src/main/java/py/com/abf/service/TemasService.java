package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.Temas;

/**
 * Service Interface for managing {@link Temas}.
 */
public interface TemasService {
    /**
     * Save a temas.
     *
     * @param temas the entity to save.
     * @return the persisted entity.
     */
    Temas save(Temas temas);

    /**
     * Updates a temas.
     *
     * @param temas the entity to update.
     * @return the persisted entity.
     */
    Temas update(Temas temas);

    /**
     * Partially updates a temas.
     *
     * @param temas the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Temas> partialUpdate(Temas temas);

    /**
     * Get all the temas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Temas> findAll(Pageable pageable);

    /**
     * Get the "id" temas.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Temas> findOne(Long id);

    /**
     * Delete the "id" temas.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
