package com.github.m2cyurealestate.real_estate_back.api.rest.routes.user;

import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.business.user.UserRole;

import java.time.LocalDateTime;

/**
 * @author Aldric Vitali Silvestre
 */
public record RespUser(
        long id,
        String name,
        String email,
        UserRole role,
        LocalDateTime creationDate,
        LocalDateTime lastLoginDate
) {

    public RespUser(User user) {
        this(user.getId(),
             user.getName(),
             user.getEmail(),
             user.getRole(),
             user.getCreationDate(),
             user.getLastLoginDate()
        );
    }
}
