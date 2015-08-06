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
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;

@Component
public class CustomerAddressService_Get {

	private static final Logger logger = LoggerFactory.getLogger(CustomerAddressService_Get.class);

	@Value("${inventory.service.url}")
	private String inventoryServiceURL;
	
	public CustomerAddressService_Get() {
		logger.debug("CustomerAddressService.ctor");
	}

	public OperationResultParam<CustomerAddress> execute(Long id) throws Exception{
		logger.trace("CustomerAddressService.execute");
		
		Long start = System.currentTimeMillis();
		OperationResultParam<CustomerAddress> opResult = null;

		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL).append(InventoryServiceActions.ADDRESS)
					.append("/").append(id.toString());
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());

			HttpEntity<String> entity = new HttpEntity<String>(headers);
			
			opResult = restTemplate.exchange(builder.build().encode().toString(), HttpMethod.GET, entity,
					new ParameterizedTypeReference<OperationResultParam<CustomerAddress>>(){}).getBody();
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