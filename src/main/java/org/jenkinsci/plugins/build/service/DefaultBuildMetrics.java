package org.jenkinsci.plugins.build.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicReference;

import org.jenkinsci.plugins.build.collector.JobCollector;
import org.jenkinsci.plugins.build.model.BuildMetricsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * @author chenlong
 */
public class DefaultBuildMetrics implements BuildMetrics {

    private static final Logger           logger = LoggerFactory.getLogger(DefaultBuildMetrics.class);

    private final AtomicReference<String> cachedMetrics;

    public DefaultBuildMetrics() {
        this.cachedMetrics = new AtomicReference<>("");
    }

    /**
     *
     * @return
     */
    @Override
    public String getMetrics() {
        return cachedMetrics.get();
    }

    /**
     *
     */
    @Override
    public void collectMetrics() {
        logger.debug("Collecting Build Metrics");
        try (StringWriter buffer = new StringWriter()) {
            final long start = System.currentTimeMillis();
            BuildMetricsVO buildMetricsVO = new JobCollector().collect();
            logger.info(String.format("Collecting Build Metrics cost：%d ms", System.currentTimeMillis() - start));
            buffer.write(getJsonStr(buildMetricsVO));
            cachedMetrics.set(buffer.toString());
        } catch (IOException e) {
            logger.debug("Unable to collect metrics");
        }
    }

    public static String getJsonStr(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception var2) {
            return null;
        }
    }
}
