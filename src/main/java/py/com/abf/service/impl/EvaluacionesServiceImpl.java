package py.com.abf.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Evaluaciones;
import py.com.abf.domain.EvaluacionesConDetalle;
import py.com.abf.domain.EvaluacionesDetalle;
import py.com.abf.domain.EvaluacionesDetalleItem;
import py.com.abf.domain.FacturaConDetalle;
import py.com.abf.domain.FacturaDetalle;
import py.com.abf.domain.FacturaDetalleItem;
import py.com.abf.domain.Facturas;
import py.com.abf.domain.Productos;
import py.com.abf.domain.Temas;
import py.com.abf.repository.EvaluacionesDetalleRepository;
import py.com.abf.repository.EvaluacionesRepository;
import py.com.abf.repository.TemasRepository;
import py.com.abf.service.EvaluacionesService;

/**
 * Service Implementation for managing {@link Evaluaciones}.
 */
@Service
@Transactional
public class EvaluacionesServiceImpl implements EvaluacionesService {

    private final Logger log = LoggerFactory.getLogger(EvaluacionesServiceImpl.class);

    private final EvaluacionesRepository evaluacionesRepository;
    private final EvaluacionesDetalleRepository evaluacionesDetalleRepository;
    private final TemasRepository temasRepository;

    public EvaluacionesServiceImpl(
        EvaluacionesRepository evaluacionesRepository,
        TemasRepository temasRepository,
        EvaluacionesDetalleRepository evaluacionesDetalleRepository
    ) {
        this.evaluacionesRepository = evaluacionesRepository;
        this.temasRepository = temasRepository;
        this.evaluacionesDetalleRepository = evaluacionesDetalleRepository;
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

    public Evaluaciones saveWithDetails(EvaluacionesConDetalle data) {
        log.debug("Request to save data : {}", data);

        Evaluaciones f = evaluacionesRepository.save(data.getCabecera());
        log.debug("Factura guardada : {}", f);

        List<EvaluacionesDetalleItem> items = data.getDetalle();
        for (EvaluacionesDetalleItem temp : items) {
            Temas p = this.temasRepository.findById(temp.getTema().longValue()).orElse(null);
            if (p != null) {
                EvaluacionesDetalle fd = new EvaluacionesDetalle();
                fd.setComentarios(temp.getComentarios());
                fd.puntaje(temp.getPuntaje());
                fd.setTemas(p);

                evaluacionesDetalleRepository.save(fd);
            }
        }

        return f;
    }
}
