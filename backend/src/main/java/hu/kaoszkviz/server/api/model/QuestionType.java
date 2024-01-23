package hu.kaoszkviz.server.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    
    public static final String TABLE_NAME = "question_type";
}
