package com.aspiderngi.prepaid.api.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aspiderngi.common.service.entity.result.OperationResult;
import com.aspiderngi.prepaid.api.ws.NCCPICCSCD3Client;
import com.oracle.xmlns.communications.ncc._2009._05._15.pi.CCSCD3RCH;
import com.oracle.xmlns.communications.ncc._2009._05._15.pi.CCSCD3RCHResponse;

@Component
public class OCSTopupService_Put {

	private static final Logger logger = LoggerFactory.getLogger(OCSTopupService_Put.class);

	@Autowired
	private NCCPICCSCD3Client nccClient;

	@Value("${prop.prepaid.ccscd3.username}")
	private String nccUser;
	
	@Value("${prop.prepaid.ccscd3.password}")
	private String nccPass;	

	@Value("${ocs.precision}")
	private String ocsPrecision;
	
	@Value("${topup.expiration}")
	private String topupExpiration;
	
	public OperationResult execute(BigDecimal amount, String msisdn) throws Exception {
		logger.info("Doing OCS topup for MSISDN: {}", msisdn);
		
		int walletExpiration = Integer.parseInt(topupExpiration);
		
		Long start = System.currentTimeMillis();
		OperationResult result = null;
		
		// Multiply the amount to OCS precision
		amount = amount.multiply(BigDecimal.valueOf(Long.parseLong(ocsPrecision)));

		try {
			CCSCD3RCH rch = new CCSCD3RCH();
			rch.setUsername(nccUser);
			rch.setPassword(nccPass);
			rch.setRECHARGETYPE("Custom");
			rch.setREFERENCE("ESB_GENERIC");
			rch.setMSISDN(msisdn);
			rch.setAMOUNT(amount.toPlainString());
			rch.setWALLETEXPIRY(BigInteger.valueOf(walletExpiration));
			rch.setMODE("3");
			rch.setBALANCETYPE("Prepay General Cash");
			rch.setNEWBUCKET("N");
			
	
			CCSCD3RCHResponse rch_resp = nccClient.CCSCD3_RCH(rch);
		} catch(Exception ex) {
			logger.warn(ex.getMessage());

			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}
		
		return result;
	}
}