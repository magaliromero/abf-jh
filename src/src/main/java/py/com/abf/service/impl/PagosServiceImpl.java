package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Pagos;
import py.com.abf.repository.PagosRepository;
import py.com.abf.service.PagosService;

/**
 * Service Implementation for managing {@link Pagos}.
 */
@Service
@Transactional
public class PagosServiceImpl implements PagosService {

    private final Logger log = LoggerFactory.getLogger(PagosServiceImpl.class);

    private final PagosRepository pagosRepository;

    public PagosServiceImpl(PagosRepository pagosRepository) {
        this.pagosRepository = pagosRepository;
    }

    @Override
    public Pagos save(Pagos pagos) {
        log.debug("Request to save Pagos : {}", pagos);
        return pagosRepository.save(pagos);
    }

    @Override
    public Pagos update(Pagos pagos) {
        log.debug("Request to update Pagos : {}", pagos);
        return pagosRepository.save(pagos);
    }

    @Override
    public Optional<Pagos> partialUpdate(Pagos pagos) {
        log.debug("Request to partially update Pagos : {}", pagos);

        return pagosRepository
            .findById(pagos.getId())
            .map(existingPagos -> {
                if (pagos.getFecha() != null) {
                    existingPagos.setFecha(pagos.getFecha());
                }
                if (pagos.getTotal() != null) {
                    existingPagos.setTotal(pagos.getTotal());
                }
                if (pagos.getCantidadHoras() != null) {
                    existingPagos.setCantidadHoras(pagos.getCantidadHoras());
                }

                return existingPagos;
            })
            .map(pagosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pagos> findAll(Pageable pageable) {
        log.debug("Request to get all Pagos");
        return pagosRepository.findAll(pageable);
    }

    public Page<Pagos> findAllWithEagerRelationships(Pageable pageable) {
        return pagosRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pagos> findOne(Long id) {
        log.debug("Request to get Pagos : {}", id);
        return pagosRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pagos : {}", id);
        pagosRepository.deleteById(id);
    }
}
