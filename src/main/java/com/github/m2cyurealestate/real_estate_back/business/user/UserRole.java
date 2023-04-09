package com.github.m2cyurealestate.real_estate_back.business.user;

/**
 * Defines the role of a registered {@link User}
 * @author Aldric Vitali Silvestre
 */
public enum UserRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
