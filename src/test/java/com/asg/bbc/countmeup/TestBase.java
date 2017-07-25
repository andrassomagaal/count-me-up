package com.asg.bbc.countmeup;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ContextConfiguration
//@Profile("dev")
public abstract class TestBase {

	// The default user for authentication is username=user and
	// password=password
	protected static final String INSERT_DEFAULT_USER = "INSERT INTO users (username, password) VALUES(?, ?)";
	protected static final String INSERT_DEFAULT_AUTHORITY = "INSERT INTO authorities (username, authority) VALUES(?, ?)";
	protected static final String DELETE_USERS = "DELETE FROM users";
	protected static final String DELETE_AUTHORITIES = "DELETE FROM authorities";

	@Autowired
	protected WebApplicationContext webApplicationContext;

	@Autowired
	protected Filter springSecurityFilterChain;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	protected MockMvc mockMvc;

	@Before
	public void baseSetup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilters(springSecurityFilterChain)
				.apply(springSecurity()).build();
	}

}
