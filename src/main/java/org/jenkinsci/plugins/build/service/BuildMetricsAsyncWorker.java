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
 * Asynchronous periodic worker for scheduled metrics collection.
 * <p>
 * Runs at a configurable interval to collect build metrics automatically.
 * Uses Jenkins' built-in AsyncPeriodicWork for background task execution.
 * </p>
 *
 * @author mingsha
 * @date 2025-07-10
 */
@Extension
public class BuildMetricsAsyncWorker extends AsyncPeriodicWork {

    private static final Logger logger = LoggerFactory.getLogger(BuildMetricsAsyncWorker.class);

    private BuildMetrics buildMetrics;

    /**
     * Constructor - initializes the async worker with a name.
     */
    public BuildMetricsAsyncWorker() {
        super("build_metrics_async_worker");
    }

    /**
     * Injects the BuildMetrics service instance.
     * @param buildMetrics the build metrics service
     */
    @Inject
    public void setBuildMetrics(BuildMetrics buildMetrics) {
        this.buildMetrics = buildMetrics;
    }

    /**
     * Returns the recurrence period for this periodic task in milliseconds.
     * @return the period in milliseconds
     */
    @Override
    public long getRecurrencePeriod() {
        long collectingMetricsPeriodInMillis =
                TimeUnit.SECONDS.toMillis(BuildMetricsConfiguration.get().getCollectingBuildMetricsPeriodInSeconds());
        logger.info("Setting collection period to {} milliseconds", collectingMetricsPeriodInMillis);
        return collectingMetricsPeriodInMillis;
    }

    /**
     * Executes the metrics collection task.
     * @param taskListener the task listener for logging
     */
    @Override
    public void execute(TaskListener taskListener) {
        logger.info("Starting Jenkins build information collection");
        buildMetrics.collectMetrics();
        logger.info("Jenkins build information collection completed");
    }

    /**
     * Returns the normal logging level for this worker.
     * @return the logging level (FINE for detailed output)
     */
    @Override
    protected Level getNormalLoggingLevel() {
        return Level.FINE;
    }
}
