package org.jenkinsci.plugins.build.service.impl;

import java.util.concurrent.atomic.AtomicReference;

import com.alibaba.fastjson2.JSON;
import org.jenkinsci.plugins.build.collector.JobCollector;
import org.jenkinsci.plugins.build.model.BuildMetricsVO;
import org.jenkinsci.plugins.build.service.BuildMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the BuildMetrics service.
 * <p>
 * Handles periodic or manual collection of Jenkins Job/Build metrics,
 * and caches the result as a JSON string for efficient retrieval.
 * </p>
 *
 * @author mingsha
 * @date 2025-07-10
 */
public class DefaultBuildMetrics implements BuildMetrics {

    private static final Logger           logger = LoggerFactory.getLogger(DefaultBuildMetrics.class);

    private final AtomicReference<String> cachedMetrics;

    /**
     * Constructor - initializes the cache.
     */
    public DefaultBuildMetrics() {
        this.cachedMetrics = new AtomicReference<>("");
    }

    /**
     * Returns the cache reference (for internal use only).
     * @return the cached metrics AtomicReference
     */
    public AtomicReference<String> getCachedMetrics() {
        return cachedMetrics;
    }

    /**
     * Returns the currently cached build metrics data.
     * @return the build metrics as a JSON string
     */
    @Override
    public String getMetrics() {
        return cachedMetrics.get();
    }

    /**
     * Collects Jenkins build metrics and updates the cache.
     */
    @Override
    public void collectMetrics() {
        logger.info("Starting build metrics collection");
        final long start = System.currentTimeMillis();
        BuildMetricsVO buildMetricsVO = new JobCollector().collect();
        String jsonStr = getJsonStr(buildMetricsVO);
        cachedMetrics.set(jsonStr != null ? jsonStr : "");
        logger.info("Build metrics collection completed in {} ms", System.currentTimeMillis() - start);
    }

    /**
     * Serializes an object to a JSON string.
     * @param object the object to serialize
     * @return the JSON string, or null if serialization fails
     */
    public static String getJsonStr(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            logger.error("JSON serialization failed", e);
            return null;
        }
    }
}
