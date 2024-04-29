
package hu.kaoszkviz.server.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import hu.kaoszkviz.server.api.jsonview.PublicJsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDTO implements DTO {
    @JsonView(PublicJsonView.class)
    private long questionId;
    
    @JsonView(PublicJsonView.class)
    private byte ordinalNumber;
    
    @JsonView(PublicJsonView.class)
    private String answer;
    
    @JsonView(PublicJsonView.class)
    private boolean correct;
    
    @JsonView(PublicJsonView.class)
    private String correctAnswer;
}
