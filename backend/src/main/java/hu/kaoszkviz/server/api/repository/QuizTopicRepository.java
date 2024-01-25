package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.QuizTopic;
import hu.kaoszkviz.server.api.model.QuizTopicId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizTopicRepository extends CrudRepository<QuizTopic, QuizTopicId>{
    
}
