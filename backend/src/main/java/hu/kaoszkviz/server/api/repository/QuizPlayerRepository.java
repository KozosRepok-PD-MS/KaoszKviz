package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.QuizPlayer;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizPlayerRepository extends CrudRepository<QuizPlayer, Long>{
    
    @Query(value = "SELECT * FROM dbo.findQuizPlayerByQuizHistoryId(:search_id)", nativeQuery = true)
    public List<QuizPlayer> searchQuizPlayerByQuizHistoryId(@Param("search_id") long searchId);
}
