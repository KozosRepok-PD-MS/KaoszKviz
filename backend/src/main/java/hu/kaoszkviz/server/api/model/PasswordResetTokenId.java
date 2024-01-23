
package hu.kaoszkviz.server.api.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PasswordResetTokenId implements Serializable {
    @Getter
    private User user;

    @Getter
    private LocalDateTime expiresAt;
}
