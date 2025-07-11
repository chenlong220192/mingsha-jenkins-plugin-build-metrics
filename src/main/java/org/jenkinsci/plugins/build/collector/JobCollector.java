package org.jenkinsci.plugins.build.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jenkinsci.plugins.build.model.BuildMetricsVO;
import org.jenkinsci.plugins.build.util.ConfigurationUtils;
import org.jenkinsci.plugins.build.service.Jobs;
import org.jenkinsci.plugins.build.service.Runs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hudson.model.Run;

/**
 * Jenkins Job 构建信息采集器。
 * <p>
 * 负责遍历所有 Jenkins Job，采集其构建历史、状态、参数等信息，
 * 并封装为 BuildMetricsVO 结构，供指标服务使用。
 * </p>
 * @author mingsha
 * @date 2025-07-10
 */
public class JobCollector {

    private static final Logger logger = LoggerFactory.getLogger(JobCollector.class);

    /**
     * 采集所有 Jenkins Job 的构建信息，封装为 BuildMetricsVO。
     * @return 构建指标数据对象
     */
    public BuildMetricsVO collect() {
        // 获取配置的命名空间
        String namespace = ConfigurationUtils.getNamespace();
        // 获取配置的采集时间窗口（分钟）
        Long collectingBuildMetricsMinutes = ConfigurationUtils.getCollectingBuildMetricsMinutes();
        // 计算目标时间点：当前时间减去配置的时间窗口
        Long targetTime = System.currentTimeMillis() - collectingBuildMetricsMinutes * 60L * 1000L;

        // 遍历所有 Jenkins Job，采集构建信息
        List<BuildMetricsVO.JobMetricsVO> jobMetricsVOList = Jobs.getAllJobs().stream().map(job -> {
            // 存储当前 Job 的所有构建记录
            List<BuildMetricsVO.BuildVO> buildList = new ArrayList<>();
            // 获取当前 Job 的最后一个构建
            Run lastBuild = job.getLastBuild();

            // 如果 Job 从未构建过，跳过
            if (lastBuild == null) {
                return null;
            }

            // 如果最后一个构建的时间早于目标时间，跳过（不在采集时间窗口内）
            if (lastBuild.getStartTimeInMillis() < targetTime) {
                return null;
            }

            // 获取 Job 名称
            String jobName = Runs.getJobNameText(lastBuild);

            // 遍历当前 Job 的所有构建历史
            Run currentBuild = lastBuild;
            while (currentBuild != null) {
                // 跳过正在构建中的记录
                if (currentBuild.isBuilding()) {
                    // 获取上一个构建记录
                    currentBuild = currentBuild.getPreviousBuild();
                    continue;
                }
                
                // 构建 BuildVO 对象并添加到列表
                buildList.add(
                        BuildMetricsVO.BuildVO.newInstance()
                                .setQueueId(String.valueOf(currentBuild.getQueueId()))
                                .setJobId(currentBuild.getId())
                                .setStartMillis(String.valueOf(currentBuild.getStartTimeInMillis()))
                                .setDuration(String.valueOf(currentBuild.getDuration()))
                                .setResult(Runs.getResultText(currentBuild))
                                .setRunner(Runs.getRunnerText(currentBuild))
                );
                // 获取上一个构建记录
                currentBuild = currentBuild.getPreviousBuild();
            }

            // 创建 JobMetricsVO 对象
            return BuildMetricsVO.JobMetricsVO.newInstance()
                    .setJobName(jobName)
                    .setBuilds(buildList);
        }).filter(Objects::nonNull).collect(Collectors.toList());

        // 创建并返回最终的构建指标数据对象
        return BuildMetricsVO.newInstance()
                .setNamespace(namespace)
                .setJobs(jobMetricsVOList);
    }

}
