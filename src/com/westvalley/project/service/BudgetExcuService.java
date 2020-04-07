package com.westvalley.project.service;

import com.westvalley.project.dao.BudgetExcuDao;
import com.westvalley.project.dto.*;
import com.westvalley.project.entity.ProjEntity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.CtrlLevelEnum;
import com.westvalley.project.enums.ExpTypeEnum;
import com.westvalley.project.enums.ProjtypeEnum;
import com.westvalley.project.po.BudgetExcuPo;
import com.westvalley.project.util.EnumsUtil;
import com.westvalley.project.util.ProjUtil;
import com.westvalley.util.LogUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSet;
import weaver.general.TimeUtil;
import weaver.general.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算结算体系  所有的预算结算都走这个方法
 */
public class BudgetExcuService {

    private LogUtil log = LogUtil.getLogger(getClass());

    private BudgetExcuDao budgetExcuDao;
    public BudgetExcuService(){
        budgetExcuDao = new BudgetExcuDao();
    }

    /**
     * 执行预算
     * @param excuPoList 执行预算数据
     * @return 执行结果
     */
    @Deprecated
    public synchronized ResultDto excuBudget(List<BudgetExcuPo> excuPoList) {
        return excuBudget(excuPoList,true);
    }

    public synchronized ResultDto excuBudget(List<BudgetExcuPo> excuPoList,boolean isDelete) {
        return budgetExcuDao.excuBudget(excuPoList,isDelete);
    }


    /**
     * 追加预算
     * @param appendDtoList 追加预算的数据
     * @param budTypeEnum FREZEE代表申请节点提交  APPEND 代表归档
     * @return
     */
    public List<BudgetExcuPo> getData4Append(List<AppendDto> appendDtoList, BudTypeEnum budTypeEnum){
        log.d("追加预算参数",appendDtoList,budTypeEnum);
        if(BudTypeEnum.FREZEE.compareTo(budTypeEnum) != 0 && BudTypeEnum.APPEND.compareTo(budTypeEnum) != 0){
            throw new IllegalArgumentException("预算类型不正确");
        }
        List<BudgetExcuPo> excuPoList = new ArrayList<>();
        String createDate = TimeUtil.getCurrentDateString();
        String createTime = TimeUtil.getOnlyCurrentTimeString();


        for (AppendDto dto : appendDtoList) {
            String remark1 = "";
            if(BudTypeEnum.FREZEE.compareTo(budTypeEnum) == 0){
                remark1 = "预算冻结-向下级追加预算";
            }else if(BudTypeEnum.APPEND.compareTo(budTypeEnum) == 0){
                remark1 = "预算扣减-向下级追加预算";
            }
            {
                //不管是提交还是归档  被追加的项目预算都是要减少的
                BudgetExcuPo excuPo = new BudgetExcuPo();
                excuPo.setRequestID(dto.getRequestID());
                excuPo.setDetailID(dto.getDetailID());
                excuPo.setProjID(dto.getPID());//被追加的项目
                excuPo.setProjNo("");
                excuPo.setProjDeptNo("");
                excuPo.setProjtype(dto.getProjType());
                excuPo.setUseType(budTypeEnum);
                excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型为减少
//            excuPo.setSplitStatu();
                excuPo.setUseAmt(dto.getUseAmt());
                excuPo.setCreDate(createDate);
                excuPo.setCreTime(createTime);
                excuPo.setCreUser(dto.getCreUser());
                excuPo.setRemark(remark1);
                excuPoList.add(excuPo);
            }
            //追加归档时 项目预算增加
            if(BudTypeEnum.APPEND.compareTo(budTypeEnum) == 0){
                ProjtypeEnum projType = dto.getProjType();
                ProjtypeEnum projtypeTemp = null;
                switch (projType){
                    case ORG:
                        projtypeTemp = ProjtypeEnum.PARENT;break;
                    case PARENT:
                        projtypeTemp = ProjtypeEnum.SON;break;
                    case SON:
                        projtypeTemp = ProjtypeEnum.GRANDSON;break;
                }
                //项目预算增加
                BudgetExcuPo excuPo = new BudgetExcuPo();
                excuPo.setRequestID(dto.getRequestID());
                excuPo.setDetailID(dto.getDetailID());
                excuPo.setProjID(dto.getProjID());//追加的项目
                excuPo.setProjNo("");
                excuPo.setProjDeptNo("");
                excuPo.setProjtype(projtypeTemp);
                excuPo.setUseType(budTypeEnum);
                excuPo.setExpType(ExpTypeEnum.INCREASE);//费用类型为增加
//            excuPo.setSplitStatu();
                excuPo.setUseAmt(-dto.getUseAmt());//金额为负
                excuPo.setCreDate(createDate);
                excuPo.setCreTime(createTime);
                excuPo.setCreUser(dto.getCreUser());
                excuPo.setRemark(remark1);
                excuPoList.add(excuPo);
            }
        }

        return excuPoList;
    }


