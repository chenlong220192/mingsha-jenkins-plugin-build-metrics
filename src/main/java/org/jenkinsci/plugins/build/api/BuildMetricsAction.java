package org.jenkinsci.plugins.build.api;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.UnprotectedRootAction;
import hudson.util.HttpResponses;
import org.jenkinsci.plugins.build.config.BuildMetricsConfiguration;
import org.jenkinsci.plugins.build.service.BuildMetrics;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mingsha
 * @date 2025-07-10
 */
@Extension
public class BuildMetricsAction implements UnprotectedRootAction {

    private BuildMetrics buildMetrics;
    private static final Logger logger = LoggerFactory.getLogger(BuildMetricsAction.class);

    /**
     * 构造方法，初始化 BuildMetricsAction。
     */
    public BuildMetricsAction() {}

    /**
     * 设置 BuildMetrics 实例。
     * @param BuildMetrics 构建指标服务
     */
    @Inject
    public void setBuildMetrics(BuildMetrics BuildMetrics) {
        this.buildMetrics = BuildMetrics;
    }

    /**
     * 获取插件图标文件名（本插件无图标，返回 null）。
     * @return null
     */
    @Override
    public String getIconFileName() {
        return null;
    }

    /**
     * 获取插件显示名称。
     * @return 显示名称
     */
    @Override
    public String getDisplayName() {
        return "Jenkins Build Metrics Collector";
    }

    /**
     * 获取插件 URL 名称。
     * @return URL 名称
     */
    @Override
    public String getUrlName() {
        return BuildMetricsConfiguration.get().getUrlName();
    }

    /**
     * 动态处理 REST 路由。
     * @param request 请求对象
     * @return 响应对象
     */
    public HttpResponse doDynamic(StaplerRequest request) {
        if (request.getRestOfPath().equals(BuildMetricsConfiguration.get().getAdditionalPath())) {
            if (hasAccess()) {
                return buildInfoResponse();
            }
            return HttpResponses.forbidden();
        }
        return HttpResponses.notFound();
    }

    /**
     * 手动触发一次构建指标采集。
     * 访问路径：/your_plugin_url/collect
     * 仅支持 Jenkins 用户（如管理员）通过 API Token 认证访问。
     */
    public HttpResponse doCollect(StaplerRequest request) {
        if (!hasJenkinsPermission()) {
            return HttpResponses.forbidden();
        }
        try {
            buildMetrics.collectMetrics();
            return HttpResponses.ok();
        } catch (Exception e) {
            logger.error("手动采集构建指标失败", e);
            return HttpResponses.error(500, "采集失败: " + e.getMessage());
        }
    }

    /**
     * 校验 Jenkins 权限（如 ADMINISTER）。
     * @return 是否有权限
     */
    private boolean hasJenkinsPermission() {
        try {
            return jenkins.model.Jenkins.get().hasPermission(jenkins.model.Jenkins.ADMINISTER);
        } catch (Exception ignore) {
            // ignore exception, just return false
            return false;
        }
    }

    /**
     * 判断是否有访问权限（当前实现始终返回 true）。
     * @return 是否有权限
     */
    private boolean hasAccess() {
        return true;
    }

    /**
     * 构建指标数据响应。
     * @return HttpResponse
     */
    private HttpResponse buildInfoResponse() {
        return new HttpResponse() {
            @Override
            public void generateResponse(org.kohsuke.stapler.StaplerRequest request, org.kohsuke.stapler.StaplerResponse response, Object node) throws IOException {
                response.setStatus(org.kohsuke.stapler.StaplerResponse.SC_OK);
                response.setContentType("text/plain; version=0.0.4; charset=utf-8");
                response.addHeader("Cache-Control", "must-revalidate,no-cache,no-store");
                response.getWriter().write(buildMetrics.getMetrics());
            }
        };
    }
}
