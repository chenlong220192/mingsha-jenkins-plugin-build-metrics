package org.jenkinsci.plugins.build.util;

import org.jenkinsci.plugins.build.config.BuildMetricsConfiguration;

/**
 * @author chenlong
 */
public class ConfigurationUtils {

    /**
     *
     * @return
     */
    public static String getNamespace() {
        return BuildMetricsConfiguration.get().getNamespace();
    }

    /**
     *
     * @return
     */
    public static Long getCollectingBuildMetricsMinutes() {
        return BuildMetricsConfiguration.get().getCollectingBuildMetricsMinutes();
    }

}
