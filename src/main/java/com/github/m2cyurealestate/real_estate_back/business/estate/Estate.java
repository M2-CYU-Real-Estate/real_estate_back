package com.github.m2cyurealestate.real_estate_back.business.estate;

import java.time.LocalDateTime;

/**
 * @author Aldric Vitali Silvestre
 */
public class Estate {

    // TODO : replace with optionals
    private Long id;

    private boolean isFavorite;

    private String title;

    private String description;

    /**
     * The link permitting to go to the estate page
     */
    private String url;

    private String imageUrl;

    /**
     * Depends on the website where the estate came from.
     * No real proof exists that each reference is unique
     */
    private String reference;

    private EstateType type;

    private String cityName;

    private String postalCode;

    private long price;

    private long houseAreaSqrtM;

    private long groundAreaSqrtM;

    private int roomCount;

    private int bedroomCount;

    private int bathroomCount;

    private boolean isTerracePresent;

    private boolean isBalconyPresent;

    private boolean isElevatorPresent;

    private boolean isGaragePresent;

    private boolean isParkingPresent;

    private boolean isFittedKitchenPresent;

    private RateClass energyClass;

    private RateClass gazEmissionClass;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    public Estate() {
    }

    public Estate(long id,
                  boolean isFavorite,
                  String title,
                  String description,
                  String url,
                  String imageUrl,
                  String reference,
                  EstateType type,
                  String cityName,
                  String postalCode,
                  long price,
                  long houseAreaSqrtM,
                  long groundAreaSqrtM,
                  int roomCount,
                  int bedroomCount,
                  int bathroomCount,
                  boolean isTerracePresent,
                  boolean isBalconyPresent,
                  boolean isElevatorPresent,
                  boolean isGaragePresent,
                  boolean isParkingPresent,
                  boolean isFittedKitchenPresent,
                  RateClass energyClass,
                  RateClass gazEmissionClass,
                  LocalDateTime createdAt,
                  LocalDateTime lastUpdatedAt) {
        this.id = id;
        this.isFavorite = isFavorite;
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.reference = reference;
        this.type = type;
        this.cityName = cityName;
        this.postalCode = postalCode;
        this.price = price;
        this.houseAreaSqrtM = houseAreaSqrtM;
        this.groundAreaSqrtM = groundAreaSqrtM;
        this.roomCount = roomCount;
        this.bedroomCount = bedroomCount;
        this.bathroomCount = bathroomCount;
        this.isTerracePresent = isTerracePresent;
        this.isBalconyPresent = isBalconyPresent;
        this.isElevatorPresent = isElevatorPresent;
        this.isGaragePresent = isGaragePresent;
        this.isParkingPresent = isParkingPresent;
        this.isFittedKitchenPresent = isFittedKitchenPresent;
        this.energyClass = energyClass;
        this.gazEmissionClass = gazEmissionClass;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public EstateType getType() {
        return type;
    }

    public void setType(EstateType type) {
        this.type = type;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getHouseAreaSqrtM() {
        return houseAreaSqrtM;
    }

    public void setHouseAreaSqrtM(long houseAreaSqrtM) {
        this.houseAreaSqrtM = houseAreaSqrtM;
    }

    public long getGroundAreaSqrtM() {
        return groundAreaSqrtM;
    }

    public void setGroundAreaSqrtM(long groundAreaSqrtM) {
        this.groundAreaSqrtM = groundAreaSqrtM;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public int getBedroomCount() {
        return bedroomCount;
    }

    public void setBedroomCount(int bedroomCount) {
        this.bedroomCount = bedroomCount;
    }

    public int getBathroomCount() {
        return bathroomCount;
    }

    public void setBathroomCount(int bathroomCount) {
        this.bathroomCount = bathroomCount;
    }

    public boolean isTerracePresent() {
        return isTerracePresent;
    }

    public void setTerracePresent(boolean terracePresent) {
        isTerracePresent = terracePresent;
    }

    public boolean isBalconyPresent() {
        return isBalconyPresent;
    }

    public void setBalconyPresent(boolean balconyPresent) {
        isBalconyPresent = balconyPresent;
    }

    public boolean isElevatorPresent() {
        return isElevatorPresent;
    }

    public void setElevatorPresent(boolean elevatorPresent) {
        isElevatorPresent = elevatorPresent;
    }

    public boolean isGaragePresent() {
        return isGaragePresent;
    }

    public void setGaragePresent(boolean garagePresent) {
        isGaragePresent = garagePresent;
    }

    public boolean isParkingPresent() {
        return isParkingPresent;
    }

    public void setParkingPresent(boolean parkingPresent) {
        isParkingPresent = parkingPresent;
    }

    public boolean isFittedKitchenPresent() {
        return isFittedKitchenPresent;
    }

    public void setFittedKitchenPresent(boolean fittedKitchenPresent) {
        isFittedKitchenPresent = fittedKitchenPresent;
    }

    public RateClass getEnergyClass() {
        return energyClass;
    }

    public void setEnergyClass(RateClass energyClass) {
        this.energyClass = energyClass;
    }

    public RateClass getGazEmissionClass() {
        return gazEmissionClass;
    }

    public void setGazEmissionClass(RateClass gazEmissionClass) {
        this.gazEmissionClass = gazEmissionClass;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
