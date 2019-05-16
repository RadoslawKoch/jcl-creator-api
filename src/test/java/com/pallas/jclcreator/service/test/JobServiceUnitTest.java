package com.pallas.jclcreator.service.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.pallas.jcl.creator.datamodel.interfaces.JobCardDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.JobDefinition;
import com.pallas.jcl.creator.datamodel.interfaces.StepDefinition;
import com.pallas.jcl.creator.job.card.JobCard;
import com.pallas.jcl.creator.job.card.params.KeywordParam;
import com.pallas.jcl.creator.job.card.params.KeywordParamName;
import com.pallas.jcl.creator.job.card.params.Programmer;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jcl.creator.job.steps.Step;
import com.pallas.jcl.creator.job.steps.params.ExecType;
import com.pallas.jcl.creator.job.steps.params.Program;
import com.pallas.jclcreator.dto.CreateJobDto;
import com.pallas.jclcreator.dto.CreateKeywordParamDto;
import com.pallas.jclcreator.dto.CreateStepDto;
import com.pallas.jclcreator.dto.UpdateJobCardDto;
import com.pallas.jclcreator.entities.JobEntity;
import com.pallas.jclcreator.service.DataAccessServiceDefinition;
import com.pallas.jclcreator.service.JobService;


@RunWith(MockitoJUnitRunner.class)
public class JobServiceUnitTest {

	@Mock
	private DataAccessServiceDefinition data;
	
	@InjectMocks
	private JobService service;
	
	private List<JobEntity> jobs;
	private JobEntity job;
	private final String jobName = "X135503X";
	private final String comment = "comment";
	private JobCardDefinition card;
	private KeywordParam param;
	private final KeywordParamName paramName = KeywordParamName.CLASS;
	private final String paramValue = "X";
	private StepDefinition step;
	private final String stepName = "STEP0001";
	private Program pgm;
	private String pgmName;
	private CreateStepDto dto;
	private CreateJobDto jobDto;
	private CreateJobDto newJob;
	private final String newJobName = "EUXPL111";
	private final String fname= "Alan";
	private final String lname = "Dulac";
	private final String accountInfo = "accinfo";
	private final UpdateJobCardDto updateDto = new UpdateJobCardDto();
	
	@Before
	public void setup() throws InvalidNameException, JclElementNotFoundException, InvalidParameterException, DuplicateJclElementException {	
		this.updateDto.setAccountInfo("TEST");
		this.updateDto.setComment("TEST");
		this.updateDto.setLastName("TEST");
		this.newJob = new CreateJobDto();
		this.newJob.setName(newJobName);
		this.pgmName = "IEFBR14"; 
		this.jobDto = new CreateJobDto();
		this.jobDto.setName(jobName);
		this.pgm = new Program(this.pgmName);
		this.step = new Step(this.stepName,pgm);
		this.param = new KeywordParam(this.paramName,this.paramValue);
		this.card = new JobCard(this.jobName);
		this.card.setAuthor(new Programmer(fname,lname));
		this.card.setAccountInfo(accountInfo);
		this.card.setJobDesc(comment);
		this.card.add(param);
		this.job = new JobEntity(card);
		this.job.add(step);
		this.jobs = new ArrayList<>();
		this.jobs.add(job);
		this.dto = new CreateStepDto();
		this.dto.setExecName(pgmName);
		this.dto.setName(stepName);
		this.dto.setType(ExecType.PGM);
		
		
		when(this.data.getJobByName(jobName)).thenReturn(job);
		when(this.data.getAll()).thenReturn(this.jobs);
		when(this.data.getJobIfPresent(jobName)).thenReturn(job);		
		when(this.data.getJobIfPresent(newJobName)).thenThrow(JclElementNotFoundException.class);
		when(this.data.getAllByAuthorName(lname)).thenReturn(jobs);
		when(this.data.getAllByNameLike(jobName)).thenReturn(jobs);
	}
	
	@Test(expected=DuplicateJclElementException.class)
	public void adding_two_times_same_job_throws_DupliacateJclElementException() throws InvalidNameException, DuplicateJclElementException {
		this.service.addNewJob(jobDto);
	}
	
	@Test
	public void adding_two_times_same_job_ends_OK() throws InvalidNameException, DuplicateJclElementException, JclElementNotFoundException {
		this.service.addNewJob(newJob);
		assert(true);
	}

	@Test
	public void adding_job_with_account_info_ends_OK() throws InvalidNameException, DuplicateJclElementException, JclElementNotFoundException {
		this.newJob.setAccountInfo("TEST");
		this.service.addNewJob(newJob);
		assert(true);
	}
	
