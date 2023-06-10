package com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate;

import com.github.m2cyurealestate.real_estate_back.business.estate.EstatePosition;

/**
 * @author Aldric Vitali Silvestre
 */
public record RespEstatePosition(
        long id,
        String title,
        String lat,
        String lon
) {

    public RespEstatePosition(EstatePosition pos) {
        this(pos.id(), pos.title(), pos.latitude(), pos.longitude());
    }
}
