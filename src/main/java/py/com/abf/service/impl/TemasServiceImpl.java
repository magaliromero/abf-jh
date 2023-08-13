package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Temas;
import py.com.abf.repository.TemasRepository;
import py.com.abf.service.TemasService;

/**
 * Service Implementation for managing {@link Temas}.
 */
@Service
@Transactional
public class TemasServiceImpl implements TemasService {

    private final Logger log = LoggerFactory.getLogger(TemasServiceImpl.class);

    private final TemasRepository temasRepository;

    public TemasServiceImpl(TemasRepository temasRepository) {
        this.temasRepository = temasRepository;
    }

    @Override
    public Temas save(Temas temas) {
        log.debug("Request to save Temas : {}", temas);
        return temasRepository.save(temas);
    }

    @Override
    public Temas update(Temas temas) {
        log.debug("Request to update Temas : {}", temas);
        return temasRepository.save(temas);
    }

    @Override
    public Optional<Temas> partialUpdate(Temas temas) {
        log.debug("Request to partially update Temas : {}", temas);

        return temasRepository
            .findById(temas.getId())
            .map(existingTemas -> {
                if (temas.getTitulo() != null) {
                    existingTemas.setTitulo(temas.getTitulo());
                }
                if (temas.getDescripcion() != null) {
                    existingTemas.setDescripcion(temas.getDescripcion());
                }

                return existingTemas;
            })
            .map(temasRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Temas> findAll(Pageable pageable) {
        log.debug("Request to get all Temas");
        return temasRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Temas> findOne(Long id) {
        log.debug("Request to get Temas : {}", id);
        return temasRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Temas : {}", id);
        temasRepository.deleteById(id);
    }
}
