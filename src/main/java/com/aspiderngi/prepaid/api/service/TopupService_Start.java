package com.aspiderngi.prepaid.api.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
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
import com.aspiderngi.common.service.util.IdealServiceActions;
import com.aspiderngi.common.service.util.InventoryServiceActions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TopupService_Start {

	private static final Logger logger = LoggerFactory.getLogger(TopupService_Start.class);

	@Value("${ideal.service.url}")
	private String idealServiceUrl;

	@Value("${inventory.service.url}")
	private String inventoryServiceUrl;

	@Value("${ideal.service.return.url}")
	private String idealServiceReturnUrl;
		
	@Value("${ideal.payment.description}")
	private String paymentDescription;

	public OperationResultParam<String> execute(Topup topup) throws Exception {
		logger.info("Calling inventory service for topup: {}", topup);

		Long start = System.currentTimeMillis();
		OperationResultParam<String> opResult = null;

		try {
			topup.setPaymentId((new Date()).getTime()); // use unix timestamp as payment ID
			topup.setPaymentReference("PP-" + topup.getCustomerId());
			topup.setPaymentDescription(paymentDescription);
			topup.setReturnUrl(idealServiceReturnUrl);

			logger.info("Calling inventory service for topup #2 : {}", topup);
			
			StringBuilder sBuilder = new StringBuilder(idealServiceUrl).append(IdealServiceActions.TRANSACTIONS);
			String redirectUrl = startiDealRequest(sBuilder.toString(), topup);

			logger.info("Got redirect url {}", redirectUrl);
		
			// request iDeal service
			if(redirectUrl != null && !redirectUrl.isEmpty()) {
				storeTopup(redirectUrl, topup);
			}
 
			opResult = new OperationResultParam<String>("OK", "OK", redirectUrl);
		} catch(Exception ex) {
			logger.warn(ex.getMessage());
			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}

		return opResult;
	}


	private OperationResultParam<Long> storeTopup(String transaction, Topup topup) throws JsonProcessingException {
		/// parse transactionId from responceBody
		String trxid = "trxid";
		int beginIndex = transaction.indexOf(trxid) + trxid.length() + 1;
		transaction = transaction.substring(beginIndex);
		int endIndex = transaction.indexOf("&");
		String id = transaction.substring(0,endIndex);
		topup.setTransactionId(id);
		logger.info("Storing trxid {}",id);

		String url = new StringBuilder(inventoryServiceUrl).append(InventoryServiceActions.TOPUP).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		RestTemplate restTemplate = new RestTemplate();

		String topupJson = new ObjectMapper().writeValueAsString(topup);
		HttpEntity<String> entity = new HttpEntity<String>(topupJson,headers);

		return restTemplate.exchange(builder.build().encode().toString(), 
				HttpMethod.POST,
				entity,
				new ParameterizedTypeReference<OperationResultParam<Long>>(){}).getBody();
	}

	private String startiDealRequest(String url, Topup topup) throws JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

		String requestLoad = new ObjectMapper().writeValueAsString(topup);
		HttpEntity<String> entity = new HttpEntity<String>(requestLoad, headers);		

		String responceBody = restTemplate.exchange(builder.build().encode().toString(), 
				HttpMethod.POST,
				entity,
				String.class).getBody();

		return responceBody.substring(1, responceBody.length()-2); 
	}	
}