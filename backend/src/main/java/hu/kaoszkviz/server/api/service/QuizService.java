package hu.kaoszkviz.server.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.kaoszkviz.server.api.dto.QuizDTO;
import hu.kaoszkviz.server.api.model.Quiz;
import hu.kaoszkviz.server.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.QuizRepository;
import hu.kaoszkviz.server.api.repository.UserRepository;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.CustomModelMapper;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class QuizService {
    
    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CustomModelMapper customModelMapper;
    
     
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
            return new ResponseEntity<>(Converter.ModelTableToJsonString(this.customModelMapper.fromModelList(quizs, QuizDTO.class)), HttpStatus.OK);
        } catch (JsonProcessingException ex) {
            return ErrorManager.def();
        }
    }
    
    public ResponseEntity<String> addQuiz(QuizDTO quizDTO) {
        if (quizDTO.getOwnerId() == -1) { quizDTO.setOwnerId(ApiKeyAuthentication.getAuth().get().getPrincipal().getId()); }
        
        Optional<User> user = this.userRepository.findById(quizDTO.getOwnerId());
        if (user.isEmpty()) { return ErrorManager.notFound("user"); }
        
        Quiz quiz = customModelMapper.map(quizDTO, Quiz.class);
        
        quiz.setStatus(Quiz.Status.ACTIVE);
        
        if (this.quizRepository.save(quiz) != null) {
            return new ResponseEntity<>("ok", HttpStatus.CREATED); //ErrorManager
        }
        
        return new ResponseEntity<>("failed to save", HttpStatus.BAD_REQUEST); //ErrorManager
    }
    
    public ResponseEntity<String> deleteById(long id){
        Optional<ApiKeyAuthentication> authOptional = ApiKeyAuthentication.getAuth();
        
        if (authOptional.isEmpty()) { return ErrorManager.unauth(); }
        
        ApiKeyAuthentication auth = authOptional.get();
        
        Optional<Quiz> quizOptional = this.quizRepository.findById(id);
        
        if (quizOptional.isEmpty()) { return ErrorManager.notFound("quiz"); }
        
        Quiz quiz = quizOptional.get();
        
        if (auth.getPrincipal().isAdmin() && quiz.getOwner().getId() != auth.getPrincipal().getId()) { return ErrorManager.unauth(); }
        
        quiz.setStatus(Quiz.Status.DELETED);
        this.quizRepository.save(quiz);
        
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
    
    public ResponseEntity<String> updateQuiz(QuizDTO quiz) {
        Optional<Quiz> OptionalQuiz = this.quizRepository.findById(quiz.getId());
        if(OptionalQuiz.isPresent()){
            Quiz quizToUpdate = OptionalQuiz.get();
            this.customModelMapper.updateFromDTO(quiz, quizToUpdate);
            this.quizRepository.save(quizToUpdate);
            return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
        } else {
            return ErrorManager.notFound("quiz");
        }
    }
}
