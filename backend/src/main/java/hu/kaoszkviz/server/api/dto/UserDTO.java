
package hu.kaoszkviz.server.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import hu.kaoszkviz.server.api.jsonview.NoContainJsonView;
import hu.kaoszkviz.server.api.jsonview.PrivateJsonView;
import hu.kaoszkviz.server.api.jsonview.PublicJsonView;
import hu.kaoszkviz.server.api.model.User.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements DTO {

    @JsonView(PublicJsonView.class)
    private long id = -1;
    
    @NotEmpty
    @Size(min = 2, message = "user name should have at least 2 characters")
    @JsonView(PublicJsonView.class)
    private String username;
    
    @NotEmpty
    @Email
    @JsonView(PrivateJsonView.class)
    private String email;
    
    //@NotEmpty
    //@Size(min = 8, message = "password should have at least 8 characters")
    @JsonView(NoContainJsonView.class)
    private String password;
    
    @JsonView(PrivateJsonView.class)
    private Boolean admin = false;
    
    @JsonView(PublicJsonView.class)
    private long profilePictureOwnerId = -1;
    
    @JsonView(PublicJsonView.class)
    private String profilePictureName = null;
    
    @JsonView(PublicJsonView.class)
    private LocalDateTime registeredAt;
    
    @Enumerated(EnumType.STRING)
    @JsonView(PrivateJsonView.class)
    private Status status;
    
}
