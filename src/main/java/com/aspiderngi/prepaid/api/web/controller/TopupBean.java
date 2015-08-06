package com.aspiderngi.prepaid.api.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspiderngi.common.service.entity.CustomerFullInfo;
import com.aspiderngi.common.service.entity.Topup;
import com.aspiderngi.common.service.entity.User;
import com.aspiderngi.common.service.entity.enums.TopupState;
import com.aspiderngi.common.service.entity.enums.TopupType;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.prepaid.api.service.CustomerFullInfoService_Get;
import com.aspiderngi.prepaid.api.service.OCSTopupService_Put;
import com.aspiderngi.prepaid.api.service.TopupService_Count;
import com.aspiderngi.prepaid.api.service.TopupService_Finish;
import com.aspiderngi.prepaid.api.service.TopupService_Get;
import com.aspiderngi.prepaid.api.service.TopupService_Start;
import com.aspiderngi.prepaid.api.ws.NCCPICCSCD3Client;

@Component
public class TopupBean implements TopupController {

	private static final Logger logger = LoggerFactory.getLogger(TopupBean.class);

	@Autowired
	private TopupService_Count topupServiceCount;
	
	@Autowired
	private TopupService_Start topUpServiceStart;
 
	@Autowired
	private TopupService_Get topupServiceGet;
	
	@Autowired
	private TopupService_Finish topUpServiceFinish;
	
	@Autowired
	private CustomerFullInfoService_Get customerFullInfoService;
	
	@Autowired
	private OCSTopupService_Put ocsTopupService;
	
	@Autowired
	private NCCPICCSCD3Client nccClient;
	
	@Autowired
	private PromotionalRewardBean promoBean;

	@Override
	public OperationResultParam<String> start(@RequestBody Topup topup) {
		OperationResultParam<String> operationResult = new OperationResultParam<String>();

		final Long customerId = ((User)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCID();
		logger.info("Starting topup for customerId: {}", customerId);
		
		topup.setCustomerId(customerId);
		
		try {
			topup.setTopUpType(TopupType.NORMAL);
			operationResult = topUpServiceStart.execute(topup);
		} catch(Exception exc) {
			logger.warn("{}", exc);
			return new OperationResultParam<String>("Internal server error", "ERROR", null);
		}

		return operationResult;
	}
	
	@Override
	public OperationResult finish(@PathVariable String transactionId) {
		final Long customerId = ((User)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCID();
		Topup topup;
		
		logger.info("Finidhing topup for customerId: {} and transaction: {}", customerId, transactionId);

		OperationResultParam<Topup> result;

		try {
			result = topUpServiceFinish.execute(customerId, transactionId);
			topup = result.getResultValue();
			
			if (topup == null) {
				throw new Exception("Couldn't get transaction");
			}
			if (topup.getStatus().getId() != TopupState.SUCCESS.getId()) {
				throw new Exception("Transaction failed");
			}
									
			OperationResultParam<CustomerFullInfo> cfiResult = customerFullInfoService.execute(customerId);
			String msisdn = "31" + cfiResult.getResultValue().getMsisdn();

			ocsTopupService.execute(BigDecimal.valueOf(topup.getAmount()), msisdn);
			
			switch (topupServiceCount.getSuccessfulCount(customerId)) {
			case 1:
				promoBean.givePromotion(PromotionalRewardBean.PromotionalType.IDEAL_TOPUP_FIRST, customerId, msisdn);
				break;
			case 2:
				promoBean.givePromotion(PromotionalRewardBean.PromotionalType.IDEAL_TOPUP_SECOND, customerId, msisdn);
				break;		
			}
			
		} catch(Exception exc) {
			logger.warn("{}", exc);
			
			return new OperationResult(exc.getMessage(), "ERROR");
		}

		return result;
	}

	@Override
	public OperationResultParam<ArrayList<Topup>> getHistory() {
		final Long customerId = ((User)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCID();
		
		logger.info("Getting topup history for customerId: {}", customerId);
		ArrayList<Topup> topups;

		try {
			topups = topupServiceGet.getHistory(customerId, 5);
		} catch(Exception exc) {
			logger.warn("{}", exc);
			return new OperationResultParam<ArrayList<Topup>>("Server internal error", "ERROR", null);
		}

		return new OperationResultParam<>("OK", "OK", topups);
	}
}