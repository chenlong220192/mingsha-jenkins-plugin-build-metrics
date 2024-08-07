package org.jenkinsci.plugins.build.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jenkinsci.plugins.build.model.BuildMetricsVO;
import org.jenkinsci.plugins.build.util.ConfigurationUtils;
import org.jenkinsci.plugins.build.util.Jobs;
import org.jenkinsci.plugins.build.util.Runs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hudson.model.Run;

/**
 * @author chenlong
 */
public class JobCollector {

    private static final Logger logger = LoggerFactory.getLogger(JobCollector.class);

    /**
     * @return
     */
    public BuildMetricsVO collect() {

        // namespace
        String namespace = ConfigurationUtils.getNamespace();
        // collectingBuildMetricsMinutes
        Long collectingBuildMetricsMinutes = ConfigurationUtils.getCollectingBuildMetricsMinutes();
        Long tarrgetTime = System.currentTimeMillis() - collectingBuildMetricsMinutes * 60L * 1000L;

        //
        List<BuildMetricsVO.JobMetricsVO> jobMetricsVOList = Jobs.getAllJobs().stream().map(job -> {
            //
            List<BuildMetricsVO.BuildVO> buildVOList = new ArrayList<>();
            // 获取最后一个构建
            Run run = job.getLastBuild();

            if (run == null) {
                return null;
            }

            // target filter
            if (run.getStartTimeInMillis() < tarrgetTime) {
                return null;
            }

            String jobName = Runs.getJobNameText(run);

            while (run != null) {
                if (run.isBuilding()) {
                    continue;
                }
                buildVOList.add(
                        BuildMetricsVO.BuildVO.newInstance()
                                .setQueueId(String.valueOf(run.getQueueId()))
                                .setJobId(run.getId())
                                .setStartMillis(String.valueOf(run.getStartTimeInMillis()))
                                .setDuration(String.valueOf(run.getDuration()))
                                .setResult(Runs.getResultText(run))
                                .setRunner(Runs.getRunnerText(run))
                );
                // 获取上一个构建
                run = run.getPreviousBuild();
            }

            return BuildMetricsVO.JobMetricsVO.newInstance()
                    .setJobName(jobName)
                    .setBuilds(buildVOList);
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return BuildMetricsVO.newInstance()
                .setNamespace(namespace)
                .setJobs(jobMetricsVOList);

    }

}
