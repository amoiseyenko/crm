package com.aspiderngi.prepaid.api.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.aspiderngi.prepaid.api.ws.NCCPICCSCD3Client;

/**
 * http://www.concretepage.com/spring-4/spring-4-soap-web-service-producer-consumer-example-with-tomcat
 * */

@Configuration
public class NCCPICCSCD3ClientConfiguration {

	@Value("${prop.prepaid.ccscd3port.port}")
	private String ccscd3port = "";

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.oracle.xmlns.communications.ncc._2009._05._15.pi");
		return marshaller;
	}
	
	@Bean
	public NCCPICCSCD3Client studentClient(Jaxb2Marshaller marshaller) {
		NCCPICCSCD3Client client = new NCCPICCSCD3Client();
		client.setDefaultUri(ccscd3port);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}