package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Matricula;

/**
 * Spring Data JPA repository for the Matricula entity.
 */
@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long>, JpaSpecificationExecutor<Matricula> {
    default Optional<Matricula> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Matricula> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Matricula> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct matricula from Matricula matricula left join fetch matricula.alumno",
        countQuery = "select count(distinct matricula) from Matricula matricula"
    )
    Page<Matricula> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct matricula from Matricula matricula left join fetch matricula.alumno")
    List<Matricula> findAllWithToOneRelationships();

    @Query("select matricula from Matricula matricula left join fetch matricula.alumno where matricula.id =:id")
    Optional<Matricula> findOneWithToOneRelationships(@Param("id") Long id);
}
