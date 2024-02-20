package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.service.QuizPlayerService;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/quizplayer")
public class QuizPlayerController {
    
    @Autowired
    private QuizPlayerService quizPlayerService;
    
    @GetMapping
    public ResponseEntity<String> getQuizPlayers(@RequestBody HashMap<String, String> requestBody) {
        String key = "quizHistoryId";
        String quizHistoryIdStr = requestBody.get(key);
        
        if (quizHistoryIdStr == null || quizHistoryIdStr.isBlank()) { ErrorManager.notFound(quizHistoryIdStr); }
        long quizHistoryId;
        
        try {
            quizHistoryId = Long.parseLong(quizHistoryIdStr);
        } catch (NumberFormatException ex) {
            return ErrorManager.nan(key);
        }
        
        return this.quizPlayerService.getQuizPlayers(quizHistoryId);
    }
}
