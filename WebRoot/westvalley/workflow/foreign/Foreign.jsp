<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="com.westvalley.util.LogUtil" %>
<%@ page import="com.westvalley.project.service.BudgetContractService" %>
<%@ page import="com.westvalley.project.entity.ContractBudgetEntity" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%
    String cmd = Util.null2String(request.getParameter("cmd"));
    JSONObject json = new JSONObject();
    LogUtil log = LogUtil.getLogger(getClass());
    RecordSet rs = new RecordSet();
    if ("getContract".equals(cmd)) {
        //获取合同未付金额
        String requestID = Util.null2String(request.getParameter("id"));
        log.d("requestID ==== ", requestID);
        String contractRequestID = Util.null2String(request.getParameter("contractRequestID"));
        BudgetContractService contractService = new BudgetContractService();
        ContractBudgetEntity cben = contractService.getContractBudgetEntity(requestID, contractRequestID);
        log.d("cben ==== ", cben);
        if (cben != null) {
            json.put("htzje", cben.getContractAmt());
            json.put("htyfje", cben.getContractUsedAmt());
            json.put("htwfje", cben.getContractBalance());
        } else {
            json.put("htzje", 0.00);
            json.put("htyfje", 0.00);
            json.put("htwfje", 0.00);
        }
    } else if ("getXMFTMX".equals(cmd)) {
        //根据合同流程带出项目分摊明细
        String htlc = Util.null2String(request.getParameter("htlc"));
        /*String sql = "select * from formtable_main_26 t1 " +
                " left join formtable_main_26_dt2 t2 on t1.id = t2.mainid " +
                " left join uf_proj t3 on t2.xmbh = t3.id " +
                " where t1.requestId = ?";*/
        String sql = "select * from WV_V_XMFTMX where requestId = ?";
        log.d("htlc === ", htlc);
        log.d("sql === ", sql);
        rs.executeQuery(sql, htlc);
        List<Map<String, String>> resultList = new ArrayList<>();
        while (rs.next()) {
            Map map = new HashMap();
            map.put("xmbh", rs.getString("xmbh"));//项目
            map.put("projNo", rs.getString("projNo"));//项目编号
            map.put("ftje", rs.getString("ftje"));//分摊金额
            map.put("ftbl", rs.getString("ftbl"));//分摊比率
            resultList.add(map);
        }
        json.put("data", resultList);
    } else if ("getFKMX".equals(cmd)) {
        //付款明细
        String htlc = Util.null2String(request.getParameter("htlc"));
        log.d("htlc === ", htlc);
        String sql = "select t2.* from uf_YXHTTZ t1 " +
                " left join uf_YXHTTZ_dt1 t2 on t1.id = t2.mainid " +
                " where t2.fkzt != '2' and t1.requestID = ?";
        log.d("sql === ", sql);
        rs.executeQuery(sql, htlc);
        List<Map<String, String>> resultList = new ArrayList<>();
        while (rs.next()) {
            Map map = new HashMap();
            map.put("id", rs.getString("id"));
            map.put("mainid", rs.getString("mainid"));
            map.put("fkje", rs.getString("fkje"));
            map.put("fktj", rs.getString("fktj"));
            map.put("bz", rs.getString("bz"));
            map.put("fkzt", rs.getString("fkzt"));
            map.put("yfje", rs.getString("yfje"));
            map.put("syje", rs.getString("syje"));
            map.put("fkqs", rs.getString("fkqs"));
            map.put("sl", rs.getString("sl"));
            resultList.add(map);
        }
        json.put("data", resultList);
    } else if ("checkoutContract".equals(cmd)) {
        //付款明细
        String htlc = Util.null2String(request.getParameter("htlc"));
        log.d("htlc === ", htlc);
        String sql = "select htlx from uf_YXHTTZ where reqid = ? ";
        rs.executeQuery(sql, htlc);
        String htlx = "9999";
        if (rs.next()) {
            htlx = rs.getString("htlx");
        }
        json.put("htlx", htlx);
    }
    out.print(json);
%>