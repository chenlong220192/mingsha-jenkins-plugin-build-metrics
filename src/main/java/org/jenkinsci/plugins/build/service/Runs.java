package org.jenkinsci.plugins.build.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hudson.model.*;

/**
 * Utility class for accessing Jenkins build (Run) information.
 * <p>
 * Provides common operations for retrieving build history data.
 * </p>
 *
 * @author mingsha
 * @date 2025-07-10
 */
@SuppressWarnings("unchecked")
public class Runs {

    /**
     * Determines if a build should be included in metrics.
     * <p>
     * Current logic: builds that are not in progress (isBuilding() returns false)
     * are included in statistics. Can be extended for more complex filtering.
     * </p>
     * @param build the build to evaluate
     * @return true if the build should be included
     */
    public static boolean includeBuildInMetrics(Run build) {
        boolean include = false;
        if (!build.isBuilding()) {
            // Build is complete, include in metrics
            include = true;
            // Get build result (can be used for extended filtering)
            Result result = build.getResult();
        }
        return include;
    }

    /**
     * Extracts the job name from a build's full display name.
     * <p>
     * Removes build number and other suffixes from the full display name.
     * </p>
     * @param run the build run
     * @return the job name, or null if extraction fails
     */
    public static String getJobNameText(Run run) {
        if (run != null) {
            String fullDisplayName = run.getFullDisplayName();
            if (fullDisplayName != null) {
                return fullDisplayName.split("#")[0].trim();
            }
        }
        return null;
    }

    /**
     * Returns the build result as a string.
     * <p>
     * Converts the build result enum to its string representation.
     * </p>
     * @param run the build run
     * @return result string such as "SUCCESS", "FAILURE", etc.
     */
    public static String getResultText(Run run) {
        if (run != null) {
            Result result = run.getResult();
            if (result != null) {
                return result.toString();
            }
        }
        return null;
    }

    /**
     * Gets the user ID that triggered the build.
     * <p>
     * Extracts user ID from build causes.
     * Currently only supports UserIdCause type.
     * </p>
     * @param run the build run
     * @return the user ID, or null if unavailable
     */
    public static String getRunnerText(Run run) {
        if (run != null) {
            Cause.UserIdCause userIdCause = (Cause.UserIdCause) run.getCause(Cause.UserIdCause.class);
            if (userIdCause != null) {
                return userIdCause.getUserId();
            }
        }
        return null;
    }

    /**
     * Gets the build parameters as a map.
     * <p>
     * Extracts all parameters from ParametersAction and returns
     * a mapping of parameter names to their values.
     * </p>
     * @param build the build
     * @return a map of parameter names to values, or an empty map if no parameters
     */
    public static Map<String, Object> getBuildParameters(Run build) {
        List<ParametersAction> actions = build.getActions(ParametersAction.class);
        Map<String, Object> answer = new HashMap<>();
        for (ParametersAction action : actions) {
            List<ParameterValue> parameters = action.getParameters();
            if (parameters != null) {
                for (ParameterValue parameter : parameters) {
                    String name = parameter.getName();
                    Object value = parameter.getValue();
                    answer.put(name, value);
                }
            }
        }
        return answer;
    }
}
