package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Facturas;
import py.com.abf.repository.FacturasRepository;
import py.com.abf.service.FacturasService;

/**
 * Service Implementation for managing {@link Facturas}.
 */
@Service
@Transactional
public class FacturasServiceImpl implements FacturasService {

    private final Logger log = LoggerFactory.getLogger(FacturasServiceImpl.class);

    private final FacturasRepository facturasRepository;

    public FacturasServiceImpl(FacturasRepository facturasRepository) {
        this.facturasRepository = facturasRepository;
    }

    @Override
    public Facturas save(Facturas facturas) {
        log.debug("Request to save Facturas : {}", facturas);
        return facturasRepository.save(facturas);
    }

    @Override
    public Facturas update(Facturas facturas) {
        log.debug("Request to update Facturas : {}", facturas);
        return facturasRepository.save(facturas);
    }

    @Override
    public Optional<Facturas> partialUpdate(Facturas facturas) {
        log.debug("Request to partially update Facturas : {}", facturas);

        return facturasRepository
            .findById(facturas.getId())
            .map(existingFacturas -> {
                if (facturas.getFecha() != null) {
                    existingFacturas.setFecha(facturas.getFecha());
                }
                if (facturas.getFacturaNro() != null) {
                    existingFacturas.setFacturaNro(facturas.getFacturaNro());
                }
                if (facturas.getTimbrado() != null) {
                    existingFacturas.setTimbrado(facturas.getTimbrado());
                }
                if (facturas.getRazonSocial() != null) {
                    existingFacturas.setRazonSocial(facturas.getRazonSocial());
                }
                if (facturas.getRuc() != null) {
                    existingFacturas.setRuc(facturas.getRuc());
                }
                if (facturas.getCondicionVenta() != null) {
                    existingFacturas.setCondicionVenta(facturas.getCondicionVenta());
                }
                if (facturas.getTotal() != null) {
                    existingFacturas.setTotal(facturas.getTotal());
                }

                return existingFacturas;
            })
            .map(facturasRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Facturas> findAll(Pageable pageable) {
        log.debug("Request to get all Facturas");
        return facturasRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Facturas> findOne(Long id) {
        log.debug("Request to get Facturas : {}", id);
        return facturasRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Facturas : {}", id);
        facturasRepository.deleteById(id);
    }
}
