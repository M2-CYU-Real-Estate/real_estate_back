package com.github.m2cyurealestate.real_estate_back.dao.estate;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
public record EstateStatistics(
        BigDecimal meanPriceBigCities,
        BigDecimal meanPriceApartment,
        BigDecimal meanPriceHouse
) {
}
