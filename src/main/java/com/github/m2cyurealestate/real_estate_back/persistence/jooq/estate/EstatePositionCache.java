package com.github.m2cyurealestate.real_estate_back.persistence.jooq.estate;

import com.github.m2cyurealestate.real_estate_back.business.estate.EstatePosition;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqCitiesTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqEstateTable;
import org.jooq.DSLContext;
import org.jooq.Record4;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A cache for the return of the estates positions,
 * updated when new entries are inserted.
 *
 * @author Aldric Vitali Silvestre
 */
class EstatePositionCache {

    public static final int MAX_POSITION_ROWS = 15_000;

    public static final JqEstateTable ESTATE = JqEstateTable.ESTATE;

    public static final JqCitiesTable CITY = JqCitiesTable.CITIES;

    private final DSLContext dsl;

    private final JooqEstateMappers estateMappers;

    private long lastEstateId = -1L;

    private List<EstatePosition> cache = List.of();

    public EstatePositionCache(DSLContext dsl, JooqEstateMappers estateMappers) {
        this.dsl = dsl;
        this.estateMappers = estateMappers;
    }

    /**
     * Fetch positions from storage or recover it from cache
     * if no new entries were inserted.
     */
    public List<EstatePosition> fetchEstatePositions() {
        long lastId = findLastId();
        if (cache.isEmpty() || lastId > lastEstateId) {
            updateCache();
            lastEstateId = lastId;
        }

        return findFromCache();
    }

    private long findLastId() {
        return dsl.select(ESTATE.ID)
                .from(ESTATE)
                .orderBy(ESTATE.ID.desc())
                .limit(1)
                .fetchOptional()
                .orElseThrow()
                .value1();
    }

    private void updateCache() {
        var recordById = dsl.select(ESTATE.ID, ESTATE.TITLE, CITY.LATITUDE, CITY.LONGITUDE)
                .distinctOn(ESTATE.ID)
                .from(ESTATE)
                .innerJoin(CITY)
                .on(ESTATE.POSTAL_CODE.eq(CITY.POSTAL_CODE))
                .limit(MAX_POSITION_ROWS)
                .stream()
                // This is more a safeguard than anything, as the distinctOn already exists
                .collect(Collectors.toMap(Record4::value1, Function.identity(),
                                          // Keep only the first record if same id found
                                          (r1, r2) -> r1
                ));
        // Update the cache here
        cache = recordById.values()
                .stream()
                .toList()
                .stream()
                .map(estateMappers::toEstatePosition)
                .toList();
    }

    private List<EstatePosition> findFromCache() {
        return List.copyOf(cache);
    }
}
