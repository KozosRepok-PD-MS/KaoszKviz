
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.PasswordResetToken;
import hu.kaoszkviz.server.api.model.PasswordResetTokenId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, PasswordResetTokenId> {
    
}
