//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.28 at 05:35:49 PM CEST 
//


package com.oracle.xmlns.communications.ncc._2009._05._15.pi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
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
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="AUTH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="return" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "authAndReturn"
})
@XmlRootElement(name = "CCSCD1_MSWResponse")
public class CCSCD1MSWResponse {

    @XmlElementRefs({
        @XmlElementRef(name = "AUTH", namespace = "http://xmlns.oracle.com/communications/ncc/2009/05/15/pi", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "return", namespace = "http://xmlns.oracle.com/communications/ncc/2009/05/15/pi", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<String>> authAndReturn;

    /**
     * Gets the value of the authAndReturn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the authAndReturn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAUTHAndReturn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<String>> getAUTHAndReturn() {
        if (authAndReturn == null) {
            authAndReturn = new ArrayList<JAXBElement<String>>();
        }
        return this.authAndReturn;
    }

}
