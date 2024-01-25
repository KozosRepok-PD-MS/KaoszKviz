package hu.kaoszkviz.server.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@IdClass(MediaId.class)
@Table(name = Media.TABLE_NAME)
public class Media {
    
    @Id
    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    @Id
    @Setter
    @Getter
    @Column(nullable = false, columnDefinition = "nvarchar(30)", length = 30, name = "file_name")
    private String fileName;
    
    @Setter
    @Getter
    @Column(nullable = false, columnDefinition = "varbinary(MAX)")
    private byte[] data;
    
    
    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mediaContent")
    private List<Quiz> quizs = new ArrayList<Quiz>();
    
    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profilePicture")
    private List<QuizPlayer> players = new ArrayList<QuizPlayer>();
    
    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profilePicture")
    private List<User> users = new ArrayList<>();
    
    public static final String TABLE_NAME = "media";
    public static final long MAX_FIE_SIZE = 5_000_000;
}
