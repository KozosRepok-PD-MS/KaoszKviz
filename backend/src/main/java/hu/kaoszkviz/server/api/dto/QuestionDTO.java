
package hu.kaoszkviz.server.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import hu.kaoszkviz.server.api.jsonview.PrivateJsonView;
import hu.kaoszkviz.server.api.jsonview.PublicJsonView;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO implements DTO {
    @JsonView(PublicJsonView.class)
    private long id;
    
    @JsonView(PublicJsonView.class)
    private long quizId;
    
    @JsonView(PrivateJsonView.class)
    private LocalDateTime createdAt;
    
    @JsonView(PublicJsonView.class)
    private String questionType;
    
    @JsonView(PublicJsonView.class)
    private String question;
    
    @JsonView(PublicJsonView.class)
    private long mediaContentOwnerId = -1;
    
    @JsonView(PublicJsonView.class)
    private String mediaContentName = "";
    
    @JsonView(PublicJsonView.class)
    private byte timeToAnswer;
}
