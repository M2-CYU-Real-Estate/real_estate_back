package com.github.m2cyurealestate.real_estate_back.api.rest.utils;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * A watch utility class designed to sleep the thread
 * if and only if the time wanted is not met.
 *
 * @author Aldric Vitali Silvestre
 */
public class Delayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Delayer.class);

    private final long targetTimeMs;

    private final long variationTimeMs;

    private final StopWatch watch;


    /**
     * Create the delayer and start the time (this can be reset with the {@link #reset()} method).
     *
     * @param targetTimeMs    The base time to meet. Must not be negative
     * @param variationTimeMs If greater than 0, this will adjust a small (random) variation in the target time.
     *                        Must not be negative
     * @throws IllegalArgumentException If the target and variation time is negative
     */
    public Delayer(long targetTimeMs, long variationTimeMs) {
        if (targetTimeMs < 0) {
            throw new IllegalArgumentException("Target time must not be negative");
        }
        if (variationTimeMs < 0) {
            throw new IllegalArgumentException("Variation time must not be negative");
        }
        this.targetTimeMs = targetTimeMs;
        this.variationTimeMs = variationTimeMs;
        this.watch = new StopWatch();
        watch.start();
    }

    /**
     * Create the delayer and start the time (this can be reset with the {@link #reset()} method).
     * <p>
     * This is a shortcut for {@link #Delayer(long, long)}.
     *
     * @param targetTimeMs The base time to meet. Must not be negative
     */
    public Delayer(long targetTimeMs) {
        this(targetTimeMs, 0);
    }

    /**
     * Reset the timer and restart it.
     */
    public void reset() {
        watch.reset();
        watch.start();
    }

    /**
     * Stops the thread if the target time is not already reached.
     */
    public void applyDelay() {
        var currentTimeMs = watch.getTime();
        var timeToMetMs = getTimeWithVariation();
        if (currentTimeMs < timeToMetMs) {
            try {
                Thread.sleep(timeToMetMs - currentTimeMs);
            } catch (InterruptedException e) {
                LOGGER.warn("Thread delay was cancelled");
            }
        }
    }

    private long getTimeWithVariation() {
        if (variationTimeMs > 0) {
            Random random = new Random();
            var variation = random.nextLong(-variationTimeMs, variationTimeMs);
            return targetTimeMs + variation;
        }
        return targetTimeMs;
    }
}
