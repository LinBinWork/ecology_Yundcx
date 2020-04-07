package com.westvalley.action.travel;


import com.westvalley.action.Voucher;
import com.westvalley.service.UserService;
import com.westvalley.util.BaseUtils;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 引用流程：差旅费用报销流程
 * 引用节点：凭证生成节点-节点后附加操作
 * 主要功能：1.生成会计凭证
 */
public class TravelVoucherAction implements Action {

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
        Date newDate = new Date();
        String rq = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
        parameter.put("BLDAT", wfMainMap.get("jzrq"));//凭证日期
        parameter.put("BUDAT", wfMainMap.get("jzrq"));//过账日期
        parameter.put("BLART", "SA");//凭证类型
        parameter.put("WAERS", "CNY");//货币
        parameter.put("HWAER", "CNY");//本位币
        parameter.put("KURSF", "1");//汇率
        UserService userService = new UserService();
        String userName = userService.getUserNameById(Integer.valueOf(wfMainMap.get("shenqr")));
        parameter.put("BKTXT", "付_" + userName + "_差旅费");//抬头文本
        List<Map<String, Object>> par = new ArrayList<>();
        List<Map<String, String>> wfMainMapList = DevUtil.getWFDetailByReqID(requestId, 2);//借方明细
        log.d("wfMainMapList == ", wfMainMapList);
        for (Map<String, String> map : wfMainMapList) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("ITEM", number);//行号
            map2.put("BSCHL", map.get("jzm"));//记账码
            map2.put("HKONT", map.get("hjkm"));//科目
            map2.put("UMSKZ", "");//特别总账标识
            map2.put("ANBWA", "");//资产业务类型
            map2.put("WRBTR", map.get("jfje"));//金额
            map2.put("DMBTR", map.get("jfje"));//本币金额
            map2.put("KOSTL", map.get("cbzx"));//成本中心
            map2.put("RSTGR", "");//现金流
            map2.put("SGTXT", map.get("zy").length() > 30 ? map.get("zy").substring(0, 20) : map.get("zy"));//摘要
            map2.put("ZZ04", map.get("ry"));//员工
            map2.put("ZZ02", "");//供应商
            map2.put("ZZ11", map.get("hsxm"));//项目号
            map2.put("ZINFO", "");//项目描述
            map2.put("ZZ09", map.get("ywlx"));//业务类型
            par.add(map2);
        }
        List<Map<String, String>> wfMainMapList2 = DevUtil.getWFDetailByReqID(requestId, 3);//贷方明细
        log.d("wfMainMapList2 == ", wfMainMapList2);
        for (Map<String, String> map : wfMainMapList2) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("ITEM", number);//行号
            map2.put("BSCHL", map.get("jzm"));//记账码
            if (map.get("jzm").equals("31")) {
                map2.put("HKONT", map.get("ry"));//科目
            } else {
                map2.put("HKONT", map.get("hjkm"));//科目
            }
            map2.put("UMSKZ", "");//特别总账标识
            map2.put("ANBWA", "");//资产业务类型
            map2.put("WRBTR", map.get("dfje"));//金额
            map2.put("DMBTR", map.get("dfje"));//本币金额
            map2.put("KOSTL", "");//成本中心
            map2.put("RSTGR", map.get("xjl"));//现金流
            map2.put("SGTXT", map.get("zy").length() > 30 ? map.get("zy").substring(0, 20) : map.get("zy"));//摘要
            map2.put("ZZ04", map.get("ry"));//员工
            map2.put("ZZ02", "");//供应商
            map2.put("ZZ11", "");//项目号
            map2.put("ZINFO", "");//项目描述
            map2.put("ZZ09", "");//业务类型
            par.add(map2);
        }
        log.d("par ========= ", par);
        Map<String, String> resultMap = Voucher.toCreateVoucher(parameter, par);
        log.d("resultMap===", resultMap.toString());
        if (resultMap != null && "S".equals(resultMap.get("status"))) {
            String writeMsg = Voucher.writeResult(request, resultMap.get("BELNR"), "0", resultMap.get("msg"));
            return writeMsg;
        } else {
            Voucher.writeResult(request, resultMap.get("BELNR"), "1", resultMap.get("msg"));
            return resultMap.get("msg");
        }
    }
}


