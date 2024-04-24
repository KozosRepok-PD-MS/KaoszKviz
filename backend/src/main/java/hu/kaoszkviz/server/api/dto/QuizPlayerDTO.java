
package hu.kaoszkviz.server.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import hu.kaoszkviz.server.api.jsonview.PublicJsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizPlayerDTO implements DTO {
    @JsonView(PublicJsonView.class)
    private long id;
    
    @JsonView(PublicJsonView.class)
    private long quizHistoryId;
    
    @JsonView(PublicJsonView.class)
    private long playerId = -1;
    
    @JsonView(PublicJsonView.class)
    private String name;
    
    @JsonView(PublicJsonView.class)
    private long profilePictureOwnerId = -1;
    
    @JsonView(PublicJsonView.class)
    private String profilePictureName = null;
    
    @JsonView(PublicJsonView.class)
    private long reachedPoint;
    
    @JsonView(PublicJsonView.class)
    private long answerTime;
}
