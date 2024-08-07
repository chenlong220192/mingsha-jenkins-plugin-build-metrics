package org.jenkinsci.plugins.build.service;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.jenkinsci.plugins.build.config.BuildMetricsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import hudson.Extension;
import hudson.model.AsyncPeriodicWork;
import hudson.model.TaskListener;

/**
 * @author chenlong
 */
@Extension
public class BuildMetricsAsyncWorker extends AsyncPeriodicWork {

    private static final Logger logger = LoggerFactory.getLogger(BuildMetricsAsyncWorker.class);

    private BuildMetrics BuildMetrics;

    public BuildMetricsAsyncWorker() {
        super("build_metrics_async_worker");
    }

    @Inject
    public void setBuildMetrics(BuildMetrics BuildMetrics) {
        this.BuildMetrics = BuildMetrics;
    }

    @Override
    public long getRecurrencePeriod() {
        long collectingMetricsPeriodInMillis =
                TimeUnit.SECONDS.toMillis(BuildMetricsConfiguration.get().getCollectingBuildMetricsPeriodInSeconds());
        logger.debug("Setting recurrence period to {} in milliseconds", collectingMetricsPeriodInMillis);
        return collectingMetricsPeriodInMillis;
    }

    @Override
    public void execute(TaskListener taskListener) {
        logger.debug("Collecting Jenkins build info");
        BuildMetrics.collectMetrics();
        logger.debug("Jenkins Build info collected successfully");
    }

    @Override
    protected Level getNormalLoggingLevel() {
        return Level.FINE;
    }

}
