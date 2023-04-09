package com.github.m2cyurealestate.real_estate_back.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Aldric Vitali Silvestre
 */
@ConfigurationProperties("server")
public record ServerProperties(
        String version,
        String urlFront
) {
}
