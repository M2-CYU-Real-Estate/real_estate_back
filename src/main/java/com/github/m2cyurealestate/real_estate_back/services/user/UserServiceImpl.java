package com.github.m2cyurealestate.real_estate_back.services.user;

import com.github.m2cyurealestate.real_estate_back.api.rest.routes.auth.ReqRegister;
import com.github.m2cyurealestate.real_estate_back.api.rest.routes.favorites.ReqAddFavorite;
import com.github.m2cyurealestate.real_estate_back.api.rest.routes.user.ReqCreateProfile;
import com.github.m2cyurealestate.real_estate_back.business.user.Profile;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.business.user.UserRole;
import com.github.m2cyurealestate.real_estate_back.dao.user.UserDao;
import com.github.m2cyurealestate.real_estate_back.security.jwt.AuthenticationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aldric Vitali Silvestre
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationHandler authenticationHandler;

    @Autowired
    public UserServiceImpl(UserDao userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationHandler authenticationHandler) {
        this.userDao = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationHandler = authenticationHandler;
    }

    @Override
    public void onLogin(User user) {
        userDao.updateLastLoginDate(user.getId());
    }

    @Override
    public User register(ReqRegister request) {
        String email = request.email();
        validateCanRegister(email);

        User user = new User(request.username(), request.email(), passwordEncoder.encode(request.password()));
        return userDao.save(user);
    }

    private void validateCanRegister(String email) {
        if (userDao.existsByEmail(email)) {
            throw new IllegalArgumentException("Another user with this email address already exists");
        }
    }

    @Override
    public User getUserById(long id) {
        return userDao.findById(id)
                .orElseThrow();
    }

    /**
     * Register a default admin, providing the already-encrypted password
     */
    @Override
    public void registerDefaultAdminIfNotFound(String username, String email, String encryptedPassword) {
        if (userDao.existsByEmail(email)) {
            LOGGER.info("Default admin already exists");
            return;
        }
        LOGGER.info("Create default admin");
        userDao.save(new User(username, email, encryptedPassword, UserRole.ADMIN));
    }

    @Override
    public void addFavorite(ReqAddFavorite request) {
        User user = authenticationHandler.getUserFromContext();
        userDao.addFavorite(user, request.estateUrl());
    }

    @Override
    public void removeFavorite(ReqAddFavorite request) {
        User user = authenticationHandler.getUserFromContext();
        userDao.removeFavorite(user, request.estateUrl());
    }

    @Override
    public List<Profile> getUserProfiles() {
        User user = authenticationHandler.getUserFromContext();
        return userDao.findProfileByUser(user);
    }

    @Override
    public Profile getProfileById(long id) {
        User user = authenticationHandler.getUserFromContext();
        return userDao.findProfileById(user, id).orElseThrow();
    }

    @Override
    public void createProfile(ReqCreateProfile request) {
        User user = authenticationHandler.getUserFromContext();
        Profile profile = createProfileFromRequest(request, user);
        userDao.addProfile(user, profile);
    }

    @Override
    public void modifyProfile(long profileId, ReqCreateProfile request) {
        User user = authenticationHandler.getUserFromContext();
        userDao.changeProfile(user, profileId, createProfileFromRequest(request, user));
    }

    private Profile createProfileFromRequest(ReqCreateProfile request, User user) {
        return new Profile(
                user.getId(),
                false,
                request.name(),
                request.budgetClass(),
                request.postalCode(),
                request.acceptableDistance(),
                request.houseArea(),
                request.rooms(),
                request.bedrooms(),
                request.bathrooms(),
                request.minEnergyClass(),
                request.balcony(),
                request.fittedKitchen(),
                request.scoreSecurity(),
                request.scoreEducation(),
                request.scoreHobbies(),
                request.scoreEnvironment(),
                request.scorePracticality()
        );
    }

    @Override
    public void deleteProfile(long profileId) {
        User user = authenticationHandler.getUserFromContext();
        userDao.removeProfile(user, profileId);
    }

    @Override
    public void setToMainProfile(long profileId) {
        User user = authenticationHandler.getUserFromContext();
        userDao.switchMainProfile(user, profileId);
    }

}
