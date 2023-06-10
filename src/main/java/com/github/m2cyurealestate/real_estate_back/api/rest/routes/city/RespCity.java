package com.github.m2cyurealestate.real_estate_back.api.rest.routes.city;

import com.github.m2cyurealestate.real_estate_back.business.city.City;
import com.github.m2cyurealestate.real_estate_back.business.city.CityScores;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
public record RespCity(
        String name,
        String simpleName,
        String postalCode,
        String inseeCode,
        String department,
        String departmentNumber,
        String region,
        int regionNumber,
        BigDecimal latitude,
        BigDecimal longitude,
        RespCityScores scores,
        RespCityScores normalizedScores
) {

    public RespCity(City city) {
        this(city.name(),
             city.simpleName(),
             city.postalCode(),
             city.inseeCode(),
             city.department(),
             city.departmentNumber(),
             city.region(),
             city.regionNumber(),
             city.latitude(),
             city.longitude(),
             new RespCityScores(city.scores()),
             new RespCityScores(city.normalizedScores())
        );
    }

    public record RespCityScores(
            double security,
            double education,
            double hobbies,
            double environment,
            double practicality
    ) {
        public RespCityScores(CityScores cityScores) {
            this(cityScores.security(),
                 cityScores.education(),
                 cityScores.hobbies(),
                 cityScores.environment(),
                 cityScores.practicality()
            );
        }
    }
}
