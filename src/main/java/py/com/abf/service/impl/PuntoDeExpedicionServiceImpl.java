package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.PuntoDeExpedicion;
import py.com.abf.repository.PuntoDeExpedicionRepository;
import py.com.abf.service.PuntoDeExpedicionService;

/**
 * Service Implementation for managing {@link PuntoDeExpedicion}.
 */
@Service
@Transactional
public class PuntoDeExpedicionServiceImpl implements PuntoDeExpedicionService {

    private final Logger log = LoggerFactory.getLogger(PuntoDeExpedicionServiceImpl.class);

    private final PuntoDeExpedicionRepository puntoDeExpedicionRepository;

    public PuntoDeExpedicionServiceImpl(PuntoDeExpedicionRepository puntoDeExpedicionRepository) {
        this.puntoDeExpedicionRepository = puntoDeExpedicionRepository;
    }

    @Override
    public PuntoDeExpedicion save(PuntoDeExpedicion puntoDeExpedicion) {
        log.debug("Request to save PuntoDeExpedicion : {}", puntoDeExpedicion);
        return puntoDeExpedicionRepository.save(puntoDeExpedicion);
    }

    @Override
    public PuntoDeExpedicion update(PuntoDeExpedicion puntoDeExpedicion) {
        log.debug("Request to update PuntoDeExpedicion : {}", puntoDeExpedicion);
        return puntoDeExpedicionRepository.save(puntoDeExpedicion);
    }

    @Override
    public Optional<PuntoDeExpedicion> partialUpdate(PuntoDeExpedicion puntoDeExpedicion) {
        log.debug("Request to partially update PuntoDeExpedicion : {}", puntoDeExpedicion);

        return puntoDeExpedicionRepository
            .findById(puntoDeExpedicion.getId())
            .map(existingPuntoDeExpedicion -> {
                if (puntoDeExpedicion.getNumeroPuntoDeExpedicion() != null) {
                    existingPuntoDeExpedicion.setNumeroPuntoDeExpedicion(puntoDeExpedicion.getNumeroPuntoDeExpedicion());
                }

                return existingPuntoDeExpedicion;
            })
            .map(puntoDeExpedicionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PuntoDeExpedicion> findAll(Pageable pageable) {
        log.debug("Request to get all PuntoDeExpedicions");
        return puntoDeExpedicionRepository.findAll(pageable);
    }

    public Page<PuntoDeExpedicion> findAllWithEagerRelationships(Pageable pageable) {
        return puntoDeExpedicionRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PuntoDeExpedicion> findOne(Long id) {
        log.debug("Request to get PuntoDeExpedicion : {}", id);
        return puntoDeExpedicionRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PuntoDeExpedicion : {}", id);
        puntoDeExpedicionRepository.deleteById(id);
    }
}
