package com.aspiderngi.prepaid.api.web.controller.admin;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspiderngi.artifacts.model.CustomerPatchOperation;
import com.aspiderngi.common.service.entity.Balance;
import com.aspiderngi.common.service.entity.CustomerAddress;
import com.aspiderngi.common.service.entity.CustomerDetails;
import com.aspiderngi.common.service.entity.CustomerFullInfo;
import com.aspiderngi.common.service.entity.CustomerNote;
import com.aspiderngi.common.service.entity.Topup;
import com.aspiderngi.common.service.entity.UsageDetails;
import com.aspiderngi.common.service.entity.UsageDetailsView;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.prepaid.api.service.entity.LoginModel;

@RestController
@RequestMapping("/admin")
public interface AdminController {
	
	@RequestMapping(value = "/balance",
			method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			params = {"msisdn"})
	public OperationResultParam<Balance> getCustomerBalance(@RequestParam(value = "msisdn", required = false, defaultValue="") String msisdn);
	
	// 4.2.3 Find Customer By ID
	@RequestMapping(value = "/customer/{customerId}",
			method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationResultParam<CustomerDetails> getCustomerById(@PathVariable Long customerId);
	
	
	//4.2.4 Update Customer
	@RequestMapping(value = "/customer/{customerId}",
			method = RequestMethod.PATCH,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody OperationResult patch(@RequestBody CustomerPatchOperation patchParams,@PathVariable Long customerId);	
	
	// 4.2.5 Find Customers By Email, FirstName or MSISDN  ( move search from CUstomerBean)
	@RequestMapping(value = "/search",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			params = {"firstName","email","msisdn"})
	public @ResponseBody OperationResultParam<ArrayList<CustomerFullInfo>> search(
			@RequestParam(value = "firstName", required = false, defaultValue="") String firstName,
			@RequestParam(value = "email", required = false, defaultValue="") String email,
			@RequestParam(value = "msisdn", required = false, defaultValue="") String msisdn);	
	
	// 4.3.1 Get Customer’s Address
	@RequestMapping(value = "/addresses/{customerId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationResultParam<CustomerAddress> getCustomerAddress(@PathVariable Long customerId);
	
	// 4.3.2 Update Customer’s Address
	@RequestMapping(value = "/addresses/{customerId}",
			method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes =	MediaType.APPLICATION_JSON_VALUE)
	public OperationResult updateCustomerAddress(@RequestBody CustomerAddress customerAddress, @PathVariable Long customerId);
	
	//4.4.1 Get Usage
	@RequestMapping(value = "/usage/{customerId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody OperationResultParam<ArrayList<UsageDetailsView>> getUsage(
							@RequestHeader("Pagination-Position") Long positionId, 
							HttpServletResponse response,
							@PathVariable Long customerId);

	//4.5.2 Get Top-Up History ( from topupBean ) 
	@RequestMapping(value = "/history/{customerId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody OperationResultParam<ArrayList<Topup>> getHistory(@PathVariable Long customerId);
	
	
	@RequestMapping(value = "/notes/{customerId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			params={"position","itemsCount"})
	public OperationResultParam<List<CustomerNote>> getCustomerNotes(@PathVariable Long customerId,@RequestParam Integer position, 
			@RequestParam Integer itemsCount);
	
	@RequestMapping(value = "/notes/{customerId}",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationResult addNote(@RequestBody CustomerNote customerNote,@PathVariable Long customerId);
	

	
	
}
