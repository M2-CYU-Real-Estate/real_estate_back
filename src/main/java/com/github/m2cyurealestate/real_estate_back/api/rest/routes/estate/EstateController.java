package com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.page.PageParams;
import com.github.m2cyurealestate.real_estate_back.api.rest.page.RespPage;
import com.github.m2cyurealestate.real_estate_back.api.rest.utils.Delayer;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.services.estate.EstateService;
import com.github.m2cyurealestate.real_estate_back.services.suggestion.SuggestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Aldric Vitali Silvestre
 */
@RestController
@RequestMapping("estates")
public class EstateController {

    private final EstateService estateService;

    private final SuggestionsService suggestionsService;

    public EstateController(EstateService estateService, SuggestionsService suggestionsService) {
        this.estateService = estateService;
        this.suggestionsService = suggestionsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespEstate> getEstateById(@PathVariable("id") long id) throws Exception {
        var estate = estateService.getById(id);
        return ResponseEntity.ok(new RespEstate(estate));
    }

    @GetMapping("/{id}/advice")
    public ResponseEntity<RespAdvice> getEstateAdvice(@PathVariable("id") long id) throws Exception {
        Delayer delayer = new Delayer(1500, 500);
        var advice = estateService.getAdvices(id);
        delayer.applyDelay();
        return ResponseEntity.ok(advice);
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<RespStatistics> getEstateStatistics(@PathVariable("id") long id) throws Exception {
        Delayer delayer = new Delayer(500, 50);
        var stats = estateService.getStatistics(id);
        delayer.applyDelay();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("")
    public ResponseEntity<RespPage<RespEstate>> getEstatesPage(
            PageParams pageParams,
            EstateFiltersParams filtersParams
    ) throws Exception {
        Page<Estate> page = estateService.getPage(pageParams, filtersParams);
        return ResponseEntity.ok(RespPage.of(page, RespEstate::new));
    }

    @GetMapping("/positions")
    public ResponseEntity<List<RespEstatePosition>> getEstatesPositions() throws Exception {
        var positions = estateService.getAllEstatePositions();
        return ResponseEntity.ok(positions.stream().map(RespEstatePosition::new).toList());
    }

    @GetMapping("/search-profile")
    public ResponseEntity<RespPage<RespEstate>> searchByProfile(
            PageParams pageParams,
            ReqSearchByProfile request
    ) throws Exception {
        var page = estateService.getByProfile(pageParams, request);
        return ResponseEntity.ok(RespPage.of(page, RespEstate::new));
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<RespEstate>> getUserSuggestions() throws Exception {
        var suggestions = suggestionsService.getSuggestions()
                .stream()
                .map(RespEstate::new)
                .toList();
        return ResponseEntity.ok(suggestions);
    }
}
