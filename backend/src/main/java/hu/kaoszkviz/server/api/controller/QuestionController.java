package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.service.QuestionService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
public class QuestionController {
    
    @Autowired
    private QuestionService questionService;
    
    @PostMapping(name = "")
    public ResponseEntity<String> addQuestion(@RequestBody HashMap<String, String> requeqtBody){
        return this.questionService.addQuestion(requeqtBody);
    }
}
