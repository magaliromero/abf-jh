package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Inscripciones;

/**
 * Spring Data JPA repository for the Inscripciones entity.
 */
@Repository
public interface InscripcionesRepository extends JpaRepository<Inscripciones, Long>, JpaSpecificationExecutor<Inscripciones> {
    default Optional<Inscripciones> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Inscripciones> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Inscripciones> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct inscripciones from Inscripciones inscripciones left join fetch inscripciones.alumnos left join fetch inscripciones.cursos",
        countQuery = "select count(distinct inscripciones) from Inscripciones inscripciones"
    )
    Page<Inscripciones> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct inscripciones from Inscripciones inscripciones left join fetch inscripciones.alumnos left join fetch inscripciones.cursos"
    )
    List<Inscripciones> findAllWithToOneRelationships();

    @Query(
        "select inscripciones from Inscripciones inscripciones left join fetch inscripciones.alumnos left join fetch inscripciones.cursos where inscripciones.id =:id"
    )
    Optional<Inscripciones> findOneWithToOneRelationships(@Param("id") Long id);
}
