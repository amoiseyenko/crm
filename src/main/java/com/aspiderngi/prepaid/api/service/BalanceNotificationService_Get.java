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

import com.aspiderngi.common.service.entity.CustomerAddress;
import com.aspiderngi.common.service.entity.LowBalanceNotification;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;

@Component
public class BalanceNotificationService_Get {

	private static final Logger logger = LoggerFactory.getLogger(BalanceNotificationService_Get.class);
	
	@Value("${inventory.service.url}")
	private String inventoryServiceURL;
	
	public BalanceNotificationService_Get() {
		logger.debug("BalanceNotificationService.ctor");
	}
	
	public OperationResultParam<LowBalanceNotification> execute(Long id) throws Exception {
		logger.trace("BalanceNotificationService.execute");
		
		Long start = System.currentTimeMillis();
		OperationResultParam<LowBalanceNotification> opResult = null;
		
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.SETTINGS)
					.append("/").append(id.toString());
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());

			HttpEntity<String> entity = new HttpEntity<String>(headers);
			
			opResult = restTemplate.exchange(builder.build().encode().toString(), HttpMethod.GET, entity,
					new ParameterizedTypeReference<OperationResultParam<LowBalanceNotification>>(){}).getBody();
			if(opResult == null) {
				throw new Exception("Unable to get response from inventory service");
			} else if(opResult.getResultValue() == null) {
				throw new Exception(opResult.getResultCode());
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