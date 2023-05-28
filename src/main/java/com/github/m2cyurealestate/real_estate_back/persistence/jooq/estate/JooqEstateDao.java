package com.github.m2cyurealestate.real_estate_back.persistence.jooq.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.EstateFiltersParams;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.dao.estate.EstateDao;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqEstateTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqUserLikesTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqEstateRecord;
import org.apache.commons.lang3.NotImplementedException;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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

    private final DSLContext dsl;

    private final JooqEstateMappers estateMappers;

    @Autowired
    public JooqEstateDao(DSLContext dsl) {
        this.dsl = dsl;
        estateMappers = new JooqEstateMappers(dsl);
    }

    @Override
    public Optional<Estate> findById(long id, Optional<User> user) {
        return dsl.selectFrom(ESTATE)
                .where(ESTATE.ID.eq((int) id))
                .fetchOptional()
                .map(fetchWithFavorite(user));
    }

    private Function<JqEstateRecord, Estate> fetchWithFavorite(Optional<User> user) {
        return r -> user
                // If we have the user connected, we want to fetch if he has a favorite
                .map(u -> {
                    boolean isFavorite = dsl.fetchExists(dsl.selectFrom(USER_LIKES)
                                                        .where(USER_LIKES.ID_USER.cast(Long.class).eq(u.getId()))
                                                        .and(USER_LIKES.ID_ESTATE.cast(Integer.class).eq(r.getId()))
                    );
                    return estateMappers.toEstate(r, isFavorite);
                })
                // We don't have the user, simply map the record to the domain class
                .orElseGet(() -> estateMappers.toEstate(r, false));
    }

    @Override
    public Page<Estate> findPage(Pageable pageable, EstateFiltersParams filtersParams, Optional<User> user) {
        // TODO not complete obviously

        SelectWhereStep<JqEstateRecord> select = dsl.selectFrom(ESTATE);

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
            var ids = estates.stream()
                    .map(Estate::getId)
                    .toList();

            var recordByEstateId = dsl.select(
                            DSL.when(USER_LIKES.ID_USER.cast(Long.class).eq(u.getId()), true)
                                    .otherwise(false),
                            USER_LIKES.ID_ESTATE.cast(Long.class)
                    ).from(USER_LIKES)
                    .where(USER_LIKES.ID_ESTATE.cast(Long.class).in(ids))
                    .fetchMap(USER_LIKES.ID_ESTATE.cast(Long.class));

            // Update the "favorite" field
            estates.forEach(e -> {
                // If the row is found, it means that we have a like !
                Optional.ofNullable(recordByEstateId.get(e.getId()))
                        .ifPresent(record -> e.setFavorite(true));
            });
        };
    }
}
