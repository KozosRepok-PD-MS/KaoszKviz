
package hu.kaoszkviz.server.api.tools;

import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;


public class Generator {

    @Autowired
    private UserRepository userRepository;
    
    public Media getRandomProfilePicture() {
        User rootUser = this.userRepository.findById(0l).orElseThrow(() -> new NotFoundException(User.class, 0));
        List<Media> medias = rootUser.getMedias();
        int randomIndex = this.getRandom(0, medias.size());
        return medias.get(randomIndex);
    }

    private int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}
