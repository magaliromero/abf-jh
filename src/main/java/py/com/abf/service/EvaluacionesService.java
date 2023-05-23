package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.Evaluaciones;

/**
 * Service Interface for managing {@link Evaluaciones}.
 */
public interface EvaluacionesService {
    /**
     * Save a evaluaciones.
     *
     * @param evaluaciones the entity to save.
     * @return the persisted entity.
     */
    Evaluaciones save(Evaluaciones evaluaciones);

    /**
     * Updates a evaluaciones.
     *
     * @param evaluaciones the entity to update.
     * @return the persisted entity.
     */
    Evaluaciones update(Evaluaciones evaluaciones);

    /**
     * Partially updates a evaluaciones.
     *
     * @param evaluaciones the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Evaluaciones> partialUpdate(Evaluaciones evaluaciones);

    /**
     * Get all the evaluaciones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Evaluaciones> findAll(Pageable pageable);

    /**
     * Get all the evaluaciones with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Evaluaciones> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" evaluaciones.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Evaluaciones> findOne(Long id);

    /**
     * Delete the "id" evaluaciones.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
