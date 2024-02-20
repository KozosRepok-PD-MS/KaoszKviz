package hu.kaoszkviz.server.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.kaoszkviz.server.api.jsonview.JsonViewEnum;
import hu.kaoszkviz.server.api.model.Quiz;
import hu.kaoszkviz.server.api.model.QuizTopic;
import hu.kaoszkviz.server.api.model.QuizTopicId;
import hu.kaoszkviz.server.api.model.Topic;
import hu.kaoszkviz.server.api.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.QuizTopicRepository;
import hu.kaoszkviz.server.api.repository.TopicRepository;
import hu.kaoszkviz.server.api.tools.Converter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class QuizTopicService {
    
    @Autowired
    private QuizTopicRepository quizTopicRepository;
    
    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private TopicRepository topicRepository;
    
    public ResponseEntity<String> getQuizTopics(String stringId){
        List<QuizTopic> quizTopics = new ArrayList<>();
        try{
            Long longId = stringId == null ? null : Long.valueOf(stringId);
            quizTopics = this.getQuizTopics(longId);
        } catch(NumberFormatException ex){
            return new ResponseEntity<>("NAN", HttpStatus.BAD_REQUEST);
        }
        try{
            return new ResponseEntity<>(Converter.ModelTableToJsonString(quizTopics, JsonViewEnum.PUBLIC_VIEW), HttpStatus.OK);
        } catch (JsonProcessingException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public List<QuizTopic> getQuizTopics(Long id){
        return this.quizTopicRepository.searchByQuizId(id);
    }
    
    public ResponseEntity<String> addQuizTopic(HashMap<String, String> datas){
        Optional<Quiz> quiz = this.quizRepository.findById(Long.valueOf(datas.get("quizId")));
        Optional<Topic> topic = this.topicRepository.findById(Long.valueOf(datas.get("topicId")));
        if (!quiz.isPresent() || !topic.isPresent()) {
            return new ResponseEntity<>("failed to find quiz or topic", HttpStatus.BAD_REQUEST);
        }
        try{
            QuizTopic quizTopic = new QuizTopic();
            
            quizTopic.setQuiz(quiz.get());
            quizTopic.setTopic(topic.get());
            return this.addQuizTopic(quizTopic);
        } catch (Exception ex){
            return new ResponseEntity<>("failed to save", HttpStatus.BAD_REQUEST);
        }
    }
    
    public ResponseEntity<String> addQuizTopic(QuizTopic quizTopic){
        if (this.quizTopicRepository.save(quizTopic) == null) {
            return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
        } else{
            return new ResponseEntity<>("ok", HttpStatus.CREATED);
        }
    }
    
    public ResponseEntity<String> deleteQuizTopic(Long quizId, Long topicId){
        Optional<Quiz> quiz = this.quizRepository.findById(quizId);
        Optional<Topic> topic = this.topicRepository.findById(topicId);
        if (!quiz.isPresent() ) {
            return new ResponseEntity<>("quiz not found", HttpStatus.BAD_REQUEST);
        } else if(!topic.isPresent()){
            return new ResponseEntity<>("topic not found", HttpStatus.BAD_REQUEST);
        } else{
            QuizTopicId quizTopicId = new QuizTopicId(quiz.get(), topic.get());
            if (this.quizTopicRepository.findById(quizTopicId).isPresent()) {
                this.quizTopicRepository.deleteById(quizTopicId);
                return new ResponseEntity<>("ok", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("quizTopic not found", HttpStatus.BAD_REQUEST);
            }
        }
    }
}
