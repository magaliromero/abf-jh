package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Clientes;
import py.com.abf.repository.ClientesRepository;
import py.com.abf.service.ClientesService;

/**
 * Service Implementation for managing {@link Clientes}.
 */
@Service
@Transactional
public class ClientesServiceImpl implements ClientesService {

    private final Logger log = LoggerFactory.getLogger(ClientesServiceImpl.class);

    private final ClientesRepository clientesRepository;

    public ClientesServiceImpl(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    @Override
    public Clientes save(Clientes clientes) {
        log.debug("Request to save Clientes : {}", clientes);
        return clientesRepository.save(clientes);
    }

    @Override
    public Clientes update(Clientes clientes) {
        log.debug("Request to update Clientes : {}", clientes);
        return clientesRepository.save(clientes);
    }

    @Override
    public Optional<Clientes> partialUpdate(Clientes clientes) {
        log.debug("Request to partially update Clientes : {}", clientes);

        return clientesRepository
            .findById(clientes.getId())
            .map(existingClientes -> {
                if (clientes.getRuc() != null) {
                    existingClientes.setRuc(clientes.getRuc());
                }
                if (clientes.getNombres() != null) {
                    existingClientes.setNombres(clientes.getNombres());
                }
                if (clientes.getApellidos() != null) {
                    existingClientes.setApellidos(clientes.getApellidos());
                }
                if (clientes.getRazonSocial() != null) {
                    existingClientes.setRazonSocial(clientes.getRazonSocial());
                }
                if (clientes.getDocumento() != null) {
                    existingClientes.setDocumento(clientes.getDocumento());
                }
                if (clientes.getEmail() != null) {
                    existingClientes.setEmail(clientes.getEmail());
                }
                if (clientes.getTelefono() != null) {
                    existingClientes.setTelefono(clientes.getTelefono());
                }
                if (clientes.getFechaNacimiento() != null) {
                    existingClientes.setFechaNacimiento(clientes.getFechaNacimiento());
                }
                if (clientes.getDireccion() != null) {
                    existingClientes.setDireccion(clientes.getDireccion());
                }

                return existingClientes;
            })
            .map(clientesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Clientes> findAll(Pageable pageable) {
        log.debug("Request to get all Clientes");
        return clientesRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Clientes> findOne(Long id) {
        log.debug("Request to get Clientes : {}", id);
        return clientesRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Clientes : {}", id);
        clientesRepository.deleteById(id);
    }
}
