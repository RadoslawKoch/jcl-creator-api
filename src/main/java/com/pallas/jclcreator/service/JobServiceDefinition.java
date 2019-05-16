package com.pallas.jclcreator.service;

import java.util.List;
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

public interface JobServiceDefinition {

    void addNewJob(CreateJobDto dto) 
		throws InvalidNameException, 
		DuplicateJclElementException;
	
    JobDefinition getJob(String name) 
                throws JclElementNotFoundException, 
		InvalidNameException;
	
    void deleteJob(String name)
		throws JclElementNotFoundException;
	
    List<JobDefinition> getAllJobs();
	
    void addParamToJobCard(String jobName,CreateKeywordParamDto param) 
		throws DuplicateJclElementException, 
		InvalidParameterException,
		JclElementNotFoundException,
		InvalidNameException;	
	
    void addParamToJobCardBefore(String jobName,KeywordParamName before,CreateKeywordParamDto param) 
		throws DuplicateJclElementException, 
		InvalidParameterException, 
		JclElementNotFoundException;	
	
    List<KeywordParamDefinition> getKeywordParams(String jobName) 
		throws JclElementNotFoundException;
	
    JobCardDefinition getJobCard(String jobName) 	
		throws JclElementNotFoundException;
	
    KeywordParamDefinition getKeywordParam(String jobName, KeywordParamName name) 
                throws JclElementNotFoundException;
	
    Author getJobAutor(String jobName) 
		throws JclElementNotFoundException;
	
    void deleteKeyWordParam(String jobName,KeywordParamName name) 
		throws JclElementNotFoundException;
	
    String getJobComment(String jobName) 
		throws JclElementNotFoundException;
	
    String getAccountInfo(String jobName) 
		throws JclElementNotFoundException;
    String getJobStringValue(String jobName)
		throws JclElementNotFoundException, 
		InvalidNameException, 
		InvalidParameterException, 
		DuplicateJclElementException;

    List<JobDefinition> getAllJobsByNameLike(String name);

    List<JobDefinition> getAllJobsAuthorName(String name);

    void updateJobCardInfo(UpdateJobCardDto dto, String jobName) throws JclElementNotFoundException;
}
