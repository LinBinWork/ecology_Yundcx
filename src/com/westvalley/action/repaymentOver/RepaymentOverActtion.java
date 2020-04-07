package com.westvalley.action.repaymentOver;

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
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 引用流程：项目关闭审批流程
 * 引用节点：归档节点
 * 主要功能：归档,释放金额
 */
public class RepaymentOverActtion implements Action {

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
            int id = Integer.valueOf(wfMainMap.get("xmmc"));
            //释放金额
            Date newDate = new Date();
            String rq = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
            String sj = new SimpleDateFormat("HH:mm:ss").format(newDate);
            BudgetService budgetService = new BudgetService();
            ProjEntity projEntity = budgetService.getProjInfo(id, requestId);
            log.d("projEntity :", projEntity);
            List<Budget23Dto> budget23DtoList = new ArrayList<>();
            Budget23Dto budget23Dto = new Budget23Dto();
            budget23Dto.setRequestID(requestId);
            budget23Dto.setPID(projEntity.getPID());
            budget23Dto.setProjID(projEntity.getId());
            budget23Dto.setProjNo(projEntity.getProjNo());
            budget23Dto.setProjDeptNo(projEntity.getProjDeptNo());
            budget23Dto.setProjtype(EnumsUtil.getProjtypeEnum(projEntity.getProjType()));
            budget23Dto.setUseType(BudTypeEnum.BORROW_REVERSAL);
            budget23Dto.setCtrlLevel(projEntity.getCtrlLevel());
            budget23Dto.setUseAmt(Double.valueOf(wfMainMap.get("bchkje")));//还款金额
            budget23Dto.setModDate(rq);
            budget23Dto.setModTime(sj);
            budget23Dto.setCreUser(wfMainMap.get("shenqr"));
            budget23DtoList.add(budget23Dto);
            ResultDto resultDto = budgetService.executeBudget23(budget23DtoList, BudTypeEnum.BORROW_REVERSAL);
            if (!resultDto.isOk()) return resultDto.getMsg();
            //关联流程的值
            String gljklc = wfMainMap.get("gljklc");
            RecordSet rs = new RecordSet();
            String requestcode = "";
            String requestname = "";
            String detailid = "";
            int projid = 0;
            String userid = "";
            String payee = "";
            String payBank = "";
            String payAccount = "";
            String payment = "";
            String brovou = "";
            String brodate = "";
            String repaydate = "";
            double money = 0.0;
            //根据关联流程 查WV_T_BorrowData  requestid = 关联流程
            String sql = "select requestid,requestcode,requestname,detailid,projid,userid,payee," +
                    "payBank,payAccount,payment,brovou,brodate,repaydate,money from WV_T_BorrowData where requestid=" + gljklc;
            rs.execute(sql);
            if (rs.next()) {
                requestcode = rs.getString("requestcode");
                requestname = rs.getString("requestname");
                /*  detailid=rs.getString("detailid");*/
                projid = rs.getInt("projid");
                userid = rs.getString("userid");
                payee = rs.getString("payee");
                payBank = rs.getString("payBank");
                payAccount = rs.getString("payAccount");
                payment = rs.getString("payment");
                brovou = rs.getString("brovou");
                brodate = rs.getString("brodate");
                repaydate = rs.getString("repaydate");
                money = rs.getDouble("money");
            }
            //删除原有记录

            String sql2 = "delete from WV_T_BorrowData where payid=" + requestId;
            log.d("sql2 ======== ", sql2);
            recordSetTrans.execute(sql2);

            String bchkje = wfMainMap.get("bchkje");
            String pzh = wfMainMap.get("pzh");
            String sql3 = "insert into WV_T_BorrowData(requestid,requestcode,requestname,detailid,projid,userid,payee," +
                    "payBank, payAccount,payment,brovou,brodate,repaydate,money,rectype,payid,paydetail,payvou,remark,isHistory) " +
                    "values('" + gljklc + "','" + requestcode + "','" + requestname + "'," + null + "," + projid + ",'" + userid +
                    "','" + payee + "','" + payBank + "','" + payAccount + "','" + payment + "','" + brovou + "','" + brodate + "','" +
                    repaydate + "'," + bchkje + ",'repay','" + requestId + "'," + null + ",'" + pzh + "','还款'," + null + ")";
            log.d("sql3 ======== ", sql3);


            boolean flag = recordSetTrans.execute(sql3);
            if (flag) {
                recordSetTrans.commit();
            }
            return "";
        } catch (Exception e) {
            recordSetTrans.rollback();
            e.printStackTrace();
            log.e("", e);
            return "系统发生异常";
        }
    }

}
