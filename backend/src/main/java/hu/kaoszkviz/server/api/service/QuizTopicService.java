package hu.kaoszkviz.server.api.service;

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
import hu.kaoszkviz.server.api.tools.ErrorManager;
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
    
    public ResponseEntity<String> getQuizTopics(Long id){
        List<QuizTopic> quizTopics = this.quizTopicRepository.searchByQuizId(id);
        return new ResponseEntity<>(Converter.ModelListToJsonString(quizTopics, JsonViewEnum.PUBLIC_VIEW), HttpStatus.OK); //ErrorManager
    }
    
    public ResponseEntity<String> addQuizTopic(HashMap<String, String> datas){
        Optional<Quiz> quiz = this.quizRepository.findById(Long.valueOf(datas.get("quizId")));
        Optional<Topic> topic = this.topicRepository.findById(Long.valueOf(datas.get("topicId")));
        if (!quiz.isPresent() || !topic.isPresent()) {
            return ErrorManager.notFound("quiz or topic");
        }
        try{
            QuizTopic quizTopic = new QuizTopic();
            
            quizTopic.setQuiz(quiz.get());
            quizTopic.setTopic(topic.get());
            return this.addQuizTopic(quizTopic);
        } catch (Exception ex){
            return new ResponseEntity<>("failed to save", HttpStatus.BAD_REQUEST); //ErrorManager
        }
    }
    
    public ResponseEntity<String> addQuizTopic(QuizTopic quizTopic){
        if (this.quizTopicRepository.save(quizTopic) == null) {
            return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST); //ErrorManager
        } else{
            return new ResponseEntity<>("ok", HttpStatus.CREATED); //ErrorManager
        }
    }
    
    public ResponseEntity<String> deleteQuizTopic(Long quizId, Long topicId){
        Optional<Quiz> quiz = this.quizRepository.findById(quizId);
        Optional<Topic> topic = this.topicRepository.findById(topicId);
        if (!quiz.isPresent() ) {
            return ErrorManager.notFound("quiz");
        } else if(!topic.isPresent()){
            return ErrorManager.notFound("topic");
        } else{
            QuizTopicId quizTopicId = new QuizTopicId(quiz.get(), topic.get());
            if (this.quizTopicRepository.findById(quizTopicId).isPresent()) {
                this.quizTopicRepository.deleteById(quizTopicId);
                return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
            }else{
                return ErrorManager.notFound("quizTopic");
            }
        }
    }
}
