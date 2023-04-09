package com.github.m2cyurealestate.real_estate_back.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m2cyurealestate.real_estate_back.api.rest.error.RespError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aldric Vitali Silvestre
 */
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public UnauthorizedEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");

        UrlPathHelper urlPathHelper = new UrlPathHelper();
        String path = urlPathHelper.getPathWithinApplication(request);

        RespError respError = new RespError(path,
                                            LocalDateTime.now(),
                                            status.value(),
                                            status.name(),
                                            "Unauthenticated or provided token is invalid (path could also be invalid)",
                                            List.of()
        );
        String stringMessage = objectMapper.writeValueAsString(respError);
        response.getOutputStream().println(stringMessage);
    }
}
