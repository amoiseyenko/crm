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
import org.springframework.web.util.UriComponentsBuilder;

import com.aspiderngi.common.service.entity.CustomerAddress;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.util.InventoryServiceActions;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomerAddressService_Put {	

	@Value("${inventory.service.url}")
	private String inventoryServiceURL;
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerAddressService_Put.class);
	
	public CustomerAddressService_Put(){
		logger.trace("CustomerAddressService_Put.constructor");
	}
	
	public OperationResult execute(Long customerId,CustomerAddress newAddress) throws Exception{

		OperationResult result = null;
		logger.trace("CustomerAddressService.execute");
		
		Long start = System.currentTimeMillis();
	 
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters()
    			.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.ADDRESS)
					.append("/").append(customerId.toString());
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
			
			String requestJson = new ObjectMapper().writeValueAsString(newAddress);
			HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
			
			result = restTemplate.exchange(builder.build().encode().toString(), HttpMethod.PUT,entity,
					OperationResult.class).getBody();
			if(result == null){
				throw new Exception("Unable to get responce from inventory service");
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