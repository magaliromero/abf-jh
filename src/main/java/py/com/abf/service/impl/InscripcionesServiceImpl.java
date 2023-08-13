package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Inscripciones;
import py.com.abf.repository.InscripcionesRepository;
import py.com.abf.service.InscripcionesService;

/**
 * Service Implementation for managing {@link Inscripciones}.
 */
@Service
@Transactional
public class InscripcionesServiceImpl implements InscripcionesService {

    private final Logger log = LoggerFactory.getLogger(InscripcionesServiceImpl.class);

    private final InscripcionesRepository inscripcionesRepository;

    public InscripcionesServiceImpl(InscripcionesRepository inscripcionesRepository) {
        this.inscripcionesRepository = inscripcionesRepository;
    }

    @Override
    public Inscripciones save(Inscripciones inscripciones) {
        log.debug("Request to save Inscripciones : {}", inscripciones);
        return inscripcionesRepository.save(inscripciones);
    }

    @Override
    public Inscripciones update(Inscripciones inscripciones) {
        log.debug("Request to update Inscripciones : {}", inscripciones);
        return inscripcionesRepository.save(inscripciones);
    }

    @Override
    public Optional<Inscripciones> partialUpdate(Inscripciones inscripciones) {
        log.debug("Request to partially update Inscripciones : {}", inscripciones);

        return inscripcionesRepository
            .findById(inscripciones.getId())
            .map(existingInscripciones -> {
                if (inscripciones.getFechaInscripcion() != null) {
                    existingInscripciones.setFechaInscripcion(inscripciones.getFechaInscripcion());
                }

                return existingInscripciones;
            })
            .map(inscripcionesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inscripciones> findAll(Pageable pageable) {
        log.debug("Request to get all Inscripciones");
        return inscripcionesRepository.findAll(pageable);
    }

    public Page<Inscripciones> findAllWithEagerRelationships(Pageable pageable) {
        return inscripcionesRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inscripciones> findOne(Long id) {
        log.debug("Request to get Inscripciones : {}", id);
        return inscripcionesRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Inscripciones : {}", id);
        inscripcionesRepository.deleteById(id);
    }
}
