package com.aspiderngi.prepaid.api.service;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.aspiderngi.artifacts.model.CustomerPatchOperation;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.util.InventoryServiceActions;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomerPatchService_Create {

	@Value("${inventory.service.url}")
	private String inventoryServiceURL;
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerPatchService_Create.class);
	
	public OperationResult execute(CustomerPatchOperation operation) throws Exception{
		OperationResult opResult = null;
		Long start = System.currentTimeMillis();

		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters()
        		.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			String requestJson = new ObjectMapper().writeValueAsString(operation);
			HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

			opResult = restTemplate.exchange(inventoryServiceURL + InventoryServiceActions.CUSTOMERS,HttpMethod.PUT,
					entity,   OperationResult.class).getBody();

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
