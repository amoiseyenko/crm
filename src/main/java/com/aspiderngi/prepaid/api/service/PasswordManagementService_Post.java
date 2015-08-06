package com.aspiderngi.prepaid.api.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.aspiderngi.artifacts.model.ForgotPasswordModel;
import com.aspiderngi.common.service.entity.CustomerDetails;
import com.aspiderngi.common.service.entity.ForgotPasswordToken;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.aspiderngi.common.service.entity.PasswordChange;

@Component
public class PasswordManagementService_Post {
	private static final Logger logger = LoggerFactory.getLogger(PasswordManagementService_Post.class);
	
	@Value("${inventory.service.url}")
	private String inventoryServiceURL;
	
	@Value("${email.service.url}")
	private String emailServiceURL;
	
	public OperationResult postToken(CustomerDetails details, ForgotPasswordToken forgotPassToken) throws Exception {
		logger.trace("PasswordManagementService_Post.postToken");
		
		Long start = System.currentTimeMillis();
		OperationResult opResult = null;
		
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.PASSWORD)
					.append("/").append(details.getCustomerId().toString());
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
			
			String requestJson = new ObjectMapper().writeValueAsString(forgotPassToken);
			HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
			
			opResult = restTemplate.exchange(builder.build().encode().toString(), HttpMethod.POST,entity,
					OperationResult.class).getBody();
			if(opResult == null) {
				throw new Exception("Unable to get responce from inventory service");
			} else if(!opResult.getResultCode().equals("OK")){
				throw new Exception(opResult.getResultMessage());
			}
			
			sendForgotPasswordEmail(details, forgotPassToken);
		} catch(Exception ex) {
			logger.warn(ex.getMessage());
			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}
		return opResult;
	}
	
	public OperationResult checkPostToken(CustomerDetails details, ForgotPasswordToken forgotPassToken) throws Exception {
		logger.trace("PasswordManagementService_Post.checkPostToken");
		
		Long start = System.currentTimeMillis();
		OperationResultParam<PasswordChange> opResult = null;
		
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.PASSWORD)
					.append("/").append(details.getCustomerId().toString());
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
			
			String requestJson = new ObjectMapper().writeValueAsString(forgotPassToken);
			HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
			
			opResult = restTemplate.exchange(builder.build().encode().toString(), HttpMethod.GET, entity,
					new ParameterizedTypeReference<OperationResultParam<PasswordChange>>(){}).getBody();
			
			if(opResult == null) {
				throw new Exception("Unable to get responce from inventory service");
			} 
						
			if(opResult.getResultCode().equals("FORGOT_PASSWORD_TOKEN_NOT_FOUND")){
				// If no token is found, create it
				postToken(details, forgotPassToken);
			} else {
				// Resend the token to the customer
				ForgotPasswordToken fpt = new ForgotPasswordToken();
				fpt.setToken(opResult.getResultValue().getToken());
				sendForgotPasswordEmail(details, fpt);
			}
		} catch(Exception ex) {
			logger.warn(ex.getMessage());
			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}
		return opResult;
	}
	
	private void sendForgotPasswordEmail(CustomerDetails customerDetails, ForgotPasswordToken token) {
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			String fullName;
			if (customerDetails.getFirstName() == null && customerDetails.getLastName() == null){
				fullName = "Heer / Mevrouw";
			} else {
				fullName = customerDetails.getFirstName() + " " + customerDetails.getLastName();
			}
			
			ForgotPasswordModel forgotPasswordModel = new ForgotPasswordModel(customerDetails.getCustomerId(), token.getToken(), fullName);
			
			String requestJson = new ObjectMapper().writeValueAsString(forgotPasswordModel);
			HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

			StringBuilder sBuilder = new StringBuilder(emailServiceURL).append(String.format(InventoryServiceActions.EMAIL, "PASSWORD", customerDetails.getEmail()));
			
			restTemplate.postForLocation(sBuilder.toString(), entity);
			
		} catch(Exception ex) {
			logger.error(ex.getMessage());
		}
	}
}