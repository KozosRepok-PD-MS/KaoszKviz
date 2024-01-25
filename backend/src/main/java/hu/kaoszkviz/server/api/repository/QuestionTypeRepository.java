package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.QuestionType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTypeRepository extends CrudRepository<QuestionType, String>{
    
}
