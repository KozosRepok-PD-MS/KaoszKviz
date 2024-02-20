
package hu.kaoszkviz.server.api.tools;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ErrorManager {
    
    /**
     * A {@code ResponseEntity} that presents the {@code Not a number} exception.
     * @return {@code ResponseEntity}
     */
    public static ResponseEntity<String> nan() {
        return ErrorManager.nan("");
    }
    /**
     * A {@code ResponseEntity} that presents the {@code Not a number} exception with the given {@code String}.
     * 
     * @param str Message for the error code.
     * 
     * @return {@code ResponseEntity}
     */
    public static ResponseEntity<String> nan(String str) {
        String msg = "NAN";
        return new ResponseEntity<>(str.isBlank() ? msg : "%s %s".formatted(str, msg), HttpStatus.NOT_ACCEPTABLE);
    }
    
    /**
     * A {@code ResponseEntity} that presents the {@code Not found} exception.
     * 
     * @return {@code ResponseEntity}
     */
    public static ResponseEntity<String> notFound() {
        return ErrorManager.notFound("");
    }
    /**
     * A {@code ResponseEntity} that presents the {@code Not found} exception with the given {@code String}.
     * 
     * @param str Message for the error code.
     * 
     * @return {@code ResponseEntity}
     */
    public static ResponseEntity<String> notFound(String str) {
        String msg = "Not found";
        return new ResponseEntity<>(str.isBlank() ? msg : "%s %s".formatted(str, msg), HttpStatus.NOT_FOUND);
    }
    
    /**
     * A {@code ResponseEntity} that presents the {@code Bad request} exception.
     * 
     * @return {@code ResponseEntity}
     */
    public static ResponseEntity<String> def() {
        return ErrorManager.def("");
    }
    /**
     * A {@code ResponseEntity} that presents the {@code Bad request} exception with the given {@code String}.
     * 
     * @param str Message for the error code.
     * 
     * @return {@code ResponseEntity}
     */
    public static ResponseEntity<String> def(String str) {
        return new ResponseEntity<>(str, HttpStatus.BAD_REQUEST);
    }
    
}
