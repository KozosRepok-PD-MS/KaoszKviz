package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.model.Answer;
import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.Question;
import hu.kaoszkviz.server.api.model.Quiz;
import hu.kaoszkviz.server.api.repository.AnswerRepository;
import hu.kaoszkviz.server.api.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.QuestionRepository;
import hu.kaoszkviz.server.api.repository.QuestionTypeRepository;
import hu.kaoszkviz.server.api.repository.QuizRepository;
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
    
    public ResponseEntity<String> addQuestion(HashMap<String, String> datas){
        Optional<Quiz> quiz = this.quizRepository.findById(Long.valueOf(datas.get("quizId")));
        if (!quiz.isPresent()) {
            return new ResponseEntity<>("failed to find quiz", HttpStatus.BAD_REQUEST);
        }
        try{
            Question question = new Question();
            question.setQuiz(quiz.get());
            question.setQuestion(datas.get("question"));
            Media media = this.mediaRepository.searchMediaByOwnerIdAndFileName(Long.parseLong(datas.get("mediaContentOwner")), datas.get("mediaContentName"));
            if (media == null) {
                return new ResponseEntity<>("failed to find media", HttpStatus.BAD_REQUEST);
            }
            question.setMediaContent(media);
            question.setTimeToAnswer((byte) Integer.parseInt(datas.get("timeToAnswer")));
            question.setQuestionType(this.questionTypeRepository.findById(datas.get("questionType")).get());
            return this.addQuestion(question);
        } catch(Exception ex){
            return new ResponseEntity<>("%s".formatted(ex), HttpStatus.BAD_REQUEST);
        }
    }
    
    private ResponseEntity<String> addQuestion(Question question){
        if (this.questionRepository.save(question) == null) {
            return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
        } else{
            this.questionRepository.save(question);
            return new ResponseEntity<>("ok", HttpStatus.CREATED);
        }
    }
    
    public ResponseEntity<String> deleteQuestion(Long id){
        if (this.questionRepository.findById(id).isPresent()) {
            if (this.questionRepository.findById(id).get().getAnswers().isEmpty()) {
                this.questionRepository.deleteById(id);
                return new ResponseEntity<>("ok", HttpStatus.OK);
            }else{
                List<Answer> answers = this.questionRepository.findById(id).get().getAnswers();
                for (Answer answer : answers) {
                    this.answerRepository.deleteByQuestionIdAndOrdinalNumber(answer.getQuestion().getId(), answer.getOrdinalNumber());
                }
                this.questionRepository.deleteById(id);
                return new ResponseEntity<>("ok, deleted answers", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
    }
    
    public ResponseEntity<String> updateQuestion(HashMap<String, String> datas){
        if (this.questionRepository.findById(Long.valueOf(datas.get("id"))).isPresent()) {
            Question updatedQuestion = this.questionRepository.findById(Long.valueOf(datas.get("id"))).get();
            updatedQuestion.setQuestion(datas.get("question"));
            Media media = this.mediaRepository.searchMediaByOwnerIdAndFileName(Long.parseLong(datas.get("mediaContentOwner")), datas.get("mediaContentName"));
            if (media == null) {
                return new ResponseEntity<>("failed to find media", HttpStatus.BAD_REQUEST);
            }
            updatedQuestion.setMediaContent(media);
            updatedQuestion.setTimeToAnswer((byte) Integer.parseInt(datas.get("timeToAnswer")));
            this.questionRepository.save(updatedQuestion);
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } else{
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
    }
}
