
package hu.kaoszkviz.server.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import hu.kaoszkviz.server.api.config.ConfigDatas;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Table(name = APIKey.TABLE_NAME)
public class APIKey {
    
    @Id
    @Getter
    @Setter
    @Column(name = "client_secret", unique = true, nullable = false)
    private UUID key;
    
    @Getter
    @Setter
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @NonNull
    private User user;
    
    
    @Getter
    @Setter
    @Column(name = "expires_at", nullable = false, updatable = false)
    private LocalDateTime expiresAt;
    
    @PrePersist
    public void expireTimeSave() {
        this.expiresAt = LocalDateTime.now().plus(ConfigDatas.API_KEY_VALIDITY_DURATION, ConfigDatas.API_KEY_VALIDITY_TYPE);
        this.key = UUID.randomUUID();
    }
    
    
    public static final String TABLE_NAME = "api_key";
}
