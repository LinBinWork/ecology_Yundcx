<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.*" %>
<%@page import="org.json.JSONObject" %>
<%@ page import="crivia.time.carry.Time" %>
<%@ page import="com.westvalley.sync.data.DataSyn" %>
<%
    JSONObject json = new JSONObject();
/** ====================手工同步==================== **/
    String synName = Util.null2String(request.getParameter("synName"));
    //手工同步
    boolean isOk = true;
    String msg = "";
    RecordSet rs = new RecordSet();
    Time startTime = Time.now();
    rs.writeLog("手动同步数据开始时间：" + startTime.getDay() + " " + startTime + " " + synName);
    try {
        DataSyn dataSyn = new DataSyn();
        isOk = dataSyn.synData(synName);
        if (isOk) {
            msg = "同步完成！";
        } else {
            msg = "同步失败！";
        }
    } catch (Exception e) {
        rs.writeLog("手动同步数据出现异常：", e);
        msg = "同步失败！";
        isOk = false;
    }
    Time endTime = Time.now();
    rs.writeLog("手动同步数据完成" + endTime.getDay() + endTime);
    int tMs = endTime.toMS() - startTime.toMS();
    rs.writeLog("手动同步数据共耗时" + Time.toTime(tMs));
    json.put("isOK", isOk);
    json.put("msg", msg);
    out.print(json);
%>