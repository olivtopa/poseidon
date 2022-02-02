package com.olivtopa.poseidon;


import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;



public class FormatToUrlEncoded {
	
	public static String getUrlEncoded(Object dto) {
	    ObjectMapper MAPPER = new ObjectMapper();
	    MAPPER.registerModule(new JavaTimeModule());
	    Map map = MAPPER.convertValue(dto, Map.class);

	    return map.toString()
	            .replace(", ", "&")
	            .replace("{", "")
	            .replace("}", "");
	}
}
