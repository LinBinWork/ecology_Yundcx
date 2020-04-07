package com.westvalley.project.service;

import com.westvalley.project.dao.*;
import com.westvalley.project.dto.*;
import com.westvalley.project.entity.*;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.CtrlLevelEnum;
import com.westvalley.project.enums.ProjtypeEnum;
import com.westvalley.project.po.BudgetExcuPo;
import com.westvalley.project.util.EnumsUtil;
import com.westvalley.project.util.ProjUtil;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import weaver.general.Util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算扣减逻辑
 */
public class BudgetService {

    private LogUtil log = LogUtil.getLogger(getClass());

    private Proj0Dao proj0Dao;
    private Proj1Dao proj1Dao;
    private Proj2Dao proj2Dao;
    private Proj3Dao proj3Dao;
    public BudgetService(){

        proj0Dao = new Proj0Dao();
        proj1Dao = new Proj1Dao();
        proj2Dao = new Proj2Dao();
        proj3Dao = new Proj3Dao();
    }


    /**
     * 执行祖项项预算校验
     * @param budget0DtoList
     * @return
     */
    public ResultDto checkBudget0(List<Budget0Dto> budget0DtoList){
        if(budget0DtoList == null || budget0DtoList.size() == 0 ){
            return  ResultDto.error("数据不能为空");
        }
        log.d("budget0DtoList",budget0DtoList.size(),budget0DtoList);
        StringBuilder msg = new StringBuilder();

        for (Budget0Dto dto : budget0DtoList) {
            int projID = dto.getProjID();
            String requestID = dto.getRequestID();
            double useAmt = dto.getUseAmt();
            // 冻结/使用 预算
            if(useAmt < 0 ){
                msg.append("使用/冻结预算，金额不能小于0！");
                continue;
            }
            Proj0Entity proj0 = proj0Dao.getProj0BudgetByID(projID,requestID);
            double projBalance = proj0.getProjBalance();
            if(useAmt > projBalance){
                msg.append("<br>使用/冻结祖项预算，金额不能大于可用余额！")
                        .append("项目编号：").append(proj0.getProjNo())
                        .append("项目名称：").append(proj0.getProjName())
                        .append(",使用/冻结金额：").append(useAmt)
                        .append(",可用余额：").append(projBalance);
                continue;
            }
        }

        return ResultDto.init(msg.length() == 0,msg.toString(),null);
    }

    /**
     * 执行祖项预算扣减
     * @param budget0DtoList
     * @param budTypeEnum FREZEE代表申请节点提交  USED 代表归档
     * @return
     */
    public synchronized ResultDto executeBudget0(List<Budget0Dto> budget0DtoList,BudTypeEnum budTypeEnum,boolean isDelete){
        ResultDto dto = checkBudget0(budget0DtoList);
        if(!dto.isOk()){
            return dto;
        }
        BudgetExcuService excuService = new BudgetExcuService();
        List<BudgetExcuPo> data4Proj0 = excuService.getData4Proj0(budget0DtoList, budTypeEnum);
        return excuService.excuBudget(data4Proj0,isDelete);
    }





    /**
     * 执行父项项预算校验
     * @param budget1DtoList
     * @return
     */
    public ResultDto checkBudget1(List<Budget1Dto> budget1DtoList){
        if(budget1DtoList == null || budget1DtoList.size() == 0 ){
            return  ResultDto.error("数据不能为空");
        }
        log.d("budget1DtoList",budget1DtoList.size(),budget1DtoList);
        StringBuilder msg = new StringBuilder();
        Map<String,Double> groupUse = new HashMap<>();//总使用金额


        for (Budget1Dto dto : budget1DtoList) {
            int projID = dto.getProjID();
            String requestID = dto.getRequestID();
            double useAmt = dto.getUseAmt();

            String key = String.valueOf(projID)+"@"+requestID;
            // 冻结/使用 预算
            if(useAmt < 0 ){
                msg.append("使用/冻结预算，金额不能小于0！");
                continue;
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
                double balance = proj1.getProjBalance();
                if(amt > balance){
                    msg.append("<br>父项预算使用/冻结总金额不能大于可用余额！")
                            .append("项目编号：").append(proj1.getProjNo())
                            .append(",项目名称：").append(proj1.getProjName())
                            .append(",使用/冻结总金额：").append(amt)
                            .append(",可用余额：").append(balance);
                    continue;
                }
            }
        }
        return ResultDto.init(msg.length() == 0,msg.toString(),null);
    }

