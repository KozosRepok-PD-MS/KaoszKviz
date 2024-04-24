
package hu.kaoszkviz.server.api.model;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = Quiz.TABLE_NAME)
public class Quiz implements Model {
    public static enum Status {ACTIVE, DELETED};
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(columnDefinition = "nvarchar(30)", length = 30, nullable = false)
    private String title;
    
    @Column(columnDefinition = "nvarchar(500)", length = 500)
    private String description;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name="media_file_name"),
        @JoinColumn(name="media_owner_id")
    })
    private Media mediaContent;
    
    @Column(name = "is_public", columnDefinition = "bit default 0")
    private boolean isPublic;
    
    @Column(name = "short_accessible_name", unique = true)
    private String shortAccessibleName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    private List<QuizHistory> quizs = new ArrayList<>();
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    private List<QuizTopic> topics = new ArrayList<>();
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    private List<Question> questions = new ArrayList<>();
    
    
    public static final String TABLE_NAME = "quiz";
}
