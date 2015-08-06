package com.aspiderngi.prepaid.api.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.aspiderngi.common.service.entity.AgentDetails;
import com.aspiderngi.common.service.entity.User;
import com.aspiderngi.prepaid.api.service.agent.AgentAuthentication;
import com.aspiderngi.prepaid.api.service.entity.UserAuthentication;

@Service
public class TokenAuthenticationService {

	private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
	private static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;

	@Autowired
	private TokenHandler tokenHandler;

	private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationService.class);

	public void addAuthentication(HttpServletResponse response, Authentication authentication) {
		logger.info("TokenAuthenticationService.addAuthentication");
		if(authentication instanceof UserAuthentication) {
			final User user = ((UserAuthentication)authentication).getDetails();
			user.setExpires(System.currentTimeMillis() + TEN_DAYS);
			response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(user));
		}
		else if(authentication instanceof AgentAuthentication) {
			final AgentDetails agent = ((AgentAuthentication)authentication).getDetails();
			agent.setExpires(System.currentTimeMillis() + TEN_DAYS);
			response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(agent));
		}
	}

	public Authentication getAuthentication(HttpServletRequest request) {
		logger.info("TokenAuthenticationService.getAuthentication");
		final String token = request.getHeader(AUTH_HEADER_NAME);

		if (token != null) {
			final UserDetails details = tokenHandler.parseUserFromToken(token);
			if(details != null) {
				if ( details instanceof User) {
					User user = (User) details;
					return new UserAuthentication(user);
				}
				else if (details instanceof AgentDetails) {
					AgentDetails agent = (AgentDetails)details;
					return new AgentAuthentication(agent);
				}
			}
		}
		return null;
	}
}