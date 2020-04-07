package com.westvalley.project.action.p1;

import com.westvalley.project.action.DevAbstractAction;
import com.westvalley.project.dto.ActionDto;
import com.westvalley.project.dto.Budget0Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.Proj0Entity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.service.BudgetService;
import com.westvalley.project.service.Proj0Service;
import com.westvalley.project.service.Proj1Service;
import com.westvalley.util.DevUtil;
import com.westvalley.util.StringUtil;
import weaver.general.TimeUtil;
import weaver.soa.workflow.request.RequestInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 父项立项拆解校验
 */
public class Proj1CKAction extends DevAbstractAction{
    /**
     * 业务逻辑
     *
     * @param info
     * @param actionDto
     * @return 返回 null 或者ok 则代表通过
     */
    @Override
    protected ResultDto runCode(RequestInfo info, ActionDto actionDto) {

        List<Budget0Dto> budget0DtoList = getbudget0DtoList(actionDto.getRequestID());
        BudgetService budgetService = new BudgetService();
        //父项立项，冻结祖项预算
        ResultDto dto = budgetService.executeBudget0(budget0DtoList,BudTypeEnum.FREZEE,true);
        return dto;
    }

    /**
     * 封装祖项预算数据，冻结预算
     * @return
     */
    public List<Budget0Dto> getbudget0DtoList(String requestID){

        Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestID);
        log.d("wfMainMap",wfMainMap);
        List<Map<String, String>> detailList2 = DevUtil.getWFDetailByReqID(requestID,2);
        log.d("detailList2",detailList2);

        List<Budget0Dto> budget0DtoList = new ArrayList<>();

        Proj0Service proj0Service = new Proj0Service();

        String lxrq = wfMainMap.get("lxrq");
        String shenqr = wfMainMap.get("shenqr");

        for (Map<String, String> map : detailList2) {

            int projID = Integer.valueOf(map.get("ftbm"));
            double useAmt = Double.valueOf(StringUtil.toNum(map.get("bclxje")));

            Proj0Entity entity = proj0Service.getProj0(projID,requestID);
            Budget0Dto dto = new Budget0Dto();

            dto.setRequestID(requestID);
            dto.setDetailID(map.get("id"));
            dto.setProjID(projID);
            dto.setProjDeptNo(entity.getProjNo());
            dto.setUseType(BudTypeEnum.FREZEE);
            dto.setUseAmt(useAmt);
            dto.setCreDate(lxrq);
            dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setCreUser(shenqr);
//            dto.setModDate();
//            dto.setModTime();
            dto.setPID(0);
            dto.setCtrlLevel(entity.getCtrlLevel());
            dto.setProjNo(entity.getProjNo());

            budget0DtoList.add(dto);

        }

        return budget0DtoList;
    }

}
