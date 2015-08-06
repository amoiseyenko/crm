package com.aspiderngi.prepaid.api.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.util.InventoryServiceActions;

@Component
public class PasswordManagementService_Put {
	@Value("${inventory.service.url}")
	private String inventoryServiceURL;
	
	private static final Logger logger = LoggerFactory.getLogger(PasswordManagementService_Put.class);
	
	public OperationResult updateForgotPassTokenActDate(Long tokenId) throws Exception{
		OperationResult opResult = null;
		Long start = System.currentTimeMillis();

		try {
			RestTemplate restTemplate = new RestTemplate();
						
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.PASSWORD)
					.append("/").append(tokenId.toString());
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
			
			HttpEntity<String> entity = new HttpEntity<String>(headers);

			opResult = restTemplate.exchange(builder.build().encode().toString(), HttpMethod.PUT,
					entity,   OperationResult.class).getBody();

			if(opResult == null){
				throw new Exception("Unable to get responce from inventory service");
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
