package in.gov.incometax.blackmoney.error;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> notFound(Exception ex) {
        return Map.of("status", 404, "error", "NOT_FOUND", "message", ex.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badRequest(Exception ex) {
        return Map.of("status", 400, "error", "BAD_REQUEST", "message", ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> conflict(Exception ex) {
        return Map.of("status", 409, "error", "CONFLICT", "message", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> internal(Exception ex) {
        return Map.of("status", 500, "error", "INTERNAL_SERVER_ERROR", "message", "An unexpected error occurred");
    }
}
