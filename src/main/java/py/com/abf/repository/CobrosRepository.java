package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Cobros;

/**
 * Spring Data JPA repository for the Cobros entity.
 */
@Repository
public interface CobrosRepository extends JpaRepository<Cobros, Long>, JpaSpecificationExecutor<Cobros> {
    default Optional<Cobros> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Cobros> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Cobros> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct cobros from Cobros cobros left join fetch cobros.alumnos",
        countQuery = "select count(distinct cobros) from Cobros cobros"
    )
    Page<Cobros> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct cobros from Cobros cobros left join fetch cobros.alumnos")
    List<Cobros> findAllWithToOneRelationships();

    @Query("select cobros from Cobros cobros left join fetch cobros.alumnos where cobros.id =:id")
    Optional<Cobros> findOneWithToOneRelationships(@Param("id") Long id);
}
