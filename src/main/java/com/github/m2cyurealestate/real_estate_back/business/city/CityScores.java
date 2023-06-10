package com.github.m2cyurealestate.real_estate_back.business.city;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
public record CityScores(
        double security,
        double education,
        double hobbies,
        double environment,
        double practicality
) {
}
