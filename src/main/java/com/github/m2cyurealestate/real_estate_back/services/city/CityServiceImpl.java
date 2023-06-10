package com.github.m2cyurealestate.real_estate_back.services.city;

import com.github.m2cyurealestate.real_estate_back.business.city.City;
import com.github.m2cyurealestate.real_estate_back.dao.city.CityDao;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aldric Vitali Silvestre
 */
@Service
public class CityServiceImpl implements CityService {

    private final CityDao cityDao;

    public CityServiceImpl(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Override
    public List<City> getCities() {
        return cityDao.findAll();
    }

    @Override
    public City getCityByInseeCode(String inseeCode) {
        return cityDao.findByInseeCode(inseeCode).orElseThrow();
    }

    @Override
    public City getCityByPostalCode(String postalCode) {
        return cityDao.findByPostalCode(postalCode).orElseThrow();
    }

}
