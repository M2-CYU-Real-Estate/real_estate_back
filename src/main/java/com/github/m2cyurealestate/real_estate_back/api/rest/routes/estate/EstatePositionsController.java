package com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate;

import com.github.m2cyurealestate.real_estate_back.services.estate.EstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Aldric Vitali Silvestre
 */
@RestController
@RequestMapping("estate-positions")
public class EstatePositionsController {

    private final EstateService estateService;

    @Autowired
    public EstatePositionsController(EstateService estateService) {
        this.estateService = estateService;
    }

    @GetMapping("")
    public ResponseEntity<List<RespEstatePosition>> getEstatesPositions() throws Exception {
        var positions = estateService.getAllEstatePositions();
        return ResponseEntity.ok(positions.stream().map(RespEstatePosition::new).toList());
    }
}
