package org.jenkinsci.plugins.build.rest;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.UnprotectedRootAction;
import hudson.util.HttpResponses;
import org.jenkinsci.plugins.build.config.BuildMetricsConfiguration;
import org.jenkinsci.plugins.build.service.BuildMetrics;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * @author chenlong
 */
@Extension
public class BuildMetricsAction implements UnprotectedRootAction {

    private BuildMetrics BuildMetrics;

    @Inject
    public void setBuildMetrics(BuildMetrics BuildMetrics) {
        this.BuildMetrics = BuildMetrics;
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Jenkins Build Metrics Collector";
    }

    @Override
    public String getUrlName() {
        return BuildMetricsConfiguration.get().getUrlName();
    }

    public HttpResponse doDynamic(StaplerRequest request) {
        if (request.getRestOfPath().equals(BuildMetricsConfiguration.get().getAdditionalPath())) {
            if (hasAccess()) {
                return buildInfoResponse();
            }
            return HttpResponses.forbidden();
        }
        return HttpResponses.notFound();
    }

    private boolean hasAccess() {
        return true;
    }

    private HttpResponse buildInfoResponse() {
        return (request, response, node) -> {
            response.setStatus(StaplerResponse.SC_OK);
            response.setContentType("text/plain; version=0.0.4; charset=utf-8");
            response.addHeader("Cache-Control", "must-revalidate,no-cache,no-store");
            response.getWriter().write(BuildMetrics.getMetrics());
        };
    }
}
