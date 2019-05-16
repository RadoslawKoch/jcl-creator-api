package com.pallas.jclcreator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pallas.jcl.creator.builders.ExecutableBuilder;
import com.pallas.jcl.creator.builders.StepBuilder;
import com.pallas.jcl.creator.datamodel.interfaces.StepDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.StepParamDefinition;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jcl.creator.job.steps.params.StepParam;
import com.pallas.jcl.creator.job.steps.params.StepParamName;
import com.pallas.jclcreator.dto.CreateStepDto;
import com.pallas.jclcreator.dto.CreateStepParamDto;
import com.pallas.jclcreator.entities.JobEntity;


@Service
public class StepService  
    implements StepServiceDefinition {
	
    @Autowired
    private DataAccessServiceDefinition dataService;
	
    @Override
    public void deleteStep(String jobName, String stepName) 
		throws JclElementNotFoundException {
        JobEntity ent = this.dataService.getJobIfPresent(jobName);
	ent.deleteById(stepName);
	this.dataService.save(ent);	
    }

    @Override
    public void addStep(String jobName, CreateStepDto step)
		throws DuplicateJclElementException, 
		InvalidNameException, 
		JclElementNotFoundException {
	JobEntity tmp = this.dataService.getJobIfPresent(jobName);
	tmp.add(this.getStep(step));
	this.dataService.save(tmp);
    }
	
    private StepDefinition getStep(CreateStepDto step) throws InvalidNameException {
	return new StepBuilder()
		.setName(step.getName())
		.setExecPosition(new ExecutableBuilder().setName(step.getExecName()).setType(step.getType()).build())
		.setComment(step.getComment())
		.build();
    }

    @Override
    public void deleteStepComment(String jobName, String step) 
		throws JclElementNotFoundException {
	JobEntity tmp = this.dataService.getJobIfPresent(jobName);
	tmp.getById(step).setComment(null);
	this.dataService.save(tmp);
    }

    @Override
    public void addStepComment(String jobName, String step, String comment) 
		throws JclElementNotFoundException {
        JobEntity tmp = this.dataService.getJobIfPresent(jobName);
	tmp.getById(step).setComment(comment);	
	this.dataService.save(tmp);
    }

    @Override
    public String getStepComment(String jobName, String stepName) 
		throws JclElementNotFoundException {
	return this.dataService.getJobIfPresent(jobName).getById(stepName).getComment();
    }

    @Override
    public void addParamToStep(String jobName, String step, CreateStepParamDto param)
		throws DuplicateJclElementException, 
		InvalidParameterException, 
		JclElementNotFoundException {
	JobEntity tmp = this.dataService.getJobIfPresent(jobName);
	tmp.getById(step).add(new StepParam(param.getName(),param.getValue()));
	this.dataService.save(tmp);		
    }

    @Override
    public StepParamDefinition getStepParam(String jobName, String stepName, StepParamName name)
		throws JclElementNotFoundException {
         return this.dataService.getJobIfPresent(jobName).getById(stepName).getById(name);
    }

    @Override
    public void deleteStepParam(String jobName, String step, StepParamName param) 
		throws JclElementNotFoundException {
        JobEntity tmp = this.dataService.getJobIfPresent(jobName);
	tmp.getById(step).deleteById(param);
	this.dataService.save(tmp);
    }

    @Override
    public List<StepParamDefinition> getStepParams(String jobName, String stepName) 
		throws JclElementNotFoundException {
	return this.dataService.getJobIfPresent(jobName).getById(stepName).getElements();
    }

    @Override
    public void addStepBefore(String jobName, String stepBefore, CreateStepDto step)
		throws JclElementNotFoundException, 
		InvalidNameException, 
		DuplicateJclElementException {
	JobEntity tmp = this.dataService.getJobIfPresent(jobName);
	StepDefinition stp = tmp.getById(stepBefore);
	StepDefinition exist = this.getStep(step);
	int index = tmp.getElements().indexOf(stp);
	if(tmp.getElements().contains(exist))
		throw new DuplicateJclElementException(step.getName());
	tmp.getElements().add(index, this.getStep(step));
	this.dataService.save(tmp);
    }

    @Override
    public List<StepDefinition> getSteps(String jobName) 
		throws JclElementNotFoundException {
	return this.dataService.getJobIfPresent(jobName).getElements();
    }

    @Override
    public StepDefinition getStep(String jobName, String stepName) 
		throws JclElementNotFoundException {
	return this.dataService.getJobIfPresent(jobName).getById(stepName);
    }	
}
