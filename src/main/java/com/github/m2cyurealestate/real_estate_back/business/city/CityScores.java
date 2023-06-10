package com.github.m2cyurealestate.real_estate_back.business.city;

/**
 * @author Aldric Vitali Silvestre
 */
public record CityScores(
        double security,
        double education,
        double hobbies,
        double environment,
        double practicality
) {

    public double average() {
        return (security + education + hobbies + environment + practicality) / 5.0;
    }
}
