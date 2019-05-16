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
import com.pallas.jcl.creator.datamodel.interfaces.KeywordParamDefinition;
import com.pallas.jcl.creator.job.card.JobCard;
import com.pallas.jcl.creator.job.card.params.KeywordParam;
import com.pallas.jcl.creator.job.card.params.KeywordParamName;
import com.pallas.jcl.creator.job.exceptions.DuplicateJclElementException;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;
import com.pallas.jcl.creator.job.exceptions.InvalidParameterException;
import com.pallas.jcl.creator.job.exceptions.JclElementNotFoundException;
import com.pallas.jclcreator.entities.JobEntity;
import com.pallas.jclcreator.repos.JobEntityRepository;
import com.pallas.jclcreator.service.DataAccessService;

@RunWith(MockitoJUnitRunner.class)
public class DataAccessServiceUnitTest {

	@Mock
	private JobEntityRepository repo;
	
	@InjectMocks
	private DataAccessService service;
	
	private String jobName = "X135503X";
	private String inexistJobName = "X122222";
	private List<JobEntity> jobs = new ArrayList<>();
	private JobCardDefinition card;
	private JobEntity job;
	private KeywordParamDefinition param;
	private KeywordParamName paramName = KeywordParamName.CLASS;
	private String paramValue = "X";
	private String authorName = "Dulac";
	
	
	
	@Before
	public void setup() throws InvalidNameException, InvalidParameterException, DuplicateJclElementException {
		this.card = new JobCard(jobName);
		this.job = new JobEntity(this.card);
		this.jobs.add(this.job);
		this.param = new KeywordParam(paramName,paramValue);
		this.job.getJobCard().add(param);
		
		when(this.repo.findAll()).thenReturn(jobs);
		when(this.repo.getJobByName(jobName)).thenReturn(job);
		when(this.repo.findByJobCardAuthorLnameLike(authorName)).thenReturn(jobs);
		when(this.repo.findByNameLike(jobName)).thenReturn(jobs);
		
	}
	
	@Test
	public void get_job_if_present_by_its_name_returns_job_definition() throws InvalidNameException, JclElementNotFoundException {
		assertTrue(this.service.getJobIfPresent(jobName).getJobCard().getName().equals(jobName));
	}
	
	@Test(expected=JclElementNotFoundException.class)
	public void get_job_if_present_by_its_name_throws_InvalidNameException() throws JclElementNotFoundException {
		this.service.getJobIfPresent(inexistJobName);
	}
	
	@Test
	public void get_job_by_its_name_returns_job_definition() {
		assertTrue(this.service.getJobByName(jobName).getJobCard().getName().equals(jobName));
	}
	
	@Test
	public void get_job_by_its_name_returns_null() {
		assertTrue(this.service.getJobByName(inexistJobName)==null);
	}
	
	@Test
	public void add_new_job_ends_ok() {
		this.service.save(job);
	}
	
	@Test
	public void get_all_jobs_returns_job_list() {
		assertTrue(this.service.getAll().size()==1);
	}
	
	@Test
	public void delete_job_ends_ok() {
		this.service.delete(job);
	}
	
	@Test
	public void delete_job__by_name_ends_ok() {
		this.service.delete(jobName);
	}
	
	@Test
	public void get_all_jobs_by_its_name_like() {
		assertTrue(this.service.getAllByNameLike(jobName).size()==1);
	}
	
	@Test
	public void get_all_jobs_by_its_author_name_like() {
		assertTrue(this.service.getAllByAuthorName(authorName).size()==1);
	}
	

}
