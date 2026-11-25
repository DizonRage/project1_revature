package org.example.exception;

import org.example.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            AccountNotFoundException.class,
            UserNotFoundException.class,
            ApplicationNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(BankException e) {
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({
            InvalidAmountException.class,
            AccountClosedException.class,
            InsufficientFundsException.class,
            InvalidTransferException.class,
            InvalidApplicationStateException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(BankException e) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({UnauthorizedException.class, InvalidCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(BankException e) {
        return build(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException e) {
        return build(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler({UsernameTakenException.class, AlreadyOwnerException.class})
    public ResponseEntity<ErrorResponse> handleConflict(BankException e) {
        return build(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldErrors().get(0);
        return build(HttpStatus.BAD_REQUEST, error.getField() + " " + error.getDefaultMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException e) {
        return build(HttpStatus.BAD_REQUEST, "Malformed JSON request");
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message));
    }
}
