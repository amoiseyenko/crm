package com.aspiderngi.prepaid.api.web.controller;

import java.util.ArrayList;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspiderngi.artifacts.model.CustomerPatchOperation;
import com.aspiderngi.common.service.entity.CustomerConfirmationInfo;
import com.aspiderngi.common.service.entity.CustomerDetails;
import com.aspiderngi.common.service.entity.CustomerFullInfo;
import com.aspiderngi.common.service.entity.CustomerRegistrationInfo;
import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.entity.result.OperationResultParam;

@RestController
@RequestMapping("/customers")
public abstract interface CustomerController {

	@RequestMapping(value = "",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody OperationResultParam<CustomerConfirmationInfo> register(@RequestBody CustomerRegistrationInfo user);
	
	@RequestMapping(value = "/{customerId}/active/{token}",
			method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody OperationResult activate(@PathVariable Long customerId, @PathVariable String token);

	
	@RequestMapping(value = "",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody OperationResultParam<CustomerDetails> find();	
	

	@RequestMapping(value = "",
			method = RequestMethod.PATCH,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody OperationResult patch(@RequestBody CustomerPatchOperation patchParams);	

//	@RequestMapping(value = "",
//			method = RequestMethod.GET,
//			produces = MediaType.APPLICATION_JSON_VALUE,
//			params = {"firstName","email","msisdn"})
//	public @ResponseBody OperationResultParam<ArrayList<CustomerFullInfo>> search(
//			@RequestParam(value = "firstName", required = false, defaultValue="") String firstName,
//			@RequestParam(value = "email", required = false, defaultValue="") String email,
//			@RequestParam(value = "msisdn", required = false, defaultValue="") String msisdn);	
	
	
}