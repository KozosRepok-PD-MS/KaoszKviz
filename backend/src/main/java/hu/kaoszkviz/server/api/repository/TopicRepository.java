
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.Topic;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TopicRepository extends CrudRepository<Topic, Long> {

    @Override
    public List<Topic> findAll();

    @Override
    public Optional<Topic> findById(Long id);
    
    @Query(value = "SELECT * FROM dbo.findTopicByString(:search_string)", nativeQuery = true)
    public List<Topic> searchBy(@Param("search_string") String searchString);
}
