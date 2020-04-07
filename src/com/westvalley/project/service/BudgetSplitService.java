package com.westvalley.project.service;

import com.westvalley.project.dao.Proj1Dao;
import com.westvalley.project.dto.Budget1Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.Proj1Entity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.po.BudgetExcuPo;
import com.westvalley.util.LogUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算分解逻辑  -- 控制级别为父项   只控制拆解
 */
public class BudgetSplitService {

    private LogUtil log = LogUtil.getLogger(getClass());

    private Proj1Dao proj1Dao;
    public BudgetSplitService(){
        proj1Dao = new Proj1Dao();
    }

    /**
     * 分解父项
     * @param budget1DtoList
     * @param budTypeEnum SPLIT_FREZEE 代表申请节点提交  SPLIT_USED 代表归档
     * @return
     */
    public synchronized ResultDto splitBudget1(List<Budget1Dto> budget1DtoList,BudTypeEnum budTypeEnum,boolean isDelete){
        ResultDto dto = checkSplit1(budget1DtoList);
        if(!dto.isOk()){
            return dto;
        }
        BudgetExcuService excuService = new BudgetExcuService();
        List<BudgetExcuPo> data4Split = excuService.getData4Split(budget1DtoList, budTypeEnum);
        return excuService.excuBudget(data4Split,isDelete);
    }

    /**
     * 父项分解校验
     * @param budget1DtoList
     * @return
     */
    public ResultDto checkSplit1(List<Budget1Dto> budget1DtoList){
        if(budget1DtoList == null || budget1DtoList.size() == 0 ){
            return  ResultDto.error("父项分解校验数据不能为空");
        }
        log.d("budget1DtoList",budget1DtoList.size(),budget1DtoList);
        StringBuilder msg = new StringBuilder();

        Map<String,Double> groupUse = new HashMap<>();//总使用金额

        for (Budget1Dto dto : budget1DtoList) {
            int projID = dto.getProjID();
            String requestID = dto.getRequestID();
            double useAmt = dto.getUseAmt();

            String key = String.valueOf(projID)+"@"+requestID;
            if("0".equals(dto.getSplit())){
                //对冲不校验
            }else {
                if (useAmt <= 0) {
                    msg.append("拆解预算，金额必须大于0！");
                    continue;
                }
            }
            Double money = groupUse.get(key);
            if(money == null){
                groupUse.put(key,useAmt);
            }else{
                groupUse.put(key,add(money,useAmt).doubleValue());
            }
        }
        if(msg.length() == 0){
            for (String key : groupUse.keySet()) {
                String[] sp = key.split("@");
                int id = Integer.parseInt(sp[0]);
                String reqID = sp[1];
                double amt = groupUse.get(key);
                Proj1Entity proj1 = proj1Dao.getProj1BudgetByID(id, reqID);
                double balance = proj1.getProjCanSplitAmt();
                if(amt > balance){
                    msg.append("<br>父项预算拆解总金额不能大于可用余额！")
                            .append("项目编号：").append(proj1.getProjNo())
                            .append(",拆解总金额：").append(amt)
                            .append(",可用余额：").append(balance);
                    continue;
                }
            }
        }
        return ResultDto.init(msg.length() == 0,msg.toString(),null);
    }

    public BigDecimal add(double a,double b){
        BigDecimal aa = new BigDecimal(String.valueOf(a));
        BigDecimal bb = new BigDecimal(String.valueOf(b));
        return aa.add(bb);
    }

}
