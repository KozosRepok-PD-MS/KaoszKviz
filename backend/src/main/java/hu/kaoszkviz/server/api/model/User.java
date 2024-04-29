
package hu.kaoszkviz.server.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = User.TABLE_NAME)
public class User implements Model {
    public static enum Status {ACTIVE, DELETED};
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable = false, columnDefinition = "nvarchar(20)", length = 20, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(columnDefinition = "bit default 0")
    private boolean admin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name="profile_picture_file_name"),
        @JoinColumn(name="profile_picture_owner_id")
    })
    private Media profilePicture;
    
    @CreationTimestamp
    @Column(updatable = false, name = "registered_at")
    private LocalDateTime registeredAt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    
    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<PasswordResetToken> passwordResetTokens = new ArrayList<PasswordResetToken>();
    
    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Quiz> quizs = new ArrayList<>();
    
    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Media> medias = new ArrayList<>();
    
    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "starter")
    private List<QuizHistory> histories = new ArrayList<>();
    
    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "player")
    private List<QuizPlayer> players = new ArrayList<>();
    
    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<APIKey> apiKeys = new ArrayList<>();

    public static final String TABLE_NAME = "usr";
}
