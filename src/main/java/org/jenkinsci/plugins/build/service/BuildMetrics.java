package org.jenkinsci.plugins.build.service;

/**
 * Jenkins 构建指标采集服务接口。
 * <p>
 * 用于定义采集 Jenkins 任务/构建相关指标的标准方法。
 * 实现类需负责具体的采集与数据获取逻辑。
 * </p>
 * @author mingsha
 * @date 2025-07-10
 */
public interface BuildMetrics {

    /**
     * 获取当前采集到的构建指标数据（如 JSON 字符串）。
     * @return 构建指标数据
     */
    String getMetrics();

    /**
     * 触发采集 Jenkins 构建指标的操作。
     */
    void collectMetrics();

}
