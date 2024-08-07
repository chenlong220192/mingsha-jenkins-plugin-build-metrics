package org.jenkinsci.plugins.build.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hudson.model.*;

/**
 * @author chenlong
 */
public class Runs {

    public static boolean includeBuildInMetrics(Run build) {
        boolean include = false;
        if (!build.isBuilding()) {
            include = true;
            Result result = build.getResult();
        }
        return include;
    }

    public static String getJobNameText(Run run) {
        if (run != null) {
            String fullDisplayName = run.getFullDisplayName();
            if (fullDisplayName != null) {
                return fullDisplayName.split("#")[0].trim();
            }
        }
        return null;
    }

    public static String getResultText(Run run) {
        if (run != null) {
            Result result = run.getResult();
            if (result != null) {
                return result.toString();
            }
        }
        return null;
    }

    public static String getRunnerText(Run run) {
        if (run != null) {
            Cause.UserIdCause userIdCause = (Cause.UserIdCause) run.getCause(Cause.UserIdCause.class);
            if (userIdCause != null) {
                return userIdCause.getUserId();
            }
        }
        return null;
    }

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
