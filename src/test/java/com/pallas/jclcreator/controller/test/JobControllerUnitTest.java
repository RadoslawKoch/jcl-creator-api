package com.pallas.jclcreator.controller.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.google.gson.Gson;
import com.pallas.jcl.creator.datamodel.interfaces.Author;
import com.pallas.jcl.creator.datamodel.interfaces.JobCardDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.JobDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.KeywordParamDefinition;
import com.pallas.jcl.creator.job.Job;
import com.pallas.jcl.creator.job.card.JobCard;
import com.pallas.jcl.creator.job.card.params.KeywordParam;
import com.pallas.jcl.creator.job.card.params.KeywordParamName;
import com.pallas.jcl.creator.job.card.params.Programmer;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jclcreator.controller.JobController;
import com.pallas.jclcreator.dto.CreateJobDto;
import com.pallas.jclcreator.dto.CreateKeywordParamDto;
import com.pallas.jclcreator.dto.UpdateJobCardDto;
import com.pallas.jclcreator.service.JobServiceDefinition;


@RunWith(SpringRunner.class)
@WebMvcTest(JobController.class) 
public class JobControllerUnitTest {

	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private JobServiceDefinition service;
	
	private final String accountInfo = "PALLAS SARL";
	private final String fname = "Radosław";
	private final String lname = "Koch";
	private final String validJobName = "X135504X";
	private final String invalidJobName = "02133XXXX";
	private final List<JobDefinition> jobs = new ArrayList<>();
	private JobDefinition job;
	private Author autor;
	private JobCardDefinition jobCard;
	private final String comment = "comm";
	private final KeywordParamName paramName = KeywordParamName.CLASS;
	private final String paramValue = "X";	
	private KeywordParamDefinition paramDto;
	private final List<KeywordParamDefinition> params = new ArrayList<>();
	private final KeywordParamName beforeParam = KeywordParamName.COND;
	private final UpdateJobCardDto dto = new UpdateJobCardDto();
	
	@Before
	public void setup() 
			throws InvalidNameException, 
			JclElementNotFoundException, 
			InvalidParameterException, 
			DuplicateJclElementException {
		this.jobCard = new JobCard(validJobName);
		this.autor = new Programmer(fname,lname);
		this.job = new Job(this.jobCard);
		this.dto.setAccountInfo("TEST");
		this.jobCard.setAccountInfo(accountInfo);
		this.jobCard.setAuthor(autor);
		this.jobCard.setJobDesc(comment);
		this.paramDto = new KeywordParam(paramName,paramValue);
		this.params.add(paramDto);
		this.jobs.add(job);
		when(this.service.getJobStringValue(validJobName)).thenReturn(this.job.toString());
		when(this.service.getAccountInfo(validJobName)).thenReturn(accountInfo);
		when(this.service.getAccountInfo(invalidJobName)).thenThrow(NullPointerException.class);	
		when(this.service.getAllJobs()).thenReturn(jobs);
		when(this.service.getJob(validJobName)).thenReturn(job);
		when(this.service.getJob(invalidJobName)).thenThrow(NullPointerException.class);
		when(this.service.getJobAutor(validJobName)).thenReturn(autor);
		when(this.service.getJobAutor(invalidJobName)).thenThrow(NullPointerException.class);
		when(this.service.getJobCard(validJobName)).thenReturn(jobCard);
		when(this.service.getJobCard(invalidJobName)).thenThrow(NullPointerException.class);
		when(this.service.getJobComment(validJobName)).thenReturn(this.comment);
		when(this.service.getJobComment(invalidJobName)).thenThrow(NullPointerException.class);
		when(this.service.getKeywordParams(validJobName)).thenReturn(params);
		when(this.service.getKeywordParams(invalidJobName)).thenThrow(NullPointerException.class);	
		
		when(this.service.getKeywordParam(invalidJobName,paramName)).thenThrow(NullPointerException.class);	
		when(this.service.getKeywordParam(validJobName,paramName)).thenReturn(paramDto);
		when(this.service.getAllJobsByNameLike(validJobName)).thenReturn(jobs);
		when(this.service.getAllJobsAuthorName(lname)).thenReturn(jobs);
		Mockito.doThrow(JclElementNotFoundException.class).when(this.service).updateJobCardInfo(dto, invalidJobName);

	}
	
