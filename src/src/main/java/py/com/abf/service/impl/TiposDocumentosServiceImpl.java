package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.TiposDocumentos;
import py.com.abf.repository.TiposDocumentosRepository;
import py.com.abf.service.TiposDocumentosService;

/**
 * Service Implementation for managing {@link TiposDocumentos}.
 */
@Service
@Transactional
public class TiposDocumentosServiceImpl implements TiposDocumentosService {

    private final Logger log = LoggerFactory.getLogger(TiposDocumentosServiceImpl.class);

    private final TiposDocumentosRepository tiposDocumentosRepository;

    public TiposDocumentosServiceImpl(TiposDocumentosRepository tiposDocumentosRepository) {
        this.tiposDocumentosRepository = tiposDocumentosRepository;
    }

    @Override
    public TiposDocumentos save(TiposDocumentos tiposDocumentos) {
        log.debug("Request to save TiposDocumentos : {}", tiposDocumentos);
        return tiposDocumentosRepository.save(tiposDocumentos);
    }

    @Override
    public TiposDocumentos update(TiposDocumentos tiposDocumentos) {
        log.debug("Request to update TiposDocumentos : {}", tiposDocumentos);
        return tiposDocumentosRepository.save(tiposDocumentos);
    }

    @Override
    public Optional<TiposDocumentos> partialUpdate(TiposDocumentos tiposDocumentos) {
        log.debug("Request to partially update TiposDocumentos : {}", tiposDocumentos);

        return tiposDocumentosRepository
            .findById(tiposDocumentos.getId())
            .map(existingTiposDocumentos -> {
                if (tiposDocumentos.getCodigo() != null) {
                    existingTiposDocumentos.setCodigo(tiposDocumentos.getCodigo());
                }
                if (tiposDocumentos.getDescripcion() != null) {
                    existingTiposDocumentos.setDescripcion(tiposDocumentos.getDescripcion());
                }

                return existingTiposDocumentos;
            })
            .map(tiposDocumentosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TiposDocumentos> findAll(Pageable pageable) {
        log.debug("Request to get all TiposDocumentos");
        return tiposDocumentosRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TiposDocumentos> findOne(Long id) {
        log.debug("Request to get TiposDocumentos : {}", id);
        return tiposDocumentosRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TiposDocumentos : {}", id);
        tiposDocumentosRepository.deleteById(id);
    }
}
