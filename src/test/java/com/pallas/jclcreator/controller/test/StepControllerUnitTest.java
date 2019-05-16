package com.pallas.jclcreator.controller.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.google.gson.Gson;
import com.pallas.jcl.creator.datamodel.interfaces.StepDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.StepParamDefinition;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jcl.creator.job.steps.Step;
import com.pallas.jcl.creator.job.steps.params.ExecType;
import com.pallas.jcl.creator.job.steps.params.Program;
import com.pallas.jcl.creator.job.steps.params.StepParam;
import com.pallas.jcl.creator.job.steps.params.StepParamName;
import com.pallas.jclcreator.controller.StepController;
import com.pallas.jclcreator.dto.CreateStepDto;
import com.pallas.jclcreator.dto.CreateStepParamDto;
import com.pallas.jclcreator.service.StepServiceDefinition;

@RunWith(SpringRunner.class)
@WebMvcTest(StepController.class) 
public class StepControllerUnitTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private StepServiceDefinition service;
	
	
	private CreateStepParamDto createParamDto = new CreateStepParamDto();
	private CreateStepDto createStepDto = new CreateStepDto();
	private StepParamDefinition param;
	private StepDefinition step;
	private List<StepDefinition> steps = new ArrayList<>();
	private List<StepParamDefinition> params = new ArrayList<>();
	
	private String jobName = "X135503X";
	private String invalidJobName = "X135503Xsasas";
	private String stepName = "STEP0001";
	private String invalidStepName = "X121312122";
	private String comment = "comment";
	private StepParamName paramName = StepParamName.TIME;
	private String paramValue = "NOLIMIT";
	private String pgmName = "IEFBR14";
	private String invalidPgmName = "IDEFBRXXXX";
	private Gson gson = new Gson();
	private String stepBefore = "STEP0000";
	
	
	@Before
	public void setup() throws JclElementNotFoundException, InvalidParameterException, InvalidNameException {
		param = new StepParam(paramName,paramValue);
		step = new Step(stepName,new Program(pgmName));
		step.getElements().add(param);
		createParamDto.setName(paramName);
		createParamDto.setValue(paramValue);
		createStepDto.setComment(comment);
		createStepDto.setType(ExecType.PGM);
		createStepDto.setExecName(pgmName);
		createStepDto.setName(stepName);
		params.add(param);
		steps.add(step);
		
		
		when(this.service.getStep(jobName, stepName)).thenReturn(step);
		when(this.service.getStep(jobName, invalidStepName)).thenThrow(NullPointerException.class);
		when(this.service.getStep(invalidJobName, stepName)).thenThrow(NullPointerException.class);
		
		when(this.service.getStepComment(jobName, stepName)).thenReturn(this.comment);
		when(this.service.getStepComment(jobName, invalidStepName)).thenThrow(NullPointerException.class);
		when(this.service.getStepComment(invalidJobName, stepName)).thenThrow(NullPointerException.class);
		
		when(this.service.getStepParam(jobName, stepName, paramName)).thenReturn(this.param);
		when(this.service.getStepParam(jobName, invalidStepName, paramName)).thenThrow(NullPointerException.class);
		when(this.service.getStepParam(invalidJobName, stepName, paramName)).thenThrow(NullPointerException.class);
		
		when(this.service.getStepParams(jobName, stepName)).thenReturn(this.params);
		when(this.service.getStepParams(jobName, invalidStepName)).thenThrow(NullPointerException.class);
		when(this.service.getStepParams(invalidJobName, stepName)).thenThrow(NullPointerException.class);
		
		when(this.service.getSteps(jobName)).thenReturn(this.steps);
		when(this.service.getSteps(invalidJobName)).thenThrow(NullPointerException.class);
			
	}
	
	@Test
	public void add_new_step_with_valid_params_returns_201() throws Exception {
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(gson.toJson(createStepDto))).andExpect(status().isCreated());	
	}
	
	@Test
	public void add_new_step_with_invalid_params_returns_400() throws Exception {
		CreateStepDto dto = new CreateStepDto();
		dto.setExecName(invalidPgmName);
		dto.setType(null);
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(gson.toJson(dto))).andExpect(status().isBadRequest());	
	}
	
	@Test
	public void add_new_step_before_antoher_with_valid_params_returns_201() throws Exception {
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps/before/"+this.stepBefore).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(gson.toJson(createStepDto))).andExpect(status().isCreated());	
	}
	
	@Test
	public void add_new_step_before_antoher_with_invalid_params_returns_400() throws Exception {
		CreateStepDto dto = new CreateStepDto();
		dto.setExecName(invalidPgmName);
		dto.setType(null);
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps/before/"+this.stepBefore).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(gson.toJson(dto))).andExpect(status().isBadRequest());	
	}
	
	@Test
	public void add_new_step_param_with_valid_params_returns_201() throws Exception {
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps/"+this.stepName+"/params").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(gson.toJson(createParamDto))).andExpect(status().isCreated());		
	}
	
	@Test
	public void add_new_step_param_with_invalid_params_returns_400() throws Exception {
		CreateStepParamDto dto = new CreateStepParamDto();
		dto.setName(null);
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps/"+this.stepName+"/params").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(gson.toJson(dto))).andExpect(status().isBadRequest());	
	}
	
	@Test
	public void add_new_step_comment_with_valid_params_returns_201() throws Exception {
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps/"+this.stepName+"/comment").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.comment)).andExpect(status().isCreated());	
	}
	
	@Test
	public void get_comment_with_valid_step_name_returns_comment_and_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.stepName+"/comment"))
				
				.andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().contains(this.comment));
	}
	
	@Test
	public void get_comment_with_invalid_step_name_returns_null_and_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.invalidStepName+"/comment"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void get_comment_with_invalid_job_name_returns_null_and_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.invalidJobName+"/steps/"+this.stepName+"/comment"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void get_step_with_valid_step_name_returns_step_and_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.stepName))
				
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().contains(this.stepName));
	}
	
	@Test
	public void get_step_with_invalid_step_name_returns_null_and_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.invalidStepName))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void get_step_with_invalid_job_name_returns_null_and_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.invalidJobName+"/steps/"+this.stepName))
				.andExpect(status().isBadRequest());
	}
	

	@Test
	public void get_steps_with_valid_step_name_returns_steps_and_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/"+this.jobName+"/steps"))
				
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().contains(this.stepName));
	}
	
	
	@Test
	public void get_steps_with_invalid_job_name_returns_null_and_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.invalidJobName+"/steps"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void get_step_params_with_valid_step_name_returns_step_params_and_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.stepName+"/params"))
				
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().contains(this.paramName.toString()));
	}
	
	
	@Test
	public void get_step_params_with_invalid_step_name_returns_null_and_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.invalidStepName+"/params"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void get_step_params_with_invalid_job_name_returns_null_and_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.invalidJobName+"/steps/"+this.stepName+"/params"))
				.andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void get_step_param_with_valid_step_name_returns_step_params_and_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.stepName+"/params/"+this.paramName))
				
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().contains(this.paramName.toString()));
	}
	
	
	@Test
	public void get_step_param_with_invalid_step_name_returns_null_and_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.invalidStepName+"/params/"+this.paramName))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void get_step_param_with_invalid_job_name_returns_null_and_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.invalidJobName+"/steps/"+this.stepName+"/params/"+this.paramName))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void delete_step_returns_200() throws Exception{
		this.mvc.perform(delete("/jobs/"+this.jobName+"/steps/"+this.stepName))
			.andExpect(status().isOk());
	}
	
	@Test
	public void delete_comment_returns_200() throws Exception{
		this.mvc.perform(delete("/jobs/"+this.jobName+"/steps/"+this.stepName+"/comment"))
			.andExpect(status().isOk());
	}
	
	@Test
	public void delete_param_returns_200() throws Exception{
		this.mvc.perform(delete("/jobs/"+this.jobName+"/steps/"+this.stepName+"/params/"+this.paramName))
			.andExpect(status().isOk());
	}
	
}
