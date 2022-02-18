package com.olivtopa.poseidon.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

public class StringToIntegerConverter implements Converter<String, Integer> {

	private static final Logger logger = LoggerFactory.getLogger(StringToIntegerConverter.class);

	@Override
	public Integer convert(@NonNull String source) {
		try {
			return Integer.parseInt(source);
		} catch (Exception e) {
			logger.info("Converting {} to Integer type failed !", source);
			return null;
		}
	}

}
