/**
 * ZSFI_COST_CENTER.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.sync.webservice.sap_com;

public class ZSFI_COST_CENTER  implements java.io.Serializable {
    private String BUKRS;

    private String KOSTL;

    private String LTEXT;

    private String SPRAS;

    private String BKZKP;

    public ZSFI_COST_CENTER() {
    }

    public ZSFI_COST_CENTER(
           String BUKRS,
           String KOSTL,
           String LTEXT,
           String SPRAS,
           String BKZKP) {
           this.BUKRS = BUKRS;
           this.KOSTL = KOSTL;
           this.LTEXT = LTEXT;
           this.SPRAS = SPRAS;
           this.BKZKP = BKZKP;
    }


    /**
     * Gets the BUKRS value for this ZSFI_COST_CENTER.
     * 
     * @return BUKRS
     */
    public String getBUKRS() {
        return BUKRS;
    }


    /**
     * Sets the BUKRS value for this ZSFI_COST_CENTER.
     * 
     * @param BUKRS
     */
    public void setBUKRS(String BUKRS) {
        this.BUKRS = BUKRS;
    }


    /**
     * Gets the KOSTL value for this ZSFI_COST_CENTER.
     * 
     * @return KOSTL
     */
    public String getKOSTL() {
        return KOSTL;
    }


    /**
     * Sets the KOSTL value for this ZSFI_COST_CENTER.
     * 
     * @param KOSTL
     */
    public void setKOSTL(String KOSTL) {
        this.KOSTL = KOSTL;
    }


    /**
     * Gets the LTEXT value for this ZSFI_COST_CENTER.
     * 
     * @return LTEXT
     */
    public String getLTEXT() {
        return LTEXT;
    }


    /**
     * Sets the LTEXT value for this ZSFI_COST_CENTER.
     * 
     * @param LTEXT
     */
    public void setLTEXT(String LTEXT) {
        this.LTEXT = LTEXT;
    }


    /**
     * Gets the SPRAS value for this ZSFI_COST_CENTER.
     * 
     * @return SPRAS
     */
    public String getSPRAS() {
        return SPRAS;
    }


    /**
     * Sets the SPRAS value for this ZSFI_COST_CENTER.
     * 
     * @param SPRAS
     */
    public void setSPRAS(String SPRAS) {
        this.SPRAS = SPRAS;
    }


    /**
     * Gets the BKZKP value for this ZSFI_COST_CENTER.
     * 
     * @return BKZKP
     */
    public String getBKZKP() {
        return BKZKP;
    }


    /**
     * Sets the BKZKP value for this ZSFI_COST_CENTER.
     * 
     * @param BKZKP
     */
    public void setBKZKP(String BKZKP) {
        this.BKZKP = BKZKP;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ZSFI_COST_CENTER)) return false;
        ZSFI_COST_CENTER other = (ZSFI_COST_CENTER) obj;
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
            ((this.KOSTL==null && other.getKOSTL()==null) || 
             (this.KOSTL!=null &&
              this.KOSTL.equals(other.getKOSTL()))) &&
            ((this.LTEXT==null && other.getLTEXT()==null) || 
             (this.LTEXT!=null &&
              this.LTEXT.equals(other.getLTEXT()))) &&
            ((this.SPRAS==null && other.getSPRAS()==null) || 
             (this.SPRAS!=null &&
              this.SPRAS.equals(other.getSPRAS()))) &&
            ((this.BKZKP==null && other.getBKZKP()==null) || 
             (this.BKZKP!=null &&
              this.BKZKP.equals(other.getBKZKP())));
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
        if (getKOSTL() != null) {
            _hashCode += getKOSTL().hashCode();
        }
        if (getLTEXT() != null) {
            _hashCode += getLTEXT().hashCode();
        }
        if (getSPRAS() != null) {
            _hashCode += getSPRAS().hashCode();
        }
        if (getBKZKP() != null) {
            _hashCode += getBKZKP().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ZSFI_COST_CENTER.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_COST_CENTER"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BUKRS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BUKRS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("KOSTL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "KOSTL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("LTEXT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LTEXT"));
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
        elemField.setFieldName("BKZKP");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BKZKP"));
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
