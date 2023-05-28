package com.github.m2cyurealestate.real_estate_back.api.rest.routes.auth;

import com.github.m2cyurealestate.real_estate_back.business.user.User;

/**
 * @author Aldric Vitali Silvestre
 */
public record RespRegister(
        long id,
        String username,
        String email
) {
    public RespRegister(User user) {
        this(user.getId(), user.getUsername(), user.getEmail());
    }
}
