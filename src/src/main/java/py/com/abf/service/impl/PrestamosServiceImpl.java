package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Materiales;
import py.com.abf.domain.Prestamos;
import py.com.abf.domain.enumeration.EstadosPrestamos;
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

    private final MaterialesServiceImpl materialesService;

    public PrestamosServiceImpl(PrestamosRepository prestamosRepository, MaterialesServiceImpl materialesService) {
        this.prestamosRepository = prestamosRepository;
        this.materialesService = materialesService;
    }

    @Override
    public Prestamos save(Prestamos prestamos) {
        log.debug("Request to save Prestamos : {}", prestamos);
        Materiales m = prestamos.getMateriales();
        Integer cantidad = m.getCantidad();
        Integer cantidadEnPrestamo = m.getCantidadEnPrestamo();
        m.setCantidad(--cantidad);
        m.setCantidadEnPrestamo(++cantidadEnPrestamo);
        materialesService.update(m);
        return prestamosRepository.save(prestamos);
    }

    @Override
    public Prestamos update(Prestamos prestamos) {
        log.debug("	: {}", prestamos);
        log.debug("Request to update estado entrante : {}", prestamos.getEstado().toString());

        log.debug("Request to update estado TEST : {}", prestamos.getEstado().compareTo(EstadosPrestamos.DEVUELTO));
        log.debug("Request to update estado TEST 2 : {}", prestamos.getEstado().compareTo(EstadosPrestamos.DEVUELTO));
        Materiales m = prestamos.getMateriales();
        Integer cantidad = m.getCantidad();
        Integer cantidadEnPrestamo = m.getCantidadEnPrestamo();
        if (prestamos.getEstado().compareTo(EstadosPrestamos.DEVUELTO) == 0) {
            log.debug("Request to update DEVUELTO : {}", prestamos.getEstado());

            m.setCantidad(++cantidad);
            m.setCantidadEnPrestamo(--cantidadEnPrestamo);
            materialesService.update(m);
        }
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
                    log.debug("Request to update estado entrante partialUpdate : {}", prestamos.getEstado());
                    log.debug("Request to update estado TEST  partialUpdate: {}", prestamos.getEstado().getValue() == "DEVUELTO");
                    log.debug("Request to update estado TEST 2 partialUpdate: {}", prestamos.getEstado().getValue().equals("DEVUELTO"));

                    existingPrestamos.setEstado(prestamos.getEstado());
                    if (prestamos.getEstado().compareTo(EstadosPrestamos.DEVUELTO) == 0) {
                        log.debug("Request to update DEVUELTO : {}", prestamos.getEstado());
                        Materiales m = prestamos.getMateriales();
                        Integer cantidad = m.getCantidad();
                        Integer cantidadEnPrestamo = m.getCantidadEnPrestamo();
                        m.setCantidad(++cantidad);
                        m.setCantidadEnPrestamo(--cantidadEnPrestamo);
                        materialesService.update(m);
                    }
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
