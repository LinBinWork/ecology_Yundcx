<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.westvalley.project.service.BudgetService" %>
<%@ page import="com.westvalley.project.entity.ProjEntity" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="com.westvalley.action.borrow.Borrow" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="com.westvalley.util.LogUtil" %>
<%
    String cmd = Util.null2String(request.getParameter("cmd"));
    JSONObject json = new JSONObject();
    LogUtil log = LogUtil.getLogger(getClass());
    RecordSet rs = new RecordSet();
    if ("getProject".equals(cmd)) {
        String id = Util.null2String(request.getParameter("id"));
        BudgetService budgetService = new BudgetService();
        ProjEntity projEntity = budgetService.getProjInfo(Integer.valueOf(id), null);
        log.d("projEntity == ", projEntity);
        json.put("projBalance", projEntity.getProjBalance());
        if (projEntity.getProjType() == 2) {
            //子项
            /*String sql = "select t2.fxxmjl,t3.zxxmjl,t4.lastname as 'zxxmName' " +
                    "from uf_proj t1 " +
                    "left join formtable_main_21 t2 on t1.reqID = t2.requestId " +
                    "left join formtable_main_21_dt1 t3 on t2.id = t3.mainid " +
                    "left join hrmresource t4 on t4.id = t3.zxxmjl " +
                    "where t1.id = ?";*/
            String sql = "select * from WV_V_Subitem where id = ? ";
            log.d("sql === ", sql);
            log.d("id === ", id);
            rs.executeQuery(sql, id);
            if (rs.next()) {
                Map map = new HashMap();
                map.put("fxxmjl", rs.getString("fxxmjl"));
                int hrmId = rs.getInt("fxxmjl");
                String sql2 = "select lastname from hrmresource where id = " + hrmId;
                log.d("sql2 ==== ", sql2);
                RecordSet rs2 = new RecordSet();
                rs2.executeQuery(sql2);
                if (rs2.next()) {
                    map.put("fxxmName", rs2.getString("lastname"));
                }
                json.put("fx", map);
                Map map1 = new HashMap();
                map1.put("zxxmjl", rs.getString("zxxmjl"));
                map1.put("zxxmName", rs.getString("zxxmName"));
                json.put("zx", map1);
            }
        } else if (projEntity.getProjType() == 3) {
            //孙子项
            /*String sql = "select t2.zxxmjl,t2.fxxmjl,t4.lastname as 'fxxmName',t5.lastname as 'zxxmName' " +
                    "from uf_proj t1 " +
                    "left join formtable_main_25 t2 on t1.reqID = t2.requestId " +
                    "left join formtable_main_25_dt1  t3 on t2.id = t3.mainid " +
                    "left join hrmresource t4 on t4.id = t2.zxxmjl " +
                    "left join hrmresource t5 on t5.id = t2.fxxmjl " +
                    "where t1.id = ?";*/
            String sql = "select * from WV_V_Subitem where id = ?";
            log.d("id ========= ", id);
            rs.executeQuery(sql, id);
            if (rs.next()) {
                Map map = new HashMap();
                map.put("fxxmjl", rs.getString("fxxmjl"));
                map.put("fxxmName", rs.getString("fxxmName"));
                json.put("fx", map);
                Map map1 = new HashMap();
                map1.put("zxxmjl", rs.getString("zxxmjl"));
                map1.put("zxxmName", rs.getString("zxxmName"));
                json.put("zx", map1);
            }
        }
    } else if ("getBorrow".equals(cmd)) {
        String userid = Util.null2String(request.getParameter("userid"));
        log.d("userId ====", userid);
        String requestId = Util.null2String(request.getParameter("requestId"));
        log.d("requestId ===", requestId);
        String sql = Borrow.getBorSQL(userid, requestId);
        log.d("sql ===", sql);
        json = Borrow.rsToJson(sql);
    } else if ("getDepartment".equals(cmd)) {
        String bmbm = Util.null2String(request.getParameter("bmbm"));//部门编码
        String sql = "select t2.sapcbzxbm as 'sapcbzxbm',t3.LTEXT as 'sapcbzxmc' from HrmDepartment t1 " +
                " left join uf_uf_OA_SAP_BMCB t2 on t1.departmentcode = t2.oabmbm " +
                " left join OA_SAP_Cbzx t3 on t2.sapcbzxmc = t3.id " +
                " where t1.id = '" + bmbm + "'";
        log.d("sql ===", sql);
        rs.executeQuery(sql);
        if (rs.next()) {
            json.put("sapcbzxbm", rs.getString("sapcbzxbm"));
            json.put("sapcbzxmc", rs.getString("sapcbzxmc"));
        }
    }
    out.print(json);
%>
