package py.com.abf.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Sucursales;

/**
 * Spring Data JPA repository for the Sucursales entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SucursalesRepository extends JpaRepository<Sucursales, Long>, JpaSpecificationExecutor<Sucursales> {}
