
package hu.kaoszkviz.server.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import hu.kaoszkviz.server.api.jsonview.PrivateJsonView;
import hu.kaoszkviz.server.api.jsonview.PublicJsonView;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = Quiz.TABLE_NAME)
public class Quiz {
    @Id
    @Getter
    @Setter
    @JsonView(PublicJsonView.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Getter
    @Setter
    @JsonView(PublicJsonView.class)
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Getter
    @Setter
    @JsonView(PublicJsonView.class)
    @Column(columnDefinition = "nvarchar(30)", length = 30, nullable = false)
    private String title;
    
    @Getter
    @Setter
    @JsonView(PublicJsonView.class)
    @Column(columnDefinition = "nvarchar(500)", length = 500)
    private String description;
    
    @Getter
    @Setter
    @JsonView(PublicJsonView.class)
    @JsonManagedReference
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name="media_file_name"),
        @JoinColumn(name="media_owner_id")
    })
    private Media mediaContent;
    
    @Getter
    @Setter
    @JsonView(PrivateJsonView.class)
    @Column(name = "is_public", columnDefinition = "bit default 0")
    private boolean isPublic;
    
    @Getter
    @Setter
    @JsonView(PublicJsonView.class)
    @Column(name = "short_accessible_name", unique = true)
    private String shortAccessibleName;
    
    
    @Getter
    @Setter
    @JsonView(PrivateJsonView.class)
    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    private List<QuizHistory> quizs = new ArrayList<>();
    
    @Getter
    @Setter
    @JsonView(PublicJsonView.class)
    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    private List<QuizTopic> topics = new ArrayList<>();
    
    @Getter
    @Setter
    @JsonView(PrivateJsonView.class)
    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    private List<Question> questions = new ArrayList<>();
    
    
    public static final String TABLE_NAME = "quiz";
}
