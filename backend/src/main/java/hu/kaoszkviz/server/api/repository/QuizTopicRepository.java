package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.QuizTopic;
import hu.kaoszkviz.server.api.model.QuizTopicId;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizTopicRepository extends CrudRepository<QuizTopic, QuizTopicId>{
    
    @Query(value = "SELECT * FROM dbo.findQuizTopicByQuizId(:search_id)", nativeQuery = true)
    public List<QuizTopic> searchByQuizId(@Param("search_id") long searchId);
}
