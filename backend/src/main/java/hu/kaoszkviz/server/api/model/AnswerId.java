
package hu.kaoszkviz.server.api.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AnswerId implements Serializable {
    @Getter
    private Question question;
    
    @Getter
    private byte ordinalNumber;
    
    @Override
    public String toString() {
        return "[questionId:%d|ordinalNumber:%d]".formatted(this.question.getId(),this.ordinalNumber);
    }
}
