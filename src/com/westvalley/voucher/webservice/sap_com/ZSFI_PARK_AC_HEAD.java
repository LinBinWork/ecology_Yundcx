/**
 * ZSFI_PARK_AC_HEAD.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.voucher.webservice.sap_com;

public class ZSFI_PARK_AC_HEAD  implements java.io.Serializable {
    private String ITEM;

    private String BUKRS;

    private String BLDAT;

    private String BUDAT;

    private String BLART;

    private String WAERS;

    private String HWAER;

    private java.math.BigDecimal KURSF;

    private String BKTXT;

    public ZSFI_PARK_AC_HEAD() {
    }

    public ZSFI_PARK_AC_HEAD(
           String ITEM,
           String BUKRS,
           String BLDAT,
           String BUDAT,
           String BLART,
           String WAERS,
           String HWAER,
           java.math.BigDecimal KURSF,
           String BKTXT) {
           this.ITEM = ITEM;
           this.BUKRS = BUKRS;
           this.BLDAT = BLDAT;
           this.BUDAT = BUDAT;
           this.BLART = BLART;
           this.WAERS = WAERS;
           this.HWAER = HWAER;
           this.KURSF = KURSF;
           this.BKTXT = BKTXT;
    }


    @Override
    public String toString() {
        return "ZSFI_PARK_AC_HEAD{" +
                "ITEM='" + ITEM + '\'' +
                ", BUKRS='" + BUKRS + '\'' +
                ", BLDAT='" + BLDAT + '\'' +
                ", BUDAT='" + BUDAT + '\'' +
                ", BLART='" + BLART + '\'' +
                ", WAERS='" + WAERS + '\'' +
                ", HWAER='" + HWAER + '\'' +
                ", KURSF=" + KURSF +
                ", BKTXT='" + BKTXT + '\'' +
                ", __equalsCalc=" + __equalsCalc +
                ", __hashCodeCalc=" + __hashCodeCalc +
                '}';
    }

    /**
     * Gets the ITEM value for this ZSFI_PARK_AC_HEAD.
     * 
     * @return ITEM
     */
    public String getITEM() {
        return ITEM;
    }


    /**
     * Sets the ITEM value for this ZSFI_PARK_AC_HEAD.
     * 
     * @param ITEM
     */
    public void setITEM(String ITEM) {
        this.ITEM = ITEM;
    }


    /**
     * Gets the BUKRS value for this ZSFI_PARK_AC_HEAD.
     * 
     * @return BUKRS
     */
    public String getBUKRS() {
        return BUKRS;
    }


    /**
     * Sets the BUKRS value for this ZSFI_PARK_AC_HEAD.
     * 
     * @param BUKRS
     */
    public void setBUKRS(String BUKRS) {
        this.BUKRS = BUKRS;
    }


    /**
     * Gets the BLDAT value for this ZSFI_PARK_AC_HEAD.
     * 
     * @return BLDAT
     */
    public String getBLDAT() {
        return BLDAT;
    }


    /**
     * Sets the BLDAT value for this ZSFI_PARK_AC_HEAD.
     * 
     * @param BLDAT
     */
    public void setBLDAT(String BLDAT) {
        this.BLDAT = BLDAT;
    }


    /**
     * Gets the BUDAT value for this ZSFI_PARK_AC_HEAD.
     * 
     * @return BUDAT
     */
    public String getBUDAT() {
        return BUDAT;
    }


    /**
     * Sets the BUDAT value for this ZSFI_PARK_AC_HEAD.
     * 
     * @param BUDAT
     */
    public void setBUDAT(String BUDAT) {
        this.BUDAT = BUDAT;
    }


    /**
     * Gets the BLART value for this ZSFI_PARK_AC_HEAD.
     * 
     * @return BLART
     */
    public String getBLART() {
        return BLART;
    }


    /**
     * Sets the BLART value for this ZSFI_PARK_AC_HEAD.
     * 
     * @param BLART
     */
    public void setBLART(String BLART) {
        this.BLART = BLART;
    }


    /**
     * Gets the WAERS value for this ZSFI_PARK_AC_HEAD.
     * 
     * @return WAERS
     */
    public String getWAERS() {
        return WAERS;
    }


    /**
     * Sets the WAERS value for this ZSFI_PARK_AC_HEAD.
     * 
     * @param WAERS
     */
    public void setWAERS(String WAERS) {
        this.WAERS = WAERS;
    }


    /**
     * Gets the HWAER value for this ZSFI_PARK_AC_HEAD.
     * 
     * @return HWAER
     */
    public String getHWAER() {
        return HWAER;
    }


    /**
     * Sets the HWAER value for this ZSFI_PARK_AC_HEAD.
     * 
     * @param HWAER
     */
    public void setHWAER(String HWAER) {
        this.HWAER = HWAER;
    }


    /**
     * Gets the KURSF value for this ZSFI_PARK_AC_HEAD.
     * 
     * @return KURSF
     */
    public java.math.BigDecimal getKURSF() {
        return KURSF;
    }


    /**
     * Sets the KURSF value for this ZSFI_PARK_AC_HEAD.
     * 
     * @param KURSF
     */
    public void setKURSF(java.math.BigDecimal KURSF) {
        this.KURSF = KURSF;
    }


    /**
     * Gets the BKTXT value for this ZSFI_PARK_AC_HEAD.
     * 
     * @return BKTXT
     */
    public String getBKTXT() {
        return BKTXT;
    }


    /**
     * Sets the BKTXT value for this ZSFI_PARK_AC_HEAD.
     * 
     * @param BKTXT
     */
    public void setBKTXT(String BKTXT) {
        this.BKTXT = BKTXT;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ZSFI_PARK_AC_HEAD)) return false;
        ZSFI_PARK_AC_HEAD other = (ZSFI_PARK_AC_HEAD) obj;
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
            ((this.BLDAT==null && other.getBLDAT()==null) || 
             (this.BLDAT!=null &&
              this.BLDAT.equals(other.getBLDAT()))) &&
            ((this.BUDAT==null && other.getBUDAT()==null) || 
             (this.BUDAT!=null &&
              this.BUDAT.equals(other.getBUDAT()))) &&
            ((this.BLART==null && other.getBLART()==null) || 
             (this.BLART!=null &&
              this.BLART.equals(other.getBLART()))) &&
            ((this.WAERS==null && other.getWAERS()==null) || 
             (this.WAERS!=null &&
              this.WAERS.equals(other.getWAERS()))) &&
            ((this.HWAER==null && other.getHWAER()==null) || 
             (this.HWAER!=null &&
              this.HWAER.equals(other.getHWAER()))) &&
            ((this.KURSF==null && other.getKURSF()==null) || 
             (this.KURSF!=null &&
              this.KURSF.equals(other.getKURSF()))) &&
            ((this.BKTXT==null && other.getBKTXT()==null) || 
             (this.BKTXT!=null &&
              this.BKTXT.equals(other.getBKTXT())));
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
        if (getBLDAT() != null) {
            _hashCode += getBLDAT().hashCode();
        }
        if (getBUDAT() != null) {
            _hashCode += getBUDAT().hashCode();
        }
        if (getBLART() != null) {
            _hashCode += getBLART().hashCode();
        }
        if (getWAERS() != null) {
            _hashCode += getWAERS().hashCode();
        }
        if (getHWAER() != null) {
            _hashCode += getHWAER().hashCode();
        }
        if (getKURSF() != null) {
            _hashCode += getKURSF().hashCode();
        }
        if (getBKTXT() != null) {
            _hashCode += getBKTXT().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ZSFI_PARK_AC_HEAD.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_PARK_AC_HEAD"));
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
        elemField.setFieldName("BLDAT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BLDAT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BUDAT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BUDAT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BLART");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BLART"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("WAERS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "WAERS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("HWAER");
        elemField.setXmlName(new javax.xml.namespace.QName("", "HWAER"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("KURSF");
        elemField.setXmlName(new javax.xml.namespace.QName("", "KURSF"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BKTXT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BKTXT"));
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
