package com.github.m2cyurealestate.real_estate_back.api.rest.routes.user;

import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.security.jwt.AuthenticationHandler;
import com.github.m2cyurealestate.real_estate_back.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("profiles/{id}")
    public ResponseEntity<Void> modifyProfile(@PathVariable("id") long id,
                                              @RequestBody ReqCreateProfile request) throws Exception {
        userService.modifyProfile(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("profiles/{id}")
    public ResponseEntity<RespProfile> getProfileById(@PathVariable("id") long id) throws Exception {
        var profile = userService.getProfileById(id);
        return ResponseEntity.ok(new RespProfile(profile));
    }

    @PostMapping("profiles")
    public ResponseEntity<Void> createProfile(@RequestBody ReqCreateProfile request) throws Exception {
        userService.createProfile(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("profiles/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable("id") long id) throws Exception {
        userService.deleteProfile(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("profiles/main/{id}")
    public ResponseEntity<Void> changeMainProfile(@PathVariable("id") long id) throws Exception {
        userService.setToMainProfile(id);
        return ResponseEntity.ok().build();
    }
}
