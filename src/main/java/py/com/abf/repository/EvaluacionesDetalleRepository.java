package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.EvaluacionesDetalle;

/**
 * Spring Data JPA repository for the EvaluacionesDetalle entity.
 */
@Repository
public interface EvaluacionesDetalleRepository
    extends JpaRepository<EvaluacionesDetalle, Long>, JpaSpecificationExecutor<EvaluacionesDetalle> {
    default Optional<EvaluacionesDetalle> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EvaluacionesDetalle> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EvaluacionesDetalle> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct evaluacionesDetalle from EvaluacionesDetalle evaluacionesDetalle left join fetch evaluacionesDetalle.evaluaciones left join fetch evaluacionesDetalle.temas",
        countQuery = "select count(distinct evaluacionesDetalle) from EvaluacionesDetalle evaluacionesDetalle"
    )
    Page<EvaluacionesDetalle> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct evaluacionesDetalle from EvaluacionesDetalle evaluacionesDetalle left join fetch evaluacionesDetalle.evaluaciones left join fetch evaluacionesDetalle.temas"
    )
    List<EvaluacionesDetalle> findAllWithToOneRelationships();

    @Query(
        "select evaluacionesDetalle from EvaluacionesDetalle evaluacionesDetalle left join fetch evaluacionesDetalle.evaluaciones left join fetch evaluacionesDetalle.temas where evaluacionesDetalle.id =:id"
    )
    Optional<EvaluacionesDetalle> findOneWithToOneRelationships(@Param("id") Long id);
}