	@Test
	public void adding_job_with_comment_ends_OK() throws InvalidNameException, DuplicateJclElementException, JclElementNotFoundException {
		this.newJob.setComment("TEST");
		this.service.addNewJob(newJob);
		assert(true);
	}
	
	
	@Test
	public void get_job_if_exists_returns_job() throws JclElementNotFoundException, InvalidNameException {
		assertTrue(this.service.getJob(jobName).getJobCard().getName().equals(jobName));
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_job_if_exists_throws_JclElementNotFoundException() throws JclElementNotFoundException, InvalidNameException {
		this.service.getJob(newJobName);
	}
	
	@Test
	public void get_job_author_returns_author_of_selected_job() throws JclElementNotFoundException {
		assertTrue(this.service.getJobAutor(jobName).getLname().equals(lname));
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_job_author_returns_author_throws_JclElementNotFound() throws JclElementNotFoundException {
		this.service.getJobAutor(newJobName);
	}
	
	@Test
	public void get_job_account_info_returns_info_of_selected_job() throws JclElementNotFoundException {
		assertTrue(this.service.getAccountInfo(jobName).equals(accountInfo));
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_job_account_info_throws_JclElementNotFound() throws JclElementNotFoundException {
		this.service.getAccountInfo(newJobName);
	}

	@Test
	public void get_job_comment_returns_info_of_selected_job() throws JclElementNotFoundException {
		assertTrue(this.service.getJobComment(jobName).equals(comment));
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_job_comment_throws_JclElementNotFound() throws JclElementNotFoundException {
		this.service.getJobComment(newJobName);
	}	

	@Test
	public void get_all_jobs_returns_list_of_jobs() throws JclElementNotFoundException {
		List<JobDefinition> list = this.service.getAllJobs();
		assertTrue(list.size()==1);
		assertTrue(list.get(0).equals(job));
	}
	
	@Test
	public void get_all_jobs_by_its_name_returns_list_of_jobs() throws JclElementNotFoundException {
		List<JobDefinition> list = this.service.getAllJobsByNameLike(jobName);
		assertTrue(list.size()==1);
		assertTrue(list.get(0).equals(job));
	}
	
	@Test
	public void get_all_jobs_by_its_autjor_name_returns_list_of_jobs() throws JclElementNotFoundException {
		List<JobDefinition> list = this.service.getAllJobsAuthorName(lname);
		assertTrue(list.size()==1);
		assertTrue(list.get(0).equals(job));
	}
	
	@Test
	public void get_string_content_returns_string_value() 
			throws JclElementNotFoundException, 
			InvalidNameException, 
			InvalidParameterException, 
			DuplicateJclElementException {
		String expected = this.job.toString();
		assertTrue(expected.equals(this.service.getJobStringValue(jobName)));
	}
	
	@Test
	public void delete_job_ends_OK() 
			throws JclElementNotFoundException {
		this.service.deleteJob(jobName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void delete_job_throws_JclElementNotFoundException() 
			throws JclElementNotFoundException {
		this.service.deleteJob(newJobName);
	}
	
	@Test
	public void add_new_keyword_params_ends_ok() 
			throws DuplicateJclElementException, 
			InvalidParameterException, 
			JclElementNotFoundException, 
			InvalidNameException {
		CreateKeywordParamDto ldto = new CreateKeywordParamDto();
		ldto.setName(KeywordParamName.NOTIFY);
		ldto.setValue("X135504");
		this.service.addParamToJobCard(jobName, ldto);
		assertTrue(this.job.getJobCard().getElements().get(1).getName()==KeywordParamName.NOTIFY);
	}
	
	@Test(expected=DuplicateJclElementException.class)
	public void add_new_keyword_params_throws_DuplicateJclElementException() 
			throws DuplicateJclElementException, 
			InvalidParameterException, 
			JclElementNotFoundException, 
			InvalidNameException {
		CreateKeywordParamDto ldto = new CreateKeywordParamDto();
		ldto.setName(paramName);
		ldto.setValue(paramValue);
		this.service.addParamToJobCard(jobName, ldto);
	}
	
	@Test(expected=InvalidParameterException.class)
	public void add_new_keyword_params_throws_InvalidParameterException() 
			throws DuplicateJclElementException, 
			InvalidParameterException, 
			JclElementNotFoundException, 
			InvalidNameException {
		CreateKeywordParamDto ldto = new CreateKeywordParamDto();
		ldto.setName(KeywordParamName.RESTART);
		ldto.setValue("XXXXXXXXXXXXXXXXXXXXXXXXX");
		this.service.addParamToJobCard(jobName, ldto);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void add_new_keyword_params_throws_JclElementNotFoundException() 
			throws DuplicateJclElementException, 
			InvalidParameterException, 
			JclElementNotFoundException, 
			InvalidNameException {
		CreateKeywordParamDto ldto = new CreateKeywordParamDto();
		ldto.setName(KeywordParamName.NOTIFY);
		ldto.setValue("X135504");
		this.service.addParamToJobCard(newJobName, ldto);
	}
	
	////////////////
	@Test
	public void add_new_keyword_params_before_ends_ok() 
			throws DuplicateJclElementException, 
			InvalidParameterException, 
			JclElementNotFoundException, 
			InvalidNameException {
		CreateKeywordParamDto ldto = new CreateKeywordParamDto();
		ldto.setName(KeywordParamName.NOTIFY);
		ldto.setValue("X135504");
		this.service.addParamToJobCardBefore(jobName,paramName, ldto);
		assertTrue(this.job.getJobCard().getElements().get(0).getName()==KeywordParamName.NOTIFY);
	}
	
	@Test(expected=DuplicateJclElementException.class)
	public void add_new_keyword_params_before_throws_DuplicateJclElementException() 
			throws DuplicateJclElementException, 
			InvalidParameterException, 
			JclElementNotFoundException, 
			InvalidNameException {
		CreateKeywordParamDto ldto = new CreateKeywordParamDto();
		ldto.setName(paramName);
		ldto.setValue(paramValue);
		this.service.addParamToJobCardBefore(jobName,paramName, ldto);
	}
	
	@Test(expected=InvalidParameterException.class)
	public void add_new_keyword_params_before_throws_InvalidParameterException() 
			throws DuplicateJclElementException, 
			InvalidParameterException, 
			JclElementNotFoundException, 
			InvalidNameException {
		CreateKeywordParamDto ldto = new CreateKeywordParamDto();
		ldto.setName(KeywordParamName.RESTART);
		ldto.setValue("XXXXXXXXXXXXXXXXXXXXXXXXX");
		this.service.addParamToJobCardBefore(jobName,paramName, ldto);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void add_new_keyword_params_before_throws_JclElementNotFoundException() 
			throws DuplicateJclElementException, 
			InvalidParameterException, 
			JclElementNotFoundException, 
			InvalidNameException {
		CreateKeywordParamDto ldto = new CreateKeywordParamDto();
		ldto.setName(KeywordParamName.NOTIFY);
		ldto.setValue("X135504");
		this.service.addParamToJobCardBefore(newJobName,paramName, ldto);
	}

	@Test
	public void get_keyword_params_by_job_name_returns_keyword_list() throws JclElementNotFoundException  {
		assertTrue(this.service.getKeywordParams(jobName).size()==1);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_keyword_params_throws_JclElementNotFoundException() throws JclElementNotFoundException  {
		this.service.getKeywordParams(newJobName);
	}
	

	@Test
	public void get_job_card_by_job_name_returns_job_card() throws JclElementNotFoundException  {
		assertTrue(this.service.getJobCard(jobName).getName().equals(jobName));
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_job_card_throws_JclElementNotFoundException() throws JclElementNotFoundException  {
		this.service.getJobCard(newJobName);
	}
	

	@Test
	public void get_keyword_param_by_job_name_and_its_name_returns_param() throws JclElementNotFoundException  {
		assertTrue(this.service.getKeywordParam(jobName, paramName).equals(param));
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_keyword_param_by_invalid_job_name_and_its_name_throws_JclElementNotFoundException() throws JclElementNotFoundException  {
		this.service.getKeywordParam(newJobName, paramName);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_keyword_param_by_job_name_and_its_invalid_name_throws_JclElementNotFoundException() throws JclElementNotFoundException  {
		this.service.getKeywordParam(jobName, KeywordParamName.LINES);
	}
	
	@Test
	public void delete_keyword_ends_OK() 
			throws JclElementNotFoundException, InvalidNameException {
		this.service.deleteKeyWordParam(jobName, paramName);
		assertTrue(this.service.getJob(jobName).getJobCard().getElements().isEmpty());
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void delete_keyword_by_invalid_param_name_throws_JclElementNotFoundException() 
			throws JclElementNotFoundException {
		this.service.deleteKeyWordParam(jobName, KeywordParamName.LINES);
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void delete_keyword_by_invalid_job_name_throws_JclElementNotFoundException() 
			throws JclElementNotFoundException {
		this.service.deleteKeyWordParam(newJobName,paramName);
	}
	
	@Test
	public void update_job_card_ends_ok_with_all_data() throws JclElementNotFoundException {
		this.service.updateJobCardInfo(updateDto, jobName);
		assertTrue(this.job.getJobCard().getAccountInfo().equals("TEST")
				&& this.job.getJobCard().getAuthor().getLname().equals("TEST")
				&& this.job.getJobCard().getJobDesc().equals("TEST"));
	}
	
	@Test
	public void update_job_card_ends_ok_with_null_values() throws JclElementNotFoundException {
		this.updateDto.setAccountInfo(null);
		this.updateDto.setComment(null);
		this.updateDto.setLastName(null);
		this.service.updateJobCardInfo(updateDto, jobName);
		assertTrue(this.job.getJobCard().getAccountInfo()==null
				&& this.job.getJobCard().getAuthor()==null
				&& this.job.getJobCard().getJobDesc()==null);
	}
}
