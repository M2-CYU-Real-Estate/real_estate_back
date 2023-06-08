package com.github.m2cyurealestate.real_estate_back.persistence.jooq.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.EstateFiltersParams;
import com.github.m2cyurealestate.real_estate_back.business.estate.EstateType;
import com.github.m2cyurealestate.real_estate_back.business.estate.RateClass;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.ConditionBuilder;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqEstateTable;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.util.Arrays;
import java.util.List;

/**
 * Handler of the multiple filters that can receive the dao.
 *
 * @author Aldric Vitali Silvestre
 */
class JooqEstateFilters {

    public static final JqEstateTable ESTATE = JqEstateTable.ESTATE;

    /**
     * Create the condition based on the filters provided.
     *
     * @param filtersParams the filters to apply
     * @return The condition from all sub-conditions present
     */
    public Condition createFilters(EstateFiltersParams filtersParams) {
        return new ConditionBuilder<>(filtersParams)
                .add(EstateFiltersParams::getType, this::createTypeCondition)
                .add(EstateFiltersParams::getCity, this::createCityCondition)
                .add(EstateFiltersParams::getMinPr, this::createMinPriceCondition)
                .add(EstateFiltersParams::getMaxPr, this::createMaxPriceCondition)
                .add(EstateFiltersParams::getMinHArea, this::createMinHouseAreaCondition)
                .add(EstateFiltersParams::getMaxHArea, this::createMaxHouseAreaCondition)
                .add(EstateFiltersParams::getTerrace, this::createTerraceCondition)
                .add(EstateFiltersParams::getBalcony, this::createBalconyCondition)
                .add(EstateFiltersParams::getParking, this::createParkingCondition)
                .add(EstateFiltersParams::getGarage, this::createGarageCondition)
                .add(EstateFiltersParams::getfKitchen, this::createFittedKitchenCondition)
                .add(EstateFiltersParams::getElevator, this::createElevatorCondition)
                .build();
    }

    // ==== Filter functions ====

    private Condition createTypeCondition(EstateType type) {
        return JooqEstateType.findFilterName(type)
                .map(ESTATE.TYPE_ESTATE::eq)
                .orElse(DSL.trueCondition());
    }

    private Condition createCityCondition(String city) {
        return ESTATE.CITY_NAME.eq(city);
    }

    private Condition createMinPriceCondition(Long minPrice) {
        return ESTATE.PRICE.ge(minPrice);
    }

    private Condition createMaxPriceCondition(Long maxPrice) {
        return ESTATE.PRICE.le(maxPrice);
    }

    private Condition createMinHouseAreaCondition(Long minArea) {
        return ESTATE.SURFACE.cast(SQLDataType.BIGINT).ge(minArea);
    }

    private Condition createMaxHouseAreaCondition(Long maxArea) {
        return ESTATE.SURFACE.cast(SQLDataType.BIGINT).le(maxArea);
    }

    private Condition createTerraceCondition(boolean wanted) {
        if (wanted) {
            return ESTATE.ISTERRACE.isTrue();
        }
        return DSL.trueCondition();
    }

    private Condition createBalconyCondition(boolean wanted) {
        if (wanted) {
            return ESTATE.ISBALCONY.isTrue();
        }
        return DSL.trueCondition();
    }

    private Condition createParkingCondition(boolean wanted) {
        if (wanted) {
            return ESTATE.ISPARKING.isTrue();
        }
        return DSL.trueCondition();
    }

    private Condition createGarageCondition(boolean wanted) {
        if (wanted) {
            return ESTATE.ISGARAGE.isTrue();
        }
        return DSL.trueCondition();
    }

    private Condition createFittedKitchenCondition(boolean wanted) {
        if (wanted) {
            return ESTATE.ISSPECIALKITCHEN.isTrue();
        }
        return DSL.trueCondition();
    }

    private Condition createElevatorCondition(boolean wanted) {
        if (wanted) {
            return ESTATE.ISELEVATOR.isTrue();
        }
        return DSL.trueCondition();
    }

    private Condition createEnergyClassCondition(RateClass rateClass) {
        if (rateClass.isSpecified()) {
            List<String> acceptableRates = createAcceptableRates(rateClass);
            return ESTATE.ENERGYCLASS.in(acceptableRates);
        }
        return DSL.trueCondition();
    }

