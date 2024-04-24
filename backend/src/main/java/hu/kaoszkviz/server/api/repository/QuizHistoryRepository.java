package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.QuizHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizHistoryRepository extends CrudRepository<QuizHistory, Long>{
    
}
