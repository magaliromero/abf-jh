package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.RegistroClases;

/**
 * Service Interface for managing {@link RegistroClases}.
 */
public interface RegistroClasesService {
    /**
     * Save a registroClases.
     *
     * @param registroClases the entity to save.
     * @return the persisted entity.
     */
    RegistroClases save(RegistroClases registroClases);

    /**
     * Updates a registroClases.
     *
     * @param registroClases the entity to update.
     * @return the persisted entity.
     */
    RegistroClases update(RegistroClases registroClases);

    /**
     * Partially updates a registroClases.
     *
     * @param registroClases the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RegistroClases> partialUpdate(RegistroClases registroClases);

    /**
     * Get all the registroClases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RegistroClases> findAll(Pageable pageable);

    /**
     * Get all the registroClases with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RegistroClases> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" registroClases.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RegistroClases> findOne(Long id);

    /**
     * Delete the "id" registroClases.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
