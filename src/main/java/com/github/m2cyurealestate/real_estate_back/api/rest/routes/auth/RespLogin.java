package com.github.m2cyurealestate.real_estate_back.api.rest.routes.auth;

import com.github.m2cyurealestate.real_estate_back.api.rest.routes.user.RespUser;
import com.github.m2cyurealestate.real_estate_back.business.user.User;

import java.time.LocalDateTime;

/**
 * @author Aldric Vitali Silvestre
 */
public record RespLogin(
        RespUser user,
        String token,
        LocalDateTime expirationDate
) {

    public RespLogin(User user, String token, LocalDateTime expirationDate) {
        this(new RespUser(user), token, expirationDate);
    }
}
