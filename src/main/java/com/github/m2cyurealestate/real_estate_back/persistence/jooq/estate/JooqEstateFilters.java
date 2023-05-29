package com.github.m2cyurealestate.real_estate_back.persistence.jooq.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.EstateFiltersParams;
import com.github.m2cyurealestate.real_estate_back.business.estate.EstateType;
import com.github.m2cyurealestate.real_estate_back.business.estate.RateClass;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqEstateTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqEstateRecord;
import org.jooq.Condition;
import org.jooq.SelectQuery;
import org.jooq.impl.SQLDataType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Handler of the multiple filters that can receive the dao.
 *
 * @author Aldric Vitali Silvestre
 */
class JooqEstateFilters {

    public static final JqEstateTable ESTATE = JqEstateTable.ESTATE;

    /**
     * Apply any filter found in the filter params in the created query
     * @param filtersParams the filters to apply
     * @param query the query to add filter clauses
     */
    public void applyFilters(EstateFiltersParams filtersParams, SelectQuery<JqEstateRecord> query) {
        filtersParams.getType().ifPresent(addTypeFilter(query));
        filtersParams.getCity().ifPresent(addCityFilter(query));

        filtersParams.getMinPr().ifPresent(addMinPriceFilter(query));
        filtersParams.getMaxPr().ifPresent(addMaxPriceFilter(query));

        filtersParams.getMinHArea().ifPresent(addMinHouseAreaFilter(query));
        filtersParams.getMaxHArea().ifPresent(addMaxHouseAreaFilter(query));

        filtersParams.getTerrace().ifPresent(addTerraceFilter(query));
        filtersParams.getBalcony().ifPresent(addBalconyFilter(query));
        filtersParams.getParking().ifPresent(addParkingFilter(query));
        filtersParams.getGarage().ifPresent(addGarageFilter(query));
        filtersParams.getfKitchen().ifPresent(addFittedKitchenFilter(query));
        filtersParams.getElevator().ifPresent(addElevatorFilter(query));

        filtersParams.getEnClass().ifPresent(addEnergyClassFilter(query));
        filtersParams.getGzClass().ifPresent(addGazEmissionFilter(query));
    }

//    public void addCondition(Condition)

    // ==== Filter functions ====

    private Consumer<EstateType> addTypeFilter(SelectQuery<JqEstateRecord> query) {
        return t -> {
            JooqEstateType.findFilterName(t)
                    .ifPresent(f -> query.addConditions(ESTATE.TYPE_ESTATE.eq(f)));
        };
    }

    private Consumer<String> addCityFilter(SelectQuery<JqEstateRecord> query) {
        return cityName -> query.addConditions(ESTATE.CITY_NAME.eq(cityName));
    }

    private Consumer<Long> addMinPriceFilter(SelectQuery<JqEstateRecord> query) {
        return minPr -> query.addConditions(ESTATE.PRICE.ge(minPr));
    }

    private Consumer<Long> addMaxPriceFilter(SelectQuery<JqEstateRecord> query) {
        return maxPr -> query.addConditions(ESTATE.PRICE.le(maxPr));
    }

    private Consumer<Long> addMinHouseAreaFilter(SelectQuery<JqEstateRecord> query) {
        return minArea -> query.addConditions(ESTATE.SURFACE.cast(SQLDataType.BIGINT).ge(minArea));
    }

    private Consumer<Long> addMaxHouseAreaFilter(SelectQuery<JqEstateRecord> query) {
        return maxArea -> query.addConditions(ESTATE.SURFACE.cast(SQLDataType.BIGINT).ge(maxArea));
    }

    private Consumer<Boolean> addTerraceFilter(SelectQuery<JqEstateRecord> query) {
        return wanted -> {
            if (wanted) {
                query.addConditions(ESTATE.ISTERRACE.eq(true));
            }
        };
    }

    private Consumer<Boolean> addBalconyFilter(SelectQuery<JqEstateRecord> query) {
        return wanted -> {
            if (wanted) {
                query.addConditions(ESTATE.ISBALCONY.eq(true));
            }
        };
    }
    private Consumer<Boolean> addParkingFilter(SelectQuery<JqEstateRecord> query) {
        return wanted -> {
            if (wanted) {
                query.addConditions(ESTATE.ISPARKING.eq(true));
            }
        };
    }

    private Consumer<Boolean> addGarageFilter(SelectQuery<JqEstateRecord> query) {
        return wanted -> {
            if (wanted) {
                query.addConditions(ESTATE.ISGARAGE.eq(true));
            }
        };
    }

    private Consumer<Boolean> addFittedKitchenFilter(SelectQuery<JqEstateRecord> query) {
        return wanted -> {
            if (wanted) {
                query.addConditions(ESTATE.ISSPECIALKITCHEN.eq(true));
            }
        };
    }

    private Consumer<Boolean> addElevatorFilter(SelectQuery<JqEstateRecord> query) {
        return wanted -> {
            if (wanted) {
                query.addConditions(ESTATE.ISELEVATOR.eq(true));
            }
        };
    }

    private Consumer<RateClass> addEnergyClassFilter(SelectQuery<JqEstateRecord> query) {
        return rateClass -> {
            if (rateClass.isSpecified()) {
                List<String> acceptableRates = createAcceptableRates(rateClass);
                query.addConditions(ESTATE.ENERGYCLASS.in(acceptableRates));
            }
        };
    }

    private Consumer<RateClass> addGazEmissionFilter(SelectQuery<JqEstateRecord> query) {
        return rateClass -> {
            if (rateClass.isSpecified()) {
                List<String> acceptableRates = createAcceptableRates(rateClass);
                query.addConditions(ESTATE.GAZEMISSION.in(acceptableRates));
            }
        };
    }

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
