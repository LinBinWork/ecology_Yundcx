package com.customization.sap;

public class Test {

    @org.junit.Test
    public void getConn(){
        SapConn sapConn = BuildSap.getSapConn();
        SapConnUtils.connect(sapConn);

    }
}
