
package hu.kaoszkviz.server.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import hu.kaoszkviz.server.api.jsonview.PrivateJsonView;
import hu.kaoszkviz.server.api.jsonview.PublicJsonView;
import hu.kaoszkviz.server.api.model.Quiz;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO implements DTO {
    @JsonView(PublicJsonView.class)
    private long id;
    
    @JsonView(PublicJsonView.class)
    private long ownerId = -1;
    
    @JsonView(PublicJsonView.class)
    private String title;
    
    @JsonView(PublicJsonView.class)
    private String description;
    
    @JsonView(PublicJsonView.class)
    private long mediaContentOwner = -1;
    
    @JsonView(PublicJsonView.class)
    private String mediaContentName = null;
    
    @JsonView(PrivateJsonView.class)
    private boolean isPublic;
    
    @JsonView(PublicJsonView.class)
    private String shortAccessibleName;
    
    private Quiz.Status status;
}
