package org.jenkinsci.plugins.build.api;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.UnprotectedRootAction;
import hudson.util.HttpResponses;
import org.jenkinsci.plugins.build.config.BuildMetricsConfiguration;
import org.jenkinsci.plugins.build.service.BuildMetrics;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.io.IOException;
import jenkins.model.Jenkins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jenkins Build Metrics REST API Action.
 * <p>
 * Exposes build metrics through HTTP endpoints for external monitoring systems (e.g., Prometheus).
 * This action provides both metrics retrieval and manual collection trigger endpoints.
 * </p>
 *
 * @author mingsha
 * @date 2025-07-10
 */
@Extension
@SuppressWarnings("deprecation")
public class BuildMetricsAction implements UnprotectedRootAction {

    private BuildMetrics buildMetrics;
    private static final Logger logger = LoggerFactory.getLogger(BuildMetricsAction.class);

    /**
     * Default constructor.
     */
    public BuildMetricsAction() {}

    /**
     * Injects the BuildMetrics service instance.
     * @param buildMetrics the build metrics service to inject
     */
    @Inject
    public void setBuildMetrics(BuildMetrics buildMetrics) {
        this.buildMetrics = buildMetrics;
    }

    /**
     * Returns the icon file name for this action.
     * This plugin does not use a sidebar icon.
     * @return null (no icon)
     */
    @Override
    public String getIconFileName() {
        return null;
    }

    /**
     * Returns the display name shown in Jenkins UI.
     * @return the display name
     */
    @Override
    public String getDisplayName() {
        return "Jenkins Build Metrics Collector";
    }

    /**
     * Returns the URL name for this action.
     * The URL is configured via BuildMetricsConfiguration.
     * @return the URL name
     */
    @Override
    public String getUrlName() {
        return BuildMetricsConfiguration.get().getUrlName();
    }

    /**
     * Handles dynamic REST routing for metrics retrieval.
     * Endpoint: /{urlName}/{additionalPath}
     * @param request the Stapler request
     * @return HTTP response
     */
    public HttpResponse doDynamic(StaplerRequest request) {
        if (request.getRestOfPath().equals(BuildMetricsConfiguration.get().getAdditionalPath())) {
            if (hasAccess()) {
                return buildInfoResponse();
            }
            return HttpResponses.forbidden();
        }
        return HttpResponses.notFound();
    }

    /**
     * Manually triggers a metrics collection.
     * <p>
     * Endpoint: /{urlName}/collect
     * Requires Jenkins ADMINISTER permission or valid API Token authentication.
     * </p>
     * @param request the Stapler request
     * @return HTTP response indicating success or failure
     */
    public HttpResponse doCollect(StaplerRequest request) {
        if (!hasJenkinsPermission()) {
            return HttpResponses.forbidden();
        }
        try {
            buildMetrics.collectMetrics();
            return HttpResponses.ok();
        } catch (Exception e) {
            logger.error("Failed to collect build metrics manually", e);
            return HttpResponses.error(500, "Collection failed: " + e.getMessage());
        }
    }

    /**
     * Checks if the current user has Jenkins ADMINISTER permission.
     * @return true if user has ADMINISTER permission, false otherwise
     */
    private boolean hasJenkinsPermission() {
        try {
            return Jenkins.get().hasPermission(Jenkins.ADMINISTER);
        } catch (Exception ignore) {
            // ignore exception, just return false
            return false;
        }
    }

    /**
     * Checks if the current user has access to view metrics.
     * Requires either ADMINISTER or READ permission.
     * @return true if access is allowed, false otherwise
     */
    private boolean hasAccess() {
        try {
            Jenkins jenkins = Jenkins.get();
            return jenkins.hasPermission(Jenkins.ADMINISTER)
                || jenkins.hasPermission(Jenkins.READ);
        } catch (Exception ignore) {
            return false;
        }
    }

    /**
     * Builds the metrics HTTP response with JSON data.
     * @return the HTTP response containing metrics data
     */
    private HttpResponse buildInfoResponse() {
        return new HttpResponse() {
            @Override
            public void generateResponse(StaplerRequest request, StaplerResponse response, Object node) throws IOException {
                response.setStatus(StaplerResponse.SC_OK);
                response.setContentType("text/plain; version=0.0.4; charset=utf-8");
                response.addHeader("Cache-Control", "must-revalidate,no-cache,no-store");
                response.getWriter().write(buildMetrics.getMetrics());
            }
        };
    }
}
