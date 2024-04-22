package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.model.Answer;
import hu.kaoszkviz.server.api.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.AnswerRepository;
import hu.kaoszkviz.server.api.repository.QuestionRepository;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class AnswerService {
    
    @Autowired
    private AnswerRepository answerRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    public ResponseEntity<String> addAnswer(HashMap<String, String> datas){
        Optional<Question> question = this.questionRepository.findById(Long.valueOf(datas.get("questionId")));
        if(!question.isPresent()){
            return ErrorManager.notFound("question");
        }
        try{
            List<Answer> answers = this.answerRepository.serachByQuestionId(question.get().getId());
            Answer answer = new Answer();
            answer.setQuestion(question.get());
            answer.setAnswer(datas.get("answer"));
            if(!answers.isEmpty()){
                answer.setOrdinalNumber((byte) (answers.get(answers.size() - 1).getOrdinalNumber()+1));
            }
            answer.setCorrect(Boolean.getBoolean(datas.get("correct")));
            answer.setCorrectAnswer(datas.get("correctAnswer"));
            return this.addAnswer(answer);
            
        } catch(Exception ex){
            return new ResponseEntity<>("failed to save", HttpStatus.BAD_REQUEST); //ErrorManager
        }
    }

    private ResponseEntity<String> addAnswer(Answer answer) {
        if(this.answerRepository.save(answer) == null){
            return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST); //ErrorManager
        } else{
            this.answerRepository.save(answer);
            return new ResponseEntity<>("ok", HttpStatus.CREATED); //ErrorManager
        }
    }
    
    public ResponseEntity<String> deleteByQuestionIdAndOrdinalNumber(Long questionId, byte ordinalNumber){
        if (this.answerRepository.serachByQuestionIdAndOrdinalNumber(questionId, ordinalNumber) != null) {
            this.answerRepository.deleteByQuestionIdAndOrdinalNumber(questionId, ordinalNumber);
            return new ResponseEntity<>("ok", HttpStatus.OK); //ErrorManager
        } else{
            return ErrorManager.notFound("question");
        }
    }
    
    public ResponseEntity<String> updateAnswer(HashMap<String, String> datas){
        if(this.answerRepository.serachByQuestionIdAndOrdinalNumber(Long.parseLong(datas.get("questionId")), (byte) Integer.parseInt(datas.get("ordinalNumber"))) != null){
            Answer updatedAnswer = this.answerRepository.serachByQuestionIdAndOrdinalNumber(Long.parseLong(datas.get("questionId")), (byte) Integer.parseInt(datas.get("ordinalNumber")));
            updatedAnswer.setAnswer(datas.get("answer"));
            updatedAnswer.setCorrect(Boolean.parseBoolean(datas.get("correct")));
            updatedAnswer.setCorrectAnswer(datas.get("correctAnswer"));
            this.answerRepository.save(updatedAnswer);
            return new ResponseEntity<>("ok", HttpStatus.OK); // ErrorManager
        } else {
            return ErrorManager.notFound("question");
        }
    }
    
    public ResponseEntity<String> getAnswers(long questionId) {
        List<Answer> answers = this.answerRepository.serachByQuestionId(questionId);
        
        if (answers.isEmpty()) {return ErrorManager.notFound();}
        
        return new ResponseEntity<>(Converter.ModelListToJsonString(answers), HttpStatus.OK);
    }
}
