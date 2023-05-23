package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Evaluaciones;

/**
 * Spring Data JPA repository for the Evaluaciones entity.
 */
@Repository
public interface EvaluacionesRepository extends JpaRepository<Evaluaciones, Long>, JpaSpecificationExecutor<Evaluaciones> {
    default Optional<Evaluaciones> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Evaluaciones> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Evaluaciones> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct evaluaciones from Evaluaciones evaluaciones left join fetch evaluaciones.alumnos left join fetch evaluaciones.funcionarios",
        countQuery = "select count(distinct evaluaciones) from Evaluaciones evaluaciones"
    )
    Page<Evaluaciones> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct evaluaciones from Evaluaciones evaluaciones left join fetch evaluaciones.alumnos left join fetch evaluaciones.funcionarios"
    )
    List<Evaluaciones> findAllWithToOneRelationships();

    @Query(
        "select evaluaciones from Evaluaciones evaluaciones left join fetch evaluaciones.alumnos left join fetch evaluaciones.funcionarios where evaluaciones.id =:id"
    )
    Optional<Evaluaciones> findOneWithToOneRelationships(@Param("id") Long id);
}
