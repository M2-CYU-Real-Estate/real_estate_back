package com.github.m2cyurealestate.real_estate_back.business.city;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Aldric Vitali Silvestre
 */
public record City(
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
        CityScores scores,
        CityScores normalizedScores
) {
}
