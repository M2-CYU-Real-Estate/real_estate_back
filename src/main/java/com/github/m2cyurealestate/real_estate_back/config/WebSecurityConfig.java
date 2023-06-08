package com.github.m2cyurealestate.real_estate_back.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m2cyurealestate.real_estate_back.api.rest.UnauthorizedEntryPoint;
import com.github.m2cyurealestate.real_estate_back.config.properties.JwtProperties;
import com.github.m2cyurealestate.real_estate_back.security.jwt.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

/**
 * The main configuration class for handling all settings for the web part
 * of the server to work properly.
 *
 * @author Aldric Vitali Silvestre
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

    // We can define a list of strings in the application.properties file
    // By default, set to { "*" }
    @Value("${corsFilter.allowOrigin:*}")
    private String[] corsFilterAllowOrigin;

    @Autowired
    private JwtProperties jwtProperties;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {
        return new UnauthorizedEntryPoint(objectMapper);
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder,
                                        UserDetailsService userDetailsService,
                                        PasswordEncoder passwordEncoder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           AuthenticationEntryPoint entryPoint,
                                           JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {
        return httpSecurity
                .addFilterBefore(createEncodingFilter(), CsrfFilter.class)
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource()).and()
                .authorizeHttpRequests()
                .requestMatchers("*").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .headers().cacheControl()
                .and().and()
                .build();
    }

    private CharacterEncodingFilter createEncodingFilter() {
        return new CharacterEncodingFilter("UTF-8", true);
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        List<String> origins = formatOrigins();

        configuration.setAllowedOriginPatterns(origins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setMaxAge(3600L);
        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(List.of("x-requested-with",
//                                                "Content-type",
//                                                "Cookie",
//                                                "Accept",
//                                                jwtProperties.header(),
//                                                "X-CSRF-TOKEN"
//        ));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", configuration);
        return configurationSource;
    }

    private List<String> formatOrigins() {
        var origins = Arrays.asList(corsFilterAllowOrigin);
        if (origins.isEmpty()) {
            return List.of("*");
        }
        return origins;
    }
}
