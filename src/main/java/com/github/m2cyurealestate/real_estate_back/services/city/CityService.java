package com.github.m2cyurealestate.real_estate_back.services.city;

import com.github.m2cyurealestate.real_estate_back.business.city.City;

import java.util.List;

/**
 * @author Aldric Vitali Silvestre
 */
public interface CityService {

    List<City> getCities();

    City getCityByInseeCode(String inseeCode);

    City getCityByPostalCode(String postalCode);
}
