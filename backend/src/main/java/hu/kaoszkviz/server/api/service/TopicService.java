package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.dto.TopicDTO;
import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.exception.UnauthorizedException;
import hu.kaoszkviz.server.api.model.QuizTopic;
import hu.kaoszkviz.server.api.model.QuizTopicId;
import hu.kaoszkviz.server.api.model.Topic;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.repository.QuizTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.TopicRepository;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.CustomModelMapper;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class TopicService {
    
    @Autowired
    private TopicRepository topicRepository;
    
    @Autowired
    private QuizTopicRepository quizTopicRepository;
    
    @Autowired
    private CustomModelMapper customModelMapper;
    
    
    public ResponseEntity<String> getTopics(){
        return this.processList(this.topicRepository.findAll());
    }
    
    public ResponseEntity<String> searchTopic(String search) {
        return this.processList(this.topicRepository.searchBy(search));
    }
    
    public ResponseEntity<String> processList(List<Topic> topics) {
        if (topics.isEmpty()) { throw new NotFoundException(Topic.class, ""); }
        
        return new ResponseEntity<>(Converter.ModelListToJsonString(this.customModelMapper.fromModelList(topics, TopicDTO.class)), HttpStatus.OK);
    }
    
    public ResponseEntity<String> deleteById(Long id){
        Topic topicToDelete = this.topicRepository.findById(id).orElseThrow(() -> new NotFoundException(Topic.class, id) );
        User authUser = ApiKeyAuthentication.getAuth().getPrincipal();
        
        if (!authUser.isAdmin()) { throw new UnauthorizedException(); }
        
        for (QuizTopic quizTopic : topicToDelete.getQuizTopics()) {
            this.quizTopicRepository.deleteById(new QuizTopicId(quizTopic.getQuiz(), quizTopic.getTopic()));
        }
        
        this.topicRepository.delete(topicToDelete);
        return new ResponseEntity<>("", HttpStatus.OK); //ErrorManager
    }
    
    public ResponseEntity<String> addTopic(TopicDTO topicDTO) {
        Topic topic = new Topic();
        this.customModelMapper.fromDTO(topicDTO, topic);
        topic = this.topicRepository.save(topic);
        
        if (topic != null) {
            return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.fromModel(topic, TopicDTO.class)), HttpStatus.CREATED); //ErrorManager
        }
        return ErrorManager.def();
    }
}
