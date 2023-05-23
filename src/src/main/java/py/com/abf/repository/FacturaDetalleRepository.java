package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.FacturaDetalle;

/**
 * Spring Data JPA repository for the FacturaDetalle entity.
 */
@Repository
public interface FacturaDetalleRepository extends JpaRepository<FacturaDetalle, Long>, JpaSpecificationExecutor<FacturaDetalle> {
    default Optional<FacturaDetalle> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FacturaDetalle> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FacturaDetalle> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct facturaDetalle from FacturaDetalle facturaDetalle left join fetch facturaDetalle.producto left join fetch facturaDetalle.factura",
        countQuery = "select count(distinct facturaDetalle) from FacturaDetalle facturaDetalle"
    )
    Page<FacturaDetalle> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct facturaDetalle from FacturaDetalle facturaDetalle left join fetch facturaDetalle.producto left join fetch facturaDetalle.factura"
    )
    List<FacturaDetalle> findAllWithToOneRelationships();

    @Query(
        "select facturaDetalle from FacturaDetalle facturaDetalle left join fetch facturaDetalle.producto left join fetch facturaDetalle.factura where facturaDetalle.id =:id"
    )
    Optional<FacturaDetalle> findOneWithToOneRelationships(@Param("id") Long id);
}