    /**
     * 合同付款/非合同付款
     * @param contractDtoList
     * @param budTypeEnum CONTRACT_FREZEE代表申请节点提交  CONTRACT_USED 代表归档
     * @param isContractPay 是否合同付款
     * @return
     */
    public List<BudgetExcuPo> getData4Contract(List<ContractDto> contractDtoList,BudTypeEnum budTypeEnum,boolean isContractPay){
        log.d("非合同付款参数",contractDtoList,budTypeEnum);
        if(BudTypeEnum.CONTRACT_FREZEE.compareTo(budTypeEnum) != 0 && BudTypeEnum.CONTRACT_USED.compareTo(budTypeEnum) != 0){
            throw new IllegalArgumentException("预算类型不正确");
        }
        List<BudgetExcuPo> excuPoList = new ArrayList<>();
        String createDate = TimeUtil.getCurrentDateString();
        String createTime = TimeUtil.getOnlyCurrentTimeString();

        RecordSet rs = new RecordSet();
        for (ContractDto dto : contractDtoList) {
            String remark1 = "";
            if(BudTypeEnum.CONTRACT_FREZEE.compareTo(budTypeEnum) == 0){
                if(isContractPay){
                    remark1 = "预算冻结-合同付款";
                }else{
                    remark1 = "预算冻结-非合同付款";
                }
            }else if(BudTypeEnum.CONTRACT_USED.compareTo(budTypeEnum) == 0){
                if(isContractPay){
                    remark1 = "预算使用-合同付款";
                }else{
                    remark1 = "预算使用-非合同付款";
                }
            }

            CtrlLevelEnum ctrlLevel = dto.getCtrlLevel();
            ExpTypeEnum expType;
            ProjtypeEnum projtype = EnumsUtil.getProjtypeEnum(dto.getProjType());

            if(isContractPay){
                //合同付款需要减少原来冻结的金额  及预算增加
                if(CtrlLevelEnum.PARENT.compareTo(ctrlLevel) == 0){
                    expType = ExpTypeEnum.NOCALC;
                }else{
                    expType = ExpTypeEnum.INCREASE;
                }
                {
                    //当前项目
                    BudgetExcuPo excuPo = new BudgetExcuPo();
                    excuPo.setRequestID(dto.getRequestID());
                    excuPo.setDetailID(dto.getDetailID());
                    excuPo.setProjID(dto.getProjID());//付款的项目
                    excuPo.setProjNo("");
                    excuPo.setProjDeptNo("");
                    excuPo.setProjtype(projtype);
                    excuPo.setUseType(budTypeEnum);
                    excuPo.setExpType(expType);//费用类型
//            excuPo.setSplitStatu();
                    excuPo.setUseAmt(-dto.getUseAmt());//金额为负
                    excuPo.setCreDate(createDate);
                    excuPo.setCreTime(createTime);
                    excuPo.setCreUser(dto.getCreUser());
                    excuPo.setRemark(remark1);
                    excuPoList.add(excuPo);
                }
                //合同冻结的金额改为使用
                //合同付款  控制级别为父项  需要改变父项的执行数据
                if(CtrlLevelEnum.PARENT.compareTo(ctrlLevel) == 0){
                    int projID = ProjUtil.getProj1ID4Child(dto.getProjID());

                    BudgetExcuPo excuPo = new BudgetExcuPo();
                    excuPo.setRequestID(dto.getRequestID());
                    excuPo.setDetailID(dto.getDetailID());
                    excuPo.setProjID(projID);//付款的项目 父项
                    excuPo.setProjNo("");
                    excuPo.setProjDeptNo("");
                    excuPo.setProjtype(ProjtypeEnum.PARENT);
                    excuPo.setUseType(budTypeEnum);
                    excuPo.setExpType(ExpTypeEnum.INCREASE);//费用类型 为增加
//            excuPo.setSplitStatu();
                    excuPo.setUseAmt(dto.getUseAmt());//金额为负
                    excuPo.setCreDate(createDate);
                    excuPo.setCreTime(createTime);
                    excuPo.setCreUser(dto.getCreUser());
                    excuPo.setRemark(remark1);
                    excuPoList.add(excuPo);
                }
            }

            //使用预算
            {
                if(CtrlLevelEnum.PARENT.compareTo(ctrlLevel) == 0){
                    expType = ExpTypeEnum.NOCALC;
                }else{
                    expType = ExpTypeEnum.DECREASE;
                }
                //当前项目
                BudgetExcuPo excuPo = new BudgetExcuPo();
                excuPo.setRequestID(dto.getRequestID());
                excuPo.setDetailID(dto.getDetailID());
                excuPo.setProjID(dto.getProjID());//付款的项目
                excuPo.setProjNo("");
                excuPo.setProjDeptNo("");
                excuPo.setProjtype(projtype);
                excuPo.setUseType(budTypeEnum);
                excuPo.setExpType(expType);//费用类型
//            excuPo.setSplitStatu();
                excuPo.setUseAmt(dto.getUseAmt());
                excuPo.setCreDate(createDate);
                excuPo.setCreTime(createTime);
                excuPo.setCreUser(dto.getCreUser());
                excuPo.setRemark(remark1);
                excuPoList.add(excuPo);
            }
            //控制级别为父项  需要增加父项的执行数据
            if(CtrlLevelEnum.PARENT.compareTo(ctrlLevel) == 0){
                int projID = ProjUtil.getProj1ID4Child(dto.getProjID());

                BudgetExcuPo excuPo = new BudgetExcuPo();
                excuPo.setRequestID(dto.getRequestID());
                excuPo.setDetailID(dto.getDetailID());
                excuPo.setProjID(projID);//付款的项目 父项
                excuPo.setProjNo("");
                excuPo.setProjDeptNo("");
                excuPo.setProjtype(ProjtypeEnum.PARENT);
                excuPo.setUseType(budTypeEnum);
                excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型
//            excuPo.setSplitStatu();
                excuPo.setUseAmt(dto.getUseAmt());
                excuPo.setCreDate(createDate);
                excuPo.setCreTime(createTime);
                excuPo.setCreUser(dto.getCreUser());
                excuPo.setRemark(remark1);
                excuPoList.add(excuPo);
            }

        }
        return excuPoList;
    }


