package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Cursos;
import py.com.abf.repository.CursosRepository;
import py.com.abf.service.CursosService;

/**
 * Service Implementation for managing {@link Cursos}.
 */
@Service
@Transactional
public class CursosServiceImpl implements CursosService {

    private final Logger log = LoggerFactory.getLogger(CursosServiceImpl.class);

    private final CursosRepository cursosRepository;

    public CursosServiceImpl(CursosRepository cursosRepository) {
        this.cursosRepository = cursosRepository;
    }

    @Override
    public Cursos save(Cursos cursos) {
        log.debug("Request to save Cursos : {}", cursos);
        return cursosRepository.save(cursos);
    }

    @Override
    public Cursos update(Cursos cursos) {
        log.debug("Request to update Cursos : {}", cursos);
        return cursosRepository.save(cursos);
    }

    @Override
    public Optional<Cursos> partialUpdate(Cursos cursos) {
        log.debug("Request to partially update Cursos : {}", cursos);

        return cursosRepository
            .findById(cursos.getId())
            .map(existingCursos -> {
                if (cursos.getNombreCurso() != null) {
                    existingCursos.setNombreCurso(cursos.getNombreCurso());
                }
                if (cursos.getDescripcion() != null) {
                    existingCursos.setDescripcion(cursos.getDescripcion());
                }
                if (cursos.getFechaInicio() != null) {
                    existingCursos.setFechaInicio(cursos.getFechaInicio());
                }
                if (cursos.getFechaFin() != null) {
                    existingCursos.setFechaFin(cursos.getFechaFin());
                }
                if (cursos.getCantidadClases() != null) {
                    existingCursos.setCantidadClases(cursos.getCantidadClases());
                }
                if (cursos.getNivel() != null) {
                    existingCursos.setNivel(cursos.getNivel());
                }

                return existingCursos;
            })
            .map(cursosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cursos> findAll(Pageable pageable) {
        log.debug("Request to get all Cursos");
        return cursosRepository.findAll(pageable);
    }

    public Page<Cursos> findAllWithEagerRelationships(Pageable pageable) {
        return cursosRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cursos> findOne(Long id) {
        log.debug("Request to get Cursos : {}", id);
        return cursosRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cursos : {}", id);
        cursosRepository.deleteById(id);
    }
}
