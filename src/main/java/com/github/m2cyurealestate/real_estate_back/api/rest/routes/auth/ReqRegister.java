package com.github.m2cyurealestate.real_estate_back.api.rest.routes.auth;

/**
 * @author Aldric Vitali Silvestre
 */
public record ReqRegister(
        String username,
        String email,
        String password
) {
}
