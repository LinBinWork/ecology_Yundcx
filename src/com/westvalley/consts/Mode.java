package com.westvalley.consts;

import com.westvalley.util.LogUtil;
import weaver.file.Prop;

public class Mode {

    private LogUtil log = LogUtil.getLogger(getClass());

    /*
     * 以下为获取payment配置文件
     */
    private static final String PAYMENT = "payment";
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

    private static String PAYINFO_ID;//营销合同台账建模ID


    static {
        PAYINFO_ID = getPaymentValue("payinfoId");
    }

    public static String getPayinfoId() {
        return PAYINFO_ID;
    }
}
