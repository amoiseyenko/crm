package com.aspiderngi.prepaid.api.web.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspiderngi.common.service.entity.Balance;
import com.aspiderngi.common.service.entity.result.OperationResultParam;

@RestController
@RequestMapping("/balance")
public interface BalanceController {

	
	@RequestMapping(value = "",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody OperationResultParam<Balance> get();
	
	
}
