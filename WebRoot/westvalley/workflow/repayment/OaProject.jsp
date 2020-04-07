<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.westvalley.project.service.BudgetService" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.westvalley.project.entity.ProjEntity" %>
<%@ page import="com.westvalley.action.borrow.Borrow" %>
<%@ page import="com.westvalley.util.LogUtil" %>

<%
    RecordSet rs = new RecordSet();
    JSONObject json = new JSONObject();
    LogUtil log = LogUtil.getLogger(getClass());
    String xmmc = Util.null2String(request.getParameter("xmmc"));
    int id = Integer.parseInt(xmmc);
    BudgetService budgetService = new BudgetService();
    ProjEntity en = budgetService.getProjInfo(id, null);
    int ProjID = en.getId();
    String ProjNo = en.getProjNo();
    String ProjName = en.getProjName();
    Double projBalance = en.getProjBalance();
    Double moneylast = 0.0;
    String gljklc = Util.null2String(request.getParameter("gljklc"));
    String sql = Borrow.getReqBorSQL(gljklc);
    log.d("sql ======= ", sql);
    rs.execute(sql);
    if (rs.next()) {
        moneylast = rs.getDouble("moneylast");
    }
    /*BaseBean bs = new BaseBean();
    bs.writeLog("sql==" + sql);*/
    json.put("ProjID", ProjID);
    json.put("ProjNo", ProjNo);
    json.put("ProjName", ProjName);
    json.put("projBalance", projBalance);
    json.put("moneylast", moneylast);
    out.print(json);
%>