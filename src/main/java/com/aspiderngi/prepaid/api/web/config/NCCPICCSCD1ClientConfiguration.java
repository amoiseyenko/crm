package com.aspiderngi.prepaid.api.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.aspiderngi.prepaid.api.ws.NCCPICCSCD1Client;

/**
 * http://www.concretepage.com/spring-4/spring-4-soap-web-service-producer-consumer-example-with-tomcat
 * */

@Configuration
public class NCCPICCSCD1ClientConfiguration {

	@Value("${prop.prepaid.ccscd1.url}")
	private String ccscd1url = "";

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.oracle.xmlns.communications.ncc._2009._05._15.pi");
		return marshaller;
	}
	
	@Bean
	public NCCPICCSCD1Client ccscd1Client(Jaxb2Marshaller marshaller) {
		NCCPICCSCD1Client client = new NCCPICCSCD1Client();
		client.setDefaultUri(ccscd1url);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}