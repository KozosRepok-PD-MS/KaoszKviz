
package hu.kaoszkviz.server.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import hu.kaoszkviz.server.api.config.ConfigDatas;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@RequiredArgsConstructor
@EqualsAndHashCode
@IdClass(PasswordResetTokenId.class)
@Table(name = PasswordResetToken.TABLE_NAME)
public class PasswordResetToken {
    @Id
    @Getter
    @Setter
    //@NonNull
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Id
    @Getter
    @Column(nullable = false, updatable = false, name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Getter
    @Column(name="reset_key") //, columnDefinition = "BINARY(16)"
    private UUID key;
    
    public PasswordResetToken() {
        this(null, null, null);
    }
    
    public PasswordResetToken(User user) {
        this(user, LocalDateTime.now().plus(ConfigDatas.PASSWORD_RESET_TOKEN_VALIDITY_DURATION, ConfigDatas.PASSWORD_RESET_TOKEN_VALIDITY_TYPE), null);
    
    }

    public PasswordResetToken(User user, LocalDateTime expiresAt, UUID key) {
        this.user = user;
        this.expiresAt = expiresAt;
        this.key = key;
    }
    
    
    
    @PrePersist
    public void setGeneratedDatas() {
        this.key = UUID.randomUUID();
    }
    
    public static final String TABLE_NAME = "password_reset_token";
}
