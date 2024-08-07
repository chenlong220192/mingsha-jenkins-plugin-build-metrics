package org.jenkinsci.plugins.build.context;

import com.google.inject.AbstractModule;
import hudson.Extension;
import org.jenkinsci.plugins.build.service.DefaultBuildMetrics;
import org.jenkinsci.plugins.build.service.BuildMetrics;

/**
 * @author chenlong
 */
@Extension
public class Context extends AbstractModule {

    @Override
    public void configure() {
        bind(BuildMetrics.class).to(DefaultBuildMetrics.class).in(com.google.inject.Singleton.class);
    }

}