    /**
     * 项目关闭释放预算
     * @param releaseDtoList
     * @param budTypeEnum FREZEE代表申请节点提交  RELEASE 代表归档
     * @return
     */
    public List<BudgetExcuPo> getData4Release(List<ReleaseDto> releaseDtoList, BudTypeEnum budTypeEnum){
        log.d("项目关闭参数",releaseDtoList,budTypeEnum);
        if(BudTypeEnum.FREZEE.compareTo(budTypeEnum) != 0 && BudTypeEnum.RELEASE.compareTo(budTypeEnum) != 0){
            throw new IllegalArgumentException("预算类型不正确");
        }
        List<BudgetExcuPo> excuPoList = new ArrayList<>();
        String createDate = TimeUtil.getCurrentDateString();
        String createTime = TimeUtil.getOnlyCurrentTimeString();

        RecordSet rs1 = new RecordSet();
        RecordSet rs2 = new RecordSet();
        RecordSet rs3 = new RecordSet();
        BudgetService budgetService = new BudgetService();
        for (ReleaseDto dto : releaseDtoList) {
            String remark1 = "";
            List<Budget23Dto> budget23DtoList = dto.getBudget23DtoList();
            boolean isDeliveryCenter = budget23DtoList.size() > 0;//是否交付中心

            if(BudTypeEnum.FREZEE.compareTo(budTypeEnum) == 0){
                //把所有的可用预算冻结
                remark1 = "预算冻结-项目关闭";

                BigDecimal parentTotal = BigDecimal.ZERO;//需要扣除的父项预算
                if(!ProjUtil.isParentCtrlLevel(dto.getProjID())){
                    //不是父项控制 则需要添加所有的下级项目

                    //如果是交付中心，则需要扣除已使用金额
                    Map<String,Double> amtMap = new HashMap<>();//总金额，用于扣减
                    if(isDeliveryCenter){
                        for (Budget23Dto budget23Dto : budget23DtoList) {
                            String key = String.valueOf(budget23Dto.getProjID());
                            double useAmt = budget23Dto.getUseAmt();
                            amtMap.put(key,useAmt);

                            //扣减项目金额
                            BudgetExcuPo excuPo = new BudgetExcuPo();
                            excuPo.setRequestID(budget23Dto.getRequestID());
                            excuPo.setDetailID(budget23Dto.getDetailID());
                            excuPo.setProjID(budget23Dto.getProjID());
                            excuPo.setProjNo("");
                            excuPo.setProjDeptNo("");
                            excuPo.setProjtype(budget23Dto.getProjtype());
                            excuPo.setUseType(budTypeEnum);
                            excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型
                            excuPo.setUseAmt(useAmt);
                            excuPo.setCreDate(createDate);
                            excuPo.setCreTime(createTime);
                            excuPo.setCreUser(dto.getCreUser());
                            excuPo.setRemark(remark1+"-交付中心");
                            excuPoList.add(excuPo);
                        }
                    }
                    List<Integer> chilrenProjIDs = ProjUtil.getChilrenProjIDs(dto.getProjID());
                    for (Integer projID : chilrenProjIDs) {
                        ProjEntity projInfo = budgetService.getProjInfo(projID, dto.getRequestID());

                        BudgetExcuPo excuPo = new BudgetExcuPo();
                        excuPo.setRequestID(dto.getRequestID());
                        excuPo.setDetailID(dto.getDetailID());
                        excuPo.setProjID(projID);
                        excuPo.setProjNo("");
                        excuPo.setProjDeptNo("");
                        excuPo.setProjtype(EnumsUtil.getProjtypeEnum(projInfo.getProjType()));
                        excuPo.setUseType(budTypeEnum);
                        excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型
//            excuPo.setSplitStatu();
                        Double aDouble = amtMap.get(String.valueOf(projID));
                        if(aDouble == null){
                            aDouble = 0.00;
                        }
                        excuPo.setUseAmt(add(projInfo.getProjBalance(),-aDouble).doubleValue());
                        excuPo.setCreDate(createDate);
                        excuPo.setCreTime(createTime);
                        excuPo.setCreUser(dto.getCreUser());
                        excuPo.setRemark(remark1);
                        excuPoList.add(excuPo);
                    }
                }else{
                    //如果交付中心，则扣除使用父项预算
                    for (Budget23Dto budget23Dto : budget23DtoList) {
                        double useAmt = budget23Dto.getUseAmt();
                        parentTotal = parentTotal.add(new BigDecimal(String.valueOf(useAmt)));
                    }
                    if(parentTotal.doubleValue() > 0){
                        BudgetExcuPo excuPo = new BudgetExcuPo();
                        excuPo.setRequestID(dto.getRequestID());
                        excuPo.setDetailID(dto.getDetailID());
                        excuPo.setProjID(dto.getProjID());
                        excuPo.setProjNo("");
                        excuPo.setProjDeptNo("");
                        excuPo.setProjtype(EnumsUtil.getProjtypeEnum(dto.getProjType()));
                        excuPo.setUseType(budTypeEnum);
                        excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型
//            excuPo.setSplitStatu();
                        excuPo.setUseAmt(parentTotal.doubleValue());
                        excuPo.setCreDate(createDate);
                        excuPo.setCreTime(createTime);
                        excuPo.setCreUser(dto.getCreUser());
                        excuPo.setRemark(remark1+"-交付中心");
                        excuPoList.add(excuPo);
                    }
                }
                //冻结父项
                ProjEntity projInfo = budgetService.getProjInfo(dto.getProjID(), dto.getRequestID());
                BudgetExcuPo excuPo = new BudgetExcuPo();
                excuPo.setRequestID(dto.getRequestID());
                excuPo.setDetailID(dto.getDetailID());
                excuPo.setProjID(dto.getProjID());
                excuPo.setProjNo("");
                excuPo.setProjDeptNo("");
                excuPo.setProjtype(EnumsUtil.getProjtypeEnum(dto.getProjType()));
                excuPo.setUseType(budTypeEnum);
                excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型
//            excuPo.setSplitStatu();
                excuPo.setUseAmt(add(projInfo.getProjBalance(),-parentTotal.doubleValue()).doubleValue());
                excuPo.setCreDate(createDate);
                excuPo.setCreTime(createTime);
                excuPo.setCreUser(dto.getCreUser());
                excuPo.setRemark(remark1);
                excuPoList.add(excuPo);
            }else if(BudTypeEnum.RELEASE.compareTo(budTypeEnum) == 0){
                remark1 = "预算释放-项目关闭";
                //释放预算数据要从下而上释放

//                double proj0ReleaseAmt = 0;//祖项要增加的金额
                double proj1ReleaseAmt = 0;//父项释放金额
                BigDecimal parentTotal = BigDecimal.ZERO;//需要扣除的父项预算
                if(!ProjUtil.isParentCtrlLevel(dto.getProjID())){

                    Map<String,Double> amtMap = new HashMap<>();//总金额，用于扣减
                    if(isDeliveryCenter) {
                        for (Budget23Dto budget23Dto : budget23DtoList) {
                            String key = String.valueOf(budget23Dto.getProjID());
                            double useAmt = budget23Dto.getUseAmt();
                            amtMap.put(key, useAmt);
                            //扣减项目金额
                            BudgetExcuPo excuPo = new BudgetExcuPo();
                            excuPo.setRequestID(budget23Dto.getRequestID());
                            excuPo.setDetailID(budget23Dto.getDetailID());
                            excuPo.setProjID(budget23Dto.getProjID());
                            excuPo.setProjNo("");
                            excuPo.setProjDeptNo("");
                            excuPo.setProjtype(budget23Dto.getProjtype());
                            excuPo.setUseType(BudTypeEnum.USED);//改为使用
                            excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型
                            excuPo.setUseAmt(useAmt);
                            excuPo.setCreDate(createDate);
                            excuPo.setCreTime(createTime);
                            excuPo.setCreUser(dto.getCreUser());
                            excuPo.setRemark("预算使用-项目关闭-交付中心");
                            excuPoList.add(excuPo);
                        }

                    }
                    rs1.executeQuery("select id from uf_proj where pid = ? ",dto.getProjID());
                    double proj2ReleaseAmt = 0;//子向合计
                    while(rs1.next()){
                        //查询出子项
                        int proj2ID = rs1.getInt(1);
                        double proj3ReleaseAmt = 0;//孙子向合计
                        rs2.executeQuery("select id from uf_proj where pid = ? ",proj2ID);
                        while(rs2.next()){
                            //查出孙子项
                            int proj3ID = rs2.getInt(1);
                            ProjEntity projInfo = budgetService.getProjInfo(proj3ID, dto.getRequestID());

                            //扣除交付中心使用金额
                            double projBalance = projInfo.getProjBalance();
                            Double aDouble = amtMap.get(String.valueOf(proj3ID));
                            if(aDouble == null){
                                aDouble = 0.00;
                            }
                            projBalance = add(projBalance,-aDouble).doubleValue();

                            //孙子项合计剩余金额
                            proj3ReleaseAmt = add(proj3ReleaseAmt, projBalance).doubleValue();

                            //释放孙子项
                            BudgetExcuPo excuPo = new BudgetExcuPo();
                            excuPo.setRequestID(dto.getRequestID());
                            excuPo.setDetailID(dto.getDetailID());
                            excuPo.setProjID(proj3ID);
                            excuPo.setProjNo("");
                            excuPo.setProjDeptNo("");
                            excuPo.setProjtype(EnumsUtil.getProjtypeEnum(projInfo.getProjType()));
                            excuPo.setUseType(budTypeEnum);
                            excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型
//            excuPo.setSplitStatu();
                            excuPo.setUseAmt(projBalance);
                            excuPo.setCreDate(createDate);
                            excuPo.setCreTime(createTime);
                            excuPo.setCreUser(dto.getCreUser());
                            excuPo.setRemark(remark1);
                            excuPoList.add(excuPo);
                        }


                        //释放子项
                        ProjEntity projInfo = budgetService.getProjInfo(proj2ID, dto.getRequestID());


                        //扣除交付中心使用金额
                        double projBalance = projInfo.getProjBalance();
                        Double aDouble = amtMap.get(String.valueOf(proj2ID));
                        if(aDouble == null){
                            aDouble = 0.00;
                        }

                        projBalance = add(projBalance,-aDouble).doubleValue();

                        //子项剩余金额 + 孙子项合计剩余金额
                        proj2ReleaseAmt = add(projBalance,proj3ReleaseAmt).doubleValue();

                        proj1ReleaseAmt = add(proj1ReleaseAmt,proj2ReleaseAmt).doubleValue();

//                        log.d("proj2ReleaseAmt  proj3ReleaseAmt -- ",projBalance,proj3ReleaseAmt);

                        BudgetExcuPo excuPo = new BudgetExcuPo();
                        excuPo.setRequestID(dto.getRequestID());
                        excuPo.setDetailID(dto.getDetailID());
                        excuPo.setProjID(proj2ID);
                        excuPo.setProjNo("");
                        excuPo.setProjDeptNo("");
                        excuPo.setProjtype(EnumsUtil.getProjtypeEnum(projInfo.getProjType()));
                        excuPo.setUseType(budTypeEnum);
                        excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型
//            excuPo.setSplitStatu();
                        excuPo.setUseAmt(proj2ReleaseAmt);//子项剩余金额 + 孙子项合计剩余金额
                        excuPo.setCreDate(createDate);
                        excuPo.setCreTime(createTime);
                        excuPo.setCreUser(dto.getCreUser());
                        excuPo.setRemark(remark1);
                        excuPoList.add(excuPo);

                    }

                }else{
                    /**父项控制*/
                    //如果交付中心，则扣除使用父项预算
                    for (Budget23Dto budget23Dto : budget23DtoList) {
                        double useAmt = budget23Dto.getUseAmt();
                        parentTotal = parentTotal.add(new BigDecimal(String.valueOf(useAmt)));
                    }
                    if(parentTotal.doubleValue() > 0){
                        BudgetExcuPo excuPo = new BudgetExcuPo();
                        excuPo.setRequestID(dto.getRequestID());
                        excuPo.setDetailID(dto.getDetailID());
                        excuPo.setProjID(dto.getProjID());
                        excuPo.setProjNo("");
                        excuPo.setProjDeptNo("");
                        excuPo.setProjtype(EnumsUtil.getProjtypeEnum(dto.getProjType()));
                        excuPo.setUseType(BudTypeEnum.USED);
                        excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型
//            excuPo.setSplitStatu();
                        excuPo.setUseAmt(parentTotal.doubleValue());
                        excuPo.setCreDate(createDate);
                        excuPo.setCreTime(createTime);
                        excuPo.setCreUser(dto.getCreUser());
                        excuPo.setRemark("预算使用-项目关闭-交付中心");
                        excuPoList.add(excuPo);
                    }
                }
//                log.d("proj1ReleaseAmt  1 -- ",proj1ReleaseAmt);
                {
                    //释放父项
                    ProjEntity projInfo = budgetService.getProjInfo(dto.getProjID(), dto.getRequestID());

                    //祖项合计  父项剩余金额 + 子项合计剩余金额
                    proj1ReleaseAmt = add(projInfo.getProjBalance(), proj1ReleaseAmt).doubleValue();
                    //扣除交付中心使用预算
                    proj1ReleaseAmt = add(proj1ReleaseAmt,-parentTotal.doubleValue()).doubleValue();
                    BudgetExcuPo excuPo = new BudgetExcuPo();
                    excuPo.setRequestID(dto.getRequestID());
                    excuPo.setDetailID(dto.getDetailID());
                    excuPo.setProjID(dto.getProjID());
                    excuPo.setProjNo("");
                    excuPo.setProjDeptNo("");
                    excuPo.setProjtype(EnumsUtil.getProjtypeEnum(projInfo.getProjType()));
                    excuPo.setUseType(budTypeEnum);
                    excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型
//            excuPo.setSplitStatu();
                    excuPo.setUseAmt(proj1ReleaseAmt);//父项剩余金额 + 子项合计剩余金额
                    excuPo.setCreDate(createDate);
                    excuPo.setCreTime(createTime);
                    excuPo.setCreUser(dto.getCreUser());
                    excuPo.setRemark(remark1);
                    excuPoList.add(excuPo);
                }
                //增加祖项
                //涉及分摊
                rs3.executeQuery("select * from wv_v_proj1more where projid = ? ",dto.getProjID());
                int counts = rs3.getCounts();
                BigDecimal total = new BigDecimal(String.valueOf(proj1ReleaseAmt));
                BigDecimal temp = BigDecimal.ZERO;
                BigDecimal one = new BigDecimal(100);
                while(rs3.next()){
                    counts--;
                    int proj0ID = rs3.getInt("ftbm");
                    double amt;
                    if(counts == 0){
                        //最后一条
                        //调整拨出
                        amt = total.subtract(temp).doubleValue();
                    }else{
                        BigDecimal ftbl = new BigDecimal(StringUtil.toNum(rs3.getString("ftbl"))).divide(one,2,BigDecimal.ROUND_HALF_UP);
                        BigDecimal ftblAmt = total.multiply(ftbl).setScale(2, BigDecimal.ROUND_HALF_UP);
                        temp = temp.add(ftblAmt);
                        amt = ftblAmt.doubleValue();
                    }
                    //预算增加 金额为负数
                    BudgetExcuPo excuPo = new BudgetExcuPo();
                    excuPo.setRequestID(dto.getRequestID());
                    excuPo.setDetailID(dto.getDetailID());
                    excuPo.setProjID(proj0ID);
                    excuPo.setProjNo("");
                    excuPo.setProjDeptNo("");
                    excuPo.setProjtype(ProjtypeEnum.ORG);
                    excuPo.setUseType(budTypeEnum);
                    excuPo.setExpType(ExpTypeEnum.INCREASE);//费用类型为增加
//            excuPo.setSplitStatu();
                    excuPo.setUseAmt(-amt);//金额为负
                    excuPo.setCreDate(createDate);
                    excuPo.setCreTime(createTime);
                    excuPo.setCreUser(dto.getCreUser());
                    excuPo.setRemark(remark1);
                    excuPoList.add(excuPo);
                }

            }
        }
        return excuPoList;
    }


