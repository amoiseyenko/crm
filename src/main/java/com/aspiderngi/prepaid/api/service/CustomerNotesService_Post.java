package com.aspiderngi.prepaid.api.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.aspiderngi.common.service.entity.CustomerNote;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.util.InventoryServiceActions;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomerNotesService_Post {

	Logger logger = LoggerFactory.getLogger(CustomerNotesService_Post.class);
	
	@Value("${inventory.service.url}")
	private String inventoryServiceURL;
	
	public OperationResult postNote(CustomerNote note,Long customerId) throws Exception{
		logger.info("Posting note {} for customer id {}",note,customerId);
		OperationResult opResult = null;
		Long start = System.currentTimeMillis();
		
		try{
				RestTemplate restTemplate = new RestTemplate();

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				
				StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.NOTES);
				
				UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
				String requestJson = new ObjectMapper().writeValueAsString(note);
				
				HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
				
				opResult = restTemplate.exchange(builder.build().encode().toString(), HttpMethod.POST,entity,
						OperationResult.class).getBody();
				
				if(opResult == null) {
					throw new Exception("Unable to get responce from inventory service");
				} else if(!opResult.getResultCode().equals("OK")){
					throw new Exception(opResult.getResultMessage());
				}
			
		}catch(Exception exc){
			logger.error("{}",exc);
			throw exc;
		}finally{
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}
		
		return null;
	}
	
}
