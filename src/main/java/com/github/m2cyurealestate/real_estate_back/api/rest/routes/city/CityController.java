package com.github.m2cyurealestate.real_estate_back.api.rest.routes.city;

import com.github.m2cyurealestate.real_estate_back.services.city.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Aldric Vitali Silvestre
 */
@RestController
@RequestMapping("cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("")
    public ResponseEntity<List<RespCity>> getCities() throws Exception {
        var resp = cityService.getCities()
                .stream()
                .map(RespCity::new)
                .toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/insee/{insee}")
    public ResponseEntity<RespCity> getCityByInseeCode(@PathVariable("insee") String inseeCode) throws Exception {
        var city = cityService.getCityByInseeCode(inseeCode);
        return ResponseEntity.ok(new RespCity(city));
    }

    @GetMapping("/postal/{postalCode}")
    public ResponseEntity<RespCity> getCityByPostalCode(@PathVariable("postalCode") String postalCode) throws Exception {
        var city = cityService.getCityByPostalCode(postalCode);
        return ResponseEntity.ok(new RespCity(city));
    }

}
