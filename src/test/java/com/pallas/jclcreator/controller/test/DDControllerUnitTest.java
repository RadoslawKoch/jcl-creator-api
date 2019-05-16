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
import com.pallas.jcl.creator.datamodel.interfaces.DDParamDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.DDStatementDefinition;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jcl.creator.job.steps.dd.DDStatement;
import com.pallas.jcl.creator.job.steps.dd.params.DDParam;
import com.pallas.jcl.creator.job.steps.dd.params.DDParamName;
import com.pallas.jclcreator.controller.DDController;
import com.pallas.jclcreator.dto.CreateDDParamDto;
import com.pallas.jclcreator.dto.CreateDDStatementDto;
import com.pallas.jclcreator.service.DDService;
import com.pallas.jclcreator.service.EventServiceDefinition;

@RunWith(SpringRunner.class)
@WebMvcTest(DDController.class) 
public class DDControllerUnitTest {


	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private DDService service;
	
//	@MockBean
//	private EventServiceDefinition eventService;
	
	
	private CreateDDStatementDto createDDDto = new CreateDDStatementDto();
	private CreateDDParamDto createDDParamDto = new CreateDDParamDto();
	private DDStatementDefinition DDDto;
	private DDParamDefinition param;
	private List<DDParamDefinition> params= new ArrayList<>();
	private List<DDStatementDefinition> dds = new ArrayList<>();
	
	private final String validDDName = "DDIN1";
	private final String invalidDDName = "0123456788";
	private final String jobName = "X135503X";
	private final String validStepName = "STEP0001";
	private final String invalidStepName = "0000000000";
	private final DDParamName paramName = DDParamName.DISP;
	private final String paramValue = "SHR";
	private final String stepBefore = "STP00000";
	private final DDParamName paramBefore = DDParamName.DSN;

	@Before
	public void setup() 
			throws JclElementNotFoundException, 
			InvalidNameException, 
			DuplicateJclElementException, 
			InvalidParameterException {
		param = new DDParam(paramName, paramValue);
		createDDDto.setName(validDDName);
		createDDParamDto.setName(paramName);
		createDDParamDto.setValue(paramValue);
		params.add(param);
		DDDto = new DDStatement(this.validDDName);
		DDDto.add(param);
		dds.add(DDDto);

		when(this.service.getDD(jobName, validStepName, validDDName)).thenReturn(this.DDDto);
		when(this.service.getDD(jobName, validStepName, invalidDDName)).thenThrow(NullPointerException.class);
		
		when(this.service.getDDParams(jobName, validStepName, validDDName)).thenReturn(this.params);
		when(this.service.getDDParams(jobName, validStepName, invalidDDName)).thenThrow(NullPointerException.class);
		
		when(this.service.getDDs(jobName, validStepName)).thenReturn(dds);
		when(this.service.getDDs(jobName, invalidStepName)).thenThrow(NullPointerException.class);
	}
	
