package com.westvalley.project.service;

import com.westvalley.project.dao.ContractDao;
import com.westvalley.project.dto.ContractDto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.ContractBudgetDetailEntity;
import com.westvalley.project.entity.ContractBudgetEntity;
import com.westvalley.project.entity.ProjEntity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.CtrlLevelEnum;
import com.westvalley.project.po.BudgetExcuPo;
import com.westvalley.project.util.ProjUtil;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import com.westvalley.util.StringUtil;
import weaver.general.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同付款  预算扣减逻辑
 */
public class BudgetContractService {

    private LogUtil log = LogUtil.getLogger(getClass());

    private ContractDao contractDao;
    private BudgetService budgetService;
    public BudgetContractService(){
        contractDao = new ContractDao();
        budgetService = new BudgetService();
    }
    /**
     * 合同使用预算 默认删除
     * @param contractRequestID 合同流程ID  为空则代表不使用合同
     * @param currentRequestID 当前流程ID
     * @param releaseAmt 预付款核销总金额
     * @param contractDtoList 付款数据
     * @param budTypeEnum CONTRACT_FREZEE代表申请节点提交  CONTRACT_USED 代表归档
     * @return
     */
    public synchronized ResultDto contractBudget(String contractRequestID, String currentRequestID,double releaseAmt,List<ContractDto> contractDtoList, BudTypeEnum budTypeEnum){
       return contractBudget(contractRequestID,currentRequestID,releaseAmt,contractDtoList,budTypeEnum,true);
    }

    /**
     * 合同使用预算
     * @param contractRequestID 合同流程ID  为空则代表不使用合同
     * @param currentRequestID 当前流程ID
     * @param releaseAmt 预付款核销总金额
     * @param contractDtoList 付款数据
     * @param budTypeEnum CONTRACT_FREZEE代表申请节点提交  CONTRACT_USED 代表归档
     * @param isDelete 是否删除数据，一条流程一个节点只能删除一次
     * @return
     */
    public synchronized ResultDto contractBudget(String contractRequestID, String currentRequestID,double releaseAmt, List<ContractDto> contractDtoList, BudTypeEnum budTypeEnum,boolean isDelete){
        log.d("contractDtoList",contractRequestID,contractDtoList);

        if(!isContract(contractRequestID)){
            //不是的话就置空
            contractRequestID = "";
        }

        ResultDto dto = checkContract(contractRequestID,currentRequestID,contractDtoList);
        if(!dto.isOk()){
            return dto;
        }
        boolean isContractPay = !StringUtil.isEmpty(contractRequestID);
        //执行合同预算
        BudgetExcuService excuService = new BudgetExcuService();
        List<BudgetExcuPo> data4Contract = excuService.getData4Contract(contractDtoList, budTypeEnum,isContractPay );
        dto = excuService.excuBudget(data4Contract,isDelete);
        if(!dto.isOk()){
            return dto;
        }
        if(isContractPay){
            dto =  contractDao.contractBudget(contractRequestID,currentRequestID,releaseContractDtoList(releaseAmt,contractDtoList));
        }
        return dto;
    }

    /**
     * 根据付款明细 比例核销
     * @param releaseAmtD
     * @param contractDtoList
     * @return
     */
    public List<ContractDto> releaseContractDtoList(double releaseAmtD, List<ContractDto> contractDtoList){

        if(releaseAmtD <= 0){
            return contractDtoList;
        }
        BigDecimal releaseBigAmt = new BigDecimal(StringUtil.toNum(releaseAmtD));

        //按比例减少本次合同付款金额
        BigDecimal total = BigDecimal.ZERO;
        Map<ContractDto,Double> amtMap = new HashMap<>();
        for (ContractDto dto : contractDtoList) {
            double useAmt = dto.getUseAmt();

            total = add(total.doubleValue(),useAmt);

            Double amt = amtMap.get(dto);
            if(amt == null){
                amtMap.put(dto,useAmt);
            }else{
                amtMap.put(dto,add(amt,useAmt).doubleValue());
            }
        }
        //计算分摊减少金额
        int counts = amtMap.size();
        Map<ContractDto,BigDecimal> preMap = new HashMap<>();
        BigDecimal temp = BigDecimal.ZERO;
        for (ContractDto key : amtMap.keySet()) {
            counts--;
            BigDecimal amt = new BigDecimal(StringUtil.toNum(amtMap.get(key)));

            BigDecimal subAmt = null;
            if(counts == 0){
                subAmt = releaseBigAmt.subtract(temp);
            }else{
                subAmt = releaseBigAmt.multiply(amt.divide(total,2,BigDecimal.ROUND_HALF_UP));
                temp = temp.add(subAmt);
            }
            preMap.put(key,subAmt);
        }
        //计算减少金额
        for (ContractDto dto : contractDtoList) {
            BigDecimal subAmt = preMap.get(dto);
            BigDecimal useAmt = add(dto.getUseAmt(), -subAmt.doubleValue());
            dto.setUseAmt(useAmt.doubleValue());
        }
        return contractDtoList;
    }


