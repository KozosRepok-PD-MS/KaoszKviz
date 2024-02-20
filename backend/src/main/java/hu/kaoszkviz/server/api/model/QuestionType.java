package hu.kaoszkviz.server.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
@Table(name = QuestionType.TABLE_NAME)
public class QuestionType {
    @Id
    @Setter
    @Getter
    private String type;
    
    @Getter
    @Setter
    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "questionType")
    private List<Question> questions = new ArrayList<>();
    
    public static final String TABLE_NAME = "question_type";
}