    /**
     * 父项项目拆解
     * @param budget1DtoList
     * @param budTypeEnum SPLIT_FREZEE 代表申请节点提交  SPLIT_USED 代表归档
     * @return
     */
    public List<BudgetExcuPo> getData4Split(List<Budget1Dto> budget1DtoList,BudTypeEnum budTypeEnum){
        log.d("父项项目拆解参数",budget1DtoList,budTypeEnum);
        if(BudTypeEnum.SPLIT_FREZEE.compareTo(budTypeEnum) != 0 && BudTypeEnum.SPLIT_USED.compareTo(budTypeEnum) != 0){
            throw new IllegalArgumentException("预算类型不正确");
        }
        List<BudgetExcuPo> excuPoList = new ArrayList<>();
        String createDate = TimeUtil.getCurrentDateString();
        String createTime = TimeUtil.getOnlyCurrentTimeString();


        for (Budget1Dto dto : budget1DtoList) {
            String remark1 = "";
            if(BudTypeEnum.SPLIT_FREZEE.compareTo(budTypeEnum) == 0){
                remark1 = "拆解预算-冻结";
            }else if(BudTypeEnum.SPLIT_USED.compareTo(budTypeEnum) == 0){
                remark1 = "拆解预算-使用";
            }
            BudgetExcuPo excuPo = new BudgetExcuPo();
            excuPo.setRequestID(dto.getRequestID());
            excuPo.setDetailID(dto.getDetailID());
            excuPo.setProjID(dto.getProjID());
            excuPo.setProjNo("");
            excuPo.setProjDeptNo("");
            excuPo.setProjtype(ProjtypeEnum.PARENT);
            excuPo.setUseType(budTypeEnum);
            excuPo.setExpType(ExpTypeEnum.NOCALC);//费用类型为不计算
            excuPo.setSplitStatu(dto.getSplit());
            excuPo.setUseAmt(dto.getUseAmt());
            excuPo.setCreDate(createDate);
            excuPo.setCreTime(createTime);
            excuPo.setCreUser(dto.getCreUser());
            excuPo.setRemark(remark1);
            excuPo.setAutoCre(dto.getAutoCre());
            excuPoList.add(excuPo);
        }

        return excuPoList;
    }


