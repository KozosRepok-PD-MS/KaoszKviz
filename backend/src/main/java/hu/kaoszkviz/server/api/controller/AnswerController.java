package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.dto.AnswerDTO;
import hu.kaoszkviz.server.api.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answer")
public class AnswerController {
    
    @Autowired
    private AnswerService answerService;
    
    @PostMapping(name = "")
    public ResponseEntity<String> addAnswer(@RequestBody AnswerDTO answerDTO){
        return this.answerService.addAnswer(answerDTO);
    }
    
    @GetMapping
    public ResponseEntity<String> getAnswersForQuestion(@RequestParam long questionId) {
        return this.answerService.getAnswers(questionId);
    }
    
    @DeleteMapping(name = "")
    public ResponseEntity<String> deleteByQuestionIdAndOrdinalNumber(@RequestParam long questionId, @RequestParam byte ordinalNumber){
        return this.answerService.deleteByQuestionIdAndOrdinalNumber(questionId, ordinalNumber);
    }
    
    @PutMapping(name = "")
    public ResponseEntity<String> updateAnswer(@RequestBody AnswerDTO answerDTO){
        return this.answerService.updateAnswer(answerDTO);
    }
}
