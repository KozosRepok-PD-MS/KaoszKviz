package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.model.Answer;
import hu.kaoszkviz.server.api.service.AnswerService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answer")
public class AnswerController {
    
    @Autowired
    private AnswerService answerService;
    
    @PostMapping(name = "")
    public ResponseEntity<String> addAnswer(@RequestBody HashMap<String, String> requeqtBody){
        return this.answerService.addAnswer(requeqtBody);
    }
    
    @DeleteMapping(name = "")
    public ResponseEntity<String> deleteByQuestionIdAndOrdinalNumber(@RequestBody HashMap<String, String> requestBody){
        return this.answerService.deleteByQuestionIdAndOrdinalNumber(Long.valueOf(requestBody.get("questionId")), (byte) Integer.parseInt(requestBody.get("ordinalNumber")));
    }
    
    @PutMapping(name = "")
    public ResponseEntity<String> updateAnswer(@RequestBody HashMap<String, String> requestBody){
        return this.answerService.updateAnswer(requestBody);
    }
}
