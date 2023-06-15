package com.github.m2cyurealestate.real_estate_back.services.suggestion;

import com.github.m2cyurealestate.real_estate_back.business.estate.ClusterArgs;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.dao.estate.EstateDao;
import com.github.m2cyurealestate.real_estate_back.dao.user.UserDao;
import com.github.m2cyurealestate.real_estate_back.security.jwt.AuthenticationHandler;
import com.github.m2cyurealestate.real_estate_back.services.ml_webservice.MLWebserviceAccessor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Aldric Vitali Silvestre
 */
@Service
public class SuggestionsServiceImpl implements SuggestionsService {

    public static final int NB_INPUT_ESTATES = 100;
    public static final int NB_WANTED_SUGGESTIONS = 40;

    private final EstateDao estateDao;

    private final UserDao userDao;

    private final AuthenticationHandler authenticationHandler;

    private final MLWebserviceAccessor webservice;

    public SuggestionsServiceImpl(
            EstateDao estateDao,
            UserDao userDao,
            AuthenticationHandler authenticationHandler,
            MLWebserviceAccessor mlWebserviceAccessor
    ) {
        this.estateDao = estateDao;
        this.userDao = userDao;
        this.authenticationHandler = authenticationHandler;
        this.webservice = mlWebserviceAccessor;
    }

    @Override
    public List<Estate> getSuggestions() {
        User user = authenticationHandler.getUserFromContext();

        List<Estate> inputEstates = findInputEstates(user);
        List<Estate> clusterEstates = findClusterEstates(inputEstates);

        return getSuggestions(inputEstates, clusterEstates);
    }

    private List<Estate> findInputEstates(User user) {
        // Maybe too many requests will be made without needing them ?
        return Stream.of(
                        findInputEstatesFromFavorites(user),
                        findInputEstatesFromNavigation(user),
                        findInputEstatesFromProfile(user)
                )
                .flatMap(List::stream)
                .limit(NB_INPUT_ESTATES)
                .toList();
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
        return List.of();
//        Pageable pageable = PageRequest.of(0, 50);
//        return userDao.findMainProfile(user)
//                .map(p -> estateDao.findByProfile(p, pageable, user).getContent())
//                .orElseGet(List::of);
    }

    private List<Estate> findClusterEstates(List<Estate> inputEstates) {
        // Find all clusters and departments needed
        List<ClusterArgs> clusterArgs = inputEstates.stream()
                .map(ClusterArgs::new)
                .distinct()
                .toList();

        return estateDao.findByClusters(clusterArgs);
    }

    private List<Estate> getSuggestions(List<Estate> inputEstates, List<Estate> clusterEstates) {
        RespGetSuggestions resp = getSuggestionsFromWebservice(inputEstates, clusterEstates);

        // We need to convert back to the estates
        var suggestionUrls = resp.properties_to_suggest()
                .stream()
                .map(ReqProperty::ref)
                .collect(Collectors.toSet()); // THIS IS THE URL !!

        return clusterEstates.stream()
                .filter(e -> suggestionUrls.contains(e.getUrl()))
                .toList();
    }

    private RespGetSuggestions getSuggestionsFromWebservice(List<Estate> inputEstates, List<Estate> clusterEstates) {
        var req = new ReqGetSuggestions(
                ReqProperty.fromEstates(inputEstates),
                ReqProperty.fromEstates(clusterEstates),
                NB_WANTED_SUGGESTIONS
        );

        return webservice.postRequest(webservice.getSuggestionsUrl(), req, RespGetSuggestions.class);
    }

    // ==== MODEL ====
    record ReqProperty(
            String ref,
            int code_departement,
            String description,
            int cluster,
            String coords
    ) {
        ReqProperty(Estate estate) {
            this(Objects.requireNonNull(estate.getUrl()), // The ref is the url in order to make them REALLY unique
                 Integer.parseInt(estate.getDepartmentNumber()),
                 Objects.requireNonNull(estate.getDescription()),
                 Objects.requireNonNull(estate.getCluster()),
                 Objects.requireNonNull(estate.getClusterCoordinates())
            );
        }

        static List<ReqProperty> fromEstates(Collection<Estate> estates) {
            return estates.stream()
                    .map(ReqProperty::new)
                    .toList();
        }
    }

    record ReqGetSuggestions(
            List<ReqProperty> properties_user_preferences,
            List<ReqProperty> properties_by_cluster,
            int nbr_similar_property
    ) {
    }

    record RespGetSuggestions(
            List<ReqProperty> properties_to_suggest
    ) {
    }

}
