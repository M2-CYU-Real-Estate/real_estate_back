package com.github.m2cyurealestate.real_estate_back.persistence.jooq.city;

import com.github.m2cyurealestate.real_estate_back.business.city.City;
import com.github.m2cyurealestate.real_estate_back.dao.city.CityDao;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqCitiesScoreTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqCitiesTable;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectOnConditionStep;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
@Repository
public class JooqCityDao implements CityDao {

    public static final JqCitiesTable CITY = JqCitiesTable.CITIES;

    public static final JqCitiesScoreTable CITY_SCORE = JqCitiesScoreTable.CITIES_SCORE;

    private final DSLContext dsl;

    private final JooqCityMappers cityMappers;

    public JooqCityDao(DSLContext dsl) {
        this.dsl = dsl;
        cityMappers = new JooqCityMappers();
    }

    @Override
    public List<City> findAll() {
        return selectAll()
                .fetch(cityMappers::toCity);
    }

    @Override
    public Optional<City> findByPostalCode(String postalCode, Optional<String> cityName) {
        var select = selectAll()
                .where(CITY.POSTAL_CODE.eq(postalCode));

        var select2 = cityName
                .map(n -> select.orderBy(DSL.field("similarity ({0}, {1})", Double.class, DSL.value(n), CITY.CITY_NAME).desc()))
                .orElseGet(() -> select.orderBy(CITY.LATITUDE.cast(Double.class)));

        return select2
                .limit(1)
                .fetchOptional(cityMappers::toCity);
    }

    @Override
    public Optional<City> findByPostalCode(String postalCode) {
        return selectAll()
                .where(CITY.POSTAL_CODE.eq(postalCode))
                .limit(1)
                .fetchOptional(cityMappers::toCity);
    }

    @Override
    public Optional<City> findByInseeCode(String inseeCode) {
        return selectAll()
                .where(CITY.INSEE_CODE.eq(inseeCode))
                .limit(1)
                .fetchOptional(cityMappers::toCity);
    }

    private SelectOnConditionStep<Record> selectAll() {
        return dsl.select(CITY.asterisk(), CITY_SCORE.asterisk())
                .from(CITY)
                .innerJoin(CITY_SCORE)
                .on(CITY_SCORE.POSTAL_CODE.eq(CITY.POSTAL_CODE));
    }
}
