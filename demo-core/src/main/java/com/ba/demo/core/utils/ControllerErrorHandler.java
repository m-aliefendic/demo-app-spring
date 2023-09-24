package com.ba.demo.core.utils;

import com.ba.demo.api.model.user.exception.UserException;
import com.ba.demo.api.model.user.exception.UserNotFoundException;
import com.ba.demo.service.internationalization.InternationalizatonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.google.common.base.Preconditions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ControllerErrorHandler {

    private final InternationalizatonService internationalizatonService;

    public static final String VALIDATION_PROBLEM_TITLE = "There are some validation error(s) while processing the request";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleNotValidArguments(final MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(convert(ex, VALIDATION_PROBLEM_TITLE));
    }

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<Problem> handleAnyException(Exception ex) {
        log.error("Intercepted a Throwable", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Problem(ErrorCode.SERVER_ERROR.getMessage(), ex.getMessage(), null));
    }

    @ExceptionHandler(value = {UserException.class})
    public ResponseEntity<Problem> handleAnyException(UserException ex) {
        log.error("Intercepted a UserException", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Problem(ErrorCode.SERVER_ERROR.getMessage(), ex.getMessage(), null));
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Problem> handleAnyException(UserNotFoundException ex) {
        log.error("Intercepted a UserNotFoundException", ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Problem(ErrorCode.NOT_FOUND.getMessage(), ex.getMessage(), null));
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Problem> handleAnyException(EntityNotFoundException ex) {
        log.error("Intercepted a EntityNotFoundException", ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Problem(ErrorCode.NOT_FOUND.getMessage(), MessagesKey.entity_not_found.getName(), null));
    }


    @ExceptionHandler(value = {SpecificException.class})
    public ResponseEntity<Problem> handleCustomException(SpecificException ex) {
        ex = new SpecificException(ex, internationalizatonService.get(ex.getMessageKey().getName(), ex.getMessageTokens()));
        log.error("Intercepted a CustomException", ex);
        return respond(ex.getErrorCode(), ex.getMessageKey(), ex.getMessageTokens());
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<Problem> handleBadCredentials(BadCredentialsException ex) {
        log.error("Intercepted a BadCredentialsException", ex);
        return respond(ErrorCode.UNAUTHORIZED, MessagesKey.bad_credentials);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Problem> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("Intercepted a MethodArgumentTypeMismatchException", e);
        return respond(ErrorCode.BAD_REQUEST, MessagesKey.bad_request, e.getCause().getMessage());
    }

    private ResponseEntity<Problem> respond(ErrorCode errorCode, MessagesKey messageKey, Object... messageTokens) {
        Problem error = new Problem(errorCode.getMessage(), internationalizatonService.get(messageKey.getName(), messageTokens), null);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(error);
    }

    public static Problem convert(final MethodArgumentNotValidException ex, final String message) {
        final BindingResult result = Preconditions.checkNotNull(ex.getBindingResult());
        final List<FieldError> fieldErrors = result.getFieldErrors();

        final List<InvalidParam> fieldValidationErrors = fieldErrors.stream().map(fieldError ->
                new InvalidParam(fieldError.getField(), fieldError.getDefaultMessage(), ValidationMessages.reason(fieldError.getDefaultMessage()))
        ).collect(Collectors.toList());

        return new Problem(message, null, fieldValidationErrors);
    }

}
