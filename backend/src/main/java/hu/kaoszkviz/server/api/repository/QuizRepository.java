
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.Quiz;
import hu.kaoszkviz.server.api.model.User;
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
    public List<Quiz> findAllByOwnerId(@Param("search_id") long searchId);
    
    @Query(value = "SELECT * FROM findQuizPublic", nativeQuery = true)
    public List<Quiz> findAllPublic();
    
    @Query(value = "SELECT * FROM dbo.findQuizPublicByOwnerId(:owner_id)", nativeQuery = true)
    public List<Quiz> findAllPublicByOwnerId(@Param("owner_id") long ownerId);
}
