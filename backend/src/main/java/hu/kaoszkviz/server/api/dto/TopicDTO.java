
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
public class TopicDTO implements DTO {
    @JsonView(PublicJsonView.class)
    private long id = -1;
    
    @JsonView(PublicJsonView.class)
    private String title;
}
