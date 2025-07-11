package org.jenkinsci.plugins.build.context;

import com.google.inject.AbstractModule;
import hudson.Extension;
import org.jenkinsci.plugins.build.service.impl.DefaultBuildMetrics;
import org.jenkinsci.plugins.build.service.BuildMetrics;

/**
 * Guice 依赖注入上下文配置。
 * <p>
 * 负责将 BuildMetrics 接口绑定到默认实现 DefaultBuildMetrics，
 * 并以单例方式注入到插件各处。
 * </p>
 * @author mingsha
 * @date 2025-07-10
 */
@Extension
public class Context extends AbstractModule {

    /**
     * 配置依赖注入绑定关系。
     */
    @Override
    public void configure() {
        bind(BuildMetrics.class).to(DefaultBuildMetrics.class).in(com.google.inject.Singleton.class);
    }

}
