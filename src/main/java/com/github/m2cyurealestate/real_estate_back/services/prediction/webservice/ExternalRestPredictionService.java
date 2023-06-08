package com.github.m2cyurealestate.real_estate_back.services.prediction.webservice;

import com.github.m2cyurealestate.real_estate_back.config.properties.WebserviceProperties;
import com.github.m2cyurealestate.real_estate_back.services.prediction.PredictionInput;
import com.github.m2cyurealestate.real_estate_back.services.prediction.PredictionService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

/**
 * Implementation of {@link PredictionService} that will give the request
 * to an external web service.
 *
 * @author Aldric Vitali Silvestre
 */
@Service
public class ExternalRestPredictionService implements PredictionService {

    private final WebClient webclient;

    @Autowired
    public ExternalRestPredictionService(WebserviceProperties webserviceProperties) {
        webclient = WebClient.builder()
                .baseUrl(webserviceProperties.url())
                .build();
    }

    @Override
    public BigDecimal predictSellingPrice(PredictionInput input) {
        WsPredictionResp resp = webclient.post()
                .uri("/predict-price")
                .bodyValue(new WsPredictionReq(input))
                .retrieve()
                .bodyToMono(WsPredictionResp.class)
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Incorrect response from webservice was given"));

        return BigDecimal.valueOf(resp.predicted_value());
    }

    /**
     * Following definition of api class :
     * <pre>
     * class PricePredictionRequest(BaseModel):
     *     house_area: int
     *     ground_area: int
     *     rooms: int
     *     # Latitude and longitude in str format in order to avoid float imprecision
     *     latitude: str
     *     longitude: str
     * </pre>
     */
    record WsPredictionReq(
            long house_area,
            long ground_area,
            long rooms,
            String latitude,
            String longitude
    ) {
        public WsPredictionReq(PredictionInput input) {
            this(input.houseAreaSqrtM(),
                 input.terrainAreaSqrtM(),
                 input.roomCount(),
                 input.latitude().toPlainString(),
                 input.longitude().toPlainString()
            );
        }
    }

    /**
     * Following definition of api class :
     * <pre>
     * class PricePredictionResponse(BaseModel):
     *     predicted_value: int
     * </pre>
     */
    record WsPredictionResp(
            long predicted_value
    ) {
    }
}
