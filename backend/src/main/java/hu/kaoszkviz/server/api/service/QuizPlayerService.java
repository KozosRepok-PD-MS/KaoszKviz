package hu.kaoszkviz.server.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.QuizPlayerRepository;

@Service
public class QuizPlayerService {
    
    @Autowired
    private QuizPlayerRepository quizPlayerRepository;
}
