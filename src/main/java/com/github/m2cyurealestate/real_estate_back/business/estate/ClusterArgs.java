package com.github.m2cyurealestate.real_estate_back.business.estate;

/**
 * @author Aldric Vitali Silvestre
 */
public record ClusterArgs(int cluster, String department) {
    public ClusterArgs(Estate estate) {
        this(estate.getCluster(), estate.getDepartmentNumber());
    }
}
