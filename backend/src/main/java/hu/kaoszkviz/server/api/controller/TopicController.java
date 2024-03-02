package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.model.Topic;
import hu.kaoszkviz.server.api.service.TopicService;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topic")
public class TopicController {
    
    @Autowired
    private TopicService topicService;
    
    @GetMapping(name = "")
    public List<Topic> getTopics(@RequestBody(required = false) HashMap<String, String> requestBody){
        return this.topicService.getTopics(requestBody.get("searchString"));
    }
    
    @PostMapping("")
    public ResponseEntity<String> addTopic(@RequestBody Topic topic) {
        return this.topicService.addTopic(topic);
    }
    
    @DeleteMapping("")
    public ResponseEntity<String> deleteById(@RequestBody HashMap<String, String> requestBody){
        return this.topicService.deleteById(requestBody.get("id"));
    }
}
