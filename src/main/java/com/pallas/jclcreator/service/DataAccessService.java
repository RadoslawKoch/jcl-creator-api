package com.pallas.jclcreator.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jclcreator.entities.JobEntity;
import com.pallas.jclcreator.repos.JobEntityRepository;

@Service
public class DataAccessService 
    implements DataAccessServiceDefinition{

    @Autowired
    private JobEntityRepository repo;
		
    @Override
    public JobEntity getJobIfPresent(String jobName) 
		throws JclElementNotFoundException {
        JobEntity ent = this.repo.getJobByName(jobName);
	if(ent==null)
		throw new JclElementNotFoundException(jobName);
	return ent;
    }
	
    @Override
    public void save(JobEntity job) {
	this.repo.save(job);
    }
	
    @Override 
    public JobEntity getJobByName(String name) {
	return this.repo.getJobByName(name);
    }

    @Override
    public List<JobEntity> getAll(){
	return this.repo.findAll();
    }
	
    @Override
    public List<JobEntity> getAllByNameLike(String name){
	return this.repo.findByNameLike(name);
    }
	
    @Override
    public List<JobEntity> getAllByAuthorName(String lname){
	return this.repo.findByJobCardAuthorLnameLike(lname);
    }

    @Override
    public void delete(JobEntity job) {
	this.repo.delete(job);	
    }

    @Override
    public void delete(String jobName) {
	this.repo.deleteById(jobName);		
    }	
}