    private Condition createGazEmissionClassCondition(RateClass rateClass) {
        if (rateClass.isSpecified()) {
            List<String> acceptableRates = createAcceptableRates(rateClass);
            return ESTATE.GAZEMISSION.in(acceptableRates);
        }
        return DSL.trueCondition();
    }

    // Old filter consumers
//    private Consumer<EstateType> addTypeFilter(SelectQuery<JqEstateRecord> query) {
//        return t -> {
//            JooqEstateType.findFilterName(t)
//                    .ifPresent(f -> query.addConditions(ESTATE.TYPE_ESTATE.eq(f)));
//        };
//    }

//    private Consumer<String> addCityFilter(SelectQuery<JqEstateRecord> query) {
//        return cityName -> query.addConditions(ESTATE.CITY_NAME.eq(cityName));
//    }

//    private Consumer<Long> addMinPriceFilter(SelectQuery<JqEstateRecord> query) {
//        return minPr -> query.addConditions(ESTATE.PRICE.ge(minPr));
//    }

//    private Consumer<Long> addMaxPriceFilter(SelectQuery<JqEstateRecord> query) {
//        return maxPr -> query.addConditions(ESTATE.PRICE.le(maxPr));
//    }

//    private Consumer<Long> addMinHouseAreaFilter(SelectQuery<JqEstateRecord> query) {
//        return minArea -> query.addConditions(ESTATE.SURFACE.cast(SQLDataType.BIGINT).ge(minArea));
//    }
//
//    private Consumer<Long> addMaxHouseAreaFilter(SelectQuery<JqEstateRecord> query) {
//        return maxArea -> query.addConditions(ESTATE.SURFACE.cast(SQLDataType.BIGINT).ge(maxArea));
//    }
//
//    private Consumer<Boolean> addTerraceFilter(SelectQuery<JqEstateRecord> query) {
//        return wanted -> {
//            if (wanted) {
//                query.addConditions(ESTATE.ISTERRACE.eq(true));
//            }
//        };
//    }
//
//    private Consumer<Boolean> addBalconyFilter(SelectQuery<JqEstateRecord> query) {
//        return wanted -> {
//            if (wanted) {
//                query.addConditions(ESTATE.ISBALCONY.eq(true));
//            }
//        };
//    }
//
//    private Consumer<Boolean> addParkingFilter(SelectQuery<JqEstateRecord> query) {
//        return wanted -> {
//            if (wanted) {
//                query.addConditions(ESTATE.ISPARKING.eq(true));
//            }
//        };
//    }
//
//    private Consumer<Boolean> addGarageFilter(SelectQuery<JqEstateRecord> query) {
//        return wanted -> {
//            if (wanted) {
//                query.addConditions(ESTATE.ISGARAGE.eq(true));
//            }
//        };
//    }
//
//    private Consumer<Boolean> addFittedKitchenFilter(SelectQuery<JqEstateRecord> query) {
//        return wanted -> {
//            if (wanted) {
//                query.addConditions(ESTATE.ISSPECIALKITCHEN.eq(true));
//            }
//        };
//    }
//
//    private Consumer<Boolean> addElevatorFilter(SelectQuery<JqEstateRecord> query) {
//        return wanted -> {
//            if (wanted) {
//                query.addConditions(ESTATE.ISELEVATOR.eq(true));
//            }
//        };
//    }
//
//    private Consumer<RateClass> addEnergyClassFilter(SelectQuery<JqEstateRecord> query) {
//        return rateClass -> {
//            if (rateClass.isSpecified()) {
//                List<String> acceptableRates = createAcceptableRates(rateClass);
//                query.addConditions(ESTATE.ENERGYCLASS.in(acceptableRates));
//            }
//        };
//    }
//
//    private Consumer<RateClass> addGazEmissionFilter(SelectQuery<JqEstateRecord> query) {
//        return rateClass -> {
//            if (rateClass.isSpecified()) {
//                List<String> acceptableRates = createAcceptableRates(rateClass);
//                query.addConditions(ESTATE.GAZEMISSION.in(acceptableRates));
//            }
//        };
//    }

    /**
     * In order to provide a valid filter, create a list of wanted strings
     * in order to transform a condition of type
     * <pre>"where rate is higher than C"</pre>
     * To :
     * <pre>"where rate is in list ['A', 'B']"</pre>
     */
    private List<String> createAcceptableRates(RateClass rateClass) {
        // Find all rates that are higher or equal
        return Arrays.stream(RateClass.values())
                .filter(r -> r.getRating() >= rateClass.getRating())
                .map(RateClass::name)
                .toList();
    }
}
