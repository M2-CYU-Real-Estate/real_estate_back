package com.github.m2cyurealestate.real_estate_back.persistence.jooq.user;

import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.dao.user.UserDao;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqUserEntityTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqUserEntityRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
@Repository
public class JooqUserDao implements UserDao {

    public static final JqUserEntityTable USER = JqUserEntityTable.USER_ENTITY;

    private final DSLContext dsl;

    private final JooqUserMappers mappers;

    @Autowired
    public JooqUserDao(DSLContext dslContext) {
        this.dsl = dslContext;
        this.mappers = new JooqUserMappers(dsl);
    }

    @Override
    public Optional<User> findById(long id) {
        return dsl.selectFrom(USER)
                .where(USER.ID.eq((int) id))
                .fetchOptional(mappers::toUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return dsl.selectFrom(USER)
                .where(USER.EMAIL.eq(email))
                .fetchOptional(mappers::toUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return dsl.fetchExists(USER, USER.EMAIL.eq(email));
    }

    @Override
    public User save(User user) {
        JqUserEntityRecord userRecord = mappers.fromUser(user);
        // Automatically sets the id
        userRecord.store();

        return userRecord.into(User.class);
    }

    @Override
    public void updateLastLoginDate(long id) {
        dsl.update(USER)
                .set(USER.LAST_LOGIN_DATE, LocalDateTime.now())
                .where(USER.ID.eq((int) id))
                .execute();
    }
}
