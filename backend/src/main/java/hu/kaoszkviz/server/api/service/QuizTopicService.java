package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.dto.QuizTopicDTO;
import hu.kaoszkviz.server.api.dto.TopicDTO;
import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.exception.UnauthorizedException;
import hu.kaoszkviz.server.api.jsonview.JsonViewEnum;
import hu.kaoszkviz.server.api.model.QuizTopic;
import hu.kaoszkviz.server.api.model.QuizTopicId;
import hu.kaoszkviz.server.api.model.Topic;
import hu.kaoszkviz.server.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.QuizTopicRepository;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.CustomModelMapper;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class QuizTopicService {
    
    @Autowired
    private QuizTopicRepository quizTopicRepository;
    
    @Autowired
    private CustomModelMapper customModelMapper;
     
    public ResponseEntity<String> getQuizTopics(Long id){
        List<QuizTopic> quizTopics = this.quizTopicRepository.searchByQuizId(id);
        List<Topic> topics = new ArrayList<>();
        
        for (QuizTopic quizTopic : quizTopics) {
            topics.add(quizTopic.getTopic());
        }
        return new ResponseEntity<>(Converter.ModelListToJsonString(customModelMapper.fromModelList(topics, TopicDTO.class), JsonViewEnum.PUBLIC_VIEW), HttpStatus.OK); //ErrorManager
    }
    
    public ResponseEntity<String> addQuizTopic(QuizTopicDTO quizTopicDTO){
        QuizTopic quizTopic = new QuizTopic();
        this.customModelMapper.fromDTO(quizTopicDTO, quizTopic);
        User authUser = ApiKeyAuthentication.getAuth().getPrincipal();
        
        if (!authUser.isAdmin() && quizTopic.getQuiz().getOwner().getId() != authUser.getId()) { throw new UnauthorizedException(); }
        if (this.quizTopicRepository.findById(new QuizTopicId(quizTopic.getQuiz(), quizTopic.getTopic())).isPresent()) { throw new DataIntegrityViolationException("quiztopic"); }
        quizTopic = this.quizTopicRepository.save(quizTopic);
        
        if(quizTopic != null) {
            return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.fromModel(quizTopic, QuizTopicDTO.class)), HttpStatus.OK);
        } 
        
        return ErrorManager.def();
    }
    
    public ResponseEntity<String> deleteQuizTopic(QuizTopicDTO quizTopicDTO) {
        QuizTopic quizTopicToDelete = new QuizTopic();
        this.customModelMapper.fromDTO(quizTopicDTO, quizTopicToDelete);
        User authUser = ApiKeyAuthentication.getAuth().getPrincipal();
        
        if (!authUser.isAdmin() && quizTopicToDelete.getQuiz().getOwner().getId() != authUser.getId()) { throw new UnauthorizedException(); }
        
        QuizTopicId id = new QuizTopicId(quizTopicToDelete.getQuiz(), quizTopicToDelete.getTopic());
        
        if (this.quizTopicRepository.findById(id).isEmpty()) {throw new NotFoundException(QuizTopic.class, id); }
        
        this.quizTopicRepository.deleteById(id);
        return new ResponseEntity<>("", HttpStatus.OK); //ErrorManager
    }
}
