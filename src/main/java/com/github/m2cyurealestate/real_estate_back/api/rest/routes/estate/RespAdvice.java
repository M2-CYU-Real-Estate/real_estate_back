package com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate;

import com.github.m2cyurealestate.real_estate_back.business.Month;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Aldric Vitali Silvestre
 */
public record RespAdvice(
       BigDecimal estimatedPrice,
       BigDecimal minPrice,
       BigDecimal maxPrice,
       BigDecimal meanPrice,
       Map<Month, BigDecimal> pricePerMonth
) {
}
