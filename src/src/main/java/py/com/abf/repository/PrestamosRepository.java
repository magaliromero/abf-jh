package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Prestamos;

/**
 * Spring Data JPA repository for the Prestamos entity.
 */
@Repository
public interface PrestamosRepository extends JpaRepository<Prestamos, Long>, JpaSpecificationExecutor<Prestamos> {
    default Optional<Prestamos> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Prestamos> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Prestamos> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct prestamos from Prestamos prestamos left join fetch prestamos.materiales left join fetch prestamos.alumnos",
        countQuery = "select count(distinct prestamos) from Prestamos prestamos"
    )
    Page<Prestamos> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct prestamos from Prestamos prestamos left join fetch prestamos.materiales left join fetch prestamos.alumnos")
    List<Prestamos> findAllWithToOneRelationships();

    @Query(
        "select prestamos from Prestamos prestamos left join fetch prestamos.materiales left join fetch prestamos.alumnos where prestamos.id =:id"
    )
    Optional<Prestamos> findOneWithToOneRelationships(@Param("id") Long id);
}
