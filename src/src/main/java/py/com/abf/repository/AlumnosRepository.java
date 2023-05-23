package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Alumnos;

/**
 * Spring Data JPA repository for the Alumnos entity.
 */
@Repository
public interface AlumnosRepository extends JpaRepository<Alumnos, Long>, JpaSpecificationExecutor<Alumnos> {
    default Optional<Alumnos> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Alumnos> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Alumnos> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct alumnos from Alumnos alumnos left join fetch alumnos.tipoDocumentos",
        countQuery = "select count(distinct alumnos) from Alumnos alumnos"
    )
    Page<Alumnos> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct alumnos from Alumnos alumnos left join fetch alumnos.tipoDocumentos")
    List<Alumnos> findAllWithToOneRelationships();

    @Query("select alumnos from Alumnos alumnos left join fetch alumnos.tipoDocumentos where alumnos.id =:id")
    Optional<Alumnos> findOneWithToOneRelationships(@Param("id") Long id);
}
