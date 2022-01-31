package com.olivtopa.poseidon.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

public class StringToDoubleConverter implements Converter<String, Double> {

	private static final Logger logger = LoggerFactory.getLogger(StringToDoubleConverter.class);
	
	@Override
	public Double convert(@NonNull String source) {
		try {
			return Double.parseDouble(source);
		} catch (Exception e) {
			logger.info("Converting {} to Double type failed !", source);
			return null;
		}
	}

}
