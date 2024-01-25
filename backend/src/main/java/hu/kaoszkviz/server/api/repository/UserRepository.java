
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
}
