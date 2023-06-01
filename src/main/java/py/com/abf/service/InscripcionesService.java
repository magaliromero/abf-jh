package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.Inscripciones;

/**
 * Service Interface for managing {@link Inscripciones}.
 */
public interface InscripcionesService {
    /**
     * Save a inscripciones.
     *
     * @param inscripciones the entity to save.
     * @return the persisted entity.
     */
    Inscripciones save(Inscripciones inscripciones);

    /**
     * Updates a inscripciones.
     *
     * @param inscripciones the entity to update.
     * @return the persisted entity.
     */
    Inscripciones update(Inscripciones inscripciones);

    /**
     * Partially updates a inscripciones.
     *
     * @param inscripciones the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Inscripciones> partialUpdate(Inscripciones inscripciones);

    /**
     * Get all the inscripciones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Inscripciones> findAll(Pageable pageable);

    /**
     * Get all the inscripciones with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Inscripciones> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" inscripciones.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Inscripciones> findOne(Long id);

    /**
     * Delete the "id" inscripciones.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
