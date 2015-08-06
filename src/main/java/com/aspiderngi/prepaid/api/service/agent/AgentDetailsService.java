package com.aspiderngi.prepaid.api.service.agent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.aspiderngi.common.service.entity.Agent;
import com.aspiderngi.common.service.entity.AgentDetails;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;

@Service
public class AgentDetailsService implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(AgentDetailsService.class);
	
    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    
    @Value("${inventory.service.url}")
	private String inventoryServiceURL;
 
    @Override
    public final AgentDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	logger.info("AgentService.loadUserByUsername: email = " + email);

    	AgentDetails agent = search(email);
        if (agent == null) {
            throw new UsernameNotFoundException("Agent " + email + " not found.");
        }

		logger.info("AgentService.loadUserByUsername: agent = " + agent.toString());
        detailsChecker.check(agent);

        return agent;
    }
    
	public AgentDetails search(String login) {
		logger.info("AgentAuthenticationService_Invoke.search");

		AgentDetails agent = null;
		Long start = System.currentTimeMillis();

		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.AGENT).append("?login=" + login);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());

			OperationResultParam<Agent> opResult = restTemplate.exchange(builder.build().encode().toString(), 
					HttpMethod.GET, 
					new HttpEntity<String>(headers),
					new ParameterizedTypeReference<OperationResultParam<Agent>>(){}).getBody();
			if(opResult.getResultValue() == null){
				logger.error("Agent with login {} not found",login);
			}
			if(opResult.getResultValue() != null) {
				agent = populate(opResult.getResultValue());
			}
		} catch(Exception ex) {
			logger.warn("{}",ex);
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}
		return agent;
	}
	
	private AgentDetails populate(Agent entity) {
		AgentDetails details = new AgentDetails();
		
		details.setAID(entity.getAID());
		details.setExpires(entity.getExpires());
		details.setFirstName(entity.getFirstName());
		details.setSecondName(entity.getSecondName());
		details.setLogin(entity.getLogin());
		details.setPassword(entity.getPassword());
		details.setRoles(populateAuthorities(entity.getRoles()));
		
		return details;
	}

	public static ArrayList<SimpleGrantedAuthority> populateAuthorities(ArrayList<String> roles) {
		ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>(roles.size());
		for(String role : roles) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" +role.toUpperCase()));
		}
		return authorities;
	}
	
}