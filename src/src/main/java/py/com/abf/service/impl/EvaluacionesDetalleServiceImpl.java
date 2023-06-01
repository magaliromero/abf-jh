package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.EvaluacionesDetalle;
import py.com.abf.repository.EvaluacionesDetalleRepository;
import py.com.abf.service.EvaluacionesDetalleService;

/**
 * Service Implementation for managing {@link EvaluacionesDetalle}.
 */
@Service
@Transactional
public class EvaluacionesDetalleServiceImpl implements EvaluacionesDetalleService {

    private final Logger log = LoggerFactory.getLogger(EvaluacionesDetalleServiceImpl.class);

    private final EvaluacionesDetalleRepository evaluacionesDetalleRepository;

    public EvaluacionesDetalleServiceImpl(EvaluacionesDetalleRepository evaluacionesDetalleRepository) {
        this.evaluacionesDetalleRepository = evaluacionesDetalleRepository;
    }

    @Override
    public EvaluacionesDetalle save(EvaluacionesDetalle evaluacionesDetalle) {
        log.debug("Request to save EvaluacionesDetalle : {}", evaluacionesDetalle);
        return evaluacionesDetalleRepository.save(evaluacionesDetalle);
    }

    @Override
    public EvaluacionesDetalle update(EvaluacionesDetalle evaluacionesDetalle) {
        log.debug("Request to update EvaluacionesDetalle : {}", evaluacionesDetalle);
        return evaluacionesDetalleRepository.save(evaluacionesDetalle);
    }

    @Override
    public Optional<EvaluacionesDetalle> partialUpdate(EvaluacionesDetalle evaluacionesDetalle) {
        log.debug("Request to partially update EvaluacionesDetalle : {}", evaluacionesDetalle);

        return evaluacionesDetalleRepository
            .findById(evaluacionesDetalle.getId())
            .map(existingEvaluacionesDetalle -> {
                if (evaluacionesDetalle.getComentarios() != null) {
                    existingEvaluacionesDetalle.setComentarios(evaluacionesDetalle.getComentarios());
                }
                if (evaluacionesDetalle.getPuntaje() != null) {
                    existingEvaluacionesDetalle.setPuntaje(evaluacionesDetalle.getPuntaje());
                }

                return existingEvaluacionesDetalle;
            })
            .map(evaluacionesDetalleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EvaluacionesDetalle> findAll(Pageable pageable) {
        log.debug("Request to get all EvaluacionesDetalles");
        return evaluacionesDetalleRepository.findAll(pageable);
    }

    public Page<EvaluacionesDetalle> findAllWithEagerRelationships(Pageable pageable) {
        return evaluacionesDetalleRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EvaluacionesDetalle> findOne(Long id) {
        log.debug("Request to get EvaluacionesDetalle : {}", id);
        return evaluacionesDetalleRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EvaluacionesDetalle : {}", id);
        evaluacionesDetalleRepository.deleteById(id);
    }
}
