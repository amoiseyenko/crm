package com.aspiderngi.prepaid.api.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspiderngi.common.service.entity.CustomerAddress;
import com.aspiderngi.common.service.entity.CustomerFullInfo;
import com.aspiderngi.common.service.entity.User;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.prepaid.api.service.CustomerAddressService_Get;
import com.aspiderngi.prepaid.api.service.CustomerAddressService_Put;
import com.aspiderngi.prepaid.api.service.CustomerFullInfoService_Get;

@Component
public class AddressesBean implements AddressesController{

	private static final Logger logger = LoggerFactory.getLogger(AddressesBean.class);
	
	@Autowired
	private CustomerAddressService_Get customerAddressService ;	
	
	@Autowired
	private CustomerAddressService_Put customerAddressServicePut;
	
	@Autowired
	private CustomerFullInfoService_Get customerFullInfoService;
	
	@Autowired
	private PromotionalRewardBean promoBean;
	
	@Override
	public OperationResultParam<CustomerAddress> getCustomerAddress(){
		final Long customerId = ((User)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCID();
		
		logger.info("Getting address for CustomerId={}", customerId);
		
		OperationResultParam<CustomerAddress> customerAddress = null;
		try {
			customerAddress = customerAddressService.execute(customerId);
		} catch (Exception exc) {
			logger.warn("{}",exc);
			return  new OperationResultParam<CustomerAddress>(exc.getMessage(), "ERORR", null);
		}

		return customerAddress;
	}

	@Override
	public OperationResult updateCustomerAddress(@RequestBody CustomerAddress customerAddress) {

		final Long customerId = ((User)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCID();
		
		logger.info("Update address for CustomerId={}", customerId);
		
		OperationResult result = null ;
		
		try {
			OperationResultParam<CustomerAddress> oldCustomerAddress = customerAddressService.execute(customerId);
		
			if (oldCustomerAddress.getResultValue() == null)
				return oldCustomerAddress;
			
		 	result = customerAddressServicePut.execute(customerId, customerAddress);
		 	
			OperationResultParam<CustomerFullInfo> cfiResult = customerFullInfoService.execute(customerId);
			String msisdn = "31" + cfiResult.getResultValue().getMsisdn();

			if (oldCustomerAddress.getResultValue().getAddress() == null) {
				promoBean.givePromotion(PromotionalRewardBean.PromotionalType.NAW_REGISTRATION, customerId, msisdn);
			}
		} catch(Exception exc){
			return new OperationResult(exc.getMessage(), "ERROR");
		}

		return result;
	}	
}