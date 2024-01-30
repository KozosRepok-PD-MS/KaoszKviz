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
import java.util.Optional;
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
                    return new ResponseEntity<>("NAN", HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
        
        try {
            return new ResponseEntity<>(Converter.ModelTableToJsonString(users), HttpStatus.OK);
        } catch (JsonProcessingException ex) {
            return new ResponseEntity<>("failed to get data", HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>("failed to delete", HttpStatus.BAD_REQUEST);
        }
    }
    
    public ResponseEntity<String> deleteById(long id) {
        if (this.userRepository.findById(id).isPresent()) {
            this.userRepository.deleteById(id);
            return new ResponseEntity<>("ok", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
    }
}
