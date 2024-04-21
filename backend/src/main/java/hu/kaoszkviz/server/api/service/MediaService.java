package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.MediaId;
import hu.kaoszkviz.server.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.MediaRepository;
import hu.kaoszkviz.server.api.repository.UserRepository;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import hu.kaoszkviz.server.api.tools.HeaderBuilder;
import java.io.IOException;
import java.util.Optional;
import javax.activation.MimetypesFileTypeMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaService {
    
    private static final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
    
    @Autowired
    private MediaRepository mediaRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public ResponseEntity<String> addMedia(MultipartFile file, String fileName) {
        ApiKeyAuthentication auth = ApiKeyAuthentication.getAuth();
        Media media = new Media();

        try {
            media.setData(file.getBytes());
        } catch (IOException ex) {
            return ErrorManager.def("failed to get file details");
        }
        User owner = auth.getPrincipal();

        Optional<User> optUser = this.userRepository.findById(owner.getId());
        if (optUser.isPresent()) {
            media.setOwner(optUser.get());
            media.setFileName(fileName);

            if (this.mediaRepository.save(media) != null) {
                return new ResponseEntity<>("ok", HttpStatus.CREATED);
            }

        }
        
        return ErrorManager.def("failed to save image");
    }
    
    public ResponseEntity<byte[]> getMedia(long owner, String fileName) {
        Optional<User> ownerOptional = this.userRepository.findById(owner);
        
        if (ownerOptional.isPresent()) { 
            Optional<Media> mediaOptional = this.mediaRepository.findById(new MediaId(ownerOptional.get(), fileName));
            
            if (mediaOptional.isPresent()) {
                Media media = mediaOptional.get();
               return new ResponseEntity<>(media.getData(), HeaderBuilder.build("Content-type", MediaService.getMediaContentType(media)), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new byte[0], HeaderBuilder.build("Content-type", "image"), HttpStatus.NOT_FOUND);
    }
    
    public static String getMediaContentType(Media media) {
        return MediaService.fileTypeMap.getContentType(media.getFileName());
    }
}
