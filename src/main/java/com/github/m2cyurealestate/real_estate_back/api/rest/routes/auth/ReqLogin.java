package com.github.m2cyurealestate.real_estate_back.api.rest.routes.auth;

/**
 * @author Aldric Vitali Silvestre
 */
public record ReqLogin(
        String email,
        String password
) {
}
