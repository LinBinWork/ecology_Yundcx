package com.westvalley.consts;

import com.westvalley.util.LogUtil;
import weaver.file.Prop;

public class Sync {

    private LogUtil log = LogUtil.getLogger(getClass());

    /*
     * 以下为获取payment配置文件
     */
    private static final String PAYMENT = "payment_sync";
    private static String mode = Prop.getPropValue(PAYMENT, "mode");

    /**
     * 获取payment.properties文件的值
     *
     * @param key
     * @return
     */
    private static String getPaymentValue(String key) {
        return Prop.getPropValue(PAYMENT, mode + "." + key);
    }

    private static String URL;//请求地址
    private static String USERNAME;//接口账号
    private static String PASSWORD;//接口密码


    static {
        URL = getPaymentValue("url");
        USERNAME = getPaymentValue("userName");
        PASSWORD = getPaymentValue("passWord");
    }

    public static String getURL() {
        return URL;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }
}
