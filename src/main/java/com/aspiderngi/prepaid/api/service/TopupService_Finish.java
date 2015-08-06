package com.aspiderngi.prepaid.api.service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.aspiderngi.common.service.entity.Topup;
import com.aspiderngi.common.service.entity.enums.TopupState;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.IdealServiceActions;
import com.aspiderngi.common.service.util.InventoryServiceActions;
import com.aspiderngi.prepaid.api.ws.NCCPICCSCD3Client;

@Component
public class TopupService_Finish {

	private static final Logger logger = LoggerFactory.getLogger(TopupService_Finish.class);

	@Autowired
	private NCCPICCSCD3Client nccClient;

	@Value("${ideal.service.url}")
	private String idealServiceUrl;
	
	@Value("${inventory.service.url}")
	private String inventoryServiceUrl;
	
	public OperationResultParam<Topup> execute(Long customerId, String transactionId) throws Exception {
		logger.info("Finishing topup for transaction: {}", transactionId);
		
		Long start = System.currentTimeMillis();
		OperationResultParam<Topup> opResult = null;

		try {
			// 1. get status of transaction from iDEAL service
			Integer transactionStatus = Integer.parseInt(getTransactionStatus(transactionId));
			
			logger.info("Transaction status returned from iDeal is: {}", transactionStatus.intValue());
			
			// 2. update DB
			opResult = updateTransactionStatus(transactionId, transactionStatus);

			return opResult;
			
		} catch(Exception ex) {
			logger.warn(ex.getMessage());
			
			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}
	}
	
	private OperationResultParam<Topup> updateTransactionStatus(String transactionId, Integer transactionStatus) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		StringBuilder sBuilder = new StringBuilder(inventoryServiceUrl).append(InventoryServiceActions.TOPUP)
				.append("/state")
				.append("/" + (transactionStatus == 500 ? TopupState.SUCCESS.getId() : TopupState.FAILED.getId()))  
				.append("/" + transactionId);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		return restTemplate.exchange(builder.build().encode().toString(), 
				HttpMethod.PUT,
				entity,
				new ParameterizedTypeReference<OperationResultParam<Topup>>(){}).getBody();
	}

	private String getTransactionStatus(String transactionId) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		StringBuilder sBuilder = new StringBuilder(idealServiceUrl).append(IdealServiceActions.TRANSACTIONS)
				.append("/").append(transactionId).append("/state");

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return restTemplate.exchange(builder.build().encode().toString(),
				HttpMethod.GET,
				entity, String.class).getBody();
	}	
}