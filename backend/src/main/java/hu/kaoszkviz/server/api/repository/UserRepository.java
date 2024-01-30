
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    public List<User> findAll();

    @Query(value = "SELECT * FROM dbo.findUserByName(:search_name)", nativeQuery = true)
    public List<User> searchByName(@Param("search_name") String searchName);
    
    
}
