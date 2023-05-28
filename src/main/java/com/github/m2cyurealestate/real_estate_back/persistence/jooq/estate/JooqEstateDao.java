package com.github.m2cyurealestate.real_estate_back.persistence.jooq.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.EstateFiltersParams;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.dao.estate.EstateDao;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqEstateTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.JqUserLikesTable;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqEstateRecord;
import org.apache.commons.lang3.NotImplementedException;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
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
        // TODO find estate page
        throw new NotImplementedException();
    }
}
