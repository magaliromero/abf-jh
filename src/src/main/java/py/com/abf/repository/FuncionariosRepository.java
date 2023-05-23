package py.com.abf.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.Funcionarios;

/**
 * Spring Data JPA repository for the Funcionarios entity.
 */
@Repository
public interface FuncionariosRepository extends JpaRepository<Funcionarios, Long>, JpaSpecificationExecutor<Funcionarios> {
    default Optional<Funcionarios> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Funcionarios> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Funcionarios> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct funcionarios from Funcionarios funcionarios left join fetch funcionarios.tipoDocumentos",
        countQuery = "select count(distinct funcionarios) from Funcionarios funcionarios"
    )
    Page<Funcionarios> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct funcionarios from Funcionarios funcionarios left join fetch funcionarios.tipoDocumentos")
    List<Funcionarios> findAllWithToOneRelationships();

    @Query("select funcionarios from Funcionarios funcionarios left join fetch funcionarios.tipoDocumentos where funcionarios.id =:id")
    Optional<Funcionarios> findOneWithToOneRelationships(@Param("id") Long id);
}
