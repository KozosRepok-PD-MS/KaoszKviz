
package hu.kaoszkviz.server.api.tools;

import hu.kaoszkviz.server.api.dto.UserDTO;
import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;


public class CustomModelMapper extends ModelMapper {
    
    public Optional<?> map(Object source) {
        if (source instanceof User) {
            User src = (User) source;
            UserDTO userDTO = this.map(src, UserDTO.class);
            Media profilePicture = src.getProfilePicture();
            if (profilePicture != null) {
                userDTO.setProfilePictureOwner(profilePicture.getOwner().getId());
                userDTO.setProfilePictureName(profilePicture.getFileName());
            } else {
                userDTO.setProfilePictureOwner(-1);
                userDTO.setProfilePictureName(null);
            }
            
            return Optional.of(userDTO);
        } else if (source instanceof UserDTO) {
            UserDTO src = (UserDTO) source;
            User user = this.map(src, User.class);
            
            
            if ((src.getProfilePictureName() == null || src.getProfilePictureName().isBlank()) || src.getProfilePictureOwner() == -1) {
                user.setProfilePicture(null);
            } else {
                //ha jók a kép adatai
                user.setProfilePicture(Media.mediaFromKeys(0, src.getProfilePictureName()));
            }
            
            return Optional.of(user);
        }
        
        return Optional.empty();
    }
    
    public <S> List<Object> map(List<S> sources) {
        return this.map(sources, Object.class);
    }
    
    public <S, D> List<D> map(List<S> sources, Class<D> destinationType) {
        List<D> modelDTOS = new ArrayList<>();
        
        if (!sources.isEmpty()) {
            for (S source : sources) {
                Optional<D> mapped = (Optional<D>) this.map(source);
                if (mapped.isPresent()) {
                    modelDTOS.add(mapped.get());
                }
            }
        }

        return modelDTOS;
    }
   
}
