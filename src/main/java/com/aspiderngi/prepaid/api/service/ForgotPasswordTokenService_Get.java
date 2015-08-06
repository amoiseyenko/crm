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

import com.aspiderngi.common.service.entity.CustomerDetails;
import com.aspiderngi.common.service.entity.PasswordChange;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;

@Component
public class ForgotPasswordTokenService_Get {

	private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordTokenService_Get.class);
	
	@Value("${inventory.service.url}")
	private String inventoryServiceURL;
	
	public ForgotPasswordTokenService_Get() {
		logger.debug("ForgotPasswordTokenService_Get");
	}
	
	public OperationResultParam<PasswordChange> getPasswordTokenByCustomerID(Long customerId, String token) throws Exception {
		logger.info("Getting forgot password token: {} for customer id={}", token, customerId);
		
		Long start = System.currentTimeMillis();
		OperationResultParam<PasswordChange> opResult = null;

		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.PASSWORD)
					.append("/").append(customerId.toString()).append("/").append(token);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
				
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			
			opResult = restTemplate.exchange(builder.build().encode().toString(), HttpMethod.GET, entity,
					new ParameterizedTypeReference<OperationResultParam<PasswordChange>>(){}).getBody();
			if(opResult == null){
				throw new Exception("Unable to get responce from inventory service");
			}else if(opResult.getResultValue() == null){
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
