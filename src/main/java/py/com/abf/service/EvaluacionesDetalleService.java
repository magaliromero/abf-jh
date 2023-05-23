package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.EvaluacionesDetalle;

/**
 * Service Interface for managing {@link EvaluacionesDetalle}.
 */
public interface EvaluacionesDetalleService {
    /**
     * Save a evaluacionesDetalle.
     *
     * @param evaluacionesDetalle the entity to save.
     * @return the persisted entity.
     */
    EvaluacionesDetalle save(EvaluacionesDetalle evaluacionesDetalle);

    /**
     * Updates a evaluacionesDetalle.
     *
     * @param evaluacionesDetalle the entity to update.
     * @return the persisted entity.
     */
    EvaluacionesDetalle update(EvaluacionesDetalle evaluacionesDetalle);

    /**
     * Partially updates a evaluacionesDetalle.
     *
     * @param evaluacionesDetalle the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EvaluacionesDetalle> partialUpdate(EvaluacionesDetalle evaluacionesDetalle);

    /**
     * Get all the evaluacionesDetalles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EvaluacionesDetalle> findAll(Pageable pageable);

    /**
     * Get all the evaluacionesDetalles with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EvaluacionesDetalle> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" evaluacionesDetalle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EvaluacionesDetalle> findOne(Long id);

    /**
     * Delete the "id" evaluacionesDetalle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
