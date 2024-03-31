package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.config.ConfigDatas;
import hu.kaoszkviz.server.api.dto.UserDTO;
import hu.kaoszkviz.server.api.jsonview.JsonViewEnum;
import hu.kaoszkviz.server.api.model.APIKey;
import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.PasswordResetToken;
import hu.kaoszkviz.server.api.model.PasswordResetTokenId;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.repository.APIKeyRepository;
import hu.kaoszkviz.server.api.repository.MediaRepository;
import hu.kaoszkviz.server.api.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.UserRepository;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.CustomModelMapper;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MediaRepository mediaRepository;
    
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Autowired
    private APIKeyRepository apiKeyRepository;
    
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    CustomModelMapper customModelMapper;
    
    public ResponseEntity<String> addUser(UserDTO user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        
        Optional<User> userToSave = (Optional<User>) this.customModelMapper.map(user);
        
        if (userToSave.isPresent()) {
            User userSave = userToSave.get();

            userSave.setStatus(User.Status.ACTIVE);
            if (this.userRepository.save(userToSave.get()) != null) {
                return new ResponseEntity<>("ok", HttpStatus.CREATED); //ErrorManager
            }
        }
        return new ResponseEntity<>("failed to save", HttpStatus.BAD_REQUEST); //ErrorManager
    }
    
    public ResponseEntity<String> getUserById(long id) {
        ApiKeyAuthentication auth = ApiKeyAuthentication.getAuth().get();
        Optional<User> user = this.userRepository.findById(id);
        
        if (user.isEmpty() || (!auth.getPrincipal().isAdmin() && user.get().getStatus() == User.Status.DELETED)) {
            return ErrorManager.notFound("user not found id: " + id);
        }
        
        try {
            return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.map(user.get()).get(), JsonViewEnum.PUBLIC_VIEW), HttpStatus.OK);
        } catch (Exception ex) {
            return ErrorManager.def("failed to get data");
        }
    }
    
    public ResponseEntity<String> getUsersByName(String searchName) {
        return this.processUserList(this.userRepository.searchByName(searchName));
    }
   
    public ResponseEntity<String> getUsers() {
        ApiKeyAuthentication auth = ApiKeyAuthentication.getAuth().get();
        
        List<User> users = auth.getPrincipal().isAdmin() ? this.userRepository.findAll() : this.userRepository.findAllByStatus(User.Status.ACTIVE);
        
        return this.processUserList(users);
    }
    
    private ResponseEntity<String> processUserList(List<User> users) {
        if (users.isEmpty()) {
            return ErrorManager.notFound("not found");
        }

        try {
            return new ResponseEntity<>(Converter.ModelTableToJsonString(this.customModelMapper.map(users, UserDTO.class), JsonViewEnum.PUBLIC_VIEW), HttpStatus.OK);
        } catch (Exception ex) {
            return ErrorManager.def("failed to get data");
        }
    }
    
    public ResponseEntity<String> deleteById(long id) {
        ApiKeyAuthentication auth = ApiKeyAuthentication.getAuth().get();
        
        if (!auth.getPrincipal().isAdmin() && auth.getPrincipal().getId() != id) { return ErrorManager.unauth(); }
        
        Optional<User> userToDelete = this.userRepository.findById(id);
        
        if (userToDelete.isPresent()) {
            User user = userToDelete.get();
            
            user.setStatus(User.Status.DELETED);
            
            this.userRepository.save(user);
            return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
        }else {
            return ErrorManager.notFound("user");
        }
    }

    
    public ResponseEntity<String> updateUser(UserDTO userDto) {
        User authUser = ApiKeyAuthentication.getAuth().get().getPrincipal();
        
        if (userDto.getId() <= 0) {
            userDto.setId(authUser.getId());
        }
        
        if (!authUser.isAdmin() && authUser.getId() != userDto.getId()) { return ErrorManager.unauth(); }
        
        Optional<User> updatedUser = this.userRepository.findById(userDto.getId());
        
        if (updatedUser.isPresent()) {
            User user = (User) updatedUser.get();
            
            if (authUser.isAdmin()) {
                user.setAdmin(userDto.getAdmin() != null ? userDto.getAdmin().booleanValue() : user.isAdmin());
            }
            
            String updatedEmail = userDto.getEmail();
            user.setEmail((updatedEmail == null || updatedEmail.isBlank()) ? user.getEmail() : userDto.getEmail());
            
            String updatedUsername = userDto.getUsername();
            user.setUsername((updatedUsername == null || updatedUsername.isBlank()) ? user.getUsername() : userDto.getUsername());
            
            if ((userDto.getProfilePictureName() != null && !userDto.getProfilePictureName().isBlank()) && userDto.getProfilePictureOwner() > 0) {
                user.setProfilePicture(Media.mediaFromKeys(userDto.getProfilePictureOwner(), userDto.getProfilePictureName()));
            }
            
            this.userRepository.save(user);
            return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
        }

        return ErrorManager.notFound("user");
    }
    
    public ResponseEntity<String> generatePasswordResetToken(String userEmail) {
        Optional<User> user = this.userRepository.getUserByEmail(userEmail);
        
        if (user.isPresent()) {
            PasswordResetToken passwordResetToken = new PasswordResetToken(user.get());
            passwordResetToken = this.passwordResetTokenRepository.save(passwordResetToken);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("RESET-TOKEN", passwordResetToken.getKey().toString());
            return new ResponseEntity<>("", headers, HttpStatus.OK); //ErrorManager
        } else {
            return ErrorManager.notFound("user");
        }
    }
    
    public ResponseEntity<String> resetPassword(UUID resetKey,String newPassword) {
        Optional<PasswordResetToken> tokenOpt = this.passwordResetTokenRepository.getByKey(resetKey);
        
        if (tokenOpt.isEmpty()) { return ErrorManager.notFound(); }
        
        PasswordResetToken token = tokenOpt.get();
        User userToModify = token.getUser();
        
        try {
            this.passwordResetTokenRepository.deleteById(new PasswordResetTokenId(userToModify, token.getExpiresAt()));
            userToModify.setPassword(this.passwordEncoder.encode(newPassword));
            this.userRepository.save(userToModify);

            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception ex) {
            return ErrorManager.def();
        }
    }
    
    public ResponseEntity<String> loginUser(String loginBase, String password) {
        Optional<User> loggingUser = this.userRepository.searchByLoginBase(loginBase);
        
        if (loggingUser.isEmpty()) { return ErrorManager.notFound("user"); }
        User user = loggingUser.get();
        
        if (!this.passwordEncoder.matches(password, user.getPassword())) { return ErrorManager.def("incorrect password"); }
        
        APIKey apiKey = new APIKey(user);
        this.apiKeyRepository.save(apiKey);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set(ConfigDatas.API_KEY_HEADER, apiKey.getKey().toString());
        return new ResponseEntity<>("", headers, HttpStatus.OK);
    }
    
    public ResponseEntity<String> logoutUser(String apiKey) {
        Optional<APIKey> apiAuth = this.apiKeyRepository.findById(UUID.fromString(apiKey));
        
        if (apiAuth.isEmpty()) { return ErrorManager.notFound("apiKey"); }
        
        this.apiKeyRepository.delete(apiAuth.get());
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
