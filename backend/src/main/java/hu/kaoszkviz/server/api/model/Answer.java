
package hu.kaoszkviz.server.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@IdClass(AnswerId.class)
@Table(name = Answer.TABLE_NAME)
public class Answer implements Model {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    
    @Id
    @Column(name = "ordinal_number", nullable = false)
    private byte ordinalNumber;
    
    @Column(columnDefinition = "nvarchar(50)", length = 50, nullable = false)
    private String answer;
    
    @Column(columnDefinition = "bit default 0")
    private boolean correct;
    
    @Column(columnDefinition = "nvarchar(50)", length = 50, name = "correct_answer")
    private String correctAnswer;

    
    public static final String TABLE_NAME = "answer";
}
