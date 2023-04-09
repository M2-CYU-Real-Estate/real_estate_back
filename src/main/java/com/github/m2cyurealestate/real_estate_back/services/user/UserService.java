package com.github.m2cyurealestate.real_estate_back.services.user;

import com.github.m2cyurealestate.real_estate_back.api.rest.auth.ReqRegister;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.business.user.UserRole;
import com.github.m2cyurealestate.real_estate_back.dao.user.UserRepository;
import com.github.m2cyurealestate.real_estate_back.security.jwt.AuthenticationHandler;
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
public class UserService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void onLogin(User user) {
        user.onLogin();
        userRepository.save(user);
    }

    public User register(ReqRegister request) {
        String email = request.email();
        validateCanRegister(email);

        User user = new User(request.username(), request.email(), passwordEncoder.encode(request.password()));
        return userRepository.save(user);
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow();
    }

    /**
     * Register a default admin, providing the already-encrypted password
     */
    public void registerDefaultAdminIfNotFound(String username, String email, String encryptedPassword) {
        if (userRepository.existsByEmail(email)) {
            LOGGER.info("Default admin already exists");
            return;
        }
        LOGGER.info("Create default admin");
        userRepository.save(new User(username, email, encryptedPassword, UserRole.ADMIN));
    }

    private void validateCanRegister(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Another user with this email address already exists");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user by email"));
    }

}
