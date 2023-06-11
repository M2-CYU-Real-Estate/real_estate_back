package com.github.m2cyurealestate.real_estate_back.business.user;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
public enum BudgetClass {
    LOW("low"),
    MEDIUM("medium"),
    NORMAL("normal"),
    RICH("rich"),
    VERY_RICH("very_rich");

    private final String groupName;

    public String getGroupName() {
        return groupName;
    }

    BudgetClass(String groupName) {
        this.groupName = groupName;
    }

    public static Optional<BudgetClass> fromString(String cls) {
        return Arrays.stream(values())
                .filter(c -> c.getGroupName().equalsIgnoreCase(cls))
                .findFirst();
    }
}
