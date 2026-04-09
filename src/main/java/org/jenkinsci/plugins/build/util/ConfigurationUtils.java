package org.jenkinsci.plugins.build.util;

import org.jenkinsci.plugins.build.config.BuildMetricsConfiguration;

/**
 * Utility class for accessing Build Metrics configuration.
 * <p>
 * Provides convenient methods to retrieve global configuration parameters.
 * </p>
 *
 * @author mingsha
 * @date 2025-07-10
 */
public class ConfigurationUtils {

    /**
     * Private constructor - prevents instantiation.
     */
    private ConfigurationUtils() {}

    /**
     * Returns the current namespace.
     * <p>
     * Retrieves the plugin namespace from global configuration,
     * used to distinguish metrics from different environments or sources.
     * </p>
     * @return the namespace string
     */
    public static String getNamespace() {
        return BuildMetricsConfiguration.get().getNamespace();
    }

    /**
     * Returns the collection time window in minutes.
     * <p>
     * Retrieves the time window from global configuration,
     * used to filter builds within a specific time range.
     * </p>
     * @return the time window in minutes
     */
    public static Long getCollectingBuildMetricsMinutes() {
        return BuildMetricsConfiguration.get().getCollectingBuildMetricsMinutes();
    }
}
