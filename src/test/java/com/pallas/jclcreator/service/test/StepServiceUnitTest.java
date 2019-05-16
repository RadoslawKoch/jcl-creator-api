package com.pallas.jclcreator.service.test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.pallas.jcl.creator.datamodel.interfaces.JobCardDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.StepDefinition;
import com.pallas.jcl.creator.job.card.JobCard;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jcl.creator.job.steps.Step;
import com.pallas.jcl.creator.job.steps.params.ExecType;
import com.pallas.jcl.creator.job.steps.params.Program;
import com.pallas.jcl.creator.job.steps.params.StepParam;
import com.pallas.jcl.creator.job.steps.params.StepParamName;
import com.pallas.jclcreator.dto.CreateStepDto;
import com.pallas.jclcreator.dto.CreateStepParamDto;
import com.pallas.jclcreator.entities.JobEntity;
import com.pallas.jclcreator.service.DataAccessServiceDefinition;
import com.pallas.jclcreator.service.StepService;

@RunWith(MockitoJUnitRunner.class)
public class StepServiceUnitTest {

	
	@Mock
	private DataAccessServiceDefinition data;
	
	@InjectMocks
	private StepService service;

	private JobEntity job;
	private JobCardDefinition card;
	private StepDefinition existStep;
	private String existStepName = "STEP0001";
	private String jobName = "X135503";
	private String newStepName= "STEP0002";
	private String pgmName = "IEFBR14";
	private String inexistJobName = "X111111X";
	private CreateStepDto validDto = new CreateStepDto();
	private CreateStepDto invalidDto = new CreateStepDto();
	private CreateStepDto duplicateDto = new CreateStepDto();
	private String stepComment = "COMMENT";
	
	@Before
	public void setup() 
			throws InvalidNameException, 
			DuplicateJclElementException, 
			JclElementNotFoundException, InvalidParameterException {
		this.card = new JobCard(jobName);
		this.job = new JobEntity(card);
		this.existStep = new Step(existStepName,new Program(pgmName));
		this.existStep.setComment(stepComment);
		this.job.add(existStep);
		this.existStep.add(new StepParam(StepParamName.TIME,"NOLIMIT"));
		validDto.setExecName(pgmName);
		validDto.setName(newStepName);
		validDto.setType(ExecType.PGM);
		
		invalidDto.setExecName(pgmName);
		invalidDto.setName("0000000");
		invalidDto.setType(ExecType.PGM);
		
		duplicateDto.setExecName(pgmName);
		duplicateDto.setName(existStepName);
		duplicateDto.setType(ExecType.PGM);
		
		when(this.data.getJobIfPresent(jobName)).thenReturn(job);
		when(this.data.getJobIfPresent(inexistJobName)).thenThrow(JclElementNotFoundException.class);
	}
	
	@Test
	public void delete_step_by_valid_job_name_and_valid_step_name_ends_OK() throws JclElementNotFoundException {
		this.service.deleteStep(jobName, existStepName);
		assertTrue(this.job.getElements().size()==0);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void delete_step_by_valid_job_name_and_valid_step_name_throws_JclElementNotFoundException() throws JclElementNotFoundException {
		this.service.deleteStep(jobName, newStepName);
	}
	
	@Test
	public void add_new_steps_ends_OK() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.service.addStep(jobName, validDto);
		assertTrue(this.job.getElements().size()==2);		
	}
	
