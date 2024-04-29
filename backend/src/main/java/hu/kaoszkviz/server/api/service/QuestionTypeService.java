package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.dto.QuestionTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.QuestionTypeRepository;
import hu.kaoszkviz.server.api.tools.Converter;
import hu.kaoszkviz.server.api.tools.CustomModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class QuestionTypeService {
    
    @Autowired
    private QuestionTypeRepository questionTypeRepository;
    
    @Autowired
    private CustomModelMapper customModelMapper;

    public ResponseEntity<String> getAllQuestionType() {
        return new ResponseEntity<>(Converter.ModelListToJsonString(this.customModelMapper.fromModelList(this.questionTypeRepository.findAll(), QuestionTypeDTO.class)), HttpStatus.OK);
    }
}
