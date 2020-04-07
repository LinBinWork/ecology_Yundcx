package com.westvalley.action.prepay;

import com.westvalley.project.action.DevAbstractAction;
import com.westvalley.project.dto.ActionDto;
import com.westvalley.project.dto.ContractDto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.service.BudgetContractService;
import com.westvalley.project.util.EnumsUtil;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import com.westvalley.util.StringUtil;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 预付款提交
 */
public class PrepayCKAction extends DevAbstractAction{


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
        Map<String, String> mainMap = DevUtil.getWFMainMapByReqID(requestID);
        log.d("mainMap",mainMap);
        String sfywht = mainMap.get("sfywht");//是否有无合同付款  0 有

        String contractRequestID = "";
        if("0".equals(sfywht)){
            contractRequestID = mainMap.get("htlc");
        }



        BudgetContractService contractService = new BudgetContractService();
        List<ContractDto> contractList = getContractList(requestID,mainMap);
        return contractService.contractBudget(contractRequestID,requestID,0,contractList, BudTypeEnum.CONTRACT_FREZEE);
    }


    /**
     * 获取预付款数据
     * @param requestID
     * @param mainMap
     * @return
     */
    public List<ContractDto> getContractList(String requestID,Map<String, String> mainMap){

        List<Map<String, String>> wfDetail1 = DevUtil.getWFDetailByReqID(requestID, 1);
        log.d("wfDetail1",wfDetail1);

        String shenqr = getMapV(mainMap, "shenqr");
        List<ContractDto> contractDtoList = new ArrayList<>();
        for (Map<String, String> map : wfDetail1) {
            ContractDto dto = new ContractDto();
            dto.setRequestID(requestID);
            dto.setDetailID(""+getMapV(map,"id"));
            dto.setProjID(Util.getIntValue(getMapV(map,"zxszx")));
            dto.setProjType(Util.getIntValue(getMapV(map,"projtype")));
            dto.setUseType(BudTypeEnum.CONTRACT_FREZEE);
            dto.setUseAmt(getMapAmtV(map,"fkje"));
            dto.setCreUser(shenqr);
            dto.setRemark("");
            dto.setCtrlLevel(EnumsUtil.getCtrlLevelEnum(Util.getIntValue(getMapV(map,"ctrlLevel"))));
            contractDtoList.add(dto);
        }

        return contractDtoList;
    }

}
