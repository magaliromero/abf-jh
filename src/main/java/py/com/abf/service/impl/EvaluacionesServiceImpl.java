package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Evaluaciones;
import py.com.abf.repository.EvaluacionesRepository;
import py.com.abf.service.EvaluacionesService;

/**
 * Service Implementation for managing {@link Evaluaciones}.
 */
@Service
@Transactional
public class EvaluacionesServiceImpl implements EvaluacionesService {

    private final Logger log = LoggerFactory.getLogger(EvaluacionesServiceImpl.class);

    private final EvaluacionesRepository evaluacionesRepository;

    public EvaluacionesServiceImpl(EvaluacionesRepository evaluacionesRepository) {
        this.evaluacionesRepository = evaluacionesRepository;
    }

    @Override
    public Evaluaciones save(Evaluaciones evaluaciones) {
        log.debug("Request to save Evaluaciones : {}", evaluaciones);
        return evaluacionesRepository.save(evaluaciones);
    }

    @Override
    public Evaluaciones update(Evaluaciones evaluaciones) {
        log.debug("Request to update Evaluaciones : {}", evaluaciones);
        return evaluacionesRepository.save(evaluaciones);
    }

    @Override
    public Optional<Evaluaciones> partialUpdate(Evaluaciones evaluaciones) {
        log.debug("Request to partially update Evaluaciones : {}", evaluaciones);

        return evaluacionesRepository
            .findById(evaluaciones.getId())
            .map(existingEvaluaciones -> {
                if (evaluaciones.getNroEvaluacion() != null) {
                    existingEvaluaciones.setNroEvaluacion(evaluaciones.getNroEvaluacion());
                }
                if (evaluaciones.getFecha() != null) {
                    existingEvaluaciones.setFecha(evaluaciones.getFecha());
                }

                return existingEvaluaciones;
            })
            .map(evaluacionesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Evaluaciones> findAll(Pageable pageable) {
        log.debug("Request to get all Evaluaciones");
        return evaluacionesRepository.findAll(pageable);
    }

    public Page<Evaluaciones> findAllWithEagerRelationships(Pageable pageable) {
        return evaluacionesRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Evaluaciones> findOne(Long id) {
        log.debug("Request to get Evaluaciones : {}", id);
        return evaluacionesRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Evaluaciones : {}", id);
        evaluacionesRepository.deleteById(id);
    }
}
