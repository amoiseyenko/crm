package com.aspiderngi.prepaid.api.service;

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
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.aspiderngi.common.service.entity.CustomerDetails;
import com.aspiderngi.common.service.entity.CustomerFullInfo;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;

@Component
public class CustomerService_Get {

	private static final Logger logger = LoggerFactory.getLogger(CustomerService_Get.class);
	
	@Value("${inventory.service.url}")
	private String inventoryServiceURL;
	
	public CustomerService_Get() {
		logger.debug("CustomerService.ctor");
	}
	
	public OperationResultParam<CustomerDetails> findCustomerDetails(Long customerId) throws Exception{
		logger.info("getting customer details for id={}",customerId);
		
		Long start = System.currentTimeMillis();
		OperationResultParam<CustomerDetails> opResult = null;

		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.CUSTOMERS)
					.append("/").append(customerId.toString());
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
				
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			
			opResult = restTemplate.exchange(builder.build().encode().toString(), HttpMethod.GET, entity,
					new ParameterizedTypeReference<OperationResultParam<CustomerDetails>>(){}).getBody();
			if(opResult == null){
				throw new Exception("Unable to get responce from inventory service");
			}else if(opResult.getResultValue() == null){
				throw new Exception(opResult.getResultCode());
			}
		} catch(Exception ex) {
			logger.warn(ex.getMessage());
			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}
		
		return opResult;		
	}
	

	public OperationResultParam<ArrayList<CustomerFullInfo>> search(String firstName, String email, String msisdn) throws Exception {
		logger.info("Searching for customer, credentials: firstName: {}, email: {},msisdn:{}",firstName,email,msisdn);
		
		Long start = System.currentTimeMillis();
		OperationResultParam<ArrayList<CustomerFullInfo>> opResult = null;

		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.CUSTOMERS)
					.append("?").append("firstName=").append(firstName).append("&email=").append(email).append("&msisdn=").append(msisdn);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
				
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			
			opResult = restTemplate.exchange(builder.build().encode().toString(), HttpMethod.GET, entity,
					new ParameterizedTypeReference<OperationResultParam<ArrayList<CustomerFullInfo>>>(){}).getBody();
			if(opResult == null){
				throw new Exception("Unable to get responce from inventory service");
			}else if(opResult.getResultValue() == null){
				throw new Exception(opResult.getResultCode());
			}
		} catch(Exception ex) {
			logger.warn(ex.getMessage());
			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}
		return opResult;
	}
	
	public OperationResultParam<CustomerDetails> getCustomerByEmail(String email) throws Exception{
		logger.info("Getting customer details by email={}", email);
		
		Long start = System.currentTimeMillis();
		OperationResultParam<CustomerDetails> opResult = null;

		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.CUSTOMERS)
					.append("?email=").append(email);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
				
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			
			opResult = restTemplate.exchange(builder.build().encode().toString(), HttpMethod.GET, entity,
					new ParameterizedTypeReference<OperationResultParam<CustomerDetails>>(){}).getBody();
			if(opResult == null){
				throw new Exception("Unable to get response from inventory service");
			}
		} catch(Exception ex) {
			logger.warn(ex.getMessage());
			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}
		
		return opResult;		
	}
}