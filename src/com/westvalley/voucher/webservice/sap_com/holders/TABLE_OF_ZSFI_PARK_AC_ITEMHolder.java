/**
 * TABLE_OF_ZSFI_PARK_AC_ITEMHolder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.voucher.webservice.sap_com.holders;

import java.util.Arrays;

public final class TABLE_OF_ZSFI_PARK_AC_ITEMHolder implements javax.xml.rpc.holders.Holder {
    public com.westvalley.voucher.webservice.sap_com.ZSFI_PARK_AC_ITEM[] value;

    public TABLE_OF_ZSFI_PARK_AC_ITEMHolder() {
    }

    public TABLE_OF_ZSFI_PARK_AC_ITEMHolder(com.westvalley.voucher.webservice.sap_com.ZSFI_PARK_AC_ITEM[] value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TABLE_OF_ZSFI_PARK_AC_ITEMHolder{" +
                "value=" + Arrays.toString(value) +
                '}';
    }
}
