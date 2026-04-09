package org.jenkinsci.plugins.build.service;

/**
 * Service interface for Jenkins build metrics collection.
 * <p>
 * Defines standard methods for collecting and retrieving Jenkins job/build metrics.
 * Implementations are responsible for the actual collection logic and data retrieval.
 * </p>
 *
 * @author mingsha
 * @date 2025-07-10
 */
public interface BuildMetrics {

    /**
     * Retrieves the current cached build metrics data.
     * @return the build metrics data (typically as a JSON string)
     */
    String getMetrics();

    /**
     * Triggers a fresh collection of Jenkins build metrics.
     */
    void collectMetrics();
}
