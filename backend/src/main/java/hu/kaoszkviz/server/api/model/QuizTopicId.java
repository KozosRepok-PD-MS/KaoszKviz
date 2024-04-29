package hu.kaoszkviz.server.api.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class QuizTopicId implements Serializable{
    private Quiz quiz;
    
    private Topic topic;

    @Override
    public String toString() {
        return "[quizId:%d|topicId:%d]".formatted(this.quiz.getId(),this.topic.getId());
    }
}
