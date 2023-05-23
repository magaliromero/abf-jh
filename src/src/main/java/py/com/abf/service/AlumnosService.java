package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.Alumnos;

/**
 * Service Interface for managing {@link Alumnos}.
 */
public interface AlumnosService {
    /**
     * Save a alumnos.
     *
     * @param alumnos the entity to save.
     * @return the persisted entity.
     */
    Alumnos save(Alumnos alumnos);

    /**
     * Updates a alumnos.
     *
     * @param alumnos the entity to update.
     * @return the persisted entity.
     */
    Alumnos update(Alumnos alumnos);

    /**
     * Partially updates a alumnos.
     *
     * @param alumnos the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Alumnos> partialUpdate(Alumnos alumnos);

    /**
     * Get all the alumnos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Alumnos> findAll(Pageable pageable);

    /**
     * Get all the alumnos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Alumnos> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" alumnos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Alumnos> findOne(Long id);

    /**
     * Delete the "id" alumnos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
