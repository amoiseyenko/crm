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
public class TopupService_Count {

	private static final Logger logger = LoggerFactory.getLogger(TopupService_Count.class);

	@Value("${inventory.service.url}")
	private String inventoryServiceUrl;

	public int getSuccessfulCount(Long customerId) throws Exception {
		logger.info("Getting topup count for customerId: {}, count = {}", customerId);
		
		Long start = System.currentTimeMillis();
		OperationResultParam<Integer> count = null;
		
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			StringBuilder sBuilder = new StringBuilder(inventoryServiceUrl).append(InventoryServiceActions.TOPUP)
					.append("/").append(customerId).append("/count/5");

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			count = restTemplate.exchange(builder.build().encode().toString(), 
					HttpMethod.GET, 
					entity,
					new ParameterizedTypeReference<OperationResultParam<Integer>>(){}).getBody();

			if(count.getResultValue() == null){
				throw new Exception(count.getResultMessage());
			}

		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}

		return count.getResultValue().intValue();
	}
}