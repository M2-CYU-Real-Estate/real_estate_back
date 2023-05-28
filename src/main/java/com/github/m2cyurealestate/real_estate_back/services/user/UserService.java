package com.github.m2cyurealestate.real_estate_back.services.user;

import com.github.m2cyurealestate.real_estate_back.api.rest.routes.auth.ReqRegister;
import com.github.m2cyurealestate.real_estate_back.business.user.User;

import java.util.NoSuchElementException;

/**
 * @author Aldric Vitali Silvestre
 */
public interface UserService {

    /**
     * Trigger actions when user successfully log in
     * @param user
     */
    void onLogin(User user);

    /**
     * Create a new user using register information
     * @param request
     * @return the created used
     */
    User register(ReqRegister request);

    User getUserById(long id) throws NoSuchElementException;

    void registerDefaultAdminIfNotFound(String username, String email, String encryptedPassword);
}
