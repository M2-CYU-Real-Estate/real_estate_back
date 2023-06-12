package com.github.m2cyurealestate.real_estate_back.api.rest.routes.user;

import com.github.m2cyurealestate.real_estate_back.business.estate.RateClass;
import com.github.m2cyurealestate.real_estate_back.business.user.BudgetClass;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
public record ReqCreateProfile(
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
}
