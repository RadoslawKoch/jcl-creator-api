package com.pallas.jclcreator.service;

import java.util.List;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jclcreator.entities.JobEntity;

public interface DataAccessServiceDefinition {

    JobEntity getJobIfPresent(String jobName) 
	throws JclElementNotFoundException;
    void save(JobEntity job);
    JobEntity getJobByName(String name);
    List<JobEntity> getAll();
    void delete(JobEntity job);
    void delete(String jobName);
    List<JobEntity> getAllByNameLike(String name);
    List<JobEntity> getAllByAuthorName(String lname);
}
