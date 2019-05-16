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
import com.pallas.jcl.creator.datamodel.interfaces.StepDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.StepParamDefinition;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jcl.creator.job.steps.params.StepParamName;
import com.pallas.jclcreator.dto.CreateStepDto;
import com.pallas.jclcreator.dto.CreateStepParamDto;
import com.pallas.jclcreator.errors.ErrorHandler;
import com.pallas.jclcreator.service.StepServiceDefinition;

@RestController
@CrossOrigin
@RequestMapping("/jobs/{jobName}/steps")
public class StepController 
	extends ErrorHandler {
	
    @Autowired
    private StepServiceDefinition service;
	
    @RequestMapping(method=RequestMethod.DELETE,value="/{stepName}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStep(
		@PathVariable String jobName,
		@PathVariable String stepName) 
		throws JclElementNotFoundException {
	this.service.deleteStep(jobName, stepName);	
    }

    @RequestMapping(method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addStep(
		@PathVariable String jobName,
		@RequestBody @Validated CreateStepDto step)
		throws DuplicateJclElementException, 
		InvalidNameException, 
		JclElementNotFoundException {
	this.service.addStep(jobName, step);		
    }
	
    @RequestMapping(method=RequestMethod.POST,value="/before/{stepName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addStepBeforeAnother(
		@PathVariable String jobName,
		@PathVariable String stepName,
		@RequestBody @Validated CreateStepDto step)
		throws DuplicateJclElementException, 
		InvalidNameException, 
		JclElementNotFoundException {
	this.service.addStepBefore(jobName, stepName, step);		
    }

    @RequestMapping(method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<StepDefinition> getSteps(
		@PathVariable String jobName) 
		throws JclElementNotFoundException {
	return this.service.getSteps(jobName);
    }

    @RequestMapping(method=RequestMethod.GET,value="/{stepName}")
    @ResponseStatus(HttpStatus.OK)
    public StepDefinition getStep(
		@PathVariable String jobName,
		@PathVariable String stepName) 
		throws JclElementNotFoundException {
	return this.service.getStep(jobName, stepName);
    }
		
    @RequestMapping(method=RequestMethod.DELETE,value="/{stepName}/comment")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStepComment(
		@PathVariable String jobName,
		@PathVariable String stepName) 
		throws JclElementNotFoundException {
	this.service.deleteStepComment(jobName, stepName);		
    }

    @RequestMapping(method=RequestMethod.POST,value="/{stepName}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void addStepComment(
		@PathVariable String jobName,
		@PathVariable String stepName, 
		@RequestBody String comment) 
		throws JclElementNotFoundException {
	this.service.addStepComment(jobName, stepName, comment);		
    }

    @RequestMapping(method=RequestMethod.GET,value="/{stepName}/comment")
    @ResponseStatus(HttpStatus.OK)
    public String getStepComment(
		@PathVariable String jobName,
		@PathVariable String stepName) 
		throws JclElementNotFoundException {
	return this.service.getStepComment(jobName, stepName);
    }

    @RequestMapping(method=RequestMethod.POST,value="/{stepName}/params")
    @ResponseStatus(HttpStatus.CREATED)
    public void addParamToStep(
		@PathVariable String jobName,
		@PathVariable String stepName,
		@RequestBody @Validated CreateStepParamDto param)
		throws DuplicateJclElementException, 
		InvalidParameterException, 
		JclElementNotFoundException {
	this.service.addParamToStep(jobName, stepName, param);		
    }

    @RequestMapping(method=RequestMethod.GET,value="/{stepName}/params/{paramName}")
    @ResponseStatus(HttpStatus.OK)
    public StepParamDefinition getStepParam(
		@PathVariable String jobName,
		@PathVariable String stepName,
		@PathVariable StepParamName paramName)
		throws JclElementNotFoundException {
	return this.service.getStepParam(jobName, stepName, paramName);
    }

    @RequestMapping(method=RequestMethod.DELETE,value="/{stepName}/params/{paramName}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStepParam(
		@PathVariable String jobName,
		@PathVariable String stepName,
		@PathVariable StepParamName paramName) 
		throws JclElementNotFoundException {
	this.service.deleteStepParam(jobName, stepName, paramName);		
    }

    @RequestMapping(method=RequestMethod.GET,value="/{stepName}/params")
    @ResponseStatus(HttpStatus.OK)
    public List<StepParamDefinition> getStepParams(
		@PathVariable String jobName,
		@PathVariable String stepName) 
		throws JclElementNotFoundException {
	return this.service.getStepParams(jobName, stepName);
    }
}
