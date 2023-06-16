package com.github.m2cyurealestate.real_estate_back.business;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
public enum Month {
    JANUARY(0.954760),
    FEBRUARY(0.954749),
    MARCH(0.953139),
    APRIL(0.964936),
    MAY(0.962224),
    JUNE(1.0),
    JULY(1.065636),
    AUGUST(1.052398),
    SEPTEMBER(1.056633),
    OCTOBER(1.051678),
    NOVEMBER(1.034279),
    DECEMBER(1.028894);

    private final double variation;

    Month(double variation) {
        this.variation = variation;
    }

    public double getVariation() {
        return variation;
    }

    public BigDecimal applyVariation(BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(getVariation()));
    }
}
