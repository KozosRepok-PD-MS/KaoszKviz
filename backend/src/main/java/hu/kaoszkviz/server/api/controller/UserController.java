
package hu.kaoszkviz.server.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hu.kaoszkviz.server.api.jsonview.PublicJsonView;
import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.service.UserService;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            return new ResponseEntity<String>("no username", HttpStatus.BAD_REQUEST);
        }
        if(requestBody.get("email") == null){
            return new ResponseEntity<String>("no email", HttpStatus.BAD_REQUEST);
        }
        if(requestBody.get("password") == null){
            return new ResponseEntity<String>("no password", HttpStatus.BAD_REQUEST);
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
}
