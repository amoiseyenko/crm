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

import com.aspiderngi.common.service.entity.Topup;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;

@Component
public class TopupService_Get {

	private static final Logger logger = LoggerFactory.getLogger(TopupService_Get.class);

	@Value("${inventory.service.url}")
	private String inventoryServiceUrl;

	public ArrayList<Topup> getHistory(Long customerId, Integer count) throws Exception {
		logger.info("Getting history for customerId: {}, count = {}", customerId, count);
		
		Long start = System.currentTimeMillis();
		OperationResultParam<ArrayList<Topup>> topups = null;
		
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			StringBuilder sBuilder = new StringBuilder(inventoryServiceUrl).append(InventoryServiceActions.TOPUP)
					.append("/").append(customerId);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			topups = restTemplate.exchange(builder.build().encode().toString(), 
					HttpMethod.GET, 
					entity,
					new ParameterizedTypeReference<OperationResultParam<ArrayList<Topup>>>(){}).getBody();

			if(topups.getResultValue() == null){
				throw new Exception(topups.getResultMessage());
			}

		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}

		return topups.getResultValue();
	}
}