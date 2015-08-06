package com.aspiderngi.prepaid.api.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.aspiderngi.common.service.entity.Balance;
import com.aspiderngi.common.service.entity.CustomerFullInfo;
import com.aspiderngi.common.service.entity.User;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.prepaid.api.service.BalanceQuery_Get;
import com.aspiderngi.prepaid.api.service.CustomerFullInfoService_Get;

@Component
public class BalanceBean implements BalanceController{

	Logger logger = LoggerFactory.getLogger(BalanceBean.class);
	
	@Autowired
	BalanceQuery_Get balanceService;
	
	@Autowired
	private CustomerFullInfoService_Get customerFullInfoService;
	
	@Override
	public OperationResultParam<Balance> get() {
		final Long customerId = ((User)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCID();		 
		
		try{
			OperationResultParam<CustomerFullInfo> cfiResult = customerFullInfoService.execute(customerId);
			String msisdn = "31" + cfiResult.getResultValue().getMsisdn();
						
			return balanceService.execute(msisdn);
		}catch(Exception exc){
			logger.error("{}",exc);
			return new OperationResultParam<Balance>("ERROR",exc.getMessage(),null);
		}
	}

}
