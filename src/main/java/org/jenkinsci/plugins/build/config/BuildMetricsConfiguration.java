package org.jenkinsci.plugins.build.config;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import jenkins.YesNoMaybe;
import jenkins.model.GlobalConfiguration;
import jenkins.model.Jenkins;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Jenkins 构建指标插件全局配置。
 * <p>
 * 用于管理插件的命名空间、采集周期、采集窗口等全局参数，
 * 支持 Jenkins 全局配置界面动态加载和持久化。
 * </p>
 * @author mingsha
 * @date 2025-07-10
 */
@Extension(dynamicLoadable = YesNoMaybe.NO)
public class BuildMetricsConfiguration extends GlobalConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BuildMetricsConfiguration.class);

    private static final String DEFAULT_NAMESPACE = "default";
    private static final String DEFAULT_ENDPOINT = "build/metrics";
    static final long DEFAULT_COLLECTING_PERIOD_IN_SECONDS = TimeUnit.MINUTES.toSeconds(2);
    static final long DEFAULT_COLLECTING_MINUTES = TimeUnit.MINUTES.toMinutes(60);

    private String urlName = null;
    private String namespace;
    private String additionalPath;
    private Long collectingBuildMetricsMinutes = null;
    private Long collectingBuildMetricsPeriodInSeconds = null;

    /**
     * 构造方法，初始化全局配置。
     */
    public BuildMetricsConfiguration() {
        load();
        setPath(urlName);
        setNamespace(namespace);
        setCollectingBuildMetricsMinutes(collectingBuildMetricsMinutes);
        setCollectingBuildMetricsPeriodInSeconds(collectingBuildMetricsPeriodInSeconds);
    }

    /**
     * 获取插件全局配置实例。
     * @return BuildMetricsConfiguration 实例
     */
    public static BuildMetricsConfiguration get() {
        Descriptor configuration = Jenkins.get().getDescriptor(BuildMetricsConfiguration.class);
        return (BuildMetricsConfiguration) configuration;
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        // 设置基本配置项
        setPath(json.getString("path"));
        setNamespace(json.getString("namespace"));
        setCollectingBuildMetricsPeriodInSeconds(json.getLong("collectingBuildMetricsPeriodInSeconds"));
        setCollectingBuildMetricsMinutes(json.getLong("collectingBuildMetricsMinutes"));

        // 验证并重新设置配置值
        collectingBuildMetricsMinutes = validateCollectingBuildMetricsMinutes(json);
        collectingBuildMetricsPeriodInSeconds = validateCollectingBuildMetricsPeriodInSeconds(json);

        // 保存配置到磁盘
        save();
        return super.configure(req, json);
    }

    /**
     * 获取路径。
     * @return 路径
     */
    public String getPath() {
        return StringUtils.isEmpty(additionalPath) ? urlName : urlName + "" + additionalPath;
    }

    /**
     * 设置路径。
     * @param path 路径
     */
    public void setPath(String path) {
        if (path == null) {
            // 使用默认端点路径
            path = DEFAULT_ENDPOINT;
        } else {
            // 获取当前路径（这里可能有逻辑问题，应该是 path 本身）
            path = getPath();
        }
        // 提取 URL 名称（路径的第一部分）
        urlName = path.split("/")[0];
        // 解析路径各部分
        List<String> pathParts = Arrays.asList(path.split("/"));
        // 构建附加路径（除第一部分外的所有部分）
        additionalPath = (pathParts.size() > 1 ? "/" : "") + StringUtils.join(pathParts.subList(1, pathParts.size()), "/");
        // 保存配置
        save();
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
     */
    public void setNamespace(String namespace) {
        if (namespace == null) {
            // 使用默认命名空间
            namespace = DEFAULT_NAMESPACE;
        }
        this.namespace = namespace;
        // 保存配置
        save();
    }

    /**
     * 获取采集周期（秒）。
     * @return 采集周期（秒）
     */
    public long getCollectingBuildMetricsPeriodInSeconds() {
        return collectingBuildMetricsPeriodInSeconds;
    }

    /**
     * 设置采集周期（秒）。
     * @param collectingMetricsPeriodInSeconds 采集周期（秒）
     */
    public void setCollectingBuildMetricsPeriodInSeconds(Long collectingMetricsPeriodInSeconds) {
        if (collectingMetricsPeriodInSeconds == null) {
            // 使用默认采集周期
            this.collectingBuildMetricsPeriodInSeconds = DEFAULT_COLLECTING_PERIOD_IN_SECONDS;
        } else {
            // 使用用户设置的采集周期
            this.collectingBuildMetricsPeriodInSeconds = collectingMetricsPeriodInSeconds;
        }
        // 保存配置
        save();
    }

    /**
     * 获取采集时间窗口（分钟）。
     * @return 采集时间窗口（分钟）
     */
    public long getCollectingBuildMetricsMinutes() {
        return collectingBuildMetricsMinutes;
    }

    /**
     * 设置采集时间窗口（分钟）。
     * @param collectingBuildMetricsMinutes 采集时间窗口（分钟）
     */
    public void setCollectingBuildMetricsMinutes(Long collectingBuildMetricsMinutes) {
        if (collectingBuildMetricsMinutes == null) {
            // 使用默认采集时间窗口
            this.collectingBuildMetricsMinutes = DEFAULT_COLLECTING_MINUTES;
        } else {
            // 使用用户设置的采集时间窗口
            this.collectingBuildMetricsMinutes = collectingBuildMetricsMinutes;
        }
        // 保存配置
        save();
    }

    /**
     * 获取 URL 名称。
     * @return URL 名称
     */
    public String getUrlName() {
        return urlName;
    }

    /**
     * 获取附加路径。
     * @return 附加路径
     */
    public String getAdditionalPath() {
        return additionalPath;
    }

    /**
     * 校验路径参数。
     * @param value 路径参数
     * @return 校验结果
     */
    public FormValidation doCheckPath(@QueryParameter String value) {
        if (StringUtils.isEmpty(value)) {
            return FormValidation.error(Messages.path_required());
        } else {
            return FormValidation.ok();
        }
    }

    /**
     * 校验采集时间窗口参数。
     * @param json 配置 JSON
     * @return 校验后的采集时间窗口
     * @throws FormException 校验失败时抛出
     */
    private Long validateCollectingBuildMetricsMinutes(JSONObject json) throws FormException {
        try {
            long value = json.getLong("collectingBuildMetricsMinutes");
            if (value > 0) {
                return value;
            }
        } catch (JSONException ignored) {
            // ignore JSONException, will throw FormException below
        }
        throw new FormException("CollectingBuildMetricsMinutes must be a positive integer", "collectingBuildMetricsMinutes");
    }

    /**
     * 校验采集周期参数。
     * @param json 配置 JSON
     * @return 校验后的采集周期
     * @throws FormException 校验失败时抛出
     */
    private Long validateCollectingBuildMetricsPeriodInSeconds(JSONObject json) throws FormException {
        try {
            long value = json.getLong("collectingBuildMetricsPeriodInSeconds");
            if (value > 0) {
                return value;
            }
        } catch (JSONException ignored) {
            // ignore JSONException, will throw FormException below
        }
        throw new FormException("CollectingBuildMetricsPeriodInSeconds must be a positive integer", "collectingBuildMetricsPeriodInSeconds");
    }


}
