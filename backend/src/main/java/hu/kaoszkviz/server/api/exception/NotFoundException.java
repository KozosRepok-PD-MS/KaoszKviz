
package hu.kaoszkviz.server.api.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    
    private Class notFoundClass;
    private String notFoundKey;

    public NotFoundException(Class notFoundClass, Object notFoundKey) {
        this.notFoundClass = notFoundClass;
        this.notFoundKey = notFoundKey.toString();
    }

    public NotFoundException(Class notFoundClass, Object notFoundKey, String message) {
        super(message);
        this.notFoundClass = notFoundClass;
        this.notFoundKey = notFoundKey.toString();
    }

    public NotFoundException(Class notFoundClass, Object notFoundKey, String message, Throwable cause) {
        super(message, cause);
        this.notFoundClass = notFoundClass;
        this.notFoundKey = notFoundKey.toString();
    }

    public NotFoundException(Class notFoundClass, Object notFoundKey, Throwable cause) {
        super(cause);
        this.notFoundClass = notFoundClass;
        this.notFoundKey = notFoundKey.toString();
    }

    public NotFoundException(Class notFoundClass, Object notFoundKey, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.notFoundClass = notFoundClass;
        this.notFoundKey = notFoundKey.toString();
    }

}
