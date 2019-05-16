package com.pallas.jclcreator.controller.test;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.pallas.jcl.creator.job.card.params.KeywordParamName;
import com.pallas.jcl.creator.job.steps.dd.params.DDParamName;
import com.pallas.jcl.creator.job.steps.params.StepParamName;
import com.pallas.jclcreator.controller.EnumController;

@RunWith(SpringRunner.class)
@WebMvcTest(EnumController.class) 
public class EnumControllerUnitTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void get_all_keywords_returns_list_of_enums() throws Exception {
		MvcResult result = this.mvc.perform(get("/keywords"))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andReturn();
		
		assertTrue(result.getResponse().getContentAsString().contains(KeywordParamName.CLASS.toString()));
	}
	
	@Test
	public void get_all_ddparams_returns_list_of_enums() throws Exception {
		MvcResult result = this.mvc.perform(get("/ddparams"))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andReturn();
		
		assertTrue(result.getResponse().getContentAsString().contains(DDParamName.DISP.toString()));
	}
	
	@Test
	public void get_all_stepparams_returns_list_of_enums() throws Exception {
		MvcResult result = this.mvc.perform(get("/stepparams"))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andReturn();
		
		assertTrue(result.getResponse().getContentAsString().contains(StepParamName.COND.toString()));
	}
}
