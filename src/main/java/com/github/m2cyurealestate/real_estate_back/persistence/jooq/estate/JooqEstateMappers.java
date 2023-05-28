package com.github.m2cyurealestate.real_estate_back.persistence.jooq.estate;

import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.estate.EstateType;
import com.github.m2cyurealestate.real_estate_back.business.estate.RateClass;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqEstateTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqEstateRecord;
import org.jooq.DSLContext;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
public class JooqEstateMappers {

    private final DSLContext dsl;

    public JooqEstateMappers(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Estate toEstate(JqEstateRecord record, boolean isFavorite) {
        return new Estate(
                (long) record.getId(),
                isFavorite,
                record.getTitle(),
                record.getDescription(),
                record.getUrl(),
                record.getImage(),
                record.getRef(),
                stringToEstateType(record.getTypeEstate()),
                record.getCityName(),
                record.getPostalCode(),
                record.getPrice().longValue(),
                extractSurface(record.getSurface()).longValue(),
                record.getTerrainSurface().longValue(),
                record.getRoomNumber().intValue(),
                record.getBedroomNumber().intValue(),
                record.getBathroomNumber().intValue(),
                record.getIsterrace(),
                record.getIsbalcony(),
                record.getIselevator(),
                record.getIsgarage(),
                record.getIsparking(),
                record.getIsspecialkitchen(),
                RateClass.fromString(record.getEnergyclass()),
                RateClass.fromString(record.getGazemission()),
                record.getDt(),
                record.getDt()
        );
    }

    private EstateType stringToEstateType(String s) {
        return switch(s) {
            case "Maison" -> EstateType.HOUSE;
            case "Appartement" -> EstateType.APARTMENT;
            default -> EstateType.UNKNOWN;
        };
    }

    // NOTE : This is a temporary method that should be deleted soon
    private BigDecimal extractSurface(String surface) {
        // Convert to big decimal to have the same logic as the other columns
        try {
            return new BigDecimal(surface);
        } catch (NumberFormatException exception) {
            return BigDecimal.ZERO;
        }
    }
}
