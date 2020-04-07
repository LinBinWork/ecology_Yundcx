<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.westvalley.util.LogUtil" %>
<%
    LogUtil log = LogUtil.getLogger(getClass());
    String xmbhs = Util.null2String(request.getParameter("xmbhs"));
    String[] xmbh = xmbhs.split("\\,");
    StringBuffer msg = new StringBuffer();
    JSONObject json = new JSONObject();
    if (xmbh.length == 0) {
        json.put("msg", msg.toString());
        out.print(json);
    } else {
        RecordSet rs = new RecordSet();
        for (String bh : xmbh) {
            log.d("项目编号:", bh);
            //查出项目是否需要执行方案
            /*String sql = "select t1.id as 'id',t1.projName as 'projName' from uf_proj t1 " +
                    " left join formtable_main_21_dt1 t2 on t1.projNo = t2.zxxmbh " +
                    " where t2.sffazx = '0' and t1.id = '" + bh + "'";*/
            String sql = "select * from WV_V_Execute where id = ?";
            log.d("sql:", sql);
            log.d("id:", bh);
            rs.executeQuery(sql, bh);
            if (rs.next()) {
                //如果存在需要执行方案申请的项目
                String id = rs.getString("id");
               /* String sql2 = "select * from formtable_main_35 t1" +
                        " left join workflow_requestbase t2 on t1.requestId = t2.requestId" +
                        " where t1.glxm = '" + id + "' and t2.currentnodetype = '3'";*/
                String sql2 = "select * from WV_V_ExistExecute where glxm = ?";
                log.d("sql2 ==== " + sql2);
                log.d("id ==== " + id);
                RecordSet rs2 = new RecordSet();
                rs2.executeQuery(sql2, id);
                if (rs2.next()) {

                } else {
                    //还未进行申请项目执行方案流程的项目 或者存在未归档的情况
                    if (msg == null || msg.equals("") || msg.toString().equals("")) {
                        msg.append(bh);
                    } else {
                        msg.append("," + bh);
                    }
                }
            }

        }
    }
    json.put("msg", msg.toString());
    out.print(json);
%>