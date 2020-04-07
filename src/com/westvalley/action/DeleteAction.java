package com.westvalley.action;


import com.westvalley.util.BaseUtils;
import com.westvalley.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import weaver.conn.RecordSetTrans;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;


/**
 * 退回
 */
public class DeleteAction implements Action {

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
        recordSetTrans.setAutoCommit(false);
        try {
            String requestId = request.getRequestid();//请求ID
            log.d("requestId == ", requestId);
            String sql = "delete from wv_proj_excuDetail where requestID = ? and useType = 9";
            log.d("sql === ", sql, requestId);
            recordSetTrans.executeUpdate(sql, requestId);
            recordSetTrans.commit();
            return "";
        } catch (Exception e) {
            recordSetTrans.rollback();
            e.printStackTrace();
            log.e("清除核销预算失败：", e);
            return "清除核销预算失败:" + e.getMessage();
        }
    }
}