    /**
     * 项目预算调拨
     * @param transferDtoList
     * @param budTypeEnum FREZEE 代表申请节点提交  TRANSFER 代表归档
     * @return
     */
    public List<BudgetExcuPo> getData4Transfer(List<TransferDto> transferDtoList,BudTypeEnum budTypeEnum){
        log.d("调拨预算参数",transferDtoList,budTypeEnum);
        if(BudTypeEnum.FREZEE.compareTo(budTypeEnum) != 0 && BudTypeEnum.TRANSFER.compareTo(budTypeEnum) != 0){
            throw new IllegalArgumentException("预算类型不正确");
        }
        List<BudgetExcuPo> excuPoList = new ArrayList<>();
        String createDate = TimeUtil.getCurrentDateString();
        String createTime = TimeUtil.getOnlyCurrentTimeString();

        for (TransferDto dto : transferDtoList) {
            String remark1 = "";
            if(BudTypeEnum.FREZEE.compareTo(budTypeEnum) == 0){
                remark1 = "预算冻结-在途冻结";
            }else if(BudTypeEnum.TRANSFER.compareTo(budTypeEnum) == 0){
                remark1 = "预算调拨-归档生效";
            }
            if(dto.getOutID() <= 0){
                //年度预算调整
                double useAmt = dto.getUseAmt();
                if(useAmt < 0){
                    //预算调减 冻结预算
                    BudgetExcuPo excuPo = new BudgetExcuPo();
                    excuPo.setRequestID(dto.getRequestID());
                    excuPo.setDetailID(dto.getDetailID());
                    excuPo.setProjID(dto.getInID());
                    excuPo.setProjNo("");
                    excuPo.setProjDeptNo("");
                    excuPo.setProjtype(ProjtypeEnum.ORG);
                    excuPo.setUseType(budTypeEnum);
                    excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型为减少
//            excuPo.setSplitStatu();
                    excuPo.setUseAmt(-dto.getUseAmt()); //用户填入的金额为负数，所以要转换一下
                    excuPo.setCreDate(createDate);
                    excuPo.setCreTime(createTime);
                    excuPo.setCreUser(dto.getCreUser());
                    excuPo.setRemark(remark1);
                    excuPoList.add(excuPo);
                }
                if(BudTypeEnum.TRANSFER.compareTo(budTypeEnum) == 0){
                    BudgetExcuPo excuPo = new BudgetExcuPo();
                    excuPo.setRequestID(dto.getRequestID());
                    excuPo.setDetailID(dto.getDetailID());
                    excuPo.setProjID(dto.getInID());
                    excuPo.setProjNo("");
                    excuPo.setProjDeptNo("");
                    excuPo.setProjtype(ProjtypeEnum.ORG);
                    excuPo.setUseType(budTypeEnum);
                    if(useAmt < 0){
                        //调减
                        excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型为减少
                    }else{
                        //调增
                        excuPo.setExpType(ExpTypeEnum.INCREASE);//费用类型为增加
                    }
                    excuPo.setUseAmt(-dto.getUseAmt()); //用户填入的金额为负数，所以要转换一下
                    excuPo.setCreDate(createDate);
                    excuPo.setCreTime(createTime);
                    excuPo.setCreUser(dto.getCreUser());
                    excuPo.setRemark(remark1);
                    excuPoList.add(excuPo);
                }
            }else{
                ProjtypeEnum projtype = EnumsUtil.getProjtypeEnum(dto.getProjType());
                {
                    //调出的减少预算
                    BudgetExcuPo excuPo = new BudgetExcuPo();
                    excuPo.setRequestID(dto.getRequestID());
                    excuPo.setDetailID(dto.getDetailID());
                    excuPo.setProjID(dto.getOutID());
                    excuPo.setProjNo("");
                    excuPo.setProjDeptNo("");
                    excuPo.setProjtype(projtype);
                    excuPo.setUseType(budTypeEnum);
                    excuPo.setExpType(ExpTypeEnum.DECREASE);//费用类型为减少
//            excuPo.setSplitStatu();
                    excuPo.setUseAmt(dto.getUseAmt());
                    excuPo.setCreDate(createDate);
                    excuPo.setCreTime(createTime);
                    excuPo.setCreUser(dto.getCreUser());
                    excuPo.setRemark(remark1);
                    excuPoList.add(excuPo);
                }
                if(BudTypeEnum.TRANSFER.compareTo(budTypeEnum) == 0){
                    //调入的增加预算
                    BudgetExcuPo excuPo = new BudgetExcuPo();
                    excuPo.setRequestID(dto.getRequestID());
                    excuPo.setDetailID(dto.getDetailID());
                    excuPo.setProjID(dto.getInID());
                    excuPo.setProjNo("");
                    excuPo.setProjDeptNo("");
                    excuPo.setProjtype(projtype);
                    excuPo.setUseType(budTypeEnum);
                    excuPo.setExpType(ExpTypeEnum.INCREASE);
//            excuPo.setSplitStatu();
                    excuPo.setUseAmt(-dto.getUseAmt()); //增加预算  金额为负
                    excuPo.setCreDate(createDate);
                    excuPo.setCreTime(createTime);
                    excuPo.setCreUser(dto.getCreUser());
                    excuPo.setRemark(remark1);
                    excuPoList.add(excuPo);
                }
            }
        }

        return excuPoList;
    }