	@Test(expected=DuplicateJclElementException.class)
	public void add_new_steps_throws_DuplicateJclElementException() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.service.addStep(jobName, duplicateDto);
	}
	
	@Test(expected=InvalidNameException.class)
	public void add_new_steps_throws_InvalidNameException() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.service.addStep(jobName, this.invalidDto);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void add_new_steps_throws_JclElementNotFoundException() throws DuplicateJclElementException, InvalidNameException, JclElementNotFoundException {
		this.service.addStep(inexistJobName, validDto);
	}
	
	@Test
	public void delete_comment_from_step_ends_OK() throws JclElementNotFoundException {
		this.service.deleteStepComment(jobName, existStepName);
		assertTrue(this.existStep.getComment()==null);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void delete_comment_from_step_with_invalid_step_name_throws_JclElementNotFoundException() throws JclElementNotFoundException {
		this.service.deleteStepComment(jobName, newStepName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void delete_comment_from_step_with_invalid_job_name_throws_JclElementNotFoundException() throws JclElementNotFoundException {
		this.service.deleteStepComment(inexistJobName, existStepName);
	}
	
	@Test
	public void add_comment_to_step_ends_OK() throws JclElementNotFoundException {
		this.service.addStepComment(jobName, existStepName, "TEST");
		assertTrue(this.existStep.getComment().equals("TEST"));
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void add_comment_to_step_with_invalid_job_name_throws_JclElementNotFoundException() throws JclElementNotFoundException {
		this.service.addStepComment(inexistJobName, existStepName, "TEST");
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void add_comment_to_step_with_invalid_step_name_throws_JclElementNotFoundException() throws JclElementNotFoundException {
		this.service.addStepComment(jobName, newStepName, "TEST");
	}
	
	@Test
	public void get_step_comment_returns_comment() throws JclElementNotFoundException {
		assertSame(this.stepComment,this.service.getStepComment(jobName, existStepName));	
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_step_comment__with_ivalid_job_namethrows_JclElementNotFoundException() throws JclElementNotFoundException {
		assertSame(this.stepComment,this.service.getStepComment(inexistJobName, existStepName));	
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_step_comment_with_invalid_step_name_throws_JclElementNotFoundException() throws JclElementNotFoundException {
		assertSame(this.stepComment,this.service.getStepComment(jobName, newStepName));	
	}
	
	@Test
	public void add_param_to_steps_ends_OK() throws JclElementNotFoundException, DuplicateJclElementException, InvalidParameterException {
		CreateStepParamDto dto = new CreateStepParamDto();
		dto.setName(StepParamName.COND);
		dto.setValue("(99,NE)");
		this.service.addParamToStep(jobName, existStepName, dto);
		assertTrue(this.existStep.getById(dto.getName()).getValue().equals("(99,NE)"));
	}
	
	@Test(expected=DuplicateJclElementException.class)
	public void add_param_to_steps_throws_DuplicateJclElementException() throws JclElementNotFoundException, DuplicateJclElementException, InvalidParameterException {
		CreateStepParamDto dto = new CreateStepParamDto();
		dto.setName(StepParamName.COND);
		dto.setValue("(99,NE)");
		this.service.addParamToStep(jobName, existStepName, dto);
		this.service.addParamToStep(jobName, existStepName, dto);
	}
	
	@Test(expected=InvalidParameterException.class)
	public void add_param_to_steps_throws_InvalidParameterException() throws JclElementNotFoundException, DuplicateJclElementException, InvalidParameterException {
		CreateStepParamDto dto = new CreateStepParamDto();
		dto.setName(StepParamName.COND);
		dto.setValue("XXXXXXXXXXXXXXXXX");
		this.service.addParamToStep(jobName, existStepName, dto);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void add_param_to_steps__by_invalid_step_name_throws_JclElementNotFoundException() throws JclElementNotFoundException, DuplicateJclElementException, InvalidParameterException {
		CreateStepParamDto dto = new CreateStepParamDto();
		dto.setName(StepParamName.COND);
		dto.setValue("(99,NE)");
		this.service.addParamToStep(jobName, newStepName, dto);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void add_param_to_steps_by_invalid_job_name_throws_JclElementNotFoundException() throws JclElementNotFoundException, DuplicateJclElementException, InvalidParameterException {
		CreateStepParamDto dto = new CreateStepParamDto();
		dto.setName(StepParamName.COND);
		dto.setValue("(99,NE)");
		this.service.addParamToStep(inexistJobName, existStepName, dto);
	}
	
	@Test
	public void get_step_param_returns_choosen_parameter() throws JclElementNotFoundException {
		assertSame(this.service.getStepParam(jobName, existStepName, StepParamName.TIME).getValue(),"NOLIMIT");
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_step_param_throws_JclElementNotFoundException_param() throws JclElementNotFoundException {
		this.service.getStepParam(jobName, existStepName, StepParamName.COND);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_step_param_throws_JclElementNotFoundException_step() throws JclElementNotFoundException {
		this.service.getStepParam(jobName, newStepName, StepParamName.TIME);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_step_param_throws_JclElementNotFoundException_job() throws JclElementNotFoundException {
		this.service.getStepParam(inexistJobName, existStepName, StepParamName.TIME);
	}

	@Test
	public void delete_step_param_ends_OK() throws JclElementNotFoundException {
		this.service.deleteStepParam(jobName, existStepName, StepParamName.TIME);
		assertTrue(this.existStep.getElements().size()==0);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void delete_step_param_throws_JclElementNotFoundException_parm() throws JclElementNotFoundException {
		this.service.deleteStepParam(jobName, existStepName, StepParamName.COND);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void delete_step_param_throws_JclElementNotFoundException_step() throws JclElementNotFoundException {
		this.service.deleteStepParam(jobName, newStepName, StepParamName.TIME);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void delete_step_param_throws_JclElementNotFoundException_job() throws JclElementNotFoundException {
		this.service.deleteStepParam(inexistJobName, existStepName, StepParamName.TIME);
	}
	
	@Test
	public void get_all_steps_from_job_returns_step_list() throws JclElementNotFoundException {
		assertTrue(this.service.getSteps(jobName).size()==1);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_all_steps_from_job_returns_throws_JclElementNotFoundException_job() throws JclElementNotFoundException {
		this.service.getSteps(inexistJobName);
	}
	
	@Test
	public void get_step_by_its_name_returns_step_definition() throws JclElementNotFoundException {
		StepDefinition step = this.service.getStep(jobName, existStepName);
		assertSame(step,existStep);			
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_step_by_its_name_throws_JclElementNotFoundException_job() throws JclElementNotFoundException {
		this.service.getStep(inexistJobName, existStepName);		
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_step_by_its_name_throws_JclElementNotFoundException_step() throws JclElementNotFoundException {
		this.service.getStep(jobName, newStepName);		
	}
	
	@Test
	public void get_step_param_list_returns_list() throws JclElementNotFoundException {
		assertSame(1, this.service.getStepParams(jobName, existStepName).size());
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_step_param_list_throws_JclElementNotFound_job() throws JclElementNotFoundException {
		this.service.getStepParams(inexistJobName, existStepName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_step_param_list_throws_JclElementNotFound_step() throws JclElementNotFoundException {
		this.service.getStepParams(inexistJobName, newStepName);
	}
	
	@Test
	public void add_step_before_another_ends_OK() throws JclElementNotFoundException, InvalidNameException, DuplicateJclElementException {
		this.service.addStepBefore(jobName, existStepName, validDto);
		assertTrue(this.job.getElements().get(0).getName().equals(newStepName));
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void add_step_before_another_throws_JclElementNotFoundException_job() 
			throws JclElementNotFoundException, 
			InvalidNameException, 
			DuplicateJclElementException {
		this.service.addStepBefore(inexistJobName, existStepName, validDto);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void add_step_before_another_throws_JclElementNotFoundException_step() 
			throws JclElementNotFoundException, 
			InvalidNameException, 
			DuplicateJclElementException {
		this.service.addStepBefore(jobName, newStepName, validDto);
	}
	
	@Test(expected=InvalidNameException.class)
	public void add_step_before_another_throws_InvalidNameException() 
			throws JclElementNotFoundException, 
			InvalidNameException, 
			DuplicateJclElementException {
		this.service.addStepBefore(jobName, existStepName, invalidDto);
	}
	
	@Test(expected=DuplicateJclElementException.class)
	public void add_step_before_another_throws_DuplicateJclElementException() 
			throws JclElementNotFoundException, 
			InvalidNameException, 
			DuplicateJclElementException {
		this.service.addStepBefore(jobName, existStepName, duplicateDto);
	}
}
