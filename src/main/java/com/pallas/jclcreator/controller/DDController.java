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
import com.pallas.jcl.creator.datamodel.interfaces.DDParamDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.DDStatementDefinition;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jcl.creator.job.steps.dd.params.DDParamName;
import com.pallas.jclcreator.dto.CreateDDParamDto;
import com.pallas.jclcreator.dto.CreateDDStatementDto;
import com.pallas.jclcreator.errors.ErrorHandler;
import com.pallas.jclcreator.service.DDServiceDefinition;

@RestController
@CrossOrigin
@RequestMapping("/jobs/{jobName}/steps/{stepName}/dd")
public class DDController 
	extends ErrorHandler {
	
    @Autowired
    private DDServiceDefinition service;
	
    @RequestMapping(method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addDDToStep(
		@PathVariable String jobName,
		@PathVariable  String stepName,		
		@RequestBody @Validated CreateDDStatementDto dd)
		throws DuplicateJclElementException,
		InvalidNameException, 
		JclElementNotFoundException {
	this.service.addDDToStep(jobName, stepName, dd);		
    }
	
    @RequestMapping(method=RequestMethod.POST,value="/before/{beforeName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addDDToStepBefore(
		@PathVariable String jobName,
		@PathVariable  String stepName,
		@PathVariable  String beforeName,
		@RequestBody @Validated CreateDDStatementDto dd)
		throws DuplicateJclElementException, 
		InvalidNameException,
		JclElementNotFoundException {
	this.service.addDDToStepBefore(jobName, stepName, beforeName, dd);		
    }

    @RequestMapping(method=RequestMethod.POST,value="/{ddName}/params")
    @ResponseStatus(HttpStatus.CREATED)
    public void addParamToDD(@PathVariable String jobName,
                @PathVariable String stepName,
		@PathVariable String ddName,
		@RequestBody @Validated CreateDDParamDto par)
		throws DuplicateJclElementException,
		InvalidParameterException, 
		JclElementNotFoundException {
	this.service.addParamToDD(jobName, stepName, ddName, par);		
    }
	
    @RequestMapping(method=RequestMethod.POST,value="/{ddName}/params/before/{beforeName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addParamToDDBefore(
		@PathVariable String jobName,
		@PathVariable String stepName,
		@PathVariable String ddName,
		@PathVariable DDParamName beforeName,
		@RequestBody @Validated CreateDDParamDto par)
		throws DuplicateJclElementException, 
		InvalidParameterException, 
		JclElementNotFoundException {
	this.service.addParamToDDBefore(jobName, stepName, ddName, par, beforeName);
    }

    @RequestMapping(method=RequestMethod.DELETE,value="/{ddName}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDD(
		@PathVariable String jobName,
		@PathVariable String stepName,
		@PathVariable String ddName) 
		throws JclElementNotFoundException {
        this.service.deleteDD(jobName, stepName, ddName);		
    }

    @RequestMapping(method=RequestMethod.DELETE,value="/{ddName}/params/{paramName}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDDParam(
		@PathVariable String jobName,
		@PathVariable String stepName,
		@PathVariable String ddName,
		@PathVariable DDParamName paramName) 
		throws JclElementNotFoundException {
	this.service.deleteDDParam(jobName, stepName, ddName, paramName);	
    }

    @RequestMapping(method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<DDStatementDefinition> getDDs(
		@PathVariable String jobName,
		@PathVariable String stepName) 
		throws JclElementNotFoundException {
	return this.service.getDDs(jobName, stepName);
    }
	
    @RequestMapping(method=RequestMethod.GET,value="/{ddName}")
    @ResponseStatus(HttpStatus.OK)
    public DDStatementDefinition getDD(
		@PathVariable String jobName,
		@PathVariable String stepName,
		@PathVariable String ddName) 
		throws JclElementNotFoundException {
	return this.service.getDD(jobName, stepName, ddName);
    }

    @RequestMapping(method=RequestMethod.GET,value="/{ddName}/params")
    @ResponseStatus(HttpStatus.OK)
    public List<DDParamDefinition> getDDParams(
		@PathVariable String jobName,
		@PathVariable String stepName,
		@PathVariable String ddName) 
		throws JclElementNotFoundException {
	return this.service.getDDParams(jobName, stepName, ddName);
    }
}