	@Test
	public void add_new_dd_statement_with_valid_parameters_returns_201() throws Exception {
		Gson convert  = new Gson();
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd")
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8)
				.content(convert.toJson(createDDDto)))
			.andExpect(status().isCreated());				
	}
	
	@Test
	public void add_new_dd_statement_with_invalid_parameters_returns_null_and_400() throws Exception {
		Gson convert  = new Gson();
		CreateDDStatementDto dto = new CreateDDStatementDto();
		dto.setName(this.invalidDDName);
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd")
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8)
				.content(convert.toJson(dto)))
			.andExpect(status().isBadRequest());			
	}
	
	@Test
	public void add_new_dd_statement_before_another_with_valid_parameters_returns_201() throws Exception {
		Gson convert  = new Gson();
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd/before/"+this.stepBefore)
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8)
				.content(convert.toJson(createDDDto)))
			.andExpect(status().isCreated());			
	}
	
	@Test
	public void add_new_dd_statement_before_another_with_invalid_parameters_returns_null_and_400() throws Exception {
		Gson convert  = new Gson();
		CreateDDStatementDto dto = new CreateDDStatementDto();
		dto.setName(this.invalidDDName);
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd/before/"+this.stepBefore)
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8)
				.content(convert.toJson(dto)))
			.andExpect(status().isBadRequest());
				
	}
	
	@Test
	public void add_new_dd_param_before_another_with_valid_parameters_returns_201() throws Exception {
		Gson convert  = new Gson();
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd/"+this.validDDName +"/params/before/"+this.paramBefore)
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8)
				.content(convert.toJson(createDDParamDto)))
			.andExpect(status().isCreated());
				
	}
	
	@Test
	public void add_new_dd_param_before_another_with_invalid_parameters_returns_null_and_400() throws Exception {
		Gson convert  = new Gson();
		CreateDDParamDto dto = new CreateDDParamDto();
		dto.setName(null);
		dto.setValue(null);
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd/"+this.validDDName +"/params/before/"+this.paramBefore)
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8)
				.content(convert.toJson(dto)))
			.andExpect(status().isBadRequest());
				
	}
	
	@Test
	public void add_new_dd_param_with_valid_parameters_returns_201() throws Exception {
		Gson convert  = new Gson();
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd/"+this.validDDName +"/params")
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8)
				.content(convert.toJson(createDDParamDto)))
			.andExpect(status().isCreated());
				
	}
	
	@Test
	public void add_new_dd_param_with_invalid_parameters_returns_null_and_400() throws Exception {
		Gson convert  = new Gson();
		CreateDDParamDto dto = new CreateDDParamDto();
		dto.setName(null);
		dto.setValue(null);
		this.mvc.perform(post("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd/"+this.validDDName +"/params")
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8)
				.content(convert.toJson(dto)))
			.andExpect(status().isBadRequest());
				
	}
	

	@Test
	public void delete_dd_param_with_valid_dd_name_returns_200() throws Exception {
		this.mvc.perform(delete("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd/"+this.validDDName+"/params/"+this.paramName.toString()))
			.andExpect(status().isOk());
				
	}
	
	@Test
	public void delete_dd_statement_with_valid_dd_name_returns_200() throws Exception {
		this.mvc.perform(delete("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd/"+this.validDDName))
			.andExpect(status().isOk());
				
	}
	
	@Test
	public void get_all_dds_by_valid_job_name_returns_dd_list_and_200() throws Exception{
		MvcResult result = this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd/"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andReturn();
		
		assertTrue(result.getResponse().getContentAsString().contains(this.validDDName));
	}
	
	@Test
	public void get_all_dds_by_invalid_job_name_returns_null_and_400() throws Exception{
		this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.invalidStepName+"/dd/"))
				.andExpect(status().isBadRequest());

	}
	
	@Test
	public void get_dd_by_valid_dd_name_returns_dd_and_200() throws Exception{
		MvcResult result = this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd/"+this.validDDName))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andReturn();
		
		assertTrue(result.getResponse().getContentAsString().contains(this.validDDName));
	}
	
	@Test
	public void get_dd_by_invalid_dd_name_returns_null_and_400() throws Exception{
		this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd/"+this.invalidDDName))
				.andExpect(status().isBadRequest());

	}
	
	@Test
	public void get_dd_params_by_valid_dd_name_returns_dd_and_200() throws Exception{
		MvcResult result = this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd/"+this.validDDName+"/params"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andReturn();
		
		assertTrue(result.getResponse().getContentAsString().contains(this.paramValue));
	}
	
	@Test
	public void get_dd_params_by_invalid_dd_name_returns_null_and_400() throws Exception{
		this.mvc.perform(get("/jobs/"+this.jobName+"/steps/"+this.validStepName+"/dd/"+this.invalidDDName+"/params"))
				.andExpect(status().isBadRequest());

	}

}
