package com.aspiderngi.prepaid.api.web.controller;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspiderngi.artifacts.model.CustomerPatchOperation;
import com.aspiderngi.common.service.entity.CustomerDetails;
import com.aspiderngi.common.service.entity.ForgotPasswordToken;
import com.aspiderngi.common.service.entity.PasswordChange;
import com.aspiderngi.common.service.entity.User;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.prepaid.api.service.CustomerPatchService_Create;
import com.aspiderngi.prepaid.api.service.CustomerService_Get;
import com.aspiderngi.prepaid.api.service.ForgotPasswordTokenService_Get;
import com.aspiderngi.prepaid.api.service.PasswordManagementService_Put;
import com.aspiderngi.prepaid.api.service.PasswordManagementService_Post;

@Component
public class PasswordManagementBean implements PasswordManagementController {

	@Autowired
	private CustomerService_Get customerServiceGet;
	
	@Autowired
	private PasswordManagementService_Post passwordManagementServicePost;
	
	@Autowired 
	private ForgotPasswordTokenService_Get forgotPasswordTokenServiceGet;
	
	@Autowired
	CustomerPatchService_Create customerPatchServiceCreate;
	
	@Autowired
	PasswordManagementService_Put passwordManagementServicePatch;
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerBean.class);
	
	@Override
	public OperationResult forgotPassword(@RequestParam String email) {
		logger.info("Executing forgot password for email={}", email);
				
		OperationResultParam<CustomerDetails> details = null; 
		try {
			// Verify that email address exists
			logger.info("Searching user by email={}", email);
			
			details = customerServiceGet.getCustomerByEmail(email);
			if (!details.getResultCode().equals("OK")) {
				return new OperationResult("CUSTOMER_NOT_FOUND", details.getResultMessage());
			}
			
			// Since there is no token available, create one
			String tokenString = new BigInteger(130, new SecureRandom()).toString();
			ForgotPasswordToken token = new ForgotPasswordToken(tokenString, new Date());
			
			logger.info("Posting forgot password token = {} for email = {}", token, email);
			passwordManagementServicePost.checkPostToken(details.getResultValue(), token);
			
		}catch(Exception exc) {
			return new OperationResult("ERROR", exc.getMessage());
		}
		 		
		return new OperationResult("OK", "OK");
	}

	@Override
	public OperationResult updatePassword(@RequestBody PasswordChange passwordChange) {
		logger.info("Executing update password for customer ID = {}", passwordChange.getCustomerId());
		
		// Verify that there is the token in the database, tied with the customer and having activation date of null
		OperationResultParam<PasswordChange> passChange = null; 
		try {
			// Verify that email address exists
			logger.info("Searching fogot password token: {} for user id={}", passwordChange.getToken(), passwordChange.getCustomerId());
			
			passChange = forgotPasswordTokenServiceGet.getPasswordTokenByCustomerID(passwordChange.getCustomerId(), passwordChange.getToken());
			if (!passChange.getResultCode().equals("OK")) {
				return new OperationResult("TOKEN_NOT_FOUND", passChange.getResultMessage());
			}		
		
			// Update password
			CustomerPatchOperation patchParams = new CustomerPatchOperation("replace", "password", (new StandardPasswordEncoder()).encode(passwordChange.getNewPassword()));
			patchParams.setCustomerId(passwordChange.getCustomerId());
			customerPatchServiceCreate.execute(patchParams);
						
			// Update activation date of forgot password token
			passwordManagementServicePatch.updateForgotPassTokenActDate(passChange.getResultValue().getId());
			
		}catch(Exception exc){
			logger.warn("{}",exc);
			return OperationResult.ERROR.setResultMessage(exc.getMessage());
		}
		
		return new OperationResult("OK", "OK");
	}

	@Override
	public OperationResult changePassword(@RequestBody PasswordChange passwordChange) {
		final Long customerId = ((User)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCID();
		logger.info("Executing update password for customer ID = {}", customerId);
		
		
		CustomerPatchOperation patchParams = new CustomerPatchOperation("replace", "password", (new StandardPasswordEncoder()).encode(passwordChange.getNewPassword()));
		patchParams.setCustomerId(customerId);
		OperationResult result = null;
		OperationResultParam<CustomerDetails> details = null;
		try{
			// Verify that the old password specified is correct
			logger.info("Retrieving user details by id={}", customerId);
			
			details = customerServiceGet.findCustomerDetails(customerId);
			if (!details.getResultCode().equals("OK")) {
				return new OperationResult("CUSTOMER_NOT_FOUND", details.getResultMessage());
			}
			
			Boolean matches = (new StandardPasswordEncoder()).matches(passwordChange.getOldPassword(), details.getResultValue().getPassword());
			if (matches) {
				result = customerPatchServiceCreate.execute(patchParams);
			} else {
				return new OperationResult("INCORRECT_PASSWORD", "Specified password is not correct");
			}
		}catch(Exception exc){
			logger.warn("{}",exc);
			return OperationResult.ERROR.setResultMessage(exc.getMessage());
		}
		return new OperationResult("OK", "OK");
	}
}