
package hu.kaoszkviz.server.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hu.kaoszkviz.server.api.config.ConfigDatas;
import hu.kaoszkviz.server.api.jsonview.PublicJsonView;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import hu.kaoszkviz.server.api.service.UserService;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.HashMap;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    UserService userService;
    
    @PostMapping(value = "")
    public ResponseEntity<String> addUser(@RequestBody HashMap<String, String> requestBody) {
        User user = new User();
        if (requestBody.get("username") == null) {
            return ErrorManager.notFound("username");
        }
        if(requestBody.get("email") == null){
            return ErrorManager.notFound("email");
        }
        if(requestBody.get("password") == null){
            return ErrorManager.notFound("password");
        } 
        user.setUsername(requestBody.get("username"));
        user.setEmail(requestBody.get("email"));
        user.setPassword(requestBody.get("password"));
        
        return this.userService.addUser(user);
    }
    
    @PostMapping(value = "/resetpassword")
    public ResponseEntity<String> generatePasswordResetToken(@RequestBody HashMap<String, String> requestBody){
        return this.userService.generatePasswordResetToken(Long.valueOf(requestBody.get("userId")));
    }
    
    @GetMapping(name = "", produces = "application/json")
    @JsonView(PublicJsonView.class)
    public ResponseEntity<String> getUsers(@RequestBody(required = false) HashMap<String, String> requestBody) {
        return this.userService.getUsers(requestBody);
    }
    
    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestBody HashMap<String, String> requestBody) {
        return this.userService.deleteById(requestBody);
    }
    
    @PutMapping(name = "")
    public ResponseEntity<String> updateUser(@RequestBody HashMap<String, String> requestBody){
        return this.userService.updateUser(requestBody);
    }
    
    @PostMapping(value = "/login")
    public ResponseEntity<String> loginUser(@RequestBody(required = false) HashMap<String, String> requestBody) {
        if (requestBody == null || requestBody.isEmpty()) { return ErrorManager.def(); }
        
        String loginBase = requestBody.get("loginBase");
        String password = requestBody.get("password");
        
        if (loginBase == null || password == null) { return ErrorManager.def("login or password not found"); }
        
        return this.userService.loginUser(loginBase, password);
    }
    
    @PostMapping(value = "/logout")
    public ResponseEntity<String> logoutUser() { //@RequestHeader(required = false, name = ConfigDatas.API_KEY_HEADER) String apiKey
        Optional<ApiKeyAuthentication> auth = ApiKeyAuthentication.getAuth();
        
        if (auth.isEmpty()) { return ErrorManager.def(); }
        
        return this.userService.logoutUser(auth.get().getCredentials());
    }
}
