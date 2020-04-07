package com.westvalley.action.borrow;


import com.westvalley.action.Voucher;
import com.westvalley.service.UserService;
import com.westvalley.util.BaseUtils;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import org.apache.commons.lang3.StringUtils;

import weaver.conn.RecordSet;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 引用流程：个人借款申请流程
 * 引用节点：申请节点-节点后附加操作
 * 主要功能：1.根据控制级别冻结对应的预算
 */
public class BorrowVoucherAction2 implements Action {

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
        Map<String, Object> parameter = new HashMap<>();
        String requestId = request.getRequestid();//请求ID
        Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestId);
        log.d("requestId == ", requestId);
        String number = wfMainMap.get("liucbh");//行号
        parameter.put("ITEM", number);//行号
        parameter.put("BUKRS", wfMainMap.get("gsbm"));//公司代码
        parameter.put("BLDAT", wfMainMap.get("jzrq"));//凭证日期
        parameter.put("BUDAT", wfMainMap.get("jzrq"));//过账日期
        parameter.put("BLART", "SA");//凭证类型
        parameter.put("WAERS", "CNY");//货币
        parameter.put("HWAER", "CNY");//本位币
        parameter.put("KURSF", "1");//汇率
        UserService userService = new UserService();
        String userName = userService.getUserNameById(Integer.valueOf(wfMainMap.get("shenqr")));
        parameter.put("BKTXT", "还借款_" + userName);//抬头文本
        List<Map<String, Object>> par = new ArrayList<>();
        List<Map<String, String>> wfMainMapList = DevUtil.getWFDetailByReqID(requestId, 1);//借方明细
        log.d("wfMainMapList == ", wfMainMapList);
        for (Map<String, String> map : wfMainMapList) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("ITEM", number);//行号
            map2.put("BSCHL", map.get("jzm"));//记账码
            map2.put("HKONT", map.get("hjkm"));//科目
            map2.put("UMSKZ", map.get("tbzz"));//特别总账标识
            map2.put("ANBWA", "");//资产业务类型
            map2.put("WRBTR", map.get("jfje"));//金额
            map2.put("DMBTR", map.get("jfje"));//本币金额
            map2.put("KOSTL", map.get("lrzx"));//成本中心
            map2.put("RSTGR", map.get("xjl"));//现金流
            map2.put("SGTXT", map.get("zy").length() > 30 ? map.get("zy").substring(0, 20) : map.get("zy"));//摘要
            map2.put("ZZ04", map.get("gys"));//员工
            map2.put("ZZ02", "");//供应商
            map2.put("ZZ11", "");//项目号
            map2.put("ZINFO", "");//项目描述
            par.add(map2);
        }
        List<Map<String, String>> wfMainMapList2 = DevUtil.getWFDetailByReqID(requestId, 2);//贷方明细
        log.d("wfMainMapList2 == ", wfMainMapList2);
        for (Map<String, String> map : wfMainMapList2) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("ITEM", number);//行号
            map2.put("BSCHL", map.get("jzm"));//记账码
            map2.put("HKONT", map.get("ry"));//科目
            map2.put("UMSKZ", map.get("tbzz"));//特别总账标识
            map2.put("ANBWA", "");//资产业务类型
            map2.put("WRBTR", map.get("dfje"));//金额
            map2.put("DMBTR", map.get("dfje"));//本币金额
            map2.put("KOSTL", map.get("lrzx"));//成本中心
            map2.put("RSTGR", map.get("xjl"));//现金流
            map2.put("SGTXT", map.get("zy").length() > 30 ? map.get("zy").substring(0, 20) : map.get("zy"));//摘要
            map2.put("ZZ04", map.get("gys"));//员工
            map2.put("ZZ02", "");//供应商
            map2.put("ZZ11", "");//项目号
            map2.put("ZINFO", "");//项目描述
            par.add(map2);
        }
        log.d("par ========= ", par);
        Map<String, String> resultMap = Voucher.toCreateVoucher(parameter, par);
        String status = resultMap.get("status");
        log.d("resultMap == ", resultMap.toString());
        RecordSet recordSet = new RecordSet();
        String workflowID = request.getWorkflowid();
        if ("S".equals(status) || "s".equals(status)) {
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
    }
}


