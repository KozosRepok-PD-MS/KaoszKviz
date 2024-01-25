package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.QuizPlayer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizPlayerRepository extends CrudRepository<QuizPlayer, Long>{
    
}
