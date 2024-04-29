
package hu.kaoszkviz.server.api.tools;

import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.repository.UserRepository;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;


public class Generator {
    private static final Random RND = new Random();
    
    @Autowired
    private UserRepository userRepository;
    
    public Media getRandomProfilePicture() {
        User rootUser = this.userRepository.findById(0l).orElseThrow(() -> new NotFoundException(User.class, 0));
        List<Media> medias = rootUser.getMedias();
        int randomIndex = RND.nextInt(0, medias.size());
        return medias.get(randomIndex);
    }
}
