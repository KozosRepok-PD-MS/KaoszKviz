package hu.kaoszkviz.server.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.MediaId;
import hu.kaoszkviz.server.api.model.PasswordResetToken;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.repository.MediaRepository;
import hu.kaoszkviz.server.api.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.UserRepository;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class UserService {
    
    private static final int PASSWORD_RESET_TOKEN_EXPIRES_MINUTES = 15;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MediaRepository mediaRepository;
    
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    public ResponseEntity<String> addUser(User user) {
        if (this.userRepository.save(user) != null) {
            return new ResponseEntity<>("ok", HttpStatus.CREATED); //ErrorManager
        }
        return new ResponseEntity<>("failed to save", HttpStatus.BAD_REQUEST); //ErrorManager
    }
    
    public ResponseEntity<String> getUsers(HashMap<String, String> requestBody) {
        List<User> users = new ArrayList<>();
        
        if (requestBody == null) {
            users = this.userRepository.findAll();
        } else {
            String searchName = requestBody.get("searchName");
            String idString = requestBody.get("userId");

            if (null != idString && !idString.isBlank()) {
                long id;

                try {
                    id = Long.parseLong(idString);
                } catch(NumberFormatException ex) {
                    return ErrorManager.nan();
                }

                Optional<User> user = this.userRepository.findById(id);

                if (user.isPresent()) {
                    users.add(user.get());
                }

            } else if (searchName != null && !searchName.isBlank()) {
                users = getUsersByName(searchName);
            }
        }
        
        if (users.isEmpty()) {
            return ErrorManager.notFound("users");
        }
        
        try {
            return new ResponseEntity<>(Converter.ModelTableToJsonString(users), HttpStatus.OK); //ErrorManager
        } catch (JsonProcessingException ex) {
            return new ResponseEntity<>("failed to get data", HttpStatus.INTERNAL_SERVER_ERROR); //ErrorManager
        }
    }
    
    public List<User> getUsersByName(String searchName) {
        if (searchName != null && !searchName.isBlank()) {
            return this.userRepository.searchByName(searchName);
        }
        return new ArrayList<>();
    }
    
    public ResponseEntity<String> deleteById(HashMap<String, String> requestBody) {
        try {
            long id = Long.parseLong(requestBody.get("userId"));
            return this.deleteById(id);
        } catch(Exception ex) {
            return ErrorManager.def(ex.getLocalizedMessage());
        }
    }
    
    public ResponseEntity<String> deleteById(long id) {
        if (this.userRepository.findById(id).isPresent()) {
            this.userRepository.deleteById(id);
            return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
        }else {
            return ErrorManager.notFound("user");
        }
    }
    
    public ResponseEntity<String> updateUser(HashMap<String, String> datas){
        if (this.userRepository.findById(Long.valueOf(datas.get("id"))).isPresent()) {
            User updatedUser = this.userRepository.findById(Long.valueOf(datas.get("id"))).get();
            updatedUser.setUsername(datas.get("username"));
            updatedUser.setEmail(datas.get("email"));
            updatedUser.setPassword(datas.get("password"));
            Optional<User> profilePictureOwner = this.userRepository.findById(Long.valueOf(datas.get("profilePictureOwnerId")));
            if (profilePictureOwner.isPresent()) {
                MediaId  mediaId = new MediaId((User) profilePictureOwner.get(), datas.get("profilePictureName"));
                Optional<Media> media = this.mediaRepository.findById(mediaId);
                if (!media.isPresent()) {
                    return ErrorManager.notFound("media");
                }
                updatedUser.setProfilePicture(media.get());
                this.userRepository.save(updatedUser);
                return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
            } else {
                return ErrorManager.notFound("profilePicturOwner user");
            }
        } else{
            return ErrorManager.notFound("user");
        }
    }
    
    public ResponseEntity<String> generatePasswordResetToken(Long userId){
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setUser(user.get());
            passwordResetToken.setExpiresAt(LocalDateTime.now().plusMinutes(UserService.PASSWORD_RESET_TOKEN_EXPIRES_MINUTES));
            this.passwordResetTokenRepository.save(passwordResetToken);
            return new ResponseEntity<>("ok, minutes: " + UserService.PASSWORD_RESET_TOKEN_EXPIRES_MINUTES, HttpStatus.OK); //ErrorManager
        } else {
            return ErrorManager.notFound("user");
        }
    }
}
