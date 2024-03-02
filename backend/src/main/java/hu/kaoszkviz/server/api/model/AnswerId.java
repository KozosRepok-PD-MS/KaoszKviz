
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
}