    /**
     * 执行父项预算扣减
     * @param budget1DtoList
     * @param budTypeEnum FREZEE代表申请节点提交  USED 代表归档
     * @return
     */
    public synchronized ResultDto executeBudget1(List<Budget1Dto> budget1DtoList,BudTypeEnum budTypeEnum,boolean isDelete){
        ResultDto dto = checkBudget1(budget1DtoList);
        if(!dto.isOk()){
            return dto;
        }
        BudgetExcuService excuService = new BudgetExcuService();
        List<BudgetExcuPo> data4Proj1 = excuService.getData4Proj1(budget1DtoList, budTypeEnum);
        return excuService.excuBudget(data4Proj1,isDelete);
    }


    /**
     * 校验子项/孙子项
     * @param budget23DtoList
     * @return
     */
    public ResultDto checkBudget23(List<? extends Budget23Dto> budget23DtoList){
        if(budget23DtoList == null || budget23DtoList.size() == 0 ){
            return  ResultDto.error("数据不能为空");
        }
        log.d("budget23DtoList",budget23DtoList.size(),budget23DtoList);

        StringBuilder msg = new StringBuilder();
        Map<String,Double> groupUse = new HashMap<>();//总使用金额
        for (Budget23Dto dto : budget23DtoList) {

            int projID = dto.getProjID();
            String requestID = dto.getRequestID();
            double useAmt = dto.getUseAmt();
            BudTypeEnum useType = dto.getUseType();
            if(BudTypeEnum.BORROW_REVERSAL.compareTo(useType) == 0){
                if(useAmt < 0 ){
                    msg.append("冲销金额不能小于0！");
                    continue;
                }
            }else{
                // 冻结/使用 预算
                if(useAmt < 0 ){
                    msg.append("使用/冻结预算，金额不能小于0！");
                    continue;
                }
                if(CtrlLevelEnum.PARENT.compareTo(dto.getCtrlLevel()) == 0){
                    //父项
                    projID = dto.getPID();
                }
                String key = String.valueOf(projID)+"@"+requestID;
                Double money = groupUse.get(key);
                if(money == null){
                    groupUse.put(key,useAmt);
                }else{
                    groupUse.put(key,add(money,useAmt).doubleValue());
                }
            }
        }
        if(msg.length() == 0){
            for (String key : groupUse.keySet()) {
                String[] sp = key.split("@");
                int id = Integer.parseInt(sp[0]);
                String reqID = sp[1];
                Double amt = groupUse.get(key);

                ProjEntity projInfo = getProjInfo(id, reqID);
                double balance = projInfo.getProjBalance();
                if(amt > balance){
                    ProjtypeEnum projtype = EnumsUtil.getProjtypeEnum(projInfo.getProjType());
                    if(ProjtypeEnum.PARENT.compareTo(projtype) == 0){
                        msg.append("<br>父项项目");
                    }else if(ProjtypeEnum.SON.compareTo(projtype) == 0){
                        msg.append("<br>子项项目");
                    }else if(ProjtypeEnum.GRANDSON.compareTo(projtype) == 0){
                        msg.append("<br>孙子项项目");
                    }
                    msg.append("预算使用/冻结总金额不能大于可用余额！")
                            .append("项目编号：").append(projInfo.getProjNo())
                            .append(",项目名称：").append(projInfo.getProjName())
                            .append(",使用/冻结总金额：").append(amt)
                            .append(",可用余额：").append(balance);
                    continue;
                }

            }
        }
        return ResultDto.init(msg.length() == 0,msg.toString(),null);
    }


    /**
     * 执行子项/孙子项预算 默认删除
     * @param budget23DtoList
     * @param budTypeEnum
     * @return
     */
    public synchronized ResultDto executeBudget23(List<? extends Budget23Dto> budget23DtoList,BudTypeEnum budTypeEnum){
        return executeBudget23(budget23DtoList,budTypeEnum,true);
    }
    /**
     * 执行子项/孙子项预算
     * @param budget23DtoList
     * @param budTypeEnum
     * @param isDelete 是否根据requestID删除数据 一条流程只能删除一次
     * @return
     */
    public synchronized ResultDto executeBudget23(List<? extends Budget23Dto> budget23DtoList,BudTypeEnum budTypeEnum,boolean isDelete){
        ResultDto dto = checkBudget23(budget23DtoList);
        if(!dto.isOk()){
            return dto;
        }
        BudgetExcuService excuService = new BudgetExcuService();
        List<BudgetExcuPo> data4Proj2 = excuService.getData4Proj23(budget23DtoList, budTypeEnum);
        return excuService.excuBudget(data4Proj2,isDelete);
    }

