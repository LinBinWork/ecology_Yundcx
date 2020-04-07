<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="com.westvalley.util.LogUtil" %>
<%@ page import="com.westvalley.action.payment.Payment" %>
<%
    String cmd = Util.null2String(request.getParameter("cmd"));
    JSONObject json = new JSONObject();
    LogUtil log = LogUtil.getLogger(getClass());
    RecordSet rs = new RecordSet();
    if ("getPayData".equals(cmd)) {
        String gysid = Util.null2String(request.getParameter("gysid"));
        log.d("gysid ====", gysid);
        String requestId = Util.null2String(request.getParameter("requestId"));
        log.d("requestId ===", requestId);
        String sql = Payment.getPayDataSQL(gysid, requestId);
        log.d("sql ===", sql);
        json = Payment.rsToJson(sql);
    }
    out.print(json);
%>
