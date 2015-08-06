package com.aspiderngi.prepaid.api.service;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.aspiderngi.artifacts.model.RegistrationModel;
import com.aspiderngi.common.service.entity.CustomerConfirmationInfo;
import com.aspiderngi.common.service.entity.CustomerRegistrationInfo;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomerService_Create {

	private static final Logger logger = LoggerFactory.getLogger(CustomerService_Create.class);

	@Value("${email.service.url}")
	private String emailServiceURL;

	@Value("${inventory.service.url}")
	private String customerServiceURL;


	public CustomerService_Create() {
		logger.trace("CustomerService_Create.ctor");
	}

	public CustomerConfirmationInfo execute(CustomerRegistrationInfo customerDetails) throws Exception {
		logger.trace("CustomerService_Create.execute");

		Long start = System.currentTimeMillis();

		customerDetails.setPassword((new StandardPasswordEncoder()).encode(customerDetails.getPassword()));

		OperationResultParam<CustomerConfirmationInfo> entityInfo = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters()
	        	.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			String requestJson = new ObjectMapper().writeValueAsString(customerDetails);
			HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
						
			entityInfo = restTemplate.exchange(customerServiceURL + InventoryServiceActions.CUSTOMERS,HttpMethod.POST,
					entity, new ParameterizedTypeReference<OperationResultParam<CustomerConfirmationInfo>>(){}).getBody();

			if(entityInfo == null){
				throw new Exception("Unable to get responce from inventory service");
			} else if(entityInfo.getResultValue() == null){
				if (entityInfo.getResultCode().equals("CONSTRAINT_VIOLATION") && entityInfo.getResultMessage().toLowerCase().contains("email_unique")){
					throw new Exception("EMAIL_ALREADY_REGISTERED");
				} else {
					throw new Exception(entityInfo.getResultCode());
				}
			}
			
			sendRegistrationEmail(customerDetails.getEmail(), entityInfo.getResultValue());
		} catch(Exception ex) {
			logger.warn(ex.getMessage());
			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}

		return entityInfo.getResultValue();
	}
	
	private void sendRegistrationEmail(String to, CustomerConfirmationInfo customerEntityInfo) {
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			RegistrationModel registrationModel = new RegistrationModel(customerEntityInfo.getId(), customerEntityInfo.getConfirmationToken());
			String requestJson = new ObjectMapper().writeValueAsString(registrationModel);
			HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

			StringBuilder sBuilder = new StringBuilder(emailServiceURL).append(String.format(InventoryServiceActions.EMAIL, "REGISTER", to));
			
			restTemplate.postForLocation(sBuilder.toString(), entity);
			
		} catch(Exception ex) {
			logger.error(ex.getMessage());
		} finally {
		}
	} 
}