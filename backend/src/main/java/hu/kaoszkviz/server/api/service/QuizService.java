package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.dto.QuizDTO;
import hu.kaoszkviz.server.api.exception.InternalServerErrorException;
import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.exception.UnauthorizedException;
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
        return new ResponseEntity<>(Converter.ModelListToJsonString(this.customModelMapper.fromModelList(quizs, QuizDTO.class)), HttpStatus.OK);
    }
    
    public ResponseEntity<String> getQuizById(long id) {
        User authUser = ApiKeyAuthentication.getAuth().getPrincipal();
        Quiz quiz = this.quizRepository.findById(id).orElseThrow(() -> new NotFoundException(Quiz.class, id));
        if (quiz.getOwner().getId() != authUser.getId() && !quiz.isPublic() && !authUser.isAdmin()) { throw new UnauthorizedException(); }
        QuizDTO quizDTO = this.customModelMapper.fromModel(quiz, QuizDTO.class);
        
        return new ResponseEntity<>(Converter.ModelToJsonString(quizDTO), HttpStatus.OK);
    }
    
    public ResponseEntity<String> addQuiz(QuizDTO quizDTO) {
        quizDTO.setOwnerId(ApiKeyAuthentication.getAuth().getPrincipal().getId());
        
        Optional<User> user = this.userRepository.findById(quizDTO.getOwnerId());
        if (user.isEmpty()) { return ErrorManager.notFound("user"); }
        
        Quiz quiz = customModelMapper.fromDTO(quizDTO, Quiz.class);
        
        quiz.setStatus(Quiz.Status.ACTIVE);
        quiz = this.quizRepository.save(quiz);
        
        if (quiz == null) { return ErrorManager.def(); }
        
        return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.fromModel(quiz, QuizDTO.class)), HttpStatus.CREATED); //ErrorManager
    }
    
    public ResponseEntity<String> deleteById(long id) {
        ApiKeyAuthentication auth = ApiKeyAuthentication.getAuth();
        
        Optional<Quiz> quizOptional = this.quizRepository.findById(id);
        
        if (quizOptional.isEmpty()) { return ErrorManager.notFound("quiz"); }
        
        Quiz quiz = quizOptional.get();
        
        if (auth.getPrincipal().isAdmin() && quiz.getOwner().getId() != auth.getPrincipal().getId()) { return ErrorManager.unauth(); }
        if (quiz.getStatus() == Quiz.Status.DELETED) { throw new InternalServerErrorException(); }
        
        quiz.setStatus(Quiz.Status.DELETED);
        this.quizRepository.save(quiz);
        
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
    
    public ResponseEntity<String> updateQuiz(QuizDTO quizDTO) {
        Quiz quiz = this.quizRepository.findById(quizDTO.getId()).orElseThrow(() -> new NotFoundException(Quiz.class, quizDTO.getId()));
        User authUser = ApiKeyAuthentication.getAuth().getPrincipal();
        
        if (quiz.getOwner().getId() != authUser.getId()) { throw new UnauthorizedException(); }

        this.customModelMapper.updateFromDTO(quizDTO, quiz);
        quiz = this.quizRepository.save(quiz);
        
        if (quiz == null) { return ErrorManager.def(); }
        
        return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.fromModel(quiz, QuizDTO.class)), HttpStatus.OK); //ErrorManager
    }
}
