package hu.kaoszkviz.server.api.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MediaId implements Serializable{
    @Getter
    private Media media;
    
    @Getter
    private String fileName;
}
