package com.westvalley.action.repaymentOver;

import com.westvalley.util.BaseUtils;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import weaver.conn.RecordSetTrans;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Map;

/**
 * 引用流程：还款流程
 * 引用节点：申请节点
 * 主要功能：1.写入借款中间表
 */
public class RepaymentApplyActtion implements Action {

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
        RecordSetTrans recordSetTrans = new RecordSetTrans();
        recordSetTrans.setAutoCommit(true);
        try {
            String requestId = request.getRequestid();//请求ID
            Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestId);
            String deleteSQL = "delete from WV_T_BorrowData where payid = ?";//删除原先的记录
            log.d("requestId == ", requestId);
            recordSetTrans.executeUpdate(deleteSQL, requestId);
            if (Double.valueOf(wfMainMap.get("bchkje")) > Double.valueOf(wfMainMap.get("dqjkye")))
                return "本次还款金额不能大于当前借款余额。";
            StringBuffer sb = new StringBuffer();
            sb.append("insert into WV_T_BorrowData(");
            sb.append("requestid,requestcode,requestname,detailid,projid,userid,payee,payBank,payAccount,payment,brovou,brodate,repaydate,money,rectype,payid,paydetail,payvou,remark)");
            sb.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            String jkid = wfMainMap.get("gljklc");
            String selectSQL = "select * from WV_T_BorrowData where requestid = '" + jkid + "' and rectype='borrow'";
            log.d("selectSQL ===", selectSQL);
            recordSetTrans.execute(selectSQL);
            String requestcode = "";//借款流程编号
            String requestname = "";//借款流程名称
            String detailid = "";//借款明细Id
            String projid = "";//项目id
            String userid = "";//借款人
            String payee = "";//收款人
            String payBank = "";//收款人开户银行
            String payAccount = "";//收款人银行账号
            String payment = "";//方式
            String brovou = "";//借款凭证号
            String brodate = "";//借款日期
            String repaydate = "";//预计还款日期
            if (recordSetTrans.next()) {
                requestcode = recordSetTrans.getString("requestcode");
                requestname = recordSetTrans.getString("requestname");
                detailid = recordSetTrans.getString("detailid");
                projid = recordSetTrans.getString("projid");
                userid = recordSetTrans.getString("userid");
                payee = recordSetTrans.getString("payee");
                payBank = recordSetTrans.getString("payBank");
                payAccount = recordSetTrans.getString("payAccount");
                payment = recordSetTrans.getString("payment");
                brovou = recordSetTrans.getString("brovou");
                brodate = recordSetTrans.getString("brodate");
                repaydate = recordSetTrans.getString("repaydate");
            }
            log.d("insertSQL === ", sb.toString());
            recordSetTrans.executeUpdate(sb.toString(),
                    jkid,
                    requestcode,
                    requestname,
                    detailid == null ? "" : detailid,
                    projid,
                    userid,
                    payee,
                    payBank,
                    payAccount,
                    payment,
                    brovou == null ? "" : brovou,
                    brodate,
                    repaydate,
                    wfMainMap.get("bchkje"),//冲销金额
                    "repay",//冲销
                    requestId,//冲销流程id
                    "",//冲销流程明细id
                    "",//冲销凭证号
                    "还款申请"
            );
            recordSetTrans.commit();
            return "";
        } catch (Exception e) {
            recordSetTrans.rollback();
            e.printStackTrace();
            log.e("系统发生异常:", e);
            return "系统发生异常";
        }
    }

}
