package com.westvalley.project.action.transfer23;

import com.westvalley.project.action.DevAbstractAction;
import com.westvalley.project.dto.ActionDto;
import com.westvalley.project.dto.AppendDto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.dto.TransferDto;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.ProjtypeEnum;
import com.westvalley.project.service.BudgetAppendService;
import com.westvalley.project.service.BudgetService;
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
 * 子项/孙子项预算调拨   冻结预算
 */
public class TransferProj23CKAction extends DevAbstractAction{
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
        String dzlx = mainMap.get("dzlx");

        if("0".equals(dzlx)){
            return transfer(requestID);
        }else if("1".equals(dzlx)){
            return appendProj2(requestID);
        }else if("2".equals(dzlx)){
            return appendProj3(requestID);
        }else{
            return  null;
        }

    }

    /**
     *孙子项追加
     * @param requestID
     * @return
     */
    public ResultDto appendProj3(String requestID) {
        List<AppendDto> appendDtoList = new ArrayList<>();

        Map<String, String> mainMap = DevUtil.getWFMainMapByReqID(requestID);
        String shenqr = mainMap.get("shenqr");

        List<Map<String, String>> wfDetail3 = DevUtil.getWFDetailByReqID(requestID, 3);
        log.d("wfDetail3",wfDetail3);
        for (Map<String, String> map : wfDetail3) {
            AppendDto dto = new AppendDto();
            dto.setRequestID(requestID);
            dto.setDetailID(""+getMapV(map,"id"));
            dto.setPID(Util.getIntValue(getMapV(map,"zx")));
            dto.setProjID(Util.getIntValue(getMapV(map,"szx")));
            dto.setProjType(ProjtypeEnum.SON);
            dto.setUseAmt(getMapAmtV(map,"brysje"));
            dto.setCreUser(shenqr);
            appendDtoList.add(dto);
        }
        BudgetAppendService appendService = new BudgetAppendService();
        return appendService.appendBudget(appendDtoList,BudTypeEnum.FREZEE,true);
    }

    /**
     * 子项追加
     * @param requestID
     * @return
     */
    public ResultDto appendProj2(String requestID) {
        List<AppendDto> appendDtoList = new ArrayList<>();

        Map<String, String> mainMap = DevUtil.getWFMainMapByReqID(requestID);
        String shenqr = mainMap.get("shenqr");

        List<Map<String, String>> wfDetail2 = DevUtil.getWFDetailByReqID(requestID, 2);
        log.d("wfDetail2",wfDetail2);
        for (Map<String, String> map : wfDetail2) {
            AppendDto dto = new AppendDto();
            dto.setRequestID(requestID);
            dto.setDetailID(""+getMapV(map,"id"));
            dto.setPID(Util.getIntValue(getMapV(map,"fx")));
            dto.setProjID(Util.getIntValue(getMapV(map,"zx")));
            dto.setProjType(ProjtypeEnum.PARENT);
            dto.setUseAmt(getMapAmtV(map,"brysje"));
            dto.setCreUser(shenqr);
            appendDtoList.add(dto);
        }
        BudgetAppendService appendService = new BudgetAppendService();
        return appendService.appendBudget(appendDtoList,BudTypeEnum.FREZEE,true);
    }


    /**
     * 预算调拨
     * @param requestID
     * @return
     */
    public ResultDto transfer(String requestID){

        List<Map<String, String>> wfDetail1 = DevUtil.getWFDetailByReqID(requestID, 1);
        log.d("wfDetail1",wfDetail1);

        ResultDto dto = checkData(wfDetail1);
        if(!dto.isOk()){
            return dto;
        }
        BudgetTransferService transferService = new BudgetTransferService();

        List<TransferDto> transferDtoList = getTransferDtoList(requestID,wfDetail1);
        return transferService.transferBudget(transferDtoList,BudTypeEnum.FREZEE,true);
    }

    /**
     * 校验数据  如果控制级别是父项，则不允许提交
     * @param wfDetail1
     * @return
     */
    public ResultDto checkData(List<Map<String, String>> wfDetail1){
        int index = 1;
        for (Map<String, String> map : wfDetail1) {
            String ctrlLevel = getMapV(map, "ctrlLevel");
            if("0".equals(ctrlLevel)){
                return ResultDto.error("第"+index+"行,控制级别为父项，不允许提交");
            }
            index++;
        }
        return ResultDto.ok();
    }

    /**
     * 冻结预算数据
     * @param wfDetail1
     * @return
     */
    public List<TransferDto> getTransferDtoList(String requestID,List<Map<String, String>> wfDetail1){
        Map<String, String> mainMap = DevUtil.getWFMainMapByReqID(requestID);
        String shenqr = mainMap.get("shenqr");
        List<TransferDto> transferDtoList = new ArrayList<>();
        for (Map<String, String> map : wfDetail1) {

            double useAmt = Double.valueOf(StringUtil.toNum(map.get("brysje")));

            TransferDto dto = new TransferDto();
            dto.setRequestID(requestID);
            dto.setDetailID(getMapV(map,"id"));
            dto.setOutPID(Util.getIntValue(getMapV(map,"OutPID")));
            dto.setOutID(Util.getIntValue(getMapV(map,"zxszx")));
            dto.setInPID(Util.getIntValue(getMapV(map,"InPID")));
            dto.setInID(Util.getIntValue(getMapV(map,"brzxszx")));
            dto.setProjType(Util.getIntValue(getMapV(map,"projType")));
            dto.setUseType(BudTypeEnum.FREZEE);
            dto.setUseAmt(useAmt);
            dto.setCreDate(TimeUtil.getCurrentDateString());
            dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setCreUser(shenqr);
//            dto.setRemark();

            transferDtoList.add(dto);

        }
        return transferDtoList;
    }





}
