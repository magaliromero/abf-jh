package py.com.abf.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Cursos;

/**
 * Spring Data JPA repository for the Cursos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CursosRepository extends JpaRepository<Cursos, Long>, JpaSpecificationExecutor<Cursos> {}
