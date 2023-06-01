package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Temas;

/**
 * Spring Data JPA repository for the Temas entity.
 */
@Repository
public interface TemasRepository extends JpaRepository<Temas, Long>, JpaSpecificationExecutor<Temas> {
    default Optional<Temas> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Temas> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Temas> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct temas from Temas temas left join fetch temas.cursos",
        countQuery = "select count(distinct temas) from Temas temas"
    )
    Page<Temas> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct temas from Temas temas left join fetch temas.cursos")
    List<Temas> findAllWithToOneRelationships();

    @Query("select temas from Temas temas left join fetch temas.cursos where temas.id =:id")
    Optional<Temas> findOneWithToOneRelationships(@Param("id") Long id);
}
