package org.jenkinsci.plugins.build.model;

import java.util.List;

/**
 * @author chenlong
 */
public class BuildMetricsVO {

    private String             namespace;
    private List<JobMetricsVO> jobs;


    public static BuildMetricsVO newInstance() {
        return new BuildMetricsVO();
    }

    public String getNamespace() {
        return namespace;
    }

    public BuildMetricsVO setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public List<JobMetricsVO> getJobs() {
        return jobs;
    }

    public BuildMetricsVO setJobs(List<JobMetricsVO> jobs) {
        this.jobs = jobs;
        return this;
    }

    public static class JobMetricsVO {

        private String             jobName;
        private List<BuildVO> builds;

        public static JobMetricsVO newInstance() {
            return new JobMetricsVO();
        }

        public String getJobName() {
            return jobName;
        }

        public JobMetricsVO setJobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public List<BuildVO> getBuilds() {
            return builds;
        }

        public JobMetricsVO setBuilds(List<BuildVO> builds) {
            this.builds = builds;
            return this;
        }
    }

    public static class BuildVO {

        private String queueId;
        private String jobId;
        private String startMillis;
        private String duration;
        private String result;
        private String runner;

        public static BuildVO newInstance() {
            return new BuildVO();
        }

        public String getQueueId() {
            return queueId;
        }

        public BuildVO setQueueId(String queueId) {
            this.queueId = queueId;
            return this;
        }

        public String getJobId() {
            return jobId;
        }

        public BuildVO setJobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public String getStartMillis() {
            return startMillis;
        }

        public BuildVO setStartMillis(String startMillis) {
            this.startMillis = startMillis;
            return this;
        }

        public String getDuration() {
            return duration;
        }

        public BuildVO setDuration(String duration) {
            this.duration = duration;
            return this;
        }

        public String getResult() {
            return result;
        }

        public BuildVO setResult(String result) {
            this.result = result;
            return this;
        }

        public String getRunner() {
            return runner;
        }

        public BuildVO setRunner(String runner) {
            this.runner = runner;
            return this;
        }
    }
}
