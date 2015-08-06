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

import com.aspiderngi.common.service.entity.UsageDetails;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;

@Component
public class UsageService_Get {

	private static final Logger logger = LoggerFactory.getLogger(UsageService_Get.class);

	@Value("${inventory.service.url}")
	private String inventoryServiceUrl;

	public ArrayList<UsageDetails> get(Long subscriptionId, Long position, Integer count) throws Exception {
		logger.info("Getting usage for subscription: {}, count = {}, position = {}", subscriptionId, count, count);
		
		Long start = System.currentTimeMillis();
		OperationResultParam<ArrayList<UsageDetails>> usages = null;
		
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			StringBuilder sBuilder = new StringBuilder(inventoryServiceUrl).append(InventoryServiceActions.USAGE)
					.append("/").append(subscriptionId)
					.append("?").append("position=").append(position).append("&count=").append(count);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			usages = restTemplate.exchange(builder.build().encode().toString(), 
					HttpMethod.GET, 
					entity,
					new ParameterizedTypeReference<OperationResultParam<ArrayList<UsageDetails>>>(){}).getBody();

			if(usages.getResultValue() == null){
				throw new Exception(usages.getResultMessage());
			}
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}

		return usages.getResultValue();
	}
}