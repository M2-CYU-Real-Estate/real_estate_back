package com.github.m2cyurealestate.real_estate_back.persistence.jooq.user;

import com.github.m2cyurealestate.real_estate_back.business.user.Profile;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.dao.user.UserDao;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqBuyingProfileEntryTable;
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
import java.util.List;
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

    public static final JqBuyingProfileEntryTable PROFILE = JqBuyingProfileEntryTable.BUYING_PROFILE_ENTRY;

    private final DSLContext dsl;

    private final JooqUserMappers userMappers;

    private final JooqProfileMappers profileMappers;

    @Autowired
    public JooqUserDao(DSLContext dslContext) {
        this.dsl = dslContext;
        this.userMappers = new JooqUserMappers(dsl);
        this.profileMappers = new JooqProfileMappers(dsl);
    }

    @Override
    public Optional<User> findById(long id) {
        return dsl.selectFrom(USER)
                .where(USER.ID.eq((int) id))
                .fetchOptional(userMappers::toUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return dsl.selectFrom(USER)
                .where(USER.EMAIL.eq(email))
                .fetchOptional(userMappers::toUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return dsl.fetchExists(USER, USER.EMAIL.eq(email));
    }

    @Override
    public User save(User user) {
        JqUserEntityRecord userRecord = userMappers.fromUser(user);
        // Automatically sets the id
        userRecord.store();

        return userMappers.toUser(userRecord);
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

        int insertResult = dsl.insertInto(USER_LIKES)
                .set(like)
                .execute();

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

    private boolean checkFavoriteExists(User user, String estateUrl) {
        var selectExists = dsl.selectFrom(USER_LIKES)
                .where(USER_LIKES.ID_USER.eq(user.getId().intValue()))
                .and(USER_LIKES.ESTATE_LINK.eq(estateUrl));
        return dsl.fetchExists(selectExists);
    }

    @Override
    public void addNavigation(User user, String estateUrl) {
        JqUserNavigationRecord navigation = new JqUserNavigationRecord(
                user.getId().intValue(),
                estateUrl,
                LocalDateTime.now()
        );

        int insertResult = dsl.insertInto(USER_NAVIGATION)
                .set(navigation)
                .execute();

        if (insertResult != 1) {
            throw new RuntimeException("Cannot insert the favorite : result code is 0");
        }
    }

    @Override
    public Optional<Profile> findMainProfile(User user) {
        return dsl.selectFrom(PROFILE)
                .where(PROFILE.ID_USER.eq(user.getId().intValue()))
                .and(PROFILE.IS_MAINPROFILE.isTrue())
                .fetchOptional(profileMappers::toProfile);
    }

    @Override
    public List<Profile> findProfileByUser(User user) {
        return dsl.selectFrom(PROFILE)
                .where(PROFILE.ID_USER.eq(user.getId().intValue()))
                .fetch(profileMappers::toProfile);
    }

    @Override
    public Optional<Profile> findProfileById(User user, long profileId) {
        return dsl.selectFrom(PROFILE)
                .where(PROFILE.ID_USER.eq(user.getId().intValue()))
                .and(PROFILE.ID_ENTRY.eq((int) profileId))
                .fetchOptional(profileMappers::toProfile);
    }

    @Override
    public void addProfile(User user, Profile profile) {
        var record = profileMappers.fromProfile(profile);

        // Check if user had any profile before
        boolean doUserHadAnyProfile = dsl.fetchExists(
                dsl.selectFrom(PROFILE).where(PROFILE.ID_USER.eq(user.getId().intValue()))
        );
        if (!doUserHadAnyProfile) {
            record.setIsMainprofile(true);
        }

        record.store();
    }

    @Override
    public void changeProfile(User user, long profileId, Profile profile) {
        var profileRecord = dsl.selectFrom(PROFILE)
                .where(PROFILE.ID_USER.eq(user.getId().intValue()))
                .and(PROFILE.ID_ENTRY.eq((int) profileId))
                .fetchOptional()
                .orElseThrow();
        profileRecord = profileMappers.updateRecord(profileRecord, profile);
        profileRecord.store();
    }

    @Override
    public void removeProfile(User user, long profileId) {
        var nbDelete = dsl.delete(PROFILE)
                .where(PROFILE.ID_USER.eq(user.getId().intValue()))
                .and(PROFILE.ID_ENTRY.eq((int) profileId))
                .execute();

        if (nbDelete != 1) {
            throw new IllegalArgumentException("No profile was deleted (got " + nbDelete + ")");
        }
    }

    @Override
    public void switchMainProfile(User user, long profileId) {
        // Find the main profile
        var mainProfile = dsl.selectFrom(PROFILE)
                .where(PROFILE.ID_USER.eq(user.getId().intValue()))
                .and(PROFILE.IS_MAINPROFILE.eq(true))
                .fetchOptional();

        mainProfile.ifPresentOrElse(mp -> {
            // Do nothing if same id
            if (mp.getIdEntry().equals((int) profileId)) {
                LOGGER.warn("Try to switch main with same id, do nothing");
                return;
            }
            // Can perform the switch
            mp.setIsMainprofile(false);
            mp.store();
            activateMainProfile(user.getId(), profileId);
        }, () -> activateMainProfile(user.getId(), profileId));
    }

    private void activateMainProfile(long userId, long profileId) {
        int nbUpdates = dsl.update(PROFILE)
                .set(PROFILE.IS_MAINPROFILE, true)
                .where(PROFILE.ID_USER.eq((int) userId))
                .and(PROFILE.ID_ENTRY.eq((int) profileId))
                .execute();

        if (nbUpdates != 1) {
            throw new IllegalArgumentException("No profile was set as main profile (got " + nbUpdates + ")");
        }
    }
}
