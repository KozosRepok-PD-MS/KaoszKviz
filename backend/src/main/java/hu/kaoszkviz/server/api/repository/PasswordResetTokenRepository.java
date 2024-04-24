
package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.PasswordResetToken;
import hu.kaoszkviz.server.api.model.PasswordResetTokenId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, PasswordResetTokenId> {

    @Query(value = "SELECT * FROM getPasswordResetTokenByKey(:reset_key)", nativeQuery = true)
    public Optional<PasswordResetToken> getByKey(@Param("reset_key") UUID resetKey);
}
