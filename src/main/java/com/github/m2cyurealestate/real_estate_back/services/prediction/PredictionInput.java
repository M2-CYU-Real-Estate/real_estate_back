package com.github.m2cyurealestate.real_estate_back.services.prediction;

import com.github.m2cyurealestate.real_estate_back.business.city.City;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
public record PredictionInput(
        long houseAreaSqrtM,
        long terrainAreaSqrtM,
        int roomCount,
        int bathroomCount,
        BigDecimal latitude,
        BigDecimal longitude
) {

    public static PredictionInput fromModels(Estate estate, City city) {
        return new PredictionInput(
                estate.getHouseAreaSqrtM(),
                estate.getGroundAreaSqrtM(),
                estate.getRoomCount(),
                estate.getBathroomCount(),
                city.latitude(),
                city.longitude()
        );
    }
}
