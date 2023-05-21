package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Materiales;
import py.com.abf.repository.MaterialesRepository;
import py.com.abf.service.MaterialesService;

/**
 * Service Implementation for managing {@link Materiales}.
 */
@Service
@Transactional
public class MaterialesServiceImpl implements MaterialesService {

    private final Logger log = LoggerFactory.getLogger(MaterialesServiceImpl.class);

    private final MaterialesRepository materialesRepository;

    public MaterialesServiceImpl(MaterialesRepository materialesRepository) {
        this.materialesRepository = materialesRepository;
    }

    @Override
    public Materiales save(Materiales materiales) {
        log.debug("Request to save Materiales : {}", materiales);
        return materialesRepository.save(materiales);
    }

    @Override
    public Materiales update(Materiales materiales) {
        log.debug("Request to update Materiales : {}", materiales);
        return materialesRepository.save(materiales);
    }

    @Override
    public Optional<Materiales> partialUpdate(Materiales materiales) {
        log.debug("Request to partially update Materiales : {}", materiales);

        return materialesRepository
            .findById(materiales.getId())
            .map(existingMateriales -> {
                if (materiales.getDescripcion() != null) {
                    existingMateriales.setDescripcion(materiales.getDescripcion());
                }
                if (materiales.getCantidad() != null) {
                    existingMateriales.setCantidad(materiales.getCantidad());
                }
                if (materiales.getCantidadEnPrestamo() != null) {
                    existingMateriales.setCantidadEnPrestamo(materiales.getCantidadEnPrestamo());
                }

                return existingMateriales;
            })
            .map(materialesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Materiales> findAll(Pageable pageable) {
        log.debug("Request to get all Materiales");
        return materialesRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Materiales> findOne(Long id) {
        log.debug("Request to get Materiales : {}", id);
        return materialesRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Materiales : {}", id);
        materialesRepository.deleteById(id);
    }
}
