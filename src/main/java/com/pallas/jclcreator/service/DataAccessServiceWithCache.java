package com.pallas.jclcreator.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jclcreator.entities.JobEntity;


@Service
@Primary
public class DataAccessServiceWithCache 
    implements DataAccessServiceDefinition {

    @Autowired
    private DataAccessService baseService;
	
    private final Map<String,JobEntity> pool = new HashMap<>();
	
    @Override
    public JobEntity getJobIfPresent(String jobName) throws JclElementNotFoundException {
	JobEntity job = this.pool.get(jobName);
	if(job==null) {
            job = this.baseService.getJobIfPresent(jobName);
            pool.put(jobName, job);
        }
	return job;
    }

    @Override
    public void save(JobEntity job) {
	this.baseService.save(job);
	if(job.getId()!=null) {
            this.pool.put(job.getId(), job);
	}
    }

    @Override
    public JobEntity getJobByName(String name) {
	JobEntity job = this.pool.get(name);
        if(job==null) {
            job = this.baseService.getJobByName(name);
            if(job==null)
		pool.put(name, job);
            }
        return job;
    }

    @Override
    public List<JobEntity> getAll() {
	return this.baseService.getAll();
    }

    @Override
    public void delete(JobEntity job) {
	this.baseService.delete(job);	
	this.pool.remove(job.getId());
    }

    @Override
    public void delete(String jobName) {
        this.baseService.delete(jobName);	
	this.pool.remove(jobName);
    }

    @Override
    public List<JobEntity> getAllByNameLike(String name) {
	return this.baseService.getAllByNameLike(name);
    }

    @Override
    public List<JobEntity> getAllByAuthorName(String lname) {
        return this.baseService.getAllByAuthorName(lname);
    }
	
    @Scheduled(fixedDelay=300_000)
    private void cleanCache() {	
	if(this.pool.size()>50) {
            this.pool.clear();	
	}
    }
}