        /**
         * 校验合同使用金额
         *
         * @param contractRequestID
         * @param currentRequestID
         * @param contractDtoList
         * @return
         */
    public ResultDto checkContract(String contractRequestID, String currentRequestID, List<ContractDto> contractDtoList){
        StringBuilder msg = new StringBuilder();
        if(StringUtil.isEmpty(contractRequestID)){
            //不使用合同
            Map<String,Double> groupUse = new HashMap<>();//总使用金额
            for (ContractDto dto : contractDtoList) {
                double useAmt = dto.getUseAmt();
                // 冻结/使用 预算
                if(useAmt < 0 ){
                    msg.append("使用/冻结预算，金额不能小于0！");
                    continue;
                }
                int projID = dto.getProjID();
                if(CtrlLevelEnum.PARENT.compareTo(dto.getCtrlLevel()) == 0){
                    projID = ProjUtil.getProj1ID4Child(projID);
                }
                String key = String.valueOf(projID);
                Double money = groupUse.get(key);
                if(money == null){
                    groupUse.put(key,useAmt);
                }else{
                    groupUse.put(key,add(money,useAmt).doubleValue());
                }
            }
            if(msg.length() == 0){
                for (String key : groupUse.keySet()) {
                    int id = Integer.parseInt(key);
                    Double amt = groupUse.get(key);

                    ProjEntity projInfo = budgetService.getProjInfo(id, currentRequestID);
                    double balance = projInfo.getProjBalance();
                    if(amt > balance){
                        msg.append("<br>项目预算使用/冻结总金额不能大于可用余额！")
                                .append("项目编号：").append(projInfo.getProjNo())
                                .append(",项目名称：").append(projInfo.getProjName())
                                .append(",使用/冻结总金额：").append(amt)
                                .append(",可用余额：").append(balance);
                        continue;
                    }
                }
            }
        }else{
            //校验合同
            ContractBudgetEntity cben = contractDao.getContractBudgetEntity(contractRequestID,currentRequestID);
            BigDecimal total = BigDecimal.ZERO;
            for (ContractDto dto : contractDtoList) {
                total = add(total.doubleValue(),dto.getUseAmt());
            }
            double balance = cben.getContractBalance();
            double amt = total.doubleValue();
            if( amt > balance){
                msg.append("<br>合同付款总金额不能大于未付金额！")
                        .append("合同编号：").append(cben.getContractNo())
                        .append(",合同名称：").append(cben.getContractName())
                        .append(",付款总金额：").append(amt)
                        .append(",未付金额：").append(balance);
            }
            Map<String,Double> detailMap = null;
            Map<String,Double> groupUse = null;
            if(msg.length() == 0){
                //校验合同付款项目及金额是否正确
                List<ContractBudgetDetailEntity> detailEntityList = cben.getDetailEntityList();
                detailMap = new HashMap<>();
                for (ContractBudgetDetailEntity detailEntity : detailEntityList) {
                    int projID = detailEntity.getProjID();
                    double freezAmt = detailEntity.getFreezAmt();
                    String key = String.valueOf(projID);
                    Double money = detailMap.get(key);
                    if(money == null){
                        detailMap.put(key,freezAmt);
                    }else{
                        detailMap.put(key,add(money,freezAmt).doubleValue());
                    }
                }
                groupUse = new HashMap<>();//总使用金额
                //校验项目是否存在合同中
                for (ContractDto dto : contractDtoList) {
                    int projID = dto.getProjID();
                    double useAmt = dto.getUseAmt();
                    String key = String.valueOf(projID);
                    if(!detailMap.containsKey(key)){
                        ProjEntity projInfo = budgetService.getProjInfo(projID,"");
                        msg.append("<br>合同预付款/付款项目必须是合同申请时填写的项目！")
                                .append("不合法的项目编号：").append(projInfo.getProjNo())
                                .append(",不合法的项目名称：").append(projInfo.getProjName());
                        continue;
                    }
                    Double money = detailMap.get(key);
                    if(money == null){
                        detailMap.put(key,useAmt);
                    }else{
                        detailMap.put(key,add(money,useAmt).doubleValue());
                    }
                }
            }
            //校验项目金额
            if(msg.length() == 0){
                for (String key : groupUse.keySet()) {
                    double freezAmt = detailMap.get(key);
                    double useAmt = groupUse.get(key);
                    if( useAmt > freezAmt){
                        ProjEntity projInfo = budgetService.getProjInfo(Util.getIntValue(key),"");
                        msg.append("<br>合同项目付款总金额不能大于项目剩余冻结金额！")
                                .append("项目编号：").append(projInfo.getProjNo())
                                .append(",项目名称：").append(projInfo.getProjName())
                                .append(",付款总金额：").append(useAmt)
                                .append(",剩余冻结金额：").append(freezAmt);
                    }
                }
            }
        }
        return ResultDto.init(msg.length() == 0,msg.toString(),null);
    }


    /**
     * 获取合同金额相关信息
     * @return
     */
    public ContractBudgetEntity getContractBudgetEntity(String contractRequestID,String currentRequestID){
        return contractDao.getContractBudgetEntity(contractRequestID,currentRequestID);
    }

    /**
     * 是否营销项目
     * @param requestID
     * @return
     */
    public boolean isContract(String requestID){
        String type = DevUtil.executeQuery("select type from wv_v_contractFlow where requestid = ? ", requestID);
        return "Y".equals(type);
    }

    public BigDecimal add(double a,double b){
        BigDecimal aa = new BigDecimal(StringUtil.toNum(a));
        BigDecimal bb = new BigDecimal(StringUtil.toNum(b));
        return aa.add(bb);
    }
}
