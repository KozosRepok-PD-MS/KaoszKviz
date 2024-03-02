package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.service.QuestionService;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
public class QuestionController {
    
    @Autowired
    private QuestionService questionService;
    
    @GetMapping
    public ResponseEntity<String> getQuestionsByQuizId(@RequestBody HashMap<String, String> requestBody){
        String key = "quizId";
        String quizIdStr = requestBody.get(key);
        if (quizIdStr == null || quizIdStr.isBlank()) { return ErrorManager.notFound(key);}
        
        long quizId;
        
        try {
            quizId = Long.parseLong(quizIdStr);
        } catch (NumberFormatException ex) {
            return ErrorManager.nan();
        }
        
        return this.questionService.getQuestionsByQuizId(quizId);
    }
    
    @PostMapping
    public ResponseEntity<String> addQuestion(@RequestBody HashMap<String, String> requeqtBody){
        return this.questionService.addQuestion(requeqtBody);
    }
    
    @DeleteMapping
    public ResponseEntity<String> deleteQuestion(@RequestBody HashMap<String, String> requestBody){
        return this.questionService.deleteQuestion(Long.valueOf(requestBody.get("id")));
    }
    
    @PutMapping
    public ResponseEntity<String> updateQuestion(@RequestBody HashMap<String, String> requestBody){
        return this.questionService.updateQuestion(requestBody);
    }
}
