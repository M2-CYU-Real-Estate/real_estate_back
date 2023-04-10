package com.github.m2cyurealestate.real_estate_back.persistence.jooq.user;

import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.business.user.UserRole;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqUserEntityTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqUserEntityRecord;
import org.jooq.DSLContext;

/**
 * @author Aldric Vitali Silvestre
 */
public class JooqUserMappers {

    private final DSLContext dsl;

    public JooqUserMappers(DSLContext dsl) {
        this.dsl = dsl;
    }

    public User toUser(JqUserEntityRecord record) {
        return new User(
                record.getId().longValue(),
                record.getName(),
                record.getEmail(),
                record.getPassword(),
                UserRole.valueOf(record.getRole()),
                record.getCreationDate(),
                record.getLastLoginDate(),
                record.getLastPasswordResetDate()
        );
    }

    public JqUserEntityRecord fromUser(User user) {
        var userRecord = dsl.newRecord(JqUserEntityTable.USER_ENTITY);
        userRecord.setName(user.getName());
        userRecord.setEmail(user.getEmail());
        userRecord.setRole(user.getRole().name());
        userRecord.setCreationDate(user.getCreationDate());
        userRecord.setLastLoginDate(user.getLastLoginDate());
        userRecord.setLastPasswordResetDate(user.getLastPasswordResetDate());
        userRecord.setPassword(user.getPassword());

        return userRecord;
    }
}
