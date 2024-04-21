package hu.kaoszkviz.server.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.kaoszkviz.server.api.config.ConfigDatas;
import hu.kaoszkviz.server.api.dto.UserDTO;
import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.jsonview.JsonViewEnum;
import hu.kaoszkviz.server.api.model.APIKey;
import hu.kaoszkviz.server.api.model.PasswordResetToken;
import hu.kaoszkviz.server.api.model.PasswordResetTokenId;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.repository.APIKeyRepository;
import hu.kaoszkviz.server.api.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.UserRepository;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.CustomModelMapper;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import hu.kaoszkviz.server.api.tools.HeaderBuilder;
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
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Autowired
    private APIKeyRepository apiKeyRepository;
    
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    CustomModelMapper customModelMapper;
    
    public ResponseEntity<String> addUser(UserDTO user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        User userToSave = new User();
        
        this.customModelMapper.fromDTO(user, userToSave);
        
        userToSave.setAdmin(false);
        userToSave.setStatus(User.Status.ACTIVE);
        if (this.userRepository.save(userToSave) != null) {
            return new ResponseEntity<>("ok", HttpStatus.CREATED); //ErrorManager
        }
        
        return new ResponseEntity<>("failed to save", HttpStatus.BAD_REQUEST); //ErrorManager
    }
    
    public ResponseEntity<String> getUserById(long id) {
        ApiKeyAuthentication auth = ApiKeyAuthentication.getAuth();
        User user = this.userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
        
        if ((!auth.getPrincipal().isAdmin() && user.getStatus() == User.Status.DELETED)) {
            throw new NotFoundException(User.class, id);
        }
        
        try {
            return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.fromModel(user, UserDTO.class), JsonViewEnum.PUBLIC_VIEW), HttpStatus.OK);
        } catch (JsonProcessingException ex) {
            return ErrorManager.def("failed to get data");
        }
    }
    
    public ResponseEntity<String> getUsersByName(String searchName) {
        return this.processUserList(this.userRepository.searchByName(searchName));
    }
   
    public ResponseEntity<String> getUsers() {
        ApiKeyAuthentication auth = ApiKeyAuthentication.getAuth();
        
        List<User> users = auth.getPrincipal().isAdmin() ? this.userRepository.findAll() : this.userRepository.findAllByStatus(User.Status.ACTIVE);
        
        return this.processUserList(users);
    }
    
    private ResponseEntity<String> processUserList(List<User> users) {
        if (users.isEmpty()) {
            throw new NotFoundException(User.class, "");
        }

        try {
            return new ResponseEntity<>(Converter.ModelTableToJsonString(this.customModelMapper.fromModelList(users, UserDTO.class), JsonViewEnum.PUBLIC_VIEW), HttpStatus.OK);
        } catch (Exception ex) {
            return ErrorManager.def("failed to get data");
        }
    }
    
    public ResponseEntity<String> deleteById(long id) {
        ApiKeyAuthentication auth = ApiKeyAuthentication.getAuth();
        
        if (!auth.getPrincipal().isAdmin() && auth.getPrincipal().getId() != id) { return ErrorManager.unauth(); }
        
        User user = this.userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
        
        user.setStatus(User.Status.DELETED);

        if (this.userRepository.save(user) != null) {
            return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
        }
        
        return ErrorManager.def("failed to save");
    }

    
    public ResponseEntity<String> updateUser(UserDTO userDto) {
        User authUser = ApiKeyAuthentication.getAuth().getPrincipal();
        
        if (userDto.getId() < 0) {
            return ErrorManager.notFound("id");
        }
        
        if (!authUser.isAdmin() && authUser.getId() != userDto.getId()) { return ErrorManager.unauth(); }
        
        User user = this.userRepository.findById(userDto.getId()).orElseThrow(() -> new NotFoundException(User.class, userDto.getId()));
            
        this.customModelMapper.updateFromDTO(userDto, user);

        if (this.userRepository.save(user) != null) {
            return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
        }
        
        return ErrorManager.def("failed to save");
    }
    
    public ResponseEntity<String> generatePasswordResetToken(String userEmail) {
        User user = this.userRepository.getUserByEmail(userEmail).orElseThrow(() -> new NotFoundException(User.class, ""));

        PasswordResetToken passwordResetToken = new PasswordResetToken(user);
        passwordResetToken = this.passwordResetTokenRepository.save(passwordResetToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(ConfigDatas.PASSWORD_RESET_TOKEN_HEADER, passwordResetToken.getKey().toString());
        return new ResponseEntity<>("", headers, HttpStatus.OK); //ErrorManager
    }
    
    public ResponseEntity<String> resetPassword(UUID resetKey,String newPassword) {
        PasswordResetToken token = this.passwordResetTokenRepository.getByKey(resetKey).orElseThrow(()-> new NotFoundException(PasswordResetToken.class, ""));
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
        User user = this.userRepository.searchByLoginBase(loginBase).orElseThrow(() -> new NotFoundException(User.class, ""));
        
        if (!this.passwordEncoder.matches(password, user.getPassword())) { return ErrorManager.def("incorrect password"); }
        
        APIKey apiKey = new APIKey(user);
        this.apiKeyRepository.save(apiKey);
        
        try {
            return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.fromModel(user, UserDTO.class)), HeaderBuilder.build(ConfigDatas.API_KEY_HEADER, apiKey.getKey().toString()), HttpStatus.OK);
        } catch (JsonProcessingException ex) {
            this.apiKeyRepository.delete(apiKey);
            return ErrorManager.def();
        }
    }
    
    public ResponseEntity<String> logoutUser(String apiKey) {
        APIKey apiAuth = this.apiKeyRepository.findById(UUID.fromString(apiKey)).orElseThrow(() -> new NotFoundException(APIKey.class, ""));
        
        this.apiKeyRepository.delete(apiAuth);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}