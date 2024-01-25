
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.Answer;
import hu.kaoszkviz.server.api.model.AnswerId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AnswerRepository extends CrudRepository<Answer, AnswerId> {
    
}
