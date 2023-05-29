package com.github.m2cyurealestate.real_estate_back.persistence.jooq;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A builder for fluently creating {@link Condition} clauses
 * from an input class.
 *
 * @param <U> The type of the object containing all filters.
 *            It is advised that this object contains optional attributes
 *            in order to be easily integrated into this builder
 */
public class ConditionBuilder<U> {

    /**
     * The object containing the filters
     */
    private final U targetFilters;

    private final List<Condition> conditions = new LinkedList<>();

    /**
     * Create the builder.
     *
     * @param targetFilters the object containing the filters.
     *                      It is advised that this object contains optional attributes
     *                      in order to be easily integrated into this builder
     */
    public ConditionBuilder(U targetFilters) {
        this.targetFilters = targetFilters;
    }

    /**
     * Add a condition factory that will create the condition if the
     * filter provided is effectively present.
     *
     * @param on               The function extracting the filter parameter from the base object
     *                         ({@link #targetFilters})
     * @param conditionFactory The function creating the {@link Condition} if the optional created by the {@code on}
     *                         parameter
     * @param <M>              The type of the current filter parameter
     * @return The builder itself, useful for chaining methods
     */
    public <M> ConditionBuilder<U> add(Function<U, Optional<M>> on,
                                       Function<M, Condition> conditionFactory) {
        var filter = on.apply(targetFilters);
        addConditionIfPresent(filter, conditionFactory);
        return this;
    }

    /**
     * Create the condition aggregating all other filter conditions encountered before
     *
     * @return The condition aggregating all others with AND clauses
     */
    public Condition build() {
        return DSL.and(conditions);
    }

    /**
     * If the filter is present, create the condition using the
     * factory function and put it in the conditions list.
     */
    private <M> void addConditionIfPresent(Optional<M> filter,
                                           Function<M, Condition> conditionFactory) {
        filter.map(conditionFactory)
                .ifPresent(conditions::add);
    }
}
