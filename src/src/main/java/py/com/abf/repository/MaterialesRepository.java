package py.com.abf.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Materiales;

/**
 * Spring Data JPA repository for the Materiales entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialesRepository extends JpaRepository<Materiales, Long>, JpaSpecificationExecutor<Materiales> {}
