package com.olivtopa.poseidon.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.olivtopa.poseidon.domain.User;
import com.olivtopa.poseidon.repositories.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/", "/css/**");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http
		.authorizeRequests().antMatchers("/bidList/**", "/curvePoint/**", "/rating/**", "/ruleName/**", "/trade/**")
		.hasAnyRole("ADMIN","USER");
		http
		.authorizeRequests().antMatchers("/user/**")
		.hasRole("ADMIN");
		http
		.authorizeRequests().anyRequest().authenticated()
		.and().formLogin().defaultSuccessUrl("/bidList/list")
		.and().logout().logoutSuccessUrl("/").logoutUrl("/app-logout")
		.and().oauth2Login().defaultSuccessUrl("/bidList/list")
		.and().csrf().disable();
		//@formatter:on
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(s -> {
			User byUserName = userRepository.findByUsername(s);
			if (byUserName != null) {
				SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_"+byUserName.getRole());
				return new org.springframework.security.core.userdetails.User(byUserName.getUsername(),
						byUserName.getPassword(), Collections.singleton(role));
			} else
				throw new UsernameNotFoundException(s);
		});
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		return daoAuthenticationProvider;

	}

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());

	}
}
