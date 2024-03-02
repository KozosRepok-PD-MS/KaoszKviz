
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.Question;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
    
    @Query(value = "SELECT * FROM dbo.findQuestionByQuizId(:search_id)", nativeQuery = true)
    public List<Question> searchQuestionByQuizId(@Param("search_id") long searchId);
}
