package com.pallas.jclcreator.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pallas.jcl.creator.datamodel.interfaces.DDParamDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.DDStatementDefinition;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jcl.creator.job.steps.dd.DDStatement;
import com.pallas.jcl.creator.job.steps.dd.params.DDParam;
import com.pallas.jcl.creator.job.steps.dd.params.DDParamName;
import com.pallas.jclcreator.dto.CreateDDParamDto;
import com.pallas.jclcreator.dto.CreateDDStatementDto;
import com.pallas.jclcreator.entities.JobEntity;

@Service
public class DDService 
    implements DDServiceDefinition{

    @Autowired
    private DataAccessServiceDefinition dataService;
	
    @Override
    public void addDDToStep(String jobName, String step, CreateDDStatementDto dd)
		throws DuplicateJclElementException, 
		InvalidNameException, 
		JclElementNotFoundException {
	DDStatement ddStatement = new DDStatement(dd.getName());
	JobEntity job = this.dataService.getJobIfPresent(jobName);		
        job.getById(step).addDD(ddStatement);
	this.dataService.save(job);
    }

    @Override
    public void addParamToDD(String jobName, String step, String dd, CreateDDParamDto par)
		throws DuplicateJclElementException, 
		InvalidParameterException, 
		JclElementNotFoundException {
	JobEntity job = this.dataService.getJobIfPresent(jobName);
	job.getById(step).getDDByName(dd).add(new DDParam(par.getName(),par.getValue()));
	this.dataService.save(job);
    }

    @Override
    public void deleteDD(String jobName, String step, String dd) 
            throws JclElementNotFoundException {
	JobEntity job = this.dataService.getJobIfPresent(jobName);
	job.getById(step).deleteDD(dd);
	this.dataService.save(job);	
    }

    @Override
    public void deleteDDParam(String jobName, String step, String dd, DDParamName name)
		throws JclElementNotFoundException {
	JobEntity job = this.dataService.getJobIfPresent(jobName);
	job.getById(step).getDDByName(dd).deleteById(name);
	this.dataService.save(job);	
    }

    @Override
    public List<DDStatementDefinition> getDDs(String jobName, String stepName) 
		throws JclElementNotFoundException {
	return this.dataService.getJobIfPresent(jobName).getById(stepName).getDDStatements();
    }

    @Override
    public DDStatementDefinition getDD(String jobName, String stepName, String ddName)
		throws JclElementNotFoundException {
        return this.dataService.getJobIfPresent(jobName).getById(stepName).getDDByName(ddName);
    }

    @Override
    public List<DDParamDefinition> getDDParams(String jobName, String stepName, String ddName)
		throws JclElementNotFoundException {
        return this.dataService.getJobIfPresent(jobName).getById(stepName).getDDByName(ddName).getElements();
    }

    @Override
    public void addParamToDDBefore(String jobName, String step, String dd, CreateDDParamDto par, DDParamName parmBefore)
		throws DuplicateJclElementException, 
		JclElementNotFoundException, 
		InvalidParameterException {
	JobEntity job = this.dataService.getJobIfPresent(jobName);
	DDParam newParam = new DDParam(par.getName(),par.getValue());
	List<DDParamDefinition> list = job.getById(step).getDDByName(dd).getElements();
	if(list.contains(newParam)) {
		throw new DuplicateJclElementException(par.getName().toString());
	}
	DDParamDefinition param = job.getById(step).getDDByName(dd).getById(parmBefore);
	int index = list.indexOf(param);
	list.add(index,newParam);
	this.dataService.save(job);		
    }

    @Override
    public void addDDToStepBefore(String jobName, String step, String ddBefore, CreateDDStatementDto dd)
		throws DuplicateJclElementException, 
		JclElementNotFoundException, 
		InvalidNameException {
	JobEntity job = this.dataService.getJobIfPresent(jobName);
	List<DDStatementDefinition> list = job.getById(step).getDDStatements();
	DDStatement newDD = new DDStatement(dd.getName());
	if(list.contains(newDD)) {
		throw new DuplicateJclElementException(dd.getName());
	}
	DDStatementDefinition def = job.getById(step).getDDByName(ddBefore);
	int index = list.indexOf(def);
	list.add(index,newDD);
	this.dataService.save(job);	
    }
}
