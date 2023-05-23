package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Alumnos;
import py.com.abf.repository.AlumnosRepository;
import py.com.abf.service.AlumnosService;

/**
 * Service Implementation for managing {@link Alumnos}.
 */
@Service
@Transactional
public class AlumnosServiceImpl implements AlumnosService {

    private final Logger log = LoggerFactory.getLogger(AlumnosServiceImpl.class);

    private final AlumnosRepository alumnosRepository;

    public AlumnosServiceImpl(AlumnosRepository alumnosRepository) {
        this.alumnosRepository = alumnosRepository;
    }

    @Override
    public Alumnos save(Alumnos alumnos) {
        log.debug("Request to save Alumnos : {}", alumnos);
        return alumnosRepository.save(alumnos);
    }

    @Override
    public Alumnos update(Alumnos alumnos) {
        log.debug("Request to update Alumnos : {}", alumnos);
        return alumnosRepository.save(alumnos);
    }

    @Override
    public Optional<Alumnos> partialUpdate(Alumnos alumnos) {
        log.debug("Request to partially update Alumnos : {}", alumnos);

        return alumnosRepository
            .findById(alumnos.getId())
            .map(existingAlumnos -> {
                if (alumnos.getElo() != null) {
                    existingAlumnos.setElo(alumnos.getElo());
                }
                if (alumnos.getFideId() != null) {
                    existingAlumnos.setFideId(alumnos.getFideId());
                }
                if (alumnos.getNombres() != null) {
                    existingAlumnos.setNombres(alumnos.getNombres());
                }
                if (alumnos.getApellidos() != null) {
                    existingAlumnos.setApellidos(alumnos.getApellidos());
                }
                if (alumnos.getNombreCompleto() != null) {
                    existingAlumnos.setNombreCompleto(alumnos.getNombreCompleto());
                }
                if (alumnos.getEmail() != null) {
                    existingAlumnos.setEmail(alumnos.getEmail());
                }
                if (alumnos.getTelefono() != null) {
                    existingAlumnos.setTelefono(alumnos.getTelefono());
                }
                if (alumnos.getFechaNacimiento() != null) {
                    existingAlumnos.setFechaNacimiento(alumnos.getFechaNacimiento());
                }
                if (alumnos.getDocumento() != null) {
                    existingAlumnos.setDocumento(alumnos.getDocumento());
                }
                if (alumnos.getEstado() != null) {
                    existingAlumnos.setEstado(alumnos.getEstado());
                }

                return existingAlumnos;
            })
            .map(alumnosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Alumnos> findAll(Pageable pageable) {
        log.debug("Request to get all Alumnos");
        return alumnosRepository.findAll(pageable);
    }

    public Page<Alumnos> findAllWithEagerRelationships(Pageable pageable) {
        return alumnosRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Alumnos> findOne(Long id) {
        log.debug("Request to get Alumnos : {}", id);
        return alumnosRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Alumnos : {}", id);
        alumnosRepository.deleteById(id);
    }
}
