package com.westvalley.project.action.p3;

import com.westvalley.project.action.DevAbstractAction;
import com.westvalley.project.dto.*;
import com.westvalley.project.entity.Proj1Entity;
import com.westvalley.project.entity.Proj2Entity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.CtrlLevelEnum;
import com.westvalley.project.service.*;
import com.westvalley.util.DevUtil;
import com.westvalley.util.StringUtil;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 孙子项立项拆解校验
 */
public class Proj3CKAction extends DevAbstractAction{
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
        int projID = Integer.valueOf(wfMainMap.get("zxxmmc"));
        Proj2Service service = new Proj2Service();
        Proj2Entity proj2 = service.getProj2(projID, requestID);


        List<Map<String, String>> detailList1 = DevUtil.getWFDetailByReqID(requestID,1);
        log.d("detailList1",detailList1);

        ResultDto dto = null;
        if(CtrlLevelEnum.PARENT.compareTo(proj2.getCtrlLevel()) == 0){
            Proj1Service proj1Service = new Proj1Service();
            Proj1Entity proj1 = proj1Service.getProj1(proj2.getPID(), requestID);
            List<Budget1Dto> budget1DtoList = getbudget1DtoList(requestID, proj1,proj2,wfMainMap,detailList1);
            BudgetSplitService budgetSplitService = new BudgetSplitService();
            dto = budgetSplitService.splitBudget1(budget1DtoList,BudTypeEnum.SPLIT_FREZEE,true);
        }else{
            List<Budget23Dto> budget2DtoList = getbudget2DtoList(requestID,proj2,wfMainMap,detailList1);
            //冻结子项预算
            BudgetService budgetService = new BudgetService();
            dto = budgetService.executeBudget23(budget2DtoList,BudTypeEnum.FREZEE);
        }

        return dto;
    }
    /**
     * 封装
     * @return
     */
    public List<Budget23Dto> getbudget2DtoList(String requestID, Proj2Entity proj2, Map<String, String> wfMainMap, List<Map<String, String>> detailList1){

        List<Budget23Dto> budget2DtoList = new ArrayList<>();

        boolean isParentCtrl = CtrlLevelEnum.PARENT.compareTo(proj2.getCtrlLevel()) == 0;

        for (Map<String, String> map : detailList1) {

            double useAmt = Double.valueOf(StringUtil.toNum(map.get("hsrmbje")));

            Budget23Dto dto = new Budget23Dto();
            dto.setRequestID(requestID);
            dto.setDetailID(map.get("id"));
            dto.setProjID(proj2.getId());
            dto.setProjDeptNo(proj2.getProjDeptNo());
            if(isParentCtrl){
                dto.setUseType(BudTypeEnum.SPLIT_FREZEE);
            }else {
                dto.setUseType(BudTypeEnum.FREZEE);
            }
            dto.setUseAmt(useAmt);

            dto.setCreDate(TimeUtil.getCurrentDateString());
            dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setCreUser(getMapV(wfMainMap,"shenqr"));
//            dto.setModDate();
//            dto.setModTime();
            dto.setPID(proj2.getPID());
            dto.setCtrlLevel(proj2.getCtrlLevel());
            dto.setProjNo(proj2.getProjNo());

            budget2DtoList.add(dto);
        }
        return budget2DtoList;
    }


    /**
     * 封装父项冻结预算
     * @param requestID
     * @param proj1
     * @param proj2
     * @param wfMainMap
     * @param detailList1
     * @return
     */
    public List<Budget1Dto> getbudget1DtoList(String requestID, Proj1Entity proj1, Proj2Entity proj2, Map<String, String> wfMainMap, List<Map<String, String>> detailList1){
        List<Budget1Dto> budget1DtoList = new ArrayList<>();

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
            dto.setCreDate(TimeUtil.getCurrentDateString());
            dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setCreUser(shenqr);
//        dto.setModDate();
//        dto.setModTime();
            dto.setPID(proj1.getPID());
            dto.setCtrlLevel(proj1.getCtrlLevel());
            dto.setProjNo(proj1.getProjNo());

            budget1DtoList.add(dto);
        }

        if(isParentCtrl){
            /*
                孙子项拆解  需要把子项的拆解金额释放  取子项和孙子项的最大值
                孙子项合计金额为 A  子项金额为B
                A <= B  释放金额为 A
                A > B 释放金额为 B
=             */
            double A = Double.valueOf(StringUtil.toNum(wfMainMap.get("zxjehj")));
            double B = proj2.getProjAmt();
            double useAmt = A <= B ? A : B;
            Budget1Dto dto = new Budget1Dto();
            dto.setRequestID(requestID);
            dto.setDetailID("0");
            dto.setProjID(proj1.getId());
            dto.setProjDeptNo(proj1.getProjDeptNo());
            dto.setUseType(BudTypeEnum.SPLIT_FREZEE);
            dto.setUseAmt(-useAmt);
            dto.setCreDate(TimeUtil.getCurrentDateString());
            dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setCreUser(shenqr);
//        dto.setModDate();
//        dto.setModTime();
            dto.setPID(proj1.getPID());
            dto.setCtrlLevel(proj1.getCtrlLevel());
            dto.setProjNo(proj1.getProjNo());
            dto.setSplit("0");

            budget1DtoList.add(dto);
        }

        return budget1DtoList;
    }

}
