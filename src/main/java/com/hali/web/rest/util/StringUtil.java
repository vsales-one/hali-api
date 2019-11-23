package com.hali.web.rest.util;


import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class StringUtil {
	public static Boolean isBlank(String string) {
		return StringUtils.isBlank(string);
	}

	public static String generateUUID() {
	    return UUID.randomUUID().toString();
    }
}
