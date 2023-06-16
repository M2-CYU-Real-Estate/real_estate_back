package com.github.m2cyurealestate.real_estate_back.api.rest.routes.estate;

import com.github.m2cyurealestate.real_estate_back.business.estate.EstateType;
import com.github.m2cyurealestate.real_estate_back.business.estate.RateClass;
import org.springdoc.core.annotations.ParameterObject;

import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
@ParameterObject
public class EstateFiltersParams {

    private Optional<EstateType> type = Optional.empty();

    private Optional<String> city = Optional.empty();

    /**
     * min price
     */
    private Optional<Long> minPr = Optional.empty();

    /**
     * max price
     */
    private Optional<Long> maxPr = Optional.empty();

    /**
     * min house area
     */
    private Optional<Long> minHArea = Optional.empty();

    /**
     * max house area
     */
    private Optional<Long> maxHArea = Optional.empty();

    private Optional<Long> rooms = Optional.empty();

    private Optional<Long> bathrooms = Optional.empty();

    /**
     * Is terrace presence mandatory ?
     */
    private Optional<Boolean> terrace = Optional.empty();

    /**
     * Is balcony presence mandatory ?
     */
    private Optional<Boolean> balcony = Optional.empty();

    /**
     * Is parking presence mandatory ?
     */
    private Optional<Boolean> parking = Optional.empty();

    /**
     * Is garage presence mandatory ?
     */
    private Optional<Boolean> garage = Optional.empty();

    /**
     * Is fKitchen presence mandatory ?
     */
    private Optional<Boolean> fKitchen = Optional.empty();

    /**
     * Is elevator presence mandatory ?
     */
    private Optional<Boolean> elevator = Optional.empty();

    /**
     * Minimal energy class suitable
     */
    private Optional<RateClass> enClass = Optional.empty();

    /**
     * Minimal gaz emission class suitable
     */
    private Optional<RateClass> gzClass = Optional.empty();

    // GETTERS / SETTERS
    public Optional<EstateType> getType() {
        return type;
    }

    public void setType(Optional<EstateType> type) {
        this.type = type;
    }

    public Optional<String> getCity() {
        return city;
    }

    public void setCity(Optional<String> city) {
        this.city = city;
    }

    public Optional<Long> getMinPr() {
        return minPr;
    }

    public void setMinPr(Optional<Long> minPr) {
        this.minPr = minPr;
    }

    public Optional<Long> getMaxPr() {
        return maxPr;
    }

    public void setMaxPr(Optional<Long> maxPr) {
        this.maxPr = maxPr;
    }

    public Optional<Long> getMinHArea() {
        return minHArea;
    }

    public void setMinHArea(Optional<Long> minHArea) {
        this.minHArea = minHArea;
    }

    public Optional<Long> getMaxHArea() {
        return maxHArea;
    }

    public void setMaxHArea(Optional<Long> maxHArea) {
        this.maxHArea = maxHArea;
    }

    public Optional<Boolean> getTerrace() {
        return terrace;
    }

    public void setTerrace(Optional<Boolean> terrace) {
        this.terrace = terrace;
    }

    public Optional<Boolean> getBalcony() {
        return balcony;
    }

    public void setBalcony(Optional<Boolean> balcony) {
        this.balcony = balcony;
    }

    public Optional<Boolean> getParking() {
        return parking;
    }

    public void setParking(Optional<Boolean> parking) {
        this.parking = parking;
    }

    public Optional<Boolean> getGarage() {
        return garage;
    }

    public void setGarage(Optional<Boolean> garage) {
        this.garage = garage;
    }

    public Optional<Boolean> getfKitchen() {
        return fKitchen;
    }

    public void setfKitchen(Optional<Boolean> fKitchen) {
        this.fKitchen = fKitchen;
    }

    public Optional<Boolean> getElevator() {
        return elevator;
    }

    public void setElevator(Optional<Boolean> elevator) {
        this.elevator = elevator;
    }

    public Optional<RateClass> getEnClass() {
        return enClass;
    }

    public void setEnClass(Optional<RateClass> enClass) {
        this.enClass = enClass;
    }

    public Optional<RateClass> getGzClass() {
        return gzClass;
    }

    public void setGzClass(Optional<RateClass> gzClass) {
        this.gzClass = gzClass;
    }

    public Optional<Long> getRooms() {
        return rooms;
    }

    public void setRooms(Optional<Long> rooms) {
        this.rooms = rooms;
    }

    public Optional<Long> getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Optional<Long> bathrooms) {
        this.bathrooms = bathrooms;
    }
}
