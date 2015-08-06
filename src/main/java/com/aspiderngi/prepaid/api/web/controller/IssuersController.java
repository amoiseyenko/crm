package com.aspiderngi.prepaid.api.web.controller;

import java.util.ArrayList;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspiderngi.common.service.entity.IssuersCountry;
import com.aspiderngi.common.service.entity.result.OperationResultParam;

@RestController
@RequestMapping("/issuers")
public interface IssuersController {

	
	@RequestMapping(value = "",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody OperationResultParam<ArrayList<IssuersCountry>> get();
	
	
}
