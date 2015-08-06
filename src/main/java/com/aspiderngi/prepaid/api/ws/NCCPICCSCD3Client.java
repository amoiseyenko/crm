package com.aspiderngi.prepaid.api.ws;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.oracle.xmlns.communications.ncc._2009._05._15.pi.CCSCD3RCH;
import com.oracle.xmlns.communications.ncc._2009._05._15.pi.CCSCD3RCHResponse;

public class NCCPICCSCD3Client extends WebServiceGatewaySupport {

	public CCSCD3RCHResponse CCSCD3_RCH(CCSCD3RCH rch) { // throws SOAPFaultException
		return (CCSCD3RCHResponse) getWebServiceTemplate().marshalSendAndReceive(rch, new SoapActionCallback("http://localhost:8080/spring4soap-1/soapws/getStudentResponse"));
	}
}
