package com.github.m2cyurealestate.real_estate_back.dao.user;

import com.github.m2cyurealestate.real_estate_back.business.user.User;

import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User save(User user);

    void updateLastLoginDate(long id);

    void addFavorite(User user, String estateUrl);

    void removeFavorite(User user, String estateUrl);

    void addNavigation(User user, String estateUrl);
}
