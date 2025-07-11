package org.jenkinsci.plugins.build.util;

import org.jenkinsci.plugins.build.config.BuildMetricsConfiguration;

/**
 * Jenkins 构建指标配置工具类。
 * <p>
 * 提供获取全局配置参数的便捷方法。
 * </p>
 * @author mingsha
 * @date 2025-07-10
 */
public class ConfigurationUtils {

    /**
     * 工具类构造方法，禁止实例化。
     */
    private ConfigurationUtils() {}

    /**
     * 获取当前命名空间。
     * <p>
     * 从全局配置中获取插件使用的命名空间，
     * 用于区分不同环境或来源的指标数据。
     * </p>
     * @return 命名空间字符串
     */
    public static String getNamespace() {
        return BuildMetricsConfiguration.get().getNamespace();
    }

    /**
     * 获取采集构建指标的时间窗口（分钟）。
     * <p>
     * 从全局配置中获取采集时间窗口，
     * 用于过滤只采集指定时间范围内的构建数据。
     * </p>
     * @return 时间窗口（分钟）
     */
    public static Long getCollectingBuildMetricsMinutes() {
        return BuildMetricsConfiguration.get().getCollectingBuildMetricsMinutes();
    }

}
