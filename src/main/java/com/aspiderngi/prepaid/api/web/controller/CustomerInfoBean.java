package com.aspiderngi.prepaid.api.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.aspiderngi.common.service.entity.CustomerFullInfo;
import com.aspiderngi.common.service.entity.User;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.prepaid.api.service.CustomerFullInfoService_Get;

@Component
public class CustomerInfoBean implements CustomerInfoController{

	private static final Logger logger = LoggerFactory.getLogger(CustomerInfoController.class);
	
	
	@Autowired
	CustomerFullInfoService_Get customerFullInfoGet;
	
	@Override
	public OperationResultParam<CustomerFullInfo> getDetails() {
		final Long customerId = ((User)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCID();
		
		logger.info("Retrieveing Customer Details for id={}", customerId);
		OperationResultParam<CustomerFullInfo> customerDetails; 
		
		try{
			 customerDetails =  customerFullInfoGet.execute(customerId);
		}catch(Exception exc){
			logger.error("{}",exc);
			return new OperationResultParam<CustomerFullInfo>("ERROR", exc.getMessage(), null);
		}
		return customerDetails;
	}

}
