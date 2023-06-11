package com.github.m2cyurealestate.real_estate_back.business.user;

import com.github.m2cyurealestate.real_estate_back.business.estate.RateClass;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
public class Profile {

    private Long id;

    private long userId;

    private boolean isMainProfile;

    private String name;

    private BudgetClass budgetClass;

    private String postalCode;

    private int acceptableDistance;

    private int houseArea;

    private int rooms;

    private int bedrooms;

    private int bathrooms;

    private RateClass minEnergyClass;

    private boolean balcony;

    private boolean fittedKitchen;

    private BigDecimal scoreSecurity;

    private BigDecimal scoreEducation;

    private BigDecimal scoreHobbies;

    private BigDecimal scoreEnvironment;

    private BigDecimal scorePracticality;

    public Profile() {
    }

    public Profile(Long id,
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
                   BigDecimal scorePracticality) {
        this.id = id;
        this.userId = userId;
        this.isMainProfile = isMainProfile;
        this.name = name;
        this.budgetClass = budgetClass;
        this.postalCode = postalCode;
        this.acceptableDistance = acceptableDistance;
        this.houseArea = houseArea;
        this.rooms = rooms;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.minEnergyClass = minEnergyClass;
        this.balcony = balcony;
        this.fittedKitchen = fittedKitchen;
        this.scoreSecurity = scoreSecurity;
        this.scoreEducation = scoreEducation;
        this.scoreHobbies = scoreHobbies;
        this.scoreEnvironment = scoreEnvironment;
        this.scorePracticality = scorePracticality;
    }

    public Profile(long userId,
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
                   BigDecimal scorePracticality) {
        this.userId = userId;
        this.isMainProfile = isMainProfile;
        this.name = name;
        this.budgetClass = budgetClass;
        this.postalCode = postalCode;
        this.acceptableDistance = acceptableDistance;
        this.houseArea = houseArea;
        this.rooms = rooms;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.minEnergyClass = minEnergyClass;
        this.balcony = balcony;
        this.fittedKitchen = fittedKitchen;
        this.scoreSecurity = scoreSecurity;
        this.scoreEducation = scoreEducation;
        this.scoreHobbies = scoreHobbies;
        this.scoreEnvironment = scoreEnvironment;
        this.scorePracticality = scorePracticality;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isMainProfile() {
        return isMainProfile;
    }

    public void setMainProfile(boolean mainProfile) {
        isMainProfile = mainProfile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BudgetClass getBudgetClass() {
        return budgetClass;
    }

    public void setBudgetClass(BudgetClass budgetClass) {
        this.budgetClass = budgetClass;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getAcceptableDistance() {
        return acceptableDistance;
    }

    public void setAcceptableDistance(int acceptableDistance) {
        this.acceptableDistance = acceptableDistance;
    }

    public int getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(int houseArea) {
        this.houseArea = houseArea;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public RateClass getMinEnergyClass() {
        return minEnergyClass;
    }

    public void setMinEnergyClass(RateClass minEnergyClass) {
        this.minEnergyClass = minEnergyClass;
    }

    public boolean isBalcony() {
        return balcony;
    }

    public void setBalcony(boolean balcony) {
        this.balcony = balcony;
    }

    public boolean isFittedKitchen() {
        return fittedKitchen;
    }

    public void setFittedKitchen(boolean fittedKitchen) {
        this.fittedKitchen = fittedKitchen;
    }

    public BigDecimal getScoreSecurity() {
        return scoreSecurity;
    }

    public void setScoreSecurity(BigDecimal scoreSecurity) {
        this.scoreSecurity = scoreSecurity;
    }

    public BigDecimal getScoreEducation() {
        return scoreEducation;
    }

    public void setScoreEducation(BigDecimal scoreEducation) {
        this.scoreEducation = scoreEducation;
    }

    public BigDecimal getScoreHobbies() {
        return scoreHobbies;
    }

    public void setScoreHobbies(BigDecimal scoreHobbies) {
        this.scoreHobbies = scoreHobbies;
    }

    public BigDecimal getScoreEnvironment() {
        return scoreEnvironment;
    }

    public void setScoreEnvironment(BigDecimal scoreEnvironment) {
        this.scoreEnvironment = scoreEnvironment;
    }

    public BigDecimal getScorePracticality() {
        return scorePracticality;
    }

    public void setScorePracticality(BigDecimal scorePracticality) {
        this.scorePracticality = scorePracticality;
    }
}
