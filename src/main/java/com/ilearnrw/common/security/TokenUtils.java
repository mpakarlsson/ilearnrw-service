package com.ilearnrw.common.security;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.security.core.token.Token;

public class TokenUtils {
	private static Logger LOG = Logger.getLogger(TokenUtils.class);

	private static final int TIMEOUT = 60 * 60 * 1000; // 1 hour

	public static boolean isExpired(Token token) {
		long timeDiff = new Date().getTime() - token.getKeyCreationTime();
		if (timeDiff > TIMEOUT) {
			LOG.debug("timeDiff is greater then TIMEOUT: " + timeDiff);
		}
		return (timeDiff > TIMEOUT);
	}

}
