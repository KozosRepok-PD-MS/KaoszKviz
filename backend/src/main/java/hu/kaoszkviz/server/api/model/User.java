
package hu.kaoszkviz.server.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = User.TABLE_NAME)
public class User {
    
    @Id
    @Setter
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Setter
    @Getter
    @Column(nullable = false, columnDefinition = "nvarchar(20)", length = 20, unique = true)
    private String username;
    
    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String email;
    
    @Getter
    @Setter
    @Column(nullable = false)
    private String password;
    
    @Getter
    @Setter
    @Column(columnDefinition = "bit default 0")
    private boolean admin;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name="profile_picture_file_name"),
        @JoinColumn(name="profile_picture_owner_id")
    })
    private Media profilePicture;
    
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, name = "registered_at")
    private LocalDateTime registeredAt;
    
    
    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<PasswordResetToken> passwordResetTokens = new ArrayList<PasswordResetToken>();
    
    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Quiz> quizs = new ArrayList<Quiz>();
    
    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Media> medias = new ArrayList<Media>();
    
    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "starter")
    private List<QuizHistory> historys = new ArrayList<QuizHistory>();
    
    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "player")
    private List<QuizPlayer> players = new ArrayList<QuizPlayer>();
    
    public static final String TABLE_NAME = "usr";
}
