package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.config.ConfigDatas;
import hu.kaoszkviz.server.api.dto.UserDTO;
import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.exception.UnauthorizedException;
import hu.kaoszkviz.server.api.jsonview.JsonViewEnum;
import hu.kaoszkviz.server.api.jsonview.PrivateJsonView;
import hu.kaoszkviz.server.api.model.APIKey;
import hu.kaoszkviz.server.api.model.Media;
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
import hu.kaoszkviz.server.api.tools.Generator;
import hu.kaoszkviz.server.api.tools.HeaderBuilder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
    private CustomModelMapper customModelMapper;
    
    @Autowired
    private EmailSenderService emailSenderService;
    
    @Autowired
    private Generator generator;
    
    public ResponseEntity<String> addUser(UserDTO user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        User userToSave = new User();
        
        this.customModelMapper.fromDTO(user, userToSave);
        
        userToSave.setAdmin(false);
        userToSave.setStatus(User.Status.ACTIVE);
        userToSave.setProfilePicture(generator.getRandomProfilePicture());
        userToSave = this.userRepository.save(userToSave);
        
        if (userToSave != null) {
            return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.fromModel(userToSave, UserDTO.class), JsonViewEnum.PRIVATE_VIEW), HttpStatus.CREATED); //ErrorManager
        }
        
        return ErrorManager.def();
    }
    
    public ResponseEntity<String> getUserById(long id) {
        ApiKeyAuthentication auth = ApiKeyAuthentication.getAuth();
        User user = this.userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
        boolean isPrivateView = auth.getPrincipal().getId() == user.getId() || user.isAdmin();
        
        if ((!auth.getPrincipal().isAdmin() && user.getStatus() == User.Status.DELETED)) {
            throw new NotFoundException(User.class, id);
        }
        
        return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.fromModel(user, UserDTO.class), isPrivateView ? JsonViewEnum.PRIVATE_VIEW : JsonViewEnum.PUBLIC_VIEW), HttpStatus.OK);
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
        
        Optional<User> rootUser = this.userRepository.findById(0l);
        if (rootUser.isPresent()) {
            users.remove(rootUser.get());
        }
        
        try {
            return new ResponseEntity<>(Converter.ModelListToJsonString(this.customModelMapper.fromModelList(users, UserDTO.class), JsonViewEnum.PUBLIC_VIEW), HttpStatus.OK);
        } catch (Exception ex) {
            return ErrorManager.def();
        }
    }
    
    public ResponseEntity<String> deleteById(long id) {
        ApiKeyAuthentication auth = ApiKeyAuthentication.getAuth();
        
        if (!auth.getPrincipal().isAdmin() && auth.getPrincipal().getId() != id) { return ErrorManager.unauth(); }
        
        User user = this.userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
        
        user.setStatus(User.Status.DELETED);

        if (this.userRepository.save(user) != null) {
            return new ResponseEntity<>("", HttpStatus.OK); //ErrorManager
        }
        
        return ErrorManager.def();
    }

    
    public ResponseEntity<String> updateUser(UserDTO userDto) {
        User authUser = ApiKeyAuthentication.getAuth().getPrincipal();
        
        if (userDto.getId() < 0) {
            return ErrorManager.notFound("id");
        }
        
        if (!authUser.isAdmin() && authUser.getId() != userDto.getId()) { return ErrorManager.unauth(); }
        
        User user = this.userRepository.findById(userDto.getId()).orElseThrow(() -> new NotFoundException(User.class, userDto.getId()));
            
        this.customModelMapper.updateFromDTO(userDto, user);
        user = this.userRepository.save(user);
        if (user != null) {
            return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.fromModel(user, UserDTO.class), JsonViewEnum.PRIVATE_VIEW), HttpStatus.OK); //ErrorManager
        }
        
        return ErrorManager.def();
    }
    
    public ResponseEntity<String> generatePasswordResetToken(String userEmail) {
        User user = this.userRepository.getUserByEmail(userEmail).orElseThrow(() -> new NotFoundException(User.class, ""));

        PasswordResetToken passwordResetToken = new PasswordResetToken(user);
        passwordResetToken = this.passwordResetTokenRepository.save(passwordResetToken);

        HttpHeaders headers = new HttpHeaders();
        //headers.add(ConfigDatas.PASSWORD_RESET_TOKEN_HEADER, passwordResetToken.getKey().toString());
        
        this.emailSenderService.sendResetPasswordToken(passwordResetToken);
        return new ResponseEntity<>("", headers, HttpStatus.OK); //ErrorManager
    }
    
    public ResponseEntity<String> resetPassword(UUID resetKey,String newPassword) {
        PasswordResetToken token = this.passwordResetTokenRepository.getByKey(resetKey).orElseThrow(()-> new NotFoundException(PasswordResetToken.class, ""));
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {throw new UnauthorizedException("token expired"); }
        
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
        
        return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.fromModel(user, UserDTO.class), JsonViewEnum.PRIVATE_VIEW), HeaderBuilder.build(ConfigDatas.API_KEY_HEADER, apiKey.getKey().toString()), HttpStatus.OK);
    }
    
    public ResponseEntity<String> logoutUser(String apiKey) {
        APIKey apiAuth = this.apiKeyRepository.findById(UUID.fromString(apiKey)).orElseThrow(() -> new NotFoundException(APIKey.class, ""));
        
        this.apiKeyRepository.delete(apiAuth);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}