    /**
     * 获取项目基本信息
     * @param id
     * @param requestID
     * @return
     */
    public ProjEntity getProjInfo(int id,String requestID){
        ProjEntity en = null;
        String projType = DevUtil.executeQuery("select projType from uf_proj where id = ?", id);
        int type = Util.getIntValue(projType);
        if(type == 0){
            en = new ProjEntity();
            Proj0Entity proj = proj0Dao.getProj0BudgetByID(id,requestID);
            en.setId(proj.getId());
            en.setPID(0);
            en.setProjNo(proj.getProjNo());
            en.setProjDeptNo(proj.getProjDeptNo());
            en.setProjName(proj.getProjName());
            en.setProjDesc(proj.getProjDesc());
            en.setProjAmt(proj.getProjAmt());
            en.setProjFreezeAmt(proj.getProjFreezeAmt());
            en.setProjUsedAmt(proj.getProjUsedAmt());
            en.setProjBalance(proj.getProjBalance());
            en.setCtrlLevel(proj.getCtrlLevel());
            en.setDeliveryCenter(1);
        }else if(type == 1){
            en = new ProjEntity();
            Proj1Entity proj = proj1Dao.getProj1BudgetByID(id,requestID);
            en.setId(proj.getId());
            en.setPID(proj.getPID());
            en.setProjNo(proj.getProjNo());
            en.setProjDeptNo(proj.getProjDeptNo());
            en.setProjName(proj.getProjName());
            en.setProjDesc(proj.getProjDesc());
            en.setProjAmt(proj.getProjAmt());
            en.setProjFreezeAmt(proj.getProjFreezeAmt());
            en.setProjUsedAmt(proj.getProjUsedAmt());
            en.setProjBalance(proj.getProjBalance());
            en.setCtrlLevel(proj.getCtrlLevel());
            en.setProjCategory(proj.getProjCategory());
            en.setDeliveryCenter(1);
        }else if(type == 2){

            en = new ProjEntity();
            Proj2Entity proj = proj2Dao.getProj2BudgetByID(id,requestID);
            en.setId(proj.getId());
            en.setPID(proj.getPID());
            en.setProjNo(proj.getProjNo());
            en.setProjDeptNo(proj.getProjDeptNo());
            en.setProjName(proj.getProjName());
            en.setProjDesc(proj.getProjDesc());
            en.setProjAmt(proj.getProjAmt());
            en.setProjFreezeAmt(proj.getProjFreezeAmt());
            en.setProjUsedAmt(proj.getProjUsedAmt());
            en.setProjBalance(proj.getProjBalance());
            en.setCtrlLevel(proj.getCtrlLevel());

            en.setDeliveryCenter(proj.getDeliveryCenter());

            int proj1ID = ProjUtil.getProj1ID4Child(id);
            Proj1Entity proj1 = proj1Dao.getProj1BudgetByID(proj1ID, requestID);
            en.setParentBalance(proj1.getProjBalance());
            //如果控制级别为父项，余额改为父项的余额
            if(CtrlLevelEnum.PARENT.compareTo(proj.getCtrlLevel()) == 0){
                en.setProjBalance(proj1.getProjBalance());
            }

        }else if(type == 3){
            en = new ProjEntity();
            Proj3Entity proj = proj3Dao.getProj3BudgetByID(id,requestID);
            en.setId(proj.getId());
            en.setPID(proj.getPID());
            en.setProjNo(proj.getProjNo());
            en.setProjDeptNo(proj.getProjDeptNo());
            en.setProjName(proj.getProjName());
            en.setProjDesc(proj.getProjDesc());
            en.setProjAmt(proj.getProjAmt());
            en.setProjFreezeAmt(proj.getProjFreezeAmt());
            en.setProjUsedAmt(proj.getProjUsedAmt());
            en.setProjBalance(proj.getProjBalance());
            en.setCtrlLevel(proj.getCtrlLevel());

            int proj1ID = ProjUtil.getProj1ID4Child(id);
            Proj1Entity proj1 = proj1Dao.getProj1BudgetByID(proj1ID, requestID);
            en.setParentBalance(proj1.getProjBalance());
            //如果控制级别为父项，余额改为父项的余额
            if(CtrlLevelEnum.PARENT.compareTo(proj.getCtrlLevel()) == 0){
                en.setProjBalance(proj1.getProjBalance());
            }
            en.setDeliveryCenter(1);
        }
        if(en != null){
            en.setProjType(type);
        }
        return en;
    }


    public BigDecimal add(double a,double b){
        BigDecimal aa = new BigDecimal(String.valueOf(a));
        BigDecimal bb = new BigDecimal(String.valueOf(b));
        return aa.add(bb);
    }

}
