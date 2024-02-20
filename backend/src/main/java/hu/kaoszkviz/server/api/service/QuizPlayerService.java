package hu.kaoszkviz.server.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.QuizPlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class QuizPlayerService {
    
    @Autowired
    private QuizPlayerRepository quizPlayerRepository;
    
    public ResponseEntity<String> getQuizPlayers(long quizHistoryId) {
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
