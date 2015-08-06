package com.aspiderngi.prepaid.api.web.controller;

import java.net.ConnectException;
import java.text.MessageFormat;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspiderngi.artifacts.model.CustomerPatchOperation;
import com.aspiderngi.common.service.entity.CustomerConfirmationInfo;
import com.aspiderngi.common.service.entity.CustomerDetails;
import com.aspiderngi.common.service.entity.CustomerFullInfo;
import com.aspiderngi.common.service.entity.CustomerRegistrationInfo;
import com.aspiderngi.common.service.entity.User;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.prepaid.api.service.CustomerDetailsService_Get;
import com.aspiderngi.prepaid.api.service.CustomerFullInfoService_Get;
import com.aspiderngi.prepaid.api.service.CustomerPatchService_Create;
import com.aspiderngi.prepaid.api.service.CustomerService_Activate;
import com.aspiderngi.prepaid.api.service.CustomerService_Create;
import com.aspiderngi.prepaid.api.service.CustomerService_Get;
import com.aspiderngi.prepaid.api.web.controller.PromotionalRewardBean.PromotionalType;

@Component
public class CustomerBean implements CustomerController {

	@Autowired
	private CustomerService_Activate customerServiceActivate;

	@Autowired
	private CustomerService_Create customerServiceCreate;

	@Autowired
	private CustomerService_Get customerServiceGet;
	
	@Autowired
	private CustomerDetailsService_Get customerDetailsServiceGet;
	
	@Autowired
	CustomerPatchService_Create customerPatchServiceCreate;
	
	@Autowired
	PromotionalRewardBean promoBean;
	
	@Autowired
	private CustomerFullInfoService_Get customerFullInfoService;

	private static final Logger logger = LoggerFactory.getLogger(CustomerBean.class);

	@Override
	public OperationResultParam<CustomerConfirmationInfo> register(@RequestBody CustomerRegistrationInfo user) {
		logger.info("Registering user: {}", user);

		CustomerConfirmationInfo customerEntityInfo = null;

		try {
			customerEntityInfo = customerServiceCreate.execute(user);
			
			OperationResultParam<CustomerFullInfo> cfiResult = customerFullInfoService.execute(customerEntityInfo.getId());
			String msisdn = "31" + cfiResult.getResultValue().getMsisdn();
			
			promoBean.givePromotion(PromotionalType.SIM_ACTIVATION, customerEntityInfo.getId(), msisdn);
			
		} catch (ConnectException ex) {
			logger.error("InvMgmgt Server is not available.");
			return new OperationResultParam<CustomerConfirmationInfo>("InvMgmgt Server is not available", "SERVER_ERROR", null);
		} catch (Exception ex) {
			logger.warn("Unable to crteate customer");
			return new OperationResultParam<CustomerConfirmationInfo>("INVALID_REQUEST_ARGUMENTS",ex.getMessage() , null);
		}

		return new OperationResultParam<CustomerConfirmationInfo>("OK", "OK", customerEntityInfo);
	}

	@Override
	public OperationResult activate(@PathVariable Long customerId, @PathVariable String token) {
		logger.info("Activating id={}, token={}", customerId, token);
		try {
			return customerServiceActivate.execute(customerId, token);
		} catch(Exception exc) {
			logger.warn("Customer is not activated");
			return OperationResult.ERROR.setResultMessage(MessageFormat.format("Customer id={0} NOT Activated", customerId));
		}
	}

	@Override
	public OperationResultParam<CustomerDetails>  find() {
		final Long customerId = ((User)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCID();

		logger.info("Searching user for id={}", customerId);

		OperationResultParam<CustomerDetails> details = null; 
		try {
			details = customerDetailsServiceGet.getCustomerDetails(customerId);
		}catch(Exception exc) {
			return new OperationResultParam<CustomerDetails>("ERROR", exc.getMessage(), null);
		}
		return  details;
	}

//	@Override
//	public OperationResultParam<ArrayList<CustomerFullInfo>> search(String firstName, String email, String msisdn) {
//		logger.info("Searching for user by params: firstName = {}, email = {}, msisdn = {}", firstName, email, msisdn);
//
//		OperationResultParam<ArrayList<CustomerFullInfo>> details = null;
//		try{
//			details = customerServiceGet.search(firstName, email, msisdn);
//		}catch(Exception exc){
//			return new OperationResultParam<ArrayList<CustomerFullInfo>>("ERROR", exc.getMessage(), null);
//		}
//		
//		return details;
//	}

	@Override
	public OperationResult patch(@RequestBody CustomerPatchOperation patchParams) {
		final Long customerId = ((User)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCID();
		patchParams.setCustomerId(customerId);
		logger.info("Patching user", patchParams);
		OperationResult result = null;
		try{
			if(patchParams.getPath().toLowerCase().equals("email")) {
				OperationResultParam<CustomerDetails> details = customerServiceGet.getCustomerByEmail(patchParams.getValue());
				if (details.getResultValue() != null && details.getResultValue().getCustomerId() != customerId) {
					return OperationResult.ERROR.setResultMessage("EMAIL_ALREADY_REGISTERED");
				}
			}
			result = customerPatchServiceCreate.execute(patchParams);
			// Check if email was updated successfully (due to constraint)
			if(patchParams.getPath().toLowerCase().equals("email") && result.getResultCode().equals("ILLEGAL_ARGUMENTS") && result.getResultMessage().toLowerCase().contains("email_unique")) {
				return OperationResult.ERROR.setResultMessage("EMAIL_ALREADY_REGISTERED");
			}
			
		}catch(Exception exc){
			logger.warn("{}",exc);
			return OperationResult.ERROR.setResultMessage(exc.getMessage());
		}
		return result;
	}

	 

}