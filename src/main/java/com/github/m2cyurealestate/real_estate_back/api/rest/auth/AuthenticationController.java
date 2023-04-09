package com.github.m2cyurealestate.real_estate_back.api.rest.auth;

import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.security.jwt.AuthenticationHandler;
import com.github.m2cyurealestate.real_estate_back.security.jwt.JwtTokenHandler;
import com.github.m2cyurealestate.real_estate_back.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author Aldric Vitali Silvestre
 */
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationHandler authenticationHandler;

    private final UserService userService;

    private final JwtTokenHandler jwtTokenHandler;

    public AuthenticationController(AuthenticationHandler authenticationHandler,
                                    UserService userService,
                                    JwtTokenHandler jwtTokenHandler) {
        this.authenticationHandler = authenticationHandler;
        this.userService = userService;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    @PostMapping("login")
    public ResponseEntity<RespLogin> login(@RequestBody ReqLogin request) throws Exception {
        LOGGER.debug("REQUEST - login");
        String token = authenticationHandler.createToken(request);
        User user = authenticationHandler.getUserFromContext();
        userService.onLogin(user);
        LocalDateTime expirationDate = jwtTokenHandler.fetchExpirationDate(token);
        return ResponseEntity.ok(new RespLogin(user, token, expirationDate));
    }

    @PostMapping("register")
    public ResponseEntity<RespRegister> register(@RequestBody ReqRegister request) throws Exception {
        LOGGER.debug("Log in request");
        User user = userService.register(request);
        return ResponseEntity.ok(new RespRegister(user));
    }
}
