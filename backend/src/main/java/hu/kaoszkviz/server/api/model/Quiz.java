
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Quiz.TABLE_NAME)
public class Quiz {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Getter
    @Setter
    @Column(columnDefinition = "nvarchar(30)", length = 30, nullable = false)
    private String title;
    
    @Getter
    @Setter
    @Column(columnDefinition = "nvarchar(500)", length = 500)
    private String description;
    
    @Getter
    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name="media_file_name"),
        @JoinColumn(name="media_owner_id")
    })
    private Media mediaContent;
    
    @Getter
    @Setter
    @Column(name = "is_public", columnDefinition = "bit default 0")
    private boolean isPublic;
    
    @Getter
    @Setter
    @Column(name = "short_accessible_name")
    private String shortAccessibleName;
    
    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    private List<QuizHistory> quizs = new ArrayList<>();
    
    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    private List<QuizTopic> topics = new ArrayList<>();
    
    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    private List<Question> medias = new ArrayList<Question>();
    
    
    public static final String TABLE_NAME = "quiz";
}
