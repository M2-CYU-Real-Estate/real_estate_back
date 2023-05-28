package com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate;

import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.estate.EstateType;
import com.github.m2cyurealestate.real_estate_back.business.estate.RateClass;

import java.time.LocalDateTime;

/**
 * @author Aldric Vitali Silvestre
 */
public record RespEstate(
        long id,
        boolean isFavorite,
        String title,
        String description,
        String url,
        String imageUrl,
        EstateType type,
        String cityName,
        String postalCode,
        long price,
        long houseAreaSqrtM,
        long groundAreaSqrtM,
        int roomCount,
        int bedroomCount,
        int bathroomCount,
        boolean isTerracePresent,
        boolean isBalconyPresent,
        boolean isElevatorPresent,
        boolean isGaragePresent,
        boolean isParkingPresent,
        boolean isFittedKitchenPresent,
        RateClass energyClass,
        RateClass gazEmissionClass,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
) {

    public RespEstate(Estate estate) {
        this(estate.getId(),
             estate.isFavorite(),
             estate.getTitle(),
             estate.getDescription(),
             estate.getUrl(),
             estate.getImageUrl(),
             estate.getType(),
             estate.getCityName(),
             estate.getPostalCode(),
             estate.getPrice(),
             estate.getHouseAreaSqrtM(),
             estate.getGroundAreaSqrtM(),
             estate.getRoomCount(),
             estate.getBedroomCount(),
             estate.getBathroomCount(),
             estate.isTerracePresent(),
             estate.isBalconyPresent(),
             estate.isElevatorPresent(),
             estate.isGaragePresent(),
             estate.isParkingPresent(),
             estate.isFittedKitchenPresent(),
             estate.getEnergyClass(),
             estate.getGazEmissionClass(),
             estate.getCreatedAt(),
             estate.getLastUpdatedAt()
        );
    }

}
