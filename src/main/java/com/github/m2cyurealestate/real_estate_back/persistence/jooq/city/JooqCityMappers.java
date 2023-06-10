package com.github.m2cyurealestate.real_estate_back.persistence.jooq.city;

import com.github.m2cyurealestate.real_estate_back.business.city.City;
import com.github.m2cyurealestate.real_estate_back.business.city.CityScores;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqCitiesRecord;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqCitiesScoreRecord;
import org.jooq.Record;

/**
 * @author Aldric Vitali Silvestre
 */
public class JooqCityMappers {

    public City toCity(Record record) {
        JqCitiesRecord cityRecord = record.into(JqCitiesRecord.class);
        JqCitiesScoreRecord scoreRecord = record.into(JqCitiesScoreRecord.class);

        return new City(
                cityRecord.getCityName(),
                cityRecord.getSimplecityName(),
                cityRecord.getPostalCode(),
                cityRecord.getInseeCode(),
                cityRecord.getDepartment(),
                cityRecord.getDepartmentNumber(),
                cityRecord.getRegion(),
                cityRecord.getRegionNumber(),
                cityRecord.getLatitude(),
                cityRecord.getLongitude(),
                new CityScores(
                        scoreRecord.getSecurityScore(),
                        scoreRecord.getEducationScore(),
                        scoreRecord.getHobbiesScore(),
                        scoreRecord.getEnvironmentScore(),
                        scoreRecord.getPracticalityScore()

                ),
                new CityScores(
                        scoreRecord.getNsSecurityScore(),
                        scoreRecord.getNsEducationScore(),
                        scoreRecord.getNsHobbiesScore(),
                        scoreRecord.getNsEnvironmentScore(),
                        scoreRecord.getNsPracticalityScore()
                )
        );
    }
}
