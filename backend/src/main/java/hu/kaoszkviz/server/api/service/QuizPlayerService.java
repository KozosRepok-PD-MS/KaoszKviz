package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.dto.QuizPlayerDTO;
import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.model.QuizHistory;
import hu.kaoszkviz.server.api.model.QuizPlayer;
import hu.kaoszkviz.server.api.repository.QuizHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.QuizPlayerRepository;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.CustomModelMapper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class QuizPlayerService {
    
    @Autowired
    private QuizPlayerRepository quizPlayerRepository;
    
    @Autowired
    private QuizHistoryRepository quizHistoryRepository;
    
    @Autowired
    private CustomModelMapper customModelMapper;
    
    public ResponseEntity<String> getQuizPlayersByQuizHistoryId(Long quizHistoryId) {
        if (!this.quizHistoryRepository.findById(quizHistoryId).isPresent()) { throw new NotFoundException(QuizHistory.class, quizHistoryId); }
        
        List<QuizPlayer> quizPlayers = this.quizPlayerRepository.searchQuizPlayerByQuizHistoryId(quizHistoryId);
        
        if(quizPlayers.isEmpty()) { throw new NotFoundException(QuizPlayer.class, ""); }
        
        return new ResponseEntity<>(Converter.ModelListToJsonString(this.customModelMapper.fromModelList(quizPlayers, QuizPlayerDTO.class)), HttpStatus.OK);
    }
    // TODO csak a szerver tudja majd használni.
    public ResponseEntity<String> addQuizPlayers(List<QuizPlayerDTO> quizPlayersDTO) {
        List<QuizPlayer> quizPlayers = this.customModelMapper.fromDTOList(quizPlayersDTO, QuizPlayer.class);
        
        for (QuizPlayer quizPlayer : quizPlayers) {
            this.quizPlayerRepository.save(quizPlayer);
        }
        
        return new ResponseEntity<>(Converter.ModelListToJsonString(this.customModelMapper.fromModelList(quizPlayers, QuizPlayerDTO.class)), HttpStatus.OK);
    }
}
