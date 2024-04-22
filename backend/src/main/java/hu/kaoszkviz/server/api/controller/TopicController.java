package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.dto.TopicDTO;
import hu.kaoszkviz.server.api.service.TopicService;
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
@RequestMapping("/topic")
public class TopicController {
    
    @Autowired
    private TopicService topicService;
    
    @GetMapping(name = "")
    public ResponseEntity<String> getTopics(@RequestParam(required = false, defaultValue = "") String searchString) {
        if (searchString.isBlank()) {
            return this.topicService.getTopics();
        }
        
        return this.topicService.searchTopic(searchString);
    }
    
    @PostMapping("")
    public ResponseEntity<String> addTopic(@RequestBody TopicDTO topic) {
        return this.topicService.addTopic(topic);
    }
    
    @DeleteMapping("")
    public ResponseEntity<String> deleteById(@RequestParam long id){
        return this.topicService.deleteById(id);
    }
}
