package com.westvalley.action.foreign;


import com.westvalley.action.Voucher;
import com.westvalley.service.UserService;
import com.westvalley.util.BaseUtils;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import weaver.conn.RecordSet;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 引用流程：差旅费用报销流程
 * 引用节点：凭证生成节点-节点后附加操作
 * 主要功能：1.生成会计凭证
 */
public class ForeignVoucherAction implements Action {

    private LogUtil log = LogUtil.getLogger(getClass());


    @Override
    public String execute(RequestInfo requestInfo) {
        String msg = doAct(requestInfo);
        if (StringUtils.isEmpty(msg)) {
            return SUCCESS;
        } else {
            BaseUtils.setRequestErrorMessage(requestInfo, msg);
            return FAILURE_AND_CONTINUE;
        }
    }

    private String doAct(RequestInfo request) {
        try {
            Map<String, Object> parameter = new HashMap<>();
            String requestId = request.getRequestid();//请求ID
            Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestId);
            log.d("requestId == ", requestId);
            if (!StringUtils.isEmpty(wfMainMap.get("pzh"))) {
                log.d("凭证已生成，无需继续生成:", requestId);
                return "";
            }
            String number = wfMainMap.get("liucbh");//行号
            parameter.put("ITEM", number);//行号
            parameter.put("BUKRS", wfMainMap.get("gsbm"));//公司代码
            Date newDate = new Date();
            String rq = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
            parameter.put("BLDAT", wfMainMap.get("jzrq"));//凭证日期
            parameter.put("BUDAT", wfMainMap.get("jzrq"));//过账日期
            parameter.put("BLART", "SA");//凭证类型
            List<Map<String, String>> wfMainMapList1 = DevUtil.getWFDetailByReqID(requestId, 1);
            log.d("wfMainMapList1 === ", wfMainMapList1);
            String bz = "CNY";
            Double hl = 1.00;
            if (wfMainMapList1 != null) {
                bz = wfMainMapList1.get(0).get("bz");
                String sql = "select * from fnacurrency where id = ?";
                RecordSet recordSet = new RecordSet();
                recordSet.executeQuery(sql, bz);
                if (recordSet.next()) {
                    bz = recordSet.getString("currencyname");
                }
                hl = Double.valueOf(wfMainMapList1.get(0).get("hl") == null ? "1" : wfMainMapList1.get(0).get("hl"));
            }
            log.d("bz === ", bz);
            log.d("hl === ", hl);
            UserService userService = new UserService();
            String userName = userService.getUserNameById(Integer.valueOf(wfMainMap.get("shenqr")));
            String fksy = wfMainMap.get("fksy").length() > 50 ? wfMainMap.get("fksy").substring(0, 50) : wfMainMap.get("fksy");//付款事由
            parameter.put("BKTXT", "付款：付_" + userName + "_" + fksy);//抬头文本
            List<Map<String, Object>> par = new ArrayList<>();
            List<Map<String, String>> wfMainMapList = DevUtil.getWFDetailByReqID(requestId, 3);//借方明细
            parameter.put("WAERS", bz);//货币
            parameter.put("HWAER", "CNY");//本位币
            parameter.put("KURSF", hl);//汇率
            log.d("wfMainMapList == ", wfMainMapList);
            BigDecimal increaseAmt = new BigDecimal(String.valueOf(hl));
            for (Map<String, String> map : wfMainMapList) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("ITEM", number);//行号
                map2.put("BSCHL", map.get("jzm"));//记账码
                if (map.get("jzm").equals("39")) {
                    map2.put("HKONT", map.get("gys"));//供应商
                } else if (map.get("jzm").equals("29")) {
                    map2.put("HKONT", map.get("gys"));//供应商
                } else {
                    map2.put("HKONT", map.get("hjkm"));//科目
                }
                BigDecimal decreaseAmt = new BigDecimal(map.get("jfjecny"));
                map2.put("UMSKZ", map.get("tbzz"));//特别总账标识
                map2.put("ANBWA", "");//资产业务类型
                map2.put("WRBTR", decreaseAmt.divide(increaseAmt).doubleValue());//金额
                map2.put("DMBTR", map.get("jfjecny"));//本币金额
                map2.put("KOSTL", map.get("lrzx"));//成本中心
                map2.put("RSTGR", "");//现金流
                map2.put("SGTXT", map.get("zy").length() > 30 ? map.get("zy").substring(0, 20) : map.get("zy"));//摘要
                map2.put("ZZ04", map.get("ry"));//员工
                map2.put("ZZ02", map.get("gys"));//供应商
                map2.put("ZZ11", map.get("xm"));//项目号
                map2.put("ZINFO", "");//项目描述
                map2.put("ZZ09", map.get("ywlx"));//业务类型
                par.add(map2);
            }
            List<Map<String, String>> wfMainMapList2 = DevUtil.getWFDetailByReqID(requestId, 6);//贷方明细
            log.d("wfMainMapList2 == ", wfMainMapList2);
            for (Map<String, String> map : wfMainMapList2) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("ITEM", number);//行号
                map2.put("BSCHL", map.get("jzm"));//记账码
                if (map.get("jzm").equals("39")) {
                    map2.put("HKONT", map.get("gys"));//科目
                } else {
                    map2.put("HKONT", map.get("hjkm"));//科目
                }
                map2.put("UMSKZ", map.get("tbzz"));//特别总账标识
                map2.put("ANBWA", "");//资产业务类型
                BigDecimal decreaseAmt = new BigDecimal(map.get("dfjecny"));
                map2.put("WRBTR", decreaseAmt.divide(increaseAmt).doubleValue());//金额
                map2.put("DMBTR", map.get("dfjecny"));//本币金额
                map2.put("KOSTL", map.get("lrzx"));//成本中心
                map2.put("RSTGR", map.get("xjl"));//现金流
                map2.put("SGTXT", map.get("zy").length() > 50 ? map.get("zy").substring(0, 50) : map.get("zy"));//摘要
                map2.put("ZZ04", "");//员工
                map2.put("ZZ02", map.get("gys"));//供应商

                String xm = map.get("xm") == null ? "" : map.get("xm").toString();
                map2.put("ZZ11", xm.length() > 10 ? xm.substring(0, 10) : xm);//项目号
                map2.put("ZINFO", "");//项目描述
                map2.put("ZZ09", "");//业务类型
                par.add(map2);
            }
            log.d("par ========= ", par);
            Map<String, String> resultMap = Voucher.toCreateVoucher(parameter, par);
            log.d("resultMap===", resultMap.toString());
            RecordSet recordSet = new RecordSet();
            String workflowID = request.getWorkflowid();
            if (resultMap != null && "S".equals(resultMap.get("status"))) {
                String tSQL = "update " + Voucher.getRequestTableName(workflowID)
                        + " set pzh ='" + resultMap.get("BELNR") + "' "
                        + ", pzsczt ='0'"
                        + ", pzscsm ='" + resultMap.get("msg") + "' "
                        + " where requestid=" + requestId;
                boolean isOk = recordSet.executeUpdate(tSQL);
                return isOk ? "" : "接口调用回写表单状态失败:e" + tSQL;
            } else {
                String tSQL = "update " + Voucher.getRequestTableName(workflowID)
                        + " set pzsczt ='1'"
                        + ", pzscsm ='" + resultMap.get("msg") + "' "
                        + " where requestid=" + requestId;
                recordSet.executeUpdate(tSQL);
                return resultMap.get("msg");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.e("凭证生成错误:", e);
            return "凭证生成错误：" + e.getMessage();
        }
    }
}


