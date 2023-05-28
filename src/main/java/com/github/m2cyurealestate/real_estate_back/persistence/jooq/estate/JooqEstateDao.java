package com.github.m2cyurealestate.real_estate_back.persistence.jooq.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.EstateFiltersParams;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.dao.estate.EstateDao;
import org.apache.commons.lang3.NotImplementedException;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
@Repository
public class JooqEstateDao implements EstateDao {

    private final DSLContext dsl;

    @Autowired
    public JooqEstateDao(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<Estate> findById(long id, Optional<User> user) {
        // TODO find estate by id
        throw new NotImplementedException();
    }

    @Override
    public Page<Estate> findPage(Pageable pageable, EstateFiltersParams filtersParams, Optional<User> user) {
        // TODO find estate page
        throw new NotImplementedException();
    }
}
