package com.github.m2cyurealestate.real_estate_back.persistence.jooq.estate;

import com.github.m2cyurealestate.real_estate_back.business.city.City;
import com.github.m2cyurealestate.real_estate_back.business.estate.RateClass;
import com.github.m2cyurealestate.real_estate_back.business.user.Profile;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqCitiesScoreTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqCitiesTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqEstateMlCTable;
import org.apache.commons.lang3.Range;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler of the multiple filters that can receive
 * the dao for a search by profile request.
 *
 * @author Aldric Vitali Silvestre
 */
public class JooqEstateProfileFilters {

    public static final int HOUSE_AREA_VARIATION = 30;
    public static final int ROOMS_VARIATION = 1;
    public static final int BEDROOMS_VARIATION = 1;
    public static final int BATHROOMS_VARIATION = 1;

    public static final JqEstateMlCTable ESTATE = JqEstateMlCTable.ESTATE_ML_C;

    public static final JqCitiesTable CITY = JqCitiesTable.CITIES;

    public static final JqCitiesScoreTable CITY_SCORE = JqCitiesScoreTable.CITIES_SCORE;

    public Condition createConditions(Profile profile, City city) {
        return DSL.and(
                createBudgetCondition(profile),
                createHouseAreaCondition(profile),
                createRoomsCondition(profile),
                createBedroomsCondition(profile),
                createBathoomsCondition(profile),
                createEnergyClassCondition(profile),
                createFeaturesCondition(profile),
                createScoresCondition(profile),
                createPositionCondition(profile, city)
        );
    }

    private Condition createBudgetCondition(Profile profile) {
        Range<Long> budgetRange = profile.getBudgetClass().getBudgetRange();
        return ESTATE.PRICE.between(budgetRange.getMinimum(), budgetRange.getMaximum());
    }

    private static Condition createHouseAreaCondition(Profile profile) {
        double houseArea = profile.getHouseArea();
        return ESTATE.SURFACE.between(houseArea - HOUSE_AREA_VARIATION,
                                      houseArea + HOUSE_AREA_VARIATION
        );
    }

    private Condition createRoomsCondition(Profile profile) {
        int rooms = profile.getRooms();
        return ESTATE.ROOM_NUMBER.between(rooms - ROOMS_VARIATION, rooms + ROOMS_VARIATION);
    }

    private Condition createBedroomsCondition(Profile profile) {
        int bedrooms = profile.getBedrooms();
        return ESTATE.BEDROOM_NUMBER.between(bedrooms - BEDROOMS_VARIATION, bedrooms + BEDROOMS_VARIATION);
    }

    private Condition createBathoomsCondition(Profile profile) {
        int bathrooms = profile.getBathrooms();
        return ESTATE.BATHROOM_NUMBER.between(bathrooms - BATHROOMS_VARIATION, bathrooms + BATHROOMS_VARIATION);
    }

    private Condition createEnergyClassCondition(Profile profile) {
        RateClass minEnergyClass = profile.getMinEnergyClass();
        List<String> wantedEnergyLabels = minEnergyClass.getGreaterClasses()
                .map(RateClass::name)
                .toList();
        return ESTATE.ENERGYCLASS.in(wantedEnergyLabels);
    }

    private Condition createFeaturesCondition(Profile profile) {
        // If not specified, it doesn't mean we don't want them !
        List<Condition> conditions = new ArrayList<>();
        if (profile.isBalcony()) {
            conditions.add(ESTATE.ISBALCONY.isTrue());
        }
        if (profile.isFittedKitchen()) {
            conditions.add(ESTATE.ISSPECIALKITCHEN.isTrue());
        }

        return DSL.and(conditions);
    }

    private Condition createScoresCondition(Profile profile) {
        return CITY_SCORE.SECURITY_SCORE.ge(profile.getScoreSecurity().doubleValue())
                .and(CITY_SCORE.EDUCATION_SCORE.ge(profile.getScoreEducation().doubleValue()))
                .and(CITY_SCORE.HOBBIES_SCORE.ge(profile.getScoreHobbies().doubleValue()))
                .and(CITY_SCORE.ENVIRONMENT_SCORE.ge(profile.getScoreEnvironment().doubleValue()))
                .and(CITY_SCORE.PRACTICALITY_SCORE.ge(profile.getScorePracticality().doubleValue()));

    }

    private Condition createPositionCondition(Profile profile, City city) {
        /* Credits for reddit : https://stackoverflow.com/a/24372831
         * raw SQL query :
         * 111.111 *
         *     DEGREES(ACOS(LEAST(1.0, COS(RADIANS(a.Latitude))
         *          * COS(RADIANS(b.Latitude))
         *          * COS(RADIANS(a.Longitude - b.Longitude))
         *          + SIN(RADIANS(a.Latitude))
         *          * SIN(RADIANS(b.Latitude))))) AS distance_in_km
         */

        double KILOMETERS_PER_DEGREE = 111.111;

        BigDecimal latitude = city.latitude();
        BigDecimal longitude = city.longitude();

        Field<BigDecimal> baseAngle = DSL.cos(DSL.rad(CITY.LATITUDE))
                .mul(DSL.cos(DSL.rad(latitude)))
                .mul(DSL.cos(DSL.rad(CITY.LONGITUDE.minus(longitude)))
                             .plus(DSL.sin(DSL.rad(CITY.LATITUDE))
                                           .mul(DSL.sin(DSL.rad(latitude))))
                );

        var acos = DSL.acos(DSL.least(DSL.val(BigDecimal.ONE), baseAngle));

        Field<Double> distanceKilometers = DSL.val(KILOMETERS_PER_DEGREE).mul(DSL.deg(acos));

        return distanceKilometers.lt((double) profile.getAcceptableDistance());
    }

}
