package hu.kaoszkviz.server.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.QuizRepository;

@Service
public class QuizService {
    
    @Autowired
    private QuizRepository quizRepository;
}
