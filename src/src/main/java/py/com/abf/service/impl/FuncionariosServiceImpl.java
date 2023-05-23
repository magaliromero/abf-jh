package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Funcionarios;
import py.com.abf.repository.FuncionariosRepository;
import py.com.abf.service.FuncionariosService;

/**
 * Service Implementation for managing {@link Funcionarios}.
 */
@Service
@Transactional
public class FuncionariosServiceImpl implements FuncionariosService {

    private final Logger log = LoggerFactory.getLogger(FuncionariosServiceImpl.class);

    private final FuncionariosRepository funcionariosRepository;

    public FuncionariosServiceImpl(FuncionariosRepository funcionariosRepository) {
        this.funcionariosRepository = funcionariosRepository;
    }

    @Override
    public Funcionarios save(Funcionarios funcionarios) {
        log.debug("Request to save Funcionarios : {}", funcionarios);
        return funcionariosRepository.save(funcionarios);
    }

    @Override
    public Funcionarios update(Funcionarios funcionarios) {
        log.debug("Request to update Funcionarios : {}", funcionarios);
        return funcionariosRepository.save(funcionarios);
    }

    @Override
    public Optional<Funcionarios> partialUpdate(Funcionarios funcionarios) {
        log.debug("Request to partially update Funcionarios : {}", funcionarios);

        return funcionariosRepository
            .findById(funcionarios.getId())
            .map(existingFuncionarios -> {
                if (funcionarios.getElo() != null) {
                    existingFuncionarios.setElo(funcionarios.getElo());
                }
                if (funcionarios.getFideId() != null) {
                    existingFuncionarios.setFideId(funcionarios.getFideId());
                }
                if (funcionarios.getNombres() != null) {
                    existingFuncionarios.setNombres(funcionarios.getNombres());
                }
                if (funcionarios.getApellidos() != null) {
                    existingFuncionarios.setApellidos(funcionarios.getApellidos());
                }
                if (funcionarios.getNombreCompleto() != null) {
                    existingFuncionarios.setNombreCompleto(funcionarios.getNombreCompleto());
                }
                if (funcionarios.getEmail() != null) {
                    existingFuncionarios.setEmail(funcionarios.getEmail());
                }
                if (funcionarios.getTelefono() != null) {
                    existingFuncionarios.setTelefono(funcionarios.getTelefono());
                }
                if (funcionarios.getFechaNacimiento() != null) {
                    existingFuncionarios.setFechaNacimiento(funcionarios.getFechaNacimiento());
                }
                if (funcionarios.getDocumento() != null) {
                    existingFuncionarios.setDocumento(funcionarios.getDocumento());
                }
                if (funcionarios.getEstado() != null) {
                    existingFuncionarios.setEstado(funcionarios.getEstado());
                }
                if (funcionarios.getTipoFuncionario() != null) {
                    existingFuncionarios.setTipoFuncionario(funcionarios.getTipoFuncionario());
                }

                return existingFuncionarios;
            })
            .map(funcionariosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Funcionarios> findAll(Pageable pageable) {
        log.debug("Request to get all Funcionarios");
        return funcionariosRepository.findAll(pageable);
    }

    public Page<Funcionarios> findAllWithEagerRelationships(Pageable pageable) {
        return funcionariosRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Funcionarios> findOne(Long id) {
        log.debug("Request to get Funcionarios : {}", id);
        return funcionariosRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Funcionarios : {}", id);
        funcionariosRepository.deleteById(id);
    }
}
