package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.Matricula;

/**
 * Service Interface for managing {@link Matricula}.
 */
public interface MatriculaService {
    /**
     * Save a matricula.
     *
     * @param matricula the entity to save.
     * @return the persisted entity.
     */
    Matricula save(Matricula matricula);

    /**
     * Updates a matricula.
     *
     * @param matricula the entity to update.
     * @return the persisted entity.
     */
    Matricula update(Matricula matricula);

    /**
     * Partially updates a matricula.
     *
     * @param matricula the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Matricula> partialUpdate(Matricula matricula);

    /**
     * Get all the matriculas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Matricula> findAll(Pageable pageable);

    /**
     * Get all the matriculas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Matricula> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" matricula.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Matricula> findOne(Long id);

    /**
     * Delete the "id" matricula.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
