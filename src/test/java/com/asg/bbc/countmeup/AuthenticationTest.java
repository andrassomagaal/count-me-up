package com.asg.bbc.countmeup;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class AuthenticationTest extends TestBase {

	@Before
	public void setup() {
		jdbcTemplate.update(INSERT_DEFAULT_USER, "user", "password");
		jdbcTemplate.update(INSERT_DEFAULT_AUTHORITY, "user", "ROLE_USER");
	}

	@After
	public void tearDown() {
		jdbcTemplate.execute(DELETE_USERS);
		jdbcTemplate.execute(DELETE_AUTHORITIES);
	}

	@Test
	public void loginSuccessTest() throws Exception {
		mockMvc.perform(formLogin()).andExpect(authenticated());
	}

	@Test
	public void loginFailTest() throws Exception {
		mockMvc.perform(formLogin().password("dummy")).andExpect(unauthenticated());
	}

	@Test
	public void logoutTest() throws Exception {
		mockMvc.perform(logout()).andExpect(unauthenticated());
	}

	@Test
	public void authenticationTest() throws Exception {
		mockMvc.perform(get("/dummy").with(anonymous())).andExpect(redirectedUrlPattern("**/login"))
				.andExpect(status().is3xxRedirection());
	}

}
