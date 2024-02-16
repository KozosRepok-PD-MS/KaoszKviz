
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.Answer;
import hu.kaoszkviz.server.api.model.AnswerId;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AnswerRepository extends CrudRepository<Answer, AnswerId> {
    
    @Query(value = "SELECT * FROM dbo.findAnswerByQuestionId(:search_id)", nativeQuery = true)
    public List<Answer> serachByQuestionId(@Param("search_id") long searchId);
    
    @Query(value = "SELECT * FROM dbo.findAnswerByQuestionIdAndOrdinalNumber(:search_id, :ordinal_number)", nativeQuery = true)
    public Answer serachByQuestionIdAndOrdinalNumber(@Param("search_id") long searchId, @Param("ordinal_number") byte ordinalNumber);
    
    @Procedure("deleteAnswerByQuestionIdAndOrdinalNumber")
    public void deleteByQuestionIdAndOrdinalNumber(@Param("question_id") long questionId, @Param("ordinal_number") byte ordinalNumber);
 }
