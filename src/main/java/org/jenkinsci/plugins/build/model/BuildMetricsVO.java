package org.jenkinsci.plugins.build.model;

/**
 * Jenkins 构建指标数据对象。
 * <p>
 * 用于封装采集到的 Jenkins Job 及其构建历史的详细信息，
 * 便于序列化、传输和展示。
 * </p>
 * @author mingsha
 * @date 2025-07-10
 */
public class BuildMetricsVO {

    /** 指标命名空间（用于区分不同来源或环境） */
    private String             namespace;
    /** 所有 Job 的构建指标列表 */
    private java.util.List<JobMetricsVO> jobs;

    /**
     * 创建 BuildMetricsVO 实例。
     * @return 新实例
     */
    public static BuildMetricsVO newInstance() {
        return new BuildMetricsVO();
    }

    /**
     * 获取命名空间。
     * @return 命名空间
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * 设置命名空间。
     * @param namespace 命名空间
     * @return 当前对象
     */
    public BuildMetricsVO setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    /**
     * 获取 Job 指标列表。
     * @return Job 指标列表
     */
    public java.util.List<JobMetricsVO> getJobs() {
        return jobs;
    }

    /**
     * 设置 Job 指标列表。
     * @param jobs Job 指标列表
     * @return 当前对象
     */
    public BuildMetricsVO setJobs(java.util.List<JobMetricsVO> jobs) {
        this.jobs = jobs;
        return this;
    }

    /**
     * 单个 Job 的构建指标信息。
     */
    public static class JobMetricsVO {

        /** Job 名称 */
        private String             jobName;
        /** Job 的所有构建历史 */
        private java.util.List<BuildVO> builds;

        public static JobMetricsVO newInstance() {
            return new JobMetricsVO();
        }

        /**
         * 获取 Job 名称。
         * @return Job 名称
         */
        public String getJobName() {
            return jobName;
        }
        /**
         * 设置 Job 名称。
         * @param jobName Job 名称
         * @return 当前对象
         */
        public JobMetricsVO setJobName(String jobName) {
            this.jobName = jobName;
            return this;
        }
        /**
         * 获取构建列表。
         * @return 构建列表
         */
        public java.util.List<BuildVO> getBuilds() {
            return builds;
        }
        /**
         * 设置构建列表。
         * @param builds 构建列表
         * @return 当前对象
         */
        public JobMetricsVO setBuilds(java.util.List<BuildVO> builds) {
            this.builds = builds;
            return this;
        }
    }

    /**
     * 单次构建的详细信息。
     */
    public static class BuildVO {

        /** 队列ID */
        private String queueId;
        /** Job ID */
        private String jobId;
        /** 构建开始时间（毫秒） */
        private String startMillis;
        /** 构建耗时（毫秒） */
        private String duration;
        /** 构建结果 */
        private String result;
        /** 触发人 */
        private String runner;

        public static BuildVO newInstance() {
            return new BuildVO();
        }

        /**
         * 获取队列ID。
         * @return 队列ID
         */
        public String getQueueId() {
            return queueId;
        }
        /**
         * 设置队列ID。
         * @param queueId 队列ID
         * @return 当前对象
         */
        public BuildVO setQueueId(String queueId) {
            this.queueId = queueId;
            return this;
        }
        /**
         * 获取 Job ID。
         * @return Job ID
         */
        public String getJobId() {
            return jobId;
        }
        /**
         * 设置 Job ID。
         * @param jobId Job ID
         * @return 当前对象
         */
        public BuildVO setJobId(String jobId) {
            this.jobId = jobId;
            return this;
        }
        /**
         * 获取构建开始时间。
         * @return 开始时间（毫秒）
         */
        public String getStartMillis() {
            return startMillis;
        }
        /**
         * 设置构建开始时间。
         * @param startMillis 开始时间（毫秒）
         * @return 当前对象
         */
        public BuildVO setStartMillis(String startMillis) {
            this.startMillis = startMillis;
            return this;
        }
        /**
         * 获取构建耗时。
         * @return 耗时（毫秒）
         */
        public String getDuration() {
            return duration;
        }
        /**
         * 设置构建耗时。
         * @param duration 耗时（毫秒）
         * @return 当前对象
         */
        public BuildVO setDuration(String duration) {
            this.duration = duration;
            return this;
        }
        /**
         * 获取构建结果。
         * @return 构建结果
         */
        public String getResult() {
            return result;
        }
        /**
         * 设置构建结果。
         * @param result 构建结果
         * @return 当前对象
         */
        public BuildVO setResult(String result) {
            this.result = result;
            return this;
        }
        /**
         * 获取触发人。
         * @return 触发人
         */
        public String getRunner() {
            return runner;
        }
        /**
         * 设置触发人。
         * @param runner 触发人
         * @return 当前对象
         */
        public BuildVO setRunner(String runner) {
            this.runner = runner;
            return this;
        }
    }
}
