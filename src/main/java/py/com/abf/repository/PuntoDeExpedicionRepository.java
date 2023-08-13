package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.PuntoDeExpedicion;

/**
 * Spring Data JPA repository for the PuntoDeExpedicion entity.
 */
@Repository
public interface PuntoDeExpedicionRepository extends JpaRepository<PuntoDeExpedicion, Long>, JpaSpecificationExecutor<PuntoDeExpedicion> {
    default Optional<PuntoDeExpedicion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PuntoDeExpedicion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PuntoDeExpedicion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct puntoDeExpedicion from PuntoDeExpedicion puntoDeExpedicion left join fetch puntoDeExpedicion.sucursales",
        countQuery = "select count(distinct puntoDeExpedicion) from PuntoDeExpedicion puntoDeExpedicion"
    )
    Page<PuntoDeExpedicion> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct puntoDeExpedicion from PuntoDeExpedicion puntoDeExpedicion left join fetch puntoDeExpedicion.sucursales")
    List<PuntoDeExpedicion> findAllWithToOneRelationships();

    @Query(
        "select puntoDeExpedicion from PuntoDeExpedicion puntoDeExpedicion left join fetch puntoDeExpedicion.sucursales where puntoDeExpedicion.id =:id"
    )
    Optional<PuntoDeExpedicion> findOneWithToOneRelationships(@Param("id") Long id);
}
