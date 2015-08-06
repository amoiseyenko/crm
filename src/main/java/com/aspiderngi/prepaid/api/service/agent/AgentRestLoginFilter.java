package com.aspiderngi.prepaid.api.service.agent;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.aspiderngi.common.service.entity.AgentDetails;
import com.aspiderngi.prepaid.api.service.entity.LoginModel;
import com.aspiderngi.prepaid.api.service.entity.UserAuthentication;
import com.aspiderngi.prepaid.api.web.security.TokenAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class AgentRestLoginFilter extends AbstractAuthenticationProcessingFilter {


	private static final Logger logger = LoggerFactory.getLogger(AgentRestLoginFilter.class);
		
	private final TokenAuthenticationService 		tokenAuthenticationService;
	
	public AgentRestLoginFilter(String urlMapping,  TokenAuthenticationService tokenAuthenticationService, AuthenticationManager authenticationManager) {
		super(new AntPathRequestMatcher(urlMapping));
		
		this.tokenAuthenticationService = tokenAuthenticationService;
 
		setAuthenticationManager(authenticationManager);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		logger.info("AgentRestLoginFilter.attemptAuthentication");
				
		final LoginModel loginModel = new ObjectMapper().readValue(request.getInputStream(), LoginModel.class);
		
		final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(loginModel.getUserName(), loginModel.getPassword());
		
		return getAuthenticationManager().authenticate(loginToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
		logger.info("AgentRestLoginFilter.successfulAuthentication");
		
		final AgentAuthentication agentAuthentication = new AgentAuthentication((AgentDetails)authentication.getPrincipal());

		tokenAuthenticationService.addAuthentication(response, agentAuthentication);		// Add the custom token as HTTP header to the response

		SecurityContextHolder.getContext().setAuthentication(agentAuthentication);		// Add the authentication to the Security context
	}
}