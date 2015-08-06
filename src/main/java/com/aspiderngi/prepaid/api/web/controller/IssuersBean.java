package com.aspiderngi.prepaid.api.web.controller;


import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspiderngi.common.service.entity.IssuersCountry;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.prepaid.api.service.IssuersService_Get;

@Component
public class IssuersBean implements IssuersController{

	Logger logger = LoggerFactory.getLogger(IssuersBean.class);
	
	@Autowired
	IssuersService_Get issuersService;
	
	@Override
	public OperationResultParam<ArrayList<IssuersCountry>> get() {
		
		ArrayList<IssuersCountry> issuers = null; 
		try{
			issuers = issuersService.execute();
		}catch(Exception exc){
			logger.error("{}",exc);
			return new OperationResultParam<ArrayList<IssuersCountry>>("ERROR",exc.getMessage(),null);
		}
		return new OperationResultParam<ArrayList<IssuersCountry>>("OK","OK",issuers);
	}

}
