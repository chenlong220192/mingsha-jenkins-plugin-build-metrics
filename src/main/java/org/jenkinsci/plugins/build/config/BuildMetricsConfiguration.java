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
 * @author chenlong
 */
@Extension(dynamicLoadable = YesNoMaybe.NO)
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

    public BuildMetricsConfiguration() {
        load();
        setPath(urlName);
        setNamespace(namespace);
        setCollectingBuildMetricsMinutes(collectingBuildMetricsMinutes);
        setCollectingBuildMetricsPeriodInSeconds(collectingBuildMetricsPeriodInSeconds);
    }

    public static BuildMetricsConfiguration get() {
        Descriptor configuration = Jenkins.getInstance().getDescriptor(BuildMetricsConfiguration.class);
        return (BuildMetricsConfiguration) configuration;
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        setPath(json.getString("path"));
        setNamespace(json.getString("namespace"));
        setCollectingBuildMetricsPeriodInSeconds(json.getLong("collectingBuildMetricsPeriodInSeconds"));
        setCollectingBuildMetricsMinutes(json.getLong("collectingBuildMetricsMinutes"));

        collectingBuildMetricsMinutes = validateCollectingBuildMetricsMinutes(json);
        collectingBuildMetricsPeriodInSeconds = validateCollectingBuildMetricsPeriodInSeconds(json);

        save();
        return super.configure(req, json);
    }

    /* --------------------------------------------------------- */

    public String getPath() {
        return StringUtils.isEmpty(additionalPath) ? urlName : urlName + "" + additionalPath;
    }

    public void setPath(String path) {
        if (path == null) {
            path = DEFAULT_ENDPOINT;
        } else {
            path = getPath();
        }
        urlName = path.split("/")[0];
        List<String> pathParts = Arrays.asList(path.split("/"));
        additionalPath = (pathParts.size() > 1 ? "/" : "") + StringUtils.join(pathParts.subList(1, pathParts.size()), "/");
        save();
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        if (namespace == null) {
            namespace = DEFAULT_NAMESPACE;
        }
        this.namespace = namespace;
        save();
    }

    public long getCollectingBuildMetricsPeriodInSeconds() {
        return collectingBuildMetricsPeriodInSeconds;
    }

    public void setCollectingBuildMetricsPeriodInSeconds(Long collectingMetricsPeriodInSeconds) {
        if (collectingMetricsPeriodInSeconds == null) {
            this.collectingBuildMetricsPeriodInSeconds = DEFAULT_COLLECTING_PERIOD_IN_SECONDS;
        } else {
            this.collectingBuildMetricsPeriodInSeconds = collectingMetricsPeriodInSeconds;
        }
        save();
    }

    public long getCollectingBuildMetricsMinutes() {
        return collectingBuildMetricsMinutes;
    }

    public void setCollectingBuildMetricsMinutes(Long collectingBuildMetricsMinutes) {
        if (collectingBuildMetricsMinutes == null) {
            this.collectingBuildMetricsMinutes = DEFAULT_COLLECTING_MINUTES;
        } else {
            this.collectingBuildMetricsMinutes = collectingBuildMetricsMinutes;
        }
        save();
    }

    public String getUrlName() {
        return urlName;
    }

    public String getAdditionalPath() {
        return additionalPath;
    }

    /* --------------------------------------------------------- */

    public FormValidation doCheckPath(@QueryParameter String value) {
        if (StringUtils.isEmpty(value)) {
            return FormValidation.error(Messages.path_required());
        } else {
            return FormValidation.ok();
        }
    }

    private Long validateCollectingBuildMetricsMinutes(JSONObject json) throws FormException {
        try {
            long value = json.getLong("collectingBuildMetricsMinutes");
            if (value > 0) {
                return value;
            }
        } catch (JSONException ignored) {
        }
        throw new FormException("CollectingBuildMetricsMinutes must be a positive integer", "collectingBuildMetricsMinutes");
    }

    private Long validateCollectingBuildMetricsPeriodInSeconds(JSONObject json) throws FormException {
        try {
            long value = json.getLong("collectingBuildMetricsPeriodInSeconds");
            if (value > 0) {
                return value;
            }
        } catch (JSONException ignored) {
        }
        throw new FormException("CollectingBuildMetricsPeriodInSeconds must be a positive integer", "collectingBuildMetricsPeriodInSeconds");
    }


}
