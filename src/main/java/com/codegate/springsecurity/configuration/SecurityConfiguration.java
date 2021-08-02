package com.codegate.springsecurity.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.codegate.springsecurity.repository.UserRepository;
import com.codegate.springsecurity.service.SSUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private SSUserDetailsService userDetailsService;

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception{
		return new SSUserDetailsService(userRepository);
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.
						authorizeRequests().
						antMatchers("/","h2-console/**").permitAll().						
						antMatchers("/admin").access("hasAnyAuthority('ADMIN')").
						anyRequest().authenticated().
				   and().
				   		formLogin().
				   		loginPage("/login").
				   		permitAll().
				   	and().
				   		logout().
				   		logoutRequestMatcher(new AntPathRequestMatcher("/logout")).
				   		logoutSuccessUrl("/login").permitAll();
		
		httpSecurity.csrf().disable();
		httpSecurity.headers().frameOptions().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
			
//		builder.
//				inMemoryAuthentication().
//				withUser("david").password(passwordEncoder().encode("password")).authorities("ADMIN").
//				and().
//				withUser("user").password(passwordEncoder().encode("password")).authorities("USER");
		
		builder.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoder());
	}

}
