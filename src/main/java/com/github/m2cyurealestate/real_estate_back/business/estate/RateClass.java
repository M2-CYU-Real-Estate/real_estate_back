package com.github.m2cyurealestate.real_estate_back.business.estate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * General enum for rated estate attributes with letter
 * (for example, the energy class)
 *
 * @author Aldric Vitali Silvestre
 */
public enum RateClass {
    A(true, 7),
    B(true, 6),
    C(true, 5),
    D(true, 4),
    E(true, 3),
    F(true, 2),
    G(true, 1),
    NC(false, 0);

    private final boolean isSpecified;
    private final int rating;

    RateClass(boolean isSpecified, int rating) {
        this.isSpecified = isSpecified;
        this.rating = rating;
    }

    /**
     * Indicate whether the enum indicate that we found a note or not
     */
    public boolean isSpecified() {
        return isSpecified;
    }

    public int getRating() {
        return rating;
    }

    /**
     * Return a list containing all energy classes that are above this enum
     * (this value is also included in the list).
     */
    public Stream<RateClass> getGreaterClasses() {
        return Arrays.stream(RateClass.values())
                .filter(c -> c.getRating() >= this.getRating())
                .sorted(Comparator.comparing(RateClass::getRating).reversed());
    }

    /**
     * A safer version of {@link #valueOf(String)} where error leads
     * to the enum {@link #NC}
     *
     * @param rate the string to convert
     * @return The enum value, defaults to {@link #NC} if cannot convert properly.
     */
    public static RateClass fromString(String rate) {
        try {
            return RateClass.valueOf(rate);
        } catch (IllegalArgumentException e) {
            return NC;
        }
    }
}
