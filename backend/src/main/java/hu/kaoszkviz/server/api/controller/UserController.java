
package hu.kaoszkviz.server.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hu.kaoszkviz.server.api.jsonview.PublicJsonView;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.service.UserService;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @PostMapping("")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        return this.userService.addUser(user);
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
