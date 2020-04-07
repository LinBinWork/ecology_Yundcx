/**
 * TABLE_OF_ZSFI_PARK_AC_HEADHolder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.voucher.webservice.sap_com.holders;

import java.util.Arrays;

public final class TABLE_OF_ZSFI_PARK_AC_HEADHolder implements javax.xml.rpc.holders.Holder {
    public com.westvalley.voucher.webservice.sap_com.ZSFI_PARK_AC_HEAD[] value;

    public TABLE_OF_ZSFI_PARK_AC_HEADHolder() {
    }

    public TABLE_OF_ZSFI_PARK_AC_HEADHolder(com.westvalley.voucher.webservice.sap_com.ZSFI_PARK_AC_HEAD[] value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TABLE_OF_ZSFI_PARK_AC_HEADHolder{" +
                "value=" + Arrays.toString(value) +
                '}';
    }
}
