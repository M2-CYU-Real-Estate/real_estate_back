package com.github.m2cyurealestate.real_estate_back.persistence.jooq.user;

import com.github.m2cyurealestate.real_estate_back.business.estate.RateClass;
import com.github.m2cyurealestate.real_estate_back.business.user.BudgetClass;
import com.github.m2cyurealestate.real_estate_back.business.user.Profile;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqBuyingProfileEntryTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqBuyingProfileEntryRecord;
import org.jooq.DSLContext;

import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
public class JooqProfileMappers {

    private final DSLContext dsl;

    public JooqProfileMappers(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Profile toProfile(JqBuyingProfileEntryRecord record) {
        return new Profile(
                Optional.ofNullable(record.getIdEntry()).map(Integer::longValue).orElse(null),
                record.getIdUser(),
                record.getIsMainprofile(),
                record.getProfileName(),
                BudgetClass.fromString(record.getBudgetClass()).orElseThrow(),
                record.getPostalCode(),
                record.getAcceptableDistance(),
                record.getHouseArea(),
                record.getRooms(),
                record.getBedrooms(),
                record.getBathrooms(),
                RateClass.fromString(record.getMinimalEnergyClass()),
                record.getBalcony(),
                record.getSpecializedKitchen(),
                record.getScoreSecurity(),
                record.getScoreEducation(),
                record.getScoreHobbies(),
                record.getScoreEnvironment(),
                record.getScorePractical()
        );
    }

    public JqBuyingProfileEntryRecord fromProfile(Profile profile) {
        var record = dsl.newRecord(JqBuyingProfileEntryTable.BUYING_PROFILE_ENTRY);
//        record.setIdEntry(Optional.ofNullable(profile.getId()).map(Long::intValue).orElse(null));
        record.setIdUser((int) profile.getUserId());
        record.setIsMainprofile(profile.isMainProfile());
        record.setProfileName(profile.getName());
        record.setBudgetClass(profile.getBudgetClass().getGroupName());
        record.setPostalCode(profile.getPostalCode());
        record.setAcceptableDistance(profile.getAcceptableDistance());
        record.setHouseArea(profile.getHouseArea());
        record.setRooms(profile.getRooms());
        record.setBedrooms(profile.getBedrooms());
        record.setBathrooms(profile.getBathrooms());
        record.setMinimalEnergyClass(profile.getMinEnergyClass().name());
        record.setBalcony(profile.isBalcony());
        record.setSpecializedKitchen(profile.isFittedKitchen());
        record.setScoreSecurity(profile.getScoreSecurity());
        record.setScoreEducation(profile.getScoreEducation());
        record.setScoreHobbies(profile.getScoreHobbies());
        record.setScoreEnvironment(profile.getScoreEnvironment());
        record.setScorePractical(profile.getScorePracticality());

        return record;
    }

    public JqBuyingProfileEntryRecord updateRecord(JqBuyingProfileEntryRecord record, Profile profile) {
        record.setIsMainprofile(profile.isMainProfile());
        record.setProfileName(profile.getName());
        record.setBudgetClass(profile.getBudgetClass().getGroupName());
        record.setPostalCode(profile.getPostalCode());
        record.setAcceptableDistance(profile.getAcceptableDistance());
        record.setHouseArea(profile.getHouseArea());
        record.setRooms(profile.getRooms());
        record.setBedrooms(profile.getBedrooms());
        record.setBathrooms(profile.getBathrooms());
        record.setMinimalEnergyClass(profile.getMinEnergyClass().name());
        record.setBalcony(profile.isBalcony());
        record.setSpecializedKitchen(profile.isFittedKitchen());
        record.setScoreSecurity(profile.getScoreSecurity());
        record.setScoreEducation(profile.getScoreEducation());
        record.setScoreHobbies(profile.getScoreHobbies());
        record.setScoreEnvironment(profile.getScoreEnvironment());
        record.setScorePractical(profile.getScorePracticality());

        return record;
    }
}
