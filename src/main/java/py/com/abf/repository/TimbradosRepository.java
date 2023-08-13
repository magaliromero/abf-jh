package py.com.abf.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Timbrados;

/**
 * Spring Data JPA repository for the Timbrados entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimbradosRepository extends JpaRepository<Timbrados, Long>, JpaSpecificationExecutor<Timbrados> {}
