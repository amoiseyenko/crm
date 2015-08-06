package com.aspiderngi.prepaid.api.web.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.aspiderngi.common.service.entity.User;
import com.aspiderngi.prepaid.api.service.entity.LoginModel;
import com.aspiderngi.prepaid.api.service.entity.UserAuthentication;
import com.aspiderngi.prepaid.api.web.security.TokenAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class RestLoginFilter extends AbstractAuthenticationProcessingFilter {

	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

	private static final Logger logger = LoggerFactory.getLogger(RestLoginFilter.class);
		
	
	private final TokenAuthenticationService 		tokenAuthenticationService;
	
	public RestLoginFilter(String urlMapping,UserDetailsService userDetailService, TokenAuthenticationService tokenAuthenticationService, AuthenticationManager authenticationManager) {
		super(new AntPathRequestMatcher(urlMapping));
		
		this.tokenAuthenticationService = tokenAuthenticationService;

		setAuthenticationManager(authenticationManager);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		logger.info("RestLoginFilter.attemptAuthentication");
				
		final LoginModel loginModel = new ObjectMapper().readValue(request.getInputStream(), LoginModel.class);
		
		final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(loginModel.getUserName(), loginModel.getPassword());
		
		return getAuthenticationManager().authenticate(loginToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
		logger.info("RestLoginFilter.successfulAuthentication");
		
		final UserAuthentication userAuthentication = new UserAuthentication((User)authentication.getPrincipal());

		tokenAuthenticationService.addAuthentication(response, userAuthentication);		// Add the custom token as HTTP header to the response

		SecurityContextHolder.getContext().setAuthentication(userAuthentication);		// Add the authentication to the Security context
	} 
}