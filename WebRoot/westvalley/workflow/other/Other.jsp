<%@ page import="weaver.general.Util" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="com.westvalley.util.LogUtil" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
    String cmd = Util.null2String(request.getParameter("cmd"));
    JSONObject json = new JSONObject();
    LogUtil log = LogUtil.getLogger(getClass());
    RecordSet rs = new RecordSet();
    if ("getKjkm".equals(cmd)) {
        //业务（预算）科目带出会计科目
        String id = Util.null2String(request.getParameter("id"));
        String sql = "select t1.HKONT as 'kjkmbm',t1.TxT50 as 'kjkmmc' from OA_SAP_KJKM t1 " +
                " left join uf_FYKM_KJKM_DYGX t2 on t1.HKONT = t2.saphjkmbm where t2.id = " + id;
        log.d("sql ===", sql);
        rs.execute(sql);
        if (rs.next()) {
            json.put("kjkmbm", rs.getString("kjkmbm"));//编码
            json.put("kjkmmc", rs.getString("kjkmmc"));//名称
        }
    }
    out.print(json);
%>