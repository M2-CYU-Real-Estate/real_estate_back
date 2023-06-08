package com.github.m2cyurealestate.real_estate_back.api.rest.routes.prediction;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
public record ReqPrediction(
        long houseArea,
        long terrainArea,
        int roomCount,
        int bathroomCount,
        String latitude,
        String longitude
) {
}
