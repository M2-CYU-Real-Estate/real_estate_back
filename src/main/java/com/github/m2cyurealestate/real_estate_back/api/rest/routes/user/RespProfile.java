package com.github.m2cyurealestate.real_estate_back.api.rest.routes.user;

import com.github.m2cyurealestate.real_estate_back.business.estate.RateClass;
import com.github.m2cyurealestate.real_estate_back.business.user.BudgetClass;
import com.github.m2cyurealestate.real_estate_back.business.user.Profile;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
public record RespProfile(
        Long id,
        long userId,
        boolean isMainProfile,
        String name,
        BudgetClass budgetClass,
        String postalCode,
        int acceptableDistance,
        int houseArea,
        int rooms,
        int bedrooms,
        int bathrooms,
        RateClass minEnergyClass,
        boolean balcony,
        boolean fittedKitchen,
        BigDecimal scoreSecurity,
        BigDecimal scoreEducation,
        BigDecimal scoreHobbies,
        BigDecimal scoreEnvironment,
        BigDecimal scorePracticality
) {

    public RespProfile(Profile profile) {
        this(profile.getId(),
             profile.getUserId(),
             profile.isMainProfile(),
             profile.getName(),
             profile.getBudgetClass(),
             profile.getPostalCode(),
             profile.getAcceptableDistance(),
             profile.getHouseArea(),
             profile.getRooms(),
             profile.getBedrooms(),
             profile.getBathrooms(),
             profile.getMinEnergyClass(),
             profile.isBalcony(),
             profile.isFittedKitchen(),
             profile.getScoreSecurity(),
             profile.getScoreEducation(),
             profile.getScoreHobbies(),
             profile.getScoreEnvironment(),
             profile.getScorePracticality()
        );
    }
}
