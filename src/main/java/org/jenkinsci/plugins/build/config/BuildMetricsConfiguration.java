package org.jenkinsci.plugins.build.config;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import jenkins.YesNoMaybe;
import jenkins.model.GlobalConfiguration;
import jenkins.model.Jenkins;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Global configuration for the Jenkins Build Metrics plugin.
 * <p>
 * Manages plugin-wide settings including namespace, collection period, and time window.
 * Supports dynamic loading from and persistence to Jenkins configuration.
 * </p>
 *
 * @author mingsha
 * @date 2025-07-10
 */
@Extension(dynamicLoadable = YesNoMaybe.NO)
@SuppressWarnings("deprecation")
public class BuildMetricsConfiguration extends GlobalConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BuildMetricsConfiguration.class);

    private static final String DEFAULT_NAMESPACE = "default";
    private static final String DEFAULT_ENDPOINT = "build/metrics";
    static final long DEFAULT_COLLECTING_PERIOD_IN_SECONDS = TimeUnit.MINUTES.toSeconds(2);
    static final long DEFAULT_COLLECTING_MINUTES = TimeUnit.MINUTES.toMinutes(60);

    private String urlName = null;
    private String namespace;
    private String additionalPath;
    private Long collectingBuildMetricsMinutes = null;
    private Long collectingBuildMetricsPeriodInSeconds = null;

    /**
     * Constructor - initializes configuration with defaults if not loaded.
     */
    public BuildMetricsConfiguration() {
        load();
        // Initialize defaults (save is handled by Jenkins automatically)
        if (urlName == null) {
            urlName = DEFAULT_ENDPOINT;
        }
        if (namespace == null) {
            namespace = DEFAULT_NAMESPACE;
        }
        if (collectingBuildMetricsMinutes == null) {
            collectingBuildMetricsMinutes = DEFAULT_COLLECTING_MINUTES;
        }
        if (collectingBuildMetricsPeriodInSeconds == null) {
            collectingBuildMetricsPeriodInSeconds = DEFAULT_COLLECTING_PERIOD_IN_SECONDS;
        }
        // Parse URL name from path
        urlName = urlName.split("/")[0];
        List<String> pathParts = Arrays.asList(urlName.split("/"));
        additionalPath = (pathParts.size() > 1 ? "/" : "") + StringUtils.join(pathParts.subList(1, pathParts.size()), "/");
    }

    /**
     * Returns the global configuration singleton instance.
     * @return the BuildMetricsConfiguration instance
     */
    public static BuildMetricsConfiguration get() {
        Descriptor configuration = Jenkins.get().getDescriptor(BuildMetricsConfiguration.class);
        return (BuildMetricsConfiguration) configuration;
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        // Set basic configuration items
        setPath(json.getString("path"));
        setNamespace(json.getString("namespace"));
        setCollectingBuildMetricsPeriodInSeconds(json.getLong("collectingBuildMetricsPeriodInSeconds"));
        setCollectingBuildMetricsMinutes(json.getLong("collectingBuildMetricsMinutes"));

        // Validate and reset configuration values
        collectingBuildMetricsMinutes = validateCollectingBuildMetricsMinutes(json);
        collectingBuildMetricsPeriodInSeconds = validateCollectingBuildMetricsPeriodInSeconds(json);

        // Save configuration to disk
        save();
        return super.configure(req, json);
    }

    /**
     * Returns the full configured path.
     * @return the path
     */
    public String getPath() {
        return StringUtils.isEmpty(additionalPath) ? urlName : urlName + "" + additionalPath;
    }

    /**
     * Sets the API endpoint path.
     * @param path the path to set
     */
    public void setPath(String path) {
        if (path == null) {
            // Use default endpoint path
            path = DEFAULT_ENDPOINT;
        }
        // Extract URL name (first part of path)
        urlName = path.split("/")[0];
        // Parse path parts
        List<String> pathParts = Arrays.asList(path.split("/"));
        // Build additional path (all parts except the first)
        additionalPath = (pathParts.size() > 1 ? "/" : "") + StringUtils.join(pathParts.subList(1, pathParts.size()), "/");
        // Note: save() is called by configure() method for consistency
    }

    /**
     * Returns the namespace for metrics organization.
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Sets the namespace for metrics organization.
     * @param namespace the namespace to set
     */
    public void setNamespace(String namespace) {
        if (namespace == null) {
            // Use default namespace
            namespace = DEFAULT_NAMESPACE;
        }
        this.namespace = namespace;
        // Note: save() is called by configure() method for consistency
    }

    /**
     * Returns the collection period in seconds.
     * @return the collection period in seconds
     */
    public long getCollectingBuildMetricsPeriodInSeconds() {
        return collectingBuildMetricsPeriodInSeconds;
    }

    /**
     * Sets the collection period in seconds.
     * @param collectingMetricsPeriodInSeconds the period in seconds
     */
    public void setCollectingBuildMetricsPeriodInSeconds(Long collectingMetricsPeriodInSeconds) {
        if (collectingMetricsPeriodInSeconds == null) {
            // Use default collection period
            this.collectingBuildMetricsPeriodInSeconds = DEFAULT_COLLECTING_PERIOD_IN_SECONDS;
        } else {
            // Use user-specified collection period
            this.collectingBuildMetricsPeriodInSeconds = collectingMetricsPeriodInSeconds;
        }
        // Note: save() is called by configure() method for consistency
    }

    /**
     * Returns the collection time window in minutes.
     * @return the time window in minutes
     */
    public long getCollectingBuildMetricsMinutes() {
        return collectingBuildMetricsMinutes;
    }

    /**
     * Sets the collection time window in minutes.
     * @param collectingBuildMetricsMinutes the time window in minutes
     */
    public void setCollectingBuildMetricsMinutes(Long collectingBuildMetricsMinutes) {
        if (collectingBuildMetricsMinutes == null) {
            // Use default collection time window
            this.collectingBuildMetricsMinutes = DEFAULT_COLLECTING_MINUTES;
        } else {
            // Use user-specified collection time window
            this.collectingBuildMetricsMinutes = collectingBuildMetricsMinutes;
        }
        // Note: save() is called by configure() method for consistency
    }

    /**
     * Returns the URL name (first segment of the path).
     * @return the URL name
     */
    public String getUrlName() {
        return urlName;
    }

    /**
     * Returns the additional path (path after the URL name).
     * @return the additional path
     */
    public String getAdditionalPath() {
        return additionalPath;
    }

    /**
     * Validates the path parameter from the configuration form.
     * @param value the path value to validate
     * @return validation result
     */
    public FormValidation doCheckPath(@QueryParameter String value) {
        if (StringUtils.isEmpty(value)) {
            return FormValidation.error(Messages.path_required());
        } else {
            return FormValidation.ok();
        }
    }

    /**
     * Validates the collecting time window parameter.
     * @param json the configuration JSON
     * @return the validated time window value
     * @throws FormException if validation fails
     */
    private Long validateCollectingBuildMetricsMinutes(JSONObject json) throws FormException {
        try {
            long value = json.getLong("collectingBuildMetricsMinutes");
            if (value > 0) {
                return value;
            }
        } catch (JSONException ignored) {
            // ignore JSONException, will throw FormException below
        }
        throw new FormException("CollectingBuildMetricsMinutes must be a positive integer", "collectingBuildMetricsMinutes");
    }

    /**
     * Validates the collection period parameter.
     * @param json the configuration JSON
     * @return the validated period value
     * @throws FormException if validation fails
     */
    private Long validateCollectingBuildMetricsPeriodInSeconds(JSONObject json) throws FormException {
        try {
            long value = json.getLong("collectingBuildMetricsPeriodInSeconds");
            if (value > 0) {
                return value;
            }
        } catch (JSONException ignored) {
            // ignore JSONException, will throw FormException below
        }
        throw new FormException("CollectingBuildMetricsPeriodInSeconds must be a positive integer", "collectingBuildMetricsPeriodInSeconds");
    }
}
