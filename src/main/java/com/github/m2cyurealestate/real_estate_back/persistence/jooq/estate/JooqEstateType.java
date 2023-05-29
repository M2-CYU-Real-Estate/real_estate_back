package com.github.m2cyurealestate.real_estate_back.persistence.jooq.estate;

import com.github.m2cyurealestate.real_estate_back.business.estate.EstateType;

import java.util.Arrays;
import java.util.Optional;

/**
 * Permit to link between what is stored in database and
 * the domain logic to retrieve for the type of the estate.
 *
 * @author Aldric Vitali Silvestre
 */
public enum JooqEstateType {
    HOUSE("Maison", EstateType.HOUSE),
    APARTMENT("Appartement", EstateType.APARTMENT);

    private final String name;
    private final EstateType estateType;

    JooqEstateType(String name, EstateType estateType) {
        this.name = name;
        this.estateType = estateType;
    }

    public String getName() {
        return name;
    }

    public EstateType getEstateType() {
        return estateType;
    }

    public static Optional<String> findFilterName(EstateType type) {
        return Arrays.stream(JooqEstateType.values())
                .filter(j -> j.getEstateType() == type)
                .map(JooqEstateType::getName)
                .findFirst();
    }

    public static EstateType findEstateType(String s) {
        return Arrays.stream(JooqEstateType.values())
                .filter(j -> j.getName().equals(s))
                .map(JooqEstateType::getEstateType)
                .findAny()
                .orElse(EstateType.UNKNOWN);
    }
}
