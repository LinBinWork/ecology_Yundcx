/**
 * ZSFI_PARK_AC_RET.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.voucher.webservice.sap_com;

public class ZSFI_PARK_AC_RET  implements java.io.Serializable {
    private String ITEM;

    private String BUKRS;

    private String GJAHR;

    private String BELNR;

    private String ISOK;

    private String MSG;

    public ZSFI_PARK_AC_RET() {
    }

    public ZSFI_PARK_AC_RET(
           String ITEM,
           String BUKRS,
           String GJAHR,
           String BELNR,
           String ISOK,
           String MSG) {
           this.ITEM = ITEM;
           this.BUKRS = BUKRS;
           this.GJAHR = GJAHR;
           this.BELNR = BELNR;
           this.ISOK = ISOK;
           this.MSG = MSG;
    }

    @Override
    public String toString() {
        return "ZSFI_PARK_AC_RET{" +
                "ITEM='" + ITEM + '\'' +
                ", BUKRS='" + BUKRS + '\'' +
                ", GJAHR='" + GJAHR + '\'' +
                ", BELNR='" + BELNR + '\'' +
                ", ISOK='" + ISOK + '\'' +
                ", MSG='" + MSG + '\'' +
                ", __equalsCalc=" + __equalsCalc +
                ", __hashCodeCalc=" + __hashCodeCalc +
                '}';
    }

    /**
     * Gets the ITEM value for this ZSFI_PARK_AC_RET.
     * 
     * @return ITEM
     */
    public String getITEM() {
        return ITEM;
    }


    /**
     * Sets the ITEM value for this ZSFI_PARK_AC_RET.
     * 
     * @param ITEM
     */
    public void setITEM(String ITEM) {
        this.ITEM = ITEM;
    }


    /**
     * Gets the BUKRS value for this ZSFI_PARK_AC_RET.
     * 
     * @return BUKRS
     */
    public String getBUKRS() {
        return BUKRS;
    }


    /**
     * Sets the BUKRS value for this ZSFI_PARK_AC_RET.
     * 
     * @param BUKRS
     */
    public void setBUKRS(String BUKRS) {
        this.BUKRS = BUKRS;
    }


    /**
     * Gets the GJAHR value for this ZSFI_PARK_AC_RET.
     * 
     * @return GJAHR
     */
    public String getGJAHR() {
        return GJAHR;
    }


    /**
     * Sets the GJAHR value for this ZSFI_PARK_AC_RET.
     * 
     * @param GJAHR
     */
    public void setGJAHR(String GJAHR) {
        this.GJAHR = GJAHR;
    }


    /**
     * Gets the BELNR value for this ZSFI_PARK_AC_RET.
     * 
     * @return BELNR
     */
    public String getBELNR() {
        return BELNR;
    }


    /**
     * Sets the BELNR value for this ZSFI_PARK_AC_RET.
     * 
     * @param BELNR
     */
    public void setBELNR(String BELNR) {
        this.BELNR = BELNR;
    }


    /**
     * Gets the ISOK value for this ZSFI_PARK_AC_RET.
     * 
     * @return ISOK
     */
    public String getISOK() {
        return ISOK;
    }


    /**
     * Sets the ISOK value for this ZSFI_PARK_AC_RET.
     * 
     * @param ISOK
     */
    public void setISOK(String ISOK) {
        this.ISOK = ISOK;
    }


    /**
     * Gets the MSG value for this ZSFI_PARK_AC_RET.
     * 
     * @return MSG
     */
    public String getMSG() {
        return MSG;
    }


    /**
     * Sets the MSG value for this ZSFI_PARK_AC_RET.
     * 
     * @param MSG
     */
    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ZSFI_PARK_AC_RET)) return false;
        ZSFI_PARK_AC_RET other = (ZSFI_PARK_AC_RET) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ITEM==null && other.getITEM()==null) || 
             (this.ITEM!=null &&
              this.ITEM.equals(other.getITEM()))) &&
            ((this.BUKRS==null && other.getBUKRS()==null) || 
             (this.BUKRS!=null &&
              this.BUKRS.equals(other.getBUKRS()))) &&
            ((this.GJAHR==null && other.getGJAHR()==null) || 
             (this.GJAHR!=null &&
              this.GJAHR.equals(other.getGJAHR()))) &&
            ((this.BELNR==null && other.getBELNR()==null) || 
             (this.BELNR!=null &&
              this.BELNR.equals(other.getBELNR()))) &&
            ((this.ISOK==null && other.getISOK()==null) || 
             (this.ISOK!=null &&
              this.ISOK.equals(other.getISOK()))) &&
            ((this.MSG==null && other.getMSG()==null) || 
             (this.MSG!=null &&
              this.MSG.equals(other.getMSG())));
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
        if (getITEM() != null) {
            _hashCode += getITEM().hashCode();
        }
        if (getBUKRS() != null) {
            _hashCode += getBUKRS().hashCode();
        }
        if (getGJAHR() != null) {
            _hashCode += getGJAHR().hashCode();
        }
        if (getBELNR() != null) {
            _hashCode += getBELNR().hashCode();
        }
        if (getISOK() != null) {
            _hashCode += getISOK().hashCode();
        }
        if (getMSG() != null) {
            _hashCode += getMSG().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ZSFI_PARK_AC_RET.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_PARK_AC_RET"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ITEM");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ITEM"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BUKRS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BUKRS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("GJAHR");
        elemField.setXmlName(new javax.xml.namespace.QName("", "GJAHR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BELNR");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BELNR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ISOK");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ISOK"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MSG");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MSG"));
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
