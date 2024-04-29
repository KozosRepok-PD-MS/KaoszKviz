
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
import jakarta.persistence.UniqueConstraint;
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
@Table(name = Question.TABLE_NAME, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"quiz_id", "question"})
})
public class Question implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
    
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_type", nullable = false)
    private QuestionType questionType;
    
    @Column(name = "question", columnDefinition = "nvarchar(100)", length = 100, nullable = false)
    private String question;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name="media_file_name"),
        @JoinColumn(name="media_owner_id")
    })
    private Media mediaContent;
    
    @Column(name = "time_to_answer", nullable = false)
    private byte timeToAnswer; 
    
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();
    
    
    public static final String TABLE_NAME = "question";
}