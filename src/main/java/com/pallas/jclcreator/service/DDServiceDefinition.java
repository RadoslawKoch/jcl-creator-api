package com.pallas.jclcreator.service;

import java.util.List;

import com.pallas.jcl.creator.datamodel.interfaces.DDParamDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.DDStatementDefinition;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jcl.creator.job.steps.dd.params.DDParamName;
import com.pallas.jclcreator.dto.CreateDDParamDto;
import com.pallas.jclcreator.dto.CreateDDStatementDto;

public interface DDServiceDefinition {

    void addDDToStep(String jobName,String step, CreateDDStatementDto dd) 
		throws DuplicateJclElementException, 
		InvalidNameException,
		JclElementNotFoundException;	
	
    void addParamToDD(String jobName,String step, String dd, CreateDDParamDto par) 
		throws DuplicateJclElementException, 
		InvalidParameterException,
                JclElementNotFoundException;
	
    void deleteDD(String jobName,String step, String dd) 
		throws JclElementNotFoundException;
	
    void deleteDDParam(String jobName,String step, String dd, DDParamName name) 
                throws JclElementNotFoundException;
	
    List<DDStatementDefinition> getDDs(String jobName,String stepName) 
		throws JclElementNotFoundException;
	
    DDStatementDefinition getDD(String jobName,String stepName,String ddName) 
		throws JclElementNotFoundException;
	
    List<DDParamDefinition> getDDParams(String jobName, String stepName,String ddName) 
		throws JclElementNotFoundException;
	
    void addParamToDDBefore(String jobName, String step, String dd, CreateDDParamDto par,DDParamName parmBefore) 
		throws DuplicateJclElementException, 
		JclElementNotFoundException, 
		InvalidParameterException;
	
    void addDDToStepBefore(String jobName,String step,String ddBefore, CreateDDStatementDto dd) 
		throws DuplicateJclElementException, 
		JclElementNotFoundException, 
                InvalidNameException;	
}
