package com.github.m2cyurealestate.real_estate_back.services.prediction;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
@Service
public interface PredictionService {

    BigDecimal predictSellingPrice(PredictionInput input);

}
