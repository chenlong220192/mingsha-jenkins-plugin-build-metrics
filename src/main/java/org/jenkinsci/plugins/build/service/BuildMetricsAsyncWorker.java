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
 * @author mingsha
 * @date 2025-07-10
 */
@Extension
public class BuildMetricsAsyncWorker extends AsyncPeriodicWork {

    private static final Logger logger = LoggerFactory.getLogger(BuildMetricsAsyncWorker.class);

    private BuildMetrics BuildMetrics;

    /**
     * 构造方法，初始化异步采集任务。
     */
    public BuildMetricsAsyncWorker() {
        super("build_metrics_async_worker");
    }

    /**
     * 注入 BuildMetrics 实例。
     * @param BuildMetrics 构建指标服务
     */
    @Inject
    public void setBuildMetrics(BuildMetrics BuildMetrics) {
        this.BuildMetrics = BuildMetrics;
    }

    /**
     * 获取采集任务的执行周期（毫秒）。
     * @return 周期（毫秒）
     */
    @Override
    public long getRecurrencePeriod() {
        long collectingMetricsPeriodInMillis =
                TimeUnit.SECONDS.toMillis(BuildMetricsConfiguration.get().getCollectingBuildMetricsPeriodInSeconds());
        logger.info("设置采集周期为 {} 毫秒", collectingMetricsPeriodInMillis);
        return collectingMetricsPeriodInMillis;
    }

    /**
     * 执行采集任务。
     * @param taskListener 任务监听器
     */
    @Override
    public void execute(TaskListener taskListener) {
        logger.info("开始采集 Jenkins 构建信息");
        BuildMetrics.collectMetrics();
        logger.info("Jenkins 构建信息采集完成");
    }

    /**
     * 获取日志级别。
     * @return 日志级别
     */
    @Override
    protected Level getNormalLoggingLevel() {
        return Level.FINE;
    }

}
