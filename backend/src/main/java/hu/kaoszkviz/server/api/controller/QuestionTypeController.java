
package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.service.QuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questiontype")
public class QuestionTypeController {
    
    @Autowired
    private QuestionTypeService questionTypeService;
    
    @GetMapping
    public ResponseEntity<String> getAllQuestionType() {
        return this.questionTypeService.getAllQuestionType();
    }
}
