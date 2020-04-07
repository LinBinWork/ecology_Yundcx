/**
 * ZSFI_ACCOUNT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.sync.webservice.sap_com;

public class ZSFI_ACCOUNT  implements java.io.Serializable {
    private String BUKRS;

    private String HKONT;

    private String TXT50;

    private String SPRAS;

    private String XSPEB;

    public ZSFI_ACCOUNT() {
    }

    public ZSFI_ACCOUNT(
           String BUKRS,
           String HKONT,
           String TXT50,
           String SPRAS,
           String XSPEB) {
           this.BUKRS = BUKRS;
           this.HKONT = HKONT;
           this.TXT50 = TXT50;
           this.SPRAS = SPRAS;
           this.XSPEB = XSPEB;
    }


    /**
     * Gets the BUKRS value for this ZSFI_ACCOUNT.
     * 
     * @return BUKRS
     */
    public String getBUKRS() {
        return BUKRS;
    }


    /**
     * Sets the BUKRS value for this ZSFI_ACCOUNT.
     * 
     * @param BUKRS
     */
    public void setBUKRS(String BUKRS) {
        this.BUKRS = BUKRS;
    }


    /**
     * Gets the HKONT value for this ZSFI_ACCOUNT.
     * 
     * @return HKONT
     */
    public String getHKONT() {
        return HKONT;
    }


    /**
     * Sets the HKONT value for this ZSFI_ACCOUNT.
     * 
     * @param HKONT
     */
    public void setHKONT(String HKONT) {
        this.HKONT = HKONT;
    }


    /**
     * Gets the TXT50 value for this ZSFI_ACCOUNT.
     * 
     * @return TXT50
     */
    public String getTXT50() {
        return TXT50;
    }


    /**
     * Sets the TXT50 value for this ZSFI_ACCOUNT.
     * 
     * @param TXT50
     */
    public void setTXT50(String TXT50) {
        this.TXT50 = TXT50;
    }


    /**
     * Gets the SPRAS value for this ZSFI_ACCOUNT.
     * 
     * @return SPRAS
     */
    public String getSPRAS() {
        return SPRAS;
    }


    /**
     * Sets the SPRAS value for this ZSFI_ACCOUNT.
     * 
     * @param SPRAS
     */
    public void setSPRAS(String SPRAS) {
        this.SPRAS = SPRAS;
    }


    /**
     * Gets the XSPEB value for this ZSFI_ACCOUNT.
     * 
     * @return XSPEB
     */
    public String getXSPEB() {
        return XSPEB;
    }


    /**
     * Sets the XSPEB value for this ZSFI_ACCOUNT.
     * 
     * @param XSPEB
     */
    public void setXSPEB(String XSPEB) {
        this.XSPEB = XSPEB;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ZSFI_ACCOUNT)) return false;
        ZSFI_ACCOUNT other = (ZSFI_ACCOUNT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.BUKRS==null && other.getBUKRS()==null) || 
             (this.BUKRS!=null &&
              this.BUKRS.equals(other.getBUKRS()))) &&
            ((this.HKONT==null && other.getHKONT()==null) || 
             (this.HKONT!=null &&
              this.HKONT.equals(other.getHKONT()))) &&
            ((this.TXT50==null && other.getTXT50()==null) || 
             (this.TXT50!=null &&
              this.TXT50.equals(other.getTXT50()))) &&
            ((this.SPRAS==null && other.getSPRAS()==null) || 
             (this.SPRAS!=null &&
              this.SPRAS.equals(other.getSPRAS()))) &&
            ((this.XSPEB==null && other.getXSPEB()==null) || 
             (this.XSPEB!=null &&
              this.XSPEB.equals(other.getXSPEB())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getBUKRS() != null) {
            _hashCode += getBUKRS().hashCode();
        }
        if (getHKONT() != null) {
            _hashCode += getHKONT().hashCode();
        }
        if (getTXT50() != null) {
            _hashCode += getTXT50().hashCode();
        }
        if (getSPRAS() != null) {
            _hashCode += getSPRAS().hashCode();
        }
        if (getXSPEB() != null) {
            _hashCode += getXSPEB().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ZSFI_ACCOUNT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_ACCOUNT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BUKRS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BUKRS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("HKONT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "HKONT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TXT50");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TXT50"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SPRAS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SPRAS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("XSPEB");
        elemField.setXmlName(new javax.xml.namespace.QName("", "XSPEB"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
