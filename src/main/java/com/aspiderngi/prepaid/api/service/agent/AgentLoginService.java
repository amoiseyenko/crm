package com.aspiderngi.prepaid.api.service.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aspiderngi.prepaid.api.service.entity.LoginModel;
import com.aspiderngi.prepaid.api.web.security.TokenHandler;

@Service
public class AgentLoginService {

	Logger logger = LoggerFactory.getLogger(AgentLoginService.class);
	
	@Autowired
	AgentDetailsService adminAuthenticationService;
	
	 StandardPasswordEncoder passwordEncoder;
	 
	 TokenHandler tokenHandler;
	
	public String login(LoginModel credentials){
		try{
			UserDetails agentDetails = adminAuthenticationService.loadUserByUsername(credentials.getUserName());
			logger.info("Got details {}",agentDetails);
			String encryptedPassword = passwordEncoder.encode(credentials.getPassword());
			logger.info("Encrypted agent password: {}",encryptedPassword);
			if( passwordEncoder.matches(credentials.getPassword(), encryptedPassword)){
				 return "";
			}
			return null;
		}catch(Exception exc){
			logger.error("{}",exc);
			return null;
		}
	}
	
}
