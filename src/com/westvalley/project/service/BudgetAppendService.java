package com.westvalley.project.service;

import com.westvalley.project.dao.Proj1Dao;
import com.westvalley.project.dto.AppendDto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.ProjEntity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.ProjtypeEnum;
import com.westvalley.project.po.BudgetExcuPo;
import com.westvalley.project.po.Proj1PrecentPo;
import com.westvalley.project.util.ProjUtil;
import com.westvalley.util.LogUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算追加逻辑
 */
public class BudgetAppendService {

    private LogUtil log = LogUtil.getLogger(getClass());


    private BudgetService budgetService;
    public BudgetAppendService(){
        budgetService = new BudgetService();
    }

    /**
     * 追加生效
     * @param appendDtoList
     * @param budTypeEnum FREZEE代表申请节点提交  APPEND 代表归档
     * @param isDelete
     * @return
     */
    public synchronized ResultDto appendBudget(List<AppendDto> appendDtoList, BudTypeEnum budTypeEnum,boolean isDelete){
        log.d("appendDtoList",appendDtoList);
        ResultDto resultDto = checkTransfer(appendDtoList);
        if(!resultDto.isOk()){
            return resultDto;
        }
        BudgetExcuService excuService = new BudgetExcuService();
        List<BudgetExcuPo> data4Append = excuService.getData4Append(appendDtoList,budTypeEnum);
        resultDto = excuService.excuBudget(data4Append,isDelete);
        if(!resultDto.isOk()){
            return resultDto;
        }

        if(BudTypeEnum.APPEND.compareTo(budTypeEnum) == 0){
            //如果涉及父项追加，需要更新分摊比例
            List<Proj1PrecentPo> proj1PrecentPoList = new ArrayList<>();
            for (AppendDto dto : appendDtoList) {
                ProjtypeEnum projType = dto.getProjType();
                if(ProjtypeEnum.ORG.compareTo(projType) != 0){
                    continue;
                }
                //祖项才有更新数据
                Proj1PrecentPo po = new Proj1PrecentPo();
                po.setProj1RequestID(ProjUtil.getProjCreRequestID(dto.getProjID()));//父项创建流程
                po.setCreRequestID(dto.getRequestID());
                po.setProj1ID(dto.getProjID());
                po.setProj0ID(dto.getPID());
                po.setBalance(dto.getPidBalance());//祖项的本次立项金额
//                po.setPerson();
//                po.setCcy();
                po.setRate(1);
                po.setUseAmt(dto.getUseAmt());//本次追加金额
                po.setCreUser(dto.getCreUser());
                po.setRemark("父项更新分摊");

                proj1PrecentPoList.add(po);
            }
            if(proj1PrecentPoList.size() > 0){
                Proj1Dao proj1Dao = new Proj1Dao();
                resultDto = proj1Dao.updatePrecent(proj1PrecentPoList);
            }
        }
        return resultDto;
    }




    /**
     * 校验追加金额
     * @param appendDtoList
     * @return
     */
    public ResultDto checkTransfer(List<AppendDto> appendDtoList){
        StringBuilder msg = new StringBuilder();
        Map<String,Double> amtMap = new HashMap<>();//拨款总金额
        for (AppendDto appendDto : appendDtoList) {
            int outID = appendDto.getPID();
            String requestID = appendDto.getRequestID();
            double useAmt = appendDto.getUseAmt();
            ProjEntity projInfo = budgetService.getProjInfo(outID,requestID);
            double projBalance = projInfo.getProjBalance();
            if(useAmt > projBalance){

                msg.append(" 项目编号：").append(projInfo.getProjNo());
                msg.append(" 项目名称：").append(projInfo.getProjName());
                msg.append(" 追加金额大于剩余预算");
                msg.append(" 追加金额：").append(useAmt) ;
                msg.append(" 剩余预算: ").append(projBalance);
                return ResultDto.error(msg.toString());
            }
            String key = String.valueOf(outID)+"@"+requestID;
            Double money = amtMap.get(key);
            if(money == null){
                amtMap.put(key,useAmt);
            }else{
                amtMap.put(key,add(money,useAmt).doubleValue());
            }
        }
        //校验总释放释放金额
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
                msg.append(" 追加总金额大于剩余预算");
                msg.append(" 追加总金额：").append(amt) ;
                msg.append(" 剩余预算: ").append(projBalance);
                return ResultDto.error(msg.toString());
            }
        }

        return ResultDto.ok("追加预算校验成功",null);
    }


    public BigDecimal add(double a,double b){
        BigDecimal aa = new BigDecimal(String.valueOf(a));
        BigDecimal bb = new BigDecimal(String.valueOf(b));
        return aa.add(bb);
    }


}
