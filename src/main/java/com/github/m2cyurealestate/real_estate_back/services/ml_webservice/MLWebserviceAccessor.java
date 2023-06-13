package com.github.m2cyurealestate.real_estate_back.services.ml_webservice;

import com.github.m2cyurealestate.real_estate_back.config.properties.WebserviceProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The main accessor for the smart webservice
 *
 * @author Aldric Vitali Silvestre
 */
@Service
public class MLWebserviceAccessor {

    private final WebClient webclient;

    public MLWebserviceAccessor(WebserviceProperties webserviceProperties) {
        webclient = WebClient.builder()
                .baseUrl(webserviceProperties.url())
                .build();
    }

    public <U, T> T postRequest(String url, U body, Class<T> responseBodyType) {
        return webclient.post()
                .uri(url)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseBodyType)
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Incorrect response from webservice was given"));
    }

    public String getPredictionUrl() {
        return "/predict-price";
    }

    public String getSuggestionsUrl() {
        return "/get-suggestions";
    }
}
