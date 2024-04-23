package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.dto.QuestionDTO;
import hu.kaoszkviz.server.api.exception.InternalServerErrorException;
import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.exception.UnauthorizedException;
import hu.kaoszkviz.server.api.model.Answer;
import hu.kaoszkviz.server.api.model.Question;
import hu.kaoszkviz.server.api.model.Quiz;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.QuestionRepository;
import hu.kaoszkviz.server.api.repository.QuizRepository;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.CustomModelMapper;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuizRepository quizRepository;    
    
    @Autowired
    private AnswerRepository answerRepository;
    
    
    @Autowired
    private CustomModelMapper customModelMapper;
    
    public ResponseEntity<String> getQuestionsByQuizId(Long quizId) {
        Optional<Quiz> quizOptional = this.quizRepository.findById(quizId);
        if(!quizOptional.isPresent()){
            return ErrorManager.notFound("quiz");
        }
        ApiKeyAuthentication auth = ApiKeyAuthentication.getAuth();
        Quiz quiz = quizOptional.get();
        
        if (!auth.getPrincipal().isAdmin() && quiz.getOwner().getId() != auth.getPrincipal().getId()) { return ErrorManager.unauth(); }
        
        List<Question> questions = this.questionRepository.searchQuestionByQuizId(quizId);
        
        if(questions.isEmpty()) {return ErrorManager.notFound("questions");}

        return new ResponseEntity<>(Converter.ModelListToJsonString(this.customModelMapper.fromModelList(questions, QuestionDTO.class)), HttpStatus.OK); //ErrorManager
    }
    
    public ResponseEntity<String> addQuestion(QuestionDTO questionDTO) {
        Question question = this.customModelMapper.fromDTO(questionDTO, Question.class);
        User authUser = ApiKeyAuthentication.getAuth().getPrincipal();
        
        if (question.getQuiz().getOwner().getId() != authUser.getId()) { throw new UnauthorizedException(); }
        
        question = this.questionRepository.save(question);
        
        if (question == null) { throw new InternalServerErrorException(); }
        
        return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.fromModel(question, QuestionDTO.class)), HttpStatus.OK);
    }
    
    public ResponseEntity<String> deleteQuestion(Long id) {
        Question question = this.questionRepository.findById(id).orElseThrow(() -> new NotFoundException(Question.class, id));
        
        for (Answer answer : question.getAnswers()) {
            this.answerRepository.delete(answer);
        }

        this.questionRepository.delete(question);
        
        return new ResponseEntity<>("", HttpStatus.OK);
    }
    
    public ResponseEntity<String> updateQuestion(QuestionDTO questionDTO) {
        Question question = this.questionRepository.findById(questionDTO.getId()).orElseThrow(() -> new NotFoundException(Question.class, questionDTO.getId()));
        
        this.customModelMapper.updateFromDTO(questionDTO, question);
        this.questionRepository.save(question);
        
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
