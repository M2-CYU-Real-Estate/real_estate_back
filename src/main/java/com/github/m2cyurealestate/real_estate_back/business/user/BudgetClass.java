package com.github.m2cyurealestate.real_estate_back.business.user;

import org.apache.commons.lang3.Range;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Aldric Vitali Silvestre
 */
public enum BudgetClass {
    LOW("low", Range.between(0L, 100_000L)),
    MEDIUM("medium", Range.between(100_000L, 250_000L)),
    NORMAL("normal", Range.between(250_000L, 500_000L)),
    RICH("rich", Range.between(500_000L, 1_000_000L)),
    VERY_RICH("very_rich", Range.between(1_000_000L, Long.MAX_VALUE));

    private final String groupName;

    private final Range<Long> budgetRange;

    public String getGroupName() {
        return groupName;
    }

    public Range<Long> getBudgetRange() {
        return budgetRange;
    }

    BudgetClass(String groupName, Range<Long> budgetRange) {
        this.groupName = groupName;
        this.budgetRange = budgetRange;
    }

    public static Optional<BudgetClass> fromString(String cls) {
        return Arrays.stream(values())
                .filter(c -> c.getGroupName().equalsIgnoreCase(cls))
                .findFirst();
    }
}
