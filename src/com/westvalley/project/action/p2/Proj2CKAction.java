package com.westvalley.project.action.p2;

import com.westvalley.project.action.DevAbstractAction;
import com.westvalley.project.dto.ActionDto;
import com.westvalley.project.dto.Budget1Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.Proj1Entity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.CtrlLevelEnum;
import com.westvalley.project.service.BudgetService;
import com.westvalley.project.service.BudgetSplitService;
import com.westvalley.project.service.Proj1Service;
import com.westvalley.util.DevUtil;
import com.westvalley.util.StringUtil;
import weaver.general.TimeUtil;
import weaver.soa.workflow.request.RequestInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 子项立项拆解校验
 */
public class Proj2CKAction extends DevAbstractAction{
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
        log.d("wfMainMap",wfMainMap);
        int projID = Integer.valueOf(wfMainMap.get("glfx"));

        Proj1Service proj1Service = new Proj1Service();
        Proj1Entity proj1 = proj1Service.getProj1(projID, requestID);
        //如果控制级别是父项，拆解不占用父项预算
        ResultDto dto = null;
        List<Budget1Dto> budget1DtoList = getbudget1DtoList(requestID,proj1,wfMainMap);

        if(CtrlLevelEnum.PARENT.compareTo(proj1.getCtrlLevel()) == 0){
            BudgetSplitService budgetSplitService = new BudgetSplitService();
            dto = budgetSplitService.splitBudget1(budget1DtoList,BudTypeEnum.SPLIT_FREZEE,true);
        }else{
            BudgetService budgetService = new BudgetService();
            //子项立项，冻结父项预算
            dto = budgetService.executeBudget1(budget1DtoList,BudTypeEnum.FREZEE,true);
        }

        return dto;
    }
    /**
     * 封装
     * @return
     */
    public List<Budget1Dto> getbudget1DtoList(String requestID, Proj1Entity proj1, Map<String, String> wfMainMap){
        List<Budget1Dto> budget1DtoList = new ArrayList<>();


        List<Map<String, String>> detailList1 = DevUtil.getWFDetailByReqID(requestID,1);
        log.d("detailList1",detailList1);



        String fjrq = wfMainMap.get("fjrq");
        String shenqr = wfMainMap.get("shenqr");

        boolean isParentCtrl = CtrlLevelEnum.PARENT.compareTo(proj1.getCtrlLevel()) == 0;
        for (Map<String, String> map : detailList1) {
            double useAmt = Double.valueOf(StringUtil.toNum(map.get("hsrmbje")));

            Budget1Dto dto = new Budget1Dto();
            dto.setRequestID(requestID);
            dto.setDetailID(map.get("id"));
            dto.setProjID(proj1.getId());
            dto.setProjDeptNo(proj1.getProjDeptNo());
            if(isParentCtrl){
                dto.setUseType(BudTypeEnum.SPLIT_FREZEE);
            }else {
                dto.setUseType(BudTypeEnum.FREZEE);
            }
            dto.setUseAmt(useAmt);
            dto.setCreDate(fjrq);
            dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setCreUser(shenqr);
//        dto.setModDate();
//        dto.setModTime();
            dto.setPID(proj1.getPID());
            dto.setCtrlLevel(proj1.getCtrlLevel());
            dto.setProjNo(proj1.getProjNo());

            budget1DtoList.add(dto);
        }


        return budget1DtoList;
    }
}
