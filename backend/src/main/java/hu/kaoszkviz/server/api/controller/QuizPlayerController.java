package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.dto.QuizPlayerDTO;
import hu.kaoszkviz.server.api.service.QuizPlayerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quizplayer")
public class QuizPlayerController {
    
    @Autowired
    private QuizPlayerService quizPlayerService;
    
    @GetMapping
    public ResponseEntity<String> getQuizPlayersByQuizHistoryId(@RequestParam long quizHistoryId){
        return this.quizPlayerService.getQuizPlayersByQuizHistoryId(quizHistoryId);
    }
    
    @PostMapping
    public ResponseEntity<String> addQuizPlayers(@RequestBody List<QuizPlayerDTO> quizPlayers) {
        return this.quizPlayerService.addQuizPlayers(quizPlayers);
    }
}
