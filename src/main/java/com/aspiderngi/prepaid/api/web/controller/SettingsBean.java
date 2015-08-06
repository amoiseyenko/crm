package com.aspiderngi.prepaid.api.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspiderngi.common.service.entity.LowBalanceNotification;
import com.aspiderngi.common.service.entity.User;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.prepaid.api.service.BalanceNotificationService_Get;
import com.aspiderngi.prepaid.api.service.BalanceNotificationService_Put;

import java.net.ConnectException;

@Component
public class SettingsBean implements SettingsController {

	@Autowired
	private BalanceNotificationService_Get balanceNotificationServiceGet;
	
	@Autowired
	private BalanceNotificationService_Put balanceNotificationServiceSet;
	
	private static final Logger logger = LoggerFactory.getLogger(SettingsBean.class);

	@Override
	public OperationResultParam<LowBalanceNotification> getLowBalanceNotification() {
		final Long customerId = ((User)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCID();

 		logger.info("Retrieving user settings for id={}", customerId);

		OperationResultParam<LowBalanceNotification> lowBalanceNotification = null; 
		try {
			lowBalanceNotification = balanceNotificationServiceGet.execute(customerId);
		} catch (ConnectException ex) {
			logger.error("InvMgmgt Server is not available.");
			return new OperationResultParam<LowBalanceNotification>("InvMgmgt Server is not available", "SERVER_ERROR", null);
		} catch(Exception exc) {
			logger.warn("Unable to retrieve customer settings.");
			return new OperationResultParam<LowBalanceNotification>("ERROR", exc.getMessage(), null);
		}
		return  lowBalanceNotification;
	}

	@Override
	public OperationResult patch(@RequestBody LowBalanceNotification lowBalanceNotification) {
		final Long customerId = ((User)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCID();

		logger.info("Retrieving user settings for id={}", customerId);
 
		try {
			balanceNotificationServiceSet.execute(customerId, lowBalanceNotification);
		} catch (ConnectException ex) {
			logger.error("InvMgmgt Server is not available.");
			return new OperationResult("InvMgmgt Server is not available", "SERVER_ERROR");
		} catch(Exception exc) {
			return new OperationResult("ERROR", exc.getMessage());
		}
		return new OperationResult("OK", "OK");
	}
}