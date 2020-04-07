<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.westvalley.project.service.Proj1Service" %>
<%@ page import="com.westvalley.project.entity.Proj1Entity" %>
<%@ page import="com.westvalley.util.LogUtil" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="com.westvalley.project.entity.ProjEntity" %>
<%@ page import="com.westvalley.project.service.BudgetService" %>
<%
    String cmd = Util.null2String(request.getParameter("cmd"));
    JSONObject json = new JSONObject();
    LogUtil log = LogUtil.getLogger(getClass());
    if (cmd.equals("getProjectData")) {
        String id = Util.null2String(request.getParameter("id"));
        Proj1Service proj1Service = new Proj1Service();
        Proj1Entity proj1Entity = proj1Service.getProj1(Integer.valueOf(id), null);//获取预算
        log.d("proj1Entity === ", proj1Entity);
        json.put("data", proj1Entity);
    } else if (cmd.equals("getProjectList")) {
        String id = Util.null2String(request.getParameter("id"));
        StringBuffer ids = new StringBuffer();
        ids.append(id + "");
        String sql = "select t2.id as 'id' from uf_proj t1 " +
                " left join uf_proj t2 on t1.id = t2.pid " +
                " where t2.id is not null and t1.id = ? " +
                " union all " +
                " select t3.id as 'id' from uf_proj t1 " +
                " left join uf_proj t2 on t1.id = t2.pid " +
                " left join uf_proj t3 on t2.id = t3.pid " +
                " where t3.id is not null and t1.id = ? ";
        log.d("sql ==== ", sql);
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql, id, id);
        List<ProjEntity> projEntities = new ArrayList<>();
        BudgetService budgetService = new BudgetService();
        while (rs.next()) {
            projEntities.add(budgetService.getProjInfo(rs.getInt("id"), null));
        }
        json.put("data", projEntities);
    }
    out.print(json);
%>