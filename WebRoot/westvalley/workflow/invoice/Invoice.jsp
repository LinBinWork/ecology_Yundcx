<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.westvalley.util.LogUtil" %>
<%
    RecordSet rs = new RecordSet();
    String fplx = Util.null2String(request.getParameter("fplx")).trim();//发票类型
    String sql = "select id from uf_FPLXB where fplx = '" + fplx + "'";
    rs.executeQuery(sql);
    LogUtil log = LogUtil.getLogger(getClass());
    log.d("sql==" + sql);
    JSONObject json = new JSONObject();
    String id = "";
    if (rs.next()) {
        id = rs.getString("id");
    }
    json.put("id", id);
    out.print(json);
%>