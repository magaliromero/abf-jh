package py.com.abf.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import py.com.abf.domain.TiposDocumentos;

/**
 * Spring Data JPA repository for the TiposDocumentos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TiposDocumentosRepository extends JpaRepository<TiposDocumentos, Long>, JpaSpecificationExecutor<TiposDocumentos> {}
