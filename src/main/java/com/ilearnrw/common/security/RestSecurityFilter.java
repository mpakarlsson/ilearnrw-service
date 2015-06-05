package com.ilearnrw.common.security;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.SecureRandomFactoryBean;
import org.springframework.security.core.token.Token;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;


/**
 * @author cantemir.mihu
 * All requests are routed through this filter. The filter looks
 * for a request parameter called "token". If it is present, it 
 * tries to interpret it as a token and if it is valid it passes it over to 
 * the AuthenticationManager to authenticate the request 
 */
public class RestSecurityFilter extends GenericFilterBean {
	private static Logger LOG = Logger
			.getLogger(RestSecurityFilter.class);

	private AuthenticationManager authenticationManager;
	private AuthenticationEntryPoint authenticationEntryPoint;

	private KeyBasedPersistenceTokenService tokenService;

	public RestSecurityFilter() {
		super();
	}

	// this is not used
	public RestSecurityFilter(AuthenticationManager authenticationManager) {
		this(authenticationManager, new BasicAuthenticationEntryPoint(), null);
		((BasicAuthenticationEntryPoint) authenticationEntryPoint)
				.setRealmName("");
	}

	public RestSecurityFilter(
			AuthenticationManager authenticationManager,
			AuthenticationEntryPoint authenticationEntryPoint,
			KeyBasedPersistenceTokenService tokenService) {
		this.authenticationManager = authenticationManager;
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.tokenService = tokenService;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		LOG.debug("doing Filter in CustomRestSecurityFilter");
		
		// if we passed through the BasicAuthenticationFilter with no authentication, we should require one
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			LOG.debug("no auth set");
			authenticationEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException("you should provide basic auth"));
			return;
		}
		
		// from this point on, we know the user provided basic auth, we don't need it anymore
		SecurityContextHolder.getContext().setAuthentication(null);
		
		Map<String, String[]> parms = request.getParameterMap();
		if (parms.containsKey("token")) {
			String tokenKey = parms.get("token")[0]; // grab the first "token"
													// parameter
	        Token receivedToken = tokenService.verifyToken(tokenKey);
		
			if (TokenUtils.isExpired(receivedToken)) {
				LOG.debug("Token expired");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
				return;
			}

	        RestToken token = RestToken.fromToken(receivedToken);

			// set the authentication into the SecurityContext
			SecurityContextHolder.getContext().setAuthentication(
					authenticationManager.authenticate(token));
		}
		// continue thru the filter chain
		chain.doFilter(request, response);

	}

}
