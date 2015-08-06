package com.aspiderngi.prepaid.api.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.aspiderngi.common.service.entity.CustomerFullInfo;
import com.aspiderngi.common.service.entity.CustomerNote;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;

@Service
public class CustomerNotesService_Get {

	Logger logger = LoggerFactory.getLogger(CustomerNotesService_Get.class);

	@Value("${inventory.service.url}")
	private String inventoryServiceURL;

	public OperationResultParam<List<CustomerNote>> getNotes(Long customerId,Integer position, Integer itemsCount) throws Exception {
		OperationResultParam<List<CustomerNote>> notes = null;

		logger.info("Getting {} customer notes for customerId {}", itemsCount,customerId);

		Long start = System.currentTimeMillis();

		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			StringBuilder sBuilder = new StringBuilder(inventoryServiceURL)
						.append(InventoryServiceActions.NOTES).append("/").append(customerId);
					

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString())
					.queryParam("position", position).queryParam("itemsCount", itemsCount);

			HttpEntity<String> entity = new HttpEntity<String>(headers);

			notes = restTemplate.exchange(builder.build().encode().toString(), 
					HttpMethod.GET, 
					entity,
					new ParameterizedTypeReference<OperationResultParam<List<CustomerNote>>>(){}).getBody();

			if(notes == null){
				throw new Exception("Unable to get responce from inventory service");
			}else if(notes.getResultValue() == null){
				throw new Exception(notes.getResultCode());
			}
		} catch(Exception ex) {
			logger.warn(ex.getMessage());
			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}

		return notes;

	}

}
