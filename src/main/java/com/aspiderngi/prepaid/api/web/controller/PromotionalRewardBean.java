package com.aspiderngi.prepaid.api.web.controller;

import java.math.BigDecimal;
import java.util.Date;

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
import com.aspiderngi.common.service.entity.enums.TopupType;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.common.service.util.InventoryServiceActions;
import com.aspiderngi.prepaid.api.service.OCSTopupService_Put;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PromotionalRewardBean {

	Logger logger = LoggerFactory.getLogger(PromotionalRewardBean.class);
	
	public static enum PromotionalType { IDEAL_TOPUP_FIRST, IDEAL_TOPUP_SECOND, NAW_REGISTRATION, SIM_ACTIVATION };

	@Autowired
	private OCSTopupService_Put ocsTopupService;
	
	@Value("${inventory.service.url}")
	private String inventoryServiceUrl;
	
	@Value("${topup.award.sim_activation}")
	private String awardSimActivation;
	
	@Value("${topup.award.first_topup}")
	private String awardFirstTopup;
	
	@Value("${topup.award.second_topup}")
	private String awardSecondTopup;
	
	@Value("${topup.award.third_topup}")
	private String awardThirdTopup;
	
	@Value("${topup.award.naw_registration}")
	private String awardNawRegistration;

	public OperationResult givePromotion(PromotionalType type, long customerId, String msisdn) {
		logger.info("Doing promotional topup for customerId: {}", customerId);
		
		try {			
			Topup topup = new Topup();
			boolean doOcsTopup = false;
			double awardSimActivationDbl = Double.parseDouble(awardSimActivation);
			double awardFirstTopupDbl = Double.parseDouble(awardFirstTopup);
			double awardSecondTopupDbl = Double.parseDouble(awardSecondTopup);
			double awardNawRegistrationDbl = Double.parseDouble(awardNawRegistration);
			double amount;
			
			switch (type) {
			case SIM_ACTIVATION:
				amount = awardSimActivationDbl;
				break;
			
			case IDEAL_TOPUP_FIRST:
				doOcsTopup = true;
				amount = awardFirstTopupDbl;
				break;
				
			case IDEAL_TOPUP_SECOND:
				doOcsTopup = true;
				amount = awardSecondTopupDbl;
				break;
							
			case NAW_REGISTRATION:
				doOcsTopup = true;
				amount = awardNawRegistrationDbl;
				break;
				
			default:
				return new OperationResult("OK", "OK");
			}
			
			// Do topup on OCS
			if (doOcsTopup) {
				ocsTopupService.execute(BigDecimal.valueOf(amount), msisdn);
			}
			
			// Store topup
			topup.setDate(new Date());
			topup.setStatus(TopupState.SUCCESS);
			topup.setTopUpType(TopupType.PROMO);
			topup.setCustomerId(customerId);
			topup.setAmount(amount);			
			storeTopup(topup);
			
		} catch(Exception ex) {
			return new OperationResult("ERROR", ex.getMessage());
		}
		
		return new OperationResult("OK", "OK");
	}

	
	private OperationResultParam<Long> storeTopup(Topup topup) throws JsonProcessingException {
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
}
