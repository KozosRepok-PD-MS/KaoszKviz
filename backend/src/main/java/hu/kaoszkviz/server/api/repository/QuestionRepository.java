
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
    
    
}
