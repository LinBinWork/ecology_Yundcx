package com.westvalley.project.action.transfer12;

import com.westvalley.project.action.DevAbstractAction;
import com.westvalley.project.dto.ActionDto;
import com.westvalley.project.dto.AppendDto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.dto.TransferDto;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.ProjtypeEnum;
import com.westvalley.project.service.BudgetAppendService;
import com.westvalley.project.service.BudgetTransferService;
import com.westvalley.util.DevUtil;
import com.westvalley.util.StringUtil;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 祖项/父项预算调拨   归档完成预算调拨
 */
public class TransferProj12OKAction extends DevAbstractAction{
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
        List<Map<String, String>> wfDetail1 = DevUtil.getWFDetailByReqID(requestID, 1);
        log.d("wfDetail1",wfDetail1);

        Map<String, String> mainMap = DevUtil.getWFMainMapByReqID(requestID);
        log.d("mainMap",mainMap);
        String dzlx = mainMap.get("dzlx");
        if("3".equals(dzlx)){
            //父项追加
            List<AppendDto> appendDtoList = new ArrayList<>();

            String shenqr = mainMap.get("shenqr");

            List<Map<String, String>> wfDetail2 = DevUtil.getWFDetailByReqID(requestID, 2);
            log.d("wfDetail2",wfDetail2);
            for (Map<String, String> map : wfDetail2) {
                AppendDto dto = new AppendDto();
                dto.setRequestID(requestID);
                dto.setDetailID(""+getMapV(map,"id"));
                dto.setPID(Util.getIntValue(getMapV(map,"zx")));
                dto.setProjID(Util.getIntValue(getMapV(map,"fx")));
                dto.setProjType(ProjtypeEnum.ORG);
                dto.setPidBalance(getMapAmtV(map,"zxysjy"));
                dto.setUseAmt(getMapAmtV(map,"brtoysje"));
                dto.setCreUser(shenqr);
                appendDtoList.add(dto);
            }
            BudgetAppendService appendService = new BudgetAppendService();
            return appendService.appendBudget(appendDtoList,BudTypeEnum.APPEND,true);
        }else {
            boolean isYearTransfer = "0".equals(dzlx);
            BudgetTransferService transferService = new BudgetTransferService();

            List<TransferDto> transferDtoList = getTransferDtoList(requestID, mainMap, wfDetail1, isYearTransfer);
            return transferService.transferBudget12(transferDtoList, isYearTransfer, BudTypeEnum.TRANSFER,true);
        }
    }


    /**
     * 冻结预算数据
     *
     * @param mainMap
     * @param wfDetail1
     * @return
     */
    public List<TransferDto> getTransferDtoList(String requestID, Map<String, String> mainMap, List<Map<String, String>> wfDetail1, boolean isYearTransfer){
        String shenqr = mainMap.get("shenqr");
        List<TransferDto> transferDtoList = new ArrayList<>();
        for (Map<String, String> map : wfDetail1) {

            double useAmt = Double.valueOf(StringUtil.toNum(map.get("brysje")));

            TransferDto dto = new TransferDto();
            dto.setRequestID(requestID);
            dto.setDetailID(getMapV(map,"id"));
//            dto.setOutPID(Util.getIntValue(getMapV(map,"OutPID")));
            dto.setOutID(Util.getIntValue(getMapV(map,"zxfx")));
//            dto.setInPID(Util.getIntValue(getMapV(map,"InPID")));
            dto.setInID(Util.getIntValue(getMapV(map,"brzxfx")));
            if(isYearTransfer){
                dto.setProjType(0);
            }else{
                dto.setProjType(Util.getIntValue(getMapV(map,"projType")));
            }
            dto.setUseType(BudTypeEnum.TRANSFER);
            dto.setUseAmt(useAmt);
            dto.setCreDate(TimeUtil.getCurrentDateString());
            dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setCreUser(shenqr);
            dto.setRemark("预算调拨完成");

            transferDtoList.add(dto);

        }
        return transferDtoList;
    }

}
