package com.aspiderngi.prepaid.api.web.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspiderngi.common.service.entity.UsageDetailsView;
import com.aspiderngi.common.service.entity.result.OperationResultParam;

@RestController
@RequestMapping("/usages")
public interface UsageController {

	@RequestMapping(value = "",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody OperationResultParam<ArrayList<UsageDetailsView>> get(
			@RequestHeader("Pagination-Position") Long positionId,
			HttpServletResponse response);

}