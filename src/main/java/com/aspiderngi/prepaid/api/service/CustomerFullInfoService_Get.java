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

import com.aspiderngi.common.service.entity.CustomerFullInfo;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;

@Component
public class CustomerFullInfoService_Get {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerFullInfoService_Get.class);
	
	public CustomerFullInfoService_Get (){}
	
	@Value("${inventory.service.url}")
	private String inventoryServiceURL;
	
	public OperationResultParam<CustomerFullInfo> execute(Long customerId) throws Exception {
		logger.info("Getting full customer details for customerId {}", customerId);
		
		Long start = System.currentTimeMillis();
		OperationResultParam<CustomerFullInfo> opResultCustomerDetails = null;

		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.CUSTOMERS)
					.append("/").append(customerId.toString()).append(InventoryServiceActions.CUSTOMER_FULL_DETAILS);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
				
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			
			  opResultCustomerDetails = restTemplate.exchange(builder.build().encode().toString(), 
					HttpMethod.GET, 
					entity,
					new ParameterizedTypeReference<OperationResultParam<CustomerFullInfo>>(){}).getBody();

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

		return opResultCustomerDetails;	
	}	
}