package com.github.m2cyurealestate.real_estate_back.security.jwt;

import com.github.m2cyurealestate.real_estate_back.config.properties.JwtProperties;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;

    private final JwtTokenHandler tokenHandler;

    @Autowired
    public JwtAuthenticationFilter(JwtProperties jwtProperties,
                                   UserDetailsService userDetailsService,
                                   JwtTokenHandler tokenHandler) {
        this.jwtProperties = jwtProperties;
        this.userDetailsService = userDetailsService;
        this.tokenHandler = tokenHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        findToken(request)
                .ifPresentOrElse(token -> onTokenFound(token, request),
                                 () -> onTokenNotFound(request)
                );

        filterChain.doFilter(request, response);
    }

    private void onTokenFound(String token, HttpServletRequest request) {
        String username = tokenHandler.fetchUserName(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        var details = new WebAuthenticationDetailsSource().buildDetails(request);
        authentication.setDetails(details);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void onTokenNotFound(HttpServletRequest request) {
        // We can be in this case when in the "auth" route for example
        LOGGER.debug("Cannot find token from request");
    }

    // ==== FIND TOKEN ====

    private Optional<String> findToken(HttpServletRequest request) {
        return findTokenFromHeaders(request)
                .or(() -> findFromCookies(request));
    }

    private Optional<String> findTokenFromHeaders(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.header()))
                .filter(StringUtils::isNotBlank);
    }

    private Optional<String> findFromCookies(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(c -> jwtProperties.header().equals(c.getName()))
                .peek(c -> LOGGER.debug("Found token from cookies"))
                .map(Cookie::getValue)
                .findFirst();
    }
}
