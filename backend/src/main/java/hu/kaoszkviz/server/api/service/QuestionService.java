package hu.kaoszkviz.server.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.kaoszkviz.server.api.model.Answer;
import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.MediaId;
import hu.kaoszkviz.server.api.model.Question;
import hu.kaoszkviz.server.api.model.Quiz;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.repository.AnswerRepository;
import hu.kaoszkviz.server.api.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.QuestionRepository;
import hu.kaoszkviz.server.api.repository.QuestionTypeRepository;
import hu.kaoszkviz.server.api.repository.QuizRepository;
import hu.kaoszkviz.server.api.repository.UserRepository;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.HashMap;
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
    private MediaRepository mediaRepository;
    
    @Autowired
    private QuestionTypeRepository questionTypeRepository;
    
    @Autowired
    private AnswerRepository answerRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public ResponseEntity<String> getQuestionsByQuizId(Long quizId){
        Optional<Quiz> quizOptional = this.quizRepository.findById(quizId);
        if(!quizOptional.isPresent()){
            return ErrorManager.notFound("quiz");
        }
        Optional<ApiKeyAuthentication> authOptional = ApiKeyAuthentication.getAuth();
        if (authOptional.isEmpty()) { return ErrorManager.unauth(); }
        
        ApiKeyAuthentication auth = authOptional.get();
        Quiz quiz = quizOptional.get();
        
        if (!auth.getPrincipal().isAdmin() && quiz.getOwner().getId() != auth.getPrincipal().getId()) { return ErrorManager.unauth(); }
        
        List<Question> questions = this.questionRepository.searchQuestionByQuizId(quizId);
        
        if(questions.isEmpty()) {return ErrorManager.notFound("questions");}

        try {
            return new ResponseEntity<>(Converter.ModelTableToJsonString(questions), HttpStatus.OK); //ErrorManager
        } catch (JsonProcessingException ex) {
            return ErrorManager.def(ex.getLocalizedMessage());
        }
    }
    
    public ResponseEntity<String> addQuestion(HashMap<String, String> datas){
        Optional<Quiz> quiz = this.quizRepository.findById(Long.valueOf(datas.get("quizId")));
        if (!quiz.isPresent()) {
            return ErrorManager.notFound("quiz");
        }
        try{
            Question question = new Question();
            question.setQuiz(quiz.get());
            question.setQuestion(datas.get("question"));
            Optional user = this.userRepository.findById(Long.valueOf(datas.get("mediaContentOwner")));
            
            if (user.isPresent()) {
                MediaId  mediaId = new MediaId((User) user.get(), datas.get("mediaContentName"));
                Optional<Media> media = this.mediaRepository.findById(mediaId);
                
                if (!media.isPresent()) {
                    return ErrorManager.notFound("media");
                }
                question.setMediaContent(media.get());
                question.setTimeToAnswer((byte) Integer.parseInt(datas.get("timeToAnswer")));
                question.setQuestionType(this.questionTypeRepository.findById(datas.get("questionType")).get());
                return this.addQuestion(question);
            } else {
                return ErrorManager.notFound("user");
            }
            
            
        } catch(Exception ex){
            return ErrorManager.def(ex.getLocalizedMessage());
        }
    }
    
    private ResponseEntity<String> addQuestion(Question question){
        if (this.questionRepository.save(question) == null) {
            return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST); //ErrorManager
        } else{
            this.questionRepository.save(question);
            return new ResponseEntity<>("ok", HttpStatus.CREATED); //ErrorManager
        }
    }
    
    public ResponseEntity<String> deleteQuestion(Long id){
        if (this.questionRepository.findById(id).isPresent()) {
            if (this.questionRepository.findById(id).get().getAnswers().isEmpty()) {
                this.questionRepository.deleteById(id);
                return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
            }else{
                List<Answer> answers = this.questionRepository.findById(id).get().getAnswers();
                for (Answer answer : answers) {
                    this.answerRepository.deleteByQuestionIdAndOrdinalNumber(answer.getQuestion().getId(), answer.getOrdinalNumber());
                }
                this.questionRepository.deleteById(id);
                return new ResponseEntity<>("ok, deleted answers", HttpStatus.OK); //ErrorManager
            }
        } else {
            return ErrorManager.notFound("question");
        }
    }
    
    public ResponseEntity<String> updateQuestion(HashMap<String, String> datas){
        if (this.questionRepository.findById(Long.valueOf(datas.get("id"))).isPresent()) {
            Question updatedQuestion = this.questionRepository.findById(Long.valueOf(datas.get("id"))).get();
            updatedQuestion.setQuestion(datas.get("question"));
            Optional user = this.userRepository.findById(Long.valueOf(datas.get("mediaContentOwner")));
            
            if (user.isPresent()) {
                MediaId  mediaId = new MediaId((User) user.get(), datas.get("mediaContentName"));
                Optional<Media> media = this.mediaRepository.findById(mediaId);
                if (!media.isPresent()) {
                    return ErrorManager.notFound("media");
                }
                updatedQuestion.setMediaContent(media.get());
                updatedQuestion.setTimeToAnswer((byte) Integer.parseInt(datas.get("timeToAnswer")));
                this.questionRepository.save(updatedQuestion);
                return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
            } else {
                return ErrorManager.notFound("user");
            }
        } else{
            return ErrorManager.notFound("question");
        }
    }
}
