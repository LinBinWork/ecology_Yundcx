/**
 * ZSFI_ASSET.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.sync.webservice.sap_com;

public class ZSFI_ASSET  implements java.io.Serializable {
    private String BUKRS;

    private String ANLN1;

    private String ANLN2;

    private String ANLKL;

    private String TXT50;

    public ZSFI_ASSET() {
    }

    public ZSFI_ASSET(
           String BUKRS,
           String ANLN1,
           String ANLN2,
           String ANLKL,
           String TXT50) {
           this.BUKRS = BUKRS;
           this.ANLN1 = ANLN1;
           this.ANLN2 = ANLN2;
           this.ANLKL = ANLKL;
           this.TXT50 = TXT50;
    }


    /**
     * Gets the BUKRS value for this ZSFI_ASSET.
     * 
     * @return BUKRS
     */
    public String getBUKRS() {
        return BUKRS;
    }


    /**
     * Sets the BUKRS value for this ZSFI_ASSET.
     * 
     * @param BUKRS
     */
    public void setBUKRS(String BUKRS) {
        this.BUKRS = BUKRS;
    }


    /**
     * Gets the ANLN1 value for this ZSFI_ASSET.
     * 
     * @return ANLN1
     */
    public String getANLN1() {
        return ANLN1;
    }


    /**
     * Sets the ANLN1 value for this ZSFI_ASSET.
     * 
     * @param ANLN1
     */
    public void setANLN1(String ANLN1) {
        this.ANLN1 = ANLN1;
    }


    /**
     * Gets the ANLN2 value for this ZSFI_ASSET.
     * 
     * @return ANLN2
     */
    public String getANLN2() {
        return ANLN2;
    }


    /**
     * Sets the ANLN2 value for this ZSFI_ASSET.
     * 
     * @param ANLN2
     */
    public void setANLN2(String ANLN2) {
        this.ANLN2 = ANLN2;
    }


    /**
     * Gets the ANLKL value for this ZSFI_ASSET.
     * 
     * @return ANLKL
     */
    public String getANLKL() {
        return ANLKL;
    }


    /**
     * Sets the ANLKL value for this ZSFI_ASSET.
     * 
     * @param ANLKL
     */
    public void setANLKL(String ANLKL) {
        this.ANLKL = ANLKL;
    }


    /**
     * Gets the TXT50 value for this ZSFI_ASSET.
     * 
     * @return TXT50
     */
    public String getTXT50() {
        return TXT50;
    }


    /**
     * Sets the TXT50 value for this ZSFI_ASSET.
     * 
     * @param TXT50
     */
    public void setTXT50(String TXT50) {
        this.TXT50 = TXT50;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ZSFI_ASSET)) return false;
        ZSFI_ASSET other = (ZSFI_ASSET) obj;
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
            ((this.ANLN1==null && other.getANLN1()==null) || 
             (this.ANLN1!=null &&
              this.ANLN1.equals(other.getANLN1()))) &&
            ((this.ANLN2==null && other.getANLN2()==null) || 
             (this.ANLN2!=null &&
              this.ANLN2.equals(other.getANLN2()))) &&
            ((this.ANLKL==null && other.getANLKL()==null) || 
             (this.ANLKL!=null &&
              this.ANLKL.equals(other.getANLKL()))) &&
            ((this.TXT50==null && other.getTXT50()==null) || 
             (this.TXT50!=null &&
              this.TXT50.equals(other.getTXT50())));
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
        if (getANLN1() != null) {
            _hashCode += getANLN1().hashCode();
        }
        if (getANLN2() != null) {
            _hashCode += getANLN2().hashCode();
        }
        if (getANLKL() != null) {
            _hashCode += getANLKL().hashCode();
        }
        if (getTXT50() != null) {
            _hashCode += getTXT50().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ZSFI_ASSET.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_ASSET"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BUKRS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BUKRS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ANLN1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ANLN1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ANLN2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ANLN2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ANLKL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ANLKL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TXT50");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TXT50"));
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
