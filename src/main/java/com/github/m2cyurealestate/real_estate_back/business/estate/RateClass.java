package com.github.m2cyurealestate.real_estate_back.business.estate;

/**
 * General enum for rated estate attributes with letter
 * (for example, the energy class)
 *
 * @author Aldric Vitali Silvestre
 */
public enum RateClass {
    A(true),
    B(true),
    C(true),
    D(true),
    E(true),
    F(true),
    G(true),
    NC(false);

    private final boolean isSpecified;

    RateClass(boolean isSpecified) {
        this.isSpecified = isSpecified;
    }

    /**
     * Indicate whether the enum indicate that we found a note or not
     */
    public boolean isSpecified() {
        return isSpecified;
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
