package com.github.m2cyurealestate.real_estate_back.config;

import com.github.m2cyurealestate.real_estate_back.config.properties.JwtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * @author Aldric Vitali Silvestre
 */
@Configuration
public class CryptographyConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CryptographyConfig.class);

    @Autowired
    private JwtProperties jwtProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public KeyStore keyStore() {
        try {
            File keyStoreFile = new File(jwtProperties.key().path());
            char[] password = jwtProperties.key().password().toCharArray();
            return KeyStore.getInstance(keyStoreFile, password);
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException exception) {
            LOGGER.error("Error while creating the key store : {}", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
