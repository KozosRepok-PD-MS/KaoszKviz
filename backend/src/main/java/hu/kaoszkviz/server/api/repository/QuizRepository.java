
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.Quiz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizRepository extends CrudRepository<Quiz, Long> {
    
}
