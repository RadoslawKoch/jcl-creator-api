package com.pallas.jclcreator.builders;

import com.pallas.jcl.creator.builders.JobBuilder;
import com.pallas.jcl.creator.builders.JobBuilderInterface;
import com.pallas.jcl.creator.datamodel.interfaces.Author;
import com.pallas.jcl.creator.datamodel.interfaces.JobDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.KeywordParamDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.StepDefinition;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jclcreator.entities.JobEntity;

public class JobEntityBuilder implements JobBuilderInterface{
	
    private final JobBuilderInterface base = new JobBuilder();

    @Override
    public JobBuilderInterface setJobName(String jobName) {
	this.base.setJobName(jobName);
	return this;
    }

    @Override
    public JobBuilderInterface setAuthor(Author author) {
        this.base.setAuthor(author);
      	return this;
    }

    @Override
    public JobBuilderInterface setAccountInfo(String accountInfo) {
	this.base.setAccountInfo(accountInfo);
	return this;
    }

    @Override
    public JobBuilderInterface setJobDescription(String jobDescription) {
	this.base.setJobDescription(jobDescription);
	return this;
    }

    @Override
    public JobBuilderInterface addStep(StepDefinition step) throws DuplicateJclElementException {
	this.base.addStep(step);
	return this;
    }

    @Override
    public JobBuilderInterface addKeywordParam(KeywordParamDefinition kwd) throws DuplicateJclElementException {
	this.base.addKeywordParam(kwd);
	return this;
    }

    @Override
    public JobDefinition build() throws InvalidNameException, DuplicateJclElementException {
	return new JobEntity(this.base.build().getJobCard());
    }
}
