package com.pallas.jclcreator.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pallas.jcl.creator.builders.JobBuilderInterface;
import com.pallas.jcl.creator.datamodel.interfaces.Author;
import com.pallas.jcl.creator.datamodel.interfaces.JobCardDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.JobDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.KeywordParamDefinition;
import com.pallas.jcl.creator.job.card.params.KeywordParam;
import com.pallas.jcl.creator.job.card.params.KeywordParamName;
import com.pallas.jcl.creator.job.card.params.Programmer;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jclcreator.builders.JobEntityBuilder;
import com.pallas.jclcreator.dto.CreateJobDto;
import com.pallas.jclcreator.dto.CreateKeywordParamDto;
import com.pallas.jclcreator.dto.UpdateJobCardDto;
import com.pallas.jclcreator.entities.JobEntity;
import com.pallas.jclcreator.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;


@Service
public class JobService 
    implements JobServiceDefinition {
	
    @Autowired
    private DataAccessServiceDefinition dataService;
	
    @Override
    public void addNewJob(CreateJobDto dto) 
	throws InvalidNameException, 
	DuplicateJclElementException {
	if(this.dataService.getJobByName(dto.getName())!=null)
            throw new DuplicateJclElementException(dto.getName());
		JobBuilderInterface job = new JobEntityBuilder()
				.setJobName(dto.getName())
				.setAccountInfo(dto.getAccountInfo())
				.setJobDescription(dto.getComment());
                User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user.getAuthor()!=null) {
			job.setAuthor(user.getAuthor());
	}
	this.dataService.save((JobEntity)job.build());		
    }

    @Override
    public JobDefinition getJob(String name) 
		throws JclElementNotFoundException, 
		InvalidNameException {
	return this.dataService.getJobIfPresent(name);
    }

    @Override
    public void deleteJob(String name) 
	throws JclElementNotFoundException {
	JobEntity job = this.dataService.getJobIfPresent(name);
	this.dataService.delete(job);		
    }

    @Override
    public List<JobDefinition> getAllJobs() {
	List<JobDefinition> tmp = new ArrayList<>();
	this.dataService.getAll().forEach(x->tmp.add(x));
	return tmp;
    }
	
    @Override
    public List<JobDefinition> getAllJobsByNameLike(String name) {
	List<JobDefinition> tmp = new ArrayList<>();
	this.dataService.getAllByNameLike(name).forEach(x->tmp.add(x));
	return tmp;
    }
	
    @Override
    public List<JobDefinition> getAllJobsAuthorName(String name) {
	List<JobDefinition> tmp = new ArrayList<>();
	this.dataService.getAllByAuthorName(name).forEach(x->tmp.add(x));
	return tmp;
    }

    @Override
    public void addParamToJobCard(String jobName, CreateKeywordParamDto param) 
		throws DuplicateJclElementException,
		InvalidParameterException, 
		JclElementNotFoundException, 
		InvalidNameException {
	JobEntity job = this.dataService.getJobIfPresent(jobName);
	job.getJobCard().add(new KeywordParam(param.getName(),param.getValue()));
	this.dataService.save(job);		
    }

    @Override
    public void addParamToJobCardBefore(String jobName, KeywordParamName before, CreateKeywordParamDto param)
		throws DuplicateJclElementException, 
		InvalidParameterException, 
		JclElementNotFoundException {
        JobEntity job = this.dataService.getJobIfPresent(jobName);
	KeywordParamDefinition tmp = new KeywordParam(param.getName(),param.getValue());
	List<KeywordParamDefinition> list = job.getJobCard().getElements();
	if(list.contains(tmp))
		throw new DuplicateJclElementException(tmp.getName().toString());
	int index = list.indexOf(job.getJobCard().getById(before));
	list.add(index,tmp);
	this.dataService.save(job);		
    }

    @Override
    public List<KeywordParamDefinition> getKeywordParams(String jobName) 
		throws JclElementNotFoundException {
	JobEntity job = this.dataService.getJobIfPresent(jobName);
	return job.getJobCard().getElements();
    }

    @Override
    public JobCardDefinition getJobCard(String jobName) 
		throws JclElementNotFoundException {
	return this.dataService.getJobIfPresent(jobName).getJobCard();
    }

    @Override
    public KeywordParamDefinition getKeywordParam(String jobName, KeywordParamName name) 
		throws JclElementNotFoundException {
	return this.dataService.getJobIfPresent(jobName).getJobCard().getById(name);
    }

    @Override
    public Author getJobAutor(String jobName) 
		throws JclElementNotFoundException {
        return this.dataService.getJobIfPresent(jobName).getJobCard().getAuthor();
    }

    @Override
    public void deleteKeyWordParam(String jobName, KeywordParamName name) 
		throws JclElementNotFoundException {
	JobEntity tmp = this.dataService.getJobIfPresent(jobName);
	tmp.getJobCard().delete(this.getKeywordIfPresent(jobName,name));
	this.dataService.save(tmp);	
    }

    private KeywordParamDefinition getKeywordIfPresent(String jobName, KeywordParamName name) 
		throws JclElementNotFoundException {		
	return this.dataService.getJobIfPresent(jobName).getJobCard().getById(name);
    }

    @Override
    public String getJobComment(String jobName) 
		throws JclElementNotFoundException {
	return this.dataService.getJobIfPresent(jobName).getJobCard().getJobDesc();
    }

    @Override
    public String getAccountInfo(String jobName) 
		throws JclElementNotFoundException {
        return this.dataService.getJobIfPresent(jobName).getJobCard().getAccountInfo();
    }

    @Override
    public String getJobStringValue(String jobName) 
		throws JclElementNotFoundException, 
		InvalidNameException,
		InvalidParameterException, 
		DuplicateJclElementException {
	return this.dataService.getJobIfPresent(jobName).toString();
    }
	
    @Override
    public void updateJobCardInfo(UpdateJobCardDto dto, String jobName) throws JclElementNotFoundException {
	JobEntity job = this.dataService.getJobIfPresent(jobName);
	if(dto.getLastName()==null) {
		job.getJobCard().setAuthor(null);
	}else {
		job.getJobCard().setAuthor(new Programmer("",dto.getLastName()));
	}
	job.getJobCard().setJobDesc(dto.getComment());
	job.getJobCard().setAccountInfo(dto.getAccountInfo());
	this.dataService.save(job);	
    }	
}
