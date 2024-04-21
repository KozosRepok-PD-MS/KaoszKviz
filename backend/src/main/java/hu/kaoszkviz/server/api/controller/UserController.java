
package hu.kaoszkviz.server.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import hu.kaoszkviz.server.api.config.ConfigDatas;
import hu.kaoszkviz.server.api.dto.UserDTO;
import hu.kaoszkviz.server.api.jsonview.PublicJsonView;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import hu.kaoszkviz.server.api.service.UserService;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    UserService userService;
    
    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody(required = false) UserDTO user) throws JsonProcessingException {
        if (user == null) { return ErrorManager.notFound("User"); }
        return this.userService.addUser(user);
    }
    
    @PostMapping(value = "/resetpassword")
    public ResponseEntity<String> resetPassword(@RequestBody(required = false) HashMap<String, String> requestBody) {
       
        String userEmail = requestBody.get("email"); 
        if (userEmail == null) { return ErrorManager.notFound("email"); }
        return this.userService.generatePasswordResetToken(userEmail);
    }
    
    @PostMapping(value = "/changepassword")
    public ResponseEntity<String> changePassword(@RequestHeader HttpHeaders headers, @RequestBody(required = false) HashMap<String, String> requestBody) {
        if (headers.containsKey(ConfigDatas.PASSWORD_RESET_TOKEN_HEADER)) {
            UUID resetKey= UUID.fromString(headers.get(ConfigDatas.PASSWORD_RESET_TOKEN_HEADER).get(0));
            
            String newPassword = requestBody.get("newPassword");
            if (newPassword == null) { return ErrorManager.notFound("newPassword"); }
            return this.userService.resetPassword(resetKey, newPassword);
        }
        
        return ErrorManager.notFound("password reset token");
    }
    
    @GetMapping(produces = "application/json")
    @JsonView(PublicJsonView.class)
    public ResponseEntity<String> getUsers(
            @RequestParam(defaultValue = "-1", required = false) long userId,
            @RequestParam(defaultValue = "", required = false) String searchName) {
        
        if (userId == -1 && searchName.isEmpty()) {
            return this.userService.getUsers();
        }
        
        if (userId != -1) {
            return this.userService.getUserById(userId);
        } else {
            return this.userService.getUsersByName(searchName);
        }
    }
    
    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestParam long userId) {
        return this.userService.deleteById(userId);
    }
    
    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDto){
        return this.userService.updateUser(userDto);
    }
    
    @PostMapping(value = "/login")
    public ResponseEntity<String> loginUser(@RequestBody(required = false) HashMap<String, String> requestBody) {
        if (requestBody == null || requestBody.isEmpty()) { return ErrorManager.def(); }
        
        String loginBase = requestBody.get("loginBase");
        if (loginBase == null) { return ErrorManager.notFound("login"); }
        
        String password = requestBody.get("password");
        if (password == null) { return ErrorManager.notFound("password"); }
        
        return this.userService.loginUser(loginBase, password);
    }
    
    @PostMapping(value = "/logout")
    public ResponseEntity<String> logoutUser() { //@RequestHeader(required = false, name = ConfigDatas.API_KEY_HEADER) String apiKey 
        return this.userService.logoutUser(ApiKeyAuthentication.getAuth().getCredentials());
    }
}
