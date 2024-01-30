
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.Quiz;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizRepository extends CrudRepository<Quiz, Long> {

    @Override
    public List<Quiz> findAll();
    
    @Query(value = "SELECT * FROM dbo.findQuizByOwnerId(:search_id)", nativeQuery = true)
    public List<Quiz> searchByOwnerId(@Param("search_id") long searchId);
}
