package com.github.m2cyurealestate.real_estate_back.services.prediction;

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
}
