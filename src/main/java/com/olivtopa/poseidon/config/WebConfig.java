package com.olivtopa.poseidon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.olivtopa.poseidon.controllers.StringToDoubleConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToDoubleConverter());
	}

}
