package py.com.abf.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Temas;

/**
 * Spring Data JPA repository for the Temas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TemasRepository extends JpaRepository<Temas, Long>, JpaSpecificationExecutor<Temas> {}
