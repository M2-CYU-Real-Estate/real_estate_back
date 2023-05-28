package com.github.m2cyurealestate.real_estate_back.services.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.page.PageParams;
import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.EstateFiltersParams;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import org.springframework.data.domain.Page;

/**
 * @author Aldric Vitali Silvestre
 */
public interface EstateService {

    Estate getById(long id);

    Page<Estate> getPage(PageParams pageParams, EstateFiltersParams filtersParams);
}
