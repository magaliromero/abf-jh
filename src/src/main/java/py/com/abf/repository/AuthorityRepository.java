package py.com.abf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import py.com.abf.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
