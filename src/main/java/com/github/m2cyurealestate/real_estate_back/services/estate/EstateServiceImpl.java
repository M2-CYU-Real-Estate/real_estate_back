package com.github.m2cyurealestate.real_estate_back.services.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.page.PageParams;
import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.EstateFiltersParams;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.estate.EstatePosition;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.dao.estate.EstateDao;
import com.github.m2cyurealestate.real_estate_back.dao.user.UserDao;
import com.github.m2cyurealestate.real_estate_back.security.jwt.AuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
@Service
public class EstateServiceImpl implements EstateService {

    private final EstateDao estateDao;

    private final UserDao userDao;

    private final AuthenticationHandler authenticationHandler;

    @Autowired
    public EstateServiceImpl(EstateDao estateDao, UserDao userDao, AuthenticationHandler authenticationHandler) {
        this.estateDao = estateDao;
        this.userDao = userDao;
        this.authenticationHandler = authenticationHandler;
    }

    @Override
    @Transactional
    public Estate getById(long id) {
        Optional<User> user = authenticationHandler.findUserFromContext();
        Estate estate = estateDao.findById(id, user)
                .orElseThrow();
        user.ifPresent(u -> userDao.addNavigation(u, estate.getUrl()));
        return estate;
    }

    @Override
    public Page<Estate> getPage(PageParams pageParams, EstateFiltersParams filtersParams) {
        Optional<User> user = authenticationHandler.findUserFromContext();

        // Create page request
        int pageSize = pageParams.getPageSize().orElse(10);
        Pageable pageable = PageRequest.of(pageParams.getPage(), pageSize);

        return estateDao.findPage(pageable, filtersParams, user);
    }

    @Override
    public List<EstatePosition> getAllEstatePositions() {
        return estateDao.findAllEstatePositions();
    }

    @Override
    public Page<Estate> getFavorites(PageParams pageParams) {
        User user = authenticationHandler.getUserFromContext();

        // Create page request
        int pageSize = pageParams.getPageSize().orElse(10);
        Pageable pageable = PageRequest.of(pageParams.getPage(), pageSize);

        return estateDao.findFavorites(pageable, user);
    }
}
