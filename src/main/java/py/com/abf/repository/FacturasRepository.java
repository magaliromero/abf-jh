package py.com.abf.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Facturas;

/**
 * Spring Data JPA repository for the Facturas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacturasRepository extends JpaRepository<Facturas, Long>, JpaSpecificationExecutor<Facturas> {}
