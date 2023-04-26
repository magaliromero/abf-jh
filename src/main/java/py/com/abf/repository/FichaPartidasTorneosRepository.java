package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.FichaPartidasTorneos;

/**
 * Spring Data JPA repository for the FichaPartidasTorneos entity.
 */
@Repository
public interface FichaPartidasTorneosRepository
    extends JpaRepository<FichaPartidasTorneos, Long>, JpaSpecificationExecutor<FichaPartidasTorneos> {
    default Optional<FichaPartidasTorneos> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FichaPartidasTorneos> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FichaPartidasTorneos> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct fichaPartidasTorneos from FichaPartidasTorneos fichaPartidasTorneos left join fetch fichaPartidasTorneos.torneos left join fetch fichaPartidasTorneos.alumnos",
        countQuery = "select count(distinct fichaPartidasTorneos) from FichaPartidasTorneos fichaPartidasTorneos"
    )
    Page<FichaPartidasTorneos> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct fichaPartidasTorneos from FichaPartidasTorneos fichaPartidasTorneos left join fetch fichaPartidasTorneos.torneos left join fetch fichaPartidasTorneos.alumnos"
    )
    List<FichaPartidasTorneos> findAllWithToOneRelationships();

    @Query(
        "select fichaPartidasTorneos from FichaPartidasTorneos fichaPartidasTorneos left join fetch fichaPartidasTorneos.torneos left join fetch fichaPartidasTorneos.alumnos where fichaPartidasTorneos.id =:id"
    )
    Optional<FichaPartidasTorneos> findOneWithToOneRelationships(@Param("id") Long id);
}
