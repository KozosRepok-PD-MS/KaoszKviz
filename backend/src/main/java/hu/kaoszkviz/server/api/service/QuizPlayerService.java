package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.model.QuizPlayer;
import hu.kaoszkviz.server.api.repository.QuizHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.QuizPlayerRepository;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class QuizPlayerService {
    
    @Autowired
    private QuizPlayerRepository quizPlayerRepository;
    
    @Autowired
    private QuizHistoryRepository quizHistoryRepository;
    
    public ResponseEntity<String> getQuizPlayersByQuizHistoryId(Long quizHistoryId) {
        if (!this.quizHistoryRepository.findById(quizHistoryId).isPresent()) {
            return ErrorManager.notFound("quizHistory");
        }
        
        List<QuizPlayer> quizPlayers = this.quizPlayerRepository.searchQuizPlayerByQuizHistoryId(quizHistoryId);
        
        if(quizPlayers.isEmpty()) {return ErrorManager.notFound("quizPlayers");}
        
        return new ResponseEntity<>(Converter.ModelListToJsonString(quizPlayers), HttpStatus.OK);
    }
}
