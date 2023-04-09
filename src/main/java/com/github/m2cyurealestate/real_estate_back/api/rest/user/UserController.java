package com.github.m2cyurealestate.real_estate_back.api.rest.user;

import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.security.jwt.AuthenticationHandler;
import com.github.m2cyurealestate.real_estate_back.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aldric Vitali Silvestre
 */
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    private final AuthenticationHandler authenticationHandler;

    @Autowired
    public UserController(UserService userService,
                          AuthenticationHandler authenticationHandler) {
        this.userService = userService;
        this.authenticationHandler = authenticationHandler;
    }

    @GetMapping("me")
    public ResponseEntity<RespUser> getCurrentUser() throws Exception {
        User user = authenticationHandler.getUserFromContext();
        return ResponseEntity.ok(new RespUser(user));
    }

    @GetMapping("{id}")
    public ResponseEntity<RespUser> getUserById(@PathVariable("id") long id) throws Exception {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new RespUser(user));
    }
}
