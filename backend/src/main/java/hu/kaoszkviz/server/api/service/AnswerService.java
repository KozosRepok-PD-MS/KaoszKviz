package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.config.ConfigDatas;
import hu.kaoszkviz.server.api.dto.AnswerDTO;
import hu.kaoszkviz.server.api.exception.InternalServerErrorException;
import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.exception.UnauthorizedException;
import hu.kaoszkviz.server.api.model.Answer;
import hu.kaoszkviz.server.api.model.AnswerId;
import hu.kaoszkviz.server.api.model.Question;
import hu.kaoszkviz.server.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.AnswerRepository;
import hu.kaoszkviz.server.api.repository.QuestionRepository;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.CustomModelMapper;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class AnswerService {
    
    @Autowired
    private AnswerRepository answerRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private CustomModelMapper customModelMapper;
    
    public ResponseEntity<String> addAnswer(AnswerDTO answerDTO) {
        Answer answer = this.customModelMapper.fromDTO(answerDTO, Answer.class);
        User authUser = ApiKeyAuthentication.getAuth().getPrincipal();
        
        if (authUser.getId() != answer.getQuestion().getQuiz().getOwner().getId()) { throw new UnauthorizedException(); }
        
        byte answerCount = this.answerRepository.countAnswersForQuestion(answer.getQuestion().getId());
        System.out.println(answerCount);
        if (answerCount >= ConfigDatas.MAX_ANSWER_FOR_QUESTION) { return ErrorManager.conflict("max answer count reached"); }
        
        answer.setOrdinalNumber(++answerCount);
        answer = this.answerRepository.save(answer);
        
        if (answer == null) { throw new InternalServerErrorException(); }
        
        return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.fromModel(answer, AnswerDTO.class)), HttpStatus.OK);
    }
    
    public ResponseEntity<String> deleteByQuestionIdAndOrdinalNumber(Long questionId, byte ordinalNumber) {
        AnswerId answerId = new AnswerId(this.questionRepository.findById(questionId).orElseThrow(() -> new NotFoundException(Question.class, questionId)), ordinalNumber);
        Answer answer = this.answerRepository.findById(answerId).orElseThrow(() ->new NotFoundException(Answer.class, answerId));
        
        this.answerRepository.delete(answer);
        // TODO benntlévőknek ordinalNumber módosítása
        return new ResponseEntity<>("", HttpStatus.OK);
    }
    
    public ResponseEntity<String> updateAnswer(AnswerDTO answerDTO) {
        AnswerId id = new AnswerId(this.questionRepository.findById(answerDTO.getQuestionId()).orElseThrow(() -> new NotFoundException(Question.class, answerDTO.getQuestionId())), answerDTO.getOrdinalNumber());
        Answer answer = this.answerRepository.findById(id).orElseThrow(() -> new NotFoundException(Answer.class, id));
        
        this.customModelMapper.updateFromDTO(answerDTO, answer);
        answer = this.answerRepository.save(answer);
        
        if (answer == null) { return ErrorManager.def(); }
        
        return new ResponseEntity<>(Converter.ModelToJsonString(this.customModelMapper.fromModel(answer, AnswerDTO.class)), HttpStatus.OK); // ErrorManager
    }
    
    public ResponseEntity<String> getAnswers(long questionId) {
        List<Answer> answers = this.answerRepository.serachByQuestionId(questionId);
        
        if (answers.isEmpty()) {return ErrorManager.notFound();}
        
        return new ResponseEntity<>(Converter.ModelListToJsonString(this.customModelMapper.fromModelList(answers, AnswerDTO.class)), HttpStatus.OK);
    }
}
