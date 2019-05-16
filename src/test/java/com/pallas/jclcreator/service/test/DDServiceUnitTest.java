package com.pallas.jclcreator.service.test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.pallas.jcl.creator.datamodel.interfaces.DDParamDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.DDStatementDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.StepDefinition;
import com.pallas.jcl.creator.job.card.JobCard;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jcl.creator.job.steps.Step;
import com.pallas.jcl.creator.job.steps.dd.DDStatement;
import com.pallas.jcl.creator.job.steps.dd.params.DDParam;
import com.pallas.jcl.creator.job.steps.dd.params.DDParamName;
import com.pallas.jcl.creator.job.steps.params.Program;
import com.pallas.jclcreator.dto.CreateDDParamDto;
import com.pallas.jclcreator.dto.CreateDDStatementDto;
import com.pallas.jclcreator.entities.JobEntity;
import com.pallas.jclcreator.service.DDService;
import com.pallas.jclcreator.service.DataAccessServiceDefinition;


@RunWith(MockitoJUnitRunner.class)
public class DDServiceUnitTest {
	
	@Mock
	private DataAccessServiceDefinition data;
	
	@InjectMocks
	private DDService service;
	
	private String jobName = "X135503X";
	private String stepName = "STEP0001";
	private String inexistJobName = "X122222";
	private String inexistStepName = "STEP0002";
	private String execName = "IEFBR14";
	private JobEntity job;
	private StepDefinition step;
	private DDStatementDefinition dd;
	private String ddName = "DDIN";
	private String inexistDdName = "DDOUT";
	private DDParamName ddParamName = DDParamName.DISP;
	private String ddParamValue = "SHR";
	private DDParamName inexistDdParamName = DDParamName.LRECL;
	private String inexistDdParamValue = "80";
	private CreateDDStatementDto validDdDto = new CreateDDStatementDto();
	private CreateDDParamDto validParamDto = new CreateDDParamDto();
	private DDParamDefinition ddParam;

	@Before
	public void setup() throws InvalidNameException, DuplicateJclElementException, JclElementNotFoundException, InvalidParameterException {
		this.step = new Step(this.stepName,new Program(this.execName));
		this.job = new JobEntity(new JobCard(this.jobName));
		this.job.setId(jobName);
		this.job.add(step);
		this.dd = new DDStatement(this.ddName);
		this.step.addDD(dd);
		this.ddParam = new DDParam(ddParamName,ddParamValue);
		this.dd.add(ddParam);
		
		this.validDdDto.setName(inexistDdName);
		this.validParamDto.setName(inexistDdParamName);
		this.validParamDto.setValue(inexistDdParamValue);
		
		when(this.data.getJobIfPresent(jobName)).thenReturn(this.job);
		when(this.data.getJobIfPresent(inexistJobName)).thenThrow(JclElementNotFoundException.class);
		
	}
	
