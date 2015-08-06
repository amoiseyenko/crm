package com.aspiderngi.prepaid.api.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aspiderngi.common.service.entity.Balance;
import com.aspiderngi.common.service.entity.result.OperationResultParam;
import com.aspiderngi.prepaid.api.ws.NCCPICCSCD1Client;
import com.oracle.xmlns.communications.ncc._2009._05._15.pi.CCSCD1QRY;
import com.oracle.xmlns.communications.ncc._2009._05._15.pi.CCSCD1QRYResponse;

@Component
public class BalanceQuery_Get {

	private static final Logger logger = LoggerFactory.getLogger(BalanceQuery_Get.class);

	@Autowired
	private NCCPICCSCD1Client nccClient;

	@Value("${inventory.service.url}")
	private String inventoryServiceUrl;
	
	@Value("${prop.prepaid.ccscd1.username}")
	private String nccUser;
	
	@Value("${prop.prepaid.ccscd1.password}")
	private String nccPass;
	
	@Value("${ocs.precision}")
	private String ocsPrecision;
	
	public OperationResultParam<Balance> execute(String msisdn) throws Exception {
		logger.info("Querying balance for msisdn: {}", msisdn);
		
		Long start = System.currentTimeMillis();

		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
		BigDecimal balance = null;
		Date walletExpirationDate = null;

		try {
			CCSCD1QRY qry = new CCSCD1QRY();
			qry.setUsername(nccUser);
			qry.setPassword(nccPass);

			qry.setMSISDN(msisdn);
			qry.setLISTTYPE("BALANCE");
			qry.setBALANCETYPE("ALL");
					
			CCSCD1QRYResponse qry_resp = nccClient.CCSCD1_QRY(qry);
			
			for(JAXBElement<?> jaxbElement : qry_resp.getAUTHAndMSISDNAndACCOUNTNUMBER()){
				
				if (jaxbElement.getName().getLocalPart().equals("WALLET_EXPIRY_DATE") && !jaxbElement.isNil()) {				
					String timestamp = (String)jaxbElement.getValue();
					walletExpirationDate = format.parse(timestamp);					
				}
				
				if(jaxbElement.getName().getLocalPart().equals("BALANCES")) {
					CCSCD1QRYResponse.BALANCES balances = (CCSCD1QRYResponse.BALANCES)jaxbElement.getValue();
					
					for(CCSCD1QRYResponse.BALANCES.BALANCEITEM balanceItem : balances.getBALANCEITEM()){						
						if("Prepay General Cash".equals(balanceItem.getBALANCETYPENAME())){
							for(CCSCD1QRYResponse.BALANCES.BALANCEITEM.BUCKETS.BUCKETITEM bucketItem : balanceItem.getBUCKETS().getBUCKETITEM()) {
															
							    balance = new BigDecimal(bucketItem.getBUCKETVALUE());							    							    
							}
						}						
					}
				}
			}	
			
			if (balance != null) {
				balance = balance.divide(new BigDecimal(ocsPrecision));
				balance = balance.setScale(2, BigDecimal.ROUND_UP);
			}
			
			return new OperationResultParam<Balance>("OK", "OK", new Balance(balance.toPlainString(), walletExpirationDate));
			
		} catch(Exception ex) {
			logger.warn(ex.getMessage());
			
			throw ex;
		} finally {
			logger.info("Execution time: " + TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start));
		}
	}
}