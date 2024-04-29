package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.dto.QuestionDTO;
import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.model.Question;
import hu.kaoszkviz.server.api.service.QuestionService;
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
@RequestMapping("/question")
public class QuestionController {
    
    @Autowired
    private QuestionService questionService;
    
    @GetMapping
    public ResponseEntity<String> getQuestionsByQuizId(@RequestParam(required = false, defaultValue = "-1") long quizId, @RequestParam(required = false, defaultValue = "-1") long id) {
        if (quizId != -1) {
            return this.questionService.getQuestionsByQuizId(quizId);
        } else if (id != -1) {
            return this.questionService.getQuestionById(id);
        }
        throw new NotFoundException(Question.class, id);
    }
    
    @PostMapping
    public ResponseEntity<String> addQuestion(@RequestBody QuestionDTO question){
        return this.questionService.addQuestion(question);
    }
    
    @DeleteMapping
    public ResponseEntity<String> deleteQuestion(@RequestParam long id){
        return this.questionService.deleteQuestion(id);
    }
    
    @PutMapping
    public ResponseEntity<String> updateQuestion(@RequestBody QuestionDTO question){
        return this.questionService.updateQuestion(question);
    }
}
