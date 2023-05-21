package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Prestamos;
import py.com.abf.repository.PrestamosRepository;
import py.com.abf.service.PrestamosService;

/**
 * Service Implementation for managing {@link Prestamos}.
 */
@Service
@Transactional
public class PrestamosServiceImpl implements PrestamosService {

    private final Logger log = LoggerFactory.getLogger(PrestamosServiceImpl.class);

    private final PrestamosRepository prestamosRepository;

    public PrestamosServiceImpl(PrestamosRepository prestamosRepository) {
        this.prestamosRepository = prestamosRepository;
    }

    @Override
    public Prestamos save(Prestamos prestamos) {
        log.debug("Request to save Prestamos : {}", prestamos);
        return prestamosRepository.save(prestamos);
    }

    @Override
    public Prestamos update(Prestamos prestamos) {
        log.debug("Request to update Prestamos : {}", prestamos);
        return prestamosRepository.save(prestamos);
    }

    @Override
    public Optional<Prestamos> partialUpdate(Prestamos prestamos) {
        log.debug("Request to partially update Prestamos : {}", prestamos);

        return prestamosRepository
            .findById(prestamos.getId())
            .map(existingPrestamos -> {
                if (prestamos.getFechaPrestamo() != null) {
                    existingPrestamos.setFechaPrestamo(prestamos.getFechaPrestamo());
                }
                if (prestamos.getFechaDevolucion() != null) {
                    existingPrestamos.setFechaDevolucion(prestamos.getFechaDevolucion());
                }
                if (prestamos.getEstado() != null) {
                    existingPrestamos.setEstado(prestamos.getEstado());
                }

                return existingPrestamos;
            })
            .map(prestamosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Prestamos> findAll(Pageable pageable) {
        log.debug("Request to get all Prestamos");
        return prestamosRepository.findAll(pageable);
    }

    public Page<Prestamos> findAllWithEagerRelationships(Pageable pageable) {
        return prestamosRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Prestamos> findOne(Long id) {
        log.debug("Request to get Prestamos : {}", id);
        return prestamosRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Prestamos : {}", id);
        prestamosRepository.deleteById(id);
    }
}
