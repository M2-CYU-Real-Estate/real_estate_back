package com.github.m2cyurealestate.real_estate_back.services.user;

import com.github.m2cyurealestate.real_estate_back.api.rest.routes.auth.ReqRegister;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.business.user.UserRole;
import com.github.m2cyurealestate.real_estate_back.dao.user.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Aldric Vitali Silvestre
 */
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userDao = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    private void validateCanRegister(String email) {
        if (userDao.existsByEmail(email)) {
            throw new IllegalArgumentException("Another user with this email address already exists");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user by email"));
    }

}
