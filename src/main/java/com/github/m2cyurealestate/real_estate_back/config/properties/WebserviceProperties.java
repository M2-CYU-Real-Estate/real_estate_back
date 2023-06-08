package com.github.m2cyurealestate.real_estate_back.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Aldric Vitali Silvestre
 */
@ConfigurationProperties("webservice")
public record WebserviceProperties(
        String url
) {
}
