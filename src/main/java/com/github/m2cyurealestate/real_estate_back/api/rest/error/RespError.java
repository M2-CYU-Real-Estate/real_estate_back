package com.github.m2cyurealestate.real_estate_back.api.rest.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The message sent when an error is thrown
 *
 * @author Aldric Vitali Silvestre
 */
public record RespError(
        String path,
        LocalDateTime timestamp,
        int status,
        String statusName,
        String message,
        Collection<String> errors
) {

    public RespError(WebRequest request, HttpStatus status, Throwable throwable) {
        this(findPath(request),
             LocalDateTime.now(),
             status.value(),
             status.name(),
             throwable.getMessage(),
             List.of()
        );
    }

    public RespError(WebRequest request, HttpStatus status, Throwable throwable, Collection<String> errors) {
        this(findPath(request),
             LocalDateTime.now(),
             status.value(),
             status.name(),
             throwable.getMessage(),
             errors
        );
    }

    public RespError(WebRequest request, HttpStatus status, String message, Collection<String> errors) {
        this(findPath(request), LocalDateTime.now(),
             status.value(),
             status.name(),
             message,
             errors
        );
    }

    public RespError(WebRequest request, HttpStatus status, String message) {
        this(findPath(request), LocalDateTime.now(),
             status.value(),
             status.name(),
             message,
             List.of()
        );
    }

    public Map<String, Object> toCustomErrorAttributes() {
        return Map.of(
                "path", path,
                "timestamp", timestamp,
                "status", status,
                "statusName", statusName,
                "message", message,
                "errors", List.copyOf(errors)
        );
    }

    private static String findPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getRequestURI();
        }
        return "";
    }

}
