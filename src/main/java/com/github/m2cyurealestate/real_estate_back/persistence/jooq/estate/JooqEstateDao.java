package com.github.m2cyurealestate.real_estate_back.persistence.jooq.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.EstateFiltersParams;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.estate.EstatePosition;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.dao.estate.EstateDao;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqCitiesTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqEstateTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqUserLikesTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqEstateRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Aldric Vitali Silvestre
 */
@Repository
public class JooqEstateDao implements EstateDao {

    public static final JqEstateTable ESTATE = JqEstateTable.ESTATE;

    public static final JqUserLikesTable USER_LIKES = JqUserLikesTable.USER_LIKES;

    public static final JqCitiesTable CITY = JqCitiesTable.CITIES;
    public static final int MAX_POSITION_ROWS = 50_000;

    private final DSLContext dsl;

    private final JooqEstateMappers estateMappers;

    private final JooqEstateFilters estateFilters;

    @Autowired
    public JooqEstateDao(DSLContext dsl) {
        this.dsl = dsl;
        estateMappers = new JooqEstateMappers(dsl);
        estateFilters = new JooqEstateFilters();
    }

    @Override
    public Optional<Estate> findById(long id, Optional<User> user) {
        return dsl.selectFrom(ESTATE).where(ESTATE.ID.eq((int) id)).fetchOptional().map(fetchWithFavorite(user));
    }

    private Function<JqEstateRecord, Estate> fetchWithFavorite(Optional<User> user) {
        return r -> user
                // If we have the user connected, we want to fetch if he has a favorite
                .map(u -> {
                    boolean isFavorite = dsl.fetchExists(dsl.selectFrom(USER_LIKES)
                                                                 .where(USER_LIKES.ID_USER.cast(Long.class)
                                                                                .eq(u.getId()))
                                                                 .and(USER_LIKES.ESTATE_LINK.eq(r.getUrl())));
                    return estateMappers.toEstate(r, isFavorite);
                })
                // We don't have the user, simply map the record to the domain class
                .orElseGet(() -> estateMappers.toEstate(r, false));
    }

    @Override
    public Page<Estate> findPage(Pageable pageable, EstateFiltersParams filtersParams, Optional<User> user) {
        // For all filters present, add conditions
        var conditions = estateFilters.createFilters(filtersParams);

        var select = dsl.selectFrom(ESTATE).where(conditions).orderBy(ESTATE.DT.desc(), ESTATE.ID.desc());

        // Perform another request to get total count
        int totalCount = dsl.fetchCount(select);

        // Then, fetch the list of elements
        List<Estate> estates = select.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(r -> estateMappers.toEstate(r, false));

        // If the user is present, we want to find the favorites
        user.ifPresent(updateFavorites(estates));

        return new PageImpl<>(estates, pageable, totalCount);
    }

    private Consumer<User> updateFavorites(List<Estate> estates) {
        return u -> {
            var ids = estates.stream().map(Estate::getUrl).toList();

            var recordByEstateId = dsl.select(DSL.when(USER_LIKES.ID_USER.cast(Long.class).eq(u.getId()), true)
                                                      .otherwise(false), USER_LIKES.ESTATE_LINK)
                    .from(USER_LIKES)
                    .where(USER_LIKES.ESTATE_LINK.in(ids))
                    .fetchMap(USER_LIKES.ESTATE_LINK);

            // Update the "favorite" field
            estates.forEach(e -> {
                // If the row is found, it means that we have a like !
                Optional.ofNullable(recordByEstateId.get(e.getUrl())).ifPresent(record -> e.setFavorite(true));
            });
        };
    }

    @Override
    public List<EstatePosition> findAllEstatePositions() {
        // Create a map in order to avoid possible duplicates
        var positions = dsl.select(ESTATE.ID, CITY.LATITUDE, CITY.LONGITUDE)
                .from(ESTATE)
                .innerJoin(CITY)
                .on(ESTATE.POSTAL_CODE.eq(CITY.POSTAL_CODE))
                .limit(MAX_POSITION_ROWS)
                .fetchMap(ESTATE.ID);
        return positions.values()
                .stream()
                .map(estateMappers::toEstatePosition)
                .toList();
    }

    @Override
    public Page<Estate> findFavorites(Pageable pageable, User user) {
        var select = dsl.select(ESTATE.asterisk())
                .from(ESTATE)
                .innerJoin(USER_LIKES)
                .on(ESTATE.URL.eq(USER_LIKES.ESTATE_LINK))
                .where(USER_LIKES.ID_USER.eq(user.getId().intValue()));

        // Perform another request to get total count
        int totalCount = dsl.fetchCount(select);

        // Then, fetch the list of elements
        List<Estate> estates = select.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(r -> estateMappers.toEstate(r.into(JqEstateRecord.class), true));

        return new PageImpl<>(estates, pageable, totalCount);
    }
}
