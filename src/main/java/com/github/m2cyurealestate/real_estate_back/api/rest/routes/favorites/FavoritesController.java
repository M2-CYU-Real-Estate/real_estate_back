package com.github.m2cyurealestate.real_estate_back.api.rest.routes.favorites;

import com.github.m2cyurealestate.real_estate_back.api.rest.page.PageParams;
import com.github.m2cyurealestate.real_estate_back.api.rest.page.RespPage;
import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.RespEstate;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.services.estate.EstateService;
import com.github.m2cyurealestate.real_estate_back.services.user.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aldric Vitali Silvestre
 */
@RestController
@RequestMapping("favorites")
public class FavoritesController {

    private final UserService userService;

    private final EstateService estateService;

    @Autowired
    public FavoritesController(UserService userService, EstateService estateService) {
        this.userService = userService;
        this.estateService = estateService;
    }

    @GetMapping("")
    public ResponseEntity<RespPage<RespEstate>> getFavorites(PageParams pageParams) throws Exception {
        Page<Estate> favorites = estateService.getFavorites(pageParams);
        return ResponseEntity.ok(RespPage.of(favorites, RespEstate::new));
    }

    @PostMapping("")
    public ResponseEntity<Void> addFavorite(@RequestBody ReqAddFavorite request) throws Exception {
        userService.addFavorite(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> removeFavorite(@RequestParam("url") String estateUrl) throws Exception {
        userService.removeFavorite(new ReqAddFavorite(estateUrl));
        return ResponseEntity.ok().build();
    }
}
