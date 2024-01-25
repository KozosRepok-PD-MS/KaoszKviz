package hu.kaoszkviz.server.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.AnswerRepository;

@Service
public class AnswerService {
    
    @Autowired
    private AnswerRepository answerRepository;
    
}
