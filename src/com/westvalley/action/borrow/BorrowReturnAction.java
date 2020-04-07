package com.westvalley.action.borrow;

import com.westvalley.project.dto.Budget23Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.ProjEntity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.service.BudgetService;
import com.westvalley.project.util.EnumsUtil;
import com.westvalley.util.BaseUtils;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import weaver.conn.RecordSetTrans;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 引用流程：个人借款申请流程
 * 引用节点：归档节点-节点前附加操作
 * 主要功能：1.将借款信息写入借款台账表
 */
public class BorrowReturnAction implements Action {

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
            String deleteSQL = "delete from WV_T_BorrowData where requestid = ? ";
            log.d("deleteSQL ===", deleteSQL);
            reSet.executeUpdate(deleteSQL, requestId);
            String requestcode = wfMainMap.get("liucbh");//流程编号brovou
            String projid = wfMainMap.get("xmmc");//项目ID
            String userid = wfMainMap.get("shenqr");//借款人
            String payee = wfMainMap.get("skr");//收款人
            String payBank = wfMainMap.get("skrkhyx");//收款人开户银行
            String payAccount = wfMainMap.get("skryxzh");//收款人银行账号
            String payment = wfMainMap.get("lkfs");//领款方式
            String brovou = wfMainMap.get("pzh");//借款凭证号
            String brodate = wfMainMap.get("shenqrq");//借款日期
            String repaydate = wfMainMap.get("yjhkrq");//预计还款日期
            String money = wfMainMap.get("bcjkje");//金额
            if (money == null || Double.valueOf(money) == 0.00) return "本次借款金额不能为0";
            String remark = wfMainMap.get("jksy");//借款事由
            String rectype = "borrow";//借款
            String insertSQL = "insert into WV_T_BorrowData(requestid,requestcode,requestname,detailid,projid,userid,payee,payBank,payAccount,payment,brovou,brodate,repaydate,money,rectype,remark)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            reSet.executeUpdate(insertSQL,
                    requestId,
                    requestcode,
                    requestName,
                    "",
                    projid,
                    userid,
                    payee,
                    payBank,
                    payAccount,
                    payment,
                    brovou,
                    brodate,
                    repaydate,
                    money,
                    rectype,
                    remark
            );
            BudgetService budgetService = new BudgetService();
            ProjEntity projEntity = budgetService.getProjInfo(Integer.valueOf(wfMainMap.get("xmmc")), null);
            log.d("projEntity :", projEntity);
            Date newDate = new Date();
            String rq = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
            String sj = new SimpleDateFormat("HH:mm:ss").format(newDate);
            List<Budget23Dto> budget23DtoList = new ArrayList<>();
            Budget23Dto budget23Dto = new Budget23Dto();
            budget23Dto.setRequestID(requestId);
            budget23Dto.setPID(projEntity.getPID());
            budget23Dto.setProjID(projEntity.getId());
            budget23Dto.setProjNo(projEntity.getProjNo());
            budget23Dto.setProjDeptNo(projEntity.getProjDeptNo());
            budget23Dto.setProjtype(EnumsUtil.getProjtypeEnum(projEntity.getProjType()));
            budget23Dto.setUseType(BudTypeEnum.USED);
            budget23Dto.setCtrlLevel(projEntity.getCtrlLevel());
            budget23Dto.setUseAmt(Double.valueOf(money));//本次借款金额
            budget23Dto.setModDate(rq);
            budget23Dto.setModTime(sj);
            budget23Dto.setCreUser(wfMainMap.get("shenqr"));
            budget23DtoList.add(budget23Dto);
            ResultDto resultDto = budgetService.executeBudget23(budget23DtoList, BudTypeEnum.USED);
            if (resultDto.isOk()) {
                reSet.commit();
                return "";
            } else {
                reSet.rollback();
                return resultDto.getMsg();
            }
        } catch (Exception e) {
            reSet.rollback();
            log.e("写入借款表发生错误：", e);
            return "后台发生错误：" + e.getMessage() + "，标识：" + requestId;
        }
    }
}
