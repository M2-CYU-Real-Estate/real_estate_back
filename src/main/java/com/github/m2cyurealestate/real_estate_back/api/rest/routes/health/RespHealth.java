package com.github.m2cyurealestate.real_estate_back.api.rest.routes.health;

import java.time.LocalDateTime;

/**
 * @author Aldric Vitali Silvestre
 */
public record RespHealth(
        String version,
        LocalDateTime date,
        boolean isAlive
) {
}
