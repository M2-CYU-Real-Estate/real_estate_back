package com.github.m2cyurealestate.real_estate_back.services.suggestion;

import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.dao.estate.EstateDao;
import com.github.m2cyurealestate.real_estate_back.dao.user.UserDao;
import com.github.m2cyurealestate.real_estate_back.security.jwt.AuthenticationHandler;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aldric Vitali Silvestre
 */
@Service
public class SuggestionsServiceImpl implements SuggestionsService {

    public static final int NB_INPUT_ESTATES = 100;

    private final EstateDao estateDao;

    private final UserDao userDao;

    private final AuthenticationHandler authenticationHandler;

    public SuggestionsServiceImpl(
            EstateDao estateDao,
            UserDao userDao,
            AuthenticationHandler authenticationHandler) {
        this.estateDao = estateDao;
        this.userDao = userDao;
        this.authenticationHandler = authenticationHandler;
    }

    @Override
    public List<Estate> getSuggestions() {
        User user = authenticationHandler.getUserFromContext();
        List<Estate> inputEstates = findInputEstates(user);
        // Call the webservice
        throw new NotImplementedException();
    }

    private List<Estate> findInputEstates(User user) {
        // Find from multiple sources, stop before if we have
        List<Estate> estates = new ArrayList<>(NB_INPUT_ESTATES);
        // TODO

        List<Estate> inputEstatesFromFavorites = findInputEstatesFromFavorites(user);

        return null;
    }

    private List<Estate> findInputEstatesFromFavorites(User user) {
        Pageable pageable = PageRequest.of(0, 50);
        return estateDao.findFavorites(pageable, user).getContent();
    }

    private List<Estate> findInputEstatesFromNavigation(User user) {
        Pageable pageable = PageRequest.of(0, 50);
        return estateDao.findNavigationEntries(pageable, user).getContent();
    }

    private List<Estate> findInputEstatesFromProfile(User user) {
        Pageable pageable = PageRequest.of(0, 50);
        return userDao.findMainProfile(user)
                .map(p -> estateDao.findByProfile(p, pageable, user).getContent())
                .orElseGet(List::of);
    }
}
