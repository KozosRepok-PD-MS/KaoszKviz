package hu.kaoszkviz.server.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.kaoszkviz.server.api.jsonview.JsonViewEnum;
import hu.kaoszkviz.server.api.model.Quiz;
import hu.kaoszkviz.server.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.QuizRepository;
import hu.kaoszkviz.server.api.repository.UserRepository;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class QuizService {
    
    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private UserRepository userRepository;
    
     
    public ResponseEntity<String> getAllQuiz() {
        List<Quiz> quizs = this.quizRepository.findAll();
        
        return this.processQuizList(quizs);
    }
    
    public ResponseEntity<String> getAllQuiz(long ownerId) {
        List<Quiz> quizs = this.quizRepository.findAllByOwnerId(ownerId);
        return this.processQuizList(quizs);
    }
    
    public ResponseEntity<String> getAllPublicQuiz() {
        List<Quiz> quizs = this.quizRepository.findAllPublic();
        
        return this.processQuizList(quizs);
    }
    
    public ResponseEntity<String> getAllPublicQuiz(long ownerId) {
        List<Quiz> quizs = this.quizRepository.findAllPublicByOwnerId(ownerId);
        
        return this.processQuizList(quizs);
    }
    
    private ResponseEntity<String> processQuizList(List<Quiz> quizs) {
        try {
            return new ResponseEntity<>(Converter.ModelTableToJsonString(quizs), HttpStatus.OK);
        } catch (JsonProcessingException ex) {
            return ErrorManager.def();
        }
    }
    
    public ResponseEntity<String> addQuiz(HashMap<String, String> quizDatas) {
        Optional<User> user = this.userRepository.findById(Long.valueOf(quizDatas.get("ownerId")));
        if (!user.isPresent()) {
            return ErrorManager.notFound("user");
        }
        try{
            Quiz quiz = new Quiz();
            
            quiz.setOwner(user.get());
            quiz.setTitle(quizDatas.get("title"));
            quiz.setDescription(quizDatas.get("description"));
            quiz.setPublic(Boolean.getBoolean(quizDatas.get("isPublic")));
            quiz.setShortAccessibleName(quizDatas.get("shortAccessibleName"));
            return this.addQuiz(quiz);
        } catch (Exception ex) {
            return new ResponseEntity<>("failed to save", HttpStatus.BAD_REQUEST); //ErrorManager
        }
        
    }
    
    public ResponseEntity<String> addQuiz(Quiz quiz){
        if(this.quizRepository.save(quiz) == null){
            return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST); //ErrorManager
        } else{
            return new ResponseEntity<>("ok", HttpStatus.CREATED); //ErrorManager
        }
    }
    
    public ResponseEntity<String> deleteById(String idString){
        try{
            return this.deleteById(Long.valueOf(idString));
        } catch(NumberFormatException ex){
            return ErrorManager.def(ex.getLocalizedMessage());
        }
    }
    
    public ResponseEntity<String> deleteById(Long id){
        if (this.quizRepository.findById(id).isPresent()) {
            this.quizRepository.deleteById(id);
            return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
        } else {
            return ErrorManager.notFound("quiz");
        }
    }
    
    public ResponseEntity<String> updateQuiz(Quiz quiz) {
        if(this.quizRepository.findById(quiz.getId()).isPresent()){
            Quiz updateQuiz =this.quizRepository.findById(quiz.getId()).get();
            updateQuiz.setDescription(quiz.getDescription());
            updateQuiz.setMediaContent(quiz.getMediaContent());
            updateQuiz.setPublic(quiz.isPublic());
            updateQuiz.setShortAccessibleName(quiz.getShortAccessibleName());
            updateQuiz.setTitle(quiz.getTitle());
            updateQuiz.setTopics(quiz.getTopics());
            this.quizRepository.save(updateQuiz);
            return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
        } else {
            return ErrorManager.notFound("quiz");
        }
    }
}
