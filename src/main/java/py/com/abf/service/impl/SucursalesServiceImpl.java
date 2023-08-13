package py.com.abf.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Sucursales;
import py.com.abf.repository.SucursalesRepository;
import py.com.abf.service.SucursalesService;

/**
 * Service Implementation for managing {@link Sucursales}.
 */
@Service
@Transactional
public class SucursalesServiceImpl implements SucursalesService {

    private final Logger log = LoggerFactory.getLogger(SucursalesServiceImpl.class);

    private final SucursalesRepository sucursalesRepository;

    public SucursalesServiceImpl(SucursalesRepository sucursalesRepository) {
        this.sucursalesRepository = sucursalesRepository;
    }

    @Override
    public Sucursales save(Sucursales sucursales) {
        log.debug("Request to save Sucursales : {}", sucursales);
        return sucursalesRepository.save(sucursales);
    }

    @Override
    public Sucursales update(Sucursales sucursales) {
        log.debug("Request to update Sucursales : {}", sucursales);
        return sucursalesRepository.save(sucursales);
    }

    @Override
    public Optional<Sucursales> partialUpdate(Sucursales sucursales) {
        log.debug("Request to partially update Sucursales : {}", sucursales);

        return sucursalesRepository
            .findById(sucursales.getId())
            .map(existingSucursales -> {
                if (sucursales.getNombreSucursal() != null) {
                    existingSucursales.setNombreSucursal(sucursales.getNombreSucursal());
                }
                if (sucursales.getDireccion() != null) {
                    existingSucursales.setDireccion(sucursales.getDireccion());
                }
                if (sucursales.getNumeroEstablecimiento() != null) {
                    existingSucursales.setNumeroEstablecimiento(sucursales.getNumeroEstablecimiento());
                }

                return existingSucursales;
            })
            .map(sucursalesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sucursales> findAll(Pageable pageable) {
        log.debug("Request to get all Sucursales");
        return sucursalesRepository.findAll(pageable);
    }

    /**
     *  Get all the sucursales where Timbrados is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Sucursales> findAllWhereTimbradosIsNull() {
        log.debug("Request to get all sucursales where Timbrados is null");
        return StreamSupport
            .stream(sucursalesRepository.findAll().spliterator(), false)
            .filter(sucursales -> sucursales.getTimbrados() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sucursales> findOne(Long id) {
        log.debug("Request to get Sucursales : {}", id);
        return sucursalesRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sucursales : {}", id);
        sucursalesRepository.deleteById(id);
    }
}
