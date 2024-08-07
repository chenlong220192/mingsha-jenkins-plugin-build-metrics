package org.jenkinsci.plugins.build.util;

import java.util.List;

import hudson.model.Job;
import jenkins.model.Jenkins;

/**
 * @author chenlong
 */
public class Jobs {

    /**
     *
     * @return
     */
    public static List<Job> getAllJobs() {
        return Jenkins.getInstance().getAllItems(Job.class);
    }

}
