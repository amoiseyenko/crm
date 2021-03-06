//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.28 at 05:35:49 PM CEST 
//


package com.oracle.xmlns.communications.ncc._2009._05._15.pi;

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
 *         &lt;element name="ORIG_MSISDN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DEST_MSISDN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CT_TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ORIG_WALLET" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DEST_WALLET" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PIN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "origmsisdn",
    "destmsisdn",
    "cttype",
    "origwallet",
    "destwallet",
    "pin"
})
@XmlRootElement(name = "CCSCD3_CTR")
public class CCSCD3CTR {

    @XmlElement(name = "AUTH")
    protected String auth;
    protected String username;
    protected String password;
    @XmlElement(name = "ORIG_MSISDN", required = true)
    protected String origmsisdn;
    @XmlElement(name = "DEST_MSISDN", required = true)
    protected String destmsisdn;
    @XmlElement(name = "CT_TYPE", required = true)
    protected String cttype;
    @XmlElement(name = "ORIG_WALLET")
    protected String origwallet;
    @XmlElement(name = "DEST_WALLET")
    protected String destwallet;
    @XmlElement(name = "PIN")
    protected String pin;

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
     * Gets the value of the origmsisdn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORIGMSISDN() {
        return origmsisdn;
    }

    /**
     * Sets the value of the origmsisdn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORIGMSISDN(String value) {
        this.origmsisdn = value;
    }

    /**
     * Gets the value of the destmsisdn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESTMSISDN() {
        return destmsisdn;
    }

    /**
     * Sets the value of the destmsisdn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESTMSISDN(String value) {
        this.destmsisdn = value;
    }

    /**
     * Gets the value of the cttype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCTTYPE() {
        return cttype;
    }

    /**
     * Sets the value of the cttype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCTTYPE(String value) {
        this.cttype = value;
    }

    /**
     * Gets the value of the origwallet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORIGWALLET() {
        return origwallet;
    }

    /**
     * Sets the value of the origwallet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORIGWALLET(String value) {
        this.origwallet = value;
    }

    /**
     * Gets the value of the destwallet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESTWALLET() {
        return destwallet;
    }

    /**
     * Sets the value of the destwallet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESTWALLET(String value) {
        this.destwallet = value;
    }

    /**
     * Gets the value of the pin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPIN() {
        return pin;
    }

    /**
     * Sets the value of the pin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPIN(String value) {
        this.pin = value;
    }

}
