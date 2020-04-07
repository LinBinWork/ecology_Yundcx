package com.westvalley.voucher.webservice.sap_com;

public class ZFFI_01Proxy implements com.westvalley.voucher.webservice.sap_com.ZFFI_01_PortType {
  private String _endpoint = null;
  private com.westvalley.voucher.webservice.sap_com.ZFFI_01_PortType zFFI_01_PortType = null;
  
  public ZFFI_01Proxy() {
    _initZFFI_01Proxy();
  }
  
  public ZFFI_01Proxy(String endpoint) {
    _endpoint = endpoint;
    _initZFFI_01Proxy();
  }
  
  private void _initZFFI_01Proxy() {
    try {
      zFFI_01_PortType = (new com.westvalley.voucher.webservice.sap_com.ZFFI_01_ServiceLocator()).getZFFI_01();
      if (zFFI_01_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)zFFI_01_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)zFFI_01_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (zFFI_01_PortType != null)
      ((javax.xml.rpc.Stub)zFFI_01_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.westvalley.voucher.webservice.sap_com.ZFFI_01_PortType getZFFI_01_PortType() {
    if (zFFI_01_PortType == null)
      _initZFFI_01Proxy();
    return zFFI_01_PortType;
  }
  
  public void ZFFI_01(com.westvalley.voucher.webservice.sap_com.holders.TABLE_OF_ZSFI_PARK_AC_ITEMHolder ZTFI_MX, com.westvalley.voucher.webservice.sap_com.holders.TABLE_OF_ZSFI_PARK_AC_RETHolder ZTFI_SC, com.westvalley.voucher.webservice.sap_com.holders.TABLE_OF_ZSFI_PARK_AC_HEADHolder ZTFI_TT) throws java.rmi.RemoteException{
    if (zFFI_01_PortType == null)
      _initZFFI_01Proxy();
    zFFI_01_PortType.ZFFI_01(ZTFI_MX, ZTFI_SC, ZTFI_TT);
  }
  
  
}