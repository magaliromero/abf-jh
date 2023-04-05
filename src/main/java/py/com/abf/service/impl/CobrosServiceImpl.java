package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Cobros;
import py.com.abf.repository.CobrosRepository;
import py.com.abf.service.CobrosService;

/**
 * Service Implementation for managing {@link Cobros}.
 */
@Service
@Transactional
public class CobrosServiceImpl implements CobrosService {

    private final Logger log = LoggerFactory.getLogger(CobrosServiceImpl.class);

    private final CobrosRepository cobrosRepository;

    public CobrosServiceImpl(CobrosRepository cobrosRepository) {
        this.cobrosRepository = cobrosRepository;
    }

    @Override
    public Cobros save(Cobros cobros) {
        log.debug("Request to save Cobros : {}", cobros);
        return cobrosRepository.save(cobros);
    }

    @Override
    public Cobros update(Cobros cobros) {
        log.debug("Request to update Cobros : {}", cobros);
        return cobrosRepository.save(cobros);
    }

    @Override
    public Optional<Cobros> partialUpdate(Cobros cobros) {
        log.debug("Request to partially update Cobros : {}", cobros);

        return cobrosRepository
            .findById(cobros.getId())
            .map(existingCobros -> {
                if (cobros.getMontoPago() != null) {
                    existingCobros.setMontoPago(cobros.getMontoPago());
                }
                if (cobros.getMontoInicial() != null) {
                    existingCobros.setMontoInicial(cobros.getMontoInicial());
                }
                if (cobros.getSaldo() != null) {
                    existingCobros.setSaldo(cobros.getSaldo());
                }
                if (cobros.getFechaRegistro() != null) {
                    existingCobros.setFechaRegistro(cobros.getFechaRegistro());
                }
                if (cobros.getFechaPago() != null) {
                    existingCobros.setFechaPago(cobros.getFechaPago());
                }
                if (cobros.getTipoPago() != null) {
                    existingCobros.setTipoPago(cobros.getTipoPago());
                }
                if (cobros.getDescripcion() != null) {
                    existingCobros.setDescripcion(cobros.getDescripcion());
                }

                return existingCobros;
            })
            .map(cobrosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cobros> findAll(Pageable pageable) {
        log.debug("Request to get all Cobros");
        return cobrosRepository.findAll(pageable);
    }

    public Page<Cobros> findAllWithEagerRelationships(Pageable pageable) {
        return cobrosRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cobros> findOne(Long id) {
        log.debug("Request to get Cobros : {}", id);
        return cobrosRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cobros : {}", id);
        cobrosRepository.deleteById(id);
    }
}
