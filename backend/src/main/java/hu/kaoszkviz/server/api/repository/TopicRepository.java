
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TopicRepository extends CrudRepository<Topic, Long> {
    
}
