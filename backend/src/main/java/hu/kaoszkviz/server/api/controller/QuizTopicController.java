package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.service.QuizTopicService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quiztopic")
public class QuizTopicController {
    
    @Autowired
    private QuizTopicService quizTopicService;
    
    @GetMapping(name = "")
    public ResponseEntity<String> getQuizTopics(@RequestParam long quizId) {
        return this.quizTopicService.getQuizTopics(quizId);
    }
    
    @PostMapping(name = "")
    public ResponseEntity<String> addQuizTopic(@RequestBody HashMap<String, String> requestBody){
        return this.quizTopicService.addQuizTopic(requestBody);
    }
    
    @DeleteMapping(name = "")
    public ResponseEntity<String> deleteQuizTopic(@RequestBody HashMap<String, String> requestBody){
        return this.quizTopicService.deleteQuizTopic(Long.valueOf(requestBody.get("quizId")), Long.valueOf(requestBody.get("topicId")));
    }
}
