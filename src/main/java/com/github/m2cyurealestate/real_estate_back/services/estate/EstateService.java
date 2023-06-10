package com.github.m2cyurealestate.real_estate_back.services.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.page.PageParams;
import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.EstateFiltersParams;
import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.RespAdvice;
import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.RespStatistics;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.estate.EstatePosition;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Aldric Vitali Silvestre
 */
public interface EstateService {

    /**
     * @param id   The id of the estate
     * @return The estate associated
     * @throws NoSuchElementException If no user can be found
     */
    Estate getById(long id) throws NoSuchElementException;

    /**
     * @param pageParams the pagination parameters
     * @param filtersParams the filters to apply
     * @return A page of estates
     */
    Page<Estate> getPage(PageParams pageParams, EstateFiltersParams filtersParams);

    List<EstatePosition> getAllEstatePositions();

    Page<Estate> getFavorites(PageParams pageParams);

    RespAdvice getAdvices(long estateId);

    RespStatistics getStatistics(long estateId);
}
