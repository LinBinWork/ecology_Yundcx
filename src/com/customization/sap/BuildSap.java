package com.customization.sap;

import com.westvalley.util.LogUtil;
import weaver.file.Prop;

public class BuildSap {

    private LogUtil log = LogUtil.getLogger(getClass());

    /*
     * 以下为获取sap配置文件
     */
    private static final String SAP = "sap";
    private static String mode = Prop.getPropValue(SAP, "mode");

    /**
     * 获取sap.properties文件的值
     *
     * @param key
     * @return
     */
    private static String getSapValue(String key) {
        return Prop.getPropValue(SAP, mode + "." + key);
    }

    private static SapConn sapConn = new SapConn();

    public static SapConn getSapConn(){
            sapConn.setJCO_ASHOST(getSapValue("host"));
            sapConn.setJCO_CLIENT(getSapValue("client"));
            sapConn.setJCO_LANG(getSapValue("lang"));
            sapConn.setJCO_PASSWD(getSapValue("passwd"));
            sapConn.setJCO_USER(getSapValue("user"));
            sapConn.setJCO_PEAK_LIMIT(getSapValue("limit"));
            sapConn.setJCO_POOL_CAPACITY(getSapValue("capacity"));
            sapConn.setJCO_SYSNR(getSapValue("sysnr"));
            sapConn.setJCO_SAPROUTER(getSapValue("router"));
        return  sapConn;
    }
}
