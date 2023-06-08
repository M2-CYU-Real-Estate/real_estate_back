package com.github.m2cyurealestate.real_estate_back.persistence.jooq.user;

import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.dao.user.UserDao;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqUserEntityTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqUserLikesTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqUserNavigationTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqUserEntityRecord;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqUserLikesRecord;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqUserNavigationRecord;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
@Repository
public class JooqUserDao implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(JooqUserDao.class);

    public static final JqUserEntityTable USER = JqUserEntityTable.USER_ENTITY;

    public static final JqUserLikesTable USER_LIKES = JqUserLikesTable.USER_LIKES;

    public static final JqUserNavigationTable USER_NAVIGATION = JqUserNavigationTable.USER_NAVIGATION;

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

    @Override
    public void addFavorite(User user, String estateUrl) {
        // Check if the favorite is already set
        boolean doesFavoriteExists = checkFavoriteExists(user, estateUrl);

        if (doesFavoriteExists) {
            LOGGER.warn("Favorite already exists, cancel the insertion");
            return;
        }

        JqUserLikesRecord like = new JqUserLikesRecord(
                user.getId().intValue(),
                estateUrl,
                LocalDateTime.now()
        );

        int insertResult = like.insert();
        if (insertResult != 1) {
            throw new RuntimeException("Cannot insert the favorite : result code is 0");
        }
    }

    @Override
    public void removeFavorite(User user, String estateUrl) {
        if (!checkFavoriteExists(user, estateUrl)) {
            throw new IllegalArgumentException("The user did not have put this favorite");
        }

        int nbDeletedRecords = dsl.delete(USER_LIKES)
                .where(USER_LIKES.ID_USER.eq(user.getId().intValue()))
                .and(USER_LIKES.ESTATE_LINK.eq(estateUrl))
                .execute();

        if (nbDeletedRecords == 0) {
            throw new RuntimeException("No favorite was deleted");
        }

        if (nbDeletedRecords != 1) {
            LOGGER.warn("More than one favorite was deleted ? " + nbDeletedRecords);
        }
    }

    @Override
    public void addNavigation(User user, String estateUrl) {
        JqUserNavigationRecord navigation = new JqUserNavigationRecord(
                user.getId().intValue(),
                estateUrl,
                LocalDateTime.now()
        );

        int insertResult = navigation.insert();
        if (insertResult != 1) {
            throw new RuntimeException("Cannot insert the favorite : result code is 0");
        }
    }

    private boolean checkFavoriteExists(User user, String estateUrl) {
        var selectExists = dsl.selectFrom(USER_LIKES)
                .where(USER_LIKES.ID_USER.eq(user.getId().intValue()))
                .and(USER_LIKES.ESTATE_LINK.eq(estateUrl));
        return dsl.fetchExists(selectExists);
    }
}
