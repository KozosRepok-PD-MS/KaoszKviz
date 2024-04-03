package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.model.Quiz;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import hu.kaoszkviz.server.api.service.QuizService;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.HashMap;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quiz")
public class QuizController {
    
    @Autowired
    private QuizService quizService;
    
    @GetMapping(name = "")
    public ResponseEntity<String> getQuizs(@RequestParam(required = false, defaultValue = "-1") long ownerId) {
        Optional<ApiKeyAuthentication> authOptional = ApiKeyAuthentication.getAuth();
        
        if (authOptional.isEmpty()) { return ErrorManager.unauth(); }
        ApiKeyAuthentication auth = authOptional.get();
        
        if (ownerId == -1) {
            if (auth.getPrincipal().isAdmin()) {
                return this.quizService.getAllQuiz();
            } else {
                return this.quizService.getAllPublicQuiz();
            }
        } else {
            if (auth.getPrincipal().isAdmin() || auth.getPrincipal().getId() == ownerId) {
                return this.quizService.getAllQuiz(ownerId);
            } else {
                return this.quizService.getAllPublicQuiz(ownerId);
            }
        }
    }
    
    @PostMapping(name = "")
    public ResponseEntity<String> addQuiz(@RequestBody HashMap<String, String> requestBody){
        return this.quizService.addQuiz(requestBody);
    }
    
    @DeleteMapping("")
    public ResponseEntity<String> deleteById(@RequestBody HashMap<String, String> requestBody){
        return this.quizService.deleteById(requestBody.get("id"));
    }
    
    @PutMapping("")
    public ResponseEntity<String> updateQuiz(@RequestBody Quiz quiz){
        return this.quizService.updateQuiz(quiz);
    }
}
