package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.RegistroClases;
import py.com.abf.repository.RegistroClasesRepository;
import py.com.abf.service.RegistroClasesService;

/**
 * Service Implementation for managing {@link RegistroClases}.
 */
@Service
@Transactional
public class RegistroClasesServiceImpl implements RegistroClasesService {

    private final Logger log = LoggerFactory.getLogger(RegistroClasesServiceImpl.class);

    private final RegistroClasesRepository registroClasesRepository;

    public RegistroClasesServiceImpl(RegistroClasesRepository registroClasesRepository) {
        this.registroClasesRepository = registroClasesRepository;
    }

    @Override
    public RegistroClases save(RegistroClases registroClases) {
        log.debug("Request to save RegistroClases : {}", registroClases);
        return registroClasesRepository.save(registroClases);
    }

    @Override
    public RegistroClases update(RegistroClases registroClases) {
        log.debug("Request to update RegistroClases : {}", registroClases);
        return registroClasesRepository.save(registroClases);
    }

    @Override
    public Optional<RegistroClases> partialUpdate(RegistroClases registroClases) {
        log.debug("Request to partially update RegistroClases : {}", registroClases);

        return registroClasesRepository
            .findById(registroClases.getId())
            .map(existingRegistroClases -> {
                if (registroClases.getFecha() != null) {
                    existingRegistroClases.setFecha(registroClases.getFecha());
                }
                if (registroClases.getCantidadHoras() != null) {
                    existingRegistroClases.setCantidadHoras(registroClases.getCantidadHoras());
                }
                if (registroClases.getAsistenciaAlumno() != null) {
                    existingRegistroClases.setAsistenciaAlumno(registroClases.getAsistenciaAlumno());
                }

                return existingRegistroClases;
            })
            .map(registroClasesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegistroClases> findAll(Pageable pageable) {
        log.debug("Request to get all RegistroClases");
        return registroClasesRepository.findAll(pageable);
    }

    public Page<RegistroClases> findAllWithEagerRelationships(Pageable pageable) {
        return registroClasesRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RegistroClases> findOne(Long id) {
        log.debug("Request to get RegistroClases : {}", id);
        return registroClasesRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RegistroClases : {}", id);
        registroClasesRepository.deleteById(id);
    }
}
