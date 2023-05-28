package com.github.m2cyurealestate.real_estate_back.api.rest.routes.health;

import com.github.m2cyurealestate.real_estate_back.config.properties.ServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author Aldric Vitali Silvestre
 */
@RestController
@RequestMapping("health")
public class HealthController {

    private final ServerProperties properties;

    @Autowired
    public HealthController(ServerProperties properties) {
        this.properties = properties;
    }

    @GetMapping("")
    public ResponseEntity<RespHealth> pingServer() throws Exception {
        return ResponseEntity.ok(new RespHealth(properties.version(), LocalDateTime.now(), true));
    }
}
