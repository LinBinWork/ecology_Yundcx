package com.westvalley.action.payment;

import com.westvalley.util.BaseUtils;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import weaver.conn.RecordSetTrans;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.List;
import java.util.Map;

/**
 * 引用流程：预付款流程
 * 引用节点：归档节点-节点前附加操作
 * 主要功能：1.将预付款信息写入中间表
 */
public class PaymentReturnAction implements Action {

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
        RecordSetTrans reSet = new RecordSetTrans();
        reSet.setAutoCommit(false);//不自动提交
        String requestId = request.getRequestid();//请求ID
        try {
            String requestName = request.getRequestManager().getRequestname();//流程名称
            log.d("requestId ===", requestId);
            Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestId);//主表信息
            log.d("wfMainMap ==", wfMainMap);
            String deleteSQL = "delete from WV_T_PaymentData where requestid = ? ";
            log.d("deleteSQL ===", deleteSQL);
            reSet.executeUpdate(deleteSQL, requestId);
            String requestcode = wfMainMap.get("liucbh");//流程编号
            String fwsmc = wfMainMap.get("fwsmc");//供应商
            String shenqrq = wfMainMap.get("shenqrq");//预付日期
            String rectype = "payment";//预付
            String insertSQL = "insert into WV_T_PaymentData(requestid,requestcode,requestname,detailid,xmid,gysid,payDate,money,bz,rectype,remark) values (?,?,?,?,?,?,?,?,?,?,?)";
            List<Map<String, String>> wfMainMapList = DevUtil.getWFDetailByReqID(requestId, 1);
            for (Map<String, String> map : wfMainMapList) {
                reSet.executeUpdate(insertSQL,
                        requestId,
                        requestcode,
                        requestName,
                        map.get("id"),
                        map.get("zxszx"),
                        fwsmc,
                        shenqrq,
                        map.get("ybje"),//原币金额
                        map.get("ybbb"),//币种
                        rectype,
                        "预付款"
                );
            }
            reSet.commit();
            return null;
        } catch (Exception e) {
            reSet.rollback();
            log.e("写入中间表发生错误：", e);
            return "后台发生错误：" + e.getMessage() + "，标识：" + requestId;
        }
    }
}
