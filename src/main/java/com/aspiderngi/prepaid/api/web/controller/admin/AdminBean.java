package com.aspiderngi.prepaid.api.web.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspiderngi.artifacts.model.CustomerPatchOperation;
import com.aspiderngi.common.service.entity.AgentDetails;
import com.aspiderngi.common.service.entity.Balance;
import com.aspiderngi.common.service.entity.CustomerAddress;
import com.aspiderngi.common.service.entity.CustomerDetails;
import com.aspiderngi.common.service.entity.CustomerFullInfo;
import com.aspiderngi.common.service.entity.CustomerNote;
import com.aspiderngi.common.service.entity.Topup;
import com.aspiderngi.common.service.entity.UsageDetailsView;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.prepaid.api.service.BalanceQuery_Get;
import com.aspiderngi.prepaid.api.service.CustomerAddressService_Get;
import com.aspiderngi.prepaid.api.service.CustomerAddressService_Put;
import com.aspiderngi.prepaid.api.service.CustomerDetailsService_Get;
import com.aspiderngi.prepaid.api.service.CustomerNotesService_Get;
import com.aspiderngi.prepaid.api.service.CustomerNotesService_Post;
import com.aspiderngi.prepaid.api.service.CustomerPatchService_Create;
import com.aspiderngi.prepaid.api.service.CustomerService_Get;
import com.aspiderngi.prepaid.api.service.TopupService_Get;
import com.aspiderngi.prepaid.api.service.entity.LoginModel;
import com.aspiderngi.prepaid.api.web.controller.UsageBean;

@Component
public class AdminBean implements AdminController {

	Logger logger = LoggerFactory.getLogger(AdminBean.class);

	@Autowired
	CustomerDetailsService_Get customerDetailsServiceGet;

	@Autowired
	CustomerPatchService_Create customerPatchServiceCreate;

	@Autowired
	CustomerService_Get customerServiceGet;

	@Autowired
	CustomerAddressService_Get customerAddressService;
	
	@Autowired
	CustomerAddressService_Put customerAddressServicePut;
	
	@Autowired
	TopupService_Get topupServiceGet;
	
	@Autowired 
	CustomerNotesService_Post customerNotesServicePost;

	@Autowired
	UsageBean usageBean;
	
	@Autowired
	BalanceQuery_Get balanceQueryGet;
 
	
	@Override
	public OperationResultParam<Balance> getCustomerBalance(@RequestParam  String msisdn){
		logger.info("Getting balance for msisnd {}",msisdn);
		OperationResultParam<Balance> customerBalance = null;
		try{
			// TODO : make request to inventory to get msisdn
			customerBalance = balanceQueryGet.execute(msisdn);
		}catch(Exception exc){
			logger.error("Unable to get balance: {}",exc);
			return new OperationResultParam<Balance>("ERROR","Internal server error", null);
		}
		return customerBalance;
	}
	
	
	@Override
	public OperationResultParam<CustomerDetails> getCustomerById(@PathVariable Long customerId) {
		logger.info("Admin.Searching user for id={}", customerId);

		OperationResultParam<CustomerDetails> details = null; 
		try {
			details = customerDetailsServiceGet.getCustomerDetails(customerId);
		}catch(Exception exc) {
			return new OperationResultParam<CustomerDetails>("ERROR", exc.getMessage(), null);
		}
		return  details;
	}

	@Override
	public OperationResult patch(@RequestBody CustomerPatchOperation patchParams,
			@PathVariable Long customerId) {
		patchParams.setCustomerId(customerId);
		logger.info("Admin.Patching user", patchParams);
		OperationResult result = null;
		try{
			result = customerPatchServiceCreate.execute(patchParams);
		}catch(Exception exc){
			logger.warn("{}",exc);
			return OperationResult.ERROR.setResultMessage(exc.getMessage());
		}
		return result;
	}

	@Override
	public OperationResultParam<ArrayList<CustomerFullInfo>> search(
			@RequestParam String firstName,@RequestParam String email, @RequestParam String msisdn) {
		logger.info("Searching for user by params: firstName = {}, email = {}, msisdn = {}", firstName, email, msisdn);

		OperationResultParam<ArrayList<CustomerFullInfo>> details = null;
		try{
			details = customerServiceGet.search(firstName, email, msisdn);
		}catch(Exception exc){
			return new OperationResultParam<ArrayList<CustomerFullInfo>>("ERROR", exc.getMessage(), null);
		}
		return details;
	}

	@Override
	public OperationResultParam<CustomerAddress> getCustomerAddress(
			@PathVariable Long customerId) {

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
	public OperationResult updateCustomerAddress(
			@RequestBody CustomerAddress customerAddress,@PathVariable Long customerId) {
		logger.info("Update address for CustomerId={}", customerId);

		OperationResult result = null ;

		try {
			result = customerAddressServicePut.execute(customerId, customerAddress);
		} catch(Exception exc){
			return new OperationResult(exc.getMessage(), "ERROR");
		}

		return result;
	}

	@Override
	public OperationResultParam<ArrayList<UsageDetailsView>> getUsage(@RequestHeader("Pagination-Position") Long positionId, HttpServletResponse response, @PathVariable Long customerId) {
		// stub here
		return usageBean.get(positionId, response);
	}

	@Override
	public OperationResultParam<ArrayList<Topup>> getHistory(@PathVariable Long customerId) {
		logger.info("Getting topup history for customerId: {}", customerId);
		ArrayList<Topup> topups = null;
		try { 
			topups = topupServiceGet.getHistory(customerId,5);
		} catch(Exception exc) {
			logger.warn("{}", exc);
			return new OperationResultParam<ArrayList<Topup>>("Server internal error", "ERROR", null);
		}
		return new OperationResultParam<>("OK", "OK",topups);		
	}
	
	

	@Autowired
	CustomerNotesService_Get customerNotesServiceGet;

	@Override
	public OperationResultParam<List<CustomerNote>> getCustomerNotes(
			@PathVariable Long customerId,@RequestParam Integer position, 
			@RequestParam Integer itemsCount) {
		OperationResultParam<List<CustomerNote>> history = null;
		try {
			history = customerNotesServiceGet.getNotes(customerId,position,itemsCount);
		} catch (Exception e) {
			return new OperationResultParam<List<CustomerNote>>("Internal server error","ERROR",null);
		}
		return history;
	}

	@Override
	public OperationResult addNote(@RequestBody CustomerNote note,
			@PathVariable Long customerId) {
		OperationResult operationResult = null;
		Long agentId = ((AgentDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getAID();
		try{
			note.setCustomerId(customerId);
			note.setAgentId(agentId); 
			operationResult = customerNotesServicePost.postNote(note,customerId);
		}catch(Exception exc){
			logger.error("Unable to store note:{}",exc);
			return new OperationResult("Server internal error","ERROR");
		}
		return operationResult;
	}

}
