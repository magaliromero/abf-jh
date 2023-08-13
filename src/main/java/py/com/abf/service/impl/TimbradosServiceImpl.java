package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Timbrados;
import py.com.abf.repository.TimbradosRepository;
import py.com.abf.service.TimbradosService;

/**
 * Service Implementation for managing {@link Timbrados}.
 */
@Service
@Transactional
public class TimbradosServiceImpl implements TimbradosService {

    private final Logger log = LoggerFactory.getLogger(TimbradosServiceImpl.class);

    private final TimbradosRepository timbradosRepository;

    public TimbradosServiceImpl(TimbradosRepository timbradosRepository) {
        this.timbradosRepository = timbradosRepository;
    }

    @Override
    public Timbrados save(Timbrados timbrados) {
        log.debug("Request to save Timbrados : {}", timbrados);
        return timbradosRepository.save(timbrados);
    }

    @Override
    public Timbrados update(Timbrados timbrados) {
        log.debug("Request to update Timbrados : {}", timbrados);
        return timbradosRepository.save(timbrados);
    }

    @Override
    public Optional<Timbrados> partialUpdate(Timbrados timbrados) {
        log.debug("Request to partially update Timbrados : {}", timbrados);

        return timbradosRepository
            .findById(timbrados.getId())
            .map(existingTimbrados -> {
                if (timbrados.getNumeroTimbrado() != null) {
                    existingTimbrados.setNumeroTimbrado(timbrados.getNumeroTimbrado());
                }
                if (timbrados.getFechaInicio() != null) {
                    existingTimbrados.setFechaInicio(timbrados.getFechaInicio());
                }
                if (timbrados.getFechaFin() != null) {
                    existingTimbrados.setFechaFin(timbrados.getFechaFin());
                }

                return existingTimbrados;
            })
            .map(timbradosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Timbrados> findAll(Pageable pageable) {
        log.debug("Request to get all Timbrados");
        return timbradosRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Timbrados> findOne(Long id) {
        log.debug("Request to get Timbrados : {}", id);
        return timbradosRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Timbrados : {}", id);
        timbradosRepository.deleteById(id);
    }
}
