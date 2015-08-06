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

import com.aspiderngi.common.service.entity.CustomerDetails;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;

@Component
public class CustomerDetailsService_Get {

	private static final Logger logger = LoggerFactory.getLogger(CustomerDetailsService_Get.class);
	
	@Value("${inventory.service.url}")
	private String inventoryServiceURL;
	
	public CustomerDetailsService_Get() {
		logger.info("CustomerDetailsService_Get.ctor");
	}
	
	public OperationResultParam<CustomerDetails> get(Long customerId) throws Exception{
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
			
			OperationResultParam<CustomerDetails> opResultCustomerDetails = restTemplate.exchange(builder.build().encode().toString(), 
					HttpMethod.GET, 
					entity,
					new ParameterizedTypeReference<OperationResultParam<CustomerDetails>>(){}).getBody();
			if(opResultCustomerDetails == null){
				throw new Exception("Unable to get responce from inventory service");
			}else if(opResultCustomerDetails.getResultValue() == null){
				throw new Exception(opResultCustomerDetails.getResultCode());
			}
		} catch(Exception ex) {
			logger.warn(ex.getMessage());
			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}
		
		return opResult;		
	}
	
	public OperationResultParam<CustomerDetails> getCustomerDetails(Long customerId) throws Exception{
		logger.info("getting customer details for id={}",customerId);
		
		Long start = System.currentTimeMillis();
		OperationResultParam<CustomerDetails> result = null;

		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.CUSTOMERS)
					.append("/").append(customerId.toString());
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
				
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			
			result = restTemplate.exchange(builder.build().encode().toString(), 
					HttpMethod.GET, 
					entity,
					new ParameterizedTypeReference<OperationResultParam<CustomerDetails>>(){}).getBody();
			if(result == null){
				throw new Exception("Unable to get responce from inventory service");
			}else if(result.getResultValue() == null) {
				throw new Exception(result.getResultCode());
			}
		} catch(Exception ex) {
			logger.warn(ex.getMessage());
			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}
		
		return result;		
	}
}