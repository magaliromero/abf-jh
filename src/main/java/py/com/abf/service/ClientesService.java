package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.Clientes;

/**
 * Service Interface for managing {@link Clientes}.
 */
public interface ClientesService {
    /**
     * Save a clientes.
     *
     * @param clientes the entity to save.
     * @return the persisted entity.
     */
    Clientes save(Clientes clientes);

    /**
     * Updates a clientes.
     *
     * @param clientes the entity to update.
     * @return the persisted entity.
     */
    Clientes update(Clientes clientes);

    /**
     * Partially updates a clientes.
     *
     * @param clientes the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Clientes> partialUpdate(Clientes clientes);

    /**
     * Get all the clientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Clientes> findAll(Pageable pageable);

    /**
     * Get the "id" clientes.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Clientes> findOne(Long id);

    /**
     * Delete the "id" clientes.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
