/**
 * ZFFI_02_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.sync.webservice.sap_com;

import com.westvalley.sync.webservice.sap_com.holders.*;

public interface ZFFI_02_PortType extends java.rmi.Remote {
    public void ZFFI_02(TABLE_OF_ZSFI_ACCOUNTHolder ET_ACCOUNT, TABLE_OF_ZSFI_ASSETHolder ET_ASSET, TABLE_OF_ZSFI_COMPANY_CODEHolder ET_COMPANY_CODE, TABLE_OF_ZSFI_COST_CENTERHolder ET_COST_CENTER, TABLE_OF_ZSFI_VENDORHolder ET_VENDOR) throws java.rmi.RemoteException;
}
