<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.westvalley.util.LogUtil" %>
<%
    /**
     * OA部门和SAP成本中心对应关系
     * 主要功能：创建编辑提交校验OA部门是否已存在，存在则不允许提交
     */
    RecordSet rs = new RecordSet();
    String oabm = Util.null2String(request.getParameter("oabm"));//oa编码
    String sql = "select id from uf_uf_OA_SAP_BMCB where oabmbm = '" + oabm + "'";
    rs.execute(sql);
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