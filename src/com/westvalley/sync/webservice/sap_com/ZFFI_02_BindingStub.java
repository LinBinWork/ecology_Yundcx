/**
 * ZFFI_02_BindingStub.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.sync.webservice.sap_com;

import com.westvalley.sync.webservice.sap_com.holders.*;

public class ZFFI_02_BindingStub extends org.apache.axis.client.Stub implements ZFFI_02_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc[] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[1];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ZFFI_02");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ET_ACCOUNT"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "TABLE_OF_ZSFI_ACCOUNT"), ZSFI_ACCOUNT[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("", "item"));
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ET_ASSET"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "TABLE_OF_ZSFI_ASSET"), ZSFI_ASSET[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("", "item"));
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ET_COMPANY_CODE"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "TABLE_OF_ZSFI_COMPANY_CODE"), ZSFI_COMPANY_CODE[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("", "item"));
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ET_COST_CENTER"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "TABLE_OF_ZSFI_COST_CENTER"), ZSFI_COST_CENTER[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("", "item"));
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ET_VENDOR"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "TABLE_OF_ZSFI_VENDOR"), ZSFI_VENDOR[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("", "item"));
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

    }

    public ZFFI_02_BindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public ZFFI_02_BindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public ZFFI_02_BindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
        Class cls;
        javax.xml.namespace.QName qName;
        javax.xml.namespace.QName qName2;
        Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
        Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
        Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
        Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
        Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
        Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
        Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
        Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
        Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
        Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "char1");
        cachedSerQNames.add(qName);
        cls = String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "char10");
        cachedSerQNames.add(qName);
        cls = String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "char12");
        cachedSerQNames.add(qName);
        cls = String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "char25");
        cachedSerQNames.add(qName);
        cls = String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "char4");
        cachedSerQNames.add(qName);
        cls = String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "char40");
        cachedSerQNames.add(qName);
        cls = String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "char50");
        cachedSerQNames.add(qName);
        cls = String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "char8");
        cachedSerQNames.add(qName);
        cls = String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "lang");
        cachedSerQNames.add(qName);
        cls = String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "TABLE_OF_ZSFI_ACCOUNT");
        cachedSerQNames.add(qName);
        cls = ZSFI_ACCOUNT[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_ACCOUNT");
        qName2 = new javax.xml.namespace.QName("", "item");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "TABLE_OF_ZSFI_ASSET");
        cachedSerQNames.add(qName);
        cls = ZSFI_ASSET[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_ASSET");
        qName2 = new javax.xml.namespace.QName("", "item");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "TABLE_OF_ZSFI_COMPANY_CODE");
        cachedSerQNames.add(qName);
        cls = ZSFI_COMPANY_CODE[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_COMPANY_CODE");
        qName2 = new javax.xml.namespace.QName("", "item");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "TABLE_OF_ZSFI_COST_CENTER");
        cachedSerQNames.add(qName);
        cls = ZSFI_COST_CENTER[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_COST_CENTER");
        qName2 = new javax.xml.namespace.QName("", "item");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "TABLE_OF_ZSFI_VENDOR");
        cachedSerQNames.add(qName);
        cls = ZSFI_VENDOR[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_VENDOR");
        qName2 = new javax.xml.namespace.QName("", "item");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_ACCOUNT");
        cachedSerQNames.add(qName);
        cls = ZSFI_ACCOUNT.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_ASSET");
        cachedSerQNames.add(qName);
        cls = ZSFI_ASSET.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_COMPANY_CODE");
        cachedSerQNames.add(qName);
        cls = ZSFI_COMPANY_CODE.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_COST_CENTER");
        cachedSerQNames.add(qName);
        cls = ZSFI_COST_CENTER.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSFI_VENDOR");
        cachedSerQNames.add(qName);
        cls = ZSFI_VENDOR.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        Class cls = (Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            Class sf = (Class)
                                    cachedSerFactories.get(i);
                            Class df = (Class)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        } else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                    cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        } catch (Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public void ZFFI_02(TABLE_OF_ZSFI_ACCOUNTHolder ET_ACCOUNT, TABLE_OF_ZSFI_ASSETHolder ET_ASSET, TABLE_OF_ZSFI_COMPANY_CODEHolder ET_COMPANY_CODE, TABLE_OF_ZSFI_COST_CENTERHolder ET_COST_CENTER, TABLE_OF_ZSFI_VENDORHolder ET_VENDOR) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:sap-com:document:sap:rfc:functions:ZFFI_02:ZFFI_02Request");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZFFI_02"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{ET_ACCOUNT.value, ET_ASSET.value, ET_COMPANY_CODE.value, ET_COST_CENTER.value, ET_VENDOR.value});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                java.util.Map _output;
                _output = _call.getOutputParams();
                try {
                    ET_ACCOUNT.value = (ZSFI_ACCOUNT[]) _output.get(new javax.xml.namespace.QName("", "ET_ACCOUNT"));
                } catch (Exception _exception) {
                    ET_ACCOUNT.value = (ZSFI_ACCOUNT[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "ET_ACCOUNT")), ZSFI_ACCOUNT[].class);
                }
                try {
                    ET_ASSET.value = (ZSFI_ASSET[]) _output.get(new javax.xml.namespace.QName("", "ET_ASSET"));
                } catch (Exception _exception) {
                    ET_ASSET.value = (ZSFI_ASSET[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "ET_ASSET")), ZSFI_ASSET[].class);
                }
                try {
                    ET_COMPANY_CODE.value = (ZSFI_COMPANY_CODE[]) _output.get(new javax.xml.namespace.QName("", "ET_COMPANY_CODE"));
                } catch (Exception _exception) {
                    ET_COMPANY_CODE.value = (ZSFI_COMPANY_CODE[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "ET_COMPANY_CODE")), ZSFI_COMPANY_CODE[].class);
                }
                try {
                    ET_COST_CENTER.value = (ZSFI_COST_CENTER[]) _output.get(new javax.xml.namespace.QName("", "ET_COST_CENTER"));
                } catch (Exception _exception) {
                    ET_COST_CENTER.value = (ZSFI_COST_CENTER[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "ET_COST_CENTER")), ZSFI_COST_CENTER[].class);
                }
                try {
                    ET_VENDOR.value = (ZSFI_VENDOR[]) _output.get(new javax.xml.namespace.QName("", "ET_VENDOR"));
                } catch (Exception _exception) {
                    ET_VENDOR.value = (ZSFI_VENDOR[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "ET_VENDOR")), ZSFI_VENDOR[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

}
