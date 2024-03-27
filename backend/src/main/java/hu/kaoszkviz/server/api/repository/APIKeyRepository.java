
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.APIKey;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface APIKeyRepository extends CrudRepository<APIKey, UUID> {
    
}
