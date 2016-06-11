package practice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ogawa, Takeshi
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
