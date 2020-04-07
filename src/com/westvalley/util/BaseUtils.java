package com.westvalley.util;

import weaver.soa.workflow.request.RequestInfo;

public class BaseUtils {

    public static void setRequestErrorMessage(RequestInfo reqInfo, String msg) {
        reqInfo.getRequestManager().setMessageid(reqInfo.getRequestid());
        reqInfo.getRequestManager().setMessagecontent(msg + "<br/>" + errorMsg(reqInfo.getRequestid()));
        reqInfo.getRequestManager().setMessage(reqInfo.getRequestManager().getRequestname());
    }

    private static String errorMsg(String requestId) {
        return "当前单据标识<b>" + requestId + "</b>"
                + "，请将此标识发送给管理员解决此问题;";
    }
}
