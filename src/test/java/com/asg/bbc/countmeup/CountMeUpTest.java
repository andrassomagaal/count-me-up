package com.asg.bbc.countmeup;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

public class CountMeUpTest extends TestBase {
	
	@Test
	@WithMockUser
	public void countMeUpTest() throws Exception {
		mockMvc.perform(get("/CountMeUp"))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
	}

}
