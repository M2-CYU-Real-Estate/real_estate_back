package com.github.m2cyurealestate.real_estate_back.dao.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.EstateFiltersParams;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.estate.EstatePosition;
import com.github.m2cyurealestate.real_estate_back.business.user.Profile;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
public interface EstateDao {

    Optional<Estate> findById(long id, Optional<User> user);

    Optional<Estate> findById(long id);

    Page<Estate> findPage(Pageable pageable, EstateFiltersParams filtersParams, Optional<User> user);

    List<EstatePosition> findAllEstatePositions();

    Page<Estate> findFavorites(Pageable pageable, User user);

    CityPriceStats getCityPriceStats(Estate estate);

    EstateStatistics getEstateStatistics(long id);

    Page<Estate> findByProfile(Profile profile, Pageable pageable, User user);
}
