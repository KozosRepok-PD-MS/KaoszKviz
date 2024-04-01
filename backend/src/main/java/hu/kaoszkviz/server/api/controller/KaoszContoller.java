
package hu.kaoszkviz.server.api.controller;

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
        
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
