package practice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	User findOneByTenantIdAndUsername(String tenantId, String username);
}
