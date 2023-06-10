package com.github.m2cyurealestate.real_estate_back.business.estate;

/**
 * The position of an estate, linked with its id
 * @author Aldric Vitali Silvestre
 */
public record EstatePosition(
        long id,

        String title,
        String latitude,
        String longitude
) {
}
