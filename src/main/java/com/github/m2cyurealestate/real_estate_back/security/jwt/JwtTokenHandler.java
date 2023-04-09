package com.github.m2cyurealestate.real_estate_back.security.jwt;

import com.github.m2cyurealestate.real_estate_back.config.properties.JwtProperties;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/**
 * @author Aldric Vitali Silvestre
 */
@Component
public class JwtTokenHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenHandler.class);

    private final JwtProperties properties;

    private final KeyStore keyStore;

    @Autowired
    public JwtTokenHandler(JwtProperties properties, KeyStore keyStore) {
        this.properties = properties;
        this.keyStore = keyStore;
    }

    public String generateToken(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new IllegalStateException("Provided authentication does not have the user details");
        }
        Instant issuedAt = Instant.now();
        return Jwts.builder()
                .setIssuer("com.github.m2cyurealestate.real_estate_back")
                .setId(UUID.randomUUID().toString())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(issuedAt.plusSeconds(properties.expirationSeconds())))
                .signWith(properties.key().signatureAlgorithm(), getSigningKey())
                .compact();
    }

    public String fetchUserName(String token) {
        return parseClaimsFromToken(token).getSubject();
    }

    public LocalDateTime fetchExpirationDate(String token) {
        return parseClaimsFromToken(token).getExpiration()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private Claims parseClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        try {
            return keyStore.getKey(properties.key().alias(),
                                   properties.key().password().toCharArray()
            );
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException exception) {
            LOGGER.error("Error while trying to fetch key from keystore : {}", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
