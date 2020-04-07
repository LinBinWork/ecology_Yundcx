/**
 * ZFFI_02_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.sync.webservice.sap_com;

public class ZFFI_02_ServiceLocator extends org.apache.axis.client.Service implements ZFFI_02_Service {

    public ZFFI_02_ServiceLocator() {
    }


    public ZFFI_02_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ZFFI_02_ServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ZFFI_02
    private String ZFFI_02_address = "http://ERPDEV01.radio1964.com:8010/sap/bc/srt/rfc/sap/zffi_02/300/zffi_02/zffi_02";

    public String getZFFI_02Address() {
        return ZFFI_02_address;
    }

    // The WSDD service name defaults to the port name.
    private String ZFFI_02WSDDServiceName = "ZFFI_02";

    public String getZFFI_02WSDDServiceName() {
        return ZFFI_02WSDDServiceName;
    }

    public void setZFFI_02WSDDServiceName(String name) {
        ZFFI_02WSDDServiceName = name;
    }

    public ZFFI_02_PortType getZFFI_02() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ZFFI_02_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getZFFI_02(endpoint);
    }

    public ZFFI_02_PortType getZFFI_02(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ZFFI_02_BindingStub _stub = new ZFFI_02_BindingStub(portAddress, this);
            _stub.setPortName(getZFFI_02WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setZFFI_02EndpointAddress(String address) {
        ZFFI_02_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ZFFI_02_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                ZFFI_02_BindingStub _stub = new ZFFI_02_BindingStub(new java.net.URL(ZFFI_02_address), this);
                _stub.setPortName(getZFFI_02WSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("ZFFI_02".equals(inputPortName)) {
            return getZFFI_02();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZFFI_02");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZFFI_02"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("ZFFI_02".equals(portName)) {
            setZFFI_02EndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
