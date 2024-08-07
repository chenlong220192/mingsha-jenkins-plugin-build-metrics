package org.jenkinsci.plugins.build.service;

/**
 * @author chenlong
 */
public interface BuildMetrics {

    /**
     *
     * @return
     */
    String getMetrics();

    /**
     *
     */
    void collectMetrics();

}
