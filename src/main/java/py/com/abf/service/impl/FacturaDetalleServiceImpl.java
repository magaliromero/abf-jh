package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.FacturaDetalle;
import py.com.abf.repository.FacturaDetalleRepository;
import py.com.abf.service.FacturaDetalleService;

/**
 * Service Implementation for managing {@link FacturaDetalle}.
 */
@Service
@Transactional
public class FacturaDetalleServiceImpl implements FacturaDetalleService {

    private final Logger log = LoggerFactory.getLogger(FacturaDetalleServiceImpl.class);

    private final FacturaDetalleRepository facturaDetalleRepository;

    public FacturaDetalleServiceImpl(FacturaDetalleRepository facturaDetalleRepository) {
        this.facturaDetalleRepository = facturaDetalleRepository;
    }

    @Override
    public FacturaDetalle save(FacturaDetalle facturaDetalle) {
        log.debug("Request to save FacturaDetalle : {}", facturaDetalle);
        return facturaDetalleRepository.save(facturaDetalle);
    }

    @Override
    public FacturaDetalle update(FacturaDetalle facturaDetalle) {
        log.debug("Request to update FacturaDetalle : {}", facturaDetalle);
        return facturaDetalleRepository.save(facturaDetalle);
    }

    @Override
    public Optional<FacturaDetalle> partialUpdate(FacturaDetalle facturaDetalle) {
        log.debug("Request to partially update FacturaDetalle : {}", facturaDetalle);

        return facturaDetalleRepository
            .findById(facturaDetalle.getId())
            .map(existingFacturaDetalle -> {
                if (facturaDetalle.getCantidad() != null) {
                    existingFacturaDetalle.setCantidad(facturaDetalle.getCantidad());
                }
                if (facturaDetalle.getPrecioUnitario() != null) {
                    existingFacturaDetalle.setPrecioUnitario(facturaDetalle.getPrecioUnitario());
                }
                if (facturaDetalle.getSubtotal() != null) {
                    existingFacturaDetalle.setSubtotal(facturaDetalle.getSubtotal());
                }
                if (facturaDetalle.getPorcentajeIva() != null) {
                    existingFacturaDetalle.setPorcentajeIva(facturaDetalle.getPorcentajeIva());
                }
                if (facturaDetalle.getValorPorcentaje() != null) {
                    existingFacturaDetalle.setValorPorcentaje(facturaDetalle.getValorPorcentaje());
                }

                return existingFacturaDetalle;
            })
            .map(facturaDetalleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacturaDetalle> findAll(Pageable pageable) {
        log.debug("Request to get all FacturaDetalles");
        return facturaDetalleRepository.findAll(pageable);
    }

    public Page<FacturaDetalle> findAllWithEagerRelationships(Pageable pageable) {
        return facturaDetalleRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FacturaDetalle> findOne(Long id) {
        log.debug("Request to get FacturaDetalle : {}", id);
        return facturaDetalleRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FacturaDetalle : {}", id);
        facturaDetalleRepository.deleteById(id);
    }
}
