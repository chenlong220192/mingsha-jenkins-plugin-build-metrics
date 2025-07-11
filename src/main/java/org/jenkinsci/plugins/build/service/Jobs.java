package org.jenkinsci.plugins.build.service;

import java.util.List;

import hudson.model.Job;
import jenkins.model.Jenkins;

/**
 * Jenkins Job 工具类。
 * <p>
 * 提供获取 Jenkins 实例下所有 Job 的便捷方法。
 * </p>
 * @author mingsha
 * @date 2025-07-10
 */
public class Jobs {

    /**
     * 工具类构造方法，禁止实例化。
     */
    private Jobs() {}

    /**
     * 获取 Jenkins 实例下的所有 Job。
     * <p>
     * 通过 Jenkins API 获取当前实例中所有类型的 Job，
     * 包括自由风格项目、流水线项目等。
     * </p>
     * @return Job 列表，如果没有 Job 返回空列表
     */
    public static List<Job> getAllJobs() {
        return Jenkins.get().getAllItems(Job.class);
    }

}
