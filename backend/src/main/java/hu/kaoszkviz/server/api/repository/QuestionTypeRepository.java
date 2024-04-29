package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.QuestionType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTypeRepository extends CrudRepository<QuestionType, String>{

    @Override
    public Optional<QuestionType> findById(String id);

    @Override
    public List<QuestionType> findAll();
    
    
}
