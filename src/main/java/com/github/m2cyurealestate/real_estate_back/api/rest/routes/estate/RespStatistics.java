package com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
public record RespStatistics(
      BigDecimal meanScore,
      BigDecimal securityScore,
      BigDecimal educationScore,
      BigDecimal hobbiesScore,
      BigDecimal environmentScore,
      BigDecimal practicalityScore,
      BigDecimal meanPriceBigCities,
      BigDecimal meanPriceApartment,
      BigDecimal meanPriceHouse
) {
}