    /**
     * 祖项项目预算使用
     * @param budget0DtoList
     * @param budTypeEnum FREZEE 代表申请节点提交  USED 代表归档
     * @return
     */
    public List<BudgetExcuPo> getData4Proj0(List<Budget0Dto> budget0DtoList,BudTypeEnum budTypeEnum){
        log.d("祖项预算使用参数",budget0DtoList,budTypeEnum);
        if(BudTypeEnum.FREZEE.compareTo(budTypeEnum) != 0 && BudTypeEnum.USED.compareTo(budTypeEnum) != 0){
            throw new IllegalArgumentException("预算类型不正确");
        }
        List<BudgetExcuPo> excuPoList = new ArrayList<>();
        String createDate = TimeUtil.getCurrentDateString();
        String createTime = TimeUtil.getOnlyCurrentTimeString();

        for (Budget0Dto dto : budget0DtoList) {
            String remark1 = "";
            if(BudTypeEnum.FREZEE.compareTo(budTypeEnum) == 0){
                remark1 = "预算冻结-在途冻结";
            }else if(BudTypeEnum.USED.compareTo(budTypeEnum) == 0){
                remark1 = "预算使用-归档生效";
            }
            BudgetExcuPo excuPo = new BudgetExcuPo();
            excuPo.setRequestID(dto.getRequestID());
            excuPo.setDetailID(dto.getDetailID());
            excuPo.setProjID(dto.getProjID());
            excuPo.setProjNo("");
            excuPo.setProjDeptNo("");
            excuPo.setProjtype(ProjtypeEnum.ORG);
            excuPo.setUseType(budTypeEnum);
            excuPo.setExpType(ExpTypeEnum.DECREASE);
//            excuPo.setSplitStatu();
            excuPo.setUseAmt(dto.getUseAmt());
            excuPo.setCreDate(createDate);
            excuPo.setCreTime(createTime);
            excuPo.setCreUser(dto.getCreUser());
            excuPo.setRemark(remark1);
            excuPoList.add(excuPo);
        }
        return excuPoList;
    }
    /**
     * 父项项目预算使用
     * @param budget1DtoList
     * @param budTypeEnum FREZEE 代表申请节点提交  USED 代表归档
     * @return
     */
    public List<BudgetExcuPo> getData4Proj1(List<Budget1Dto> budget1DtoList,BudTypeEnum budTypeEnum){
        log.d("父项预算使用参数",budget1DtoList,budTypeEnum);
        if(BudTypeEnum.FREZEE.compareTo(budTypeEnum) != 0 && BudTypeEnum.USED.compareTo(budTypeEnum) != 0){
            throw new IllegalArgumentException("预算类型不正确");
        }
        List<BudgetExcuPo> excuPoList = new ArrayList<>();
        String createDate = TimeUtil.getCurrentDateString();
        String createTime = TimeUtil.getOnlyCurrentTimeString();

        for (Budget1Dto dto : budget1DtoList) {
            String remark1 = "";
            if(BudTypeEnum.FREZEE.compareTo(budTypeEnum) == 0){
                remark1 = "预算冻结-在途冻结";
            }else if(BudTypeEnum.USED.compareTo(budTypeEnum) == 0){
                remark1 = "预算使用-归档生效";
            }
            BudgetExcuPo excuPo = new BudgetExcuPo();
            excuPo.setRequestID(dto.getRequestID());
            excuPo.setDetailID(dto.getDetailID());
            excuPo.setProjID(dto.getProjID());
            excuPo.setProjNo("");
            excuPo.setProjDeptNo("");
            excuPo.setProjtype(ProjtypeEnum.PARENT);
            excuPo.setUseType(budTypeEnum);
            excuPo.setExpType(ExpTypeEnum.DECREASE);
//            excuPo.setSplitStatu();
            excuPo.setUseAmt(dto.getUseAmt());
            excuPo.setCreDate(createDate);
            excuPo.setCreTime(createTime);
            excuPo.setCreUser(dto.getCreUser());
            excuPo.setRemark(remark1);
            excuPo.setAutoCre(dto.getAutoCre());
            excuPoList.add(excuPo);
        }
        return excuPoList;
    }


