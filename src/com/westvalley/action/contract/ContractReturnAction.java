package com.westvalley.action.contract;

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
 * 引用流程：项目合同审批申请流程
 * 引用节点：归档节点
 * 主要功能：预算修改为已使用状态。
 */
public class ContractReturnAction implements Action {

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
            String requestId = request.getRequestid();//请求ID
            Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestId);
            log.d("标识：", requestId);
            List<Map<String, String>> wfMainMapList = DevUtil.getWFDetailByReqID(requestId, 2);//明细表数据
            log.d("wfMainMapList：", wfMainMapList);
            BudgetService budgetService = new BudgetService();
            boolean htlxmc = wfMainMap.get("htlxmc").equals("1");//合同类型
            List<Budget23Dto> budget23DtoList = new ArrayList<>();
            Date newDate = new Date();
            String rq = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
            String sj = new SimpleDateFormat("HH:mm:ss").format(newDate);
            for (Map<String, String> map : wfMainMapList) {
                ProjEntity projEntity = budgetService.getProjInfo(Integer.valueOf(map.get("xmbh")), null);
                log.d("projEntity：", projEntity);
                Budget23Dto budget23Dto = new Budget23Dto();
                budget23Dto.setRequestID(requestId);
                budget23Dto.setPID(projEntity.getPID());
                budget23Dto.setProjID(projEntity.getId());
                budget23Dto.setProjNo(projEntity.getProjNo());
                budget23Dto.setProjDeptNo(projEntity.getProjDeptNo());
                budget23Dto.setProjtype(EnumsUtil.getProjtypeEnum(projEntity.getProjType()));
                if (htlxmc) {
                    //无发票置换直接扣减对应项目预算
                    budget23Dto.setUseType(BudTypeEnum.PAY);
                } else {
                    budget23Dto.setUseType(BudTypeEnum.APPLY);
                }
                budget23Dto.setCtrlLevel(projEntity.getCtrlLevel());
                budget23Dto.setUseAmt(StringUtils.isEmpty(map.get("ftje")) ? 0 : Double.valueOf(map.get("ftje")));
                budget23Dto.setCreUser(wfMainMap.get("shenqr"));
                budget23Dto.setModDate(rq);
                budget23Dto.setModTime(sj);
                budget23DtoList.add(budget23Dto);
            }
            ResultDto resultDto = null;
            if (htlxmc) {
                //无发票置换直接扣减对应项目预算
                resultDto = budgetService.executeBudget23(budget23DtoList, BudTypeEnum.PAY);
            } else {
                resultDto = budgetService.executeBudget23(budget23DtoList, BudTypeEnum.ARCHIVE);
            }
            return resultDto.isOk() ? "" : resultDto.getMsg();
        } catch (Exception e) {
            e.printStackTrace();
            log.e("发生错误：", e);
            return "系统发生错误,请将此标识:" + request.getRequestid() + "发送给管理员";
        }
    }

}