	@Test
	public void adding_dd_to_step_ends_OK() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.service.addDDToStep(jobName, stepName, validDdDto);
		assertTrue(this.step.getDDStatements().size()==2 && this.step.getDDStatements().get(1).getName().equals(this.inexistDdName));
	}
	
	@Test(expected=DuplicateJclElementException.class)
	public void adding_dd_to_step_throws_DuplicateJclElementException() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.validDdDto.setName(ddName);
		this.service.addDDToStep(jobName, stepName, validDdDto);
	}
	
	@Test(expected=InvalidNameException.class)
	public void adding_dd_to_step_throws_InvalidNameException() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.validDdDto.setName("XXXXXXXXXXXXXXXXXXXXXXx");
		this.service.addDDToStep(jobName, stepName, validDdDto);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void adding_dd_to_step_throws_JclElementNotFoundException_job() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.service.addDDToStep(inexistJobName, stepName, validDdDto);
	}

	@Test(expected=JclElementNotFoundException.class)
	public void adding_dd_to_step_throws_JclElementNotFoundException_step() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.service.addDDToStep(jobName, inexistStepName, validDdDto);
	}
	
	@Test
	public void adding_dd_param_to_dd_ends_OK() throws DuplicateJclElementException, InvalidParameterException, JclElementNotFoundException {
		this.service.addParamToDD(jobName, stepName, ddName, validParamDto);
		assertTrue(this.dd.getElements().size()==2 && this.dd.getElements().get(1).getName()==this.inexistDdParamName);
	}
	
	@Test(expected=DuplicateJclElementException.class)
	public void adding_dd_param_to_dd_throws_DuplicateJclElementException() throws DuplicateJclElementException, InvalidParameterException, JclElementNotFoundException {
		this.validParamDto.setName(ddParamName);
		this.validParamDto.setValue(ddParamValue);
		this.service.addParamToDD(jobName, stepName, ddName, validParamDto);
	}
	
	@Test(expected=InvalidParameterException.class)
	public void adding_dd_param_to_dd_throws_InvalidParameterException() throws DuplicateJclElementException, InvalidParameterException, JclElementNotFoundException {
		this.validParamDto.setName(ddParamName);
		this.validParamDto.setValue("XXXXXXXXXXx");
		this.service.addParamToDD(jobName, stepName, ddName, validParamDto);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void adding_dd_param_to_dd_throws_JclElementNotFoundException_job() throws DuplicateJclElementException, InvalidParameterException, JclElementNotFoundException {
		this.service.addParamToDD(inexistJobName, stepName, ddName, validParamDto);
	}
	
	
	@Test(expected=JclElementNotFoundException.class)
	public void adding_dd_param_to_dd_throws_JclElementNotFoundException_step() throws DuplicateJclElementException, InvalidParameterException, JclElementNotFoundException {
		this.service.addParamToDD(jobName, inexistStepName, ddName, validParamDto);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void adding_dd_param_to_dd_throws_JclElementNotFoundException_dd() throws DuplicateJclElementException, InvalidParameterException, JclElementNotFoundException {
		this.service.addParamToDD(jobName, stepName, "TEST", validParamDto);
	}
	
	@Test
	public void adding_dd_to_before_step_ends_OK() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.service.addDDToStepBefore(jobName, stepName,ddName, validDdDto);
		assertTrue(this.step.getDDStatements().size()==2 && this.step.getDDStatements().get(0).getName().equals(this.inexistDdName));
	}
	
	@Test(expected=DuplicateJclElementException.class)
	public void adding_dd_to_before_step_throws_DuplicateJclElementException() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.validDdDto.setName(ddName);
		this.service.addDDToStepBefore(jobName, stepName,ddName, validDdDto);
	}
	
	@Test(expected=InvalidNameException.class)
	public void adding_dd_to_before_step_throws_InvalidNameException() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.validDdDto.setName("XXXXXXXXXXXXXXXXXXXXXXx");
		this.service.addDDToStepBefore(jobName, stepName,ddName, validDdDto);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void adding_dd_to_before_step_throws_JclElementNotFoundException_job() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.service.addDDToStepBefore(inexistJobName, stepName,ddName, validDdDto);
	}

	@Test(expected=JclElementNotFoundException.class)
	public void adding_dd_to_before_step_throws_JclElementNotFoundException_step() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.service.addDDToStepBefore(jobName, inexistStepName,ddName, validDdDto);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void adding_dd_to_before_step_throws_JclElementNotFoundException_before() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.service.addDDToStepBefore(jobName, inexistStepName,"TEST", validDdDto);
	}
	
	@Test
	public void adding_dd_param_to_before_dd_ends_OK() throws DuplicateJclElementException, InvalidParameterException, JclElementNotFoundException {
		this.service.addParamToDDBefore(jobName, stepName, ddName, validParamDto,this.ddParamName);
		assertTrue(this.dd.getElements().size()==2 && this.dd.getElements().get(0).getName()==this.inexistDdParamName);
	}
	
	@Test(expected=DuplicateJclElementException.class)
	public void adding_dd_param_to_before_dd_throws_DuplicateJclElementException() throws DuplicateJclElementException, InvalidParameterException, JclElementNotFoundException {
		this.validParamDto.setName(ddParamName);
		this.validParamDto.setValue(ddParamValue);
		this.service.addParamToDDBefore(jobName, stepName, ddName, validParamDto,ddParamName);
	}
	
	@Test(expected=InvalidParameterException.class)
	public void adding_dd_param_to_before_dd_throws_InvalidParameterException() throws DuplicateJclElementException, InvalidParameterException, JclElementNotFoundException {
		this.validParamDto.setName(ddParamName);
		this.validParamDto.setValue("XXXXXXXXXXx");
		this.service.addParamToDDBefore(jobName, stepName, ddName, validParamDto,ddParamName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void adding_dd_param_to_before_dd_throws_JclElementNotFoundException_job() throws DuplicateJclElementException, InvalidParameterException, JclElementNotFoundException {
		this.service.addParamToDDBefore(inexistJobName, stepName, ddName, validParamDto,ddParamName);
	}
	
	
	@Test(expected=JclElementNotFoundException.class)
	public void adding_dd_param_to_before_dd_throws_JclElementNotFoundException_step() throws DuplicateJclElementException, InvalidParameterException, JclElementNotFoundException {
		this.service.addParamToDDBefore(jobName, inexistStepName, ddName, validParamDto,ddParamName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void adding_dd_param_to_before_dd_throws_JclElementNotFoundException_dd() throws DuplicateJclElementException, InvalidParameterException, JclElementNotFoundException {
		this.service.addParamToDDBefore(jobName, stepName, "TEST", validParamDto,ddParamName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void adding_dd_param_to_before_dd_throws_JclElementNotFoundException_param() throws DuplicateJclElementException, InvalidParameterException, JclElementNotFoundException {
		this.service.addParamToDDBefore(jobName, stepName, ddName, validParamDto,DDParamName.DSN);
	}

	@Test
	public void deleting_dd_from_step_ends_OK() throws JclElementNotFoundException {
		this.service.deleteDD(jobName, stepName, ddName);
		assertTrue(this.step.getDDStatements().size()==0);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void deleting_dd_from_step_throws_JclElementNotFoundException_job() throws JclElementNotFoundException {
		this.service.deleteDD(inexistJobName, stepName, ddName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void deleting_dd_from_step_throws_JclElementNotFoundException_step() throws JclElementNotFoundException {
		this.service.deleteDD(jobName, inexistStepName, ddName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void deleting_dd_from_step_throws_JclElementNotFoundException_dd() throws JclElementNotFoundException {
		this.service.deleteDD(jobName, inexistStepName, inexistDdName);
	}

	@Test
	public void deleting_dd_param_from_dd_ends_OK() throws JclElementNotFoundException {
		assertTrue(this.dd.getElements().size()==1);
		this.service.deleteDDParam(jobName, stepName, ddName, ddParamName);
		assertTrue(this.dd.getElements().size()==0);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void deleting_dd_param_from_dd_throws_JclElementNotFoundException_job() throws JclElementNotFoundException {
		this.service.deleteDDParam(inexistJobName, stepName, ddName, ddParamName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void deleting_dd_param_from_dd_throws_JclElementNotFoundException_step() throws JclElementNotFoundException {
		this.service.deleteDDParam(jobName, inexistStepName, ddName, ddParamName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void deleting_dd_param_from_dd_throws_JclElementNotFoundException_dd() throws JclElementNotFoundException {
		this.service.deleteDDParam(jobName, stepName, inexistDdName, ddParamName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void deleting_dd_param_from_dd_throws_JclElementNotFoundException_param() throws JclElementNotFoundException {
		this.service.deleteDDParam(jobName, stepName, ddName, DDParamName.SPACE);
	}

	@Test
	public void get_all_dd_statements_returns_its_list() throws JclElementNotFoundException {
		List<DDStatementDefinition> list = this.service.getDDs(jobName, stepName);
		assertTrue(list.size()==1 && list.get(0).equals(dd));
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_all_dd_statements_throws_JclElementNotFoundException_job() throws JclElementNotFoundException {
		this.service.getDDs(inexistJobName, stepName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_all_dd_statements_throws_JclElementNotFoundException_step() throws JclElementNotFoundException {
		this.service.getDDs(jobName, inexistStepName);
	}

	@Test
	public void get_dd_statement_by_its_name_returns_its_definition() throws JclElementNotFoundException {
		assertSame(this.service.getDD(jobName, stepName, ddName).getName(),this.ddName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_dd_statement_by_its_name_throws_JclElementNotFoundException_job() throws JclElementNotFoundException {
		this.service.getDD(inexistJobName, stepName, ddName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_dd_statement_by_its_name_throws_JclElementNotFoundException_step() throws JclElementNotFoundException {
		this.service.getDD(jobName, inexistStepName, ddName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_dd_statement_by_its_name_throws_JclElementNotFoundException_dd() throws JclElementNotFoundException {
		this.service.getDD(jobName, stepName, inexistDdName);
	}

	@Test
	public void get_dd_params_from_dd_returns_its_list() throws JclElementNotFoundException {
		List<DDParamDefinition> list = this.service.getDDParams(jobName, stepName, ddName);
		assertSame(list.get(0),ddParam);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_dd_params_from_dd_throws_JclElementNotFoundException_job() throws JclElementNotFoundException {
		this.service.getDDParams(inexistJobName, stepName, ddName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_dd_params_from_dd_throws_JclElementNotFoundException_step() throws JclElementNotFoundException {
		this.service.getDDParams(jobName, inexistStepName, ddName);
	}

	@Test(expected=JclElementNotFoundException.class)
	public void get_dd_params_from_dd_throws_JclElementNotFoundException_dd() throws JclElementNotFoundException {
		this.service.getDDParams(jobName, stepName, inexistDdName);
	}

}
