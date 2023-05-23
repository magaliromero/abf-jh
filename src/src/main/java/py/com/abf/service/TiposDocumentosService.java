package py.com.abf.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import py.com.abf.domain.TiposDocumentos;

/**
 * Service Interface for managing {@link TiposDocumentos}.
 */
public interface TiposDocumentosService {
    /**
     * Save a tiposDocumentos.
     *
     * @param tiposDocumentos the entity to save.
     * @return the persisted entity.
     */
    TiposDocumentos save(TiposDocumentos tiposDocumentos);

    /**
     * Updates a tiposDocumentos.
     *
     * @param tiposDocumentos the entity to update.
     * @return the persisted entity.
     */
    TiposDocumentos update(TiposDocumentos tiposDocumentos);

    /**
     * Partially updates a tiposDocumentos.
     *
     * @param tiposDocumentos the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TiposDocumentos> partialUpdate(TiposDocumentos tiposDocumentos);

    /**
     * Get all the tiposDocumentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TiposDocumentos> findAll(Pageable pageable);

    /**
     * Get the "id" tiposDocumentos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TiposDocumentos> findOne(Long id);

    /**
     * Delete the "id" tiposDocumentos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
