package com.westvalley.sync.webservice.sap_com;

import com.westvalley.sync.webservice.sap_com.holders.*;

public class ZFFI_02Proxy implements ZFFI_02_PortType {
  private String _endpoint = null;
  private ZFFI_02_PortType zFFI_02_PortType = null;
  
  public ZFFI_02Proxy() {
    _initZFFI_02Proxy();
  }
  
  public ZFFI_02Proxy(String endpoint) {
    _endpoint = endpoint;
    _initZFFI_02Proxy();
  }
  
  private void _initZFFI_02Proxy() {
    try {
      zFFI_02_PortType = (new ZFFI_02_ServiceLocator()).getZFFI_02();
      if (zFFI_02_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)zFFI_02_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)zFFI_02_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (zFFI_02_PortType != null)
      ((javax.xml.rpc.Stub)zFFI_02_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ZFFI_02_PortType getZFFI_02_PortType() {
    if (zFFI_02_PortType == null)
      _initZFFI_02Proxy();
    return zFFI_02_PortType;
  }
  
  public void ZFFI_02(TABLE_OF_ZSFI_ACCOUNTHolder ET_ACCOUNT, TABLE_OF_ZSFI_ASSETHolder ET_ASSET, TABLE_OF_ZSFI_COMPANY_CODEHolder ET_COMPANY_CODE, TABLE_OF_ZSFI_COST_CENTERHolder ET_COST_CENTER, TABLE_OF_ZSFI_VENDORHolder ET_VENDOR) throws java.rmi.RemoteException{
    if (zFFI_02_PortType == null)
      _initZFFI_02Proxy();
    zFFI_02_PortType.ZFFI_02(ET_ACCOUNT, ET_ASSET, ET_COMPANY_CODE, ET_COST_CENTER, ET_VENDOR);
  }
  
  
}