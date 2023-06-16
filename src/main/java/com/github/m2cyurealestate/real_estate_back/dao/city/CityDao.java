package com.github.m2cyurealestate.real_estate_back.dao.city;

import com.github.m2cyurealestate.real_estate_back.business.city.City;

import java.util.List;
import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
public interface CityDao {

    List<City> findAll();

    Optional<City> findByPostalCode(String postalCode);

    Optional<City> findByPostalCode(String postalCode, Optional<String> cityname);

    Optional<City> findByInseeCode(String inseeCode);

}
