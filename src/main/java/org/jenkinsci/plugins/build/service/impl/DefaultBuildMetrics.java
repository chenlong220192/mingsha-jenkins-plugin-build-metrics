package org.jenkinsci.plugins.build.service.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicReference;

import com.alibaba.fastjson2.JSON;
import org.jenkinsci.plugins.build.collector.JobCollector;
import org.jenkinsci.plugins.build.model.BuildMetricsVO;
import org.jenkinsci.plugins.build.service.BuildMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jenkins 构建指标采集服务默认实现。
 * <p>
 * 负责定时或手动采集 Jenkins Job/Build 的相关指标，并缓存为 JSON 字符串。
 * </p>
 * @author mingsha
 * @date 2025-07-10
 */
public class DefaultBuildMetrics implements BuildMetrics {

    private static final Logger           logger = LoggerFactory.getLogger(DefaultBuildMetrics.class);

    private final AtomicReference<String> cachedMetrics;

    /**
     * 构造方法，初始化缓存对象。
     */
    public DefaultBuildMetrics() {
        this.cachedMetrics = new AtomicReference<>("");
    }

    /**
     * 获取缓存对象（仅供内部使用）。
     * @return 缓存的指标数据
     */
    public AtomicReference<String> getCachedMetrics() {
        return cachedMetrics;
    }

    /**
     * 获取当前缓存的构建指标数据。
     * @return 构建指标 JSON 字符串
     */
    @Override
    public String getMetrics() {
        return cachedMetrics.get();
    }

    /**
     * 采集 Jenkins 构建指标，并更新缓存。
     */
    @Override
    public void collectMetrics() {
        logger.info("开始采集构建指标");
        try (StringWriter buffer = new StringWriter()) {
            final long start = System.currentTimeMillis();
            BuildMetricsVO buildMetricsVO = new JobCollector().collect();
            logger.info("构建指标采集完成，耗时：{} ms", System.currentTimeMillis() - start);
            buffer.write(getJsonStr(buildMetricsVO));
            cachedMetrics.set(buffer.toString());
        } catch (IOException e) {
            logger.error("采集构建指标时发生IO异常", e);
        }
    }

    /**
     * 将对象序列化为 JSON 字符串。
     * @param object 需要序列化的对象
     * @return JSON 字符串
     */
    public static String getJsonStr(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            logger.error("JSON序列化失败", e);
            return null;
        }
    }

}
