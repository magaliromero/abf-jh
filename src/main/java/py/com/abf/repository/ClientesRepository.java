package py.com.abf.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Clientes;

/**
 * Spring Data JPA repository for the Clientes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientesRepository extends JpaRepository<Clientes, Long>, JpaSpecificationExecutor<Clientes> {}
