package com.pallas.jclcreator.service;

import java.util.List;
import com.pallas.jcl.creator.datamodel.interfaces.StepDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.StepParamDefinition;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jcl.creator.job.steps.params.StepParamName;
import com.pallas.jclcreator.dto.CreateStepDto;
import com.pallas.jclcreator.dto.CreateStepParamDto;

public interface StepServiceDefinition {

    void deleteStep(String jobName, String stepName)
		throws JclElementNotFoundException;		
	
    void addStep(String jobName,CreateStepDto step) 
		throws DuplicateJclElementException, 
		InvalidNameException,
		JclElementNotFoundException;
	
    void deleteStepComment(String jobName,String step)
		throws JclElementNotFoundException;	
	
    void addStepComment(String jobName,String step, String comment)
		throws JclElementNotFoundException;	
	
    List<StepDefinition> getSteps(String jobName)
		throws JclElementNotFoundException;	
	
    StepDefinition getStep(String jobName,String stepName)
		throws JclElementNotFoundException;	
	
    String getStepComment(String jobName, String stepName)
		throws JclElementNotFoundException;	
	
    void addParamToStep(String jobName,String step, CreateStepParamDto param) 
		throws DuplicateJclElementException, 
		InvalidParameterException,
		JclElementNotFoundException;
	
    StepParamDefinition getStepParam(String jobName,String stepName, StepParamName name)
		throws JclElementNotFoundException;	
	
    void deleteStepParam(String jobName,String step, StepParamName param)
		throws JclElementNotFoundException;	
	
    List<StepParamDefinition> getStepParams(String jobName, String stepName)
                throws JclElementNotFoundException;
    
    void addStepBefore(String jobName,String stepBefore,CreateStepDto step) 
   		throws JclElementNotFoundException, 
		InvalidNameException, 
		DuplicateJclElementException;
}
