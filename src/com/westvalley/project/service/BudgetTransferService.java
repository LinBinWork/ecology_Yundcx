package com.westvalley.project.service;

import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.dto.TransferDto;
import com.westvalley.project.entity.ProjEntity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.po.BudgetExcuPo;
import com.westvalley.util.LogUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算调拨逻辑
 */
public class BudgetTransferService {

    private LogUtil log = LogUtil.getLogger(getClass());

    private BudgetService budgetService;
    public BudgetTransferService(){
        budgetService = new BudgetService();
    }

    /**
     * 调拨生效
     * @param transferDtoList
     * @param budTypeEnum FREZEE 代表申请节点提交  TRANSFER 代表归档
     * @return
     */
    public synchronized ResultDto transferBudget(List<TransferDto> transferDtoList, BudTypeEnum budTypeEnum,boolean isDelete){
        log.d("transferDtoList",transferDtoList);
        ResultDto dto = checkTransfer(transferDtoList);
        if(!dto.isOk()){
            return dto;
        }
        BudgetExcuService excuService = new BudgetExcuService();
        List<BudgetExcuPo> data4Transfer = excuService.getData4Transfer(transferDtoList, budTypeEnum);
        return excuService.excuBudget(data4Transfer,isDelete);
    }


    /**
     * 校验调拨金额
     * @param transferDtoList
     * @return
     */
    public ResultDto checkTransfer(List<TransferDto> transferDtoList){
        StringBuilder msg = new StringBuilder();
        Map<String,Double> amtMap = new HashMap<>();//拨款总金额
        for (TransferDto transferDto : transferDtoList) {
            int outID = transferDto.getOutID();
            String requestID = transferDto.getRequestID();
            double useAmt = transferDto.getUseAmt();
            if(useAmt <= 0 ){
                return ResultDto.error("调拨金额必须大于0");
            }
            String key = String.valueOf(outID)+"@"+requestID;
            Double money = amtMap.get(key);
            if(money == null){
                amtMap.put(key,useAmt);
            }else{
                amtMap.put(key,add(money,useAmt).doubleValue());
            }
        }
        for (String key : amtMap.keySet()) {
            String[] sp = key.split("@");
            int outID = Integer.parseInt(sp[0]);
            String reqID = sp[1];
            Double amt = amtMap.get(key);
            ProjEntity projInfo = budgetService.getProjInfo(outID,reqID);
            double projBalance = projInfo.getProjBalance();
            if(amt > projBalance){
                msg.append(" 项目编号：").append(projInfo.getProjNo());
                msg.append(" 项目名称：").append(projInfo.getProjName());
                msg.append(" 调拨总金额大于剩余预算");
                msg.append(" 调拨总金额：").append(amt) ;
                msg.append(" 剩余预算: ").append(projBalance);
                return ResultDto.error(msg.toString());
            }
        }

        return ResultDto.ok("调拨预算校验成功",null);
    }

    /**
     * 祖项父项调拨
     * @param transferDtoList
     * @param isYearTransfer 是否年度预算调整
     * @param budTypeEnum FREZEE 代表申请节点提交  TRANSFER 代表归档
     * @return
     */
    public synchronized ResultDto transferBudget12(List<TransferDto> transferDtoList,boolean isYearTransfer,BudTypeEnum budTypeEnum,boolean isDelete){
        log.d("transferDtoList",transferDtoList);
        ResultDto dto = null;
        if(isYearTransfer){
            dto = checkTransfer12(transferDtoList,isYearTransfer);
        }else{
            dto = checkTransfer(transferDtoList);
        }
        if(!dto.isOk()){
            return dto;
        }
        BudgetExcuService excuService = new BudgetExcuService();
        List<BudgetExcuPo> data4Transfer = excuService.getData4Transfer(transferDtoList, budTypeEnum);
        return excuService.excuBudget(data4Transfer,isDelete);
    }
    /**
     * 祖项/父项调拨
     * @param transferDtoList 数据
     * @param isYearTransfer 是否年度预算调整
     * @return
     */
    public ResultDto checkTransfer12(List<TransferDto> transferDtoList,boolean isYearTransfer){
        if(isYearTransfer){
            StringBuilder msg = new StringBuilder();
            //年度预算校验
            for (TransferDto dto : transferDtoList) {
                double useAmt = dto.getUseAmt();
                if(useAmt > 0){
                    //调增 不校验
                    log.d("调增不校验",dto.getInID(),useAmt);
                }else{
                    ProjEntity projInfo = budgetService.getProjInfo(dto.getInID(),dto.getRequestID());
                    double projBalance = projInfo.getProjBalance();
                    //调减金额不能超过剩余金额
                    if(add(projBalance,useAmt).compareTo(BigDecimal.ZERO) <= 0){
                        msg.append(" 项目编号：").append(projInfo.getProjNo());
                        msg.append(" 项目名称：").append(projInfo.getProjName());
                        msg.append(" 调减总金额大于剩余预算");
                        msg.append(" 调减总金额：").append(useAmt) ;
                        msg.append(" 剩余预算: ").append(projBalance);
                        msg.append("<br>");
                    }
                }
            }
            return ResultDto.init(msg.length() == 0,msg.toString(),null);
        }else{
            return checkTransfer(transferDtoList);
        }
    }

    public BigDecimal add(double a,double b){
        BigDecimal aa = new BigDecimal(String.valueOf(a));
        BigDecimal bb = new BigDecimal(String.valueOf(b));
        return aa.add(bb);
    }


}
