package lead.exchange.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lead.exchange.exception.ResourceAlreadyExistsException;
import lead.exchange.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;


@Slf4j
@ControllerAdvice
public class BaseController {
    @ExceptionHandler(ResourceNotFoundException.class) //404
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(IllegalArgumentException.class) //404
    public ResponseEntity<Object> handleResourceNotFound(IllegalArgumentException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(IllegalStateException.class) //404
    public ResponseEntity<Object> handleResourceNotFound(IllegalStateException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class) //404
    public ResponseEntity<Object> handleResourceNotFound(ResourceAlreadyExistsException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) //404
    public ResponseEntity<Object> handleResourceNotFound(MethodArgumentNotValidException ex, WebRequest request) {
        String error = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> String.format("%s: %s", e.getField(), e.getDefaultMessage()))
            .collect(Collectors.joining("\n"));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, error, (ServletWebRequest) request);
    }



    private ResponseEntity<Object> buildErrorResponse(
            Exception ex, HttpStatus status,
            WebRequest request) {

        return buildErrorResponse(status, ex.getLocalizedMessage(), (ServletWebRequest) request);
    }

    private static @NotNull ResponseEntity<Object> buildErrorResponse(
        HttpStatus status,
        String ex,
        ServletWebRequest request
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", ex);
        body.put("path", request.getRequest().getRequestURI());

        return new ResponseEntity<>(body, new HttpHeaders(), status);
    }
}
