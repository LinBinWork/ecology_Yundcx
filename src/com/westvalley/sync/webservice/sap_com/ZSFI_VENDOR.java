/**
 * ZSFI_VENDOR.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.sync.webservice.sap_com;

public class ZSFI_VENDOR  implements java.io.Serializable {
    private String LIFNR;

    private String NAME1;

    private String ZGYLX;

    private String AKONT;

    private String BUKRS;

    private String ZBANK;

    private String ACCNAME;

    private String XBLCK;

    public ZSFI_VENDOR() {
    }

    public ZSFI_VENDOR(
           String LIFNR,
           String NAME1,
           String ZGYLX,
           String AKONT,
           String BUKRS,
           String ZBANK,
           String ACCNAME,
           String XBLCK) {
           this.LIFNR = LIFNR;
           this.NAME1 = NAME1;
           this.ZGYLX = ZGYLX;
           this.AKONT = AKONT;
           this.BUKRS = BUKRS;
           this.ZBANK = ZBANK;
           this.ACCNAME = ACCNAME;
           this.XBLCK = XBLCK;
    }


    /**
     * Gets the LIFNR value for this ZSFI_VENDOR.
     * 
     * @return LIFNR
     */
    public String getLIFNR() {
        return LIFNR;
    }


    /**
     * Sets the LIFNR value for this ZSFI_VENDOR.
     * 
     * @param LIFNR
     */
    public void setLIFNR(String LIFNR) {
        this.LIFNR = LIFNR;
    }


    /**
     * Gets the NAME1 value for this ZSFI_VENDOR.
     * 
     * @return NAME1
     */
    public String getNAME1() {
        return NAME1;
    }


    /**
     * Sets the NAME1 value for this ZSFI_VENDOR.
     * 
     * @param NAME1
     */
    public void setNAME1(String NAME1) {
        this.NAME1 = NAME1;
    }


    /**
     * Gets the ZGYLX value for this ZSFI_VENDOR.
     * 
     * @return ZGYLX
     */
    public String getZGYLX() {
        return ZGYLX;
    }


    /**
     * Sets the ZGYLX value for this ZSFI_VENDOR.
     * 
     * @param ZGYLX
     */
    public void setZGYLX(String ZGYLX) {
        this.ZGYLX = ZGYLX;
    }


    /**
     * Gets the AKONT value for this ZSFI_VENDOR.
     * 
     * @return AKONT
     */
    public String getAKONT() {
        return AKONT;
    }


    /**
     * Sets the AKONT value for this ZSFI_VENDOR.
     * 
     * @param AKONT
     */
    public void setAKONT(String AKONT) {
        this.AKONT = AKONT;
    }


    /**
     * Gets the BUKRS value for this ZSFI_VENDOR.
     * 
     * @return BUKRS
     */
    public String getBUKRS() {
        return BUKRS;
    }


    /**
     * Sets the BUKRS value for this ZSFI_VENDOR.
     * 
     * @param BUKRS
     */
    public void setBUKRS(String BUKRS) {
        this.BUKRS = BUKRS;
    }


    /**
     * Gets the ZBANK value for this ZSFI_VENDOR.
     * 
     * @return ZBANK
     */
    public String getZBANK() {
        return ZBANK;
    }


    /**
     * Sets the ZBANK value for this ZSFI_VENDOR.
     * 
     * @param ZBANK
     */
    public void setZBANK(String ZBANK) {
        this.ZBANK = ZBANK;
    }


    /**
     * Gets the ACCNAME value for this ZSFI_VENDOR.
     * 
     * @return ACCNAME
     */
    public String getACCNAME() {
        return ACCNAME;
    }


    /**
     * Sets the ACCNAME value for this ZSFI_VENDOR.
     * 
     * @param ACCNAME
     */
    public void setACCNAME(String ACCNAME) {
        this.ACCNAME = ACCNAME;
    }


    /**
     * Gets the XBLCK value for this ZSFI_VENDOR.
     * 
     * @return XBLCK
     */
    public String getXBLCK() {
        return XBLCK;
    }


    /**
     * Sets the XBLCK value for this ZSFI_VENDOR.
     * 
     * @param XBLCK
     */
    public void setXBLCK(String XBLCK) {
        this.XBLCK = XBLCK;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ZSFI_VENDOR)) return false;
        ZSFI_VENDOR other = (ZSFI_VENDOR) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.LIFNR==null && other.getLIFNR()==null) || 
             (this.LIFNR!=null &&
              this.LIFNR.equals(other.getLIFNR()))) &&
            ((this.NAME1==null && other.getNAME1()==null) || 
             (this.NAME1!=null &&
              this.NAME1.equals(other.getNAME1()))) &&
            ((this.ZGYLX==null && other.getZGYLX()==null) || 
             (this.ZGYLX!=null &&
              this.ZGYLX.equals(other.getZGYLX()))) &&
            ((this.AKONT==null && other.getAKONT()==null) || 
             (this.AKONT!=null &&
              this.AKONT.equals(other.getAKONT()))) &&
            ((this.BUKRS==null && other.getBUKRS()==null) || 
             (this.BUKRS!=null &&
              this.BUKRS.equals(other.getBUKRS()))) &&
            ((this.ZBANK==null && other.getZBANK()==null) || 
             (this.ZBANK!=null &&
              this.ZBANK.equals(other.getZBANK()))) &&
            ((this.ACCNAME==null && other.getACCNAME()==null) || 
             (this.ACCNAME!=null &&
              this.ACCNAME.equals(other.getACCNAME()))) &&
            ((this.XBLCK==null && other.getXBLCK()==null) || 
             (this.XBLCK!=null &&
              this.XBLCK.equals(other.getXBLCK())));
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
        if (getLIFNR() != null) {
            _hashCode += getLIFNR().hashCode();
        }
        if (getNAME1() != null) {
            _hashCode += getNAME1().hashCode();
        }
        if (getZGYLX() != null) {
            _hashCode += getZGYLX().hashCode();
        }
        if (getAKONT() != null) {
            _hashCode += getAKONT().hashCode();
        }
        if (getBUKRS() != null) {
            _hashCode += getBUKRS().hashCode();
        }
        if (getZBANK() != null) {
            _hashCode += getZBANK().hashCode();
        }
        if (getACCNAME() != null) {
            _hashCode += getACCNAME().hashCode();
        }
        if (getXBLCK() != null) {
            _hashCode += getXBLCK().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ZSFI_VENDOR.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_VENDOR"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("LIFNR");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LIFNR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("NAME1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NAME1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZGYLX");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZGYLX"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AKONT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AKONT"));
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
        elemField.setFieldName("ZBANK");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZBANK"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ACCNAME");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ACCNAME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("XBLCK");
        elemField.setXmlName(new javax.xml.namespace.QName("", "XBLCK"));
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
