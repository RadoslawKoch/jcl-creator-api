package com.pallas.jclcreator.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.pallas.jcl.creator.datamodel.interfaces.Author;
import com.pallas.jcl.creator.datamodel.interfaces.JobCardDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.JobDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.KeywordParamDefinition;
import com.pallas.jcl.creator.job.card.params.KeywordParamName;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jclcreator.dto.CreateJobDto;
import com.pallas.jclcreator.dto.CreateKeywordParamDto;
import com.pallas.jclcreator.dto.UpdateJobCardDto;
import com.pallas.jclcreator.errors.ErrorHandler;
import com.pallas.jclcreator.service.JobServiceDefinition;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/jobs")
public class JobController 
	extends ErrorHandler{
	
    @Autowired
    private JobServiceDefinition service;
		
    @RequestMapping(method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<JobDefinition> getAllJobs(Principal principal){
        return this.service.getAllJobs();
    }
	
    @RequestMapping(method=RequestMethod.GET,value="/search/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<JobDefinition> getAllJobsByNameLike(@PathVariable String name){
	return this.service.getAllJobsByNameLike(name);	
    }
	
    @RequestMapping(method=RequestMethod.GET,value="/search/author/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<JobDefinition> getAllJobsByAuthorName(@PathVariable String name){
	return this.service.getAllJobsAuthorName(name);
    }
	
    @RequestMapping(method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewJob(
		@RequestBody @Validated CreateJobDto dto) 
		throws InvalidNameException, 
		DuplicateJclElementException {
	this.service.addNewJob(dto);	
    }
	
    @RequestMapping(method=RequestMethod.GET,value="/{jobName}")
    @ResponseStatus(HttpStatus.OK)
    public JobDefinition getJob(
		@PathVariable String jobName) 
		throws JclElementNotFoundException, 
		InvalidNameException {
	JobDefinition def = this.service.getJob(jobName);
	return def;
    }
	
    @RequestMapping(method=RequestMethod.GET,value="/{jobName}/txt")
    @ResponseStatus(HttpStatus.OK)
    public String getJobInTxt(
                @PathVariable String jobName) 
		throws JclElementNotFoundException, 
		InvalidNameException, 
		InvalidParameterException, 
		DuplicateJclElementException {
	return this.service.getJobStringValue(jobName);
    }
	
    @RequestMapping(method=RequestMethod.DELETE,value="/{jobName}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteJob(
		@PathVariable String jobName) 
		throws JclElementNotFoundException {
        this.service.deleteJob(jobName);
    }

    @RequestMapping(method=RequestMethod.POST,value="/{jobName}/card/params")
    @ResponseStatus(HttpStatus.CREATED)
    public void addParamToJobCard(
		@PathVariable String jobName,
		@RequestBody @Validated CreateKeywordParamDto param)
		throws DuplicateJclElementException, 
		InvalidParameterException, 
		JclElementNotFoundException, 
		InvalidNameException {
	this.service.addParamToJobCard(jobName, param);	
    }
	
    @RequestMapping(method=RequestMethod.POST,value="/{jobName}/card/params/before/{beforeName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addParamToJobCardBefore(
		@PathVariable String jobName,
		@PathVariable KeywordParamName beforeName,
		@RequestBody @Validated CreateKeywordParamDto param)
		throws DuplicateJclElementException, 
		InvalidParameterException, 
		JclElementNotFoundException {
	this.service.addParamToJobCardBefore(jobName, beforeName, param);
    }

    @RequestMapping(method=RequestMethod.GET,value="/{jobName}/card/params")
    @ResponseStatus(HttpStatus.OK)
    public List<KeywordParamDefinition> getKeywordParams(
		@PathVariable String jobName) 
		throws JclElementNotFoundException {
	return this.service.getKeywordParams(jobName);
    }

    @RequestMapping(method=RequestMethod.GET,value="/{jobName}/card")
    @ResponseStatus(HttpStatus.OK)
    public JobCardDefinition getJobCard(
		@PathVariable String jobName) 
		throws JclElementNotFoundException {
	return this.service.getJobCard(jobName);
    }

    @RequestMapping(method=RequestMethod.GET,value="/{jobName}/card/params/{paramName}")
    @ResponseStatus(HttpStatus.OK)
    public KeywordParamDefinition getKeywordParams(
		@PathVariable String jobName,
		@PathVariable KeywordParamName paramName) 
		throws JclElementNotFoundException {
	return this.service.getKeywordParam(jobName, paramName);
    }

    @RequestMapping(method=RequestMethod.GET,value="/{jobName}/autor")
    @ResponseStatus(HttpStatus.OK)
    public Author getJobAutor(
		@PathVariable String jobName) 
		throws JclElementNotFoundException {
	return this.service.getJobAutor(jobName);
    }

    @RequestMapping(method=RequestMethod.DELETE,value="/{jobName}/card/params/{paramName}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteKeyWordParam(
		@PathVariable String jobName,
		@PathVariable KeywordParamName paramName) 
		throws JclElementNotFoundException {
	this.service.deleteKeyWordParam(jobName,paramName);	
    }

    @RequestMapping(method=RequestMethod.GET,value="/{jobName}/comment")
    @ResponseStatus(HttpStatus.OK)
    public String getJobComment(
		@PathVariable String jobName) 
		throws JclElementNotFoundException {
	return this.service.getJobComment(jobName);
    }

    @RequestMapping(method=RequestMethod.GET,value="/{jobName}/account")
    @ResponseStatus(HttpStatus.OK)
    public String getAccountInfo(
		@PathVariable String jobName) 
		throws JclElementNotFoundException {
	return this.service.getAccountInfo(jobName);
    }
	
    @RequestMapping(method=RequestMethod.PUT, value="/{jobName}/card")
    @ResponseStatus(HttpStatus.OK)
    public void updateJobCard(
		@PathVariable String jobName, 
		@RequestBody @Validated UpdateJobCardDto dto) 
		throws JclElementNotFoundException {
	this.service.updateJobCardInfo(dto, jobName);
    }
}
