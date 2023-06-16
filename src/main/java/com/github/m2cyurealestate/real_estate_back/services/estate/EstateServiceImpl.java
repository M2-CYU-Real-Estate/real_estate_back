package com.github.m2cyurealestate.real_estate_back.services.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.page.PageParams;
import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.EstateFiltersParams;
import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.ReqSearchByProfile;
import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.RespAdvice;
import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.RespStatistics;
import com.github.m2cyurealestate.real_estate_back.business.Month;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.estate.EstatePosition;
import com.github.m2cyurealestate.real_estate_back.business.user.Profile;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.dao.city.CityDao;
import com.github.m2cyurealestate.real_estate_back.dao.estate.EstateDao;
import com.github.m2cyurealestate.real_estate_back.dao.user.UserDao;
import com.github.m2cyurealestate.real_estate_back.security.jwt.AuthenticationHandler;
import com.github.m2cyurealestate.real_estate_back.services.prediction.PredictionInput;
import com.github.m2cyurealestate.real_estate_back.services.prediction.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Aldric Vitali Silvestre
 */
@Service
public class EstateServiceImpl implements EstateService {

    private final EstateDao estateDao;

    private final UserDao userDao;

    private final CityDao cityDao;

    private final AuthenticationHandler authenticationHandler;

    private final PredictionService predictionService;

    @Autowired
    public EstateServiceImpl(EstateDao estateDao,
                             UserDao userDao,
                             CityDao cityDao, AuthenticationHandler authenticationHandler,
                             PredictionService predictionService) {
        this.estateDao = estateDao;
        this.userDao = userDao;
        this.cityDao = cityDao;
        this.authenticationHandler = authenticationHandler;
        this.predictionService = predictionService;
    }

    @Override
    @Transactional
    public Estate getById(long id) {
        Optional<User> user = authenticationHandler.findUserFromContext();
        Estate estate = estateDao.findById(id, user)
                .orElseThrow();
        user.ifPresent(u -> userDao.addNavigation(u, estate.getUrl()));
        return estate;
    }

    @Override
    public Page<Estate> getPage(PageParams pageParams, EstateFiltersParams filtersParams) {
        Optional<User> user = authenticationHandler.findUserFromContext();

        // Create page request
        int pageSize = pageParams.getPageSize().orElse(10);
        Pageable pageable = PageRequest.of(pageParams.getPage(), pageSize);

        return estateDao.findPage(pageable, filtersParams, user);
    }

    @Override
    public List<EstatePosition> getAllEstatePositions() {
        return estateDao.findAllEstatePositions();
    }

    @Override
    public Page<Estate> getFavorites(PageParams pageParams) {
        User user = authenticationHandler.getUserFromContext();

        // Create page request
        int pageSize = pageParams.getPageSize().orElse(10);
        Pageable pageable = PageRequest.of(pageParams.getPage(), pageSize);

        return estateDao.findFavorites(pageable, user);
    }

    @Override
    public RespAdvice getAdvices(long estateId) {
        var estate = estateDao.findById(estateId).orElseThrow();
        var city = cityDao.findByPostalCode(estate.getPostalCode()).orElseThrow();

        // Predict price
        var input = PredictionInput.fromModels(estate, city);
        var estimatedPrice = predictionService.predictSellingPrice(input);
//        estimatedPrice = refineEstimatedPrice(estimatedPrice, estate);

        var cityStats = estateDao.getCityPriceStats(estate);

        var pricePerMonth = computePricePerMonth(estimatedPrice);

        return new RespAdvice(
                estimatedPrice,
                cityStats.minPrice(),
                cityStats.maxPrice(),
                cityStats.meanPrice(),
                pricePerMonth
        );
    }

    private BigDecimal refineEstimatedPrice(BigDecimal estimatedPrice, Estate estate) {
        var estPrice = estimatedPrice.longValue();
        var estatePrice = estate.getPrice();
        if (Math.abs(estPrice - estatePrice) < 50_000) {
            return estimatedPrice;
        }
        long variation = defineVariationRange(estate.getPrice());
        Random random = new Random(estate.getId());
        var r = random.nextLong(estatePrice - variation, estatePrice + variation);
        return BigDecimal.valueOf(r);
    }

    private long defineVariationRange(long estatePrice) {
        if (estatePrice < 100_000) {
            return 20_000L;
        }
        return 50_000L;
    }

    private Map<Month, BigDecimal> computePricePerMonth(BigDecimal price) {
        return Arrays.stream(Month.values())
                .collect(Collectors.toMap(Function.identity(), m -> m.applyVariation(price)));
    }

    @Override
    public RespStatistics getStatistics(long estateId) {
        var estate = estateDao.findById(estateId).orElseThrow();
        var scores = cityDao.findByPostalCode(estate.getPostalCode()).orElseThrow().scores();
        var stats = estateDao.getEstateStatistics(estateId);
        return new RespStatistics(
                BigDecimal.valueOf(scores.average()),
                BigDecimal.valueOf(scores.security()),
                BigDecimal.valueOf(scores.education()),
                BigDecimal.valueOf(scores.hobbies()),
                BigDecimal.valueOf(scores.environment()),
                BigDecimal.valueOf(scores.practicality()),
                stats.meanPriceBigCities(),
                stats.meanPriceApartment(),
                stats.meanPriceHouse()
        );
    }

    @Override
    public Page<Estate> getByProfile(PageParams pageParams, ReqSearchByProfile request) {
        User user = authenticationHandler.getUserFromContext();
        var profile = userDao.findProfileById(user, request.profileId())
                .orElseThrow();

        int pageSize = pageParams.getPageSize().orElse(10);
        Pageable pageable = PageRequest.of(pageParams.getPage(), pageSize);

        return estateDao.findByProfile(profile, pageable, user);
    }
}
