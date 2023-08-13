package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Cursos;

/**
 * Spring Data JPA repository for the Cursos entity.
 */
@Repository
public interface CursosRepository extends JpaRepository<Cursos, Long>, JpaSpecificationExecutor<Cursos> {
    default Optional<Cursos> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Cursos> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Cursos> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct cursos from Cursos cursos left join fetch cursos.temas",
        countQuery = "select count(distinct cursos) from Cursos cursos"
    )
    Page<Cursos> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct cursos from Cursos cursos left join fetch cursos.temas")
    List<Cursos> findAllWithToOneRelationships();

    @Query("select cursos from Cursos cursos left join fetch cursos.temas where cursos.id =:id")
    Optional<Cursos> findOneWithToOneRelationships(@Param("id") Long id);
}
