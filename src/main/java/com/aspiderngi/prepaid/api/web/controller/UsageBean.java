package com.aspiderngi.prepaid.api.web.controller;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aspiderngi.common.service.entity.CustomerDetails;
import com.aspiderngi.common.service.entity.UsageDetails;
import com.aspiderngi.common.service.entity.UsageDetailsView;
import com.aspiderngi.common.service.entity.User;
import com.aspiderngi.common.service.entity.enums.UsageType;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.prepaid.api.service.CustomerDetailsService_Get;
import com.aspiderngi.prepaid.api.service.UsageService_Get;

@Component
public class UsageBean implements UsageController {

	private static final Logger logger = LoggerFactory.getLogger(UsageBean.class);
	
	private static final Integer USAGE_FETCH_AMOUNT = 30; 

	@Autowired
	private UsageService_Get usageService;
	
	@Autowired
	private CustomerDetailsService_Get customerDetailsService;
	
	@Override
	public @ResponseBody OperationResultParam<ArrayList<UsageDetailsView>> get(
			@RequestHeader("Pagination-Position") Long position,
			HttpServletResponse response) {

		final Long customerId = ((User)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCID();

		logger.info("Getting usage details for customerId={} and position={}", customerId, position);

		ArrayList<UsageDetailsView> usageDetails = new ArrayList<UsageDetailsView>(USAGE_FETCH_AMOUNT);
		
		try {
			OperationResultParam<CustomerDetails> customerDetailsParam = customerDetailsService.getCustomerDetails(customerId);
			
			if(customerDetailsParam == null || customerDetailsParam.getResultValue() == null) {
				logger.error("Customer not found for id = {}", customerId);
			}
			
			Long subscriptionId = customerDetailsParam.getResultValue().getSubscriptionId();

			ArrayList<UsageDetails> usageDetailsOriginal = usageService.get(subscriptionId, position, USAGE_FETCH_AMOUNT);

			if(usageDetails.size() > 0) {
				response.setHeader("Pagination-Position", usageDetailsOriginal.get(usageDetails.size() - 1).getPosition().toString());
			}
			
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMaximumFractionDigits(2);
	        Currency currency = Currency.getInstance("EUR");
	        numberFormat.setCurrency(currency);
	        						
			for(UsageDetails usageDetail : usageDetailsOriginal){
				UsageDetailsView udv = new UsageDetailsView();
				
				udv.setUsageDate(usageDetail.getUsageDate());
				udv.setDialedNumber(usageDetail.getDialedNumber());

				UsageType usageType = usageDetail.getUsageType();
				
				switch(usageType){
					case SMS:
						udv.setUsageDuration(String.valueOf(((Double)(usageDetail.getUsageDuration() / 60.0)).intValue()));
						udv.setUsageType("SMS");
						break;
					case VOICE:
						// Convert back to seconds
						long dur = (long)(usageDetail.getUsageDuration() * 100);
						udv.setUsageDuration(String.format("%02d:%02d:%02d", dur/3600, (dur%3600)/60, (dur%60)));
						udv.setUsageType("Belminuten");
						break;
					case DATA:
						udv.setUsageType("Internet");
						break;
					case MMS:
						udv.setUsageDuration(String.valueOf(usageDetail.getUsageDuration()));
						udv.setUsageType("MMS");
						break;
				}
				
				udv.setUsageCost(numberFormat.format(usageDetail.getUsageCost() / 1000.0));

				usageDetails.add(udv);				
			}

		} catch (Exception e) {
			return new OperationResultParam<ArrayList<UsageDetailsView>>("ERROR", e.getMessage(), null);
		}


		return new OperationResultParam<ArrayList<UsageDetailsView>>("OK", "OK", usageDetails);
	}	
}