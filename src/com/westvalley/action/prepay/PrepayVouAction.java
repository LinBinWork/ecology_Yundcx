package com.westvalley.action.prepay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.westvalley.action.Voucher;
import com.westvalley.project.action.DevAbstractAction;
import com.westvalley.project.dto.ActionDto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.util.DevUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.general.TimeUtil;
import weaver.soa.workflow.request.RequestInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预付款生成SAP凭证
 */
public class PrepayVouAction extends DevAbstractAction {

    /**
     * 业务逻辑
     *
     * @param info
     * @param actionDto
     * @return 返回 null 或者ok 则代表通过
     */
    @Override
    protected ResultDto runCode(RequestInfo info, ActionDto actionDto) {
        String requestID = actionDto.getRequestID();
        Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestID);
        log.d("wfMainMap", wfMainMap);

        if ("0".equals(getMapV(wfMainMap, "pzsczt"))) {
            return ResultDto.ok("已经生成凭证，不需要再次生成");
        }

        List<Map<String, String>> wfDetail2 = DevUtil.getWFDetailByReqID(requestID, 2);
        List<Map<String, String>> wfDetail3 = DevUtil.getWFDetailByReqID(requestID, 3);

        log.d("wfDetail2", wfDetail2);
        log.d("wfDetail3", wfDetail3);

        //生成凭证头
        String number = DevUtil.getWFReqMark(requestID);//流程编号
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("ITEM", number);//行号
        parameter.put("BUKRS", wfMainMap.get("gongsdm"));//公司代码
        parameter.put("BLDAT", wfMainMap.get("jzrq"));//凭证日期
        parameter.put("BUDAT", wfMainMap.get("jzrq"));//过账日期
        parameter.put("BLART", "SA");//凭证类型
        parameter.put("WAERS", "CNY");//货币
        parameter.put("HWAER", "CNY");//本位币
        parameter.put("KURSF", "1");//汇率
        parameter.put("BKTXT", StringUtil.truncateStr("付_" + DevUtil.getHrmLastName(getMapV(wfMainMap, "shenqr")) + getMapV(wfMainMap, "yfksy"), 25));//抬头文本

        //明细信息
        List<Map<String, Object>> par = new ArrayList<>();

        //借方
        for (Map<String, String> map : wfDetail2) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("ITEM", number);//行号
            map2.put("BSCHL", map.get("jzm"));//记账码
            map2.put("HKONT", map.get("gys"));//科目
            map2.put("UMSKZ", map.get("tbzz"));//特别总账标识
            map2.put("ANBWA", "");//资产业务类型
            map2.put("WRBTR", getMapAmtV(map, "jfje"));//金额
            map2.put("DMBTR", getMapAmtV(map, "jfje"));//本币金额
            map2.put("KOSTL", map.get("cbzx"));//成本中心
            map2.put("RSTGR", "");//现金流
            map2.put("SGTXT", map.get("zy").length() > 30 ? map.get("zy").substring(0, 20) : map.get("zy"));//摘要
            map2.put("ZZ04", "");//员工
            map2.put("ZZ02", map.get("gys"));//供应商
            map2.put("ZZ11", "");//项目号
            map2.put("ZINFO", "");//项目描述
            par.add(map2);
        }
        //贷方
        for (Map<String, String> map : wfDetail3) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("ITEM", number);//行号
            map2.put("BSCHL", map.get("jzm"));//记账码
            map2.put("HKONT", map.get("hjkm"));//科目
            map2.put("UMSKZ", map.get("tbzz"));//特别总账标识
            map2.put("ANBWA", "");//资产业务类型
            map2.put("WRBTR", getMapAmtV(map, "dfje"));//金额
            map2.put("DMBTR", getMapAmtV(map, "dfje"));//本币金额
            map2.put("KOSTL", "");//成本中心
            map2.put("RSTGR", map.get("xjl"));//现金流
            map2.put("SGTXT", map.get("zy").length() > 30 ? map.get("zy").substring(0, 20) : map.get("zy"));//摘要
            map2.put("ZZ04", "");//员工
            map2.put("ZZ02", "");//供应商
            map2.put("ZZ11", "");//项目号
            map2.put("ZINFO", "");//项目描述
            par.add(map2);
        }
        log.d("推送SAP凭证数据", parameter, par);
        //SAP返回数据
        Map<String, String> resultMap = Voucher.toCreateVoucher(parameter, par);
        String status = resultMap.get("status");
        String msg = resultMap.get("msg");
        String belner = resultMap.get("BELNR");
        log.d("预付款凭证返回信息 ", resultMap);
        boolean b = false;
        if ("S".equalsIgnoreCase(status)) {
            b = true;
        }
        DevUtil.updateWFMainData(requestID, "pzsczt", b ? 0 : 1);//凭证状态
        DevUtil.updateWFMainData(requestID, "pzh", belner);//凭证号
        DevUtil.updateWFMainData(requestID, "pzxx", msg);//凭证信息
        return ResultDto.init(b, msg, belner);
    }


}
