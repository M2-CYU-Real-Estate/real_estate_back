package com.github.m2cyurealestate.real_estate_back.services.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.page.PageParams;
import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.EstateFiltersParams;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import org.springframework.data.domain.Page;

import java.util.NoSuchElementException;
import java.util.Optional;

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
}
