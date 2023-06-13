package com.github.m2cyurealestate.real_estate_back.services.prediction.webservice;

import com.github.m2cyurealestate.real_estate_back.services.ml_webservice.MLWebserviceAccessor;
import com.github.m2cyurealestate.real_estate_back.services.prediction.PredictionInput;
import com.github.m2cyurealestate.real_estate_back.services.prediction.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Implementation of {@link PredictionService} that will give the request
 * to an external web service.
 *
 * @author Aldric Vitali Silvestre
 */
@Service
public class ExternalRestPredictionService implements PredictionService {

    private final MLWebserviceAccessor webservice;

    @Autowired
    public ExternalRestPredictionService(MLWebserviceAccessor webservice) {
        this.webservice = webservice;
    }

    @Override
    public BigDecimal predictSellingPrice(PredictionInput input) {
        WsPredictionReq wsPredictionReq = new WsPredictionReq(input);

        WsPredictionResp resp = webservice.postRequest(webservice.getPredictionUrl(),
                                                       wsPredictionReq,
                                                       WsPredictionResp.class
        );
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
