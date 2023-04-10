package com.github.m2cyurealestate.real_estate_back.api.rest.exception_handler;

import com.github.m2cyurealestate.real_estate_back.api.rest.error.RespError;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.webjars.NotFoundException;

import java.util.NoSuchElementException;

/**
 * The class responsible for handling exceptions thrown in the controllers.
 * <p>
 * Depending on the exception type thrown, a different HTTP code will be raised.
 *
 * @author Aldric Vitali Silvestre
 */
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<RespError> handleNonSpecificException(Throwable throwable, WebRequest request) {
        return createResponse(throwable, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NoSuchElementException.class, NotFoundException.class})
    public ResponseEntity<RespError> handleNotFoundExceptions(Exception exception, WebRequest request) {
        return createResponse(exception, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadCredentialsException.class, AccessDeniedException.class, AuthenticationException.class})
    public ResponseEntity<RespError> handleAuthenticationException(Exception exception, WebRequest request) {
        return createResponse(exception, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ConversionFailedException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<RespError> handleValidationException(Exception exception, WebRequest request) {
        return createResponse(exception, request, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<RespError> createResponse(Throwable throwable, WebRequest request, HttpStatus status) {
        RespError resp = new RespError(request, status, throwable);
        return ResponseEntity.status(status)
                .body(resp);
    }
}
