package com.asg.bbc.countmeup;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery("SELECT username, password, 'true' as enabled FROM users WHERE username=?")
		.authoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username=?");
	}

	/**
	 * Enable authentication to all URL except login/logout </br>
	 * CountMeUp requires ADMIN role 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/CountMeUp").hasRole("ADMIN")
		.anyRequest().authenticated().and()
		.formLogin().loginPage("/login").permitAll().and()
		.logout().permitAll();
	}

}
