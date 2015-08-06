package com.aspiderngi.prepaid.api.web.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.aspiderngi.prepaid.api.web.security.TokenAuthenticationService;

@Component
@Scope("prototype")
public class RestAuthenticationFilter extends GenericFilterBean {

	private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationFilter.class);

	private final TokenAuthenticationService authenticationService;

	public RestAuthenticationFilter(TokenAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {

		logger.info("RestAuthenticationFilter.doFilter");

		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			Authentication authentication = authenticationService.getAuthentication(httpRequest);

			logger.info("authentication: " + (authentication == null ? "" : authentication.getDetails()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			filterChain.doFilter(request, response);

			// SecurityContextHolder.getContext().setAuthentication(null);
		}
	}
}