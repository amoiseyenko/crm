package com.aspiderngi.prepaid.api.service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.aspiderngi.common.service.entity.IssuersCountry;
import com.aspiderngi.common.service.util.IdealServiceActions;

@Component
public class IssuersService_Get {

	private static final Logger logger = LoggerFactory.getLogger(IssuersService_Get.class);

	@Value("${ideal.service.url}")
	private String idealServiceUrl;

	public IssuersService_Get (){}

	public ArrayList<IssuersCountry> execute() throws Exception{
		logger.info("Getting issuers.");
		ArrayList<IssuersCountry>  issuers = null;
		Long start = System.currentTimeMillis();

		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_XML);

			StringBuilder sBuilder = new StringBuilder(idealServiceUrl).append(IdealServiceActions.ISSUERS);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sBuilder.toString());
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			String  responseBody  = restTemplate.exchange(builder.build().encode().toString(), HttpMethod.GET, entity,
					String.class).getBody() ;

			issuers = new ObjectMapper().readValue(responseBody, new TypeReference<ArrayList<IssuersCountry>>(){});
			if(issuers == null){
				throw new Exception("Unable to get responce from iDeal service");
			} 
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}

		return issuers;

	}



}
