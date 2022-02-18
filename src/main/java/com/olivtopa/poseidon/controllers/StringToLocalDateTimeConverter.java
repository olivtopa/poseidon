package com.olivtopa.poseidon.controllers;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

	private static final Logger logger = LoggerFactory.getLogger(StringToLocalDateTimeConverter.class);

	@Override
	public LocalDateTime convert(@NonNull String source) {
		try {
			return LocalDateTime.parse(source);
		} catch (Exception e) {
			logger.info("Converting {} to LocalDateTime failed !", source);
			return null;
		}

	}

}
