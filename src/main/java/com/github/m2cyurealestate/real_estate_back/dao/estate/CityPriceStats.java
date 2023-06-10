package com.github.m2cyurealestate.real_estate_back.dao.estate;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
public record CityPriceStats(
        BigDecimal minPrice,
        BigDecimal maxPrice,
        BigDecimal meanPrice
) {
}
