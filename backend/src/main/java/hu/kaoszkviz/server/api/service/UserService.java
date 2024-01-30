package hu.kaoszkviz.server.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.kaoszkviz.server.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.UserRepository;
import hu.kaoszkviz.server.api.tools.Converter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public ResponseEntity<String> addUser(User user) {
        if (this.userRepository.save(user) != null) {
            return new ResponseEntity<>("ok", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("failed to save", HttpStatus.BAD_REQUEST);
    }
    
    public ResponseEntity<String> getUsers(HashMap<String, String> requestBody) {
        
        List<User> users = this.userRepository.findAll();
        
        try {
            return new ResponseEntity<>(Converter.ModelTableToJsonString(users), HttpStatus.OK);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<User> getUsersByName(String searchName) {
        if (searchName == null || searchName.isBlank()) {
            List<User> users = this.userRepository.searchByName(searchName);
            
            if (!users.isEmpty()) {
                return users;
            }
        }
        return new ArrayList<>();
    }
}
