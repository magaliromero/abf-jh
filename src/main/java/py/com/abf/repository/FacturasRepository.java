package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Facturas;

/**
 * Spring Data JPA repository for the Facturas entity.
 */
@Repository
public interface FacturasRepository extends JpaRepository<Facturas, Long>, JpaSpecificationExecutor<Facturas> {
    default Optional<Facturas> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Facturas> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Facturas> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct facturas from Facturas facturas left join fetch facturas.clientes",
        countQuery = "select count(distinct facturas) from Facturas facturas"
    )
    Page<Facturas> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct facturas from Facturas facturas left join fetch facturas.clientes")
    List<Facturas> findAllWithToOneRelationships();

    @Query("select facturas from Facturas facturas left join fetch facturas.clientes where facturas.id =:id")
    Optional<Facturas> findOneWithToOneRelationships(@Param("id") Long id);
}
