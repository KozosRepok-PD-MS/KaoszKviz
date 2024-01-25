package hu.kaoszkviz.server.api.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class QuizTopicId implements Serializable{
    @Getter
    private Quiz quiz;
    
    @Getter
    private Topic topic;
}
