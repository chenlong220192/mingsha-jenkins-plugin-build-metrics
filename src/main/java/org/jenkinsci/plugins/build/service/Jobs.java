package org.jenkinsci.plugins.build.service;

import java.util.List;

import hudson.model.Job;
import jenkins.model.Jenkins;

/**
 * Utility class for accessing Jenkins Jobs.
 * <p>
 * Provides convenient methods for retrieving all jobs from the Jenkins instance.
 * </p>
 *
 * @author mingsha
 * @date 2025-07-10
 */
public class Jobs {

    /**
     * Private constructor - prevents instantiation.
     */
    private Jobs() {}

    /**
     * Retrieves all jobs from the Jenkins instance.
     * <p>
     * Gets all job types including freestyle projects, pipeline projects, etc.
     * </p>
     * @return a list of all jobs, or an empty list if no jobs exist
     */
    public static List<Job> getAllJobs() {
        return Jenkins.get().getAllItems(Job.class);
    }
}
