package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.TopicRepository;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class TopicService {
    
    @Autowired
    private TopicRepository topicRepository;
    
    public List<Topic> getTopics(String txt){
        if (txt == null || txt.isBlank()) {
            return this.topicRepository.findAll();
        } else {
            return this.topicRepository.searchBy(txt);
        }
    }
    
    public ResponseEntity<String> deleteById(String idString){
        try {
           return this.deleteById(Long.valueOf(idString));
        } catch(NumberFormatException ex) {
            return ErrorManager.def(ex.getLocalizedMessage());
        }
    }
    
    public ResponseEntity<String> deleteById(Long id){
        if (this.topicRepository.findById(id).isPresent()) {
            this.topicRepository.deleteById(id);
            return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
        }else {
            return ErrorManager.notFound("topic");
        }
    }
    
    public ResponseEntity<String> addTopic(Topic topic) {
        if (this.topicRepository.save(topic) == null) {
            return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST); //ErrorManager
        } else{
            return new ResponseEntity<>("ok", HttpStatus.CREATED); //ErrorManager
        }
    }
}
