package com.github.m2cyurealestate.real_estate_back.persistence.jooq.estate;

import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.estate.EstatePosition;
import com.github.m2cyurealestate.real_estate_back.business.estate.RateClass;
import com.github.m2cyurealestate.real_estate_back.dao.estate.CityPriceStats;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqEstateRecord;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Record4;

import java.math.BigDecimal;

/**
 * @author Aldric Vitali Silvestre
 */
public class JooqEstateMappers {

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
                record.getSurface().longValue(),
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

    public EstatePosition toEstatePosition(Record4<Integer, String, BigDecimal, BigDecimal> record) {
        return new EstatePosition(record.value1(),
                                  record.value2(),
                                  record.value3().toPlainString(),
                                  record.value4().toPlainString()
        );
    }

    public CityPriceStats toCityPriceStats(Record3<Long, Long, BigDecimal> record) {
        return new CityPriceStats(
                new BigDecimal(record.value1()),
                new BigDecimal(record.value2()),
                record.value3()
        );
    }
}