	@Test
	public void create_new_job_with_valid_input_returns_status_code_of_201() throws Exception {
		Gson tmp = new Gson();
		CreateJobDto ldto = new CreateJobDto();
		ldto.setAccountInfo(accountInfo);
		ldto.setComment("");
		ldto.setName(validJobName);		
		this.mvc.perform(post("/jobs").contentType(MediaType.APPLICATION_JSON_UTF8).content(tmp.toJson(ldto)))
			.andExpect(status().isCreated());
	}
	
	@Test
	public void create_new_job_with_invalid_input_returns_status_code_of_400() throws Exception {
		Gson tmp = new Gson();
		CreateJobDto ldto = new CreateJobDto();
		ldto.setAccountInfo(accountInfo);
		ldto.setComment("");
		ldto.setName(invalidJobName);		
		this.mvc.perform(post("/jobs").contentType(MediaType.APPLICATION_JSON_UTF8).content(tmp.toJson(ldto)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void add_new_keyword_param_with_valid_input_returns_status_code_of_201() throws Exception {
		Gson tmp = new Gson();
		CreateKeywordParamDto ldto = new CreateKeywordParamDto();
		ldto.setName(paramName);	
		ldto.setValue(paramValue);
		this.mvc.perform(post("/jobs/"+this.validJobName+"/card/params").contentType(MediaType.APPLICATION_JSON_UTF8).content(tmp.toJson(ldto)))
			.andExpect(status().isCreated());
	}
	
	
	@Test
	public void add_new_keyword_param_with_invalid_input_returns_status_code_of_400() throws Exception {
		Gson tmp = new Gson();
		CreateKeywordParamDto ldto = new CreateKeywordParamDto();
		ldto.setName(null);	
		ldto.setValue(null);
		this.mvc.perform(post("/jobs/"+this.validJobName+"/card/params").contentType(MediaType.APPLICATION_JSON_UTF8).content(tmp.toJson(dto)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void add_new_keyword_param_before_another_with_valid_input_returns_status_code_of_201() throws Exception {
		Gson tmp = new Gson();
		CreateKeywordParamDto ldto = new CreateKeywordParamDto();
		ldto.setName(paramName);	
		ldto.setValue(paramValue);
		this.mvc.perform(post("/jobs/"+this.validJobName+"/card/params/before/"+this.beforeParam).contentType(MediaType.APPLICATION_JSON_UTF8).content(tmp.toJson(ldto)))
			.andExpect(status().isCreated());
	}
	
	
	@Test
	public void add_new_keyword_param_before_another_with_invalid_input_returns_status_code_of_400() throws Exception {
		Gson tmp = new Gson();
		CreateKeywordParamDto ldto = new CreateKeywordParamDto();
		ldto.setName(null);	
		ldto.setValue(null);
		this.mvc.perform(post("/jobs/"+this.validJobName+"/card/params/before/"+this.beforeParam).contentType(MediaType.APPLICATION_JSON_UTF8).content(tmp.toJson(ldto)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void get_all_jobs_returns_list_and_status_of_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs"))
				.andExpect(status().isOk()).andExpect(content()
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
			assertTrue(result.getResponse().getContentAsString().contains(validJobName));
	}
	
	@Test
	public void get_job_by_its_name_returns_job_and_status_of_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/"+this.validJobName))
				.andExpect(status().isOk()).andExpect(content()
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
			assertTrue(result.getResponse().getContentAsString().contains(validJobName));
	}
	
	
	@Test
	public void get_job_by_its_name_returns_null_and_status_of_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.invalidJobName))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void get_jobs_by_its_name_returns_job_and_status_of_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/search/"+this.validJobName))
				.andExpect(status().isOk()).andExpect(content()
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
			assertTrue(result.getResponse().getContentAsString().contains(validJobName));
	}
	
	@Test
	public void get_jobs_by_its_author_name_returns_job_and_status_of_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/search/author/"+this.lname))
				.andExpect(status().isOk()).andExpect(content()
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
			assertTrue(result.getResponse().getContentAsString().contains(validJobName));
	}
	
	
	@Test
	public void delete_job_by_its_name_returns_200() throws Exception {
		this.mvc.perform(delete("/jobs/"+this.validJobName))
				.andExpect(status().isOk());
	}
	
	@Test
	public void get_job_params_by_its_name_returns_params_and_status_of_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/"+this.validJobName+"/card/params"))
				.andExpect(status().isOk()).andExpect(content()
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
			assertTrue(result.getResponse().getContentAsString().contains(paramName.toString()));
	}
	
	@Test
	public void get_job_params_by_invalid_name_returns_null_and_status_of_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.invalidJobName+"/card/params"))
				.andExpect(status().isBadRequest());
	}
	

	@Test
	public void get_job_card_by_its_name_returns_card_and_status_of_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/"+this.validJobName+"/card"))
				.andExpect(status().isOk()).andExpect(content()
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
			assertTrue(result.getResponse().getContentAsString().contains(this.validJobName));
	}
	
	@Test
	public void get_job_card_by_invalid_name_returns_null_and_status_of_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.invalidJobName+"/card"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void get_job_param_card_param_by_its_name_returns_param_and_status_of_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/"+this.validJobName+"/card/params/"+this.paramName))
				.andExpect(status().isOk()).andExpect(content()
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
			assertTrue(result.getResponse().getContentAsString().contains(this.paramValue));
	}
	
	@Test
	public void get_job_card_param_by_its__invalid_name_returns_null_and_status_of_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.invalidJobName+"/card/params/"+this.paramName))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void get_job_author_by_job_name_returns_author_and_status_of_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/"+this.validJobName+"/autor"))
				.andExpect(status().isOk()).andExpect(content()
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
			assertTrue(result.getResponse().getContentAsString().contains(this.lname));
	}
	
	@Test
	public void get_job_author_by_invalid_job_name_returns_null_and_status_of_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.invalidJobName+"/autor"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void delete_job_param_by_its_name_returns_200() throws Exception {
		this.mvc.perform(delete("/jobs/"+this.validJobName+"/card/params/"+this.paramName))
				.andExpect(status().isOk());
	}
	
	@Test
	public void get_job_comment_by_job_invalid_name_returns_null_and_status_of_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.invalidJobName+"/comment"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void get_job_comment_by_job_name_returns_comment_and_status_of_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/"+this.validJobName+"/comment"))
				.andExpect(status().isOk())
				.andReturn();
			assertTrue(result.getResponse().getContentAsString().contains(this.comment));
	}
	
	@Test
	public void get_account_info_by_job_invalid_name_returns_null_and_status_of_400() throws Exception {
		this.mvc.perform(get("/jobs/"+this.invalidJobName+"/account"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void get_account_info_by_job_name_returns_info_and_status_of_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/"+this.validJobName+"/account"))
				.andExpect(status().isOk())
				.andReturn();
			assertTrue(result.getResponse().getContentAsString().contains(this.accountInfo));
	}
	
	@Test
	public void get_job_string_conent_returns_string_and_200() throws Exception {
		MvcResult result = this.mvc.perform(get("/jobs/"+this.validJobName+"/txt"))
				.andExpect(status().isOk()).andReturn();
		
		assertTrue(result.getResponse().getContentAsString().equals(this.job.toString()));

	}
	
	@Test
	public void update_job_card_ends_ok() throws Exception {
		Gson tmp = new Gson();
		this.mvc.perform(put("/jobs/"+this.validJobName+"/card")
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(tmp.toJson(dto)))
		.andExpect(status().isOk());
	}
	
	@Test
	public void update_job_card_ends_throws_JclElementNotFoundException() throws Exception {
		Gson tmp = new Gson();
		this.mvc.perform(put("/jobs/"+this.invalidJobName+"/card")
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(tmp.toJson(dto)))
		.andExpect(status().isOk());
	}
}
