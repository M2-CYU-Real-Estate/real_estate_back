package com.github.m2cyurealestate.real_estate_back.dao.user;

import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.dao.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
