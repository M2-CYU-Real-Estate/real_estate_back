package com.github.m2cyurealestate.real_estate_back.persistence.jooq.estate;

import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.estate.EstateType;
import com.github.m2cyurealestate.real_estate_back.business.estate.RateClass;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqEstateRecord;
import org.jooq.DSLContext;

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
                JooqEstateType.findEstateType(record.getTypeEstate()),
                record.getCityName(),
                record.getPostalCode(),
                record.getPrice(),
                extractSurface(record.getSurface()),
                record.getTerrainSurface().longValue(),
                record.getRoomNumber(),
                record.getBedroomNumber(),
                record.getBathroomNumber(),
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

    // NOTE : This is a temporary method that should be deleted soon
    private Long extractSurface(String surface) {
        // Convert to big decimal to have the same logic as the other columns
        try {
            return Long.parseLong(surface);
        } catch (NumberFormatException exception) {
            return -1L;
        }
    }
}
