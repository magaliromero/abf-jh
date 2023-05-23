package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Matricula;
import py.com.abf.repository.MatriculaRepository;
import py.com.abf.service.MatriculaService;

/**
 * Service Implementation for managing {@link Matricula}.
 */
@Service
@Transactional
public class MatriculaServiceImpl implements MatriculaService {

    private final Logger log = LoggerFactory.getLogger(MatriculaServiceImpl.class);

    private final MatriculaRepository matriculaRepository;

    public MatriculaServiceImpl(MatriculaRepository matriculaRepository) {
        this.matriculaRepository = matriculaRepository;
    }

    @Override
    public Matricula save(Matricula matricula) {
        log.debug("Request to save Matricula : {}", matricula);
        return matriculaRepository.save(matricula);
    }

    @Override
    public Matricula update(Matricula matricula) {
        log.debug("Request to update Matricula : {}", matricula);
        return matriculaRepository.save(matricula);
    }

    @Override
    public Optional<Matricula> partialUpdate(Matricula matricula) {
        log.debug("Request to partially update Matricula : {}", matricula);

        return matriculaRepository
            .findById(matricula.getId())
            .map(existingMatricula -> {
                if (matricula.getConcepto() != null) {
                    existingMatricula.setConcepto(matricula.getConcepto());
                }
                if (matricula.getMonto() != null) {
                    existingMatricula.setMonto(matricula.getMonto());
                }
                if (matricula.getFechaInscripcion() != null) {
                    existingMatricula.setFechaInscripcion(matricula.getFechaInscripcion());
                }
                if (matricula.getFechaInicio() != null) {
                    existingMatricula.setFechaInicio(matricula.getFechaInicio());
                }
                if (matricula.getFechaPago() != null) {
                    existingMatricula.setFechaPago(matricula.getFechaPago());
                }
                if (matricula.getEstado() != null) {
                    existingMatricula.setEstado(matricula.getEstado());
                }

                return existingMatricula;
            })
            .map(matriculaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Matricula> findAll(Pageable pageable) {
        log.debug("Request to get all Matriculas");
        return matriculaRepository.findAll(pageable);
    }

    public Page<Matricula> findAllWithEagerRelationships(Pageable pageable) {
        return matriculaRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Matricula> findOne(Long id) {
        log.debug("Request to get Matricula : {}", id);
        return matriculaRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Matricula : {}", id);
        matriculaRepository.deleteById(id);
    }
}
