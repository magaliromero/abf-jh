package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Pagos;

/**
 * Spring Data JPA repository for the Pagos entity.
 */
@Repository
public interface PagosRepository extends JpaRepository<Pagos, Long>, JpaSpecificationExecutor<Pagos> {
    default Optional<Pagos> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Pagos> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Pagos> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct pagos from Pagos pagos left join fetch pagos.producto left join fetch pagos.funcionario",
        countQuery = "select count(distinct pagos) from Pagos pagos"
    )
    Page<Pagos> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct pagos from Pagos pagos left join fetch pagos.producto left join fetch pagos.funcionario")
    List<Pagos> findAllWithToOneRelationships();

    @Query("select pagos from Pagos pagos left join fetch pagos.producto left join fetch pagos.funcionario where pagos.id =:id")
    Optional<Pagos> findOneWithToOneRelationships(@Param("id") Long id);
}
