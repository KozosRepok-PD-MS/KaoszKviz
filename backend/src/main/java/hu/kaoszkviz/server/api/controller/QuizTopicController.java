package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.service.QuizTopicService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quiztopic")
public class QuizTopicController {
    
    @Autowired
    private QuizTopicService quizTopicService;
    
    @GetMapping(name = "")
    public ResponseEntity<String> getQuizTopics(@RequestBody HashMap<String, String> requestBody){
        return this.quizTopicService.getQuizTopics(requestBody.get("quizId"));
    }
    
    @PostMapping(name = "")
    public ResponseEntity<String> addQuizTopic(@RequestBody HashMap<String, String> requestBody){
        return this.quizTopicService.addQuizTopic(requestBody);
    }
}