    /**
     * 子项/孙子项目预算使用
     * @param budget2DtoList
     * @param budTypeEnum FREZEE 代表申请节点提交  USED/BORROW_REVERSAL 代表归档
     * @return
     */
    public List<BudgetExcuPo> getData4Proj23(List<? extends Budget23Dto> budget2DtoList,BudTypeEnum budTypeEnum){
        log.d("子项/孙子项预算使用参数",budget2DtoList,budTypeEnum);
        if(BudTypeEnum.FREZEE.compareTo(budTypeEnum) != 0
                && BudTypeEnum.USED.compareTo(budTypeEnum) != 0
                && BudTypeEnum.APPLY.compareTo(budTypeEnum) != 0
                && BudTypeEnum.ARCHIVE.compareTo(budTypeEnum) != 0
                && BudTypeEnum.PAY.compareTo(budTypeEnum) != 0
                && BudTypeEnum.BORROW_REVERSAL.compareTo(budTypeEnum) != 0){
            throw new IllegalArgumentException("预算类型不正确");
        }
        List<BudgetExcuPo> excuPoList = new ArrayList<>();
        String createDate = TimeUtil.getCurrentDateString();
        String createTime = TimeUtil.getOnlyCurrentTimeString();

        RecordSet rs = new RecordSet();
        for (Budget23Dto dto : budget2DtoList) {
            ProjtypeEnum projtype = dto.getProjtype();
            if(projtype == null){
                rs.executeQuery("select projtype from uf_proj where id = ? ",dto.getProjID());
                if(rs.next()){
                    dto.setProjtype(EnumsUtil.getProjtypeEnum(Util.getIntValue(rs.getString(1))));
                }
            }

            String remark1 = "";
            if(BudTypeEnum.FREZEE.compareTo(budTypeEnum) == 0){
                remark1 = "预算冻结-在途冻结";
            }else if(BudTypeEnum.USED.compareTo(budTypeEnum) == 0){
                remark1 = "预算使用-归档生效";
            }else if(BudTypeEnum.BORROW_REVERSAL.compareTo(budTypeEnum) == 0){
                remark1 = "预算冲销-归档生效";
            }else if(BudTypeEnum.APPLY.compareTo(budTypeEnum) == 0){
                remark1 = "预算冻结-合同申请";
            }else if(BudTypeEnum.ARCHIVE.compareTo(budTypeEnum) == 0){
                remark1 = "预算冻结-合同归档待付款";
            }else if(BudTypeEnum.PAY.compareTo(budTypeEnum) == 0){
                remark1 = "预算使用-合同归档直接扣减预算";
            }
            CtrlLevelEnum ctrlLevel = dto.getCtrlLevel();
            boolean isParentCtrl = CtrlLevelEnum.PARENT.compareTo(ctrlLevel) == 0;
            {
                BudgetExcuPo excuPo = new BudgetExcuPo();
                excuPo.setRequestID(dto.getRequestID());
                excuPo.setDetailID(dto.getDetailID());
                excuPo.setProjID(dto.getProjID());
                excuPo.setProjNo("");
                excuPo.setProjDeptNo("");
                excuPo.setProjtype(dto.getProjtype());
                excuPo.setUseType(budTypeEnum);
                if (BudTypeEnum.BORROW_REVERSAL.compareTo(budTypeEnum) == 0) {
                    excuPo.setExpType(ExpTypeEnum.INCREASE);//冲销预算增加
                    excuPo.setUseAmt(-dto.getUseAmt());//增加金额 预算为负
                } else {
                    excuPo.setExpType(ExpTypeEnum.DECREASE);
                    excuPo.setUseAmt(dto.getUseAmt());
                }
                if(isParentCtrl){
                    excuPo.setExpType(ExpTypeEnum.NOCALC);//父项控制 子项不计算
                }
                excuPo.setCreDate(createDate);
                excuPo.setCreTime(createTime);
                excuPo.setCreUser(dto.getCreUser());
                excuPo.setRemark(remark1);
                excuPoList.add(excuPo);
            }
            if(isParentCtrl){
                //如果是控制级别是父项的，需要添加一条父项使用
                BudgetExcuPo excuPo = new BudgetExcuPo();
                excuPo.setRequestID(dto.getRequestID());
                excuPo.setDetailID(dto.getDetailID());
                excuPo.setProjID(ProjUtil.getProj1ID4Child(dto.getProjID()));
                excuPo.setProjNo("");
                excuPo.setProjDeptNo("");
                excuPo.setProjtype(ProjtypeEnum.PARENT);
                excuPo.setUseType(budTypeEnum);
                if (BudTypeEnum.BORROW_REVERSAL.compareTo(budTypeEnum) == 0) {
                    excuPo.setExpType(ExpTypeEnum.INCREASE);//冲销预算增加
                    excuPo.setUseAmt(-dto.getUseAmt());//增加金额 预算为负
                } else {
                    excuPo.setExpType(ExpTypeEnum.DECREASE);
                    excuPo.setUseAmt(dto.getUseAmt());
                }
                excuPo.setCreDate(createDate);
                excuPo.setCreTime(createTime);
                excuPo.setCreUser(dto.getCreUser());
                excuPo.setRemark(remark1);
                excuPoList.add(excuPo);
            }
        }
        return excuPoList;
    }



    public BigDecimal add(double a, double b){
        BigDecimal aa = new BigDecimal(String.valueOf(a));
        BigDecimal bb = new BigDecimal(String.valueOf(b));
        return aa.add(bb);
    }

}
