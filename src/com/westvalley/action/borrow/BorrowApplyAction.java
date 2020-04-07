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
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 引用流程：个人借款申请流程
 * 引用节点：申请节点-节点后附加操作
 * 主要功能：1.根据控制级别冻结对应的预算
 */
public class BorrowApplyAction implements Action {

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
        String requestId = request.getRequestid();//请求ID
        Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestId);
        log.d("wfMainMap :", wfMainMap);
        String money = wfMainMap.get("bcjkje");//金额
        if (money == null || Double.valueOf(money) == 0.00) return "本次借款金额不能为0";
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
        budget23Dto.setUseType(BudTypeEnum.FREZEE);
        budget23Dto.setCtrlLevel(projEntity.getCtrlLevel());
        budget23Dto.setUseAmt(Double.valueOf(money));//本次借款金额
        budget23Dto.setCreDate(rq);
        budget23Dto.setCreTime(sj);
        budget23Dto.setCreUser(wfMainMap.get("shenqr"));
        budget23DtoList.add(budget23Dto);
        ResultDto resultDto = budgetService.executeBudget23(budget23DtoList, BudTypeEnum.FREZEE);
        return resultDto.isOk() ? "" : resultDto.getMsg();
    }
}


