
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.Answer;
import hu.kaoszkviz.server.api.model.AnswerId;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AnswerRepository extends CrudRepository<Answer, AnswerId> {
    
    @Query(value = "SELECT * FROM dbo.findAnswerByQuestionId(:search_id)", nativeQuery = true)
    public List<Answer> serachByQuestionId(@Param("search_id") long searchId);
    
    @Query(value = "SELECT COUNT(*) FROM answer WHERE question_id=:question_id", nativeQuery = true)
    public byte countAnswersForQuestion(@Param("question_id") long searchId);
 }
