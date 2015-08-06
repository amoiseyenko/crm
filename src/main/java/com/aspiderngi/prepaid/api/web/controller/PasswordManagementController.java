package com.aspiderngi.prepaid.api.web.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.common.service.entity.PasswordChange;

@RestController
@RequestMapping("/pwd-mgmt")
public interface PasswordManagementController {

	
	@RequestMapping(value = "/forgot-pwd",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody OperationResult forgotPassword(@RequestParam(value="email", required=true) String email);
	
	@RequestMapping(value = "/update-pwd",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody OperationResult updatePassword(@RequestBody PasswordChange passwordChange);
	
	@RequestMapping(value = "/change-pwd",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody OperationResult changePassword(@RequestBody PasswordChange passwordChange);
}
