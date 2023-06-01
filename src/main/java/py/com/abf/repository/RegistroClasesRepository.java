package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.RegistroClases;

/**
 * Spring Data JPA repository for the RegistroClases entity.
 */
@Repository
public interface RegistroClasesRepository extends JpaRepository<RegistroClases, Long>, JpaSpecificationExecutor<RegistroClases> {
    default Optional<RegistroClases> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<RegistroClases> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<RegistroClases> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct registroClases from RegistroClases registroClases left join fetch registroClases.temas left join fetch registroClases.funcionario left join fetch registroClases.alumnos",
        countQuery = "select count(distinct registroClases) from RegistroClases registroClases"
    )
    Page<RegistroClases> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct registroClases from RegistroClases registroClases left join fetch registroClases.temas left join fetch registroClases.funcionario left join fetch registroClases.alumnos"
    )
    List<RegistroClases> findAllWithToOneRelationships();

    @Query(
        "select registroClases from RegistroClases registroClases left join fetch registroClases.temas left join fetch registroClases.funcionario left join fetch registroClases.alumnos where registroClases.id =:id"
    )
    Optional<RegistroClases> findOneWithToOneRelationships(@Param("id") Long id);
}
