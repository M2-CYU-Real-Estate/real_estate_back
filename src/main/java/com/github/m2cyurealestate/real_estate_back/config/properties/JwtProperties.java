package com.github.m2cyurealestate.real_estate_back.config.properties;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Aldric Vitali Silvestre
 */
@ConfigurationProperties("jwt")
public record JwtProperties(
        String header,
        long expirationSeconds,
        Key key
) {

    public record Key(
            String path,
            String alias,
            String password,
            String aliasPassword,
            SignatureAlgorithm signatureAlgorithm
    ) {
    }
}
