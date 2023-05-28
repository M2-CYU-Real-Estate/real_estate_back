package com.github.m2cyurealestate.real_estate_back.security.jwt;

import com.github.m2cyurealestate.real_estate_back.api.rest.routes.auth.ReqLogin;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
@Service
public class AuthenticationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHandler.class);

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtTokenHandler tokenHandler;

    @Autowired
    public AuthenticationHandler(AuthenticationManager authenticationManager,
                                 UserDetailsService userDetailsService,
                                 JwtTokenHandler tokenHandler) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenHandler = tokenHandler;
    }

    public String createToken(ReqLogin request) {
        LOGGER.debug("Trying to log {}", request.email());

        // Will use the UserDetailsService under the hood
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenHandler.generateToken(authentication);
    }

    /**
     * Find the user from the security context
     *
     * @return The authenticated user
     * @throws ProviderNotFoundException if the authentication cannot be retrieved nor contains a principal
     * @throws NoSuchElementException    if the user cannot be retrieved from the authentication
     */
    public User getUserFromContext() {
        // We know that we stored that in the JwtAuthenticationFilter
        var principal = Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .orElseThrow(() -> new ProviderNotFoundException("No authentication nor principal found"));
        if (principal instanceof User user) {
            return user;
        }
        throw new NoSuchElementException("Authenticated user cannot be properly retrieved");
    }
}
