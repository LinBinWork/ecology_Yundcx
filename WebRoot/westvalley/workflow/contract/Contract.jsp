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
                json.put("money", proj0Entity.getProjBalance());
            } else if (type.equals("1")) {
                //父项
                Proj1Service proj1Service = new Proj1Service();
                Proj1Entity proj1Entity = proj1Service.getProj1(budgetEntity.getId(), null);
                json.put("money", proj1Entity.getProjBalance());
            } else if (type.equals("2")) {
                //子项
                Proj2Service proj2Service = new Proj2Service();
                Proj2Entity proj2Entity = proj2Service.getProj2(budgetEntity.getId(), null);
                json.put("money", proj2Entity.getProjBalance());
            } else if (type.equals("3")) {
                //孙子项
                Proj3Service proj3Service = new Proj3Service();
                Proj3Entity proj3Entity = proj3Service.getProj3(budgetEntity.getId(), null);
                json.put("money", proj3Entity.getProjBalance());
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
                /*String sql = "select t2.zxxmjl as 'zxxmjl',t3.fxxmjl as 'sjxmjl',t4.lastname as 'zxxmName',t7.lastname as 'fxxmName' from uf_proj t1 " +
                        " left join formtable_main_21_dt1 t2 on t1.detailID = t2.id " +
                        " left join formtable_main_21 t3 on t2.mainid = t3.id " +
                        " left join hrmresource t4 on t2.zxxmjl = t4.id " +
                        " left join uf_proj t5 on t3.glfx = t5.id " +
                        " left join formtable_main_18 t6 on t6.requestId = t5.reqID " +
                        " left join hrmresource t7 on t6.xmjl = t7.id " +
                        " where t1.id = '" + id + "'";*/
                String sql = "select * from WV_V_SubitemProject where id = ?";
                log.d("id === ", id);
                log.d("sql == ", sql);
                rs.executeQuery(sql);
                if (rs.next()) {
                    json.put("xmjl", rs.getString("zxxmjl"));
                    json.put("jlname", rs.getString("zxxmName"));
                    json.put("sjxmjl", rs.getString("fxxmjl"));
                    json.put("sjname", rs.getString("fxxmName"));
                } else {
                    json.put("xmjl", "");
                    json.put("jlname", "");
                    json.put("sjxmjl", "");
                    json.put("sjname", "");
                }
            } else if (type.equals("3")) {
                //孙子项
                /*String sql = "select t2.szxxmjl as 'zxxmjl',t3.zxxmjl as 'fxxmjl',t4.lastname as 'zxxmName',t5.lastname as 'fxxmName' from uf_proj t1 " +
                        " left join formtable_main_25_dt1 t2 on t1.detailID = t2.id " +
                        " left join formtable_main_25 t3 on t2.mainid = t3.id " +
                        " left join hrmresource t4 on t2.szxxmjl = t4.id " +
                        " left join hrmresource t5 on t3.zxxmjl = t5.id " +
                        " where t1.id = '" + id + "'";*/
                String sql = "select * from WV_V_GrandsonProject where id = ?";
                log.d("id === ",id);
                rs.executeQuery(sql);
                if (rs.next()) {
                    json.put("xmjl", rs.getString("zxxmjl"));
                    json.put("jlname", rs.getString("zxxmName"));
                    json.put("sjxmjl", rs.getString("fxxmjl"));
                    json.put("sjname", rs.getString("fxxmName"));
                } else {
                    json.put("xmjl", "");
                    json.put("jlname", "");
                    json.put("sjxmjl", "");
                    json.put("sjname", "");
                }
            } else {
                json.put("xmjl", "");
                json.put("sjxmjl", "");
            }
        }
    }
    out.print(json);
%>