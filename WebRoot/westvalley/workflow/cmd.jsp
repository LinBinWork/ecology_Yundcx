<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.westvalley.project.service.BudgetService" %>
<%@ page import="com.westvalley.project.entity.ProjEntity" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="com.westvalley.action.borrow.Borrow" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="com.westvalley.util.LogUtil" %>
<%@ page import="com.westvalley.util.DevUtil" %>
<%
    String cmd = Util.null2String(request.getParameter("cmd"));
    JSONObject json = new JSONObject();
    LogUtil log = LogUtil.getLogger(getClass());
    RecordSet rs = new RecordSet();
    if ("getBorrow".equals(cmd)) {
        //借款历史记录
        String userid = Util.null2String(request.getParameter("userid"));
        log.d("userId ====", userid);
        String requestId = Util.null2String(request.getParameter("requestId"));
        log.d("requestId ===", requestId);
        String sql = Borrow.getBorSQL(userid, requestId);
        log.d("sql ===", sql);
        json = Borrow.rsToJson(sql);
    } else if ("getDepartment".equals(cmd)) {
        //根据申请人部门带出成本中心
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
    } else if ("getBorrowSum".equals(cmd)) {
        //个人借款余额
        String userid = Util.null2String(request.getParameter("userid"));
        String requestId = Util.null2String(request.getParameter("requestId"));
        String sql = Borrow.getUserBorSQL(userid, requestId);
        log.d("sql ===", sql);
        json = Borrow.rsToJson(sql);
    } else if ("getProject".equals(cmd)) {
        //带出项目经理，上级经理
        String id = Util.null2String(request.getParameter("id"));
        BudgetService budgetService = new BudgetService();
        ProjEntity projEntity = budgetService.getProjInfo(Integer.valueOf(id), null);
        log.d("projEntity == ", projEntity);
        json.put("projBalance", projEntity.getProjBalance());
        if (projEntity.getProjType() == 2) {
            //子项
            /*String sql = "select t2.zxxmjl as 'zxxmjl',t3.fxxmjl as 'fxxmjl',t4.lastname as 'zxxmName',t7.lastname as 'fxxmName' from uf_proj t1 " +
                    " left join formtable_main_21_dt1 t2 on t1.detailID = t2.id " +
                    " left join formtable_main_21 t3 on t2.mainid = t3.id " +
                    " left join hrmresource t4 on t2.zxxmjl = t4.id " +
                    " left join uf_proj t5 on t3.glfx = t5.id " +
                    " left join formtable_main_18 t6 on t6.requestId = t5.reqID " +
                    " left join hrmresource t7 on t6.xmjl = t7.id " +
                    " where t1.id = '" + id + "'";*/
            String sql = "select * from WV_V_SubitemProject where id = ?";
            log.d("sql === ", sql);
            log.d("id === ", id);
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
        } else if (projEntity.getProjType() == 3) {
            //孙子项
            /*String sql = "select t2.szxxmjl as 'zxxmjl',t3.zxxmjl as 'fxxmjl',t4.lastname as 'zxxmName',t5.lastname as 'fxxmName' from uf_proj t1 " +
                    " left join formtable_main_25_dt1 t2 on t1.detailID = t2.id " +
                    " left join formtable_main_25 t3 on t2.mainid = t3.id " +
                    " left join hrmresource t4 on t2.szxxmjl = t4.id " +
                    " left join hrmresource t5 on t3.zxxmjl = t5.id " +
                    " where t1.id = '" + id + "'";*/
            String sql = "select * from WV_V_GrandsonProject where id = ?";
            log.d("id === ",id);
            rs.executeQuery(sql,id);
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
    } else if ("getGys".equals(cmd)) {
        //根据工号带出供应商(人员)
        String gh = Util.null2String(request.getParameter("gh"));
        String sql = "select * from OA_SAP_GYS where gh = '" + gh + "'";
        rs.execute(sql);
        log.d("sql ===", sql);
        if (rs.next()) {
            json.put("PARTNER", rs.getString("PARTNER"));//供应商编码
            json.put("NAME1", rs.getString("NAME1"));//供应商名称
        }
    } else if ("getFplx".equals(cmd)) {
        //根据发票类型带出供应商
        String fplx = Util.null2String(request.getParameter("fplx"));
        String sql = "select * from uf_FPLXB where id = " + fplx;
        rs.execute(sql);
        log.d("sql ===", sql);
        if (rs.next()) {
            json.put("sxbm", rs.getString("sxbm"));//税项编码
            json.put("sjkm", rs.getString("sjkm"));//税金科目
        }
    } else if ("getYwlx".equals(cmd)) {
        //业务类型带出会计科目
        String ywlx = Util.null2String(request.getParameter("ywlx"));
        String sql = "select t1.saphjkmbm as 'saphjkmbm',t2.TXT50 as 'saphjkmmc' from uf_FYKM_KJKM_DYGX t1 " +
                " left join OA_SAP_Kjkm t2 on t1.saphjkmbm = t2.hkont where t1.id = " + ywlx;
        rs.execute(sql);
        log.d("sql ===", sql);
        if (rs.next()) {
            json.put("saphjkmbm", rs.getString("saphjkmbm"));//编码
            json.put("saphjkmmc", rs.getString("saphjkmmc"));//名称
        }
    } else if ("getFxData".equals(cmd)) {
        //获取父项信息
        String id = Util.null2String(request.getParameter("id"));//子项/孙子项项目id
        log.d("id ===== ", id);
        String projType = DevUtil.executeQuery("select projType from uf_proj where id = ?", id);
        log.d("projType == ", projType);
        if (projType.equals("2")) {
            //子项
            //获取子项对应的父项
            String PID = DevUtil.executeQuery("select pid from uf_proj where id = ?", id);
            json.put("PID", PID);
        } else if (projType.equals("3")) {
            //孙子项
            String PID1 = DevUtil.executeQuery("select pid from uf_proj where id = ?", id);
            String PID2 = DevUtil.executeQuery("select pid from uf_proj where id = ?", PID1);
            json.put("PID", PID2);
        }
    } else if ("getCbzx".equals(cmd)) {
        //带出分摊部门对应的成本中心
        String ftbmN = Util.null2String(request.getParameter("ftbmN"));//分摊部门名称
        String gsbm = Util.null2String(request.getParameter("gsbm"));//公司编码
        String sql = "select * from OA_SAP_Cbzx where LTEXT = '" + ftbmN + "' and BUKRS = '" + gsbm + "'";
        log.d("sql ====== ", sql);
        rs.execute(sql);
        if (rs.next()) {
            json.put("cbzxmc", rs.getString("LTEXT"));//名称
            json.put("cbzxbm", rs.getString("KOSTL"));//编码
        }
    } else if ("getApportionData".equals(cmd)) {
        //获取分摊信息
        String id = Util.null2String(request.getParameter("id"));//子项/孙子项项目id
        log.d("id ===== ", id);
        String projType = DevUtil.executeQuery("select projType from uf_proj where id = ?", id);
        log.d("projType == ", projType);
        if (projType.equals("2")) {
            //子项
            //获取子项对应的父项
            String PID = DevUtil.executeQuery("select pid from uf_proj where id = ?", id);
            String sql = "select * from WV_V_Apportion where id = '" + PID + "'";
            log.d("sql ======= ", sql);
            json = Borrow.rsToJson(sql);
        } else if (projType.equals("3")) {
            //孙子项
            String PID1 = DevUtil.executeQuery("select pid from uf_proj where id = ?", id);
            String PID2 = DevUtil.executeQuery("select pid from uf_proj where id = ?", PID1);
            String sql = "select * from WV_V_Apportion where id = '" + PID2 + "'";
            log.d("sql ======= ", sql);
            json = Borrow.rsToJson(sql);
        }
    }
    out.print(json);
%>
