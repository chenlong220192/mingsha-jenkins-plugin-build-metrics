package org.jenkinsci.plugins.build.context;

import com.google.inject.AbstractModule;
import hudson.Extension;
import org.jenkinsci.plugins.build.service.impl.DefaultBuildMetrics;
import org.jenkinsci.plugins.build.service.BuildMetrics;

/**
 * Guice dependency injection context configuration.
 * <p>
 * Binds the BuildMetrics interface to its default implementation DefaultBuildMetrics,
 * and injects it as a singleton throughout the plugin.
 * </p>
 *
 * @author mingsha
 * @date 2025-07-10
 */
@Extension
public class Context extends AbstractModule {

    /**
     * Configures dependency injection bindings.
     */
    @Override
    public void configure() {
        bind(BuildMetrics.class).to(DefaultBuildMetrics.class).in(com.google.inject.Singleton.class);
    }
}
