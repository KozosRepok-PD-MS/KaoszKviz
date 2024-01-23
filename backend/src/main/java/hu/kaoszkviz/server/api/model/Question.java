
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = Question.TABLE_NAME)
public class Question {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Getter
    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
    
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;
    
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_type")
    private QuestionType questionType;
    
    @Getter
    @Setter
    @Column(name = "question", columnDefinition = "nvarchar(100)", length = 100)
    private String question;
    
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
    @Column(name = "time_to_answer")
    private byte timeToAnswer; 
    
    
    public static final String TABLE_NAME = "question";
}