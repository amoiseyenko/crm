//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.28 at 05:35:49 PM CEST 
//


package com.oracle.xmlns.communications.ncc._2009._05._15.pi;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AUTH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RECHARGE_TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="REFERENCE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MSISDN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ACCOUNT_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AMOUNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BALANCE_EXPIRY" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="WALLET_EXPIRY" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="MODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WALLET_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BALANCE_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LIMIT_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EXTRA_EDR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BALMODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NEW_BUCKET" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SCENARIO_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SCENARIO_NAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "auth",
    "username",
    "password",
    "rechargetype",
    "reference",
    "msisdn",
    "accountnumber",
    "amount",
    "balanceexpiry",
    "walletexpiry",
    "mode",
    "wallettype",
    "balancetype",
    "limittype",
    "extraedr",
    "balmode",
    "newbucket",
    "scenarioid",
    "scenarioname"
})
@XmlRootElement(name = "CCSCD3_RCH")
public class CCSCD3RCH {

    @XmlElement(name = "AUTH")
    protected String auth;
    protected String username;
    protected String password;
    @XmlElement(name = "RECHARGE_TYPE", required = true)
    protected String rechargetype;
    @XmlElement(name = "REFERENCE", required = true)
    protected String reference;
    @XmlElement(name = "MSISDN")
    protected String msisdn;
    @XmlElement(name = "ACCOUNT_NUMBER")
    protected String accountnumber;
    @XmlElement(name = "AMOUNT")
    protected String amount;
    @XmlElement(name = "BALANCE_EXPIRY")
    protected BigInteger balanceexpiry;
    @XmlElement(name = "WALLET_EXPIRY")
    protected BigInteger walletexpiry;
    @XmlElement(name = "MODE")
    protected String mode;
    @XmlElement(name = "WALLET_TYPE")
    protected String wallettype;
    @XmlElement(name = "BALANCE_TYPE")
    protected String balancetype;
    @XmlElement(name = "LIMIT_TYPE")
    protected String limittype;
    @XmlElement(name = "EXTRA_EDR")
    protected String extraedr;
    @XmlElement(name = "BALMODE")
    protected String balmode;
    @XmlElement(name = "NEW_BUCKET")
    protected String newbucket;
    @XmlElement(name = "SCENARIO_ID")
    protected String scenarioid;
    @XmlElement(name = "SCENARIO_NAME")
    protected String scenarioname;

    /**
     * Gets the value of the auth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTH() {
        return auth;
    }

    /**
     * Sets the value of the auth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTH(String value) {
        this.auth = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the rechargetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECHARGETYPE() {
        return rechargetype;
    }

    /**
     * Sets the value of the rechargetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECHARGETYPE(String value) {
        this.rechargetype = value;
    }

    /**
     * Gets the value of the reference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREFERENCE() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREFERENCE(String value) {
        this.reference = value;
    }

    /**
     * Gets the value of the msisdn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSISDN() {
        return msisdn;
    }

    /**
     * Sets the value of the msisdn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSISDN(String value) {
        this.msisdn = value;
    }

    /**
     * Gets the value of the accountnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACCOUNTNUMBER() {
        return accountnumber;
    }

    /**
     * Sets the value of the accountnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACCOUNTNUMBER(String value) {
        this.accountnumber = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAMOUNT() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAMOUNT(String value) {
        this.amount = value;
    }

    /**
     * Gets the value of the balanceexpiry property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBALANCEEXPIRY() {
        return balanceexpiry;
    }

    /**
     * Sets the value of the balanceexpiry property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBALANCEEXPIRY(BigInteger value) {
        this.balanceexpiry = value;
    }

    /**
     * Gets the value of the walletexpiry property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getWALLETEXPIRY() {
        return walletexpiry;
    }

    /**
     * Sets the value of the walletexpiry property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setWALLETEXPIRY(BigInteger value) {
        this.walletexpiry = value;
    }

    /**
     * Gets the value of the mode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMODE() {
        return mode;
    }

    /**
     * Sets the value of the mode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMODE(String value) {
        this.mode = value;
    }

    /**
     * Gets the value of the wallettype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWALLETTYPE() {
        return wallettype;
    }

    /**
     * Sets the value of the wallettype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWALLETTYPE(String value) {
        this.wallettype = value;
    }

    /**
     * Gets the value of the balancetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBALANCETYPE() {
        return balancetype;
    }

    /**
     * Sets the value of the balancetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBALANCETYPE(String value) {
        this.balancetype = value;
    }

    /**
     * Gets the value of the limittype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLIMITTYPE() {
        return limittype;
    }

    /**
     * Sets the value of the limittype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLIMITTYPE(String value) {
        this.limittype = value;
    }

    /**
     * Gets the value of the extraedr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXTRAEDR() {
        return extraedr;
    }

    /**
     * Sets the value of the extraedr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXTRAEDR(String value) {
        this.extraedr = value;
    }

    /**
     * Gets the value of the balmode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBALMODE() {
        return balmode;
    }

    /**
     * Sets the value of the balmode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBALMODE(String value) {
        this.balmode = value;
    }

    /**
     * Gets the value of the newbucket property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNEWBUCKET() {
        return newbucket;
    }

    /**
     * Sets the value of the newbucket property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNEWBUCKET(String value) {
        this.newbucket = value;
    }

    /**
     * Gets the value of the scenarioid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCENARIOID() {
        return scenarioid;
    }

    /**
     * Sets the value of the scenarioid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCENARIOID(String value) {
        this.scenarioid = value;
    }

    /**
     * Gets the value of the scenarioname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCENARIONAME() {
        return scenarioname;
    }

    /**
     * Sets the value of the scenarioname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCENARIONAME(String value) {
        this.scenarioname = value;
    }

}
