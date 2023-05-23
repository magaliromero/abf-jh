package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.Funcionarios;

/**
 * Service Interface for managing {@link Funcionarios}.
 */
public interface FuncionariosService {
    /**
     * Save a funcionarios.
     *
     * @param funcionarios the entity to save.
     * @return the persisted entity.
     */
    Funcionarios save(Funcionarios funcionarios);

    /**
     * Updates a funcionarios.
     *
     * @param funcionarios the entity to update.
     * @return the persisted entity.
     */
    Funcionarios update(Funcionarios funcionarios);

    /**
     * Partially updates a funcionarios.
     *
     * @param funcionarios the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Funcionarios> partialUpdate(Funcionarios funcionarios);

    /**
     * Get all the funcionarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Funcionarios> findAll(Pageable pageable);

    /**
     * Get all the funcionarios with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Funcionarios> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" funcionarios.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Funcionarios> findOne(Long id);

    /**
     * Delete the "id" funcionarios.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
