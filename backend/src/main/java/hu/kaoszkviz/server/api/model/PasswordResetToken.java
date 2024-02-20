
package hu.kaoszkviz.server.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@IdClass(PasswordResetTokenId.class)
@Table(name = PasswordResetToken.TABLE_NAME)
public class PasswordResetToken {
    @Id
    @Getter
    @Setter
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Id
    @Getter
    @Setter
    @Column(nullable = false, updatable = false, name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="reset_key", columnDefinition = "BINARY(16)")
    private UUID key;
    
    
    public static final String TABLE_NAME = "password_reset_token";
}
