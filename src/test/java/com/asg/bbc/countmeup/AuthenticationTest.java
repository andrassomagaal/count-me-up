package com.asg.bbc.countmeup;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthenticationTest extends TestBase {

	@Before
	public void setup() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		jdbcTemplate.update(INSERT_DEFAULT_USER, "user", encoder.encode("password"));
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
