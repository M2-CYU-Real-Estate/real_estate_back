package com.github.m2cyurealestate.real_estate_back.business.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Describes a registered user in the storage
 *
 * @author Aldric Vitali Silvestre
 */
public class User implements UserDetails {

    private Long id;

    private String name;

    private String email;

    private String password;

    private UserRole role = UserRole.USER;

    private LocalDateTime creationDate = LocalDateTime.now();

    private LocalDateTime lastLoginDate = LocalDateTime.now();

    private LocalDateTime lastPasswordResetDate = LocalDateTime.now();

    // ==== CONSTRUCTORS ====

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name,
                String email,
                String password,
                UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(Long id,
                String name,
                String email,
                String password,
                UserRole role,
                LocalDateTime creationDate,
                LocalDateTime lastLoginDate,
                LocalDateTime lastPasswordResetDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.creationDate = creationDate;
        this.lastLoginDate = lastLoginDate;
        this.lastPasswordResetDate = lastPasswordResetDate;
    }
// ==== NON-TRIVIAL METHODS ====

    public void onLogin() {
        lastLoginDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public LocalDateTime getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(LocalDateTime lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    // ==== USER DETAILS IMPLEMENTATIONS ====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRoleName()));
    }

    @Override
    public String getUsername() {
        // We want the unique id here
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name,
                                                             user.name
        ) && Objects.equals(email, user.email) && Objects.equals(password,
                                                                 user.password
        ) && role == user.role && Objects.equals(creationDate, user.creationDate) && Objects.equals(
                lastLoginDate,
                user.lastLoginDate
        ) && Objects.equals(lastPasswordResetDate, user.lastPasswordResetDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, role, creationDate, lastLoginDate, lastPasswordResetDate);
    }
}
