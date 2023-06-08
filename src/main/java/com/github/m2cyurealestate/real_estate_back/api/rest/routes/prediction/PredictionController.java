package com.github.m2cyurealestate.real_estate_back.api.rest.routes.prediction;

import com.github.m2cyurealestate.real_estate_back.api.rest.utils.Delayer;
import com.github.m2cyurealestate.real_estate_back.services.prediction.PredictionInput;
import com.github.m2cyurealestate.real_estate_back.services.prediction.PredictionService;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
@RestController
@RequestMapping("prediction")
public class PredictionController {

    public static final int MINIMUM_DELAY = 1000;
    private final PredictionService predictionService;

    @Autowired
    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping("")
    public ResponseEntity<RespPrediction> predictPrice(@RequestBody ReqPrediction request) throws Exception {
        var delayer = new Delayer(1000, 200);
        var input = new PredictionInput(
                request.houseArea(),
                request.terrainArea(),
                request.roomCount(),
                request.bathroomCount(),
                new BigDecimal(request.latitude()),
                new BigDecimal(request.longitude())
        );
        var price = predictionService.predictSellingPrice(input);
        delayer.applyDelay();
        return ResponseEntity.ok(new RespPrediction(price));
    }
}
