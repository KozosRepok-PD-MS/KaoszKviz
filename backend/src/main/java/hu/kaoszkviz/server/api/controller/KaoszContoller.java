
package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class KaoszContoller {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        if (e instanceof DataIntegrityViolationException) {
            return new ResponseEntity<>("already exists", HttpStatus.CONFLICT);
        }
        
        if (e instanceof NotFoundException notFoundEx) {
            return ErrorManager.notFound(e.getMessage());
        }
        
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
