package com.aspiderngi.prepaid.api.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.aspiderngi.common.service.entity.CustomerDetails;
import com.aspiderngi.common.service.entity.User;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;

@Service
public class CustomerAuthenticationService_Invoke implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerAuthenticationService_Invoke.class);
	
    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Value("${inventory.service.url}")
	private String inventoryServiceURL;
 
    @Override
    public final UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	
    	logger.info("UserService.loadUserByUsername: email = " + email);

    	CustomerDetails customerDetails = search(email);
    	
        if (customerDetails == null) {
            throw new UsernameNotFoundException("User " + email + " not found.");
        }

//        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        if(loginDetails.getRole() != null)
//        	for(String role : loginDetails.getRole().split("\\,", -1)) 
//        		authorities.add(new SimpleGrantedAuthority("ROLE_"+role)); 

		final UserDetails user = new User(customerDetails.getCustomerId(), customerDetails.getEmail(), customerDetails.getPassword(), "");
		logger.info("UserService.loadUserByUsername: user = " + user.toString());

        detailsChecker.check(user);

        return user;
    }
    
	public CustomerDetails search(String email) {
		logger.info("CustomerAuthenticationService_Invoke.search");

		CustomerDetails customerDetails = null;
		Long start = System.currentTimeMillis();

		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			// TODO: artem: for some reason Inv Mgmt method search is crashing if we do not provide firstname and msisdn.
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.CUSTOMERS).append("?email=" + email);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());

			OperationResultParam<CustomerDetails> opResult = restTemplate.exchange(builder.build().encode().toString(), 
					HttpMethod.GET, 
					new HttpEntity<String>(headers),
					new ParameterizedTypeReference<OperationResultParam<CustomerDetails>>(){}).getBody();

			customerDetails = opResult.getResultValue();
		} catch(Exception ex) {
			logger.warn("{}",ex);
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}

		return customerDetails;
	}
}