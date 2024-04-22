
package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.exception.InternalServerErrorException;
import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.exception.UnauthorizedException;
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
        String msg = e.getMessage();
        
        if (e instanceof DataIntegrityViolationException) {
            return new ResponseEntity<>("already exists", HttpStatus.CONFLICT);
        }
        
        if (e instanceof NotFoundException notFoundEx) {
            return ErrorManager.notFound(notFoundEx.getNotFoundClass().getSimpleName().toLowerCase() + ":" + notFoundEx.getNotFoundKey() + (msg != null ? ("->" + msg) : ""));
        }
        
        if (e instanceof UnauthorizedException unauthEx) {
            return ErrorManager.unauth();
        }
        if (e instanceof InternalServerErrorException iseEx) {
            return ErrorManager.internal();
        }
        
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
