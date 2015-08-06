package com.aspiderngi.prepaid.api.ws;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.oracle.xmlns.communications.ncc._2009._05._15.pi.CCSCD1QRY;
import com.oracle.xmlns.communications.ncc._2009._05._15.pi.CCSCD1QRYResponse;

public class NCCPICCSCD1Client extends WebServiceGatewaySupport {

	public CCSCD1QRYResponse CCSCD1_QRY(CCSCD1QRY qry) { // throws SOAPFaultException
		return (CCSCD1QRYResponse)this.getWebServiceTemplate().marshalSendAndReceive(qry);
		//return (CCSCD1QRYResponse) getWebServiceTemplate().marshalSendAndReceive(qry, new SoapActionCallback("http://localhost:8080/spring4soap-1/soapws/getStudentResponse"));
	}
}
