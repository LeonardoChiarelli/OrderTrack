package br.com.OrderTrack.Payment.general.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<?> handle400Error() { return  ResponseEntity.notFound().build(); }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle400Error(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream()
                .map(errorDataValidation::new)
                .toList());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handle400Error(HttpMessageNotReadableException ex) { return ResponseEntity.badRequest().body(ex.getMessage()); }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleBusinessLogicError(ValidationException ex) { return ResponseEntity.badRequest().body(ex.getMessage()); }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsError() { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"); }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationError() { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Failed"); }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedError() { return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied"); }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle500Error(Exception ex) { return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getLocalizedMessage()); }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<?> handleObjectOptimisticLockingError() { return ResponseEntity.status(HttpStatus.CONFLICT).body("Another client made an order that contained your items"); }



    private record errorDataValidation(String field, String message) {
        public errorDataValidation(FieldError fieldError) {
            this(fieldError.getField(),  fieldError.getDefaultMessage());
        }
    }
}
