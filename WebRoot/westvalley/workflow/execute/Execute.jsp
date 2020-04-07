<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.westvalley.service.ProjectService" %>
<%@ page import="com.westvalley.entity.BudgetEntity" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="com.westvalley.project.service.*" %>
<%@ page import="com.westvalley.project.entity.*" %>
<%@ page import="com.westvalley.util.LogUtil" %>
<%
    String op = Util.null2String(request.getParameter("op"));
    JSONObject json = new JSONObject();
    LogUtil log = LogUtil.getLogger(getClass());
    if (op.equals("getMoney")) {
        String id = Util.null2String(request.getParameter("id"));
        ProjectService projectService = new ProjectService();
        BudgetEntity budgetEntity = projectService.getBudgetEntityById(Integer.valueOf(id));
        log.d("budgetEntity :", budgetEntity);
        if (budgetEntity != null) {
            String type = budgetEntity.getProjType();//项目类型
            BudgetService budgetService = new BudgetService();
            ProjEntity projEntity = budgetService.getProjInfo(budgetEntity.getId(), null);
            if (type.equals("0")) {
                //祖项
                Proj0Service proj0Service = new Proj0Service();
                Proj0Entity proj0Entity = proj0Service.getProj0(budgetEntity.getId(), null);
                json.put("money", proj0Entity.getProjAmt());
            } else if (type.equals("1")) {
                //父项
                Proj1Service proj1Service = new Proj1Service();
                Proj1Entity proj1Entity = proj1Service.getProj1(budgetEntity.getId(), null);
                json.put("money", proj1Entity.getProjAmt());
            } else if (type.equals("2")) {
                //子项
                Proj2Service proj2Service = new Proj2Service();
                Proj2Entity proj2Entity = proj2Service.getProj2(budgetEntity.getId(), null);
                json.put("money", proj2Entity.getProjAmt());
            } else if (type.equals("3")) {
                //孙子项
                Proj3Service proj3Service = new Proj3Service();
                Proj3Entity proj3Entity = proj3Service.getProj3(budgetEntity.getId(), null);
                json.put("money", proj3Entity.getProjAmt());
            }
        } else {
            json.put("money", 0.00);
        }
    } else if (op.equals("getManager")) {
        String id = Util.null2String(request.getParameter("id"));
        ProjectService projectService = new ProjectService();
        BudgetEntity budgetEntity = projectService.getBudgetEntityById(Integer.valueOf(id));
        if (budgetEntity != null) {
            String type = budgetEntity.getProjType();//项目类型
            RecordSet rs = new RecordSet();
            if (type.equals("2")) {
                //子项
                /*String sql = "select t2.zxxmjl as 'xmjl',t2.zxxmfzr as 'zxxmfzr',t4.lastname as 'jlname',t5.lastname as 'zxxmfzrname' from uf_proj t1 " +
                        " left join formtable_main_21_dt1 t2 on t1.detailID = t2.id " +
                        " left join hrmresource t4 on t2.zxxmjl = t4.id " +
                        " left join hrmresource t5 on t2.zxxmfzr = t5.id " +
                        " where t1.id = '" + id + "'";*/
                String sql = "select * from WV_V_SubitemManager where id = ? ";
                log.d("sql == ", sql);
                log.d("id == ", id);
                rs.executeQuery(sql,id);
                if (rs.next()) {
                    json.put("xmjl", rs.getString("xmjl"));
                    json.put("jlname", rs.getString("jlname"));
                    json.put("zxxmfzr", rs.getString("zxxmfzr"));
                    json.put("zxxmfzrname", rs.getString("zxxmfzrname"));
                } else {
                    json.put("xmjl", "");
                    json.put("jlname", "");
                    json.put("zxxmfzr", "");
                    json.put("zxxmfzrname", "");
                }
            }
        }
    }
    out.print(json);
%>