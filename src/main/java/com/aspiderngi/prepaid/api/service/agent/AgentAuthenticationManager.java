package com.aspiderngi.prepaid.api.service.agent;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

import com.aspiderngi.common.service.entity.AgentDetails;

@Component
public class AgentAuthenticationManager  implements AuthenticationManager{

	private Logger logger = LoggerFactory.getLogger(AgentAuthenticationManager.class);
	
	@Autowired
 	AgentDetailsService agentDetailsService;
	
	StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String agentLogin = authentication.getName();
		AgentDetails agent =  agentDetailsService.loadUserByUsername(agentLogin);
		
		if(agent == null) {
			logger.info("Agent with login {} not found",agentLogin);
			throw new BadCredentialsException(MessageFormat.format("Agent with login {0} not found",agentLogin));
		}
		if( !passwordEncoder.matches((String) authentication.getCredentials(), agent.getPassword())) {
			logger.info("Wrong password for login {}",agentLogin);
			throw new BadCredentialsException(MessageFormat.format("Wrong password for login {0}",agentLogin));
		}
		
		logger.info("Successfull authorisation for login {}",agentLogin);
		
		return new UsernamePasswordAuthenticationToken(agent,agent.getPassword(),agent.getAuthorities()) ;
	}

	

}
