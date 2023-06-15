package com.github.m2cyurealestate.real_estate_back.persistence.jooq.estate;

import com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate.EstateFiltersParams;
import com.github.m2cyurealestate.real_estate_back.business.estate.ClusterArgs;
import com.github.m2cyurealestate.real_estate_back.business.estate.Estate;
import com.github.m2cyurealestate.real_estate_back.business.estate.EstatePosition;
import com.github.m2cyurealestate.real_estate_back.business.user.Profile;
import com.github.m2cyurealestate.real_estate_back.business.user.User;
import com.github.m2cyurealestate.real_estate_back.dao.city.CityDao;
import com.github.m2cyurealestate.real_estate_back.dao.estate.CityPriceStats;
import com.github.m2cyurealestate.real_estate_back.dao.estate.EstateDao;
import com.github.m2cyurealestate.real_estate_back.dao.estate.EstateStatistics;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.*;
import com.github.m2cyurealestate.real_estate_back.persistence.jooq.model.tables.records.JqEstateMlCRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
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

    public static final JqEstateMlCTable ESTATE = JqEstateMlCTable.ESTATE_ML_C;

    public static final Class<JqEstateMlCRecord> ESTATE_RECORD_CLASS = JqEstateMlCRecord.class;

    public static final JqUserLikesTable USER_LIKES = JqUserLikesTable.USER_LIKES;

    public static final JqUserNavigationTable USER_NAVIGATION = JqUserNavigationTable.USER_NAVIGATION;

    public static final JqCitiesTable CITY = JqCitiesTable.CITIES;

    public static final JqCitiesScoreTable CITY_SCORE = JqCitiesScoreTable.CITIES_SCORE;

    private final DSLContext dsl;

    private final JooqEstateMappers estateMappers;

    private final JooqEstateFilters estateFilters;

    private final JooqEstateProfileFilters estateProfileFilters;

    private final CityDao cityDao;

    private final EstatePositionCache positionCache;


    @Autowired
    public JooqEstateDao(DSLContext dsl, CityDao cityDao) {
        this.dsl = dsl;
        this.cityDao = cityDao;
        estateMappers = new JooqEstateMappers();
        estateFilters = new JooqEstateFilters();
        positionCache = new EstatePositionCache(dsl, estateMappers);
        estateProfileFilters = new JooqEstateProfileFilters();
    }

    @Override
    public Optional<Estate> findById(long id, Optional<User> user) {
        return dsl.selectFrom(ESTATE).where(ESTATE.ID.eq((int) id)).fetchOptional().map(fetchWithFavorite(user));
    }

    @Override
    public Optional<Estate> findById(long id) {
        return findById(id, Optional.empty());
    }

    private Function<JqEstateMlCRecord, Estate> fetchWithFavorite(Optional<User> user) {
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
        return positionCache.fetchEstatePositions();
    }

    @Override
    public Page<Estate> findNavigationEntries(Pageable pageable, User user) {
        var select = dsl.select(ESTATE.asterisk())
                .from(ESTATE)
                .innerJoin(USER_NAVIGATION)
                .on(ESTATE.URL.eq(USER_NAVIGATION.ESTATE_LINK))
                .and(USER_NAVIGATION.ID_USER.eq(user.getId().intValue()))
                .orderBy(USER_NAVIGATION.DT.desc());

        // Perform another request to get total count
        int totalCount = dsl.fetchCount(select);

        // Then, fetch the list of elements
        List<Estate> estates = select.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(r -> estateMappers.toEstate(r.into(ESTATE_RECORD_CLASS), true));

        return new PageImpl<>(estates, pageable, totalCount);
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
                .fetch(r -> estateMappers.toEstate(r.into(ESTATE_RECORD_CLASS), true));

        return new PageImpl<>(estates, pageable, totalCount);
    }

    @Override
    public CityPriceStats getCityPriceStats(Estate estate) {
        return dsl.select(DSL.min(ESTATE.PRICE),
                          DSL.max(ESTATE.PRICE),
                          DSL.avg(ESTATE.PRICE)
                )
                .from(ESTATE)
                .innerJoin(CITY)
                .on(ESTATE.POSTAL_CODE.eq(estate.getPostalCode()))
                .and(ESTATE.PRICE.notEqual(-1L))
                .fetchOptional(estateMappers::toCityPriceStats)
                .orElseThrow();
    }

    @Override
    public EstateStatistics getEstateStatistics(long estateId) {
        BigDecimal meanPriceBigCities = fetchMeanPriceBigCities();
        CityAvg meanPrices = fetchMeanPrices(estateId);
        return new EstateStatistics(
                meanPriceBigCities,
                meanPrices.apartmentMean(),
                meanPrices.houseMean()
        );
    }

    private BigDecimal fetchMeanPriceBigCities() {
        return dsl.select(DSL.avg(ESTATE.PRICE))
                .from(ESTATE)
                .innerJoin(CITY)
                .on(ESTATE.POSTAL_CODE.eq(CITY.POSTAL_CODE))
                // Paris etc, have multiple codes
                .where(CITY.CITY_NAME.like("Paris %"),
                       CITY.CITY_NAME.like("Marseille %"),
                       CITY.CITY_NAME.like("Lyon %"),
                       // We have Montpelier, Angers, etc.
                       CITY.INSEE_CODE.eq("31555"),
                       CITY.INSEE_CODE.eq("06088"),
                       CITY.INSEE_CODE.eq("44109"),
                       CITY.INSEE_CODE.eq("34172"),
                       CITY.INSEE_CODE.eq("59350"),
                       CITY.INSEE_CODE.eq("83137"),
                       CITY.INSEE_CODE.eq("21231"),
                       CITY.INSEE_CODE.eq("67482"),
                       CITY.INSEE_CODE.eq("35238"),
                       CITY.INSEE_CODE.eq("42218"),
                       CITY.INSEE_CODE.eq("38185"),
                       CITY.INSEE_CODE.eq("33063"),
                       CITY.INSEE_CODE.eq("49007")
                )
                .fetchOptional(Record1::value1)
                .orElse(BigDecimal.valueOf(-1));
    }

    private CityAvg fetchMeanPrices(long estateId) {
        String postalCode = dsl.selectDistinct(CITY.POSTAL_CODE)
                .from(ESTATE)
                .innerJoin(CITY)
                .on(ESTATE.POSTAL_CODE.eq(CITY.POSTAL_CODE))
                .where(ESTATE.ID.eq((int) estateId))
                .fetchOptional(Record1::value1)
                .orElseThrow();

        Field<String> upperTypeEstate = DSL.upper(ESTATE.TYPE_ESTATE);
        Map<String, Record2<String, BigDecimal>> pricePerType = dsl.select(
                        upperTypeEstate,
                        DSL.avg(ESTATE.PRICE)
                )
                .from(ESTATE)
                .innerJoin(CITY)
                .on(ESTATE.POSTAL_CODE.eq(CITY.POSTAL_CODE))
                .where(CITY.POSTAL_CODE.eq(postalCode))
                .groupBy(upperTypeEstate)
                .fetchMap(upperTypeEstate);

        BigDecimal apartmentMean = Optional.ofNullable(pricePerType.get(JooqEstateType.APARTMENT.getName().toUpperCase()))
                .map(Record2::value2)
                .orElse(BigDecimal.valueOf(-1L));

        BigDecimal houseMean = Optional.ofNullable(pricePerType.get(JooqEstateType.HOUSE.getName().toUpperCase()))
                .map(Record2::value2)
                .orElse(BigDecimal.valueOf(-1L));

        return new CityAvg(houseMean, apartmentMean);
    }

    record CityAvg(
            BigDecimal houseMean,
            BigDecimal apartmentMean
    ) {
    }

    @Override
    public Page<Estate> findByProfile(Profile profile, Pageable pageable, User user) {
        var city = cityDao.findByPostalCode(profile.getPostalCode()).orElseThrow();

        var conditions = estateProfileFilters.createConditions(profile, city);

        // Need to perform the join for the conditions to be applied
        var select = dsl.select(ESTATE.asterisk())
                .distinctOn(ESTATE.ID)
                .from(ESTATE)
                .innerJoin(CITY)
                .on(CITY.POSTAL_CODE.eq(ESTATE.POSTAL_CODE))
                .innerJoin(CITY_SCORE)
                .on(CITY_SCORE.POSTAL_CODE.eq(CITY.POSTAL_CODE))
                .where(conditions);

        // Perform another request to get total count
        int totalCount = dsl.fetchCount(select);

        // Then, fetch the list of elements
        List<Estate> estates = select.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(r -> {
                    var rec = r.into(ESTATE_RECORD_CLASS);
                    return estateMappers.toEstate(rec, false);
                });

        // If the user is present, we want to find the favorites
        updateFavorites(estates).accept(user);

        return new PageImpl<>(estates, pageable, totalCount);
    }

    @Override
    public List<Estate> findByCluster(int cluster, String departmentNumber) {
        return dsl.selectFrom(ESTATE)
                .where(ESTATE.CLUSTER.eq(cluster))
                .and(ESTATE.DEPARTMENT_NUMBER.eq(departmentNumber))
                // Don't need to know if they are favorite here
                .fetch(estateMappers::toEstate);
    }

    @Override
    public List<Estate> findByClusters(Collection<ClusterArgs> clusterArgs) {
        List<Row2<Integer, String>> clusterRows = clusterArgs.stream()
                .map(c -> DSL.row(c.cluster(), c.department()))
                .toList();

        return dsl.selectFrom(ESTATE)
                // Need to perform combination checking in order
                // to avoid fetching wrong estates
                .where(DSL.row(ESTATE.CLUSTER, ESTATE.DEPARTMENT_NUMBER).in(clusterRows))
                // Don't need to know if they are favorite here
                .fetch(estateMappers::toEstate);
    }

}
