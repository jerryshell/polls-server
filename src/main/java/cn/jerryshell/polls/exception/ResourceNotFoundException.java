package cn.jerryshell.polls.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException build(String resourceName, String fieldName, Object fieldValue) {
        String message = String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue);
        return new ResourceNotFoundException(message);
    }
}
